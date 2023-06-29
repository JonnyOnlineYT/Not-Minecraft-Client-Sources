package org.spongepowered.asm.lib.signature;

public class SignatureReader {
   private final String signature;

   public SignatureReader(String signature) {
      this.signature = signature;
   }

   public void accept(SignatureVisitor v) {
      String signature = this.signature;
      int len = signature.length();
      int pos;
      if (signature.charAt(0) == '<') {
         pos = 2;

         char c;
         do {
            int end = signature.indexOf(58, pos);
            v.visitFormalTypeParameter(signature.substring(pos - 1, end));
            pos = end + 1;
            c = signature.charAt(pos);
            if (c == 'L' || c == '[' || c == 'T') {
               pos = parseType(signature, pos, v.visitClassBound());
            }

            while((c = signature.charAt(pos++)) == ':') {
               pos = parseType(signature, pos, v.visitInterfaceBound());
            }
         } while(c != '>');
      } else {
         pos = 0;
      }

      if (signature.charAt(pos) == '(') {
         ++pos;

         while(signature.charAt(pos) != ')') {
            pos = parseType(signature, pos, v.visitParameterType());
         }

         for(pos = parseType(signature, pos + 1, v.visitReturnType()); pos < len; pos = parseType(signature, pos + 1, v.visitExceptionType())) {
         }
      } else {
         for(pos = parseType(signature, pos, v.visitSuperclass()); pos < len; pos = parseType(signature, pos, v.visitInterface())) {
         }
      }

   }

   public void acceptType(SignatureVisitor v) {
      parseType(this.signature, 0, v);
   }

   private static int parseType(String signature, int pos, SignatureVisitor v) {
      char c;
      switch (c = signature.charAt(pos++)) {
         case 'B':
         case 'C':
         case 'D':
         case 'F':
         case 'I':
         case 'J':
         case 'S':
         case 'V':
         case 'Z':
            v.visitBaseType(c);
            return pos;
         case 'E':
         case 'G':
         case 'H':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'U':
         case 'W':
         case 'X':
         case 'Y':
         default:
            int start = pos;
            boolean visited = false;
            boolean inner = false;

            while(true) {
               label40:
               while(true) {
                  String name;
                  switch (c = signature.charAt(pos++)) {
                     case '.':
                     case ';':
                        if (!visited) {
                           name = signature.substring(start, pos - 1);
                           if (inner) {
                              v.visitInnerClassType(name);
                           } else {
                              v.visitClassType(name);
                           }
                        }

                        if (c == ';') {
                           v.visitEnd();
                           return pos;
                        }

                        start = pos;
                        visited = false;
                        inner = true;
                        break;
                     case '<':
                        name = signature.substring(start, pos - 1);
                        if (inner) {
                           v.visitInnerClassType(name);
                        } else {
                           v.visitClassType(name);
                        }

                        visited = true;

                        while(true) {
                           while(true) {
                              switch (c = signature.charAt(pos)) {
                                 case '*':
                                    ++pos;
                                    v.visitTypeArgument();
                                    break;
                                 case '+':
                                 case '-':
                                    pos = parseType(signature, pos + 1, v.visitTypeArgument(c));
                                    break;
                                 case '>':
                                    continue label40;
                                 default:
                                    pos = parseType(signature, pos, v.visitTypeArgument('='));
                              }
                           }
                        }
                  }
               }
            }
         case 'T':
            int end = signature.indexOf(59, pos);
            v.visitTypeVariable(signature.substring(pos, end));
            return end + 1;
         case '[':
            return parseType(signature, pos, v.visitArrayType());
      }
   }
}
