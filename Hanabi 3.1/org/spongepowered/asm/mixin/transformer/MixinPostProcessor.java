package org.spongepowered.asm.mixin.transformer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.FieldVisitor;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.transformers.MixinClassWriter;
import org.spongepowered.asm.transformers.TreeTransformer;
import org.spongepowered.asm.util.Bytecode;

class MixinPostProcessor extends TreeTransformer implements MixinConfig.IListener {
   private final Set syntheticInnerClasses = new HashSet();
   private final Map accessorMixins = new HashMap();
   private final Set loadable = new HashSet();

   public void onInit(MixinInfo mixin) {
      Iterator var2 = mixin.getSyntheticInnerClasses().iterator();

      while(var2.hasNext()) {
         String innerClass = (String)var2.next();
         this.registerSyntheticInner(innerClass.replace('/', '.'));
      }

   }

   public void onPrepare(MixinInfo mixin) {
      String className = mixin.getClassName();
      if (mixin.isLoadable()) {
         this.registerLoadable(className);
      }

      if (mixin.isAccessor()) {
         this.registerAccessor(mixin);
      }

   }

   void registerSyntheticInner(String className) {
      this.syntheticInnerClasses.add(className);
   }

   void registerLoadable(String className) {
      this.loadable.add(className);
   }

   void registerAccessor(MixinInfo mixin) {
      this.registerLoadable(mixin.getClassName());
      this.accessorMixins.put(mixin.getClassName(), mixin);
   }

   boolean canTransform(String className) {
      return this.syntheticInnerClasses.contains(className) || this.loadable.contains(className);
   }

   public String getName() {
      return this.getClass().getName();
   }

   public boolean isDelegationExcluded() {
      return true;
   }

   public byte[] transformClassBytes(String name, String transformedName, byte[] bytes) {
      if (this.syntheticInnerClasses.contains(transformedName)) {
         return this.processSyntheticInner(bytes);
      } else if (this.accessorMixins.containsKey(transformedName)) {
         MixinInfo mixin = (MixinInfo)this.accessorMixins.get(transformedName);
         return this.processAccessor(bytes, mixin);
      } else {
         return bytes;
      }
   }

   private byte[] processSyntheticInner(byte[] bytes) {
      ClassReader cr = new ClassReader(bytes);
      ClassWriter cw = new MixinClassWriter(cr, 0);
      ClassVisitor visibilityVisitor = new ClassVisitor(327680, cw) {
         public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access | 1, name, signature, superName, interfaces);
         }

         public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            if ((access & 6) == 0) {
               access |= 1;
            }

            return super.visitField(access, name, desc, signature, value);
         }

         public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ((access & 6) == 0) {
               access |= 1;
            }

            return super.visitMethod(access, name, desc, signature, exceptions);
         }
      };
      cr.accept(visibilityVisitor, 8);
      return cw.toByteArray();
   }

   private byte[] processAccessor(byte[] bytes, MixinInfo mixin) {
      if (!MixinEnvironment.getCompatibilityLevel().isAtLeast(MixinEnvironment.CompatibilityLevel.JAVA_8)) {
         return bytes;
      } else {
         boolean transformed = false;
         MixinInfo.MixinClassNode classNode = mixin.getClassNode(0);
         ClassInfo targetClass = (ClassInfo)mixin.getTargets().get(0);
         Iterator iter = classNode.mixinMethods.iterator();

         while(true) {
            MixinInfo.MixinMethodNode methodNode;
            AnnotationNode accessor;
            AnnotationNode invoker;
            do {
               do {
                  if (!iter.hasNext()) {
                     if (transformed) {
                        return this.writeClass(classNode);
                     }

                     return bytes;
                  }

                  methodNode = (MixinInfo.MixinMethodNode)iter.next();
               } while(!Bytecode.hasFlag((MethodNode)methodNode, 8));

               accessor = methodNode.getVisibleAnnotation(Accessor.class);
               invoker = methodNode.getVisibleAnnotation(Invoker.class);
            } while(accessor == null && invoker == null);

            ClassInfo.Method method = getAccessorMethod(mixin, methodNode, targetClass);
            createProxy(methodNode, targetClass, method);
            transformed = true;
         }
      }
   }

   private static ClassInfo.Method getAccessorMethod(MixinInfo mixin, MethodNode methodNode, ClassInfo targetClass) throws MixinTransformerError {
      ClassInfo.Method method = mixin.getClassInfo().findMethod((MethodNode)methodNode, 10);
      if (!method.isRenamed()) {
         throw new MixinTransformerError("Unexpected state: " + mixin + " loaded before " + targetClass + " was conformed");
      } else {
         return method;
      }
   }

   private static void createProxy(MethodNode methodNode, ClassInfo targetClass, ClassInfo.Method method) {
      methodNode.instructions.clear();
      Type[] args = Type.getArgumentTypes(methodNode.desc);
      Type returnType = Type.getReturnType(methodNode.desc);
      Bytecode.loadArgs(args, methodNode.instructions, 0);
      methodNode.instructions.add((AbstractInsnNode)(new MethodInsnNode(184, targetClass.getName(), method.getName(), methodNode.desc, false)));
      methodNode.instructions.add((AbstractInsnNode)(new InsnNode(returnType.getOpcode(172))));
      methodNode.maxStack = Bytecode.getFirstNonArgLocalIndex(args, false);
      methodNode.maxLocals = 0;
   }
}
