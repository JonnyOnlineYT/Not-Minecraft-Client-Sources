package net.minecraft.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Arrays;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Component;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public class AdvancementRewards
{
    public static final AdvancementRewards field_192114_a = new AdvancementRewards(0, new Namespaced[0], new Namespaced[0], FunctionObject.CacheableFunction.field_193519_a);
    private final int field_192115_b;
    private final Namespaced[] field_192116_c;
    private final Namespaced[] field_192117_d;
    private final FunctionObject.CacheableFunction field_193129_e;

    public AdvancementRewards(int p_i47587_1_, Namespaced[] p_i47587_2_, Namespaced[] p_i47587_3_, FunctionObject.CacheableFunction p_i47587_4_)
    {
        field_192115_b = p_i47587_1_;
        field_192116_c = p_i47587_2_;
        field_192117_d = p_i47587_3_;
        field_193129_e = p_i47587_4_;
    }

    public void func_192113_a(EntityPlayerMP p_192113_1_)
    {
        p_192113_1_.addExperience(field_192115_b);
        LootContext lootcontext = (new LootContext.Builder(p_192113_1_.getServerWorld())).withLootedEntity(p_192113_1_).build();
        boolean flag = false;

        for (Namespaced resourcelocation : field_192116_c)
        {
            for (ItemStack itemstack : p_192113_1_.world.getLootTableManager().getLootTableFromLocation(resourcelocation).generateLootForPools(p_192113_1_.getRNG(), lootcontext))
            {
                if (p_192113_1_.func_191521_c(itemstack))
                {
                    p_192113_1_.world.playSound(null, p_192113_1_.posX, p_192113_1_.posY, p_192113_1_.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((p_192113_1_.getRNG().nextFloat() - p_192113_1_.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    flag = true;
                }
                else
                {
                    EntityItem entityitem = p_192113_1_.dropItem(itemstack, false);

                    if (entityitem != null)
                    {
                        entityitem.setNoPickupDelay();
                        entityitem.setOwner(p_192113_1_.getName());
                    }
                }
            }
        }

        if (flag)
        {
            p_192113_1_.inventoryContainer.detectAndSendChanges();
        }

        if (field_192117_d.length > 0)
        {
            p_192113_1_.func_193102_a(field_192117_d);
        }

        MinecraftServer minecraftserver = p_192113_1_.mcServer;
        FunctionObject functionobject = field_193129_e.func_193518_a(minecraftserver.func_193030_aL());

        if (functionobject != null)
        {
            ICommandSender icommandsender = new ICommandSender()
            {
                public String getName()
                {
                    return p_192113_1_.getName();
                }
                public Component getDisplayName()
                {
                    return p_192113_1_.getDisplayName();
                }
                public void addChatMessage(Component component)
                {
                }
                public boolean canCommandSenderUseCommand(int permLevel, String commandName)
                {
                    return permLevel <= 2;
                }
                public BlockPos getPosition()
                {
                    return p_192113_1_.getPosition();
                }
                public Vec3d getPositionVector()
                {
                    return p_192113_1_.getPositionVector();
                }
                public World getEntityWorld()
                {
                    return p_192113_1_.world;
                }
                public Entity getCommandSenderEntity()
                {
                    return p_192113_1_;
                }
                public boolean sendCommandFeedback()
                {
                    return minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
                }
                public void setCommandStat(CommandResultStats.Type type, int amount)
                {
                    p_192113_1_.setCommandStat(type, amount);
                }
                public MinecraftServer getServer()
                {
                    return p_192113_1_.getServer();
                }
            };
            minecraftserver.func_193030_aL().func_194019_a(functionobject, icommandsender);
        }
    }

    public String toString()
    {
        return "AdvancementRewards{experience=" + field_192115_b + ", loot=" + Arrays.toString(field_192116_c) + ", recipes=" + Arrays.toString(field_192117_d) + ", function=" + field_193129_e + '}';
    }

    public static class Deserializer implements JsonDeserializer<AdvancementRewards>
    {
        public AdvancementRewards deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "rewards");
            int i = JsonUtils.getInt(jsonobject, "experience", 0);
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "loot", new JsonArray());
            Namespaced[] aresourcelocation = new Namespaced[jsonarray.size()];

            for (int j = 0; j < aresourcelocation.length; ++j)
            {
                aresourcelocation[j] = new Namespaced(JsonUtils.getString(jsonarray.get(j), "loot[" + j + "]"));
            }

            JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "recipes", new JsonArray());
            Namespaced[] aresourcelocation1 = new Namespaced[jsonarray1.size()];

            for (int k = 0; k < aresourcelocation1.length; ++k)
            {
                aresourcelocation1[k] = new Namespaced(JsonUtils.getString(jsonarray1.get(k), "recipes[" + k + "]"));
                IRecipe irecipe = CraftingManager.func_193373_a(aresourcelocation1[k]);

                if (irecipe == null)
                {
                    throw new JsonSyntaxException("Unknown recipe '" + aresourcelocation1[k] + "'");
                }
            }

            FunctionObject.CacheableFunction functionobject$cacheablefunction;

            if (jsonobject.has("function"))
            {
                functionobject$cacheablefunction = new FunctionObject.CacheableFunction(new Namespaced(JsonUtils.getString(jsonobject, "function")));
            }
            else
            {
                functionobject$cacheablefunction = FunctionObject.CacheableFunction.field_193519_a;
            }

            return new AdvancementRewards(i, aresourcelocation, aresourcelocation1, functionobject$cacheablefunction);
        }
    }
}
