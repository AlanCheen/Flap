package me.yifeiyuan.flap.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
public class FlapTransform extends Transform {

    final String PROXY_PACKAGE_PATH_PREFIX = "me/yifeiyuan/flap/apt/proxies/";

    public static final String FLAP_CLASS_FILE_NAME = "me/yifeiyuan/flap/Flap.class";

    public static final String FLAP_INJECT_METHOD_NAME = "injectProxiesByPlugin";

    //me.yifeiyuan.flap.Flap 那个文件
    static File flapFile;

    private Project project;

    private List<String> proxyClassList = new ArrayList<>();

    public FlapTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "me.yifeiyuan.flap.FlapTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        Log.println("===================== flap transform start ===================== ");

        if (transformInvocation.isIncremental()) {
            Log.println("增量编译");
        } else {
            Log.println("全量编译");
        }

        if (!transformInvocation.isIncremental() && transformInvocation.getOutputProvider() != null) {
            transformInvocation.getOutputProvider().deleteAll();
        }

        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        List<TransformInput> inputs = (List<TransformInput>) transformInvocation.getInputs();

        for (TransformInput input : inputs) {
            //处理 jar 包中的 class 文件，找到 @Proxy 注解生成的文件
            handleJarInputs(outputProvider, input.getJarInputs());

            // 处理文件夹目录中的 class 文件
            handleDirectoryInputs(outputProvider,input.getDirectoryInputs());
        }

        if (flapFile != null) {
            new AutoRegister(proxyClassList).registerFor(flapFile);
        }

        Log.println("===================== flap transform end ===================== ");

    }

    private void handleDirectoryInputs(TransformOutputProvider outputProvider, Collection<DirectoryInput> directoryInputs) throws IOException {
        Log.println("handleDirectoryInputs");
        for (DirectoryInput directoryInput : directoryInputs) {

            File dest = outputProvider.getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);

            handleFiles(directoryInput.getFile());

            FileUtils.copyDirectory(directoryInput.getFile(), dest);
        }
    }

    private void handleFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                handleFiles(file1);
            }
        } else {
            if (file.getAbsolutePath().contains(PROXY_PACKAGE_PATH_PREFIX) && file.getName().endsWith("Proxy.class")) {
                Log.println(">>>>>>>>>>>> 发现 proxy class :" + file.getName());
                String fileName = file.getName();
                int index = fileName.indexOf(".");
                String className = PROXY_PACKAGE_PATH_PREFIX + fileName.substring(0, index);
                Log.println("className:"+className);
                proxyClassList.add(className);
            }
        }
    }

    private void handleJarInputs(TransformOutputProvider outputProvider, Collection<JarInput> jarInputs) throws IOException {
        Log.println("handleJarInputs start");
        for (JarInput jarInput : jarInputs) {

            String inputName = jarInput.getName();

            if (inputName.endsWith(".jar")) {
                inputName = inputName.substring(0, inputName.length() - 4);
            }

            String hex = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath());

            File srcFile = jarInput.getFile();

            String destFileName = inputName + "-" + hex;
            File destFile = outputProvider.getContentLocation(destFileName, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);

            if (shouldProcessPreDexJarFile(srcFile.getAbsolutePath())) {
                scanJarFile(srcFile, destFile);
            }

            FileUtils.copyFile(srcFile, destFile);
        }
        Log.println("handleJarInputs end");
    }

    //Users/xxx/.gradle/caches/transforms-1/files-1.1/cursoradapter-1.0.0.aar/6f2bc1b47d5cce5ab25d89f7c1b420fa/jars/classes.jar
    //shouldProcessPreDexJarFile /Users/mingjue/.gradle/caches/modules-2/files-2.1/org.jetbrains/annotations/13.0/919f0dfe192fb4e063e7dacadee7f8bb9a2672a9/annotations-13.0.jar
    private boolean shouldProcessPreDexJarFile(String path) {
        return !path.contains("org.jetbrains") &&
                !path.contains("androidx.constraintlayout") &&
                !path.contains("lifecycle-livedata") &&
                !path.contains("databinding-") &&
                !path.contains("vectordrawable-") &&
                !path.contains("/android/m2repository");
    }

    private void scanJarFile(File src, File dest) throws IOException {
        if (null != src) {

            JarFile jarFile = new JarFile(src);

            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();

                if (FLAP_CLASS_FILE_NAME.equals(entryName)) {//Flap/flap/build/intermediates/intermediate-jars/debug/classes.jar
                    Log.println(entryName);
                    Log.println(">>>>>>>>>>>> 发现 Flap class file <<<<<<<<<<<<");
                    flapFile = dest;
                } else if (entryName.startsWith(PROXY_PACKAGE_PATH_PREFIX)) {
                    Log.println(">>>>>>>>> 发现 proxy class :" + entryName);
                    int index = entryName.indexOf(".");
                    String className = entryName.substring(0, index);
                    Log.println("className:"+className);
                    proxyClassList.add(className);
                }
            }

            jarFile.close();
        }

    }

    private boolean shouldProcessClass(String entryName) {
        return entryName != null && entryName.startsWith(PROXY_PACKAGE_PATH_PREFIX);
    }
}
