package org.spongepowered.asm.lib.tree.analysis;

import java.util.List;
import org.spongepowered.asm.lib.Type;

public class SimpleVerifier extends BasicVerifier {
   private final Type currentClass;
   private final Type currentSuperClass;
   private final List currentClassInterfaces;
   private final boolean isInterface;
   private ClassLoader loader;

   public SimpleVerifier() {
      this((Type)null, (Type)null, false);
   }

   public SimpleVerifier(Type currentClass, Type currentSuperClass, boolean isInterface) {
      this(currentClass, currentSuperClass, (List)null, isInterface);
   }

   public SimpleVerifier(Type currentClass, Type currentSuperClass, List currentClassInterfaces, boolean isInterface) {
      this(327680, currentClass, currentSuperClass, currentClassInterfaces, isInterface);
   }

   protected SimpleVerifier(int api, Type currentClass, Type currentSuperClass, List currentClassInterfaces, boolean isInterface) {
      super(api);
      this.loader = this.getClass().getClassLoader();
      this.currentClass = currentClass;
      this.currentSuperClass = currentSuperClass;
      this.currentClassInterfaces = currentClassInterfaces;
      this.isInterface = isInterface;
   }

   public void setClassLoader(ClassLoader loader) {
      this.loader = loader;
   }

   public BasicValue newValue(Type type) {
      if (type == null) {
         return BasicValue.UNINITIALIZED_VALUE;
      } else {
         boolean isArray = type.getSort() == 9;
         if (isArray) {
            switch (type.getElementType().getSort()) {
               case 1:
               case 2:
               case 3:
               case 4:
                  return new BasicValue(type);
            }
         }

         BasicValue v = super.newValue(type);
         if (BasicValue.REFERENCE_VALUE.equals(v)) {
            if (isArray) {
               v = this.newValue(type.getElementType());
               String desc = v.getType().getDescriptor();

               for(int i = 0; i < type.getDimensions(); ++i) {
                  desc = '[' + desc;
               }

               v = new BasicValue(Type.getType(desc));
            } else {
               v = new BasicValue(type);
            }
         }

         return v;
      }
   }

   protected boolean isArrayValue(BasicValue value) {
      Type t = value.getType();
      return t != null && ("Lnull;".equals(t.getDescriptor()) || t.getSort() == 9);
   }

   protected BasicValue getElementValue(BasicValue objectArrayValue) throws AnalyzerException {
      Type arrayType = objectArrayValue.getType();
      if (arrayType != null) {
         if (arrayType.getSort() == 9) {
            return this.newValue(Type.getType(arrayType.getDescriptor().substring(1)));
         }

         if ("Lnull;".equals(arrayType.getDescriptor())) {
            return objectArrayValue;
         }
      }

      throw new Error("Internal error");
   }

   protected boolean isSubTypeOf(BasicValue value, BasicValue expected) {
      Type expectedType = expected.getType();
      Type type = value.getType();
      switch (expectedType.getSort()) {
         case 5:
         case 6:
         case 7:
         case 8:
            return type.equals(expectedType);
         case 9:
         case 10:
            if ("Lnull;".equals(type.getDescriptor())) {
               return true;
            } else {
               if (type.getSort() != 10 && type.getSort() != 9) {
                  return false;
               }

               return this.isAssignableFrom(expectedType, type);
            }
         default:
            throw new Error("Internal error");
      }
   }

   public BasicValue merge(BasicValue v, BasicValue w) {
      if (v.equals(w)) {
         return v;
      } else {
         Type t = v.getType();
         Type u = w.getType();
         if (t != null && (t.getSort() == 10 || t.getSort() == 9) && u != null && (u.getSort() == 10 || u.getSort() == 9)) {
            if ("Lnull;".equals(t.getDescriptor())) {
               return w;
            } else if ("Lnull;".equals(u.getDescriptor())) {
               return v;
            } else if (this.isAssignableFrom(t, u)) {
               return v;
            } else if (this.isAssignableFrom(u, t)) {
               return w;
            } else {
               while(t != null && !this.isInterface(t)) {
                  t = this.getSuperClass(t);
                  if (this.isAssignableFrom(t, u)) {
                     return this.newValue(t);
                  }
               }

               return BasicValue.REFERENCE_VALUE;
            }
         } else {
            return BasicValue.UNINITIALIZED_VALUE;
         }
      }
   }

   protected boolean isInterface(Type t) {
      return this.currentClass != null && t.equals(this.currentClass) ? this.isInterface : this.getClass(t).isInterface();
   }

   protected Type getSuperClass(Type t) {
      if (this.currentClass != null && t.equals(this.currentClass)) {
         return this.currentSuperClass;
      } else {
         Class c = this.getClass(t).getSuperclass();
         return c == null ? null : Type.getType(c);
      }
   }

   protected boolean isAssignableFrom(Type t, Type u) {
      if (t.equals(u)) {
         return true;
      } else if (this.currentClass != null && t.equals(this.currentClass)) {
         if (this.getSuperClass(u) == null) {
            return false;
         } else if (!this.isInterface) {
            return this.isAssignableFrom(t, this.getSuperClass(u));
         } else {
            return u.getSort() == 10 || u.getSort() == 9;
         }
      } else if (this.currentClass != null && u.equals(this.currentClass)) {
         if (this.isAssignableFrom(t, this.currentSuperClass)) {
            return true;
         } else {
            if (this.currentClassInterfaces != null) {
               for(int i = 0; i < this.currentClassInterfaces.size(); ++i) {
                  Type v = (Type)this.currentClassInterfaces.get(i);
                  if (this.isAssignableFrom(t, v)) {
                     return true;
                  }
               }
            }

            return false;
         }
      } else {
         Class tc = this.getClass(t);
         if (tc.isInterface()) {
            tc = Object.class;
         }

         return tc.isAssignableFrom(this.getClass(u));
      }
   }

   protected Class getClass(Type t) {
      try {
         return t.getSort() == 9 ? Class.forName(t.getDescriptor().replace('/', '.'), false, this.loader) : Class.forName(t.getClassName(), false, this.loader);
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException(var3.toString());
      }
   }
}
