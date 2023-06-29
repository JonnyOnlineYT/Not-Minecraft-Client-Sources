package org.spongepowered.asm.mixin.injection.points;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;

@InjectionPoint.AtCode("CONSTANT")
public class BeforeConstant extends InjectionPoint {
   private static final Logger logger = LogManager.getLogger("mixin");
   private final int ordinal;
   private final boolean nullValue;
   private final Integer intValue;
   private final Float floatValue;
   private final Long longValue;
   private final Double doubleValue;
   private final String stringValue;
   private final Type typeValue;
   private final int[] expandOpcodes;
   private final boolean expand;
   private final String matchByType;
   private final boolean log;

   public BeforeConstant(IMixinContext context, AnnotationNode node, String returnType) {
      super((String)Annotations.getValue(node, "slice", (Object)""), InjectionPoint.Selector.DEFAULT, (String)null);
      Boolean empty = (Boolean)Annotations.getValue(node, "nullValue", (Object)((Boolean)null));
      this.ordinal = (Integer)Annotations.getValue(node, "ordinal", (int)-1);
      this.nullValue = empty != null && empty;
      this.intValue = (Integer)Annotations.getValue(node, "intValue", (Object)((Integer)null));
      this.floatValue = (Float)Annotations.getValue(node, "floatValue", (Object)((Float)null));
      this.longValue = (Long)Annotations.getValue(node, "longValue", (Object)((Long)null));
      this.doubleValue = (Double)Annotations.getValue(node, "doubleValue", (Object)((Double)null));
      this.stringValue = (String)Annotations.getValue(node, "stringValue", (Object)((String)null));
      this.typeValue = (Type)Annotations.getValue(node, "classValue", (Object)((Type)null));
      this.matchByType = this.validateDiscriminator(context, returnType, empty, "on @Constant annotation");
      this.expandOpcodes = this.parseExpandOpcodes(Annotations.getValue(node, "expandZeroConditions", true, Constant.Condition.class));
      this.expand = this.expandOpcodes.length > 0;
      this.log = (Boolean)Annotations.getValue(node, "log", (Object)Boolean.FALSE);
   }

   public BeforeConstant(InjectionPointData data) {
      super(data);
      String strNullValue = data.get("nullValue", (String)null);
      Boolean empty = strNullValue != null ? Boolean.parseBoolean(strNullValue) : null;
      this.ordinal = data.getOrdinal();
      this.nullValue = empty != null && empty;
      this.intValue = Ints.tryParse(data.get("intValue", ""));
      this.floatValue = Floats.tryParse(data.get("floatValue", ""));
      this.longValue = Longs.tryParse(data.get("longValue", ""));
      this.doubleValue = Doubles.tryParse(data.get("doubleValue", ""));
      this.stringValue = data.get("stringValue", (String)null);
      String strClassValue = data.get("classValue", (String)null);
      this.typeValue = strClassValue != null ? Type.getObjectType(strClassValue.replace('.', '/')) : null;
      this.matchByType = this.validateDiscriminator(data.getContext(), "V", empty, "in @At(\"CONSTANT\") args");
      if ("V".equals(this.matchByType)) {
         throw new InvalidInjectionException(data.getContext(), "No constant discriminator could be parsed in @At(\"CONSTANT\") args");
      } else {
         List conditions = new ArrayList();
         String strConditions = data.get("expandZeroConditions", "").toLowerCase();
         Constant.Condition[] var7 = Constant.Condition.values();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Constant.Condition condition = var7[var9];
            if (strConditions.contains(condition.name().toLowerCase())) {
               conditions.add(condition);
            }
         }

         this.expandOpcodes = this.parseExpandOpcodes(conditions);
         this.expand = this.expandOpcodes.length > 0;
         this.log = data.get("log", false);
      }
   }

   private String validateDiscriminator(IMixinContext context, String returnType, Boolean empty, String type) {
      int c = count(empty, this.intValue, this.floatValue, this.longValue, this.doubleValue, this.stringValue, this.typeValue);
      if (c == 1) {
         returnType = null;
      } else if (c > 1) {
         throw new InvalidInjectionException(context, "Conflicting constant discriminators specified " + type + " for " + context);
      }

      return returnType;
   }

   private int[] parseExpandOpcodes(List conditions) {
      Set opcodes = new HashSet();
      Iterator var3 = conditions.iterator();

      while(var3.hasNext()) {
         Constant.Condition condition = (Constant.Condition)var3.next();
         Constant.Condition actual = condition.getEquivalentCondition();
         int[] var6 = actual.getOpcodes();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            int opcode = var6[var8];
            opcodes.add(opcode);
         }
      }

      return Ints.toArray(opcodes);
   }

   public boolean find(String desc, InsnList insns, Collection nodes) {
      boolean found = false;
      this.log("BeforeConstant is searching for constants in method with descriptor {}", desc);
      ListIterator iter = insns.iterator();
      int ordinal = 0;
      int last = 0;

      while(iter.hasNext()) {
         AbstractInsnNode insn = (AbstractInsnNode)iter.next();
         boolean matchesInsn = this.expand ? this.matchesConditionalInsn(last, insn) : this.matchesConstantInsn(insn);
         if (matchesInsn) {
            this.log("    BeforeConstant found a matching constant{} at ordinal {}", this.matchByType != null ? " TYPE" : " value", ordinal);
            if (this.ordinal == -1 || this.ordinal == ordinal) {
               this.log("      BeforeConstant found {}", Bytecode.describeNode(insn).trim());
               nodes.add(insn);
               found = true;
            }

            ++ordinal;
         }

         if (!(insn instanceof LabelNode) && !(insn instanceof FrameNode)) {
            last = insn.getOpcode();
         }
      }

      return found;
   }

   private boolean matchesConditionalInsn(int last, AbstractInsnNode insn) {
      int[] var3 = this.expandOpcodes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int conditionalOpcode = var3[var5];
         int opcode = insn.getOpcode();
         if (opcode == conditionalOpcode) {
            if (last != 148 && last != 149 && last != 150 && last != 151 && last != 152) {
               this.log("  BeforeConstant found {} instruction", Bytecode.getOpcodeName(opcode));
               return true;
            }

            this.log("  BeforeConstant is ignoring {} following {}", Bytecode.getOpcodeName(opcode), Bytecode.getOpcodeName(last));
            return false;
         }
      }

      if (this.intValue != null && this.intValue == 0 && Bytecode.isConstant(insn)) {
         Object value = Bytecode.getConstant(insn);
         this.log("  BeforeConstant found INTEGER constant: value = {}", value);
         return value instanceof Integer && (Integer)value == 0;
      } else {
         return false;
      }
   }

   private boolean matchesConstantInsn(AbstractInsnNode insn) {
      if (!Bytecode.isConstant(insn)) {
         return false;
      } else {
         Object value = Bytecode.getConstant(insn);
         if (value == null) {
            this.log("  BeforeConstant found NULL constant: nullValue = {}", this.nullValue);
            return this.nullValue || "Ljava/lang/Object;".equals(this.matchByType);
         } else if (value instanceof Integer) {
            this.log("  BeforeConstant found INTEGER constant: value = {}, intValue = {}", value, this.intValue);
            return value.equals(this.intValue) || "I".equals(this.matchByType);
         } else if (value instanceof Float) {
            this.log("  BeforeConstant found FLOAT constant: value = {}, floatValue = {}", value, this.floatValue);
            return value.equals(this.floatValue) || "F".equals(this.matchByType);
         } else if (value instanceof Long) {
            this.log("  BeforeConstant found LONG constant: value = {}, longValue = {}", value, this.longValue);
            return value.equals(this.longValue) || "J".equals(this.matchByType);
         } else if (value instanceof Double) {
            this.log("  BeforeConstant found DOUBLE constant: value = {}, doubleValue = {}", value, this.doubleValue);
            return value.equals(this.doubleValue) || "D".equals(this.matchByType);
         } else if (value instanceof String) {
            this.log("  BeforeConstant found STRING constant: value = {}, stringValue = {}", value, this.stringValue);
            return value.equals(this.stringValue) || "Ljava/lang/String;".equals(this.matchByType);
         } else if (!(value instanceof Type)) {
            return false;
         } else {
            this.log("  BeforeConstant found CLASS constant: value = {}, typeValue = {}", value, this.typeValue);
            return value.equals(this.typeValue) || "Ljava/lang/Class;".equals(this.matchByType);
         }
      }
   }

   protected void log(String message, Object... params) {
      if (this.log) {
         logger.info(message, params);
      }

   }

   private static int count(Object... values) {
      int counter = 0;
      Object[] var2 = values;
      int var3 = values.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object value = var2[var4];
         if (value != null) {
            ++counter;
         }
      }

      return counter;
   }
}
