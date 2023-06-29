package org.spongepowered.asm.lib;

public class Attribute {
   public final String type;
   byte[] value;
   Attribute next;

   protected Attribute(String type) {
      this.type = type;
   }

   public boolean isUnknown() {
      return true;
   }

   public boolean isCodeAttribute() {
      return false;
   }

   protected Label[] getLabels() {
      return null;
   }

   protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) {
      Attribute attr = new Attribute(this.type);
      attr.value = new byte[len];
      System.arraycopy(cr.b, off, attr.value, 0, len);
      return attr;
   }

   protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
      ByteVector v = new ByteVector();
      v.data = this.value;
      v.length = this.value.length;
      return v;
   }

   final int getCount() {
      int count = 0;

      for(Attribute attr = this; attr != null; attr = attr.next) {
         ++count;
      }

      return count;
   }

   final int getSize(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
      Attribute attr = this;

      int size;
      for(size = 0; attr != null; attr = attr.next) {
         cw.newUTF8(attr.type);
         size += attr.write(cw, code, len, maxStack, maxLocals).length + 6;
      }

      return size;
   }

   final void put(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals, ByteVector out) {
      for(Attribute attr = this; attr != null; attr = attr.next) {
         ByteVector b = attr.write(cw, code, len, maxStack, maxLocals);
         out.putShort(cw.newUTF8(attr.type)).putInt(b.length);
         out.putByteArray(b.data, 0, b.length);
      }

   }
}
