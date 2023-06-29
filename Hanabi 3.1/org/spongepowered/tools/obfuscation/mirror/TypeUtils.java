package org.spongepowered.tools.obfuscation.mirror;

import java.util.Iterator;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import org.spongepowered.asm.util.SignaturePrinter;

public abstract class TypeUtils {
   private static final int MAX_GENERIC_RECURSION_DEPTH = 5;
   private static final String OBJECT_SIG = "java.lang.Object";
   private static final String OBJECT_REF = "java/lang/Object";

   private TypeUtils() {
   }

   public static PackageElement getPackage(TypeMirror type) {
      return !(type instanceof DeclaredType) ? null : getPackage((TypeElement)((DeclaredType)type).asElement());
   }

   public static PackageElement getPackage(TypeElement type) {
      Element parent;
      for(parent = type.getEnclosingElement(); parent != null && !(parent instanceof PackageElement); parent = parent.getEnclosingElement()) {
      }

      return (PackageElement)parent;
   }

   public static String getElementType(Element element) {
      if (element instanceof TypeElement) {
         return "TypeElement";
      } else if (element instanceof ExecutableElement) {
         return "ExecutableElement";
      } else if (element instanceof VariableElement) {
         return "VariableElement";
      } else if (element instanceof PackageElement) {
         return "PackageElement";
      } else {
         return element instanceof TypeParameterElement ? "TypeParameterElement" : element.getClass().getSimpleName();
      }
   }

   public static String stripGenerics(String type) {
      StringBuilder sb = new StringBuilder();
      int pos = 0;

      for(int depth = 0; pos < type.length(); ++pos) {
         char c = type.charAt(pos);
         if (c == '<') {
            ++depth;
         }

         if (depth == 0) {
            sb.append(c);
         } else if (c == '>') {
            --depth;
         }
      }

      return sb.toString();
   }

   public static String getName(VariableElement field) {
      return field != null ? field.getSimpleName().toString() : null;
   }

   public static String getName(ExecutableElement method) {
      return method != null ? method.getSimpleName().toString() : null;
   }

   public static String getJavaSignature(Element element) {
      if (element instanceof ExecutableElement) {
         ExecutableElement method = (ExecutableElement)element;
         StringBuilder desc = (new StringBuilder()).append("(");
         boolean extra = false;

         for(Iterator var4 = method.getParameters().iterator(); var4.hasNext(); extra = true) {
            VariableElement arg = (VariableElement)var4.next();
            if (extra) {
               desc.append(',');
            }

            desc.append(getTypeName(arg.asType()));
         }

         desc.append(')').append(getTypeName(method.getReturnType()));
         return desc.toString();
      } else {
         return getTypeName(element.asType());
      }
   }

   public static String getJavaSignature(String descriptor) {
      return (new SignaturePrinter("", descriptor)).setFullyQualified(true).toDescriptor();
   }

   public static String getTypeName(TypeMirror type) {
      switch (type.getKind()) {
         case ARRAY:
            return getTypeName(((ArrayType)type).getComponentType()) + "[]";
         case DECLARED:
            return getTypeName((DeclaredType)type);
         case TYPEVAR:
            return getTypeName(getUpperBound(type));
         case ERROR:
            return "java.lang.Object";
         default:
            return type.toString();
      }
   }

   public static String getTypeName(DeclaredType type) {
      return type == null ? "java.lang.Object" : getInternalName((TypeElement)type.asElement()).replace('/', '.');
   }

   public static String getDescriptor(Element element) {
      if (element instanceof ExecutableElement) {
         return getDescriptor((ExecutableElement)element);
      } else {
         return element instanceof VariableElement ? getInternalName((VariableElement)element) : getInternalName(element.asType());
      }
   }

   public static String getDescriptor(ExecutableElement method) {
      if (method == null) {
         return null;
      } else {
         StringBuilder signature = new StringBuilder();
         Iterator var2 = method.getParameters().iterator();

         while(var2.hasNext()) {
            VariableElement var = (VariableElement)var2.next();
            signature.append(getInternalName(var));
         }

         String returnType = getInternalName(method.getReturnType());
         return String.format("(%s)%s", signature, returnType);
      }
   }

   public static String getInternalName(VariableElement field) {
      return field == null ? null : getInternalName(field.asType());
   }

   public static String getInternalName(TypeMirror type) {
      switch (type.getKind()) {
         case ARRAY:
            return "[" + getInternalName(((ArrayType)type).getComponentType());
         case DECLARED:
            return "L" + getInternalName((DeclaredType)type) + ";";
         case TYPEVAR:
            return "L" + getInternalName(getUpperBound(type)) + ";";
         case ERROR:
            return "Ljava/lang/Object;";
         case BOOLEAN:
            return "Z";
         case BYTE:
            return "B";
         case CHAR:
            return "C";
         case DOUBLE:
            return "D";
         case FLOAT:
            return "F";
         case INT:
            return "I";
         case LONG:
            return "J";
         case SHORT:
            return "S";
         case VOID:
            return "V";
         default:
            throw new IllegalArgumentException("Unable to parse type symbol " + type + " with " + type.getKind() + " to equivalent bytecode type");
      }
   }

   public static String getInternalName(DeclaredType type) {
      return type == null ? "java/lang/Object" : getInternalName((TypeElement)type.asElement());
   }

   public static String getInternalName(TypeElement element) {
      if (element == null) {
         return null;
      } else {
         StringBuilder reference = new StringBuilder();
         reference.append(element.getSimpleName());

         for(Element parent = element.getEnclosingElement(); parent != null; parent = parent.getEnclosingElement()) {
            if (parent instanceof TypeElement) {
               reference.insert(0, "$").insert(0, parent.getSimpleName());
            } else if (parent instanceof PackageElement) {
               reference.insert(0, "/").insert(0, ((PackageElement)parent).getQualifiedName().toString().replace('.', '/'));
            }
         }

         return reference.toString();
      }
   }

   private static DeclaredType getUpperBound(TypeMirror type) {
      try {
         return getUpperBound0(type, 5);
      } catch (IllegalStateException var2) {
         throw new IllegalArgumentException("Type symbol \"" + type + "\" is too complex", var2);
      } catch (IllegalArgumentException var3) {
         throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + type, var3);
      }
   }

   private static DeclaredType getUpperBound0(TypeMirror type, int depth) {
      if (depth == 0) {
         throw new IllegalStateException("Generic symbol \"" + type + "\" is too complex, exceeded " + 5 + " iterations attempting to determine upper bound");
      } else if (type instanceof DeclaredType) {
         return (DeclaredType)type;
      } else if (type instanceof TypeVariable) {
         try {
            TypeMirror upper = ((TypeVariable)type).getUpperBound();
            --depth;
            return getUpperBound0(upper, depth);
         } catch (IllegalStateException var3) {
            throw var3;
         } catch (IllegalArgumentException var4) {
            throw var4;
         } catch (Exception var5) {
            throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + type);
         }
      } else {
         return null;
      }
   }

   public static boolean isAssignable(ProcessingEnvironment processingEnv, TypeMirror targetType, TypeMirror superClass) {
      boolean assignable = processingEnv.getTypeUtils().isAssignable(targetType, superClass);
      if (!assignable && targetType instanceof DeclaredType && superClass instanceof DeclaredType) {
         TypeMirror rawTargetType = toRawType(processingEnv, (DeclaredType)targetType);
         TypeMirror rawSuperType = toRawType(processingEnv, (DeclaredType)superClass);
         return processingEnv.getTypeUtils().isAssignable(rawTargetType, rawSuperType);
      } else {
         return assignable;
      }
   }

   private static TypeMirror toRawType(ProcessingEnvironment processingEnv, DeclaredType targetType) {
      return processingEnv.getElementUtils().getTypeElement(((TypeElement)targetType.asElement()).getQualifiedName()).asType();
   }

   public static Visibility getVisibility(Element element) {
      if (element == null) {
         return null;
      } else {
         Iterator var1 = element.getModifiers().iterator();

         while(var1.hasNext()) {
            Modifier modifier = (Modifier)var1.next();
            switch (modifier) {
               case PUBLIC:
                  return Visibility.PUBLIC;
               case PROTECTED:
                  return Visibility.PROTECTED;
               case PRIVATE:
                  return Visibility.PRIVATE;
            }
         }

         return Visibility.PACKAGE;
      }
   }
}
