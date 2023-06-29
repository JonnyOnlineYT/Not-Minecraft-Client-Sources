package org.spongepowered.asm.lib;

final class AnnotationWriter extends AnnotationVisitor {
   private final ClassWriter cw;
   private int size;
   private final boolean named;
   private final ByteVector bv;
   private final ByteVector parent;
   private final int offset;
   AnnotationWriter next;
   AnnotationWriter prev;

   AnnotationWriter(ClassWriter cw, boolean named, ByteVector bv, ByteVector parent, int offset) {
      super(327680);
      this.cw = cw;
      this.named = named;
      this.bv = bv;
      this.parent = parent;
      this.offset = offset;
   }

   public void visit(String name, Object value) {
      ++this.size;
      if (this.named) {
         this.bv.putShort(this.cw.newUTF8(name));
      }

      if (value instanceof String) {
         this.bv.put12(115, this.cw.newUTF8((String)value));
      } else if (value instanceof Byte) {
         this.bv.put12(66, this.cw.newInteger((Byte)value).index);
      } else if (value instanceof Boolean) {
         int v = (Boolean)value ? 1 : 0;
         this.bv.put12(90, this.cw.newInteger(v).index);
      } else if (value instanceof Character) {
         this.bv.put12(67, this.cw.newInteger((Character)value).index);
      } else if (value instanceof Short) {
         this.bv.put12(83, this.cw.newInteger((Short)value).index);
      } else if (value instanceof Type) {
         this.bv.put12(99, this.cw.newUTF8(((Type)value).getDescriptor()));
      } else {
         int i;
         if (value instanceof byte[]) {
            byte[] v = (byte[])((byte[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(66, this.cw.newInteger(v[i]).index);
            }
         } else if (value instanceof boolean[]) {
            boolean[] v = (boolean[])((boolean[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(90, this.cw.newInteger(v[i] ? 1 : 0).index);
            }
         } else if (value instanceof short[]) {
            short[] v = (short[])((short[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(83, this.cw.newInteger(v[i]).index);
            }
         } else if (value instanceof char[]) {
            char[] v = (char[])((char[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(67, this.cw.newInteger(v[i]).index);
            }
         } else if (value instanceof int[]) {
            int[] v = (int[])((int[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(73, this.cw.newInteger(v[i]).index);
            }
         } else if (value instanceof long[]) {
            long[] v = (long[])((long[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(74, this.cw.newLong(v[i]).index);
            }
         } else if (value instanceof float[]) {
            float[] v = (float[])((float[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(70, this.cw.newFloat(v[i]).index);
            }
         } else if (value instanceof double[]) {
            double[] v = (double[])((double[])value);
            this.bv.put12(91, v.length);

            for(i = 0; i < v.length; ++i) {
               this.bv.put12(68, this.cw.newDouble(v[i]).index);
            }
         } else {
            Item i = this.cw.newConstItem(value);
            this.bv.put12(".s.IFJDCS".charAt(i.type), i.index);
         }
      }

   }

   public void visitEnum(String name, String desc, String value) {
      ++this.size;
      if (this.named) {
         this.bv.putShort(this.cw.newUTF8(name));
      }

      this.bv.put12(101, this.cw.newUTF8(desc)).putShort(this.cw.newUTF8(value));
   }

   public AnnotationVisitor visitAnnotation(String name, String desc) {
      ++this.size;
      if (this.named) {
         this.bv.putShort(this.cw.newUTF8(name));
      }

      this.bv.put12(64, this.cw.newUTF8(desc)).putShort(0);
      return new AnnotationWriter(this.cw, true, this.bv, this.bv, this.bv.length - 2);
   }

   public AnnotationVisitor visitArray(String name) {
      ++this.size;
      if (this.named) {
         this.bv.putShort(this.cw.newUTF8(name));
      }

      this.bv.put12(91, 0);
      return new AnnotationWriter(this.cw, false, this.bv, this.bv, this.bv.length - 2);
   }

   public void visitEnd() {
      if (this.parent != null) {
         byte[] data = this.parent.data;
         data[this.offset] = (byte)(this.size >>> 8);
         data[this.offset + 1] = (byte)this.size;
      }

   }

   int getSize() {
      int size = 0;

      for(AnnotationWriter aw = this; aw != null; aw = aw.next) {
         size += aw.bv.length;
      }

      return size;
   }

   void put(ByteVector out) {
      int n = 0;
      int size = 2;
      AnnotationWriter aw = this;

      AnnotationWriter last;
      for(last = null; aw != null; aw = aw.next) {
         ++n;
         size += aw.bv.length;
         aw.visitEnd();
         aw.prev = last;
         last = aw;
      }

      out.putInt(size);
      out.putShort(n);

      for(aw = last; aw != null; aw = aw.prev) {
         out.putByteArray(aw.bv.data, 0, aw.bv.length);
      }

   }

   static void put(AnnotationWriter[] panns, int off, ByteVector out) {
      int size = 1 + 2 * (panns.length - off);

      int i;
      for(i = off; i < panns.length; ++i) {
         size += panns[i] == null ? 0 : panns[i].getSize();
      }

      out.putInt(size).putByte(panns.length - off);

      for(i = off; i < panns.length; ++i) {
         AnnotationWriter aw = panns[i];
         AnnotationWriter last = null;

         int n;
         for(n = 0; aw != null; aw = aw.next) {
            ++n;
            aw.visitEnd();
            aw.prev = last;
            last = aw;
         }

         out.putShort(n);

         for(aw = last; aw != null; aw = aw.prev) {
            out.putByteArray(aw.bv.data, 0, aw.bv.length);
         }
      }

   }

   static void putTarget(int typeRef, TypePath typePath, ByteVector out) {
      switch (typeRef >>> 24) {
         case 0:
         case 1:
         case 22:
            out.putShort(typeRef >>> 16);
            break;
         case 19:
         case 20:
         case 21:
            out.putByte(typeRef >>> 24);
            break;
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
            out.putInt(typeRef);
            break;
         default:
            out.put12(typeRef >>> 24, (typeRef & 16776960) >> 8);
      }

      if (typePath == null) {
         out.putByte(0);
      } else {
         int length = typePath.b[typePath.offset] * 2 + 1;
         out.putByteArray(typePath.b, typePath.offset, length);
      }

   }
}
