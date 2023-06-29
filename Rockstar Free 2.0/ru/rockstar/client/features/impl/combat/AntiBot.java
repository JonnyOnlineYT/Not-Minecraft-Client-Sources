package ru.rockstar.client.features.impl.combat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;


public class AntiBot extends Feature {
    public static List<Entity> isBotPlayer = new ArrayList<>();
    public ListSetting antiBotMode = new ListSetting("AntiBot Mode", "Matrix", () -> true, "Matrix", "Matrix New", "Reflex");
    public BooleanSetting invisIgnore = new BooleanSetting("Remove Invisible", "Игнорирует невидимых сущностей", false, () -> true);

    public AntiBot() {
        super("AntiBot", "Добавляет сущностей заспавненых античитом в блэк-лист", 0, Category.COMBAT);
        addSettings(antiBotMode,invisIgnore);
    }


    private boolean checkPosition(final double pos1, final double pos2, final double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }
    
    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        String abmode = antiBotMode.getOptions();
        for (Entity entity : mc.world.loadedEntityList) {
            switch (abmode) {
                case "Matrix":
                	for (final Entity e : mc.world.loadedEntityList) {
        				if (e.ticksExisted < 5 && e instanceof EntityOtherPlayerMP) {
        					if (((EntityOtherPlayerMP) e).hurtTime > 0 && mc.player.getDistanceToEntity(e) <= 25
        							&& mc.getConnection().getPlayerInfo(e.getUniqueID()).getResponseTime() != 0) {
        						mc.world.removeEntity(e);
        					}
        				}
        			}
                    break;
                case "Matrix New":
                		if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && entity instanceof EntityOtherPlayerMP) {
                            isBotPlayer.add(entity);
                        }
                    break;
                case "Reflex":
                    if (entity.getDisplayName().getUnformattedText().length() == 8 && mc.player.posY < entity.posY && entity.ticksExisted == 1 && !entity.isCollidedVertically && !entity.isEntityInsideOpaqueBlock() && entity.fallDistance == 0 && !(entity.posX == 0) && !(entity.posZ == 0)) {
                        isBotPlayer.add(entity);
                        break;
                    }
                    if (invisIgnore.getBoolValue() && entity.isInvisible() && entity != mc.player) {
                        isBotPlayer.add(entity);
                    }
            }
        }
    }
    
    
}
