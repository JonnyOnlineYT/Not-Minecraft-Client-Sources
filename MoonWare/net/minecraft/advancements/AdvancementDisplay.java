package net.minecraft.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;

import javax.annotation.Nullable;
import java.io.IOException;

public class AdvancementDisplay {
    private final Component field_192300_a;
    private final Component field_193225_b;
    private final ItemStack field_192301_b;
    private final Namespaced field_192302_c;
    private final FrameType field_192303_d;
    private final boolean field_193226_f;
    private final boolean field_193227_g;
    private final boolean field_193228_h;
    private float field_192304_e;
    private float field_192305_f;

    public AdvancementDisplay(ItemStack p_i47586_1_, Component p_i47586_2_, Component p_i47586_3_, @Nullable Namespaced p_i47586_4_, FrameType p_i47586_5_, boolean p_i47586_6_, boolean p_i47586_7_, boolean p_i47586_8_) {
        field_192300_a = p_i47586_2_;
        field_193225_b = p_i47586_3_;
        field_192301_b = p_i47586_1_;
        field_192302_c = p_i47586_4_;
        field_192303_d = p_i47586_5_;
        field_193226_f = p_i47586_6_;
        field_193227_g = p_i47586_7_;
        field_193228_h = p_i47586_8_;
    }

    public void func_192292_a(float p_192292_1_, float p_192292_2_) {
        field_192304_e = p_192292_1_;
        field_192305_f = p_192292_2_;
    }

    public Component func_192297_a() {
        return field_192300_a;
    }

    public Component func_193222_b() {
        return field_193225_b;
    }

    public ItemStack func_192298_b() {
        return field_192301_b;
    }

    @Nullable
    public Namespaced func_192293_c() {
        return field_192302_c;
    }

    public FrameType func_192291_d() {
        return field_192303_d;
    }

    public float func_192299_e() {
        return field_192304_e;
    }

    public float func_192296_f() {
        return field_192305_f;
    }

    public boolean func_193223_h() {
        return field_193226_f;
    }

    public boolean func_193220_i() {
        return field_193227_g;
    }

    public boolean func_193224_j() {
        return field_193228_h;
    }

    public static AdvancementDisplay func_192294_a(JsonObject p_192294_0_, JsonDeserializationContext p_192294_1_) {
        Component itextcomponent = JsonUtils.deserializeClass(p_192294_0_, "title", p_192294_1_, Component.class);
        Component itextcomponent1 = JsonUtils.deserializeClass(p_192294_0_, "description", p_192294_1_, Component.class);

        if (itextcomponent != null && itextcomponent1 != null) {
            ItemStack itemstack = func_193221_a(JsonUtils.getJsonObject(p_192294_0_, "icon"));
            Namespaced resourcelocation = p_192294_0_.has("background") ? new Namespaced(JsonUtils.getString(p_192294_0_, "background")) : null;
            FrameType frametype = p_192294_0_.has("frame") ? FrameType.func_192308_a(JsonUtils.getString(p_192294_0_, "frame")) : FrameType.TASK;
            boolean flag = JsonUtils.getBoolean(p_192294_0_, "show_toast", true);
            boolean flag1 = JsonUtils.getBoolean(p_192294_0_, "announce_to_chat", true);
            boolean flag2 = JsonUtils.getBoolean(p_192294_0_, "hidden", false);
            return new AdvancementDisplay(itemstack, itextcomponent, itextcomponent1, resourcelocation, frametype, flag, flag1, flag2);
        } else {
            throw new JsonSyntaxException("Both title and description must be set");
        }
    }

    private static ItemStack func_193221_a(JsonObject p_193221_0_) {
        if (!p_193221_0_.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
        } else {
            Item item = JsonUtils.getItem(p_193221_0_, "item");
            int i = JsonUtils.getInt(p_193221_0_, "data", 0);
            return new ItemStack(item, 1, i);
        }
    }

    public void func_192290_a(PacketBuffer p_192290_1_) {
        p_192290_1_.writeTextComponent(field_192300_a);
        p_192290_1_.writeTextComponent(field_193225_b);
        p_192290_1_.writeItemStackToBuffer(field_192301_b);
        p_192290_1_.writeEnumValue(field_192303_d);
        int i = 0;

        if (field_192302_c != null) {
            i |= 1;
        }

        if (field_193226_f) {
            i |= 2;
        }

        if (field_193228_h) {
            i |= 4;
        }

        p_192290_1_.writeInt(i);

        if (field_192302_c != null) {
            p_192290_1_.func_192572_a(field_192302_c);
        }

        p_192290_1_.writeFloat(field_192304_e);
        p_192290_1_.writeFloat(field_192305_f);
    }

    public static AdvancementDisplay func_192295_b(PacketBuffer p_192295_0_) throws IOException {
        Component itextcomponent = p_192295_0_.readTextComponent();
        Component itextcomponent1 = p_192295_0_.readTextComponent();
        ItemStack itemstack = p_192295_0_.readItemStackFromBuffer();
        FrameType frametype = p_192295_0_.readEnumValue(FrameType.class);
        int i = p_192295_0_.readInt();
        Namespaced resourcelocation = (i & 1) != 0 ? p_192295_0_.func_192575_l() : null;
        boolean flag = (i & 2) != 0;
        boolean flag1 = (i & 4) != 0;
        AdvancementDisplay displayinfo = new AdvancementDisplay(itemstack, itextcomponent, itextcomponent1, resourcelocation, frametype, flag, false, flag1);
        displayinfo.func_192292_a(p_192295_0_.readFloat(), p_192295_0_.readFloat());
        return displayinfo;
    }
}
