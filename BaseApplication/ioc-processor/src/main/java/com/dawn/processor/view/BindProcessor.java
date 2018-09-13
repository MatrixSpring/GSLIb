package com.dawn.processor.view;

import com.dawn.annotation.view.GSBind;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

//不要写成Process.class
@AutoService(Processor.class)
////设置支持的注解类型（也可以通过重写方法实现）
//@SupportedAnnotationTypes({"com.chrissen.apt_annotation.BindView"})
//设置支持（也可以通过重写方法实现）
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class BindProcessor extends AbstractProcessor {
    //生成java文件的类（生成代理工具类）
    private Filer mFiler;
    private Messager messager;
    //工具类，用于获取Element信息
    private Elements elements;

    private static final String SUFFIX ="ViewBinder";


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    /**
     * Element 的子类有一下四种
     * VariableElement 一般代表成员变量
     * ExecutableElement 一般代表类中的方法
     * TypeElement 一般代表表类
     * PackageElement 一般代表package
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
       Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(GSBind.class);
        //收集信息，分类
        Map<String, List<VariableElement>> cacheMap = new HashMap<>();
        for(Element element : elementSet){
            VariableElement variableElement = (VariableElement) element;
            String className = getClassName(variableElement);

            List<VariableElement> filedList = cacheMap.get(className);
            if(null == filedList){
                filedList = new ArrayList<>();
                cacheMap.put(className, filedList);
            }
            filedList.add(variableElement);
        }

        //产生java文件
        Iterator<String> iterator = cacheMap.keySet().iterator();
        while (iterator.hasNext()){
            //准备好生成Java文件产生的信息
            //获取class的名字
            String className = iterator.next();
            //　获取class中的所有成员属性
            List<VariableElement> cacheElements = cacheMap.get(className);
            //获取包名
            String packageName = getPackageName(cacheElements.get(0));
            //获取最后生成文件的文件名：className+"$"+SUFFIX
            String bindViewClass = className+"$"+SUFFIX;
            //生成额外的文件,X写文件流
            Writer writer = null;
            try{
                JavaFileObject javaFileObject = mFiler.createSourceFile(bindViewClass);
                //拼接字符串
                writer = javaFileObject.openWriter();
                //获取简短新代理类名称
                String sampleClass = cacheElements.get(0).getEnclosingElement().getSimpleName().toString();
                String sampleBindViewClass = sampleClass+"$"+SUFFIX;
                writer.write(generateJavaCode(packageName,sampleBindViewClass,className, cacheElements));
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * 构建class框架的写法
     * @param mPackageName
     * @param mProxyClassName
     * @param className
     * @param cacheElements
     * @return
     */
    public String generateJavaCode(String mPackageName,String mProxyClassName,String className, List<VariableElement> cacheElements){
        StringBuilder builder = new StringBuilder();
        builder.append("package " + mPackageName).append(";\n\n");
        builder.append("import com.dawn.api.view.*;\n");
        builder.append("public class ");
        builder.append(mProxyClassName);
        builder.append(" implements " + SUFFIX + "<" + className + ">");
        builder.append("{\n\n");
        generateMethod(builder,className,cacheElements);
        builder.append("\n}\n");
        return builder.toString();
    }

    private void generateMethod(StringBuilder builder,String className, List<VariableElement> cacheElements){
        builder.append("\t");
        builder.append("public void bind("+className+" target){");
        builder.append("\n");
        //处理所有的成员属性
        for(VariableElement element :cacheElements){
            GSBind bindView = element.getAnnotation(GSBind.class);
            String filedName = element.getSimpleName().toString();
            TypeMirror typeMirror = element.asType();
            int id = bindView.value();
            builder.append("\t\t");
            builder.append("target.");
            builder.append(filedName );
            builder.append("= (");
            builder.append(typeMirror.toString());
            builder.append(")");
            builder.append("target.findViewById(");
            builder.append(""+id);
            builder.append(");");
            builder.append("\n");

        }
        builder.append("\t}\n");
    }

    private String getPackageName(VariableElement variableElement){
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        String packeName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        return packeName;
    }

    private String getClassName(VariableElement element) {
        String packageName =getPackageName(element);
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String className =typeElement.getSimpleName().toString();
        return packageName+"."+className;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(GSBind.class.getCanonicalName());
        return annotations;
//        return super.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
//        return super.getSupportedSourceVersion();
    }


}
