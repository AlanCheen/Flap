package me.yifeiyuan.flap.plugin;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.POP;


/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
class AutoRegister {

    //需要被注入的 Proxy 的类名
    private List<String> classNames;

    public AutoRegister(List<String> classNames) {
        this.classNames = classNames;
    }

    /**
     * todo 可能 className 直接搞个列表一起处理就行了
     *
     * @param flapFile Flap 这个类所在的 jar 包文件
     */
    public void registerFor(File flapFile) {

        Log.println("registerFor");

        File optJar = new File(flapFile.getParent(), flapFile.getName() + ".opt");
        if (optJar.exists()) {
            optJar.delete();
        }

        try {
            JarFile jarFile = new JarFile(flapFile);

            Enumeration<JarEntry> entries = jarFile.entries();

            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar));

            while (entries.hasMoreElements()) {

                JarEntry jarEntry = entries.nextElement();

                String entryName = jarEntry.getName();

                ZipEntry zipEntry = new ZipEntry(entryName);

                InputStream inputStream = jarFile.getInputStream(jarEntry);

                jarOutputStream.putNextEntry(zipEntry);

                if (FlapTransform.FLAP_CLASS_FILE_NAME.equals(entryName)) {
                    Log.println("发现 Flap class,准备注入代码：" + entryName);
                    byte[] bytes = visitFlap(inputStream);
                    jarOutputStream.write(bytes);
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream));
                }

                inputStream.close();
                jarOutputStream.closeEntry();
            }

            jarOutputStream.close();
            jarFile.close();

            if (flapFile.exists()) {
                flapFile.delete();
            }
            optJar.renameTo(flapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] visitFlap(InputStream inputStream) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new FlapClassVisitor(Opcodes.ASM5, cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    class FlapClassVisitor extends ClassVisitor {

        FlapClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            if (FlapTransform.FLAP_INJECT_METHOD_NAME.equals(name)) {
                mv = new InjectMethodVisitor(Opcodes.ASM5, mv);
                Log.println("visitMethod 找到注入方法 准备注入:" + name);
            }

            return mv;
        }
    }

    class InjectMethodVisitor extends MethodVisitor {

        public InjectMethodVisitor(int i, MethodVisitor methodVisitor) {
            super(i, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {

            for (String className : classNames) {

                Log.println(">>>>>>>>> 正在注入：" + className);

                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitLdcInsn("flap");
                mv.visitMethodInsn(INVOKESPECIAL, "kotlin/jvm/internal/Intrinsics", "checkNotNullParameter", "(Ljava/lang/Object;Ljava/lang/String;)V", false);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(NEW, className);
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V", false);
                mv.visitTypeInsn(CHECKCAST, "me/yifeiyuan/flap/AdapterDelegate");
                mv.visitMethodInsn(INVOKEVIRTUAL, "me/yifeiyuan/flap/Flap", "registerAdapterDelegate", "(Lme/yifeiyuan/flap/AdapterDelegate;)V", false);
                mv.visitInsn(POP);
            }

            super.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals);
        }
    }
}
