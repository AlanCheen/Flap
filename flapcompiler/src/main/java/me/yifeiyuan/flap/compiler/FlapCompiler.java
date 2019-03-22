package me.yifeiyuan.flap.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import me.yifeiyuan.flap.annotations.FlapItemFactory;


@AutoService(Processor.class)
//@SupportedAnnotationTypes("")
public class FlapCompiler extends AbstractProcessor {

    private Filer filer;       // File util, write class file into disk.
    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();               // Generate class.
        typeUtils = processingEnv.getTypeUtils();            // Get type utils.
        elementUtils = processingEnv.getElementUtils();      // Get class meta.
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "FlapProcessor init");
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {

        for (final TypeElement typeElement : set) {

            if (FlapItemFactory.class.getCanonicalName().equals(typeElement.getQualifiedName().toString())) {
                processFlapItemFactory(roundEnvironment, typeElement);
            }
        }

        return true;
    }

    private void processFlapItemFactory(final RoundEnvironment roundEnvironment, final TypeElement typeElement) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(FlapItemFactory.class);

        for (final Element element : elements) {
            FlapItemFactory factory = element.getAnnotation(FlapItemFactory.class);
            if (null != factory) {
                try {
                    TypeSpec flapItemFactoryTypeSpec = createFlapItemTypeSpec(roundEnvironment, typeElement, (TypeElement) element, factory);
                    if (flapItemFactoryTypeSpec == null) {
                        continue;
                    }
                    JavaFile.builder("me.yifeiyuan.flap.ann.factories", flapItemFactoryTypeSpec)
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param roundEnvironment 环境
     * @param typeElement
     * @param flapItemElement  被 FlapItemFactory 注解了的那个类
     * @param factory          注解了目标类的 注解，可以获取值
     *
     * @return
     */
    private TypeSpec createFlapItemTypeSpec(final RoundEnvironment roundEnvironment, final TypeElement typeElement, final TypeElement flapItemElement, final FlapItemFactory factory) {

        ClassName flapItemClass = (ClassName) ClassName.get(flapItemElement.asType());

        //要生成的类的名字
        String targetClassName = flapItemElement.getSimpleName().toString() + "Factory";

        ClassName modelklass = null;
        int itemViewType = factory.itemViewType();

        try {
            Class<?> modelClass = factory.modelClass();
        } catch (MirroredTypeException e) {
            TypeMirror typeMirror = e.getTypeMirror();
            TypeElement el = (TypeElement) typeUtils.asElement(typeMirror);
            modelklass = ClassName.get(el);
        }


        ClassName layoutInflater = ClassName.get("android.view", "LayoutInflater");
        ClassName viewGroup = ClassName.get("android.view", "ViewGroup");

        MethodSpec onCreateViewHolderMethod = MethodSpec.methodBuilder("onCreateViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(layoutInflater, "inflater")
                .addParameter(viewGroup, "parent")
                .addParameter(TypeName.INT, "itemViewType")
                .returns(flapItemClass)
                .addStatement("return new $T(inflater.inflate(itemViewType,parent,false))", flapItemClass)
                .build();

        MethodSpec getItemViewTypeMethod = MethodSpec.methodBuilder("getItemViewType")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(modelklass, "model")
                .returns(Integer.TYPE)
                .addStatement("return " + itemViewType)
                .build();

        ClassName flapItemFactoryInterface = ClassName.get("me.yifeiyuan.flap.internal", "FlapItemFactory");

        ParameterizedTypeName name = ParameterizedTypeName.get(flapItemFactoryInterface, modelklass, flapItemClass);

        TypeSpec flapItemFactoryTypeSpec = TypeSpec.classBuilder(targetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(onCreateViewHolderMethod)
                .addMethod(getItemViewTypeMethod)
                .addSuperinterface(name)//实现接口
                .build();

        return flapItemFactoryTypeSpec;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> anns = new LinkedHashSet<>();
        anns.add(FlapItemFactory.class.getCanonicalName());
        return anns;
    }
}
