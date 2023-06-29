package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.signature.SignatureVisitor;

public class CheckSignatureAdapter extends SignatureVisitor {
   public static final int CLASS_SIGNATURE = 0;
   public static final int METHOD_SIGNATURE = 1;
   public static final int TYPE_SIGNATURE = 2;
   private static final int EMPTY = 1;
   private static final int FORMAL = 2;
   private static final int BOUND = 4;
   private static final int SUPER = 8;
   private static final int PARAM = 16;
   private static final int RETURN = 32;
   private static final int SIMPLE_TYPE = 64;
   private static final int CLASS_TYPE = 128;
   private static final int END = 256;
   private final int type;
   private int state;
   private boolean canBeVoid;
   private final SignatureVisitor sv;

   public CheckSignatureAdapter(int type, SignatureVisitor sv) {
      this(327680, type, sv);
   }

   protected CheckSignatureAdapter(int api, int type, SignatureVisitor sv) {
      super(api);
      this.type = type;
      this.state = 1;
      this.sv = sv;
   }

   public void visitFormalTypeParameter(String name) {
      if (this.type != 2 && (this.state == 1 || this.state == 2 || this.state == 4)) {
         CheckMethodAdapter.checkIdentifier(name, "formal type parameter");
         this.state = 2;
         if (this.sv != null) {
            this.sv.visitFormalTypeParameter(name);
         }

      } else {
         throw new IllegalStateException();
      }
   }

   public SignatureVisitor visitClassBound() {
      if (this.state != 2) {
         throw new IllegalStateException();
      } else {
         this.state = 4;
         SignatureVisitor v = this.sv == null ? null : this.sv.visitClassBound();
         return new CheckSignatureAdapter(2, v);
      }
   }

   public SignatureVisitor visitInterfaceBound() {
      if (this.state != 2 && this.state != 4) {
         throw new IllegalArgumentException();
      } else {
         SignatureVisitor v = this.sv == null ? null : this.sv.visitInterfaceBound();
         return new CheckSignatureAdapter(2, v);
      }
   }

   public SignatureVisitor visitSuperclass() {
      if (this.type == 0 && (this.state & 7) != 0) {
         this.state = 8;
         SignatureVisitor v = this.sv == null ? null : this.sv.visitSuperclass();
         return new CheckSignatureAdapter(2, v);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public SignatureVisitor visitInterface() {
      if (this.state != 8) {
         throw new IllegalStateException();
      } else {
         SignatureVisitor v = this.sv == null ? null : this.sv.visitInterface();
         return new CheckSignatureAdapter(2, v);
      }
   }

   public SignatureVisitor visitParameterType() {
      if (this.type == 1 && (this.state & 23) != 0) {
         this.state = 16;
         SignatureVisitor v = this.sv == null ? null : this.sv.visitParameterType();
         return new CheckSignatureAdapter(2, v);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public SignatureVisitor visitReturnType() {
      if (this.type == 1 && (this.state & 23) != 0) {
         this.state = 32;
         SignatureVisitor v = this.sv == null ? null : this.sv.visitReturnType();
         CheckSignatureAdapter cv = new CheckSignatureAdapter(2, v);
         cv.canBeVoid = true;
         return cv;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public SignatureVisitor visitExceptionType() {
      if (this.state != 32) {
         throw new IllegalStateException();
      } else {
         SignatureVisitor v = this.sv == null ? null : this.sv.visitExceptionType();
         return new CheckSignatureAdapter(2, v);
      }
   }

   public void visitBaseType(char descriptor) {
      if (this.type == 2 && this.state == 1) {
         if (descriptor == 'V') {
            if (!this.canBeVoid) {
               throw new IllegalArgumentException();
            }
         } else if ("ZCBSIFJD".indexOf(descriptor) == -1) {
            throw new IllegalArgumentException();
         }

         this.state = 64;
         if (this.sv != null) {
            this.sv.visitBaseType(descriptor);
         }

      } else {
         throw new IllegalStateException();
      }
   }

   public void visitTypeVariable(String name) {
      if (this.type == 2 && this.state == 1) {
         CheckMethodAdapter.checkIdentifier(name, "type variable");
         this.state = 64;
         if (this.sv != null) {
            this.sv.visitTypeVariable(name);
         }

      } else {
         throw new IllegalStateException();
      }
   }

   public SignatureVisitor visitArrayType() {
      if (this.type == 2 && this.state == 1) {
         this.state = 64;
         SignatureVisitor v = this.sv == null ? null : this.sv.visitArrayType();
         return new CheckSignatureAdapter(2, v);
      } else {
         throw new IllegalStateException();
      }
   }

   public void visitClassType(String name) {
      if (this.type == 2 && this.state == 1) {
         CheckMethodAdapter.checkInternalName(name, "class name");
         this.state = 128;
         if (this.sv != null) {
            this.sv.visitClassType(name);
         }

      } else {
         throw new IllegalStateException();
      }
   }

   public void visitInnerClassType(String name) {
      if (this.state != 128) {
         throw new IllegalStateException();
      } else {
         CheckMethodAdapter.checkIdentifier(name, "inner class name");
         if (this.sv != null) {
            this.sv.visitInnerClassType(name);
         }

      }
   }

   public void visitTypeArgument() {
      if (this.state != 128) {
         throw new IllegalStateException();
      } else {
         if (this.sv != null) {
            this.sv.visitTypeArgument();
         }

      }
   }

   public SignatureVisitor visitTypeArgument(char wildcard) {
      if (this.state != 128) {
         throw new IllegalStateException();
      } else if ("+-=".indexOf(wildcard) == -1) {
         throw new IllegalArgumentException();
      } else {
         SignatureVisitor v = this.sv == null ? null : this.sv.visitTypeArgument(wildcard);
         return new CheckSignatureAdapter(2, v);
      }
   }

   public void visitEnd() {
      if (this.state != 128) {
         throw new IllegalStateException();
      } else {
         this.state = 256;
         if (this.sv != null) {
            this.sv.visitEnd();
         }

      }
   }
}
