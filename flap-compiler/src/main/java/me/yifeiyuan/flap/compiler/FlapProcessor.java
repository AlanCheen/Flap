package me.yifeiyuan.flap.compiler;

import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
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

import me.yifeiyuan.flap.annotations.Delegate;

@AutoService(Processor.class)
public class FlapProcessor extends AbstractProcessor {

    private static final String PKG_NAME_DELEGATES = "me.yifeiyuan.flap.apt.delegates";

    private static final String NAME_SUFFIX = "AdapterDelegate";

    private final ClassName CLASS_KEEP = ClassName.bestGuess("androidx.annotation.Keep");
    private final ClassName CLASS_FLAP = ClassName.bestGuess("me.yifeiyuan.flap.Flap");

    private final ClassName CLASS_OBJECT = ClassName.bestGuess("java.lang.Object");
    private final ClassName CLASS_COMPONENT = ClassName.bestGuess("me.yifeiyuan.flap.Component");
    private final ClassName CLASS_LIST = ClassName.bestGuess("java.util.List");
    private final ClassName CLASS_FLAP_ADAPTER = ClassName.bestGuess("me.yifeiyuan.flap.FlapAdapter");

    private final ClassName CLASS_ADAPTER_DELEGATE = ClassName.bestGuess("me.yifeiyuan.flap.delegate.AdapterDelegate");

    private final ClassName CLASS_LAYOUT_INFLATER = ClassName.get("android.view", "LayoutInflater");
    private final ClassName CLASS_VIEW_GROUP = ClassName.get("android.view", "ViewGroup");

    private static final String KEY_OPTION_R_CLASS_PATH = "packageName";

    private Filer filer;
    private Elements elements;
    private Types types;
    private Messager messager;

    private String rPackageName;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "FlapProcessor init");

        Map<String, String> options = processingEnv.getOptions();
        if (options.containsKey(KEY_OPTION_R_CLASS_PATH)) {
            rPackageName = options.get(KEY_OPTION_R_CLASS_PATH);
        }
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {

        for (final TypeElement typeElement : set) {
            if (Delegate.class.getCanonicalName().equals(typeElement.getQualifiedName().toString())) {
                processComponent(roundEnvironment, typeElement);
            }
        }

        return true;
    }

    private void processComponent(final RoundEnvironment roundEnvironment, final TypeElement typeElement) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Delegate.class);

        for (final Element element : elements) {
            Delegate delegate = element.getAnnotation(Delegate.class);
            if (null != delegate) {
                try {
                    TypeSpec flapAdapterDelegateTypeSpec = createAdapterDelegateTypeSpec(roundEnvironment, typeElement, (TypeElement) element, delegate);
                    JavaFile.builder(PKG_NAME_DELEGATES, flapAdapterDelegateTypeSpec)
                            .addFileComment("该文件由 Flap APT 自动生成，请勿修改。")
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 为 Component 生成 AdapterDelegate
     *
     * @param roundEnvironment 环境
     * @param typeElement
     * @param component        当前正在处理的组件
     * @param adapterDelegate  注解了目标类的 注解，可以获取值
     * @return ComponentProxy TypeSpec
     */
    private TypeSpec createAdapterDelegateTypeSpec(final RoundEnvironment roundEnvironment, final TypeElement typeElement, final TypeElement component, final Delegate adapterDelegate) {

        ClassName componentClass = (ClassName) ClassName.get(component.asType());

        //要生成的 AdapterDelegate 类的名字,加个后缀
        String targetClassName = component.getSimpleName().toString() + NAME_SUFFIX;

        int layoutId = adapterDelegate.layoutId();

        String layoutName = adapterDelegate.layoutName();

        boolean useDataBinding = adapterDelegate.useDataBinding();

        DeclaredType declaredType = component.getSuperclass().accept(new ModelVisitor(), null);
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        TypeElement itemModelType = (TypeElement) types.asElement(args.get(0));

        ClassName itemModelClass = ClassName.get(itemModelType);

        MethodSpec.Builder onCreateViewHolderMethodBuilder = MethodSpec.methodBuilder("onCreateViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CLASS_LAYOUT_INFLATER, "inflater")
                .addParameter(CLASS_VIEW_GROUP, "parent")
                .addParameter(TypeName.INT, "layoutId")
                .returns(componentClass);

        if (useDataBinding) {
            onCreateViewHolderMethodBuilder.addStatement("return new $T(androidx.databinding.DataBindingUtil.inflate(inflater,layoutId,parent,false))", componentClass);
        } else {

            boolean useViewBinding = adapterDelegate.useViewBinding();

            if (useViewBinding) {

                String bindingName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, layoutName);

                String fullBindingName = rPackageName + ".databinding." + bindingName + "Binding";

                onCreateViewHolderMethodBuilder
                        .addStatement(fullBindingName + " binding = " + fullBindingName + ".inflate(inflater,parent,false)")
                        .addStatement("return new $T(binding)", componentClass);
            } else {
                onCreateViewHolderMethodBuilder.addStatement("return new $T(inflater.inflate(layoutId,parent,false))", componentClass);
            }
        }

        MethodSpec onCreateViewHolderMethod = onCreateViewHolderMethodBuilder.build();

        MethodSpec.Builder getItemViewTypeMethodBuilder = MethodSpec.methodBuilder("getItemViewType")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
//                .addParameter(itemModelClass, "model")
                .addParameter(CLASS_OBJECT, "model")
                .returns(Integer.TYPE);

        if ("".equals(layoutName)) {
            getItemViewTypeMethodBuilder.addStatement("return " + layoutId);
        } else {
            getItemViewTypeMethodBuilder.addStatement("return " + rPackageName + ".R.layout." + layoutName);
        }

        MethodSpec.Builder getItemIdMethodBuilder = MethodSpec.methodBuilder("getItemId")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
//                .addParameter(itemModelClass, "model")
                .addParameter(CLASS_OBJECT, "model")
                .addStatement("return 0")
                .returns(Long.TYPE);

        MethodSpec getItemViewTypeMethod = getItemViewTypeMethodBuilder.build();

        MethodSpec.Builder delegateMethodBuilder = MethodSpec.methodBuilder("delegate")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CLASS_OBJECT, "model")
                .addStatement("return model.getClass() == $T.class", itemModelClass)
                .returns(Boolean.TYPE);

        //AdapterDelegate<List> payloads
//        ParameterizedTypeName payloads = ParameterizedTypeName.get(CLASS_ADAPTER_DELEGATE, CLASS_LIST);

        MethodSpec.Builder onBindViewHolderMethodBuilder = MethodSpec.methodBuilder("onBindViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CLASS_COMPONENT, "component")
                .addParameter(CLASS_OBJECT, "data")
                .addParameter(Integer
                        .TYPE, "position")
                .addParameter(CLASS_LIST, "payloads")
                .addParameter(CLASS_FLAP_ADAPTER, "adapter")
                .addStatement("component.bindData(data, position, payloads, adapter, this)");

//        MethodSpec getComponentModelClass = MethodSpec.methodBuilder("getComponentModelClass")
//                .addAnnotation(Override.class)
//                .addModifiers(Modifier.PUBLIC)
//                .returns(Class.class)
//                .addStatement("return " + itemModelClass + ".class")
//                .build();

        MethodSpec.Builder onViewAttachedToWindow = MethodSpec.methodBuilder("onViewAttachedToWindow")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CLASS_FLAP_ADAPTER, "adapter")
                .addParameter(CLASS_COMPONENT, "component")
                .addStatement("component.onViewAttachedToWindow(adapter)");

        MethodSpec.Builder onViewDetachedFromWindow = MethodSpec.methodBuilder("onViewDetachedFromWindow")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CLASS_FLAP_ADAPTER, "adapter")
                .addParameter(CLASS_COMPONENT, "component")
                .addStatement("component.onViewAttachedToWindow(adapter)");

        ParameterizedTypeName name = ParameterizedTypeName.get(CLASS_ADAPTER_DELEGATE, itemModelClass, componentClass);

        TypeSpec.Builder builder =
                TypeSpec.classBuilder(targetClassName)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(CLASS_KEEP)
                        .addMethod(onCreateViewHolderMethod)
                        .addMethod(getItemViewTypeMethod)
                        .addMethod(getItemIdMethodBuilder.build())
                        .addMethod(delegateMethodBuilder.build())
                        .addMethod(onBindViewHolderMethodBuilder.build())
//                        .addMethod(getComponentModelClass)
                        .addMethod(onViewAttachedToWindow.build())
                        .addMethod(onViewDetachedFromWindow.build())
                        .addSuperinterface(name);

        return builder.build();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(Delegate.class.getCanonicalName());
        return annotationTypes;
    }

    private static class ModelVisitor extends SimpleTypeVisitor8<DeclaredType, Void> {

        @Override
        public DeclaredType visitDeclared(DeclaredType declaredType, Void o) {
            return declaredType;
        }
    }
}
