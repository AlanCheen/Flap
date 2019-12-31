package me.yifeiyuan.flap.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
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
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import me.yifeiyuan.flap.annotations.Component;
import me.yifeiyuan.flap.annotations.ComponentFactoryManager;

@AutoService(Processor.class)
public class FlapProcessor extends AbstractProcessor {

    private static final String PKG_NAME_FACTORIES = "me.yifeiyuan.flap.apt.factories";
    private static final String PKG_NAME_MANAGER = "me.yifeiyuan.flap.apt.manager";

    private static final String FACTORY_NAME_SUFFIX = "Factory";

    private final ClassName CLASS_KEEP = ClassName.bestGuess("android.support.annotation.Keep");
    private final ClassName CLASS_FLAP = ClassName.bestGuess("me.yifeiyuan.flap.Flap");
    private final ClassName CLASS_FLAP_ITEM_FACTORY = ClassName.bestGuess("me.yifeiyuan.flap.internal.FlapItemFactory");

    private static final String KEY_OPTION_AUTO_REGISTER = "autoRegister";

    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;

    /**
     * 是否自动注册 Factories
     */
    private boolean autoRegisterFactories = true;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();               // Generate class.
        typeUtils = processingEnv.getTypeUtils();            // Get type utils.
        elementUtils = processingEnv.getElementUtils();      // Get class meta.
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "FlapProcessor init");

        Map<String, String> options = processingEnv.getOptions();
        if (options.containsKey(KEY_OPTION_AUTO_REGISTER)) {
            autoRegisterFactories = Boolean.parseBoolean(options.get(KEY_OPTION_AUTO_REGISTER));
        }
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {

        for (final TypeElement typeElement : set) {

            if (Component.class.getCanonicalName().equals(typeElement.getQualifiedName().toString())) {
                processComponentAnnotation(roundEnvironment, typeElement);
            } else if (ComponentFactoryManager.class.getCanonicalName().equals(typeElement.getQualifiedName().toString())) {
                processComponentFactoryManager(roundEnvironment, typeElement);
            }
        }

        return true;
    }

    private void processComponentAnnotation(final RoundEnvironment roundEnvironment, final TypeElement typeElement) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Component.class);

        for (final Element element : elements) {
            Component factory = element.getAnnotation(Component.class);
            if (null != factory) {
                try {
                    TypeSpec flapItemFactoryTypeSpec = createFlapItemTypeSpec(roundEnvironment, typeElement, (TypeElement) element, factory);
                    JavaFile.builder(PKG_NAME_FACTORIES, flapItemFactoryTypeSpec)
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
     * @param typeElement      @Flap
     * @param flapItemElement  被 FlapItemFactory 注解了的那个类
     * @param factory          注解了目标类的 注解，可以获取值
     *
     * @return FlapItemFactory TypeSpec
     */
    private TypeSpec createFlapItemTypeSpec(final RoundEnvironment roundEnvironment, final TypeElement typeElement, final TypeElement flapItemElement, final Component factory) {

        ClassName flapItemClass = (ClassName) ClassName.get(flapItemElement.asType());

        //要生成的类的名字
        String targetClassName = flapItemElement.getSimpleName().toString() + FACTORY_NAME_SUFFIX;

        int layoutId = factory.layoutId();
        boolean autoRegister = factory.autoRegister();

        DeclaredType declaredType = flapItemElement.getSuperclass().accept(new FlapItemModelVisitor(), null);
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        TypeElement itemModelType = (TypeElement) typeUtils.asElement(args.get(0));

        ClassName itemModelClass = ClassName.get(itemModelType);

        ClassName layoutInflater = ClassName.get("android.view", "LayoutInflater");
        ClassName viewGroup = ClassName.get("android.view", "ViewGroup");

        MethodSpec onCreateViewHolderMethod = MethodSpec.methodBuilder("onCreateViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(layoutInflater, "inflater")
                .addParameter(viewGroup, "parent")
                .addParameter(TypeName.INT, "layoutId")
                .returns(flapItemClass)
                .addStatement("return new $T(inflater.inflate(layoutId,parent,false))", flapItemClass)
                .build();

        MethodSpec getItemViewTypeMethod = MethodSpec.methodBuilder("getItemViewType")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(itemModelClass, "model")
                .returns(Integer.TYPE)
                .addStatement("return " + layoutId)
                .build();

        MethodSpec getItemModelClass = MethodSpec.methodBuilder("getItemModelClass")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(Class.class)
                .addStatement("return " + itemModelClass + ".class")
                .build();

        ParameterizedTypeName name = ParameterizedTypeName.get(CLASS_FLAP_ITEM_FACTORY, itemModelClass, flapItemClass);

        TypeSpec.Builder builder =
                TypeSpec.classBuilder(targetClassName)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(CLASS_KEEP)
                        .addMethod(onCreateViewHolderMethod)
                        .addMethod(getItemViewTypeMethod)
                        .addMethod(getItemModelClass)
                        .addSuperinterface(name);//实现接口

        if (autoRegister) {
            builder.addAnnotation(ComponentFactoryManager.class);
        }
        return builder.build();
    }

    /**
     * 处理 ComponentFactoryManager 注解
     * @param roundEnvironment
     * @param typeElement
     */
    private void processComponentFactoryManager(final RoundEnvironment roundEnvironment, final TypeElement typeElement) {

        if (!autoRegisterFactories) {
            return;
        }
        List<ClassName> factories = new ArrayList<>();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ComponentFactoryManager.class);

        for (final Element element : elements) {

            TypeElement flapItemFactory = (TypeElement) element;
            ClassName factoryClass = ClassName.get(flapItemFactory);
            factories.add(factoryClass);
        }

        TypeSpec manager = TypeSpec.classBuilder("FlapItemFactoryManager")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(CLASS_KEEP)
                .addMethod(createInjectMethod(factories))
                .build();

        try {
            JavaFile.builder(PKG_NAME_MANAGER, manager).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MethodSpec createInjectMethod(final List<ClassName> factories) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(void.class)
                .addParameter(CLASS_FLAP, "flap", Modifier.FINAL);

        for (final ClassName factory : factories) {
            builder.addStatement("flap.register(new $T())", factory);
        }

        return builder.build();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(Component.class.getCanonicalName());
        annotationTypes.add(ComponentFactoryManager.class.getCanonicalName());
        return annotationTypes;
    }

    public class FlapItemModelVisitor extends SimpleTypeVisitor8<DeclaredType, Void> {

        @Override
        public DeclaredType visitDeclared(DeclaredType declaredType, Void o) {
            return declaredType;
        }
    }
}
