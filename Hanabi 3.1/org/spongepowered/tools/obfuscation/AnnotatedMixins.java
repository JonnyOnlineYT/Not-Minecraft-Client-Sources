package org.spongepowered.tools.obfuscation;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.util.ITokenProvider;
import org.spongepowered.tools.obfuscation.interfaces.IJavadocProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandleSimulated;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import org.spongepowered.tools.obfuscation.validation.ParentValidator;
import org.spongepowered.tools.obfuscation.validation.TargetValidator;

final class AnnotatedMixins implements IMixinAnnotationProcessor, ITokenProvider, ITypeHandleProvider, IJavadocProvider {
   private static final String MAPID_SYSTEM_PROPERTY = "mixin.target.mapid";
   private static Map instances = new HashMap();
   private final IMixinAnnotationProcessor.CompilerEnvironment env;
   private final ProcessingEnvironment processingEnv;
   private final Map mixins = new HashMap();
   private final List mixinsForPass = new ArrayList();
   private final IObfuscationManager obf;
   private final List validators;
   private final Map tokenCache = new HashMap();
   private final TargetMap targets;
   private Properties properties;

   private AnnotatedMixins(ProcessingEnvironment processingEnv) {
      this.env = this.detectEnvironment(processingEnv);
      this.processingEnv = processingEnv;
      this.printMessage(Kind.NOTE, "SpongePowered MIXIN Annotation Processor Version=0.7.11");
      this.targets = this.initTargetMap();
      this.obf = new ObfuscationManager(this);
      this.obf.init();
      this.validators = ImmutableList.of(new ParentValidator(this), new TargetValidator(this));
      this.initTokenCache(this.getOption("tokens"));
   }

   protected TargetMap initTargetMap() {
      TargetMap targets = TargetMap.create(System.getProperty("mixin.target.mapid"));
      System.setProperty("mixin.target.mapid", targets.getSessionId());
      String targetsFileName = this.getOption("dependencyTargetsFile");
      if (targetsFileName != null) {
         try {
            targets.readImports(new File(targetsFileName));
         } catch (IOException var4) {
            this.printMessage(Kind.WARNING, "Could not read from specified imports file: " + targetsFileName);
         }
      }

      return targets;
   }

   private void initTokenCache(String tokens) {
      if (tokens != null) {
         Pattern tokenPattern = Pattern.compile("^([A-Z0-9\\-_\\.]+)=([0-9]+)$");
         String[] tokenValues = tokens.replaceAll("\\s", "").toUpperCase().split("[;,]");
         String[] var4 = tokenValues;
         int var5 = tokenValues.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String tokenValue = var4[var6];
            Matcher tokenMatcher = tokenPattern.matcher(tokenValue);
            if (tokenMatcher.matches()) {
               this.tokenCache.put(tokenMatcher.group(1), Integer.parseInt(tokenMatcher.group(2)));
            }
         }
      }

   }

   public ITypeHandleProvider getTypeProvider() {
      return this;
   }

   public ITokenProvider getTokenProvider() {
      return this;
   }

   public IObfuscationManager getObfuscationManager() {
      return this.obf;
   }

   public IJavadocProvider getJavadocProvider() {
      return this;
   }

   public ProcessingEnvironment getProcessingEnvironment() {
      return this.processingEnv;
   }

   public IMixinAnnotationProcessor.CompilerEnvironment getCompilerEnvironment() {
      return this.env;
   }

   public Integer getToken(String token) {
      if (this.tokenCache.containsKey(token)) {
         return (Integer)this.tokenCache.get(token);
      } else {
         String option = this.getOption(token);
         Integer value = null;

         try {
            value = Integer.parseInt(option);
         } catch (Exception var5) {
         }

         this.tokenCache.put(token, value);
         return value;
      }
   }

   public String getOption(String option) {
      if (option == null) {
         return null;
      } else {
         String value = (String)this.processingEnv.getOptions().get(option);
         return value != null ? value : this.getProperties().getProperty(option);
      }
   }

   public Properties getProperties() {
      if (this.properties == null) {
         this.properties = new Properties();

         try {
            Filer filer = this.processingEnv.getFiler();
            FileObject propertyFile = filer.getResource(StandardLocation.SOURCE_PATH, "", "mixin.properties");
            if (propertyFile != null) {
               InputStream inputStream = propertyFile.openInputStream();
               this.properties.load(inputStream);
               inputStream.close();
            }
         } catch (Exception var4) {
         }
      }

      return this.properties;
   }

   private IMixinAnnotationProcessor.CompilerEnvironment detectEnvironment(ProcessingEnvironment processingEnv) {
      return processingEnv.getClass().getName().contains("jdt") ? IMixinAnnotationProcessor.CompilerEnvironment.JDT : IMixinAnnotationProcessor.CompilerEnvironment.JAVAC;
   }

   public void writeMappings() {
      this.obf.writeMappings();
   }

   public void writeReferences() {
      this.obf.writeReferences();
   }

   public void clear() {
      this.mixins.clear();
   }

   public void registerMixin(TypeElement mixinType) {
      String name = mixinType.getQualifiedName().toString();
      if (!this.mixins.containsKey(name)) {
         AnnotatedMixin mixin = new AnnotatedMixin(this, mixinType);
         this.targets.registerTargets(mixin);
         mixin.runValidators(IMixinValidator.ValidationPass.EARLY, this.validators);
         this.mixins.put(name, mixin);
         this.mixinsForPass.add(mixin);
      }

   }

   public AnnotatedMixin getMixin(TypeElement mixinType) {
      return this.getMixin(mixinType.getQualifiedName().toString());
   }

   public AnnotatedMixin getMixin(String mixinType) {
      return (AnnotatedMixin)this.mixins.get(mixinType);
   }

   public Collection getMixinsTargeting(TypeMirror targetType) {
      return this.getMixinsTargeting((TypeElement)((DeclaredType)targetType).asElement());
   }

   public Collection getMixinsTargeting(TypeElement targetType) {
      List minions = new ArrayList();
      Iterator var3 = this.targets.getMixinsTargeting(targetType).iterator();

      while(var3.hasNext()) {
         TypeReference mixin = (TypeReference)var3.next();
         TypeHandle handle = mixin.getHandle(this.processingEnv);
         if (handle != null) {
            minions.add(handle.getType());
         }
      }

      return minions;
   }

   public void registerAccessor(TypeElement mixinType, ExecutableElement method) {
      AnnotatedMixin mixinClass = this.getMixin(mixinType);
      if (mixinClass == null) {
         this.printMessage(Kind.ERROR, "Found @Accessor annotation on a non-mixin method", method);
      } else {
         AnnotationHandle accessor = AnnotationHandle.of(method, Accessor.class);
         mixinClass.registerAccessor(method, accessor, this.shouldRemap(mixinClass, accessor));
      }
   }

   public void registerInvoker(TypeElement mixinType, ExecutableElement method) {
      AnnotatedMixin mixinClass = this.getMixin(mixinType);
      if (mixinClass == null) {
         this.printMessage(Kind.ERROR, "Found @Accessor annotation on a non-mixin method", method);
      } else {
         AnnotationHandle invoker = AnnotationHandle.of(method, Invoker.class);
         mixinClass.registerInvoker(method, invoker, this.shouldRemap(mixinClass, invoker));
      }
   }

   public void registerOverwrite(TypeElement mixinType, ExecutableElement method) {
      AnnotatedMixin mixinClass = this.getMixin(mixinType);
      if (mixinClass == null) {
         this.printMessage(Kind.ERROR, "Found @Overwrite annotation on a non-mixin method", method);
      } else {
         AnnotationHandle overwrite = AnnotationHandle.of(method, Overwrite.class);
         mixinClass.registerOverwrite(method, overwrite, this.shouldRemap(mixinClass, overwrite));
      }
   }

   public void registerShadow(TypeElement mixinType, VariableElement field, AnnotationHandle shadow) {
      AnnotatedMixin mixinClass = this.getMixin(mixinType);
      if (mixinClass == null) {
         this.printMessage(Kind.ERROR, "Found @Shadow annotation on a non-mixin field", field);
      } else {
         mixinClass.registerShadow(field, shadow, this.shouldRemap(mixinClass, shadow));
      }
   }

   public void registerShadow(TypeElement mixinType, ExecutableElement method, AnnotationHandle shadow) {
      AnnotatedMixin mixinClass = this.getMixin(mixinType);
      if (mixinClass == null) {
         this.printMessage(Kind.ERROR, "Found @Shadow annotation on a non-mixin method", method);
      } else {
         mixinClass.registerShadow(method, shadow, this.shouldRemap(mixinClass, shadow));
      }
   }

   public void registerInjector(TypeElement mixinType, ExecutableElement method, AnnotationHandle inject) {
      AnnotatedMixin mixinClass = this.getMixin(mixinType);
      if (mixinClass == null) {
         this.printMessage(Kind.ERROR, "Found " + inject + " annotation on a non-mixin method", method);
      } else {
         InjectorRemap remap = new InjectorRemap(this.shouldRemap(mixinClass, inject));
         mixinClass.registerInjector(method, inject, remap);
         remap.dispatchPendingMessages(this);
      }
   }

   public void registerSoftImplements(TypeElement mixin, AnnotationHandle implementsAnnotation) {
      AnnotatedMixin mixinClass = this.getMixin(mixin);
      if (mixinClass == null) {
         this.printMessage(Kind.ERROR, "Found @Implements annotation on a non-mixin class");
      } else {
         mixinClass.registerSoftImplements(implementsAnnotation);
      }
   }

   public void onPassStarted() {
      this.mixinsForPass.clear();
   }

   public void onPassCompleted(RoundEnvironment roundEnv) {
      if (!"true".equalsIgnoreCase(this.getOption("disableTargetExport"))) {
         this.targets.write(true);
      }

      Iterator var2 = ((Collection)(roundEnv.processingOver() ? this.mixins.values() : this.mixinsForPass)).iterator();

      while(var2.hasNext()) {
         AnnotatedMixin mixin = (AnnotatedMixin)var2.next();
         mixin.runValidators(roundEnv.processingOver() ? IMixinValidator.ValidationPass.FINAL : IMixinValidator.ValidationPass.LATE, this.validators);
      }

   }

   private boolean shouldRemap(AnnotatedMixin mixinClass, AnnotationHandle annotation) {
      return annotation.getBoolean("remap", mixinClass.remap());
   }

   public void printMessage(Diagnostic.Kind kind, CharSequence msg) {
      if (this.env == IMixinAnnotationProcessor.CompilerEnvironment.JAVAC || kind != Kind.NOTE) {
         this.processingEnv.getMessager().printMessage(kind, msg);
      }

   }

   public void printMessage(Diagnostic.Kind kind, CharSequence msg, Element element) {
      this.processingEnv.getMessager().printMessage(kind, msg, element);
   }

   public void printMessage(Diagnostic.Kind kind, CharSequence msg, Element element, AnnotationMirror annotation) {
      this.processingEnv.getMessager().printMessage(kind, msg, element, annotation);
   }

   public void printMessage(Diagnostic.Kind kind, CharSequence msg, Element element, AnnotationMirror annotation, AnnotationValue value) {
      this.processingEnv.getMessager().printMessage(kind, msg, element, annotation, value);
   }

   public TypeHandle getTypeHandle(String name) {
      name = name.replace('/', '.');
      Elements elements = this.processingEnv.getElementUtils();
      TypeElement element = elements.getTypeElement(name);
      if (element != null) {
         try {
            return new TypeHandle(element);
         } catch (NullPointerException var7) {
         }
      }

      int lastDotPos = name.lastIndexOf(46);
      if (lastDotPos > -1) {
         String pkg = name.substring(0, lastDotPos);
         PackageElement packageElement = elements.getPackageElement(pkg);
         if (packageElement != null) {
            return new TypeHandle(packageElement, name);
         }
      }

      return null;
   }

   public TypeHandle getSimulatedHandle(String name, TypeMirror simulatedTarget) {
      name = name.replace('/', '.');
      int lastDotPos = name.lastIndexOf(46);
      if (lastDotPos > -1) {
         String pkg = name.substring(0, lastDotPos);
         PackageElement packageElement = this.processingEnv.getElementUtils().getPackageElement(pkg);
         if (packageElement != null) {
            return new TypeHandleSimulated(packageElement, name, simulatedTarget);
         }
      }

      return new TypeHandleSimulated(name, simulatedTarget);
   }

   public String getJavadoc(Element element) {
      Elements elements = this.processingEnv.getElementUtils();
      return elements.getDocComment(element);
   }

   public static AnnotatedMixins getMixinsForEnvironment(ProcessingEnvironment processingEnv) {
      AnnotatedMixins mixins = (AnnotatedMixins)instances.get(processingEnv);
      if (mixins == null) {
         mixins = new AnnotatedMixins(processingEnv);
         instances.put(processingEnv, mixins);
      }

      return mixins;
   }
}
