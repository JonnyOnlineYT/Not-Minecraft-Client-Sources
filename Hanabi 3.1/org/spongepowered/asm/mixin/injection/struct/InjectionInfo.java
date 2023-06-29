package org.spongepowered.asm.mixin.injection.struct;

import com.google.common.base.Strings;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.code.ISliceContext;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.code.InjectorTarget;
import org.spongepowered.asm.mixin.injection.code.MethodSlice;
import org.spongepowered.asm.mixin.injection.code.MethodSlices;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;

public abstract class InjectionInfo extends SpecialMethodInfo implements ISliceContext {
   protected final boolean isStatic;
   protected final Deque targets;
   protected final MethodSlices slices;
   protected final String atKey;
   protected final List injectionPoints;
   protected final Map targetNodes;
   protected Injector injector;
   protected InjectorGroupInfo group;
   private final List injectedMethods;
   private int expectedCallbackCount;
   private int requiredCallbackCount;
   private int maxCallbackCount;
   private int injectedCallbackCount;

   protected InjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
      this(mixin, method, annotation, "at");
   }

   protected InjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation, String atKey) {
      super(mixin, method, annotation);
      this.targets = new ArrayDeque();
      this.injectionPoints = new ArrayList();
      this.targetNodes = new LinkedHashMap();
      this.injectedMethods = new ArrayList(0);
      this.expectedCallbackCount = 1;
      this.requiredCallbackCount = 0;
      this.maxCallbackCount = Integer.MAX_VALUE;
      this.injectedCallbackCount = 0;
      this.isStatic = Bytecode.methodIsStatic(method);
      this.slices = MethodSlices.parse(this);
      this.atKey = atKey;
      this.readAnnotation();
   }

   protected void readAnnotation() {
      if (this.annotation != null) {
         String type = "@" + Bytecode.getSimpleName(this.annotation);
         List injectionPoints = this.readInjectionPoints(type);
         this.findMethods(this.parseTargets(type), type);
         this.parseInjectionPoints(injectionPoints);
         this.parseRequirements();
         this.injector = this.parseInjector(this.annotation);
      }
   }

   protected Set parseTargets(String type) {
      List methods = Annotations.getValue(this.annotation, "method", false);
      if (methods == null) {
         throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing method name", type, this.method.name));
      } else {
         Set members = new LinkedHashSet();
         Iterator var4 = methods.iterator();

         while(var4.hasNext()) {
            String method = (String)var4.next();

            try {
               MemberInfo targetMember = MemberInfo.parseAndValidate(method, this.mixin);
               if (targetMember.owner != null && !targetMember.owner.equals(this.mixin.getTargetClassRef())) {
                  throw new InvalidInjectionException(this, String.format("%s annotation on %s specifies a target class '%s', which is not supported", type, this.method.name, targetMember.owner));
               }

               members.add(targetMember);
            } catch (InvalidMemberDescriptorException var7) {
               throw new InvalidInjectionException(this, String.format("%s annotation on %s, has invalid target descriptor: \"%s\". %s", type, this.method.name, method, this.mixin.getReferenceMapper().getStatus()));
            }
         }

         return members;
      }
   }

   protected List readInjectionPoints(String type) {
      List ats = Annotations.getValue(this.annotation, this.atKey, false);
      if (ats == null) {
         throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing '%s' value(s)", type, this.method.name, this.atKey));
      } else {
         return ats;
      }
   }

   protected void parseInjectionPoints(List ats) {
      this.injectionPoints.addAll(InjectionPoint.parse(this.mixin, this.method, this.annotation, (List)ats));
   }

   protected void parseRequirements() {
      this.group = this.mixin.getInjectorGroups().parseGroup(this.method, this.mixin.getDefaultInjectorGroup()).add(this);
      Integer expect = (Integer)Annotations.getValue(this.annotation, "expect");
      if (expect != null) {
         this.expectedCallbackCount = expect;
      }

      Integer require = (Integer)Annotations.getValue(this.annotation, "require");
      if (require != null && require > -1) {
         this.requiredCallbackCount = require;
      } else if (this.group.isDefault()) {
         this.requiredCallbackCount = this.mixin.getDefaultRequiredInjections();
      }

      Integer allow = (Integer)Annotations.getValue(this.annotation, "allow");
      if (allow != null) {
         this.maxCallbackCount = Math.max(Math.max(this.requiredCallbackCount, 1), allow);
      }

   }

   protected abstract Injector parseInjector(AnnotationNode var1);

   public boolean isValid() {
      return this.targets.size() > 0 && this.injectionPoints.size() > 0;
   }

   public void prepare() {
      this.targetNodes.clear();
      Iterator var1 = this.targets.iterator();

      while(var1.hasNext()) {
         MethodNode targetMethod = (MethodNode)var1.next();
         Target target = this.mixin.getTargetMethod(targetMethod);
         InjectorTarget injectorTarget = new InjectorTarget(this, target);
         this.targetNodes.put(target, this.injector.find(injectorTarget, this.injectionPoints));
         injectorTarget.dispose();
      }

   }

   public void inject() {
      Iterator var1 = this.targetNodes.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry entry = (Map.Entry)var1.next();
         this.injector.inject((Target)entry.getKey(), (List)entry.getValue());
      }

      this.targets.clear();
   }

   public void postInject() {
      Iterator var1 = this.injectedMethods.iterator();

      while(var1.hasNext()) {
         MethodNode method = (MethodNode)var1.next();
         this.classNode.methods.add(method);
      }

      String description = this.getDescription();
      String refMapStatus = this.mixin.getReferenceMapper().getStatus();
      String dynamicInfo = this.getDynamicInfo();
      if (this.mixin.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_INJECTORS) && this.injectedCallbackCount < this.expectedCallbackCount) {
         throw new InvalidInjectionException(this, String.format("Injection validation failed: %s %s%s in %s expected %d invocation(s) but %d succeeded. %s%s", description, this.method.name, this.method.desc, this.mixin, this.expectedCallbackCount, this.injectedCallbackCount, refMapStatus, dynamicInfo));
      } else if (this.injectedCallbackCount < this.requiredCallbackCount) {
         throw new InjectionError(String.format("Critical injection failure: %s %s%s in %s failed injection check, (%d/%d) succeeded. %s%s", description, this.method.name, this.method.desc, this.mixin, this.injectedCallbackCount, this.requiredCallbackCount, refMapStatus, dynamicInfo));
      } else if (this.injectedCallbackCount > this.maxCallbackCount) {
         throw new InjectionError(String.format("Critical injection failure: %s %s%s in %s failed injection check, %d succeeded of %d allowed.%s", description, this.method.name, this.method.desc, this.mixin, this.injectedCallbackCount, this.maxCallbackCount, dynamicInfo));
      }
   }

   public void notifyInjected(Target target) {
   }

   protected String getDescription() {
      return "Callback method";
   }

   public String toString() {
      return describeInjector(this.mixin, this.annotation, this.method);
   }

   public Collection getTargets() {
      return this.targets;
   }

   public MethodSlice getSlice(String id) {
      return this.slices.get(this.getSliceId(id));
   }

   public String getSliceId(String id) {
      return "";
   }

   public int getInjectedCallbackCount() {
      return this.injectedCallbackCount;
   }

   public MethodNode addMethod(int access, String name, String desc) {
      MethodNode method = new MethodNode(327680, access | 4096, name, desc, (String)null, (String[])null);
      this.injectedMethods.add(method);
      return method;
   }

   public void addCallbackInvocation(MethodNode handler) {
      ++this.injectedCallbackCount;
   }

   private void findMethods(Set searchFor, String type) {
      this.targets.clear();
      int passes = this.mixin.getEnvironment().getOption(MixinEnvironment.Option.REFMAP_REMAP) ? 2 : 1;
      Iterator var4 = searchFor.iterator();

      while(var4.hasNext()) {
         MemberInfo member = (MemberInfo)var4.next();
         int count = 0;

         label63:
         for(int pass = 0; pass < passes && count < 1; ++pass) {
            int ordinal = 0;
            Iterator var9 = this.classNode.methods.iterator();

            while(true) {
               MethodNode target;
               boolean isMixinMethod;
               do {
                  do {
                     if (!var9.hasNext()) {
                        member = member.transform((String)null);
                        continue label63;
                     }

                     target = (MethodNode)var9.next();
                  } while(!member.matches(target.name, target.desc, ordinal));

                  isMixinMethod = Annotations.getVisible(target, MixinMerged.class) != null;
               } while(member.matchAll && (Bytecode.methodIsStatic(target) != this.isStatic || target == this.method || isMixinMethod));

               this.checkTarget(target);
               this.targets.add(target);
               ++ordinal;
               ++count;
            }
         }
      }

      if (this.targets.size() == 0) {
         throw new InvalidInjectionException(this, String.format("%s annotation on %s could not find any targets matching %s in the target class %s. %s%s", type, this.method.name, namesOf(searchFor), this.mixin.getTarget(), this.mixin.getReferenceMapper().getStatus(), this.getDynamicInfo()));
      }
   }

   private void checkTarget(MethodNode target) {
      AnnotationNode merged = Annotations.getVisible(target, MixinMerged.class);
      if (merged != null) {
         if (Annotations.getVisible(target, Final.class) != null) {
            throw new InvalidInjectionException(this, String.format("%s cannot inject into @Final method %s::%s%s merged by %s", this, this.classNode.name, target.name, target.desc, Annotations.getValue(merged, "mixin")));
         }
      }
   }

   protected String getDynamicInfo() {
      AnnotationNode annotation = Annotations.getInvisible(this.method, Dynamic.class);
      String description = Strings.nullToEmpty((String)Annotations.getValue(annotation));
      Type upstream = (Type)Annotations.getValue(annotation, "mixin");
      if (upstream != null) {
         description = String.format("{%s} %s", upstream.getClassName(), description).trim();
      }

      return description.length() > 0 ? String.format(" Method is @Dynamic(%s)", description) : "";
   }

   public static InjectionInfo parse(MixinTargetContext mixin, MethodNode method) {
      AnnotationNode annotation = getInjectorAnnotation(mixin.getMixin(), method);
      if (annotation == null) {
         return null;
      } else if (annotation.desc.endsWith(Inject.class.getSimpleName() + ";")) {
         return new CallbackInjectionInfo(mixin, method, annotation);
      } else if (annotation.desc.endsWith(ModifyArg.class.getSimpleName() + ";")) {
         return new ModifyArgInjectionInfo(mixin, method, annotation);
      } else if (annotation.desc.endsWith(ModifyArgs.class.getSimpleName() + ";")) {
         return new ModifyArgsInjectionInfo(mixin, method, annotation);
      } else if (annotation.desc.endsWith(Redirect.class.getSimpleName() + ";")) {
         return new RedirectInjectionInfo(mixin, method, annotation);
      } else if (annotation.desc.endsWith(ModifyVariable.class.getSimpleName() + ";")) {
         return new ModifyVariableInjectionInfo(mixin, method, annotation);
      } else {
         return annotation.desc.endsWith(ModifyConstant.class.getSimpleName() + ";") ? new ModifyConstantInjectionInfo(mixin, method, annotation) : null;
      }
   }

   public static AnnotationNode getInjectorAnnotation(IMixinInfo mixin, MethodNode method) {
      AnnotationNode annotation = null;

      try {
         annotation = Annotations.getSingleVisible(method, Inject.class, ModifyArg.class, ModifyArgs.class, Redirect.class, ModifyVariable.class, ModifyConstant.class);
         return annotation;
      } catch (IllegalArgumentException var4) {
         throw new InvalidMixinException(mixin, String.format("Error parsing annotations on %s in %s: %s", method.name, mixin.getClassName(), var4.getMessage()));
      }
   }

   public static String getInjectorPrefix(AnnotationNode annotation) {
      if (annotation != null) {
         if (annotation.desc.endsWith(ModifyArg.class.getSimpleName() + ";")) {
            return "modify";
         }

         if (annotation.desc.endsWith(ModifyArgs.class.getSimpleName() + ";")) {
            return "args";
         }

         if (annotation.desc.endsWith(Redirect.class.getSimpleName() + ";")) {
            return "redirect";
         }

         if (annotation.desc.endsWith(ModifyVariable.class.getSimpleName() + ";")) {
            return "localvar";
         }

         if (annotation.desc.endsWith(ModifyConstant.class.getSimpleName() + ";")) {
            return "constant";
         }
      }

      return "handler";
   }

   static String describeInjector(IMixinContext mixin, AnnotationNode annotation, MethodNode method) {
      return String.format("%s->@%s::%s%s", mixin.toString(), Bytecode.getSimpleName(annotation), method.name, method.desc);
   }

   private static String namesOf(Collection members) {
      int index = 0;
      int count = members.size();
      StringBuilder sb = new StringBuilder();

      for(Iterator var4 = members.iterator(); var4.hasNext(); ++index) {
         MemberInfo member = (MemberInfo)var4.next();
         if (index > 0) {
            if (index == count - 1) {
               sb.append(" or ");
            } else {
               sb.append(", ");
            }
         }

         sb.append('\'').append(member.name).append('\'');
      }

      return sb.toString();
   }
}
