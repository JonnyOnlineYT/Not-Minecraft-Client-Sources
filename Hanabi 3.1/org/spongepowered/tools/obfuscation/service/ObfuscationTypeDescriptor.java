package org.spongepowered.tools.obfuscation.service;

public class ObfuscationTypeDescriptor {
   private final String key;
   private final String inputFileArgName;
   private final String extraInputFilesArgName;
   private final String outFileArgName;
   private final Class environmentType;

   public ObfuscationTypeDescriptor(String key, String inputFileArgName, String outFileArgName, Class environmentType) {
      this(key, inputFileArgName, (String)null, outFileArgName, environmentType);
   }

   public ObfuscationTypeDescriptor(String key, String inputFileArgName, String extraInputFilesArgName, String outFileArgName, Class environmentType) {
      this.key = key;
      this.inputFileArgName = inputFileArgName;
      this.extraInputFilesArgName = extraInputFilesArgName;
      this.outFileArgName = outFileArgName;
      this.environmentType = environmentType;
   }

   public final String getKey() {
      return this.key;
   }

   public String getInputFileOption() {
      return this.inputFileArgName;
   }

   public String getExtraInputFilesOption() {
      return this.extraInputFilesArgName;
   }

   public String getOutputFileOption() {
      return this.outFileArgName;
   }

   public Class getEnvironmentType() {
      return this.environmentType;
   }
}
