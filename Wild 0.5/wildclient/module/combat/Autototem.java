//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import black.nigger.wildclient.module.Category;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.NonNullList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import black.nigger.wildclient.module.Module;

public class Autototem extends Module
{
    private final int OFFHAND_SLOT = 45;
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent playerTickEvent) {
        final ItemStack itemStack = Autototem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        final NonNullList nonNullList = Minecraft.getMinecraft().player.inventory.mainInventory;
        for (int i = 0; i < nonNullList.size(); ++i) {
            if (nonNullList.get(i) != ItemStack.EMPTY && (itemStack == null || itemStack.getItem() != Items.TOTEM_OF_UNDYING) && ((ItemStack)nonNullList.get(i)).getItem() == Items.TOTEM_OF_UNDYING) {
                new ItemStack(Items.TOTEM_OF_UNDYING);
                this.b(i);
                break;
            }
        }
    }
    
    public Autototem() {
        super("AutoTotem", "automatically use totems", Category.COMBAT);
    }
    
    public void b(int n) {
        n = 0;
        if (Autototem.mc.player.openContainer instanceof ContainerPlayer && Autototem.mc.player.ticksExisted % 5 == 0) {
            Autototem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Autototem.mc.player);
            final int n2 = n + 36;
            Autototem.mc.playerController.windowClick(0, n2, 0, ClickType.PICKUP, (EntityPlayer)Autototem.mc.player);
        }
    }
}
