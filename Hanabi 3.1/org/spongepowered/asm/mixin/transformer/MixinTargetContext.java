package org.spongepowered.asm.mixin.transformer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.injection.throwables.InjectionValidationException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.mixin.struct.MemberRef;
import org.spongepowered.asm.mixin.struct.SourceMap;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.obfuscation.RemapperChain;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.ClassSignature;

public class MixinTargetContext extends ClassContext implements IMixinContext {
   private static final Logger logger = LogManager.getLogger("mixin");
   private final MixinInfo mixin;
   private final ClassNode classNode;
   private final TargetClassContext targetClass;
   private final String sessionId;
   private final ClassInfo targetClassInfo;
   private final BiMap innerClasses = HashBiMap.create();
   private final List shadowMethods = new ArrayList();
   private final Map shadowFields = new LinkedHashMap();
   private final List mergedMethods = new ArrayList();
   private final InjectorGroupInfo.Map injectorGroups = new InjectorGroupInfo.Map();
   private final List injectors = new ArrayList();
   private final List accessors = new ArrayList();
   private final boolean inheritsFromMixin;
   private final boolean detachedSuper;
   private final SourceMap.File stratum;
   private int minRequiredClassVersion;

   MixinTargetContext(MixinInfo mixin, ClassNode classNode, TargetClassContext context) {
      this.minRequiredClassVersion = MixinEnvironment.CompatibilityLevel.JAVA_6.classVersion();
      this.mixin = mixin;
      this.classNode = classNode;
      this.targetClass = context;
      this.targetClassInfo = ClassInfo.forName(this.getTarget().getClassRef());
      this.stratum = context.getSourceMap().addFile(this.classNode);
      this.inheritsFromMixin = mixin.getClassInfo().hasMixinInHierarchy() || this.targetClassInfo.hasMixinTargetInHierarchy();
      this.detachedSuper = !this.classNode.superName.equals(this.getTarget().getClassNode().superName);
      this.sessionId = context.getSessionId();
      this.requireVersion(classNode.version);
      InnerClassGenerator icg = (InnerClassGenerator)context.getExtensions().getGenerator(InnerClassGenerator.class);
      Iterator var5 = this.mixin.getInnerClasses().iterator();

      while(var5.hasNext()) {
         String innerClass = (String)var5.next();
         this.innerClasses.put(innerClass, icg.registerInnerClass(this.mixin, innerClass, this));
      }

   }

   void addShadowMethod(MethodNode method) {
      this.shadowMethods.add(method);
   }

   void addShadowField(FieldNode fieldNode, ClassInfo.Field fieldInfo) {
      this.shadowFields.put(fieldNode, fieldInfo);
   }

   void addAccessorMethod(MethodNode method, Class type) {
      this.accessors.add(AccessorInfo.of(this, method, type));
   }

   void addMixinMethod(MethodNode method) {
      Annotations.setVisible(method, MixinMerged.class, "mixin", this.getClassName());
      this.getTarget().addMixinMethod(method);
   }

   void methodMerged(MethodNode method) {
      this.mergedMethods.add(method);
      this.targetClassInfo.addMethod(method);
      this.getTarget().methodMerged(method);
      Annotations.setVisible(method, MixinMerged.class, "mixin", this.getClassName(), "priority", this.getPriority(), "sessionId", this.sessionId);
   }

   public String toString() {
      return this.mixin.toString();
   }

   public MixinEnvironment getEnvironment() {
      return this.mixin.getParent().getEnvironment();
   }

   public boolean getOption(MixinEnvironment.Option option) {
      return this.getEnvironment().getOption(option);
   }

   public ClassNode getClassNode() {
      return this.classNode;
   }

   public String getClassName() {
      return this.mixin.getClassName();
   }

   public String getClassRef() {
      return this.mixin.getClassRef();
   }

   public TargetClassContext getTarget() {
      return this.targetClass;
   }

   public String getTargetClassRef() {
      return this.getTarget().getClassRef();
   }

   public ClassNode getTargetClassNode() {
      return this.getTarget().getClassNode();
   }

   public ClassInfo getTargetClassInfo() {
      return this.targetClassInfo;
   }

   protected ClassInfo getClassInfo() {
      return this.mixin.getClassInfo();
   }

   public ClassSignature getSignature() {
      return this.getClassInfo().getSignature();
   }

   public SourceMap.File getStratum() {
      return this.stratum;
   }

   public int getMinRequiredClassVersion() {
      return this.minRequiredClassVersion;
   }

   public int getDefaultRequiredInjections() {
      return this.mixin.getParent().getDefaultRequiredInjections();
   }

   public String getDefaultInjectorGroup() {
      return this.mixin.getParent().getDefaultInjectorGroup();
   }

   public int getMaxShiftByValue() {
      return this.mixin.getParent().getMaxShiftByValue();
   }

   public InjectorGroupInfo.Map getInjectorGroups() {
      return this.injectorGroups;
   }

   public boolean requireOverwriteAnnotations() {
      return this.mixin.getParent().requireOverwriteAnnotations();
   }

   public ClassInfo findRealType(ClassInfo mixin) {
      if (mixin == this.getClassInfo()) {
         return this.targetClassInfo;
      } else {
         ClassInfo type = this.targetClassInfo.findCorrespondingType(mixin);
         if (type == null) {
            throw new InvalidMixinException(this, "Resolution error: unable to find corresponding type for " + mixin + " in hierarchy of " + this.targetClassInfo);
         } else {
            return type;
         }
      }
   }

   public void transformMethod(MethodNode method) {
      this.validateMethod(method);
      this.transformDescriptor(method);
      this.transformLVT(method);
      this.stratum.applyOffset(method);
      AbstractInsnNode lastInsn = null;

      AbstractInsnNode insn;
      for(Iterator iter = method.instructions.iterator(); iter.hasNext(); lastInsn = insn) {
         insn = (AbstractInsnNode)iter.next();
         if (insn instanceof MethodInsnNode) {
            this.transformMethodRef(method, iter, new MemberRef.Method((MethodInsnNode)insn));
         } else if (insn instanceof FieldInsnNode) {
            this.transformFieldRef(method, iter, new MemberRef.Field((FieldInsnNode)insn));
            this.checkFinal(method, iter, (FieldInsnNode)insn);
         } else if (insn instanceof TypeInsnNode) {
            this.transformTypeNode(method, iter, (TypeInsnNode)insn, lastInsn);
         } else if (insn instanceof LdcInsnNode) {
            this.transformConstantNode(method, iter, (LdcInsnNode)insn);
         } else if (insn instanceof InvokeDynamicInsnNode) {
            this.transformInvokeDynamicNode(method, iter, (InvokeDynamicInsnNode)insn);
         }
      }

   }

   private void validateMethod(MethodNode method) {
      if (Annotations.getInvisible(method, SoftOverride.class) != null) {
         ClassInfo.Method superMethod = this.targetClassInfo.findMethodInHierarchy(method.name, method.desc, ClassInfo.SearchType.SUPER_CLASSES_ONLY, ClassInfo.Traversal.SUPER);
         if (superMethod == null || !superMethod.isInjected()) {
            throw new InvalidMixinException(this, "Mixin method " + method.name + method.desc + " is tagged with @SoftOverride but no valid method was found in superclasses of " + this.getTarget().getClassName());
         }
      }

   }

   private void transformLVT(MethodNode method) {
      if (method.localVariables != null) {
         Iterator var2 = method.localVariables.iterator();

         while(var2.hasNext()) {
            LocalVariableNode local = (LocalVariableNode)var2.next();
            if (local != null && local.desc != null) {
               local.desc = this.transformSingleDescriptor(Type.getType(local.desc));
            }
         }

      }
   }

   private void transformMethodRef(MethodNode method, Iterator iter, MemberRef methodRef) {
      this.transformDescriptor(methodRef);
      if (methodRef.getOwner().equals(this.getClassRef())) {
         methodRef.setOwner(this.getTarget().getClassRef());
         ClassInfo.Method md = this.getClassInfo().findMethod(methodRef.getName(), methodRef.getDesc(), 10);
         if (md != null && md.isRenamed() && md.getOriginalName().equals(methodRef.getName()) && md.isSynthetic()) {
            methodRef.setName(md.getName());
         }

         this.upgradeMethodRef(method, methodRef, md);
      } else if (this.innerClasses.containsKey(methodRef.getOwner())) {
         methodRef.setOwner((String)this.innerClasses.get(methodRef.getOwner()));
         methodRef.setDesc(this.transformMethodDescriptor(methodRef.getDesc()));
      } else if (this.detachedSuper || this.inheritsFromMixin) {
         if (methodRef.getOpcode() == 183) {
            this.updateStaticBinding(method, methodRef);
         } else if (methodRef.getOpcode() == 182 && ClassInfo.forName(methodRef.getOwner()).isMixin()) {
            this.updateDynamicBinding(method, methodRef);
         }
      }

   }

   private void transformFieldRef(MethodNode method, Iterator iter, MemberRef fieldRef) {
      if ("super$".equals(fieldRef.getName())) {
         if (!(fieldRef instanceof MemberRef.Field)) {
            throw new InvalidMixinException(this.mixin, "Cannot call imaginary super from method handle.");
         }

         this.processImaginarySuper(method, ((MemberRef.Field)fieldRef).insn);
         iter.remove();
      }

      this.transformDescriptor(fieldRef);
      if (fieldRef.getOwner().equals(this.getClassRef())) {
         fieldRef.setOwner(this.getTarget().getClassRef());
         ClassInfo.Field field = this.getClassInfo().findField(fieldRef.getName(), fieldRef.getDesc(), 10);
         if (field != null && field.isRenamed() && field.getOriginalName().equals(fieldRef.getName()) && field.isStatic()) {
            fieldRef.setName(field.getName());
         }
      } else {
         ClassInfo fieldOwner = ClassInfo.forName(fieldRef.getOwner());
         if (fieldOwner.isMixin()) {
            ClassInfo actualOwner = this.targetClassInfo.findCorrespondingType(fieldOwner);
            fieldRef.setOwner(actualOwner != null ? actualOwner.getName() : this.getTarget().getClassRef());
         }
      }

   }

   private void checkFinal(MethodNode method, Iterator iter, FieldInsnNode fieldNode) {
      if (fieldNode.owner.equals(this.getTarget().getClassRef())) {
         int opcode = fieldNode.getOpcode();
         if (opcode != 180 && opcode != 178) {
            Iterator var5 = this.shadowFields.entrySet().iterator();

            Map.Entry shadow;
            FieldNode shadowFieldNode;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               shadow = (Map.Entry)var5.next();
               shadowFieldNode = (FieldNode)shadow.getKey();
            } while(!shadowFieldNode.desc.equals(fieldNode.desc) || !shadowFieldNode.name.equals(fieldNode.name));

            ClassInfo.Field shadowField = (ClassInfo.Field)shadow.getValue();
            if (shadowField.isDecoratedFinal()) {
               if (shadowField.isDecoratedMutable()) {
                  if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                     logger.warn("Write access to @Mutable @Final field {} in {}::{}", new Object[]{shadowField, this.mixin, method.name});
                  }
               } else if (!"<init>".equals(method.name) && !"<clinit>".equals(method.name)) {
                  logger.error("Write access detected to @Final field {} in {}::{}", new Object[]{shadowField, this.mixin, method.name});
                  if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
                     throw new InvalidMixinException(this.mixin, "Write access detected to @Final field " + shadowField + " in " + this.mixin + "::" + method.name);
                  }
               } else {
                  logger.warn("@Final field {} in {} should be final", new Object[]{shadowField, this.mixin});
               }
            }

         }
      }
   }

   private void transformTypeNode(MethodNode method, Iterator iter, TypeInsnNode typeInsn, AbstractInsnNode lastNode) {
      if (typeInsn.getOpcode() == 192 && typeInsn.desc.equals(this.getTarget().getClassRef()) && lastNode.getOpcode() == 25 && ((VarInsnNode)lastNode).var == 0) {
         iter.remove();
      } else {
         if (typeInsn.desc.equals(this.getClassRef())) {
            typeInsn.desc = this.getTarget().getClassRef();
         } else {
            String newName = (String)this.innerClasses.get(typeInsn.desc);
            if (newName != null) {
               typeInsn.desc = newName;
            }
         }

         this.transformDescriptor(typeInsn);
      }
   }

   private void transformConstantNode(MethodNode method, Iterator iter, LdcInsnNode ldcInsn) {
      ldcInsn.cst = this.transformConstant(method, iter, ldcInsn.cst);
   }

   private void transformInvokeDynamicNode(MethodNode method, Iterator iter, InvokeDynamicInsnNode dynInsn) {
      this.requireVersion(51);
      dynInsn.desc = this.transformMethodDescriptor(dynInsn.desc);
      dynInsn.bsm = this.transformHandle(method, iter, dynInsn.bsm);

      for(int i = 0; i < dynInsn.bsmArgs.length; ++i) {
         dynInsn.bsmArgs[i] = this.transformConstant(method, iter, dynInsn.bsmArgs[i]);
      }

   }

   private Object transformConstant(MethodNode method, Iterator iter, Object constant) {
      if (constant instanceof Type) {
         Type type = (Type)constant;
         String desc = this.transformDescriptor(type);
         return !type.toString().equals(desc) ? Type.getType(desc) : constant;
      } else {
         return constant instanceof Handle ? this.transformHandle(method, iter, (Handle)constant) : constant;
      }
   }

   private Handle transformHandle(MethodNode method, Iterator iter, Handle handle) {
      MemberRef.Handle memberRef = new MemberRef.Handle(handle);
      if (memberRef.isField()) {
         this.transformFieldRef(method, iter, memberRef);
      } else {
         this.transformMethodRef(method, iter, memberRef);
      }

      return memberRef.getMethodHandle();
   }

   private void processImaginarySuper(MethodNode method, FieldInsnNode fieldInsn) {
      if (fieldInsn.getOpcode() != 180) {
         if ("<init>".equals(method.name)) {
            throw new InvalidMixinException(this, "Illegal imaginary super declaration: field " + fieldInsn.name + " must not specify an initialiser");
         } else {
            throw new InvalidMixinException(this, "Illegal imaginary super access: found " + Bytecode.getOpcodeName(fieldInsn.getOpcode()) + " opcode in " + method.name + method.desc);
         }
      } else if ((method.access & 2) == 0 && (method.access & 8) == 0) {
         if (Annotations.getInvisible(method, SoftOverride.class) == null) {
            throw new InvalidMixinException(this, "Illegal imaginary super access: method " + method.name + method.desc + " is not decorated with @SoftOverride");
         } else {
            Iterator methodIter = method.instructions.iterator(method.instructions.indexOf(fieldInsn));

            while(methodIter.hasNext()) {
               AbstractInsnNode insn = (AbstractInsnNode)methodIter.next();
               if (insn instanceof MethodInsnNode) {
                  MethodInsnNode methodNode = (MethodInsnNode)insn;
                  if (methodNode.owner.equals(this.getClassRef()) && methodNode.name.equals(method.name) && methodNode.desc.equals(method.desc)) {
                     methodNode.setOpcode(183);
                     this.updateStaticBinding(method, new MemberRef.Method(methodNode));
                     return;
                  }
               }
            }

            throw new InvalidMixinException(this, "Illegal imaginary super access: could not find INVOKE for " + method.name + method.desc);
         }
      } else {
         throw new InvalidMixinException(this, "Illegal imaginary super access: method " + method.name + method.desc + " is private or static");
      }
   }

   private void updateStaticBinding(MethodNode method, MemberRef methodRef) {
      this.updateBinding(method, methodRef, ClassInfo.Traversal.SUPER);
   }

   private void updateDynamicBinding(MethodNode method, MemberRef methodRef) {
      this.updateBinding(method, methodRef, ClassInfo.Traversal.ALL);
   }

   private void updateBinding(MethodNode method, MemberRef methodRef, ClassInfo.Traversal traversal) {
      if (!"<init>".equals(method.name) && !methodRef.getOwner().equals(this.getTarget().getClassRef()) && !this.getTarget().getClassRef().startsWith("<")) {
         ClassInfo.Method superMethod = this.targetClassInfo.findMethodInHierarchy(methodRef.getName(), methodRef.getDesc(), traversal.getSearchType(), traversal);
         if (superMethod != null) {
            if (superMethod.getOwner().isMixin()) {
               throw new InvalidMixinException(this, "Invalid " + methodRef + " in " + this + " resolved " + superMethod.getOwner() + " but is mixin.");
            }

            methodRef.setOwner(superMethod.getImplementor().getName());
         } else if (ClassInfo.forName(methodRef.getOwner()).isMixin()) {
            throw new MixinTransformerError("Error resolving " + methodRef + " in " + this);
         }

      }
   }

   public void transformDescriptor(FieldNode field) {
      if (this.inheritsFromMixin || this.innerClasses.size() != 0) {
         field.desc = this.transformSingleDescriptor(field.desc, false);
      }
   }

   public void transformDescriptor(MethodNode method) {
      if (this.inheritsFromMixin || this.innerClasses.size() != 0) {
         method.desc = this.transformMethodDescriptor(method.desc);
      }
   }

   public void transformDescriptor(MemberRef member) {
      if (this.inheritsFromMixin || this.innerClasses.size() != 0) {
         if (member.isField()) {
            member.setDesc(this.transformSingleDescriptor(member.getDesc(), false));
         } else {
            member.setDesc(this.transformMethodDescriptor(member.getDesc()));
         }

      }
   }

   public void transformDescriptor(TypeInsnNode typeInsn) {
      if (this.inheritsFromMixin || this.innerClasses.size() != 0) {
         typeInsn.desc = this.transformSingleDescriptor(typeInsn.desc, true);
      }
   }

   private String transformDescriptor(Type type) {
      return type.getSort() == 11 ? this.transformMethodDescriptor(type.getDescriptor()) : this.transformSingleDescriptor(type);
   }

   private String transformSingleDescriptor(Type type) {
      return type.getSort() < 9 ? type.toString() : this.transformSingleDescriptor(type.toString(), false);
   }

   private String transformSingleDescriptor(String desc, boolean isObject) {
      String type = desc;

      while(type.startsWith("[") || type.startsWith("L")) {
         if (type.startsWith("[")) {
            type = type.substring(1);
         } else {
            type = type.substring(1, type.indexOf(";"));
            isObject = true;
         }
      }

      if (!isObject) {
         return desc;
      } else {
         String innerClassName = (String)this.innerClasses.get(type);
         if (innerClassName != null) {
            return desc.replace(type, innerClassName);
         } else if (this.innerClasses.inverse().containsKey(type)) {
            return desc;
         } else {
            ClassInfo typeInfo = ClassInfo.forName(type);
            if (!typeInfo.isMixin()) {
               return desc;
            } else {
               return desc.replace(type, this.findRealType(typeInfo).toString());
            }
         }
      }
   }

   private String transformMethodDescriptor(String desc) {
      StringBuilder newDesc = new StringBuilder();
      newDesc.append('(');
      Type[] var3 = Type.getArgumentTypes(desc);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Type arg = var3[var5];
         newDesc.append(this.transformSingleDescriptor(arg));
      }

      return newDesc.append(')').append(this.transformSingleDescriptor(Type.getReturnType(desc))).toString();
   }

   public Target getTargetMethod(MethodNode method) {
      return this.getTarget().getTargetMethod(method);
   }

   MethodNode findMethod(MethodNode method, AnnotationNode annotation) {
      Deque aliases = new LinkedList();
      aliases.add(method.name);
      if (annotation != null) {
         List aka = (List)Annotations.getValue(annotation, "aliases");
         if (aka != null) {
            aliases.addAll(aka);
         }
      }

      return this.getTarget().findMethod(aliases, method.desc);
   }

   MethodNode findRemappedMethod(MethodNode method) {
      RemapperChain remapperChain = this.getEnvironment().getRemappers();
      String remappedName = remapperChain.mapMethodName(this.getTarget().getClassRef(), method.name, method.desc);
      if (remappedName.equals(method.name)) {
         return null;
      } else {
         Deque aliases = new LinkedList();
         aliases.add(remappedName);
         return this.getTarget().findAliasedMethod(aliases, method.desc);
      }
   }

   FieldNode findField(FieldNode field, AnnotationNode shadow) {
      Deque aliases = new LinkedList();
      aliases.add(field.name);
      if (shadow != null) {
         List aka = (List)Annotations.getValue(shadow, "aliases");
         if (aka != null) {
            aliases.addAll(aka);
         }
      }

      return this.getTarget().findAliasedField(aliases, field.desc);
   }

   FieldNode findRemappedField(FieldNode field) {
      RemapperChain remapperChain = this.getEnvironment().getRemappers();
      String remappedName = remapperChain.mapFieldName(this.getTarget().getClassRef(), field.name, field.desc);
      if (remappedName.equals(field.name)) {
         return null;
      } else {
         Deque aliases = new LinkedList();
         aliases.add(remappedName);
         return this.getTarget().findAliasedField(aliases, field.desc);
      }
   }

   protected void requireVersion(int version) {
      this.minRequiredClassVersion = Math.max(this.minRequiredClassVersion, version);
      if (version > MixinEnvironment.getCompatibilityLevel().classVersion()) {
         throw new InvalidMixinException(this, "Unsupported mixin class version " + version);
      }
   }

   public Extensions getExtensions() {
      return this.targetClass.getExtensions();
   }

   public IMixinInfo getMixin() {
      return this.mixin;
   }

   MixinInfo getInfo() {
      return this.mixin;
   }

   public int getPriority() {
      return this.mixin.getPriority();
   }

   public Set getInterfaces() {
      return this.mixin.getInterfaces();
   }

   public Collection getShadowMethods() {
      return this.shadowMethods;
   }

   public List getMethods() {
      return this.classNode.methods;
   }

   public Set getShadowFields() {
      return this.shadowFields.entrySet();
   }

   public List getFields() {
      return this.classNode.fields;
   }

   public Level getLoggingLevel() {
      return this.mixin.getLoggingLevel();
   }

   public boolean shouldSetSourceFile() {
      return this.mixin.getParent().shouldSetSourceFile();
   }

   public String getSourceFile() {
      return this.classNode.sourceFile;
   }

   public IReferenceMapper getReferenceMapper() {
      return this.mixin.getParent().getReferenceMapper();
   }

   public void preApply(String transformedName, ClassNode targetClass) {
      this.mixin.preApply(transformedName, targetClass);
   }

   public void postApply(String transformedName, ClassNode targetClass) {
      try {
         this.injectorGroups.validateAll();
      } catch (InjectionValidationException var5) {
         InjectorGroupInfo group = var5.getGroup();
         throw new InjectionError(String.format("Critical injection failure: Callback group %s in %s failed injection check: %s", group, this.mixin, var5.getMessage()));
      }

      this.mixin.postApply(transformedName, targetClass);
   }

   public String getUniqueName(MethodNode method, boolean preservePrefix) {
      return this.getTarget().getUniqueName(method, preservePrefix);
   }

   public String getUniqueName(FieldNode field) {
      return this.getTarget().getUniqueName(field);
   }

   public void prepareInjections() {
      this.injectors.clear();
      Iterator var1 = this.mergedMethods.iterator();

      while(var1.hasNext()) {
         MethodNode method = (MethodNode)var1.next();
         InjectionInfo injectInfo = InjectionInfo.parse(this, method);
         if (injectInfo != null) {
            if (injectInfo.isValid()) {
               injectInfo.prepare();
               this.injectors.add(injectInfo);
            }

            method.visibleAnnotations.remove(injectInfo.getAnnotation());
         }
      }

   }

   public void applyInjections() {
      Iterator var1 = this.injectors.iterator();

      InjectionInfo injectInfo;
      while(var1.hasNext()) {
         injectInfo = (InjectionInfo)var1.next();
         injectInfo.inject();
      }

      var1 = this.injectors.iterator();

      while(var1.hasNext()) {
         injectInfo = (InjectionInfo)var1.next();
         injectInfo.postInject();
      }

      this.injectors.clear();
   }

   public List generateAccessors() {
      Iterator var1 = this.accessors.iterator();

      while(var1.hasNext()) {
         AccessorInfo accessor = (AccessorInfo)var1.next();
         accessor.locate();
      }

      List methods = new ArrayList();
      Iterator var6 = this.accessors.iterator();

      while(var6.hasNext()) {
         AccessorInfo accessor = (AccessorInfo)var6.next();
         MethodNode generated = accessor.generate();
         this.getTarget().addMixinMethod(generated);
         methods.add(generated);
      }

      return methods;
   }
}
