package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventPreMotionUpdate;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.friend.Friend;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.math.*;
import org.moonware.client.helpers.player.InventoryHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.world.EntityHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.components.draggable.HudComponent;
import org.moonware.client.ui.shader.notification.Notification;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class Aura extends Feature {
    private static final List<Notification> notifications = new CopyOnWriteArrayList<>();
    float yaw;
    float pitch;
    float pitch2;
    float yaw2;
    private int changeSlotCounter;
    private boolean isBreaked;
    public static EntityLivingBase target;
    private double time;
    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting team;
    public static BooleanSetting armorStand;
    public static BooleanSetting walls;
    public static BooleanSetting invis;
    public static BooleanSetting click;
    public static NumberSetting fov;
    public static NumberSetting predict;
    public static NumberSetting rotYawSpeed;
    public static NumberSetting rotPitchSpeed;
    public static NumberSetting rotYawRandom;
    public static NumberSetting rotPitchRandom;
    public static boolean isAttacking;
    public static ListSetting sort;
    private NotificationType type;
    public static ListSetting part;
    public HudComponent drag;
    private float healthBarWidth;
    public static ListSetting rotationMode = new ListSetting("Rotation Mode", "Matrix", () -> true, "Matrix","Sunrise", "Magic");
    public static BooleanSetting onlycrits;
    public static NumberSetting CritsDistance;
    public static NumberSetting thudX;
    public static NumberSetting thudY;
    public static BooleanSetting targetHud;
    public static BooleanSetting smartcrits;
    public static NumberSetting attackCoolDown = new NumberSetting("Attack CoolDown", "Редактирует скорость удара", 0.85F, 0.1F, 1F, 0.01F, () -> true);

    private final BooleanSetting shieldBreak = new BooleanSetting("Break Shield", "Автоматически ломает щит сопернику", false, () -> true);
    private final NumberSetting breakerDelay = new NumberSetting("Breaker Delay", "Настройка скорости шилдбрейкера(чем меньше , тем быстрее)", 50, 1, 400, 0.5F, shieldBreak::getBoolValue);
    public static BooleanSetting ShieldDes = new BooleanSetting("Shield Desync", "Десинкает вашь щит, мешая другому клиенту сломать вам щит", true, () -> true);
    public static BooleanSetting circleESP = new BooleanSetting("CircleESP",true, () -> true);
    private final NumberSetting desms = new NumberSetting("Desync Delay", "Настройка скорости шилдбрейкера(чем меньше , тем быстрее)", 50, 1, 400, 0.5F, ShieldDes::getBoolValue);
    public static ListSetting targetHudMode;


    private double hui;
    public static BooleanSetting AttackBoost = new BooleanSetting("Attack Boost", "бустит вашу скорость при ударе(может флагаться)", true, () -> true);
    public static NumberSetting speed = new NumberSetting("Boost value", "уровень буста",1.04f,  1, 7, 0.01F, AttackBoost::getBoolValue);
    public static BooleanSetting sims;
    public static BooleanSetting jello;
    private double circleAnim;
    double height;
    boolean animat;
    public ListSetting bebraPonyxana;
    public NumberSetting circlesize;
    public NumberSetting points;
    public BooleanSetting depthTest;
    public ColorSetting targetEspColor;
    private double hpWidth;
    private double cooldownbarwidth;
    private double HealthBarWidth;
    private String enemyNickname;
    private double enemyHP;
    private double enemyDistance;
    private EntityPlayer entityPlayer;
    private EntityPlayer entit;
    private Entity entity;
    private double thelp;
    public static BooleanSetting targetHuds = new BooleanSetting("TargetHud", "показывает информацию о таргете на вашем экране",true, () -> true);
    public static ListSetting targetHudModes = new ListSetting("TargetHud Mode", "Moon", () -> targetHuds.getBoolValue(), "Moon","Circle", "Moon2", "Astolfo","MoonTwo", "moonware", "Rich", "Novoline Old", "Novoline New", "Dev", "Minecraft", "Flux", "Test");
    public static ColorSetting targetHudColor = new ColorSetting("TargetHUD Color", Color.PINK.getRGB(), () -> targetHuds.getBoolValue() && !targetHudModes.currentMode.equals("Moon"));
    public static ColorSetting targetMoonColor1 = new ColorSetting("Gradient color one", new Color(255, 133, 0).getRGB(), () -> targetHudModes.currentMode.equalsIgnoreCase("Moon"));
    public static ColorSetting targetMoon2Color1 = new ColorSetting("Gradient color one", new Color(255, 133, 0).getRGB(), () -> targetHudModes.currentMode.equalsIgnoreCase("Moon2"));
    public static ColorSetting targetMoon2Color2 = new ColorSetting("Gradient color one", new Color(255, 133, 0).getRGB(), () -> targetHudModes.currentMode.equalsIgnoreCase("Moon2"));
    public static ColorSetting targetMoonColor2 = new ColorSetting("Gradient color two", new Color(255, 55, 255).getRGB(), () -> targetHudModes.currentMode.equals("Moon"));

    public Aura() {
        super("Aura", "Тоже самое что киллаура, только под другим названием", Type.Combat);
        sort = new ListSetting("AssistSort Mode", "Distance", () -> true, "Distance", "Higher Armor", "Lowest Armor", "Health", "Angle", "HurtTime");
        part = new ListSetting("Aim-Part Mode", "Chest", () -> true, "Chest", "Head", "Leggings", "Boots");
        range = new NumberSetting("Range","Расстояние при котором будет работать аура", 4, 1, 10, 0.1F, () -> true);
        onlycrits = new BooleanSetting("OnlyCrits", false, () -> !smartcrits.getBoolValue());
        CritsDistance = new NumberSetting("Crits FallDistance","Расстояние при котором будет бить аура", 0.25f, 0.05f, 1, 0.05F, () -> onlycrits.getBoolValue() && !smartcrits.getBoolValue());
        targetHud = new BooleanSetting("targetHud", false, () -> true);
        thudX = new NumberSetting("Crits FallDistance","Расстояние при котором будет бить аура", 100f, -700f, 700f, 1F, () -> targetHud.getBoolValue());
        thudY = new NumberSetting("Crits FallDistance","Расстояние при котором будет бить аура", 100f, -700f, 700f, 1F, () -> targetHud.getBoolValue());
        players = new BooleanSetting("Players", true, () -> true);
        mobs = new BooleanSetting("Mobs", false, () -> true);
        armorStand = new BooleanSetting("Mobs", false, () -> true);
        team = new BooleanSetting("Team Check", false, () -> true);
        walls = new BooleanSetting("Walls", false, () -> true);
        invis = new BooleanSetting("Invisible", false, () -> true);
        click = new BooleanSetting("Click Only", false, () -> true);
        smartcrits = new BooleanSetting("Smart Criticals", true, () -> true);
        predict = new NumberSetting("Aim Predict", 0.5f, 0.5f, 5, 0.1f, () -> true);
        fov = new NumberSetting("Assist FOV", 180, 5, 180, 5, () -> true);
        sims = new BooleanSetting("Sims", "отрисовывает треугольник из симса на таргете", true, () -> true);
        jello = new BooleanSetting("Jello", "отрисовывает Jello круг вокруг таргета", false, () -> true);
        rotYawSpeed = new NumberSetting("Rotation Yaw Speed","Скорость ротации по вертикали(можете сделать больше или меньше если вас кикает)", 10, 0.1F, 10, 0.1F, () -> true);
        rotPitchSpeed = new NumberSetting("Rotation Pitch Speed", "Скорость ротации по горизонтали(можете сделать больше или меньше если вас кикает)", 10, 0.1F, 10, 0.1F, () -> true);
        rotYawRandom = new NumberSetting("Yaw Randomize","Уровень рандомизации ротаций по вертикали(можете сделать больше или меньше если вас кикает)", 1, 0, 3, 0.1F, () -> true);
        rotPitchRandom = new NumberSetting("Pitch Randomize","Уровень рандомизации ротаций по горизонтали(можете сделать больше или меньше если вас кикает)", 1, 0, 3, 0.1F, () -> true);
        targetHudMode = new ListSetting("TargetHud Mode", "Circle", () -> targetHuds.getBoolValue(), "Default","Circle", "Moon","Moon2", "Astolfo", "moonware", "Rich", "Novoline Old", "Novoline New", "Dev", "Minecraft", "Flux", "Test");
        addSettings(rotationMode, sort, part, targetHudMode, attackCoolDown, targetHuds, targetMoon2Color1, targetMoon2Color2, targetHudColor, targetMoonColor1, targetMoonColor2, range, onlycrits, smartcrits, CritsDistance, AttackBoost, speed, shieldBreak,breakerDelay, ShieldDes,desms, ShieldDes, players, armorStand, mobs, walls, invis, team, predict, fov, sims, jello);
    }

    public void onRender() {
        /*
        EntityLivingBase renderingTarget = getSortEntities();
        if (circleESP.getBoolValue()) {
            time += .01 * (Feature.deltaTime() * .1);
            final double height = 0.5 * (1 + Math.sin(2 * Math.PI * (time * .3)));
            boolean down = false;
            if (height > .995) {
                down = true;
            } else if (height < .01) {
                down = false;
            }

            final double x = renderingTarget.posX + (renderingTarget.posX - renderingTarget.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
            final double y = renderingTarget.posY + (renderingTarget.posY - renderingTarget.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
            final double z = renderingTarget.posZ + (renderingTarget.posZ - renderingTarget.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;

            GlStateManager.enableBlend();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GL11.glLineWidth(1.5F);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_CULL_FACE);
            final double size = renderingTarget.width * 1.2;
            final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);
            final double yOffset = ((renderingTarget.height * (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? .5 : 1)) + .2) * height;
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            {
                for (int j = 0; j < 361; j++) {
                    Client.RENDER2D.color(Client.INSTANCE.getClientColor().setAlpha((int) (down ? 255 * height : 255 * (1 - height))));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * size, y + yOffset, z - Math.sin(Math.toRadians(j)) * size);
                    Client.RENDER2D.color(Client.INSTANCE.getClientColor().setAlpha(0));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * size, y + yOffset + ((down ? -.5 * (1 - height) : .5 * height)), z - Math.sin(Math.toRadians(j)) * size);
                }
            }
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            {
                for (int j = 0; j < 361; j++) {
                    Client.RENDER2D.color(ClientHelper.getClientColor());
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * size, y + yOffset, z - Math.sin(Math.toRadians(j)) * size);
                }
            }
            GL11.glEnd();
            GlStateManager.enableAlpha();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.resetColor();
            return;
        }
        time += (.01 + (renderingTarget.hurtTime * .005)) * (Client.DELTA_UTIL.deltaTime * .1);
        final Vec3 pos = renderingTarget.getPositionVector();
        final int amount = 360;
        final double health = MathHelper.clamp_double(renderingTarget.getHealth() / 60, 0, .4);
        for (int i = 0; i < amount; i++) {
            final double angle = Math.PI * i / (getValueByName("AuraESP modes").isCombo("Boxes") ? (amount / 2) : (amount * 1.5));
            final double x = Math.cos(angle * 2 + time);
            final double y = getValueByName("AuraESP modes").isCombo("Boxes") ? 0 : ((Math.cos(((i * 2) * .01) + (time * 4)) * .1)) + .2;
            final double z = Math.sin(angle * 2 + time);
            if (i % (getValueByName("AuraESP modes").isCombo("Boxes") ? 20 : 5) == 0) {
                if (getValueByName("AuraESP modes").isCombo("Boxes")) {
                    Client.RENDER3D.drawBox(pos.addVector((x * 1), y, (z * 1)), new Vec3(.05, .05, .05), renderingTarget.hurtTime > 0 ? new Color(255, 0, 0) : Client.INSTANCE.getClientColor());
                } else {
                    Client.RENDER3D.drawBox(pos.addVector((x * 1), y, (z * 1)), new Vec3(.05, ((Math.sin(((i * 2) * .01) + (time * 4)) * .1)) + health, .05), renderingTarget.hurtTime > 0 ? new BetterColor(255, 0, 0).setAlpha(i - 50).addColoring(i / 8) : Client.INSTANCE.getClientColor().setAlpha(i).addColoring((i - 50) / 4));
                }
            }
        }
        w
         */
    }
    public static boolean canSeeEntityAtFov(Entity entityIn, float scope) {
        double diffX = entityIn.posX - Minecraft.player.posX;
        double diffZ = entityIn.posZ - Minecraft.player.posZ;
        float newYaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(newYaw, Minecraft.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float) (Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }

    public static EntityLivingBase getSortEntities() {
        List<EntityLivingBase> entity = new ArrayList<>();
        for (Entity e : Minecraft.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
                if (Minecraft.player.getDistanceToEntity(player) < range.getNumberValue() && (canAssist(player))) {
                    if (player.getHealth() > 0) {
                        entity.add(player);
                    } else {
                        entity.remove(player);
                    }
                }
            }
        }

        String sortMode = sort.getOptions();

        if (sortMode.equalsIgnoreCase("Angle")) {
            entity.sort(((o1, o2) -> (int) (Math.abs(RotationHelper.getAngleEntity(o1) - Minecraft.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity(o2) - Minecraft.player.rotationYaw))));
        } else if (sortMode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        } else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(Minecraft.player::getDistanceToEntity));
        } else if (sortMode.equalsIgnoreCase("HurtTime")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getHurtTime).reversed());
        }

        if (entity.isEmpty())
            return null;

        return entity.get(0);
    }

    public static boolean canAssist(EntityLivingBase player) {
        for (Friend friend : MoonWare.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        for (Entity antibot : MoonWare.antiBot.getBotPlayers()) {
            if (!player.getName().equals(antibot.getName())) {
                continue;
            }
            return false;
        }


        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.getBoolValue()) {
                return false;
            }
        }
        if (player.isInvisible() && !invis.getBoolValue()) {
            return false;
        }

        if (player == AntiBot.isBotPlayer) {
            return false;
        }
        if (!canSeeEntityAtFov(player, fov.getNumberValue() * 2)) {
            return false;
        }
        if (team.getBoolValue() && EntityHelper.isTeamWithYou(player)) {
            return false;
        }
        if (!player.canEntityBeSeen(Minecraft.player)) {
            return walls.getBoolValue();
        }
        return player != Minecraft.player;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (AttackBoost.getBoolValue()) {
            setSuffix("" + MathematicHelper.round(speed.getNumberValue(), 2));
        }
        if (ShieldDes.getBoolValue()) {
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (Minecraft.gameSettings.keyBindUseItem.isKeyDown()) {
                    Minecraft.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                if (Minecraft.player.ticksExisted % (desms.getNumberValue() / 5) == 0) {
                    Minecraft.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
                }
            }
        }
    }


    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        EntityLivingBase entity = getSortEntities();
        String mode = rotationMode.getCurrentMode();
        setSuffix(mode + ", " + MathematicHelper.round(range.getNumberValue(), 1));
        float[] rots = getRotations1(entity, true, 1.5f, 1.5f);
        float[] sunriseRots = RotationHelper.getRotations1(entity, true, 0, 0);
        if (entity != null) {
            if (Minecraft.player.getDistanceToEntity(entity) <= range.getNumberValue() && mode.equalsIgnoreCase("Matrix")) {
                if (entity != Minecraft.player) {
                    //float[] rots2 = getRotationsForAssist(entity);
                    float[] rots2 = getRotations1(entity, true, 1.5f, 1.5f);

                    if (click.getBoolValue() && !(Minecraft.gameSettings.keyBindAttack.isKeyDown()))
                        return;

                    if (canAssist(entity) && entity.getHealth() > 0) {
                        event.setYaw(rots2[0]);
                        event.setPitch(rots2[1]);
                        yaw = rots2[1];
                        pitch = rots2[1];
                        Minecraft.player.renderYawOffset = rots2[0];
                        Minecraft.player.rotationYawHead = rots2[0];
                        Minecraft.player.rotationPitchHead = rots2[1];
                        if (Minecraft.player.getCooledAttackStrength(0) == 1 && !onlycrits.getBoolValue()) {
                            event.setYaw(rots2[0]);
                            event.setPitch(rots2[1]);
                            yaw = rots2[1];
                            pitch = rots2[1];
                            Minecraft.player.renderYawOffset = rots2[0];
                            Minecraft.player.rotationYawHead = rots2[0];
                            Minecraft.player.rotationPitchHead = rots2[1];
                            Minecraft.playerController.attackEntity(Minecraft.player, entity);
                            Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                        }
                        if (Minecraft.player.getCooledAttackStrength(0) == 1 && onlycrits.getBoolValue() && !smartcrits.getBoolValue()) {
                            event.setYaw(rots2[0]);
                            event.setPitch(rots2[1]);
                            if (CritsDistance.getNumberValue() <= Minecraft.player.fallDistance) {
                                event.setYaw(rots2[0]);
                                event.setPitch(rots2[1]);
                                if (!Minecraft.player.onGround) {
                                    event.setYaw(rots2[0]);
                                    event.setPitch(rots2[1]);
                                    yaw = rots2[1];
                                    pitch = rots2[1];
                                    Minecraft.player.renderYawOffset = rots2[0];
                                    Minecraft.player.rotationYawHead = rots2[0];
                                    Minecraft.player.rotationPitchHead = rots2[1];
                                    Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                                }
                            }
                        }
                        if (Minecraft.player.getCooledAttackStrength(0) == 1 && smartcrits.getBoolValue()) {
                            event.setYaw(rots2[0]);
                            event.setPitch(rots2[1]);
                            if (CritsDistance.getNumberValue() <= Minecraft.player.fallDistance) {
                                event.setYaw(rots2[0]);
                                event.setPitch(rots2[1]);
                                if (!Minecraft.player.onGround) {
                                    event.setYaw(rots2[0]);
                                    event.setPitch(rots2[1]);
                                    yaw = rots2[1];
                                    pitch = rots2[1];
                                    Minecraft.player.renderYawOffset = rots2[0];
                                    Minecraft.player.rotationYawHead = rots2[0];
                                    Minecraft.player.rotationPitchHead = rots2[1];
                                    Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                                }
                            }
                            if (Minecraft.player.onGround || Minecraft.player.isInWater() || Minecraft.player.isInLava() || Minecraft.player.isInWeb || Minecraft.player.isOnLadder()) {
                                event.setYaw(rots2[0]);
                                event.setPitch(rots2[1]);
                                if (Helper.timerHelper.hasReached(25) && Minecraft.player.getCooledAttackStrength(0) == 1) {
                                    event.setYaw(rots2[0]);
                                    event.setPitch(rots2[1]);
                                    yaw = rots2[1];
                                    pitch = rots2[1];
                                    Minecraft.player.renderYawOffset = rots2[0];
                                    Minecraft.player.rotationYawHead = rots2[0];
                                    Minecraft.player.rotationPitchHead = rots2[1];
                                    Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                    Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                                }
                            }
                        }
                    }
                }
            }
            if (mode.equalsIgnoreCase("Magic")) {
                if (Minecraft.player.getDistanceToEntity(entity) <= range.getNumberValue()) {
                    if (Minecraft.player.getCooledAttackStrength(0) == 1 && !onlycrits.getBoolValue()) {
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        yaw = rots[1];
                        pitch = rots[1];
                        Minecraft.player.rotationYaw = rots[0];
                        Minecraft.player.rotationPitch = rots[1];
                        Minecraft.player.rotationYawHead = rots[0];
                        Minecraft.player.rotationPitchHead = rots[1];
                        Minecraft.playerController.attackEntity(Minecraft.player, entity);
                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    if (Minecraft.player.getCooledAttackStrength(0) == 1 && onlycrits.getBoolValue()) {
                        if (CritsDistance.getNumberValue() <= Minecraft.player.fallDistance) {
                            event.setYaw(rots[0]);
                            event.setPitch(rots[1]);
                            yaw = rots[1];
                            pitch = rots[1];
                            Minecraft.player.rotationYaw = rots[0];
                            Minecraft.player.rotationPitch = rots[1];
                            Minecraft.player.rotationYawHead = rots[0];
                            Minecraft.player.rotationPitchHead = rots[1];
                            Minecraft.playerController.attackEntity(Minecraft.player, entity);
                            Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                            if (!Minecraft.player.onGround) {
                                Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                            }
                        }
                    }
                }
            }
            if (mode.equalsIgnoreCase("Sunrise")) {
                yaw2 = GCDFix.getFixedRotation(SHelper.EaseOutBack2(yaw2, sunriseRots[0], attackCoolDown.getNumberValue() * 0.7f + 0.3f));
                pitch2 = GCDFix.getFixedRotation(SHelper.Rotate(pitch2, sunriseRots[1], 0.35f, 2.1f));
                event.setYaw(yaw2);
                event.setPitch(pitch2);

                yaw = yaw2;
                pitch = pitch2;
                Minecraft.player.renderYawOffset = sunriseRots[0];
                Minecraft.player.rotationYawHead = sunriseRots[0];
                Minecraft.player.rotationPitchHead = sunriseRots[1];
                if (entity != null) {
                    if (entity != Minecraft.player) {
                        //float[] rots2 = getRotationsForAssist(entity);
                        float[] rots2 = getRotations1(entity, true, 1.5f, 1.5f);

                        if (click.getBoolValue() && !(Minecraft.gameSettings.keyBindAttack.isKeyDown()))
                            return;

                        if (canAssist(entity) && entity.getHealth() > 0) {
                            if (Minecraft.player.getCooledAttackStrength(0) == 1 && !onlycrits.getBoolValue()) {
                                Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                            }
                            if (Minecraft.player.getCooledAttackStrength(0) == 1 && onlycrits.getBoolValue() && !smartcrits.getBoolValue()) {
                                if (CritsDistance.getNumberValue() <= Minecraft.player.fallDistance) {
                                    if (!Minecraft.player.onGround) {
                                        Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                                    }
                                }
                            }
                            if (Minecraft.player.getCooledAttackStrength(0) == 1 && smartcrits.getBoolValue()) {
                                if (CritsDistance.getNumberValue() <= Minecraft.player.fallDistance) {
                                    if (!Minecraft.player.onGround) {
                                        Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                                    }
                                }
                                if (Minecraft.player.onGround || Minecraft.player.isInWater() || Minecraft.player.isInLava() || Minecraft.player.isInWeb || Minecraft.player.isOnLadder()) {
                                    if (Helper.timerHelper.hasReached(25) && Minecraft.player.getCooledAttackStrength(0) == 1) {
                                        Minecraft.playerController.attackEntity(Minecraft.player, entity);
                                        Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (target != null && InventoryHelper.doesHotbarHaveAxe() && shieldBreak.getBoolValue() && (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield)) {
                if (target.isBlocking() && target.isHandActive() && target.isActiveItemStackBlocking() && Minecraft.player.getDistanceToEntity(target) < range.getNumberValue() && RotationHelper.isLookingAtEntity(yaw, pitch, 0.06F, 0.06F, 0.06F, target, range.getNumberValue())) {
                    if (RotationHelper.isAimAtMe(target, 65.0F)) {
                        if (Minecraft.player.inventory.currentItem != InventoryHelper.getAxe()) {
                            Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem = InventoryHelper.getAxe()));
                        }

                        if (Minecraft.player.inventory.currentItem == InventoryHelper.getAxe()) {
                            if (Helper.timerHelper.hasReached(40.0F)) {
                                isBreaked = true;
                                Minecraft.playerController.attackEntity(Minecraft.player, target);
                                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                                Minecraft.player.resetCooldown();
                                Helper.timerHelper.reset();
                            }

                            changeSlotCounter = -1;
                        } else {
                            changeSlotCounter = 0;
                        }
                    }
                } else if (Minecraft.player.inventory.currentItem != InventoryHelper.getSwordAtHotbar() && changeSlotCounter == -1 && InventoryHelper.getSwordAtHotbar() != -1 && (!target.isBlocking() || !target.isHandActive() || !target.isActiveItemStackBlocking())) {
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem = InventoryHelper.getSwordAtHotbar()));
                    NotificationManager.publicity("Shield-Breaker", "successfully broken shield " + entity.getName(), 3, NotificationType.SUCCESS);
                    changeSlotCounter = 0;
                    isBreaked = false;
                }
            }
        }
    }
    @EventTarget
    public void onRender(EventRender3D event3D) {
        EntityLivingBase target = getSortEntities();
        if (target != null && target.getHealth() > 0.0 && Minecraft.player.getDistanceToEntity(target) <= range.getNumberValue()) {
            if (sims.getBoolValue()) {
                float radius = 0.2f;
                int side = 4;

                if (animat) {
                    height = MathHelper.lerp(height, 0.4, 2 * Feature.deltaTime());
                    if (height > 0.39) animat = false;
                } else {
                    height = MathHelper.lerp(height, 0.1, 4 * Feature.deltaTime());
                    if (height < 0.11) animat = true;
                }

                GL11.glPushMatrix();
                GL11.glTranslated(target.lastTickPosX + (target.posX - target.lastTickPosX) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosX, (target.lastTickPosY + (target.posY - target.lastTickPosY) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosY) + target.height + height, target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosZ);
                GL11.glRotatef((Minecraft.player.ticksExisted + Minecraft.timer.renderPartialTicks) * 10, 0.0f, 1.0F, 0.0f);
                RectHelper.setColor(target.hurtTime > 0 ? RectHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / (long) 15) / 100.0 + 6.0F * (1 * 2.55) / 60).getRGB() : RectHelper.TwoColoreffect(new Color(90, 200, 79), new Color(30, 120, 20), Math.abs(System.currentTimeMillis() / (long) 15) / 100.0 + 6.0F * (1 * 2.55) / 90).getRGB());
                RectHelper.enableSmoothLine(0.5F);
                Cylinder c = new Cylinder();
                c.setDrawStyle(GLU.GLU_LINE);
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                c.draw(0F, radius, 0.3f, side, 100);
                GL11.glTranslated(0.0, 0.0, 0.3);
                c.draw(radius, 0f, 0.3f, side, 100);
                RectHelper.disableSmoothLine();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
            if (jello.getBoolValue()) {
                double everyTime = 1500;
                double drawTime = (System.currentTimeMillis() % everyTime);
                boolean drawMode = drawTime > (everyTime / 2);
                double drawPercent = drawTime / (everyTime / 2);
                // true when goes up
                if (!drawMode) {
                    drawPercent = 1 - drawPercent;
                } else {
                    drawPercent -= 1;
                }

                drawPercent = SHelper.easeInOutQuad(drawPercent, 2);

                Minecraft.gameRenderer.disableLightmap();
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_BLEND);
                if (depthTest.getBoolValue())
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glShadeModel(7425);
                Minecraft.gameRenderer.disableLightmap();

                double radius = circlesize.getNumberValue();
                double height = target.height + 0.1;
                double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosX;
                double y = (target.lastTickPosY + (target.posY - target.lastTickPosY) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosY) + height * drawPercent;
                double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosZ;
                double eased = (height / 3) * ((drawPercent > 0.5) ? 1 - drawPercent : drawPercent) * ((drawMode) ? -1 : 1);

                for (int lox = 0; lox < 360; lox += 5) {
                    double x1 = x - Math.sin(lox * Math.PI / 180F) * radius;
                    double z1 = z + Math.cos(lox * Math.PI / 180F) * radius;
                    double x2 = x - Math.sin((lox - 5) * Math.PI / 180F) * radius;
                    double z2 = z + Math.cos((lox - 5) * Math.PI / 180F) * radius;
                    GL11.glBegin(GL11.GL_QUADS);
                    RectHelper.glColor(targetEspColor.getColorValue(), 0f);
                    GL11.glVertex3d(x1, y + eased, z1);
                    GL11.glVertex3d(x2, y + eased, z2);
                    RectHelper.glColor(targetEspColor.getColorValue(), 255);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glEnd();

                    GL11.glBegin(GL_LINE_LOOP);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glEnd();
                }

                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glShadeModel(7424);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glPopMatrix();
            }
        }
    }
    @EventTarget
    public void EventMot(EventPreMotionUpdate event) {

    }

    @EventTarget
    public void onUpdate1(EventUpdate event) {
        /*
        if (autoShieldUnPress.getCurrentValue() && KillAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (KillAura.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    KillAura.mc.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                KillAura.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }
        if (shieldDesync.getCurrentValue() && KillAura.mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.NORTH));
            KillAura.mc.playerController.processRightClick(KillAura.mc.player, KillAura.mc.world, EnumHand.OFF_HAND);
        }


    }
    @EventTarget
    public void onAttackSilent(EventAttackSilent eventAttackSilent) {
        isAttacking = true;
        if (KillAura.mc.player.isBlocking() && this.shieldFixerTimer.hasReached(fixerDelay.getCurrentValue()) && KillAura.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemShield && shieldFixer.getCurrentValue()) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.UP));
            KillAura.mc.playerController.processRightClick(KillAura.mc.player, KillAura.mc.world, EnumHand.OFF_HAND);
            this.shieldFixerTimer.reset();
        }
    }

         */
    }


    private float[] getRotationsForAssist(EntityLivingBase entityIn) {
        float yaw = RotationHelper.updateRotation(GCDFix.getFixedRotation(Minecraft.player.rotationYaw + MWUtils.randomFloat(-rotYawRandom.getNumberValue(), rotYawRandom.getNumberValue())), getRotation(entityIn, predict.getNumberValue())[0], rotYawSpeed.getNumberValue() * 10);
        float pitch = RotationHelper.updateRotation(GCDFix.getFixedRotation(Minecraft.player.rotationPitch + MWUtils.randomFloat(-rotPitchRandom.getNumberValue(), rotPitchRandom.getNumberValue())), getRotation(entityIn, predict.getNumberValue())[1], rotPitchSpeed.getNumberValue() * 10);
        return new float[]{yaw, pitch};
    }
    public static float[] getRotations1(Entity entityIn, boolean random, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) - Minecraft.player.posX - Minecraft.player.motionX;
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) - Minecraft.player.posZ - Minecraft.player.motionZ;
        double diffY;


        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (Minecraft.player.posY + Minecraft.player.getEyeHeight()) - 0.35;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (Minecraft.player.posY + Minecraft.player.getEyeHeight());
        }
        if (!Minecraft.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (Minecraft.player.posY + Minecraft.player.getEyeHeight());
        }
        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float randomYaw = 0.0f;
        if (random) {
            randomYaw = MWUtils.randomFloat(yawRandom, -yawRandom);
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = MWUtils.randomFloat(pitchRandom, -pitchRandom);
        }

        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90)) + randomYaw;
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180 / Math.PI)) + randomPitch;

        yaw = (Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw)));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(Minecraft.player.rotationYaw, yaw, MWUtils.randomFloat(360, 360));
        pitch = RotationHelper.updateRotation(Minecraft.player.rotationPitch, pitch, MWUtils.randomFloat(360, 360));
        return new float[]{yaw, pitch};
    }
    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs(MathHelper.wrapDegrees(getYawToEntity(entity, Minecraft.player) - entityYaw)) <= breakRadius;
    }
    public static float getYawToEntity(Entity mainEntity, Entity targetEntity) {
        double pX = mainEntity.posX;
        double pZ = mainEntity.posZ;
        double eX = targetEntity.posX;
        double eZ = targetEntity.posZ;
        double dX = pX - eX;
        double dZ = pZ - eZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float) yaw;
    }
    private float[] getRotation(Entity e, float predictValue) {

        String mode = part.getOptions();
        float aimPoint = 0;
        if (mode.equalsIgnoreCase("Head")) {
            aimPoint = 0f;
        } else if (mode.equalsIgnoreCase("Chest")) {
            aimPoint = 0.5f;
        } else if (mode.equalsIgnoreCase("Leggings")) {
            aimPoint = 0.9f;
        } else if (mode.equalsIgnoreCase("Boots")) {
            aimPoint = 1.3f;
        }

        double xDelta = e.posX + (e.posX - e.prevPosX) * predictValue - Minecraft.player.posX - Minecraft.player.motionX * predictValue;
        double zDelta = e.posZ + (e.posZ - e.prevPosZ) * predictValue - Minecraft.player.posZ - Minecraft.player.motionZ * predictValue;
        double diffY = e.posY + e.getEyeHeight() - (Minecraft.player.posY + Minecraft.player.getEyeHeight() + aimPoint);

        double distance = MathHelper.sqrt(xDelta * xDelta + zDelta * zDelta);

        float yaw = (float) ((MathHelper.atan2(zDelta, xDelta) * 180.0D / Math.PI) - 90.0F) + MWUtils.randomFloat(-rotYawRandom.getNumberValue(), rotYawRandom.getNumberValue());
        float pitch = ((float) (-(MathHelper.atan2(diffY, distance) * 180.0D / Math.PI))) + MWUtils.randomFloat(-rotPitchRandom.getNumberValue(), rotPitchRandom.getNumberValue());

        yaw = (Minecraft.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw)));
        pitch = Minecraft.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -200F, 200F);
        return new float[]{yaw, pitch};
    }
}
