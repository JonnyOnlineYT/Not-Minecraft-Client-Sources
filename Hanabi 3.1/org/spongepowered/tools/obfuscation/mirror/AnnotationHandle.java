package org.spongepowered.tools.obfuscation.mirror;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public final class AnnotationHandle {
   public static final AnnotationHandle MISSING = new AnnotationHandle((AnnotationMirror)null);
   private final AnnotationMirror annotation;

   private AnnotationHandle(AnnotationMirror annotation) {
      this.annotation = annotation;
   }

   public AnnotationMirror asMirror() {
      return this.annotation;
   }

   public boolean exists() {
      return this.annotation != null;
   }

   public String toString() {
      return this.annotation == null ? "@{UnknownAnnotation}" : "@" + this.annotation.getAnnotationType().asElement().getSimpleName();
   }

   public Object getValue(String key, Object defaultValue) {
      if (this.annotation == null) {
         return defaultValue;
      } else {
         AnnotationValue value = this.getAnnotationValue(key);
         if (defaultValue instanceof Enum && value != null) {
            VariableElement varValue = (VariableElement)value.getValue();
            return varValue == null ? defaultValue : Enum.valueOf(defaultValue.getClass(), varValue.getSimpleName().toString());
         } else {
            return value != null ? value.getValue() : defaultValue;
         }
      }
   }

   public Object getValue() {
      return this.getValue("value", (Object)null);
   }

   public Object getValue(String key) {
      return this.getValue(key, (Object)null);
   }

   public boolean getBoolean(String key, boolean defaultValue) {
      return (Boolean)this.getValue(key, defaultValue);
   }

   public AnnotationHandle getAnnotation(String key) {
      Object value = this.getValue(key);
      if (value instanceof AnnotationMirror) {
         return of((AnnotationMirror)value);
      } else {
         if (value instanceof AnnotationValue) {
            Object mirror = ((AnnotationValue)value).getValue();
            if (mirror instanceof AnnotationMirror) {
               return of((AnnotationMirror)mirror);
            }
         }

         return null;
      }
   }

   public List getList() {
      return this.getList("value");
   }

   public List getList(String key) {
      List list = (List)this.getValue(key, Collections.emptyList());
      return unwrapAnnotationValueList(list);
   }

   public List getAnnotationList(String key) {
      Object val = this.getValue(key, (Object)null);
      if (val == null) {
         return Collections.emptyList();
      } else if (val instanceof AnnotationMirror) {
         return ImmutableList.of(of((AnnotationMirror)val));
      } else {
         List list = (List)val;
         List annotations = new ArrayList(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            AnnotationValue value = (AnnotationValue)var5.next();
            annotations.add(new AnnotationHandle((AnnotationMirror)value.getValue()));
         }

         return Collections.unmodifiableList(annotations);
      }
   }

   protected AnnotationValue getAnnotationValue(String key) {
      Iterator var2 = this.annotation.getElementValues().keySet().iterator();

      ExecutableElement elem;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         elem = (ExecutableElement)var2.next();
      } while(!elem.getSimpleName().contentEquals(key));

      return (AnnotationValue)this.annotation.getElementValues().get(elem);
   }

   protected static List unwrapAnnotationValueList(List list) {
      if (list == null) {
         return Collections.emptyList();
      } else {
         List unfolded = new ArrayList(list.size());
         Iterator var2 = list.iterator();

         while(var2.hasNext()) {
            AnnotationValue value = (AnnotationValue)var2.next();
            unfolded.add(value.getValue());
         }

         return unfolded;
      }
   }

   protected static AnnotationMirror getAnnotation(Element elem, Class annotationClass) {
      if (elem == null) {
         return null;
      } else {
         List annotations = elem.getAnnotationMirrors();
         if (annotations == null) {
            return null;
         } else {
            Iterator var3 = annotations.iterator();

            while(var3.hasNext()) {
               AnnotationMirror annotation = (AnnotationMirror)var3.next();
               Element element = annotation.getAnnotationType().asElement();
               if (element instanceof TypeElement) {
                  TypeElement annotationElement = (TypeElement)element;
                  if (annotationElement.getQualifiedName().contentEquals(annotationClass.getName())) {
                     return annotation;
                  }
               }
            }

            return null;
         }
      }
   }

   public static AnnotationHandle of(AnnotationMirror annotation) {
      return new AnnotationHandle(annotation);
   }

   public static AnnotationHandle of(Element elem, Class annotationClass) {
      return new AnnotationHandle(getAnnotation(elem, annotationClass));
   }
}
