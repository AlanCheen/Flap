package me.yifeiyuan.flap.plugin;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;


/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
class AutoRegister {


    public void registerFor(File flapFile, String className) {

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
                    Log.println("发现 Flap class,准备注入代码："+entryName);
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

    static class FlapClassVisitor extends ClassVisitor {

        FlapClassVisitor(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            Log.println("visitMethod:"+name);
            if (FlapTransform.FLAP_INJECT_METHOD_NAME.equals(name)) {
                mv = new InjectMethodVisitor(Opcodes.ASM5, mv);
            }

            return mv;
        }
    }

    static class InjectMethodVisitor extends MethodVisitor {

        public InjectMethodVisitor(int i, MethodVisitor methodVisitor) {
            super(i, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {

            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {

                mv.visitLdcInsn("me.yifeiyuan.flap.apt.proxies.SimpleTextComponentProxy");
                // generate invoke register method into Flap.injectFactories()
                mv.visitMethodInsn(Opcodes.INVOKESTATIC
                        , FlapTransform.FLAP_CLASS_FILE_NAME
                        , FlapTransform.FLAP_INJECT_METHOD_NAME
                        , "(Ljava/lang/String;)V"
                        , false);
            }

            super.visitInsn(opcode);
        }


        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals);
        }
    }
}
