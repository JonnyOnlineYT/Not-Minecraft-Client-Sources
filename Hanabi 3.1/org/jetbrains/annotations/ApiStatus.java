package org.jetbrains.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class ApiStatus {
   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.TYPE, ElementType.METHOD})
   public @interface OverrideOnly {
   }

   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.TYPE, ElementType.METHOD})
   public @interface NonExtendable {
   }

   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
   public @interface AvailableSince {
      String value();
   }

   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
   public @interface ScheduledForRemoval {
      String inVersion() default "";
   }

   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
   public @interface Internal {
   }

   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
   public @interface Experimental {
   }
}
