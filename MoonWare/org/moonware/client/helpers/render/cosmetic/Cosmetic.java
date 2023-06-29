package org.moonware.client.helpers.render.cosmetic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Namespaced;
import org.moonware.client.helpers.render.cosmetic.impl.DragonWing;

public class Cosmetic {
    public static void renderAccessory(String[] accessorys, EntityPlayer player, float partialTicks) {
        for (String accessory : accessorys) {
            switch (accessory) {
                case "Dragon_wing":
                    DragonWing.render(player, partialTicks);
                   break;
            }
        }
    }

    public static Namespaced getCape(String cape) {
        return new Namespaced("moonware/" + cape.toLowerCase() + ".png");
    }
    public static Namespaced getWing(String wing) {
        return new Namespaced("moonware/wings/" + wing.toLowerCase() + ".png");
    }
}
