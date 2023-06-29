package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class PhoneticEngine {
   private static final Map NAME_PREFIXES = new EnumMap(NameType.class);
   private static final int DEFAULT_MAX_PHONEMES = 20;
   private final Lang lang;
   private final NameType nameType;
   private final RuleType ruleType;
   private final boolean concat;
   private final int maxPhonemes;

   private static String join(Iterable strings, String sep) {
      StringBuilder sb = new StringBuilder();
      Iterator si = strings.iterator();
      if (si.hasNext()) {
         sb.append((String)si.next());
      }

      while(si.hasNext()) {
         sb.append(sep).append((String)si.next());
      }

      return sb.toString();
   }

   public PhoneticEngine(NameType nameType, RuleType ruleType, boolean concat) {
      this(nameType, ruleType, concat, 20);
   }

   public PhoneticEngine(NameType nameType, RuleType ruleType, boolean concat, int maxPhonemes) {
      if (ruleType == RuleType.RULES) {
         throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
      } else {
         this.nameType = nameType;
         this.ruleType = ruleType;
         this.concat = concat;
         this.lang = Lang.instance(nameType);
         this.maxPhonemes = maxPhonemes;
      }
   }

   private PhonemeBuilder applyFinalRules(PhonemeBuilder phonemeBuilder, Map finalRules) {
      Objects.requireNonNull(finalRules, "finalRules");
      if (finalRules.isEmpty()) {
         return phonemeBuilder;
      } else {
         Map phonemes = new TreeMap(Rule.Phoneme.COMPARATOR);
         Iterator var4 = phonemeBuilder.getPhonemes().iterator();

         while(var4.hasNext()) {
            Rule.Phoneme phoneme = (Rule.Phoneme)var4.next();
            PhonemeBuilder subBuilder = PhoneticEngine.PhonemeBuilder.empty(phoneme.getLanguages());
            String phonemeText = phoneme.getPhonemeText().toString();

            RulesApplication rulesApplication;
            for(int i = 0; i < phonemeText.length(); i = rulesApplication.getI()) {
               rulesApplication = (new RulesApplication(finalRules, phonemeText, subBuilder, i, this.maxPhonemes)).invoke();
               boolean found = rulesApplication.isFound();
               subBuilder = rulesApplication.getPhonemeBuilder();
               if (!found) {
                  subBuilder.append(phonemeText.subSequence(i, i + 1));
               }
            }

            Iterator var13 = subBuilder.getPhonemes().iterator();

            while(var13.hasNext()) {
               Rule.Phoneme newPhoneme = (Rule.Phoneme)var13.next();
               if (phonemes.containsKey(newPhoneme)) {
                  Rule.Phoneme oldPhoneme = (Rule.Phoneme)phonemes.remove(newPhoneme);
                  Rule.Phoneme mergedPhoneme = oldPhoneme.mergeWithLanguage(newPhoneme.getLanguages());
                  phonemes.put(mergedPhoneme, mergedPhoneme);
               } else {
                  phonemes.put(newPhoneme, newPhoneme);
               }
            }
         }

         return new PhonemeBuilder(phonemes.keySet());
      }
   }

   public String encode(String input) {
      Languages.LanguageSet languageSet = this.lang.guessLanguages(input);
      return this.encode(input, languageSet);
   }

   public String encode(String input, Languages.LanguageSet languageSet) {
      Map rules = Rule.getInstanceMap(this.nameType, RuleType.RULES, languageSet);
      Map finalRules1 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
      Map finalRules2 = Rule.getInstanceMap(this.nameType, this.ruleType, languageSet);
      input = input.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
      String combined;
      if (this.nameType == NameType.GENERIC) {
         String l;
         if (input.length() >= 2 && input.substring(0, 2).equals("d'")) {
            String remainder = input.substring(2);
            l = "d" + remainder;
            return "(" + this.encode(remainder) + ")-(" + this.encode(l) + ")";
         }

         Iterator var6 = ((Set)NAME_PREFIXES.get(this.nameType)).iterator();

         while(var6.hasNext()) {
            l = (String)var6.next();
            if (input.startsWith(l + " ")) {
               String remainder = input.substring(l.length() + 1);
               combined = l + remainder;
               return "(" + this.encode(remainder) + ")-(" + this.encode(combined) + ")";
            }
         }
      }

      List words = Arrays.asList(input.split("\\s+"));
      List words2 = new ArrayList();
      switch (this.nameType) {
         case SEPHARDIC:
            Iterator var15 = words.iterator();

            while(var15.hasNext()) {
               combined = (String)var15.next();
               String[] parts = combined.split("'");
               String lastPart = parts[parts.length - 1];
               words2.add(lastPart);
            }

            words2.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
            break;
         case ASHKENAZI:
            words2.addAll(words);
            words2.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
            break;
         case GENERIC:
            words2.addAll(words);
            break;
         default:
            throw new IllegalStateException("Unreachable case: " + this.nameType);
      }

      if (this.concat) {
         input = join(words2, " ");
      } else {
         if (words2.size() != 1) {
            StringBuilder result = new StringBuilder();
            Iterator var19 = words2.iterator();

            while(var19.hasNext()) {
               String word = (String)var19.next();
               result.append("-").append(this.encode(word));
            }

            return result.substring(1);
         }

         input = (String)words.iterator().next();
      }

      PhonemeBuilder phonemeBuilder = PhoneticEngine.PhonemeBuilder.empty(languageSet);

      RulesApplication rulesApplication;
      for(int i = 0; i < input.length(); phonemeBuilder = rulesApplication.getPhonemeBuilder()) {
         rulesApplication = (new RulesApplication(rules, input, phonemeBuilder, i, this.maxPhonemes)).invoke();
         i = rulesApplication.getI();
      }

      phonemeBuilder = this.applyFinalRules(phonemeBuilder, finalRules1);
      phonemeBuilder = this.applyFinalRules(phonemeBuilder, finalRules2);
      return phonemeBuilder.makeString();
   }

   public Lang getLang() {
      return this.lang;
   }

   public NameType getNameType() {
      return this.nameType;
   }

   public RuleType getRuleType() {
      return this.ruleType;
   }

   public boolean isConcat() {
      return this.concat;
   }

   public int getMaxPhonemes() {
      return this.maxPhonemes;
   }

   static {
      NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList("bar", "ben", "da", "de", "van", "von"))));
      NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
      NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
   }

   private static final class RulesApplication {
      private final Map finalRules;
      private final CharSequence input;
      private final PhonemeBuilder phonemeBuilder;
      private int i;
      private final int maxPhonemes;
      private boolean found;

      public RulesApplication(Map finalRules, CharSequence input, PhonemeBuilder phonemeBuilder, int i, int maxPhonemes) {
         Objects.requireNonNull(finalRules, "finalRules");
         this.finalRules = finalRules;
         this.phonemeBuilder = phonemeBuilder;
         this.input = input;
         this.i = i;
         this.maxPhonemes = maxPhonemes;
      }

      public int getI() {
         return this.i;
      }

      public PhonemeBuilder getPhonemeBuilder() {
         return this.phonemeBuilder;
      }

      public RulesApplication invoke() {
         this.found = false;
         int patternLength = 1;
         List rules = (List)this.finalRules.get(this.input.subSequence(this.i, this.i + patternLength));
         if (rules != null) {
            Iterator var3 = rules.iterator();

            while(var3.hasNext()) {
               Rule rule = (Rule)var3.next();
               String pattern = rule.getPattern();
               patternLength = pattern.length();
               if (rule.patternAndContextMatches(this.input, this.i)) {
                  this.phonemeBuilder.apply(rule.getPhoneme(), this.maxPhonemes);
                  this.found = true;
                  break;
               }
            }
         }

         if (!this.found) {
            patternLength = 1;
         }

         this.i += patternLength;
         return this;
      }

      public boolean isFound() {
         return this.found;
      }
   }

   static final class PhonemeBuilder {
      private final Set phonemes;

      public static PhonemeBuilder empty(Languages.LanguageSet languages) {
         return new PhonemeBuilder(new Rule.Phoneme("", languages));
      }

      private PhonemeBuilder(Rule.Phoneme phoneme) {
         this.phonemes = new LinkedHashSet();
         this.phonemes.add(phoneme);
      }

      private PhonemeBuilder(Set phonemes) {
         this.phonemes = phonemes;
      }

      public void append(CharSequence str) {
         Iterator var2 = this.phonemes.iterator();

         while(var2.hasNext()) {
            Rule.Phoneme ph = (Rule.Phoneme)var2.next();
            ph.append(str);
         }

      }

      public void apply(Rule.PhonemeExpr phonemeExpr, int maxPhonemes) {
         Set newPhonemes = new LinkedHashSet(maxPhonemes);
         Iterator var4 = this.phonemes.iterator();

         label25:
         while(var4.hasNext()) {
            Rule.Phoneme left = (Rule.Phoneme)var4.next();
            Iterator var6 = phonemeExpr.getPhonemes().iterator();

            while(var6.hasNext()) {
               Rule.Phoneme right = (Rule.Phoneme)var6.next();
               Languages.LanguageSet languages = left.getLanguages().restrictTo(right.getLanguages());
               if (!languages.isEmpty()) {
                  Rule.Phoneme join = new Rule.Phoneme(left, right, languages);
                  if (newPhonemes.size() < maxPhonemes) {
                     newPhonemes.add(join);
                     if (newPhonemes.size() >= maxPhonemes) {
                        break label25;
                     }
                  }
               }
            }
         }

         this.phonemes.clear();
         this.phonemes.addAll(newPhonemes);
      }

      public Set getPhonemes() {
         return this.phonemes;
      }

      public String makeString() {
         StringBuilder sb = new StringBuilder();

         Rule.Phoneme ph;
         for(Iterator var2 = this.phonemes.iterator(); var2.hasNext(); sb.append(ph.getPhonemeText())) {
            ph = (Rule.Phoneme)var2.next();
            if (sb.length() > 0) {
               sb.append("|");
            }
         }

         return sb.toString();
      }

      // $FF: synthetic method
      PhonemeBuilder(Set x0, Object x1) {
         this(x0);
      }
   }
}
