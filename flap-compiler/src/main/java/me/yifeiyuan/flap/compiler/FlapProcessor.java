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

import me.yifeiyuan.flap.annotations.AutoRegister;
import me.yifeiyuan.flap.annotations.Proxy;

@AutoService(Processor.class)
public class FlapProcessor extends AbstractProcessor {

    private static final String PKG_NAME_PROXIES = "me.yifeiyuan.flap.apt.proxies";
    private static final String PKG_NAME_MANAGER = "me.yifeiyuan.flap.apt.manager";

    private static final String NAME_SUFFIX = "Proxy";

    private final ClassName CLASS_KEEP = ClassName.bestGuess("androidx.annotation.Keep");
    private final ClassName CLASS_FLAP = ClassName.bestGuess("me.yifeiyuan.flap.Flap");
    private final ClassName CLASS_COMPONENT_PROXY = ClassName.bestGuess("me.yifeiyuan.flap.internal.ComponentProxy");

//    private static final String KEY_OPTION_AUTO_REGISTER = "autoRegister";

    private Filer filer;
    private Elements elements;
    private Types types;
    private Messager messager;

    /**
     * 是否自动注册 Factories
     */
//    private boolean autoRegisterFactories = true;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();               // Generate class.
        types = processingEnv.getTypeUtils();            // Get type utils.
        elements = processingEnv.getElementUtils();      // Get class meta.
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "FlapProcessor init");

//        Map<String, String> options = processingEnv.getOptions();
//        if (options.containsKey(KEY_OPTION_AUTO_REGISTER)) {
//            autoRegisterFactories = Boolean.parseBoolean(options.get(KEY_OPTION_AUTO_REGISTER));
//        }
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {

        for (final TypeElement typeElement : set) {

            if (Proxy.class.getCanonicalName().equals(typeElement.getQualifiedName().toString())) {
                processComponent(roundEnvironment, typeElement);
            }
//            else if (AutoRegister.class.getCanonicalName().equals(typeElement.getQualifiedName().toString())) {
//                processComponentProxyManager(roundEnvironment, typeElement);
//            }
        }

        return true;
    }

    private void processComponent(final RoundEnvironment roundEnvironment, final TypeElement typeElement) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Proxy.class);

        for (final Element element : elements) {
            Proxy factory = element.getAnnotation(Proxy.class);
            if (null != factory) {
                try {
                    TypeSpec flapItemFactoryTypeSpec = createComponentProxyTypeSpec(roundEnvironment, typeElement, (TypeElement) element, factory);
                    JavaFile.builder(PKG_NAME_PROXIES, flapItemFactoryTypeSpec)
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 为 Component 生成 ComponentProxy
     *
     * @param roundEnvironment     环境
     * @param typeElement          @Component
     * @param flapComponentElement 被 FlapComponent 注解了的那个类
     * @param componentProxy       注解了目标类的 注解，可以获取值
     *
     * @return ComponentProxy TypeSpec
     */
    private TypeSpec createComponentProxyTypeSpec(final RoundEnvironment roundEnvironment, final TypeElement typeElement, final TypeElement flapComponentElement, final Proxy componentProxy) {

        ClassName flapItemClass = (ClassName) ClassName.get(flapComponentElement.asType());

        //要生成的类的名字
        String targetClassName = flapComponentElement.getSimpleName().toString() + NAME_SUFFIX;

        int layoutId = componentProxy.layoutId();
//        boolean autoRegister = componentProxy.autoRegister();

        boolean useDataBinding = componentProxy.useDataBinding();

        DeclaredType declaredType = flapComponentElement.getSuperclass().accept(new FlapItemModelVisitor(), null);
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        TypeElement itemModelType = (TypeElement) types.asElement(args.get(0));

        ClassName itemModelClass = ClassName.get(itemModelType);

        ClassName layoutInflater = ClassName.get("android.view", "LayoutInflater");
        ClassName viewGroup = ClassName.get("android.view", "ViewGroup");

        MethodSpec.Builder onCreateViewHolderMethodBuilder = MethodSpec.methodBuilder("createComponent")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(layoutInflater, "inflater")
                .addParameter(viewGroup, "parent")
                .addParameter(TypeName.INT, "layoutId")
                .returns(flapItemClass);

        if (useDataBinding) {
            onCreateViewHolderMethodBuilder.addStatement("return new $T(androidx.databinding.DataBindingUtil.inflate(inflater,layoutId,parent,false))",flapItemClass);
        } else {
            onCreateViewHolderMethodBuilder.addStatement("return new $T(inflater.inflate(layoutId,parent,false))", flapItemClass);
        }

        MethodSpec onCreateViewHolderMethod = onCreateViewHolderMethodBuilder.build();

        MethodSpec getItemViewTypeMethod = MethodSpec.methodBuilder("getItemViewType")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(itemModelClass, "model")
                .returns(Integer.TYPE)
                .addStatement("return " + layoutId)
                .build();

        MethodSpec getComponentModelClass = MethodSpec.methodBuilder("getComponentModelClass")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(Class.class)
                .addStatement("return " + itemModelClass + ".class")
                .build();

        ParameterizedTypeName name = ParameterizedTypeName.get(CLASS_COMPONENT_PROXY, itemModelClass, flapItemClass);

        TypeSpec.Builder builder =
                TypeSpec.classBuilder(targetClassName)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(CLASS_KEEP)
                        .addMethod(onCreateViewHolderMethod)
                        .addMethod(getItemViewTypeMethod)
                        .addMethod(getComponentModelClass)
                        .addSuperinterface(name);

//        if (autoRegister) {
//            builder.addAnnotation(AutoRegister.class);
//        }
        return builder.build();
    }

//    /**
//     * 处理 AutoRegister 注解，把需要自动注册的组件处理一下。
//     *
//     * @param roundEnvironment
//     * @param typeElement
//     */
//    private void processComponentProxyManager(final RoundEnvironment roundEnvironment, final TypeElement typeElement) {
//
//        if (!autoRegisterFactories) {
//            return;
//        }
//        List<ClassName> factories = new ArrayList<>();
//
//        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoRegister.class);
//
//        for (final Element element : elements) {
//            TypeElement flapItemFactory = (TypeElement) element;
//            ClassName factoryClass = ClassName.get(flapItemFactory);
//            factories.add(factoryClass);
//        }
//
//        TypeSpec manager = TypeSpec.classBuilder("ComponentAutoRegister")
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addAnnotation(CLASS_KEEP)
//                .addMethod(createInjectMethod(factories))
//                .build();
//
//        try {
//            JavaFile.builder(PKG_NAME_MANAGER, manager).build().writeTo(filer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    private MethodSpec createInjectMethod(final List<ClassName> factories) {
//
//        MethodSpec.Builder builder = MethodSpec.methodBuilder("inject")
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
//                .returns(void.class)
//                .addParameter(CLASS_FLAP, "flap", Modifier.FINAL);
//
//        for (final ClassName factory : factories) {
//            builder.addStatement("flap.register(new $T())", factory);
//        }
//
//        return builder.build();
//    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(Proxy.class.getCanonicalName());
        annotationTypes.add(AutoRegister.class.getCanonicalName());
        return annotationTypes;
    }

    private static class FlapItemModelVisitor extends SimpleTypeVisitor8<DeclaredType, Void> {

        @Override
        public DeclaredType visitDeclared(DeclaredType declaredType, Void o) {
            return declaredType;
        }
    }
}
