package com.dawn.processor.router;

import com.dawn.annotation.router.Action;
import com.dawn.processor.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ModuleProcessor extends AbstractProcessor {
    public Elements mElementUtils;
    private Filer mFiler;
    private final String KEY_MODULE_NAME = "moduleName";
    private TypeMirror iRouterAction = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        iRouterAction = mElementUtils.getTypeElement(Consts.ROUTERACTION).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 1. 有没配置 modelName 防止 class 类冲突
        String moduleName = "";

        Map<String, String> options = processingEnv.getOptions();
        if(isNotEmpty(options)){
            moduleName = options.get(KEY_MODULE_NAME);
        }

        if(!Utils.isEmpty(moduleName)){
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
        }else{
            String errorMessage = "These no module name, at 'build.gradle', like :\n" +
                    "apt {\n" +
                    "    arguments {\n" +
                    "        moduleName project.getName();\n" +
                    "    }\n" +
                    "}\n";
            throw new RuntimeException("DRouter::Compiler >>> No module name, for more information, look at gradle log.\n" + errorMessage);
        }

        // 生成类继承和实现接口
        ClassName routerAssistClassName = ClassName.get("com.dawn.api.action", "IRouterModule");
        ClassName mapClassName = ClassName.get("java.util", "Map");
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("GSRouter$$Module$$" + moduleName)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addSuperinterface(routerAssistClassName)
                .addField(mapClassName, "actions", Modifier.PRIVATE);

        //构造函数
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
        constructorMethodBuilder.addStatement("actions = new $T<>()", ClassName.bestGuess("java.util.HashMap"));

        //解析到所有的action信息
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Action.class);
        Map<String,String> modules = new HashMap<>(elements.size());

        ClassName actionWrapperClassName = ClassName.get("com.drouter.api.extra", "ActionWrapper");
        ClassName threadModeClassName = ClassName.get("com.drouter.base", "ThreadMode");
        for(Element element : elements){
            //获取注解上面的action
            Action actionAnnotation = element.getAnnotation(Action.class);
            String actionName = actionAnnotation.path();

            //必须以配置的 gradle 的包名开头
            if(!actionName.startsWith(moduleName+"/")){
                error(element, "Path name of the action must begin with %s%s", moduleName, "/");
            }

            //获取action的classname
            Element enclosingElement = element.getEnclosingElement();
            String packageName = mElementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
            String actionClassName = packageName + "." + element.getSimpleName();

            //判断Interceptor 注解类是否实现了 ActionInterceptor
            if(!((TypeElement)element).getInterfaces().contains(iRouterAction)){
                error(element, "%s verify failed, @Action must be implements %s", element.getSimpleName().toString(), Consts.ROUTERACTION);
            }

            if(modules.containsKey(actionName)){
                //输出错误，Action名称冲突重复了
                error(element, "%s module name already exists", actionName);
            }

            //添加到集合中
            modules.put(actionName, actionClassName);

            constructorMethodBuilder.addStatement("this.actions.put($S,$T.build($T.class, $S, "
                            + actionAnnotation.extraProcess() + ", $T." + actionAnnotation.threadMode() + "))",
                    actionName, actionWrapperClassName, ClassName.bestGuess(actionClassName), actionName, threadModeClassName);
        }

        //实现方法
        MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("findAction")
                .addParameter(String.class,"actionName")
                .addAnnotation(Override.class)
                .returns(actionWrapperClassName)
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL);
        unbindMethodBuilder.addStatement("return (ActionWrapper)actions.get(actionName)");

        classBuilder.addMethod(constructorMethodBuilder.build());
        classBuilder.addMethod(unbindMethodBuilder.build());

        //生成类，看下效果
        // 生成类，看下效果
        try {
            JavaFile.builder(Consts.ROUTER_MODULE_PACK_NAME, classBuilder.build())
                    .addFileComment("DRouter 自动生成")
                    .build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("翻车了！");
        }

        return false;
    }

    /**
     * 给定处理的版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        // 需要解析的自定义注解 BindView  OnClick
        annotations.add(Action.class);
        return annotations;
    }

    private boolean isNotEmpty(Map<String, String> options){
        return options != null && !options.isEmpty();
    }

    private void error(Element element, String message, String... args){
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(kind, message, element);
    }

}
