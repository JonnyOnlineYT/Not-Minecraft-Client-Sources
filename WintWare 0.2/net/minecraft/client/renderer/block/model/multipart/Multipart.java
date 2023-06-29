package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.block.model.VariantList;

public class Multipart {
   private final List<Selector> selectors;
   private BlockStateContainer stateContainer;

   public Multipart(List<Selector> selectorsIn) {
      this.selectors = selectorsIn;
   }

   public List<Selector> getSelectors() {
      return this.selectors;
   }

   public Set<VariantList> getVariants() {
      Set<VariantList> set = Sets.newHashSet();
      Iterator var2 = this.selectors.iterator();

      while(var2.hasNext()) {
         Selector selector = (Selector)var2.next();
         set.add(selector.getVariantList());
      }

      return set;
   }

   public void setStateContainer(BlockStateContainer stateContainerIn) {
      this.stateContainer = stateContainerIn;
   }

   public BlockStateContainer getStateContainer() {
      return this.stateContainer;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else {
         if (p_equals_1_ instanceof Multipart) {
            Multipart multipart = (Multipart)p_equals_1_;
            if (this.selectors.equals(multipart.selectors)) {
               if (this.stateContainer == null) {
                  return multipart.stateContainer == null;
               }

               return this.stateContainer.equals(multipart.stateContainer);
            }
         }

         return false;
      }
   }

   public int hashCode() {
      return 31 * this.selectors.hashCode() + (this.stateContainer == null ? 0 : this.stateContainer.hashCode());
   }

   public static class Deserializer implements JsonDeserializer<Multipart> {
      public Multipart deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
         return new Multipart(this.getSelectors(p_deserialize_3_, p_deserialize_1_.getAsJsonArray()));
      }

      private List<Selector> getSelectors(JsonDeserializationContext context, JsonArray elements) {
         List<Selector> list = Lists.newArrayList();
         Iterator var4 = elements.iterator();

         while(var4.hasNext()) {
            JsonElement jsonelement = (JsonElement)var4.next();
            list.add((Selector)context.deserialize(jsonelement, Selector.class));
         }

         return list;
      }
   }
}
