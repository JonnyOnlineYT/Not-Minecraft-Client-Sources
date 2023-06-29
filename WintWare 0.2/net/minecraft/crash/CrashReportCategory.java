package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class CrashReportCategory {
   private final CrashReport crashReport;
   private final String name;
   private final List<CrashReportCategory.Entry> children = Lists.newArrayList();
   private StackTraceElement[] stackTrace = new StackTraceElement[0];

   public CrashReportCategory(CrashReport report, String name) {
      this.crashReport = report;
      this.name = name;
   }

   public static String getCoordinateInfo(double x, double y, double z) {
      return String.format("%.2f,%.2f,%.2f - %s", x, y, z, getCoordinateInfo(new BlockPos(x, y, z)));
   }

   public static String getCoordinateInfo(BlockPos pos) {
      return getCoordinateInfo(pos.getX(), pos.getY(), pos.getZ());
   }

   public static String getCoordinateInfo(int x, int y, int z) {
      StringBuilder stringbuilder = new StringBuilder();

      try {
         stringbuilder.append(String.format("World: (%d,%d,%d)", x, y, z));
      } catch (Throwable var16) {
         stringbuilder.append("(Error finding world loc)");
      }

      stringbuilder.append(", ");

      int k2;
      int l2;
      int i3;
      int j3;
      int k3;
      int l3;
      int i4;
      int j4;
      int k4;
      try {
         k2 = x >> 4;
         l2 = z >> 4;
         i3 = x & 15;
         j3 = y >> 4;
         k3 = z & 15;
         l3 = k2 << 4;
         i4 = l2 << 4;
         j4 = (k2 + 1 << 4) - 1;
         k4 = (l2 + 1 << 4) - 1;
         stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", i3, j3, k3, k2, l2, l3, i4, j4, k4));
      } catch (Throwable var15) {
         stringbuilder.append("(Error finding chunk loc)");
      }

      stringbuilder.append(", ");

      try {
         k2 = x >> 9;
         l2 = z >> 9;
         i3 = k2 << 5;
         j3 = l2 << 5;
         k3 = (k2 + 1 << 5) - 1;
         l3 = (l2 + 1 << 5) - 1;
         i4 = k2 << 9;
         j4 = l2 << 9;
         k4 = (k2 + 1 << 9) - 1;
         int j2 = (l2 + 1 << 9) - 1;
         stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", k2, l2, i3, j3, k3, l3, i4, j4, k4, j2));
      } catch (Throwable var14) {
         stringbuilder.append("(Error finding world loc)");
      }

      return stringbuilder.toString();
   }

   public void setDetail(String nameIn, ICrashReportDetail<String> detail) {
      try {
         this.addCrashSection(nameIn, detail.call());
      } catch (Throwable var4) {
         this.addCrashSectionThrowable(nameIn, var4);
      }

   }

   public void addCrashSection(String sectionName, Object value) {
      this.children.add(new CrashReportCategory.Entry(sectionName, value));
   }

   public void addCrashSectionThrowable(String sectionName, Throwable throwable) {
      this.addCrashSection(sectionName, throwable);
   }

   public int getPrunedStackTrace(int size) {
      StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
      if (astacktraceelement.length <= 0) {
         return 0;
      } else {
         this.stackTrace = new StackTraceElement[astacktraceelement.length - 3 - size];
         System.arraycopy(astacktraceelement, 3 + size, this.stackTrace, 0, this.stackTrace.length);
         return this.stackTrace.length;
      }
   }

   public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement s1, StackTraceElement s2) {
      if (this.stackTrace.length != 0 && s1 != null) {
         StackTraceElement stacktraceelement = this.stackTrace[0];
         if (stacktraceelement.isNativeMethod() == s1.isNativeMethod() && stacktraceelement.getClassName().equals(s1.getClassName()) && stacktraceelement.getFileName().equals(s1.getFileName()) && stacktraceelement.getMethodName().equals(s1.getMethodName())) {
            if (s2 != null != this.stackTrace.length > 1) {
               return false;
            } else if (s2 != null && !this.stackTrace[1].equals(s2)) {
               return false;
            } else {
               this.stackTrace[0] = s1;
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void trimStackTraceEntriesFromBottom(int amount) {
      StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - amount];
      System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
      this.stackTrace = astacktraceelement;
   }

   public void appendToStringBuilder(StringBuilder builder) {
      builder.append("-- ").append(this.name).append(" --\n");
      builder.append("Details:");
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         CrashReportCategory.Entry crashreportcategory$entry = (CrashReportCategory.Entry)var2.next();
         builder.append("\n\t");
         builder.append(crashreportcategory$entry.getKey());
         builder.append(": ");
         builder.append(crashreportcategory$entry.getValue());
      }

      if (this.stackTrace != null && this.stackTrace.length > 0) {
         builder.append("\nStacktrace:");
         StackTraceElement[] var6 = this.stackTrace;
         int var7 = var6.length;

         for(int var4 = 0; var4 < var7; ++var4) {
            StackTraceElement stacktraceelement = var6[var4];
            builder.append("\n\tat ");
            builder.append(stacktraceelement);
         }
      }

   }

   public StackTraceElement[] getStackTrace() {
      return this.stackTrace;
   }

   public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final Block blockIn, final int blockData) {
      final int i = Block.getIdFromBlock(blockIn);
      category.setDetail("Block type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            try {
               return String.format("ID #%d (%s // %s)", i, blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
            } catch (Throwable var2) {
               return "ID #" + i;
            }
         }
      });
      category.setDetail("Block data value", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            if (blockData < 0) {
               return "Unknown? (Got " + blockData + ")";
            } else {
               String s = String.format("%4s", Integer.toBinaryString(blockData)).replace(" ", "0");
               return String.format("%1$d / 0x%1$X / 0b%2$s", blockData, s);
            }
         }
      });
      category.setDetail("Block location", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(pos);
         }
      });
   }

   public static void addBlockInfo(CrashReportCategory category, final BlockPos pos, final IBlockState state) {
      category.setDetail("Block", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return state.toString();
         }
      });
      category.setDetail("Block location", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(pos);
         }
      });
   }

   static class Entry {
      private final String key;
      private final String value;

      public Entry(String key, Object value) {
         this.key = key;
         if (value == null) {
            this.value = "~~NULL~~";
         } else if (value instanceof Throwable) {
            Throwable throwable = (Throwable)value;
            this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
         } else {
            this.value = value.toString();
         }

      }

      public String getKey() {
         return this.key;
      }

      public String getValue() {
         return this.value;
      }
   }
}
