package org.spongepowered.asm.lib;

public class TypeReference {
   public static final int CLASS_TYPE_PARAMETER = 0;
   public static final int METHOD_TYPE_PARAMETER = 1;
   public static final int CLASS_EXTENDS = 16;
   public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
   public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
   public static final int FIELD = 19;
   public static final int METHOD_RETURN = 20;
   public static final int METHOD_RECEIVER = 21;
   public static final int METHOD_FORMAL_PARAMETER = 22;
   public static final int THROWS = 23;
   public static final int LOCAL_VARIABLE = 64;
   public static final int RESOURCE_VARIABLE = 65;
   public static final int EXCEPTION_PARAMETER = 66;
   public static final int INSTANCEOF = 67;
   public static final int NEW = 68;
   public static final int CONSTRUCTOR_REFERENCE = 69;
   public static final int METHOD_REFERENCE = 70;
   public static final int CAST = 71;
   public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
   public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
   public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
   public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
   private int value;

   public TypeReference(int typeRef) {
      this.value = typeRef;
   }

   public static TypeReference newTypeReference(int sort) {
      return new TypeReference(sort << 24);
   }

   public static TypeReference newTypeParameterReference(int sort, int paramIndex) {
      return new TypeReference(sort << 24 | paramIndex << 16);
   }

   public static TypeReference newTypeParameterBoundReference(int sort, int paramIndex, int boundIndex) {
      return new TypeReference(sort << 24 | paramIndex << 16 | boundIndex << 8);
   }

   public static TypeReference newSuperTypeReference(int itfIndex) {
      itfIndex &= 65535;
      return new TypeReference(268435456 | itfIndex << 8);
   }

   public static TypeReference newFormalParameterReference(int paramIndex) {
      return new TypeReference(369098752 | paramIndex << 16);
   }

   public static TypeReference newExceptionReference(int exceptionIndex) {
      return new TypeReference(385875968 | exceptionIndex << 8);
   }

   public static TypeReference newTryCatchReference(int tryCatchBlockIndex) {
      return new TypeReference(1107296256 | tryCatchBlockIndex << 8);
   }

   public static TypeReference newTypeArgumentReference(int sort, int argIndex) {
      return new TypeReference(sort << 24 | argIndex);
   }

   public int getSort() {
      return this.value >>> 24;
   }

   public int getTypeParameterIndex() {
      return (this.value & 16711680) >> 16;
   }

   public int getTypeParameterBoundIndex() {
      return (this.value & '\uff00') >> 8;
   }

   public int getSuperTypeIndex() {
      return (short)((this.value & 16776960) >> 8);
   }

   public int getFormalParameterIndex() {
      return (this.value & 16711680) >> 16;
   }

   public int getExceptionIndex() {
      return (this.value & 16776960) >> 8;
   }

   public int getTryCatchBlockIndex() {
      return (this.value & 16776960) >> 8;
   }

   public int getTypeArgumentIndex() {
      return this.value & 255;
   }

   public int getValue() {
      return this.value;
   }
}
