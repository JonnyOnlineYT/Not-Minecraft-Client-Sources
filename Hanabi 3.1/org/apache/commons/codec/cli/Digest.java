package org.apache.commons.codec.cli;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class Digest {
   private final String algorithm;
   private final String[] args;
   private final String[] inputs;

   public static void main(String[] args) throws IOException {
      (new Digest(args)).run();
   }

   private Digest(String[] args) {
      if (args == null) {
         throw new IllegalArgumentException("args");
      } else if (args.length == 0) {
         throw new IllegalArgumentException(String.format("Usage: java %s [algorithm] [FILE|DIRECTORY|string] ...", Digest.class.getName()));
      } else {
         this.args = args;
         this.algorithm = args[0];
         if (args.length <= 1) {
            this.inputs = null;
         } else {
            this.inputs = new String[args.length - 1];
            System.arraycopy(args, 1, this.inputs, 0, this.inputs.length);
         }

      }
   }

   private void println(String prefix, byte[] digest) {
      this.println(prefix, digest, (String)null);
   }

   private void println(String prefix, byte[] digest, String fileName) {
      System.out.println(prefix + Hex.encodeHexString(digest) + (fileName != null ? "  " + fileName : ""));
   }

   private void run() throws IOException {
      if (!this.algorithm.equalsIgnoreCase("ALL") && !this.algorithm.equals("*")) {
         MessageDigest messageDigest = DigestUtils.getDigest(this.algorithm, (MessageDigest)null);
         if (messageDigest != null) {
            this.run("", messageDigest);
         } else {
            this.run("", DigestUtils.getDigest(this.algorithm.toUpperCase(Locale.ROOT)));
         }

      } else {
         this.run(MessageDigestAlgorithms.values());
      }
   }

   private void run(String[] digestAlgorithms) throws IOException {
      String[] var2 = digestAlgorithms;
      int var3 = digestAlgorithms.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String messageDigestAlgorithm = var2[var4];
         if (DigestUtils.isAvailable(messageDigestAlgorithm)) {
            this.run(messageDigestAlgorithm + " ", messageDigestAlgorithm);
         }
      }

   }

   private void run(String prefix, MessageDigest messageDigest) throws IOException {
      if (this.inputs == null) {
         this.println(prefix, DigestUtils.digest(messageDigest, System.in));
      } else {
         String[] var3 = this.inputs;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String source = var3[var5];
            File file = new File(source);
            if (file.isFile()) {
               this.println(prefix, DigestUtils.digest(messageDigest, file), source);
            } else if (file.isDirectory()) {
               File[] listFiles = file.listFiles();
               if (listFiles != null) {
                  this.run(prefix, messageDigest, listFiles);
               }
            } else {
               byte[] bytes = source.getBytes(Charset.defaultCharset());
               this.println(prefix, DigestUtils.digest(messageDigest, bytes));
            }
         }

      }
   }

   private void run(String prefix, MessageDigest messageDigest, File[] files) throws IOException {
      File[] var4 = files;
      int var5 = files.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         File file = var4[var6];
         if (file.isFile()) {
            this.println(prefix, DigestUtils.digest(messageDigest, file), file.getName());
         }
      }

   }

   private void run(String prefix, String messageDigestAlgorithm) throws IOException {
      this.run(prefix, DigestUtils.getDigest(messageDigestAlgorithm));
   }

   public String toString() {
      return String.format("%s %s", super.toString(), Arrays.toString(this.args));
   }
}
