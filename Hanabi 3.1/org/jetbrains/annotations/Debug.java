package org.jetbrains.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.intellij.lang.annotations.Language;

public final class Debug {
   @Target({ElementType.TYPE})
   @Retention(RetentionPolicy.CLASS)
   public @interface Renderer {
      @Language(
         value = "JAVA",
         prefix = "class Renderer{String $text(){return ",
         suffix = ";}}"
      )
      @NonNls String text() default "";

      @Language(
         value = "JAVA",
         prefix = "class Renderer{Object[] $childrenArray(){return ",
         suffix = ";}}"
      )
      @NonNls String childrenArray() default "";

      @Language(
         value = "JAVA",
         prefix = "class Renderer{boolean $hasChildren(){return ",
         suffix = ";}}"
      )
      @NonNls String hasChildren() default "";
   }
}
