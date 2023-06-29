package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.codec.Resources;

public class Lang {
   private static final Map Langs = new EnumMap(NameType.class);
   private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/%s_lang.txt";
   private final Languages languages;
   private final List rules;

   public static Lang instance(NameType nameType) {
      return (Lang)Langs.get(nameType);
   }

   public static Lang loadFromResource(String languageRulesResourceName, Languages languages) {
      List rules = new ArrayList();
      Scanner scanner = new Scanner(Resources.getInputStream(languageRulesResourceName), "UTF-8");
      Throwable var4 = null;

      try {
         boolean inExtendedComment = false;

         while(scanner.hasNextLine()) {
            String rawLine = scanner.nextLine();
            String line = rawLine;
            if (inExtendedComment) {
               if (rawLine.endsWith("*/")) {
                  inExtendedComment = false;
               }
            } else if (rawLine.startsWith("/*")) {
               inExtendedComment = true;
            } else {
               int cmtI = rawLine.indexOf("//");
               if (cmtI >= 0) {
                  line = rawLine.substring(0, cmtI);
               }

               line = line.trim();
               if (line.length() != 0) {
                  String[] parts = line.split("\\s+");
                  if (parts.length != 3) {
                     throw new IllegalArgumentException("Malformed line '" + rawLine + "' in language resource '" + languageRulesResourceName + "'");
                  }

                  Pattern pattern = Pattern.compile(parts[0]);
                  String[] langs = parts[1].split("\\+");
                  boolean accept = parts[2].equals("true");
                  rules.add(new LangRule(pattern, new HashSet(Arrays.asList(langs)), accept));
               }
            }
         }
      } catch (Throwable var20) {
         var4 = var20;
         throw var20;
      } finally {
         if (scanner != null) {
            if (var4 != null) {
               try {
                  scanner.close();
               } catch (Throwable var19) {
                  var4.addSuppressed(var19);
               }
            } else {
               scanner.close();
            }
         }

      }

      return new Lang(rules, languages);
   }

   private Lang(List rules, Languages languages) {
      this.rules = Collections.unmodifiableList(rules);
      this.languages = languages;
   }

   public String guessLanguage(String text) {
      Languages.LanguageSet ls = this.guessLanguages(text);
      return ls.isSingleton() ? ls.getAny() : "any";
   }

   public Languages.LanguageSet guessLanguages(String input) {
      String text = input.toLowerCase(Locale.ENGLISH);
      Set langs = new HashSet(this.languages.getLanguages());
      Iterator var4 = this.rules.iterator();

      while(var4.hasNext()) {
         LangRule rule = (LangRule)var4.next();
         if (rule.matches(text)) {
            if (rule.acceptOnMatch) {
               langs.retainAll(rule.languages);
            } else {
               langs.removeAll(rule.languages);
            }
         }
      }

      Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
      return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
   }

   static {
      NameType[] var0 = NameType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         NameType s = var0[var2];
         Langs.put(s, loadFromResource(String.format("org/apache/commons/codec/language/bm/%s_lang.txt", s.getName()), Languages.getInstance(s)));
      }

   }

   private static final class LangRule {
      private final boolean acceptOnMatch;
      private final Set languages;
      private final Pattern pattern;

      private LangRule(Pattern pattern, Set languages, boolean acceptOnMatch) {
         this.pattern = pattern;
         this.languages = languages;
         this.acceptOnMatch = acceptOnMatch;
      }

      public boolean matches(String txt) {
         return this.pattern.matcher(txt).find();
      }

      // $FF: synthetic method
      LangRule(Pattern x0, Set x1, boolean x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
