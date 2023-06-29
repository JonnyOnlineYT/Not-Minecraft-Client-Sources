package org.spongepowered.asm.mixin.transformer;

import com.google.common.base.Strings;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.util.throwables.SyntheticBridgeException;

class MixinPreProcessorStandard {
   private static final Logger logger = LogManager.getLogger("mixin");
   protected final MixinInfo mixin;
   protected final MixinInfo.MixinClassNode classNode;
   protected final MixinEnvironment env;
   protected final Profiler profiler = MixinEnvironment.getProfiler();
   private final boolean verboseLogging;
   private final boolean strictUnique;
   private boolean prepared;
   private boolean attached;

   MixinPreProcessorStandard(MixinInfo mixin, MixinInfo.MixinClassNode classNode) {
      this.mixin = mixin;
      this.classNode = classNode;
      this.env = mixin.getParent().getEnvironment();
      this.verboseLogging = this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
      this.strictUnique = this.env.getOption(MixinEnvironment.Option.DEBUG_UNIQUE);
   }

   final MixinPreProcessorStandard prepare() {
      if (this.prepared) {
         return this;
      } else {
         this.prepared = true;
         Profiler.Section prepareTimer = this.profiler.begin("prepare");
         Iterator var2 = this.classNode.mixinMethods.iterator();

         while(var2.hasNext()) {
            MixinInfo.MixinMethodNode mixinMethod = (MixinInfo.MixinMethodNode)var2.next();
            ClassInfo.Method method = this.mixin.getClassInfo().findMethod((MethodNode)mixinMethod);
            this.prepareMethod(mixinMethod, method);
         }

         var2 = this.classNode.fields.iterator();

         while(var2.hasNext()) {
            FieldNode mixinField = (FieldNode)var2.next();
            this.prepareField(mixinField);
         }

         prepareTimer.end();
         return this;
      }
   }

   protected void prepareMethod(MixinInfo.MixinMethodNode mixinMethod, ClassInfo.Method method) {
      this.prepareShadow(mixinMethod, method);
      this.prepareSoftImplements(mixinMethod, method);
   }

   protected void prepareShadow(MixinInfo.MixinMethodNode mixinMethod, ClassInfo.Method method) {
      AnnotationNode shadowAnnotation = Annotations.getVisible((MethodNode)mixinMethod, Shadow.class);
      if (shadowAnnotation != null) {
         String prefix = (String)Annotations.getValue(shadowAnnotation, "prefix", Shadow.class);
         if (mixinMethod.name.startsWith(prefix)) {
            Annotations.setVisible((MethodNode)mixinMethod, MixinRenamed.class, "originalName", mixinMethod.name);
            String newName = mixinMethod.name.substring(prefix.length());
            mixinMethod.name = method.renameTo(newName);
         }

      }
   }

   protected void prepareSoftImplements(MixinInfo.MixinMethodNode mixinMethod, ClassInfo.Method method) {
      Iterator var3 = this.mixin.getSoftImplements().iterator();

      while(var3.hasNext()) {
         InterfaceInfo iface = (InterfaceInfo)var3.next();
         if (iface.renameMethod(mixinMethod)) {
            method.renameTo(mixinMethod.name);
         }
      }

   }

   protected void prepareField(FieldNode mixinField) {
   }

   final MixinPreProcessorStandard conform(TargetClassContext target) {
      return this.conform(target.getClassInfo());
   }

   final MixinPreProcessorStandard conform(ClassInfo target) {
      Profiler.Section conformTimer = this.profiler.begin("conform");
      Iterator var3 = this.classNode.mixinMethods.iterator();

      while(var3.hasNext()) {
         MixinInfo.MixinMethodNode mixinMethod = (MixinInfo.MixinMethodNode)var3.next();
         if (mixinMethod.isInjector()) {
            ClassInfo.Method method = this.mixin.getClassInfo().findMethod((MethodNode)mixinMethod, 10);
            this.conformInjector(target, mixinMethod, method);
         }
      }

      conformTimer.end();
      return this;
   }

   private void conformInjector(ClassInfo targetClass, MixinInfo.MixinMethodNode mixinMethod, ClassInfo.Method method) {
      MethodMapper methodMapper = targetClass.getMethodMapper();
      methodMapper.remapHandlerMethod(this.mixin, mixinMethod, method);
   }

   MixinTargetContext createContextFor(TargetClassContext target) {
      MixinTargetContext context = new MixinTargetContext(this.mixin, this.classNode, target);
      this.conform(target);
      this.attach(context);
      return context;
   }

   final MixinPreProcessorStandard attach(MixinTargetContext context) {
      if (this.attached) {
         throw new IllegalStateException("Preprocessor was already attached");
      } else {
         this.attached = true;
         Profiler.Section attachTimer = this.profiler.begin("attach");
         Profiler.Section timer = this.profiler.begin("methods");
         this.attachMethods(context);
         timer = timer.next("fields");
         this.attachFields(context);
         timer = timer.next("transform");
         this.transform(context);
         timer.end();
         attachTimer.end();
         return this;
      }
   }

   protected void attachMethods(MixinTargetContext context) {
      Iterator iter = this.classNode.mixinMethods.iterator();

      while(iter.hasNext()) {
         MixinInfo.MixinMethodNode mixinMethod = (MixinInfo.MixinMethodNode)iter.next();
         if (!this.validateMethod(context, mixinMethod)) {
            iter.remove();
         } else if (this.attachInjectorMethod(context, mixinMethod)) {
            context.addMixinMethod(mixinMethod);
         } else if (this.attachAccessorMethod(context, mixinMethod)) {
            iter.remove();
         } else if (this.attachShadowMethod(context, mixinMethod)) {
            context.addShadowMethod(mixinMethod);
            iter.remove();
         } else if (this.attachOverwriteMethod(context, mixinMethod)) {
            context.addMixinMethod(mixinMethod);
         } else if (this.attachUniqueMethod(context, mixinMethod)) {
            iter.remove();
         } else {
            this.attachMethod(context, mixinMethod);
            context.addMixinMethod(mixinMethod);
         }
      }

   }

   protected boolean validateMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod) {
      return true;
   }

   protected boolean attachInjectorMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod) {
      return mixinMethod.isInjector();
   }

   protected boolean attachAccessorMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod) {
      return this.attachAccessorMethod(context, mixinMethod, MixinPreProcessorStandard.SpecialMethod.ACCESSOR) || this.attachAccessorMethod(context, mixinMethod, MixinPreProcessorStandard.SpecialMethod.INVOKER);
   }

   protected boolean attachAccessorMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod, SpecialMethod type) {
      AnnotationNode annotation = mixinMethod.getVisibleAnnotation(type.annotation);
      if (annotation == null) {
         return false;
      } else {
         String description = type + " method " + mixinMethod.name;
         ClassInfo.Method method = this.getSpecialMethod(mixinMethod, type);
         if (MixinEnvironment.getCompatibilityLevel().isAtLeast(MixinEnvironment.CompatibilityLevel.JAVA_8) && method.isStatic()) {
            if (this.mixin.getTargets().size() > 1) {
               throw new InvalidAccessorException(context, description + " in multi-target mixin is invalid. Mixin must have exactly 1 target.");
            }

            String uniqueName = context.getUniqueName(mixinMethod, true);
            logger.log(this.mixin.getLoggingLevel(), "Renaming @Unique method {}{} to {} in {}", new Object[]{mixinMethod.name, mixinMethod.desc, uniqueName, this.mixin});
            mixinMethod.name = method.renameTo(uniqueName);
         } else {
            if (!method.isAbstract()) {
               throw new InvalidAccessorException(context, description + " is not abstract");
            }

            if (method.isStatic()) {
               throw new InvalidAccessorException(context, description + " cannot be static");
            }
         }

         context.addAccessorMethod(mixinMethod, type.annotation);
         return true;
      }
   }

   protected boolean attachShadowMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod) {
      return this.attachSpecialMethod(context, mixinMethod, MixinPreProcessorStandard.SpecialMethod.SHADOW);
   }

   protected boolean attachOverwriteMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod) {
      return this.attachSpecialMethod(context, mixinMethod, MixinPreProcessorStandard.SpecialMethod.OVERWRITE);
   }

   protected boolean attachSpecialMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod, SpecialMethod type) {
      AnnotationNode annotation = mixinMethod.getVisibleAnnotation(type.annotation);
      if (annotation == null) {
         return false;
      } else {
         if (type.isOverwrite) {
            this.checkMixinNotUnique(mixinMethod, type);
         }

         ClassInfo.Method method = this.getSpecialMethod(mixinMethod, type);
         MethodNode target = context.findMethod(mixinMethod, annotation);
         if (target == null) {
            if (type.isOverwrite) {
               return false;
            }

            target = context.findRemappedMethod(mixinMethod);
            if (target == null) {
               throw new InvalidMixinException(this.mixin, String.format("%s method %s in %s was not located in the target class %s. %s%s", type, mixinMethod.name, this.mixin, context.getTarget(), context.getReferenceMapper().getStatus(), getDynamicInfo((MethodNode)mixinMethod)));
            }

            mixinMethod.name = method.renameTo(target.name);
         }

         if ("<init>".equals(target.name)) {
            throw new InvalidMixinException(this.mixin, String.format("Nice try! %s in %s cannot alias a constructor", mixinMethod.name, this.mixin));
         } else if (!Bytecode.compareFlags((MethodNode)mixinMethod, (MethodNode)target, 8)) {
            throw new InvalidMixinException(this.mixin, String.format("STATIC modifier of %s method %s in %s does not match the target", type, mixinMethod.name, this.mixin));
         } else {
            this.conformVisibility(context, mixinMethod, type, target);
            if (!target.name.equals(mixinMethod.name)) {
               if (type.isOverwrite && (target.access & 2) == 0) {
                  throw new InvalidMixinException(this.mixin, "Non-private method cannot be aliased. Found " + target.name);
               }

               mixinMethod.name = method.renameTo(target.name);
            }

            return true;
         }
      }
   }

   private void conformVisibility(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod, SpecialMethod type, MethodNode target) {
      Bytecode.Visibility visTarget = Bytecode.getVisibility(target);
      Bytecode.Visibility visMethod = Bytecode.getVisibility((MethodNode)mixinMethod);
      if (visMethod.ordinal() >= visTarget.ordinal()) {
         if (visTarget == Bytecode.Visibility.PRIVATE && visMethod.ordinal() > Bytecode.Visibility.PRIVATE.ordinal()) {
            context.getTarget().addUpgradedMethod(target);
         }

      } else {
         String message = String.format("%s %s method %s in %s cannot reduce visibiliy of %s target method", visMethod, type, mixinMethod.name, this.mixin, visTarget);
         if (type.isOverwrite && !this.mixin.getParent().conformOverwriteVisibility()) {
            throw new InvalidMixinException(this.mixin, message);
         } else {
            if (visMethod == Bytecode.Visibility.PRIVATE) {
               if (type.isOverwrite) {
                  logger.warn("Static binding violation: {}, visibility will be upgraded.", new Object[]{message});
               }

               context.addUpgradedMethod(mixinMethod);
               Bytecode.setVisibility((MethodNode)mixinMethod, visTarget);
            }

         }
      }
   }

   protected ClassInfo.Method getSpecialMethod(MixinInfo.MixinMethodNode mixinMethod, SpecialMethod type) {
      ClassInfo.Method method = this.mixin.getClassInfo().findMethod((MethodNode)mixinMethod, 10);
      this.checkMethodNotUnique(method, type);
      return method;
   }

   protected void checkMethodNotUnique(ClassInfo.Method method, SpecialMethod type) {
      if (method.isUnique()) {
         throw new InvalidMixinException(this.mixin, String.format("%s method %s in %s cannot be @Unique", type, method.getName(), this.mixin));
      }
   }

   protected void checkMixinNotUnique(MixinInfo.MixinMethodNode mixinMethod, SpecialMethod type) {
      if (this.mixin.isUnique()) {
         throw new InvalidMixinException(this.mixin, String.format("%s method %s found in a @Unique mixin %s", type, mixinMethod.name, this.mixin));
      }
   }

   protected boolean attachUniqueMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod) {
      ClassInfo.Method method = this.mixin.getClassInfo().findMethod((MethodNode)mixinMethod, 10);
      if (method != null && (method.isUnique() || this.mixin.isUnique() || method.isSynthetic())) {
         if (method.isSynthetic()) {
            context.transformDescriptor((MethodNode)mixinMethod);
            method.remapTo(mixinMethod.desc);
         }

         MethodNode target = context.findMethod(mixinMethod, (AnnotationNode)null);
         if (target == null) {
            return false;
         } else {
            String type = method.isSynthetic() ? "synthetic" : "@Unique";
            if (Bytecode.getVisibility((MethodNode)mixinMethod).ordinal() < Bytecode.Visibility.PUBLIC.ordinal()) {
               String uniqueName = context.getUniqueName(mixinMethod, false);
               logger.log(this.mixin.getLoggingLevel(), "Renaming {} method {}{} to {} in {}", new Object[]{type, mixinMethod.name, mixinMethod.desc, uniqueName, this.mixin});
               mixinMethod.name = method.renameTo(uniqueName);
               return false;
            } else if (this.strictUnique) {
               throw new InvalidMixinException(this.mixin, String.format("Method conflict, %s method %s in %s cannot overwrite %s%s in %s", type, mixinMethod.name, this.mixin, target.name, target.desc, context.getTarget()));
            } else {
               AnnotationNode unique = Annotations.getVisible((MethodNode)mixinMethod, Unique.class);
               if (unique != null && (Boolean)Annotations.getValue(unique, "silent", (Object)Boolean.FALSE)) {
                  context.addMixinMethod(mixinMethod);
                  return true;
               } else if (Bytecode.hasFlag((MethodNode)mixinMethod, 64)) {
                  try {
                     Bytecode.compareBridgeMethods(target, mixinMethod);
                     logger.debug("Discarding sythetic bridge method {} in {} because existing method in {} is compatible", new Object[]{type, mixinMethod.name, this.mixin, context.getTarget()});
                     return true;
                  } catch (SyntheticBridgeException var8) {
                     if (this.verboseLogging || this.env.getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
                        var8.printAnalysis(context, target, mixinMethod);
                     }

                     throw new InvalidMixinException(this.mixin, var8.getMessage());
                  }
               } else {
                  logger.warn("Discarding {} public method {} in {} because it already exists in {}", new Object[]{type, mixinMethod.name, this.mixin, context.getTarget()});
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   protected void attachMethod(MixinTargetContext context, MixinInfo.MixinMethodNode mixinMethod) {
      ClassInfo.Method method = this.mixin.getClassInfo().findMethod((MethodNode)mixinMethod);
      if (method != null) {
         ClassInfo.Method parentMethod = this.mixin.getClassInfo().findMethodInHierarchy((MethodNode)mixinMethod, ClassInfo.SearchType.SUPER_CLASSES_ONLY);
         if (parentMethod != null && parentMethod.isRenamed()) {
            mixinMethod.name = method.renameTo(parentMethod.getName());
         }

         MethodNode target = context.findMethod(mixinMethod, (AnnotationNode)null);
         if (target != null) {
            this.conformVisibility(context, mixinMethod, MixinPreProcessorStandard.SpecialMethod.MERGE, target);
         }

      }
   }

   protected void attachFields(MixinTargetContext context) {
      Iterator iter = this.classNode.fields.iterator();

      while(true) {
         while(iter.hasNext()) {
            FieldNode mixinField = (FieldNode)iter.next();
            AnnotationNode shadow = Annotations.getVisible(mixinField, Shadow.class);
            boolean isShadow = shadow != null;
            if (!this.validateField(context, mixinField, shadow)) {
               iter.remove();
            } else {
               ClassInfo.Field field = this.mixin.getClassInfo().findField(mixinField);
               context.transformDescriptor(mixinField);
               field.remapTo(mixinField.desc);
               if (field.isUnique() && isShadow) {
                  throw new InvalidMixinException(this.mixin, String.format("@Shadow field %s cannot be @Unique", mixinField.name));
               }

               FieldNode target = context.findField(mixinField, shadow);
               if (target == null) {
                  if (shadow == null) {
                     continue;
                  }

                  target = context.findRemappedField(mixinField);
                  if (target == null) {
                     throw new InvalidMixinException(this.mixin, String.format("Shadow field %s was not located in the target class %s. %s%s", mixinField.name, context.getTarget(), context.getReferenceMapper().getStatus(), getDynamicInfo(mixinField)));
                  }

                  mixinField.name = field.renameTo(target.name);
               }

               if (!Bytecode.compareFlags((FieldNode)mixinField, (FieldNode)target, 8)) {
                  throw new InvalidMixinException(this.mixin, String.format("STATIC modifier of @Shadow field %s in %s does not match the target", mixinField.name, this.mixin));
               }

               if (field.isUnique()) {
                  if ((mixinField.access & 6) != 0) {
                     String uniqueName = context.getUniqueName(mixinField);
                     logger.log(this.mixin.getLoggingLevel(), "Renaming @Unique field {}{} to {} in {}", new Object[]{mixinField.name, mixinField.desc, uniqueName, this.mixin});
                     mixinField.name = field.renameTo(uniqueName);
                  } else {
                     if (this.strictUnique) {
                        throw new InvalidMixinException(this.mixin, String.format("Field conflict, @Unique field %s in %s cannot overwrite %s%s in %s", mixinField.name, this.mixin, target.name, target.desc, context.getTarget()));
                     }

                     logger.warn("Discarding @Unique public field {} in {} because it already exists in {}. Note that declared FIELD INITIALISERS will NOT be removed!", new Object[]{mixinField.name, this.mixin, context.getTarget()});
                     iter.remove();
                  }
               } else {
                  if (!target.desc.equals(mixinField.desc)) {
                     throw new InvalidMixinException(this.mixin, String.format("The field %s in the target class has a conflicting signature", mixinField.name));
                  }

                  if (!target.name.equals(mixinField.name)) {
                     if ((target.access & 2) == 0 && (target.access & 4096) == 0) {
                        throw new InvalidMixinException(this.mixin, "Non-private field cannot be aliased. Found " + target.name);
                     }

                     mixinField.name = field.renameTo(target.name);
                  }

                  iter.remove();
                  if (isShadow) {
                     boolean isFinal = field.isDecoratedFinal();
                     if (this.verboseLogging && Bytecode.hasFlag((FieldNode)target, 16) != isFinal) {
                        String message = isFinal ? "@Shadow field {}::{} is decorated with @Final but target is not final" : "@Shadow target {}::{} is final but shadow is not decorated with @Final";
                        logger.warn(message, new Object[]{this.mixin, mixinField.name});
                     }

                     context.addShadowField(mixinField, field);
                  }
               }
            }
         }

         return;
      }
   }

   protected boolean validateField(MixinTargetContext context, FieldNode field, AnnotationNode shadow) {
      if (Bytecode.hasFlag((FieldNode)field, 8) && !Bytecode.hasFlag((FieldNode)field, 2) && !Bytecode.hasFlag((FieldNode)field, 4096) && shadow == null) {
         throw new InvalidMixinException(context, String.format("Mixin %s contains non-private static field %s:%s", context, field.name, field.desc));
      } else {
         String prefix = (String)Annotations.getValue(shadow, "prefix", Shadow.class);
         if (field.name.startsWith(prefix)) {
            throw new InvalidMixinException(context, String.format("@Shadow field %s.%s has a shadow prefix. This is not allowed.", context, field.name));
         } else if ("super$".equals(field.name)) {
            if (field.access != 2) {
               throw new InvalidMixinException(this.mixin, String.format("Imaginary super field %s.%s must be private and non-final", context, field.name));
            } else if (!field.desc.equals("L" + this.mixin.getClassRef() + ";")) {
               throw new InvalidMixinException(this.mixin, String.format("Imaginary super field %s.%s must have the same type as the parent mixin (%s)", context, field.name, this.mixin.getClassName()));
            } else {
               return false;
            }
         } else {
            return true;
         }
      }
   }

   protected void transform(MixinTargetContext context) {
      Iterator var2 = this.classNode.methods.iterator();

      while(var2.hasNext()) {
         MethodNode mixinMethod = (MethodNode)var2.next();
         Iterator iter = mixinMethod.instructions.iterator();

         while(iter.hasNext()) {
            AbstractInsnNode insn = (AbstractInsnNode)iter.next();
            if (insn instanceof MethodInsnNode) {
               this.transformMethod((MethodInsnNode)insn);
            } else if (insn instanceof FieldInsnNode) {
               this.transformField((FieldInsnNode)insn);
            }
         }
      }

   }

   protected void transformMethod(MethodInsnNode methodNode) {
      Profiler.Section metaTimer = this.profiler.begin("meta");
      ClassInfo owner = ClassInfo.forName(methodNode.owner);
      if (owner == null) {
         throw new RuntimeException(new ClassNotFoundException(methodNode.owner.replace('/', '.')));
      } else {
         ClassInfo.Method method = owner.findMethodInHierarchy((MethodInsnNode)methodNode, ClassInfo.SearchType.ALL_CLASSES, 2);
         metaTimer.end();
         if (method != null && method.isRenamed()) {
            methodNode.name = method.getName();
         }

      }
   }

   protected void transformField(FieldInsnNode fieldNode) {
      Profiler.Section metaTimer = this.profiler.begin("meta");
      ClassInfo owner = ClassInfo.forName(fieldNode.owner);
      if (owner == null) {
         throw new RuntimeException(new ClassNotFoundException(fieldNode.owner.replace('/', '.')));
      } else {
         ClassInfo.Field field = owner.findField(fieldNode, 2);
         metaTimer.end();
         if (field != null && field.isRenamed()) {
            fieldNode.name = field.getName();
         }

      }
   }

   protected static String getDynamicInfo(MethodNode method) {
      return getDynamicInfo("Method", Annotations.getInvisible(method, Dynamic.class));
   }

   protected static String getDynamicInfo(FieldNode method) {
      return getDynamicInfo("Field", Annotations.getInvisible(method, Dynamic.class));
   }

   private static String getDynamicInfo(String targetType, AnnotationNode annotation) {
      String description = Strings.nullToEmpty((String)Annotations.getValue(annotation));
      Type upstream = (Type)Annotations.getValue(annotation, "mixin");
      if (upstream != null) {
         description = String.format("{%s} %s", upstream.getClassName(), description).trim();
      }

      return description.length() > 0 ? String.format(" %s is @Dynamic(%s)", targetType, description) : "";
   }

   static enum SpecialMethod {
      MERGE(true),
      OVERWRITE(true, Overwrite.class),
      SHADOW(false, Shadow.class),
      ACCESSOR(false, Accessor.class),
      INVOKER(false, Invoker.class);

      final boolean isOverwrite;
      final Class annotation;
      final String description;

      private SpecialMethod(boolean isOverwrite, Class type) {
         this.isOverwrite = isOverwrite;
         this.annotation = type;
         this.description = "@" + Bytecode.getSimpleName(type);
      }

      private SpecialMethod(boolean isOverwrite) {
         this.isOverwrite = isOverwrite;
         this.annotation = null;
         this.description = "overwrite";
      }

      public String toString() {
         return this.description;
      }
   }
}
