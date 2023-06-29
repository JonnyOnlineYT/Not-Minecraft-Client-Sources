package net.minecraft.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase {
   private static final Logger field_191551_b = LogManager.getLogger();
   private static final Pattern field_193583_c = Pattern.compile("[A-Za-z0-9._+-]+");
   private final Map<String, NBTBase> tagMap = Maps.newHashMap();

   void write(DataOutput output) throws IOException {
      Iterator var2 = this.tagMap.keySet().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         NBTBase nbtbase = (NBTBase)this.tagMap.get(s);
         writeEntry(s, nbtbase, output);
      }

      output.writeByte(0);
   }

   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
      sizeTracker.read(384L);
      if (depth > 512) {
         throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
      } else {
         this.tagMap.clear();

         byte b0;
         while((b0 = readType(input, sizeTracker)) != 0) {
            String s = readKey(input, sizeTracker);
            sizeTracker.read((long)(224 + 16 * s.length()));
            NBTBase nbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);
            if (this.tagMap.put(s, nbtbase) != null) {
               sizeTracker.read(288L);
            }
         }

      }
   }

   public Set<String> getKeySet() {
      return this.tagMap.keySet();
   }

   public byte getId() {
      return 10;
   }

   public int getSize() {
      return this.tagMap.size();
   }

   public void setTag(String key, NBTBase value) {
      this.tagMap.put(key, value);
   }

   public void setByte(String key, byte value) {
      this.tagMap.put(key, new NBTTagByte(value));
   }

   public void setShort(String key, short value) {
      this.tagMap.put(key, new NBTTagShort(value));
   }

   public void setInteger(String key, int value) {
      this.tagMap.put(key, new NBTTagInt(value));
   }

   public void setLong(String key, long value) {
      this.tagMap.put(key, new NBTTagLong(value));
   }

   public void setUniqueId(String key, UUID value) {
      this.setLong(key + "Most", value.getMostSignificantBits());
      this.setLong(key + "Least", value.getLeastSignificantBits());
   }

   @Nullable
   public UUID getUniqueId(String key) {
      return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
   }

   public boolean hasUniqueId(String key) {
      return this.hasKey(key + "Most", 99) && this.hasKey(key + "Least", 99);
   }

   public void setFloat(String key, float value) {
      this.tagMap.put(key, new NBTTagFloat(value));
   }

   public void setDouble(String key, double value) {
      this.tagMap.put(key, new NBTTagDouble(value));
   }

   public void setString(String key, String value) {
      this.tagMap.put(key, new NBTTagString(value));
   }

   public void setByteArray(String key, byte[] value) {
      this.tagMap.put(key, new NBTTagByteArray(value));
   }

   public void setIntArray(String key, int[] value) {
      this.tagMap.put(key, new NBTTagIntArray(value));
   }

   public void setBoolean(String key, boolean value) {
      this.setByte(key, (byte)(value ? 1 : 0));
   }

   public NBTBase getTag(String key) {
      return (NBTBase)this.tagMap.get(key);
   }

   public byte getTagId(String key) {
      NBTBase nbtbase = (NBTBase)this.tagMap.get(key);
      return nbtbase == null ? 0 : nbtbase.getId();
   }

   public boolean hasKey(String key) {
      return this.tagMap.containsKey(key);
   }

   public boolean hasKey(String key, int type) {
      int i = this.getTagId(key);
      if (i == type) {
         return true;
      } else if (type != 99) {
         return false;
      } else {
         return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
      }
   }

   public byte getByte(String key) {
      try {
         if (this.hasKey(key, 99)) {
            return ((NBTPrimitive)this.tagMap.get(key)).getByte();
         }
      } catch (ClassCastException var3) {
      }

      return 0;
   }

   public short getShort(String key) {
      try {
         if (this.hasKey(key, 99)) {
            return ((NBTPrimitive)this.tagMap.get(key)).getShort();
         }
      } catch (ClassCastException var3) {
      }

      return 0;
   }

   public int getInteger(String key) {
      try {
         if (this.hasKey(key, 99)) {
            return ((NBTPrimitive)this.tagMap.get(key)).getInt();
         }
      } catch (ClassCastException var3) {
      }

      return 0;
   }

   public long getLong(String key) {
      try {
         if (this.hasKey(key, 99)) {
            return ((NBTPrimitive)this.tagMap.get(key)).getLong();
         }
      } catch (ClassCastException var3) {
      }

      return 0L;
   }

   public float getFloat(String key) {
      try {
         if (this.hasKey(key, 99)) {
            return ((NBTPrimitive)this.tagMap.get(key)).getFloat();
         }
      } catch (ClassCastException var3) {
      }

      return 0.0F;
   }

   public double getDouble(String key) {
      try {
         if (this.hasKey(key, 99)) {
            return ((NBTPrimitive)this.tagMap.get(key)).getDouble();
         }
      } catch (ClassCastException var3) {
      }

      return 0.0D;
   }

   public String getString(String key) {
      try {
         if (this.hasKey(key, 8)) {
            return ((NBTBase)this.tagMap.get(key)).getString();
         }
      } catch (ClassCastException var3) {
      }

      return "";
   }

   public byte[] getByteArray(String key) {
      try {
         if (this.hasKey(key, 7)) {
            return ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
         }
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(key, 7, var3));
      }

      return new byte[0];
   }

   public int[] getIntArray(String key) {
      try {
         if (this.hasKey(key, 11)) {
            return ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
         }
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(key, 11, var3));
      }

      return new int[0];
   }

   public NBTTagCompound getCompoundTag(String key) {
      try {
         if (this.hasKey(key, 10)) {
            return (NBTTagCompound)this.tagMap.get(key);
         }
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(key, 10, var3));
      }

      return new NBTTagCompound();
   }

   public NBTTagList getTagList(String key, int type) {
      try {
         if (this.getTagId(key) == 9) {
            NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);
            if (!nbttaglist.hasNoTags() && nbttaglist.getTagType() != type) {
               return new NBTTagList();
            }

            return nbttaglist;
         }
      } catch (ClassCastException var4) {
         throw new ReportedException(this.createCrashReport(key, 9, var4));
      }

      return new NBTTagList();
   }

   public boolean getBoolean(String key) {
      return this.getByte(key) != 0;
   }

   public void removeTag(String key) {
      this.tagMap.remove(key);
   }

   public String toString() {
      StringBuilder stringbuilder = new StringBuilder("{");
      Collection<String> collection = this.tagMap.keySet();
      if (field_191551_b.isDebugEnabled()) {
         List<String> list = Lists.newArrayList(this.tagMap.keySet());
         Collections.sort(list);
         collection = list;
      }

      String s;
      for(Iterator var5 = ((Collection)collection).iterator(); var5.hasNext(); stringbuilder.append(func_193582_s(s)).append(':').append(this.tagMap.get(s))) {
         s = (String)var5.next();
         if (stringbuilder.length() != 1) {
            stringbuilder.append(',');
         }
      }

      return stringbuilder.append('}').toString();
   }

   public boolean hasNoTags() {
      return this.tagMap.isEmpty();
   }

   private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex) {
      CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
      CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
      crashreportcategory.setDetail("Tag type found", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.this.tagMap.get(key)).getId()];
         }
      });
      crashreportcategory.setDetail("Tag type expected", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return NBTBase.NBT_TYPES[expectedType];
         }
      });
      crashreportcategory.addCrashSection("Tag name", key);
      return crashreport;
   }

   public NBTTagCompound copy() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      Iterator var2 = this.tagMap.keySet().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         nbttagcompound.setTag(s, ((NBTBase)this.tagMap.get(s)).copy());
      }

      return nbttagcompound;
   }

   public boolean equals(Object p_equals_1_) {
      return super.equals(p_equals_1_) && Objects.equals(this.tagMap.entrySet(), ((NBTTagCompound)p_equals_1_).tagMap.entrySet());
   }

   public int hashCode() {
      return super.hashCode() ^ this.tagMap.hashCode();
   }

   private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException {
      output.writeByte(data.getId());
      if (data.getId() != 0) {
         output.writeUTF(name);
         data.write(output);
      }

   }

   private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
      return input.readByte();
   }

   private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
      return input.readUTF();
   }

   static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
      NBTBase nbtbase = NBTBase.createNewByType(id);

      try {
         nbtbase.read(input, depth, sizeTracker);
         return nbtbase;
      } catch (IOException var9) {
         CrashReport crashreport = CrashReport.makeCrashReport(var9, "Loading NBT data");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
         crashreportcategory.addCrashSection("Tag name", key);
         crashreportcategory.addCrashSection("Tag type", id);
         throw new ReportedException(crashreport);
      }
   }

   public void merge(NBTTagCompound other) {
      Iterator var2 = other.tagMap.keySet().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         NBTBase nbtbase = (NBTBase)other.tagMap.get(s);
         if (nbtbase.getId() == 10) {
            if (this.hasKey(s, 10)) {
               NBTTagCompound nbttagcompound = this.getCompoundTag(s);
               nbttagcompound.merge((NBTTagCompound)nbtbase);
            } else {
               this.setTag(s, nbtbase.copy());
            }
         } else {
            this.setTag(s, nbtbase.copy());
         }
      }

   }

   protected static String func_193582_s(String p_193582_0_) {
      return field_193583_c.matcher(p_193582_0_).matches() ? p_193582_0_ : NBTTagString.func_193588_a(p_193582_0_);
   }
}
