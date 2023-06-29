package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.lib.AnnotationVisitor;

public class AnnotationNode extends AnnotationVisitor {
   public String desc;
   public List values;

   public AnnotationNode(String desc) {
      this(327680, desc);
      if (this.getClass() != AnnotationNode.class) {
         throw new IllegalStateException();
      }
   }

   public AnnotationNode(int api, String desc) {
      super(api);
      this.desc = desc;
   }

   AnnotationNode(List values) {
      super(327680);
      this.values = values;
   }

   public void visit(String name, Object value) {
      if (this.values == null) {
         this.values = new ArrayList(this.desc != null ? 2 : 1);
      }

      if (this.desc != null) {
         this.values.add(name);
      }

      ArrayList l;
      int var6;
      int var7;
      int i;
      if (value instanceof byte[]) {
         byte[] v = (byte[])((byte[])value);
         l = new ArrayList(v.length);
         byte[] var5 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            i = var5[var7];
            l.add(Byte.valueOf((byte)i));
         }

         this.values.add(l);
      } else if (value instanceof boolean[]) {
         boolean[] v = (boolean[])((boolean[])value);
         l = new ArrayList(v.length);
         boolean[] var17 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            boolean b = var17[var7];
            l.add(b);
         }

         this.values.add(l);
      } else if (value instanceof short[]) {
         short[] v = (short[])((short[])value);
         l = new ArrayList(v.length);
         short[] var19 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            i = var19[var7];
            l.add(Short.valueOf((short)i));
         }

         this.values.add(l);
      } else if (value instanceof char[]) {
         char[] v = (char[])((char[])value);
         l = new ArrayList(v.length);
         char[] var20 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            char c = var20[var7];
            l.add(c);
         }

         this.values.add(l);
      } else if (value instanceof int[]) {
         int[] v = (int[])((int[])value);
         l = new ArrayList(v.length);
         int[] var21 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            i = var21[var7];
            l.add(i);
         }

         this.values.add(l);
      } else if (value instanceof long[]) {
         long[] v = (long[])((long[])value);
         l = new ArrayList(v.length);
         long[] var22 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            long lng = var22[var7];
            l.add(lng);
         }

         this.values.add(l);
      } else if (value instanceof float[]) {
         float[] v = (float[])((float[])value);
         l = new ArrayList(v.length);
         float[] var23 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            float f = var23[var7];
            l.add(f);
         }

         this.values.add(l);
      } else if (value instanceof double[]) {
         double[] v = (double[])((double[])value);
         l = new ArrayList(v.length);
         double[] var24 = v;
         var6 = v.length;

         for(var7 = 0; var7 < var6; ++var7) {
            double d = var24[var7];
            l.add(d);
         }

         this.values.add(l);
      } else {
         this.values.add(value);
      }

   }

   public void visitEnum(String name, String desc, String value) {
      if (this.values == null) {
         this.values = new ArrayList(this.desc != null ? 2 : 1);
      }

      if (this.desc != null) {
         this.values.add(name);
      }

      this.values.add(new String[]{desc, value});
   }

   public AnnotationVisitor visitAnnotation(String name, String desc) {
      if (this.values == null) {
         this.values = new ArrayList(this.desc != null ? 2 : 1);
      }

      if (this.desc != null) {
         this.values.add(name);
      }

      AnnotationNode annotation = new AnnotationNode(desc);
      this.values.add(annotation);
      return annotation;
   }

   public AnnotationVisitor visitArray(String name) {
      if (this.values == null) {
         this.values = new ArrayList(this.desc != null ? 2 : 1);
      }

      if (this.desc != null) {
         this.values.add(name);
      }

      List array = new ArrayList();
      this.values.add(array);
      return new AnnotationNode(array);
   }

   public void visitEnd() {
   }

   public void check(int api) {
   }

   public void accept(AnnotationVisitor av) {
      if (av != null) {
         if (this.values != null) {
            for(int i = 0; i < this.values.size(); i += 2) {
               String name = (String)this.values.get(i);
               Object value = this.values.get(i + 1);
               accept(av, name, value);
            }
         }

         av.visitEnd();
      }

   }

   static void accept(AnnotationVisitor av, String name, Object value) {
      if (av != null) {
         if (value instanceof String[]) {
            String[] typeconst = (String[])((String[])value);
            av.visitEnum(name, typeconst[0], typeconst[1]);
         } else if (value instanceof AnnotationNode) {
            AnnotationNode an = (AnnotationNode)value;
            an.accept(av.visitAnnotation(name, an.desc));
         } else if (value instanceof List) {
            AnnotationVisitor v = av.visitArray(name);
            if (v != null) {
               List array = (List)value;

               for(int j = 0; j < array.size(); ++j) {
                  accept(v, (String)null, array.get(j));
               }

               v.visitEnd();
            }
         } else {
            av.visit(name, value);
         }
      }

   }
}
