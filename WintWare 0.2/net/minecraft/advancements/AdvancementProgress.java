package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;

public class AdvancementProgress implements Comparable<AdvancementProgress> {
   private final Map<String, CriterionProgress> field_192110_a = Maps.newHashMap();
   private String[][] field_192111_b = new String[0][];

   public void func_192099_a(Map<String, Criterion> p_192099_1_, String[][] p_192099_2_) {
      Set<String> set = p_192099_1_.keySet();
      Iterator iterator = this.field_192110_a.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry<String, CriterionProgress> entry = (Entry)iterator.next();
         if (!set.contains(entry.getKey())) {
            iterator.remove();
         }
      }

      Iterator var7 = set.iterator();

      while(var7.hasNext()) {
         String s = (String)var7.next();
         if (!this.field_192110_a.containsKey(s)) {
            this.field_192110_a.put(s, new CriterionProgress(this));
         }
      }

      this.field_192111_b = p_192099_2_;
   }

   public boolean func_192105_a() {
      if (this.field_192111_b.length == 0) {
         return false;
      } else {
         String[][] var1 = this.field_192111_b;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String[] astring = var1[var3];
            boolean flag = false;
            String[] var6 = astring;
            int var7 = astring.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String s = var6[var8];
               CriterionProgress criterionprogress = this.func_192106_c(s);
               if (criterionprogress != null && criterionprogress.func_192151_a()) {
                  flag = true;
                  break;
               }
            }

            if (!flag) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean func_192108_b() {
      Iterator var1 = this.field_192110_a.values().iterator();

      CriterionProgress criterionprogress;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         criterionprogress = (CriterionProgress)var1.next();
      } while(!criterionprogress.func_192151_a());

      return true;
   }

   public boolean func_192109_a(String p_192109_1_) {
      CriterionProgress criterionprogress = (CriterionProgress)this.field_192110_a.get(p_192109_1_);
      if (criterionprogress != null && !criterionprogress.func_192151_a()) {
         criterionprogress.func_192153_b();
         return true;
      } else {
         return false;
      }
   }

   public boolean func_192101_b(String p_192101_1_) {
      CriterionProgress criterionprogress = (CriterionProgress)this.field_192110_a.get(p_192101_1_);
      if (criterionprogress != null && criterionprogress.func_192151_a()) {
         criterionprogress.func_192154_c();
         return true;
      } else {
         return false;
      }
   }

   public String toString() {
      return "AdvancementProgress{criteria=" + this.field_192110_a + ", requirements=" + Arrays.deepToString(this.field_192111_b) + '}';
   }

   public void func_192104_a(PacketBuffer p_192104_1_) {
      p_192104_1_.writeVarIntToBuffer(this.field_192110_a.size());
      Iterator var2 = this.field_192110_a.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, CriterionProgress> entry = (Entry)var2.next();
         p_192104_1_.writeString((String)entry.getKey());
         ((CriterionProgress)entry.getValue()).func_192150_a(p_192104_1_);
      }

   }

   public static AdvancementProgress func_192100_b(PacketBuffer p_192100_0_) {
      AdvancementProgress advancementprogress = new AdvancementProgress();
      int i = p_192100_0_.readVarIntFromBuffer();

      for(int j = 0; j < i; ++j) {
         advancementprogress.field_192110_a.put(p_192100_0_.readStringFromBuffer(32767), CriterionProgress.func_192149_a(p_192100_0_, advancementprogress));
      }

      return advancementprogress;
   }

   @Nullable
   public CriterionProgress func_192106_c(String p_192106_1_) {
      return (CriterionProgress)this.field_192110_a.get(p_192106_1_);
   }

   public float func_192103_c() {
      if (this.field_192110_a.isEmpty()) {
         return 0.0F;
      } else {
         float f = (float)this.field_192111_b.length;
         float f1 = (float)this.func_194032_h();
         return f1 / f;
      }
   }

   @Nullable
   public String func_193126_d() {
      if (this.field_192110_a.isEmpty()) {
         return null;
      } else {
         int i = this.field_192111_b.length;
         if (i <= 1) {
            return null;
         } else {
            int j = this.func_194032_h();
            return j + "/" + i;
         }
      }
   }

   private int func_194032_h() {
      int i = 0;
      String[][] var2 = this.field_192111_b;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String[] astring = var2[var4];
         boolean flag = false;
         String[] var7 = astring;
         int var8 = astring.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String s = var7[var9];
            CriterionProgress criterionprogress = this.func_192106_c(s);
            if (criterionprogress != null && criterionprogress.func_192151_a()) {
               flag = true;
               break;
            }
         }

         if (flag) {
            ++i;
         }
      }

      return i;
   }

   public Iterable<String> func_192107_d() {
      List<String> list = Lists.newArrayList();
      Iterator var2 = this.field_192110_a.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, CriterionProgress> entry = (Entry)var2.next();
         if (!((CriterionProgress)entry.getValue()).func_192151_a()) {
            list.add(entry.getKey());
         }
      }

      return list;
   }

   public Iterable<String> func_192102_e() {
      List<String> list = Lists.newArrayList();
      Iterator var2 = this.field_192110_a.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, CriterionProgress> entry = (Entry)var2.next();
         if (((CriterionProgress)entry.getValue()).func_192151_a()) {
            list.add(entry.getKey());
         }
      }

      return list;
   }

   @Nullable
   public Date func_193128_g() {
      Date date = null;
      Iterator var2 = this.field_192110_a.values().iterator();

      while(true) {
         CriterionProgress criterionprogress;
         do {
            do {
               if (!var2.hasNext()) {
                  return date;
               }

               criterionprogress = (CriterionProgress)var2.next();
            } while(!criterionprogress.func_192151_a());
         } while(date != null && !criterionprogress.func_193140_d().before(date));

         date = criterionprogress.func_193140_d();
      }
   }

   public int compareTo(AdvancementProgress p_compareTo_1_) {
      Date date = this.func_193128_g();
      Date date1 = p_compareTo_1_.func_193128_g();
      if (date == null && date1 != null) {
         return 1;
      } else if (date != null && date1 == null) {
         return -1;
      } else {
         return date == null && date1 == null ? 0 : date.compareTo(date1);
      }
   }

   public static class Serializer implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress> {
      public JsonElement serialize(AdvancementProgress p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
         JsonObject jsonobject = new JsonObject();
         JsonObject jsonobject1 = new JsonObject();
         Iterator var6 = p_serialize_1_.field_192110_a.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<String, CriterionProgress> entry = (Entry)var6.next();
            CriterionProgress criterionprogress = (CriterionProgress)entry.getValue();
            if (criterionprogress.func_192151_a()) {
               jsonobject1.add((String)entry.getKey(), criterionprogress.func_192148_e());
            }
         }

         if (!jsonobject1.entrySet().isEmpty()) {
            jsonobject.add("criteria", jsonobject1);
         }

         jsonobject.addProperty("done", p_serialize_1_.func_192105_a());
         return jsonobject;
      }

      public AdvancementProgress deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
         JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "advancement");
         JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "criteria", new JsonObject());
         AdvancementProgress advancementprogress = new AdvancementProgress();
         Iterator var7 = jsonobject1.entrySet().iterator();

         while(var7.hasNext()) {
            Entry<String, JsonElement> entry = (Entry)var7.next();
            String s = (String)entry.getKey();
            advancementprogress.field_192110_a.put(s, CriterionProgress.func_192152_a(advancementprogress, JsonUtils.getString((JsonElement)entry.getValue(), s)));
         }

         return advancementprogress;
      }
   }
}
