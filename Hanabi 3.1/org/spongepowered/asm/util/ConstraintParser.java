package org.spongepowered.asm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.util.throwables.ConstraintViolationException;
import org.spongepowered.asm.util.throwables.InvalidConstraintException;

public final class ConstraintParser {
   private ConstraintParser() {
   }

   public static Constraint parse(String expr) {
      if (expr != null && expr.length() != 0) {
         String[] exprs = expr.replaceAll("\\s", "").toUpperCase().split(";");
         Constraint head = null;
         String[] var3 = exprs;
         int var4 = exprs.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String subExpr = var3[var5];
            Constraint next = new Constraint(subExpr);
            if (head == null) {
               head = next;
            } else {
               head.append(next);
            }
         }

         return head != null ? head : ConstraintParser.Constraint.NONE;
      } else {
         return ConstraintParser.Constraint.NONE;
      }
   }

   public static Constraint parse(AnnotationNode annotation) {
      String constraints = (String)Annotations.getValue(annotation, "constraints", (Object)"");
      return parse(constraints);
   }

   public static class Constraint {
      public static final Constraint NONE = new Constraint();
      private static final Pattern pattern = Pattern.compile("^([A-Z0-9\\-_\\.]+)\\((?:(<|<=|>|>=|=)?([0-9]+)(<|(-)([0-9]+)?|>|(\\+)([0-9]+)?)?)?\\)$");
      private final String expr;
      private String token;
      private String[] constraint;
      private int min = Integer.MIN_VALUE;
      private int max = Integer.MAX_VALUE;
      private Constraint next;

      Constraint(String expr) {
         this.expr = expr;
         Matcher matcher = pattern.matcher(expr);
         if (!matcher.matches()) {
            throw new InvalidConstraintException("Constraint syntax was invalid parsing: " + this.expr);
         } else {
            this.token = matcher.group(1);
            this.constraint = new String[]{matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), matcher.group(7), matcher.group(8)};
            this.parse();
         }
      }

      private Constraint() {
         this.expr = null;
         this.token = "*";
         this.constraint = new String[0];
      }

      private void parse() {
         if (this.has(1)) {
            this.max = this.min = this.val(1);
            boolean hasModifier = this.has(0);
            if (this.has(4)) {
               if (hasModifier) {
                  throw new InvalidConstraintException("Unexpected modifier '" + this.elem(0) + "' in " + this.expr + " parsing range");
               } else {
                  this.max = this.val(4);
                  if (this.max < this.min) {
                     throw new InvalidConstraintException("Invalid range specified '" + this.max + "' is less than " + this.min + " in " + this.expr);
                  }
               }
            } else if (this.has(6)) {
               if (hasModifier) {
                  throw new InvalidConstraintException("Unexpected modifier '" + this.elem(0) + "' in " + this.expr + " parsing range");
               } else {
                  this.max = this.min + this.val(6);
               }
            } else {
               String leading;
               if (hasModifier) {
                  if (this.has(3)) {
                     throw new InvalidConstraintException("Unexpected trailing modifier '" + this.elem(3) + "' in " + this.expr);
                  }

                  leading = this.elem(0);
                  if (">".equals(leading)) {
                     ++this.min;
                     this.max = Integer.MAX_VALUE;
                  } else if (">=".equals(leading)) {
                     this.max = Integer.MAX_VALUE;
                  } else if ("<".equals(leading)) {
                     this.max = --this.min;
                     this.min = Integer.MIN_VALUE;
                  } else if ("<=".equals(leading)) {
                     this.max = this.min;
                     this.min = Integer.MIN_VALUE;
                  }
               } else if (this.has(2)) {
                  leading = this.elem(2);
                  if ("<".equals(leading)) {
                     this.max = this.min;
                     this.min = Integer.MIN_VALUE;
                  } else {
                     this.max = Integer.MAX_VALUE;
                  }
               }

            }
         }
      }

      private boolean has(int index) {
         return this.constraint[index] != null;
      }

      private String elem(int index) {
         return this.constraint[index];
      }

      private int val(int index) {
         return this.constraint[index] != null ? Integer.parseInt(this.constraint[index]) : 0;
      }

      void append(Constraint next) {
         if (this.next != null) {
            this.next.append(next);
         } else {
            this.next = next;
         }
      }

      public String getToken() {
         return this.token;
      }

      public int getMin() {
         return this.min;
      }

      public int getMax() {
         return this.max;
      }

      public void check(ITokenProvider environment) throws ConstraintViolationException {
         if (this != NONE) {
            Integer value = environment.getToken(this.token);
            if (value == null) {
               throw new ConstraintViolationException("The token '" + this.token + "' could not be resolved in " + environment, this);
            }

            if (value < this.min) {
               throw new ConstraintViolationException("Token '" + this.token + "' has a value (" + value + ") which is less than the minimum value " + this.min + " in " + environment, this, value);
            }

            if (value > this.max) {
               throw new ConstraintViolationException("Token '" + this.token + "' has a value (" + value + ") which is greater than the maximum value " + this.max + " in " + environment, this, value);
            }
         }

         if (this.next != null) {
            this.next.check(environment);
         }

      }

      public String getRangeHumanReadable() {
         if (this.min == Integer.MIN_VALUE && this.max == Integer.MAX_VALUE) {
            return "ANY VALUE";
         } else if (this.min == Integer.MIN_VALUE) {
            return String.format("less than or equal to %d", this.max);
         } else if (this.max == Integer.MAX_VALUE) {
            return String.format("greater than or equal to %d", this.min);
         } else {
            return this.min == this.max ? String.format("%d", this.min) : String.format("between %d and %d", this.min, this.max);
         }
      }

      public String toString() {
         return String.format("Constraint(%s [%d-%d])", this.token, this.min, this.max);
      }
   }
}
