package org.spongepowered.asm.mixin.transformer;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.struct.SourceMap;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.ClassSignature;

class TargetClassContext extends ClassContext implements ITargetClassContext {
   private static final Logger logger = LogManager.getLogger("mixin");
   private final MixinEnvironment env;
   private final Extensions extensions;
   private final String sessionId;
   private final String className;
   private final ClassNode classNode;
   private final ClassInfo classInfo;
   private final SourceMap sourceMap;
   private final ClassSignature signature;
   private final SortedSet mixins;
   private final Map targetMethods = new HashMap();
   private final Set mixinMethods = new HashSet();
   private int nextUniqueMethodIndex;
   private int nextUniqueFieldIndex;
   private boolean applied;
   private boolean forceExport;

   TargetClassContext(MixinEnvironment env, Extensions extensions, String sessionId, String name, ClassNode classNode, SortedSet mixins) {
      this.env = env;
      this.extensions = extensions;
      this.sessionId = sessionId;
      this.className = name;
      this.classNode = classNode;
      this.classInfo = ClassInfo.fromClassNode(classNode);
      this.signature = this.classInfo.getSignature();
      this.mixins = mixins;
      this.sourceMap = new SourceMap(classNode.sourceFile);
      this.sourceMap.addFile(this.classNode);
   }

   public String toString() {
      return this.className;
   }

   boolean isApplied() {
      return this.applied;
   }

   boolean isExportForced() {
      return this.forceExport;
   }

   Extensions getExtensions() {
      return this.extensions;
   }

   String getSessionId() {
      return this.sessionId;
   }

   String getClassRef() {
      return this.classNode.name;
   }

   String getClassName() {
      return this.className;
   }

   public ClassNode getClassNode() {
      return this.classNode;
   }

   List getMethods() {
      return this.classNode.methods;
   }

   List getFields() {
      return this.classNode.fields;
   }

   public ClassInfo getClassInfo() {
      return this.classInfo;
   }

   SortedSet getMixins() {
      return this.mixins;
   }

   SourceMap getSourceMap() {
      return this.sourceMap;
   }

   void mergeSignature(ClassSignature signature) {
      this.signature.merge(signature);
   }

   void addMixinMethod(MethodNode method) {
      this.mixinMethods.add(method);
   }

   void methodMerged(MethodNode method) {
      if (!this.mixinMethods.remove(method)) {
         logger.debug("Unexpected: Merged unregistered method {}{} in {}", new Object[]{method.name, method.desc, this});
      }

   }

   MethodNode findMethod(Deque aliases, String desc) {
      return this.findAliasedMethod(aliases, desc, true);
   }

   MethodNode findAliasedMethod(Deque aliases, String desc) {
      return this.findAliasedMethod(aliases, desc, false);
   }

   private MethodNode findAliasedMethod(Deque aliases, String desc, boolean includeMixinMethods) {
      String alias = (String)aliases.poll();
      if (alias == null) {
         return null;
      } else {
         Iterator var5 = this.classNode.methods.iterator();

         MethodNode target;
         while(var5.hasNext()) {
            target = (MethodNode)var5.next();
            if (target.name.equals(alias) && target.desc.equals(desc)) {
               return target;
            }
         }

         if (includeMixinMethods) {
            var5 = this.mixinMethods.iterator();

            while(var5.hasNext()) {
               target = (MethodNode)var5.next();
               if (target.name.equals(alias) && target.desc.equals(desc)) {
                  return target;
               }
            }
         }

         return this.findAliasedMethod(aliases, desc);
      }
   }

   FieldNode findAliasedField(Deque aliases, String desc) {
      String alias = (String)aliases.poll();
      if (alias == null) {
         return null;
      } else {
         Iterator var4 = this.classNode.fields.iterator();

         FieldNode target;
         do {
            if (!var4.hasNext()) {
               return this.findAliasedField(aliases, desc);
            }

            target = (FieldNode)var4.next();
         } while(!target.name.equals(alias) || !target.desc.equals(desc));

         return target;
      }
   }

   Target getTargetMethod(MethodNode method) {
      if (!this.classNode.methods.contains(method)) {
         throw new IllegalArgumentException("Invalid target method supplied to getTargetMethod()");
      } else {
         String targetName = method.name + method.desc;
         Target target = (Target)this.targetMethods.get(targetName);
         if (target == null) {
            target = new Target(this.classNode, method);
            this.targetMethods.put(targetName, target);
         }

         return target;
      }
   }

   String getUniqueName(MethodNode method, boolean preservePrefix) {
      String uniqueIndex = Integer.toHexString(this.nextUniqueMethodIndex++);
      String pattern = preservePrefix ? "%2$s_$md$%1$s$%3$s" : "md%s$%s$%s";
      return String.format(pattern, this.sessionId.substring(30), method.name, uniqueIndex);
   }

   String getUniqueName(FieldNode field) {
      String uniqueIndex = Integer.toHexString(this.nextUniqueFieldIndex++);
      return String.format("fd%s$%s$%s", this.sessionId.substring(30), field.name, uniqueIndex);
   }

   void applyMixins() {
      if (this.applied) {
         throw new IllegalStateException("Mixins already applied to target class " + this.className);
      } else {
         this.applied = true;
         MixinApplicatorStandard applicator = this.createApplicator();
         applicator.apply(this.mixins);
         this.applySignature();
         this.upgradeMethods();
         this.checkMerges();
      }
   }

   private MixinApplicatorStandard createApplicator() {
      return (MixinApplicatorStandard)(this.classInfo.isInterface() ? new MixinApplicatorInterface(this) : new MixinApplicatorStandard(this));
   }

   private void applySignature() {
      this.getClassNode().signature = this.signature.toString();
   }

   private void checkMerges() {
      Iterator var1 = this.mixinMethods.iterator();

      while(var1.hasNext()) {
         MethodNode method = (MethodNode)var1.next();
         if (!method.name.startsWith("<")) {
            logger.debug("Unexpected: Registered method {}{} in {} was not merged", new Object[]{method.name, method.desc, this});
         }
      }

   }

   void processDebugTasks() {
      if (this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
         AnnotationNode classDebugAnnotation = Annotations.getVisible(this.classNode, Debug.class);
         if (classDebugAnnotation != null) {
            this.forceExport = Boolean.TRUE.equals(Annotations.getValue(classDebugAnnotation, "export"));
            if (Boolean.TRUE.equals(Annotations.getValue(classDebugAnnotation, "print"))) {
               Bytecode.textify((ClassNode)this.classNode, System.err);
            }
         }

         Iterator var2 = this.classNode.methods.iterator();

         while(var2.hasNext()) {
            MethodNode method = (MethodNode)var2.next();
            AnnotationNode methodDebugAnnotation = Annotations.getVisible(method, Debug.class);
            if (methodDebugAnnotation != null && Boolean.TRUE.equals(Annotations.getValue(methodDebugAnnotation, "print"))) {
               Bytecode.textify((MethodNode)method, System.err);
            }
         }

      }
   }
}
