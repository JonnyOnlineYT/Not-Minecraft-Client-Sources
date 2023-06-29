package org.spongepowered.asm.mixin.injection.struct;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionPointException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class InjectionPointData {
   private static final Pattern AT_PATTERN = createPattern();
   private final Map args = new HashMap();
   private final IMixinContext context;
   private final MethodNode method;
   private final AnnotationNode parent;
   private final String at;
   private final String type;
   private final InjectionPoint.Selector selector;
   private final String target;
   private final String slice;
   private final int ordinal;
   private final int opcode;
   private final String id;

   public InjectionPointData(IMixinContext context, MethodNode method, AnnotationNode parent, String at, List args, String target, String slice, int ordinal, int opcode, String id) {
      this.context = context;
      this.method = method;
      this.parent = parent;
      this.at = at;
      this.target = target;
      this.slice = Strings.nullToEmpty(slice);
      this.ordinal = Math.max(-1, ordinal);
      this.opcode = opcode;
      this.id = id;
      this.parseArgs(args);
      this.args.put("target", target);
      this.args.put("ordinal", String.valueOf(ordinal));
      this.args.put("opcode", String.valueOf(opcode));
      Matcher matcher = AT_PATTERN.matcher(at);
      this.type = parseType(matcher, at);
      this.selector = parseSelector(matcher);
   }

   private void parseArgs(List args) {
      if (args != null) {
         Iterator var2 = args.iterator();

         while(var2.hasNext()) {
            String arg = (String)var2.next();
            if (arg != null) {
               int eqPos = arg.indexOf(61);
               if (eqPos > -1) {
                  this.args.put(arg.substring(0, eqPos), arg.substring(eqPos + 1));
               } else {
                  this.args.put(arg, "");
               }
            }
         }

      }
   }

   public String getAt() {
      return this.at;
   }

   public String getType() {
      return this.type;
   }

   public InjectionPoint.Selector getSelector() {
      return this.selector;
   }

   public IMixinContext getContext() {
      return this.context;
   }

   public MethodNode getMethod() {
      return this.method;
   }

   public Type getMethodReturnType() {
      return Type.getReturnType(this.method.desc);
   }

   public AnnotationNode getParent() {
      return this.parent;
   }

   public String getSlice() {
      return this.slice;
   }

   public LocalVariableDiscriminator getLocalVariableDiscriminator() {
      return LocalVariableDiscriminator.parse(this.parent);
   }

   public String get(String key, String defaultValue) {
      String value = (String)this.args.get(key);
      return value != null ? value : defaultValue;
   }

   public int get(String key, int defaultValue) {
      return parseInt(this.get(key, String.valueOf(defaultValue)), defaultValue);
   }

   public boolean get(String key, boolean defaultValue) {
      return parseBoolean(this.get(key, String.valueOf(defaultValue)), defaultValue);
   }

   public MemberInfo get(String key) {
      try {
         return MemberInfo.parseAndValidate(this.get(key, ""), this.context);
      } catch (InvalidMemberDescriptorException var3) {
         throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\").%s descriptor \"%s\" on %s", new Object[]{this.at, key, this.target, InjectionInfo.describeInjector(this.context, this.parent, this.method)});
      }
   }

   public MemberInfo getTarget() {
      try {
         return MemberInfo.parseAndValidate(this.target, this.context);
      } catch (InvalidMemberDescriptorException var2) {
         throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\") descriptor \"%s\" on %s", new Object[]{this.at, this.target, InjectionInfo.describeInjector(this.context, this.parent, this.method)});
      }
   }

   public int getOrdinal() {
      return this.ordinal;
   }

   public int getOpcode() {
      return this.opcode;
   }

   public int getOpcode(int defaultOpcode) {
      return this.opcode > 0 ? this.opcode : defaultOpcode;
   }

   public int getOpcode(int defaultOpcode, int... validOpcodes) {
      int[] var3 = validOpcodes;
      int var4 = validOpcodes.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int validOpcode = var3[var5];
         if (this.opcode == validOpcode) {
            return this.opcode;
         }
      }

      return defaultOpcode;
   }

   public String getId() {
      return this.id;
   }

   public String toString() {
      return this.type;
   }

   private static Pattern createPattern() {
      return Pattern.compile(String.format("^([^:]+):?(%s)?$", Joiner.on('|').join(InjectionPoint.Selector.values())));
   }

   public static String parseType(String at) {
      Matcher matcher = AT_PATTERN.matcher(at);
      return parseType(matcher, at);
   }

   private static String parseType(Matcher matcher, String at) {
      return matcher.matches() ? matcher.group(1) : at;
   }

   private static InjectionPoint.Selector parseSelector(Matcher matcher) {
      return matcher.matches() && matcher.group(2) != null ? InjectionPoint.Selector.valueOf(matcher.group(2)) : InjectionPoint.Selector.DEFAULT;
   }

   private static int parseInt(String string, int defaultValue) {
      try {
         return Integer.parseInt(string);
      } catch (Exception var3) {
         return defaultValue;
      }
   }

   private static boolean parseBoolean(String string, boolean defaultValue) {
      try {
         return Boolean.parseBoolean(string);
      } catch (Exception var3) {
         return defaultValue;
      }
   }
}
