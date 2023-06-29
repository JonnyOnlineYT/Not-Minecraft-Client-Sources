package org.apache.commons.codec.language.bm;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.codec.Resources;

public class Languages {
   public static final String ANY = "any";
   private static final Map LANGUAGES = new EnumMap(NameType.class);
   private final Set languages;
   public static final LanguageSet NO_LANGUAGES;
   public static final LanguageSet ANY_LANGUAGE;

   public static Languages getInstance(NameType nameType) {
      return (Languages)LANGUAGES.get(nameType);
   }

   public static Languages getInstance(String languagesResourceName) {
      Set ls = new HashSet();
      Scanner lsScanner = new Scanner(Resources.getInputStream(languagesResourceName), "UTF-8");
      Throwable var3 = null;

      try {
         boolean inExtendedComment = false;

         while(lsScanner.hasNextLine()) {
            String line = lsScanner.nextLine().trim();
            if (inExtendedComment) {
               if (line.endsWith("*/")) {
                  inExtendedComment = false;
               }
            } else if (line.startsWith("/*")) {
               inExtendedComment = true;
            } else if (line.length() > 0) {
               ls.add(line);
            }
         }

         Languages var16 = new Languages(Collections.unmodifiableSet(ls));
         return var16;
      } catch (Throwable var14) {
         var3 = var14;
         throw var14;
      } finally {
         if (lsScanner != null) {
            if (var3 != null) {
               try {
                  lsScanner.close();
               } catch (Throwable var13) {
                  var3.addSuppressed(var13);
               }
            } else {
               lsScanner.close();
            }
         }

      }
   }

   private static String langResourceName(NameType nameType) {
      return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", nameType.getName());
   }

   private Languages(Set languages) {
      this.languages = languages;
   }

   public Set getLanguages() {
      return this.languages;
   }

   static {
      NameType[] var0 = NameType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         NameType s = var0[var2];
         LANGUAGES.put(s, getInstance(langResourceName(s)));
      }

      NO_LANGUAGES = new LanguageSet() {
         public boolean contains(String language) {
            return false;
         }

         public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the empty language set.");
         }

         public boolean isEmpty() {
            return true;
         }

         public boolean isSingleton() {
            return false;
         }

         public LanguageSet restrictTo(LanguageSet other) {
            return this;
         }

         public LanguageSet merge(LanguageSet other) {
            return other;
         }

         public String toString() {
            return "NO_LANGUAGES";
         }
      };
      ANY_LANGUAGE = new LanguageSet() {
         public boolean contains(String language) {
            return true;
         }

         public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the any language set.");
         }

         public boolean isEmpty() {
            return false;
         }

         public boolean isSingleton() {
            return false;
         }

         public LanguageSet restrictTo(LanguageSet other) {
            return other;
         }

         public LanguageSet merge(LanguageSet other) {
            return other;
         }

         public String toString() {
            return "ANY_LANGUAGE";
         }
      };
   }

   public static final class SomeLanguages extends LanguageSet {
      private final Set languages;

      private SomeLanguages(Set languages) {
         this.languages = Collections.unmodifiableSet(languages);
      }

      public boolean contains(String language) {
         return this.languages.contains(language);
      }

      public String getAny() {
         return (String)this.languages.iterator().next();
      }

      public Set getLanguages() {
         return this.languages;
      }

      public boolean isEmpty() {
         return this.languages.isEmpty();
      }

      public boolean isSingleton() {
         return this.languages.size() == 1;
      }

      public LanguageSet restrictTo(LanguageSet other) {
         if (other == Languages.NO_LANGUAGES) {
            return other;
         } else if (other == Languages.ANY_LANGUAGE) {
            return this;
         } else {
            SomeLanguages sl = (SomeLanguages)other;
            Set ls = new HashSet(Math.min(this.languages.size(), sl.languages.size()));
            Iterator var4 = this.languages.iterator();

            while(var4.hasNext()) {
               String lang = (String)var4.next();
               if (sl.languages.contains(lang)) {
                  ls.add(lang);
               }
            }

            return from(ls);
         }
      }

      public LanguageSet merge(LanguageSet other) {
         if (other == Languages.NO_LANGUAGES) {
            return this;
         } else if (other == Languages.ANY_LANGUAGE) {
            return other;
         } else {
            SomeLanguages sl = (SomeLanguages)other;
            Set ls = new HashSet(this.languages);
            Iterator var4 = sl.languages.iterator();

            while(var4.hasNext()) {
               String lang = (String)var4.next();
               ls.add(lang);
            }

            return from(ls);
         }
      }

      public String toString() {
         return "Languages(" + this.languages.toString() + ")";
      }

      // $FF: synthetic method
      SomeLanguages(Set x0, Object x1) {
         this(x0);
      }
   }

   public abstract static class LanguageSet {
      public static LanguageSet from(Set langs) {
         return (LanguageSet)(langs.isEmpty() ? Languages.NO_LANGUAGES : new SomeLanguages(langs));
      }

      public abstract boolean contains(String var1);

      public abstract String getAny();

      public abstract boolean isEmpty();

      public abstract boolean isSingleton();

      public abstract LanguageSet restrictTo(LanguageSet var1);

      abstract LanguageSet merge(LanguageSet var1);
   }
}
