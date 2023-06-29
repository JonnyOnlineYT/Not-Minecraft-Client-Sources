package org.apache.commons.codec.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.Resources;
import org.apache.commons.codec.StringEncoder;

public class DaitchMokotoffSoundex implements StringEncoder {
   private static final String COMMENT = "//";
   private static final String DOUBLE_QUOTE = "\"";
   private static final String MULTILINE_COMMENT_END = "*/";
   private static final String MULTILINE_COMMENT_START = "/*";
   private static final String RESOURCE_FILE = "org/apache/commons/codec/language/dmrules.txt";
   private static final int MAX_LENGTH = 6;
   private static final Map RULES = new HashMap();
   private static final Map FOLDINGS = new HashMap();
   private final boolean folding;

   private static void parseRules(Scanner scanner, String location, Map ruleMapping, Map asciiFoldings) {
      int currentLine = 0;
      boolean inMultilineComment = false;

      while(true) {
         while(true) {
            String rawLine;
            String line;
            label56:
            do {
               while(true) {
                  while(scanner.hasNextLine()) {
                     ++currentLine;
                     rawLine = scanner.nextLine();
                     line = rawLine;
                     if (!inMultilineComment) {
                        if (!rawLine.startsWith("/*")) {
                           int cmtI = rawLine.indexOf("//");
                           if (cmtI >= 0) {
                              line = rawLine.substring(0, cmtI);
                           }

                           line = line.trim();
                           continue label56;
                        }

                        inMultilineComment = true;
                     } else if (rawLine.endsWith("*/")) {
                        inMultilineComment = false;
                     }
                  }

                  return;
               }
            } while(line.length() == 0);

            String[] parts;
            String leftCharacter;
            String rightCharacter;
            if (line.contains("=")) {
               parts = line.split("=");
               if (parts.length != 2) {
                  throw new IllegalArgumentException("Malformed folding statement split into " + parts.length + " parts: " + rawLine + " in " + location);
               }

               leftCharacter = parts[0];
               rightCharacter = parts[1];
               if (leftCharacter.length() != 1 || rightCharacter.length() != 1) {
                  throw new IllegalArgumentException("Malformed folding statement - patterns are not single characters: " + rawLine + " in " + location);
               }

               asciiFoldings.put(leftCharacter.charAt(0), rightCharacter.charAt(0));
            } else {
               parts = line.split("\\s+");
               if (parts.length != 4) {
                  throw new IllegalArgumentException("Malformed rule statement split into " + parts.length + " parts: " + rawLine + " in " + location);
               }

               try {
                  leftCharacter = stripQuotes(parts[0]);
                  rightCharacter = stripQuotes(parts[1]);
                  String replacement2 = stripQuotes(parts[2]);
                  String replacement3 = stripQuotes(parts[3]);
                  Rule r = new Rule(leftCharacter, rightCharacter, replacement2, replacement3);
                  char patternKey = r.pattern.charAt(0);
                  List rules = (List)ruleMapping.get(patternKey);
                  if (rules == null) {
                     rules = new ArrayList();
                     ruleMapping.put(patternKey, rules);
                  }

                  ((List)rules).add(r);
               } catch (IllegalArgumentException var17) {
                  throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, var17);
               }
            }
         }
      }
   }

   private static String stripQuotes(String str) {
      if (str.startsWith("\"")) {
         str = str.substring(1);
      }

      if (str.endsWith("\"")) {
         str = str.substring(0, str.length() - 1);
      }

      return str;
   }

   public DaitchMokotoffSoundex() {
      this(true);
   }

   public DaitchMokotoffSoundex(boolean folding) {
      this.folding = folding;
   }

   private String cleanup(String input) {
      StringBuilder sb = new StringBuilder();
      char[] var3 = input.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char ch = var3[var5];
         if (!Character.isWhitespace(ch)) {
            ch = Character.toLowerCase(ch);
            if (this.folding && FOLDINGS.containsKey(ch)) {
               ch = (Character)FOLDINGS.get(ch);
            }

            sb.append(ch);
         }
      }

      return sb.toString();
   }

   public Object encode(Object obj) throws EncoderException {
      if (!(obj instanceof String)) {
         throw new EncoderException("Parameter supplied to DaitchMokotoffSoundex encode is not of type java.lang.String");
      } else {
         return this.encode((String)obj);
      }
   }

   public String encode(String source) {
      return source == null ? null : this.soundex(source, false)[0];
   }

   public String soundex(String source) {
      String[] branches = this.soundex(source, true);
      StringBuilder sb = new StringBuilder();
      int index = 0;
      String[] var5 = branches;
      int var6 = branches.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String branch = var5[var7];
         sb.append(branch);
         ++index;
         if (index < branches.length) {
            sb.append('|');
         }
      }

      return sb.toString();
   }

   private String[] soundex(String source, boolean branching) {
      if (source == null) {
         return null;
      } else {
         String input = this.cleanup(source);
         Set currentBranches = new LinkedHashSet();
         currentBranches.add(new Branch());
         char lastChar = 0;

         int ch;
         for(int index = 0; index < input.length(); ++index) {
            ch = input.charAt(index);
            if (!Character.isWhitespace((char)ch)) {
               String inputContext = input.substring(index);
               List rules = (List)RULES.get(Character.valueOf((char)ch));
               if (rules != null) {
                  List nextBranches = branching ? new ArrayList() : Collections.emptyList();
                  Iterator var11 = rules.iterator();

                  while(var11.hasNext()) {
                     Rule rule = (Rule)var11.next();
                     if (rule.matches(inputContext)) {
                        if (branching) {
                           ((List)nextBranches).clear();
                        }

                        String[] replacements = rule.getReplacements(inputContext, lastChar == 0);
                        boolean branchingRequired = replacements.length > 1 && branching;
                        Iterator var15 = currentBranches.iterator();

                        while(var15.hasNext()) {
                           Branch branch = (Branch)var15.next();
                           String[] var17 = replacements;
                           int var18 = replacements.length;

                           for(int var19 = 0; var19 < var18; ++var19) {
                              String nextReplacement = var17[var19];
                              Branch nextBranch = branchingRequired ? branch.createBranch() : branch;
                              boolean force = lastChar == 'm' && ch == 110 || lastChar == 'n' && ch == 109;
                              nextBranch.processNextReplacement(nextReplacement, force);
                              if (!branching) {
                                 break;
                              }

                              ((List)nextBranches).add(nextBranch);
                           }
                        }

                        if (branching) {
                           currentBranches.clear();
                           currentBranches.addAll((Collection)nextBranches);
                        }

                        index += rule.getPatternLength() - 1;
                        break;
                     }
                  }

                  lastChar = (char)ch;
               }
            }
         }

         String[] result = new String[currentBranches.size()];
         ch = 0;

         Branch branch;
         for(Iterator var24 = currentBranches.iterator(); var24.hasNext(); result[ch++] = branch.toString()) {
            branch = (Branch)var24.next();
            branch.finish();
         }

         return result;
      }
   }

   static {
      Scanner scanner = new Scanner(Resources.getInputStream("org/apache/commons/codec/language/dmrules.txt"), "UTF-8");
      Throwable var1 = null;

      try {
         parseRules(scanner, "org/apache/commons/codec/language/dmrules.txt", RULES, FOLDINGS);
      } catch (Throwable var10) {
         var1 = var10;
         throw var10;
      } finally {
         if (scanner != null) {
            if (var1 != null) {
               try {
                  scanner.close();
               } catch (Throwable var9) {
                  var1.addSuppressed(var9);
               }
            } else {
               scanner.close();
            }
         }

      }

      Iterator var12 = RULES.entrySet().iterator();

      while(var12.hasNext()) {
         Map.Entry rule = (Map.Entry)var12.next();
         List ruleList = (List)rule.getValue();
         Collections.sort(ruleList, new Comparator() {
            public int compare(Rule rule1, Rule rule2) {
               return rule2.getPatternLength() - rule1.getPatternLength();
            }
         });
      }

   }

   private static final class Rule {
      private final String pattern;
      private final String[] replacementAtStart;
      private final String[] replacementBeforeVowel;
      private final String[] replacementDefault;

      protected Rule(String pattern, String replacementAtStart, String replacementBeforeVowel, String replacementDefault) {
         this.pattern = pattern;
         this.replacementAtStart = replacementAtStart.split("\\|");
         this.replacementBeforeVowel = replacementBeforeVowel.split("\\|");
         this.replacementDefault = replacementDefault.split("\\|");
      }

      public int getPatternLength() {
         return this.pattern.length();
      }

      public String[] getReplacements(String context, boolean atStart) {
         if (atStart) {
            return this.replacementAtStart;
         } else {
            int nextIndex = this.getPatternLength();
            boolean nextCharIsVowel = nextIndex < context.length() ? this.isVowel(context.charAt(nextIndex)) : false;
            return nextCharIsVowel ? this.replacementBeforeVowel : this.replacementDefault;
         }
      }

      private boolean isVowel(char ch) {
         return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u';
      }

      public boolean matches(String context) {
         return context.startsWith(this.pattern);
      }

      public String toString() {
         return String.format("%s=(%s,%s,%s)", this.pattern, Arrays.asList(this.replacementAtStart), Arrays.asList(this.replacementBeforeVowel), Arrays.asList(this.replacementDefault));
      }
   }

   private static final class Branch {
      private final StringBuilder builder;
      private String cachedString;
      private String lastReplacement;

      private Branch() {
         this.builder = new StringBuilder();
         this.lastReplacement = null;
         this.cachedString = null;
      }

      public Branch createBranch() {
         Branch branch = new Branch();
         branch.builder.append(this.toString());
         branch.lastReplacement = this.lastReplacement;
         return branch;
      }

      public boolean equals(Object other) {
         if (this == other) {
            return true;
         } else {
            return !(other instanceof Branch) ? false : this.toString().equals(((Branch)other).toString());
         }
      }

      public void finish() {
         while(this.builder.length() < 6) {
            this.builder.append('0');
            this.cachedString = null;
         }

      }

      public int hashCode() {
         return this.toString().hashCode();
      }

      public void processNextReplacement(String replacement, boolean forceAppend) {
         boolean append = this.lastReplacement == null || !this.lastReplacement.endsWith(replacement) || forceAppend;
         if (append && this.builder.length() < 6) {
            this.builder.append(replacement);
            if (this.builder.length() > 6) {
               this.builder.delete(6, this.builder.length());
            }

            this.cachedString = null;
         }

         this.lastReplacement = replacement;
      }

      public String toString() {
         if (this.cachedString == null) {
            this.cachedString = this.builder.toString();
         }

         return this.cachedString;
      }

      // $FF: synthetic method
      Branch(Object x0) {
         this();
      }
   }
}
