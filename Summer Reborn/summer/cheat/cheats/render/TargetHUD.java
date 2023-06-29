package summer.cheat.cheats.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.*;
import summer.cheat.cheats.combat.KillAura;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.render.EventRender2D;
import summer.cheat.guiutil.Setting;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TargetHUD extends Cheats {
    public static Minecraft mc = Minecraft.getMinecraft();
    private String mode;
    private static final Color COLOR;
    private final TimerUtils animationStopwatch;
    private EntityOtherPlayerMP target;
    private double healthBarWidth;
    private double hudHeight;
    public EntityLivingBase lastEnt;
    public float lastHealth;
    public float damageDelt;
    public float lastPlayerHealth;
    public float damageDeltToPlayer;
    public DecimalFormat format;
    public double animation;
    private Setting modes;

    public TargetHUD() {
        super("TargetHud", "Shows the current aura target", Selection.RENDER, true);
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Autumn");
        options.add("Novo");
        options.add("Summer");
        options.add("Sight");
        options.add("Exhibition");

        Summer.INSTANCE.settingsManager.Property(modes = new Setting("Mode", this, "Summer", options));
        this.animationStopwatch = new TimerUtils();
    }

    @EventTarget
    public void onUpdate(EventRender2D event) {
        this.mode = modes.getValString();
        if (this.mode.equalsIgnoreCase("Autumn")) {
            final KillAura aura = CheatManager.getInstance(KillAura.class);
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            final float scaledWidth = sr.getScaledWidth();
            final float scaledHeight = sr.getScaledHeight();
            if (aura.target != null && aura.isToggled()) {
                if (aura.target instanceof EntityOtherPlayerMP) {
                    this.target = (EntityOtherPlayerMP) aura.target;
                    final float width = 140.0f;
                    final float height = 40.0f;
                    final float xOffset = 40.0f;
                    final float x = scaledWidth / 2.0f - 70.0f;
                    final float y = scaledHeight / 2.0f + 80.0f;
                    final float health = this.target.getHealth();
                    double hpPercentage = health / this.target.getMaxHealth();
                    hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                    final double hpWidth = 92.0 * hpPercentage;
                    final int healthColor = ColorUtils.getHealthColor(this.target.getHealth(), this.target.getMaxHealth())
                            .getRGB();
                    final String healthStr = String.valueOf((int) this.target.getHealth() / 1.0f);
                    if (this.animationStopwatch.hasReached(15L)) {
                        this.healthBarWidth = AnimationUtil.INSTANCE.animate(hpWidth, this.healthBarWidth, 0.3529999852180481);
                        this.hudHeight = AnimationUtil.INSTANCE.animate(40.0, this.hudHeight, 0.10000000149011612);
                        this.animationStopwatch.reset();
                    }
                    GL11.glEnable(3089);
                    RenderUtils.prepareScissorBox(x, y, x + 140.0f, (float) (y + this.hudHeight));
                    Gui.drawRect(x, y, x + 140.0f, y + 40.0f, TargetHUD.COLOR.getRGB());
                    Gui.drawRect(x + 32.0f, y + 38.0f, x + 34.0f + this.healthBarWidth, y + 40.0f, healthColor);
                    //   mc.fontRendererObj.drawStringWithShadow("| | | | | | | | | | | | | | | | | | |", x + 5.0f + 46.0f - mc.fontRendererObj.getStringWidth(healthStr) / 2.0f, y + 16.0f, new Color(0,0,0).getRGB());
                    Minecraft.fontRendererObj.drawStringWithShadow(healthStr, x + 33.0f + 46.0f - Minecraft.fontRendererObj.getStringWidth(healthStr) / 2.0f, y + 16.0f, -1);
                    Minecraft.fontRendererObj.drawStringWithShadow(this.target.getName(), x + 50.0f, y + 2.0f, -1);
                    Summer.INSTANCE.fontManager.getFont("EVO 12").drawStringWithShadow("OnGround : " + KillAura.target.onGround + " | ", x + 30.0f, y + 26.0f, -1);
                    Summer.INSTANCE.fontManager.getFont("EVO 12").drawStringWithShadow(" Pitch : " + (int) KillAura.target.rotationYaw, x + 83.0f, y + 26.0f, -1);
                    //    Draw.drawImg(new ResourceLocation("client/icons/unnamed.png"), -2, 83, 83, 20);
                    // mc.fontRendererObj.drawStringWithShadow(""+Killaura.target.hurtTime, x + 120.0f, y + 30.0f, -1);
                    GuiInventory.drawEntityOnScreen((int) (x + 13.333333f), (int) (y + 40.0f), 20, this.target.rotationYaw, this.target.rotationPitch, this.target);
                    GL11.glDisable(3089);
                }
            } else {
                this.healthBarWidth = 92.0;
                this.hudHeight = 0.0;
                this.target = null;
            }
        }
        if (this.mode.equalsIgnoreCase("Novo")) {
            final KillAura ka = CheatManager.getInstance(KillAura.class);
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            if (KillAura.target != null && ka.isToggled()) {
                if (KillAura.target instanceof EntityOtherPlayerMP) {
                    float healthPercentage = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                    float startX = 20;
                    float renderX = (sr.getScaledWidth() / 2) + startX;
                    float renderY = (sr.getScaledHeight() / 2) + 10;
                    int maxX2 = 30;
                    if (ka.target.getCurrentArmor(3) != null) {
                        maxX2 += 15;
                    }
                    if (ka.target.getCurrentArmor(2) != null) {
                        maxX2 += 15;
                    }
                    if (ka.target.getCurrentArmor(1) != null) {
                        maxX2 += 15;
                    }
                    if (ka.target.getCurrentArmor(0) != null) {
                        maxX2 += 15;
                    }
                    if (ka.target.getHeldItem() != null) {
                        maxX2 += 15;
                    }
                    final int healthColor = ColorUtils.getHealthColor(KillAura.target.getHealth(), KillAura.target.getMaxHealth())
                            .getRGB();
                    float maxX = Math.max(maxX2, Minecraft.fontRendererObj.getStringWidth(KillAura.target.getName()) + 30);
                    Gui.drawRect(renderX, renderY, renderX + maxX, renderY + 40, new Color(0, 0, 0, 0.3f).getRGB());
                    Gui.drawRect(renderX, renderY + 38, renderX + (maxX * healthPercentage), renderY + 40, healthColor);
                    Minecraft.fontRendererObj.drawStringWithShadow(KillAura.target.getName(), renderX + 25, renderY + 7, -1);
                    int xAdd = 0;
                    double multiplier = 0.85;
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(multiplier, multiplier, multiplier);
                    if (KillAura.target.getCurrentArmor(3) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(3), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getCurrentArmor(2) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(2), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getCurrentArmor(1) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(1), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getCurrentArmor(0) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(0), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getHeldItem() != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getHeldItem(), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                    }
                    GlStateManager.popMatrix();
                    GuiInventory.drawEntityOnScreen((int) renderX + 12, (int) renderY + 33, 15, KillAura.target.rotationYaw, KillAura.target.rotationPitch, KillAura.target);
                } else {
                    this.healthBarWidth = 92.0;
                    this.hudHeight = 0.0;
                    this.target = null;
                }
            }
        }
        if (this.mode.equalsIgnoreCase("Summer")) {
            final KillAura ka = CheatManager.getInstance(KillAura.class);
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            final float scaledWidth = sr.getScaledWidth();
            final float scaledHeight = sr.getScaledHeight();
            if (KillAura.target != null && ka.isToggled()) {
                if (KillAura.target instanceof EntityOtherPlayerMP) {
                    this.target = (EntityOtherPlayerMP) KillAura.target;
                    final float width = 140.0f;
                    final float height = 40.0f;
                    final float xOffset = 40.0f;
                    final float x = scaledWidth / 2.0f - 70.0f;
                    final float y = scaledHeight / 2.0f + 80.0f;
                    final float health = this.target.getHealth();
                    double hpPercentage = health / this.target.getMaxHealth();
                    hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                    final double hpWidth = 92.0 * hpPercentage;
                    final int healthColor = ColorUtils.getHealthColor(this.target.getHealth(), this.target.getMaxHealth())
                            .getRGB();
                    final String healthStr = String.valueOf((int) this.target.getHealth() / 1.0f);

                    this.healthBarWidth = AnimationUtil.INSTANCE.animate(hpWidth, this.healthBarWidth, 0.3529999852180481);
                    this.hudHeight = AnimationUtil.INSTANCE.animate(40.0, this.hudHeight, 0.10000000149011612);
                    GL11.glEnable(3089);
                    RenderUtils.prepareScissorBox(x, y, x + 140.0f, (float) (y + this.hudHeight));
                    Gui.drawRect(x + 15, y, x + 140.0f, y + 40.0f, new Color(0, 0, 0, 240).getRGB());
                    Gui.drawRect(x + 30.0f, y + 26.0f, x + 35.0f + this.healthBarWidth, y + 35.0f, new Color(219, 255, 1).getRGB());
                    //   mc.fontRendererObj.drawStringWithShadow("| | | | | | | | | | | | | | | | | | |", x + 5.0f + 46.0f - mc.fontRendererObj.getStringWidth(healthStr) / 2.0f, y + 16.0f, new Color(0,0,0).getRGB());
                    Summer.INSTANCE.fontManager.getFont("ROBO 13").drawStringWithShadow(healthStr, x + 33.0f + 46.0f - mc.fontRendererObj.getStringWidth(healthStr) / 2.0f, y + 25.0f, new Color(245, 255, 0).getRGB());
                    Summer.INSTANCE.fontManager.getFont("ROBO 13").drawStringWithShadow(this.target.getName(), x + 38.0f, y + 14.0f, new Color(245, 255, 0).getRGB());
                    Draw.drawImg(new ResourceLocation("client/icons/icon.png"), x + 16, y, 23, 23);
                    //Client.instance.fm.getFont("EVO 12").drawStringWithShadow("OnGround : " + Killaura.target.onGround + " | ", x + 30.0f, y + 26.0f, -1);
                    //	Client.instance.fm.getFont("EVO 12").drawStringWithShadow(" Pitch : " + (int)Killaura.target.rotationYaw, x + 83.0f, y + 26.0f, -1);
                    //    Draw.drawImg(new ResourceLocation("client/icons/unnamed.png"), -2, 83, 83, 20);
                    // mc.fontRendererObj.drawStringWithShadow(""+Killaura.target.hurtTime, x + 120.0f, y + 30.0f, -1);
                    //GuiInventory.drawEntityOnScreen((int)(x + 13.333333f), (int)(y + 40.0f), 20, this.target.rotationYaw, this.target.rotationPitch, this.target);
                    GL11.glDisable(3089);
                }
            } else {
                this.healthBarWidth = 92.0;
                this.hudHeight = 0.0;
                this.target = null;
            }
        }
        if (this.mode.equalsIgnoreCase("Sight")) {
            float red = 0.0F;
            float green = 1.0F;
            float blue = 0.0F;
            red = 0.0F;
            green = 1.0F;
            blue = 0.0F;
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            float healthPercentage = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
            if (!(KillAura.target instanceof EntityPlayer))
                return;
            float startX = 20.0F;
            if (mc.getNetHandler() != null && KillAura.target.getUniqueID() != null) {
                NetworkPlayerInfo i = mc.getNetHandler().getPlayerInfo(KillAura.target.getUniqueID());
                if (i != null) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(i.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect((int) ((sr.getScaledWidth() / 2) + startX), sr.getScaledHeight() / 2 + 10, 8.0F, 7.0F, 8, 8, 40, 40, 64.0F, 64.0F);
                    if (((EntityPlayer) KillAura.target).isWearing(EnumPlayerModelParts.HAT))
                        Gui.drawScaledCustomSizeModalRect((int) ((sr.getScaledWidth() / 2) + startX), sr.getScaledHeight() / 2 + 10, 40.0F, 8.0F, 8, 8, 40, 40, 64.0F, 64.0F);
                }
            }
            String name = KillAura.target.getName();
            float stringX = Minecraft.fontRendererObj.getStringWidth(name);
            float healthX = 130.0F;
            Gui.drawRect((sr.getScaledWidth() / 2) + startX, (sr.getScaledHeight() / 2 + 10), (sr.getScaledWidth() / 2) + Math.max(stringX + startX + 30.0F, healthX) + 20.0F, (sr.getScaledHeight() / 2 + 75), (new Color(0.0F, 0.0F, 0.0F, 0.4F)).getRGB());
            Minecraft.fontRendererObj.drawString(name, ((sr.getScaledWidth() / 2) + startX + 45.0F), (sr.getScaledHeight() / 2 + 15), (new Color(255, 255, 255)).getRGB());
            if (mc.getNetHandler() != null && KillAura.target.getUniqueID() != null) {
                NetworkPlayerInfo i = mc.getNetHandler().getPlayerInfo(KillAura.target.getUniqueID());
                if (i != null)
                    Minecraft.fontRendererObj.drawString(String.valueOf(i.getResponseTime()) + "ms", ((sr.getScaledWidth() / 2) + Math.max(stringX + startX + 30.0F, healthX) + 19.0F - Minecraft.fontRendererObj.getStringWidth(String.valueOf(i.getResponseTime()) + "ms")), (sr.getScaledHeight() / 2 + 63), (new Color(255, 255, 255)).getRGB());
            }
            float multiplier = 1.0F;
            final int healthColor = ColorUtils.getHealthColor(KillAura.target.getHealth(), KillAura.target.getMaxHealth())
                    .getRGB();
            GlStateManager.scale(multiplier, multiplier, multiplier);
            mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getHeldItem(), (int) ((sr.getScaledWidth() / 2) + startX + 6.0F), sr.getScaledHeight() / 2 + 53);
            mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(3), (int) ((sr.getScaledWidth() / 2) + startX + 25.0F), sr.getScaledHeight() / 2 + 53);
            mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(2), (int) ((sr.getScaledWidth() / 2) + startX + 44.0F), sr.getScaledHeight() / 2 + 53);
            mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(1), (int) ((sr.getScaledWidth() / 2) + startX + 63.0F), sr.getScaledHeight() / 2 + 53);
            mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(0), (int) ((sr.getScaledWidth() / 2) + startX + 82.0F), sr.getScaledHeight() / 2 + 53);
            GlStateManager.scale(1.0F / multiplier, 1.0F / multiplier, 1.0F / multiplier);
            Gui.drawRect((sr.getScaledWidth() / 2) + startX, (sr.getScaledHeight() / 2 + 72), (sr.getScaledWidth() / 2) + startX + Math.max(stringX + 8.0F, healthX) * healthPercentage, (sr.getScaledHeight() / 2 + 75), healthColor);
            GlStateManager.scale(0.5D, 0.5D, 0.5D);
            Minecraft.fontRendererObj.drawString("OnGround: " + KillAura.target.onGround, (sr.getScaledWidth() / 2 + 65) / 0.5D, (sr.getScaledHeight() / 2 + 25) / 0.5D, -1);
            Minecraft.fontRendererObj.drawString("Yaw: " + KillAura.target.rotationYaw, (sr.getScaledWidth() / 2 + 65) / 0.5D, (sr.getScaledHeight() / 2 + 30) / 0.5D, -1);
            Minecraft.fontRendererObj.drawString("Pitch: " + KillAura.target.rotationYaw, (sr.getScaledWidth() / 2 + 65) / 0.5D, (sr.getScaledHeight() / 2 + 35) / 0.5D, -1);
            Minecraft.fontRendererObj.drawString("Health: " + KillAura.target.getHealth(), (sr.getScaledWidth() / 2 + 65) / 0.5D, (sr.getScaledHeight() / 2 + 40) / 0.5D, -1);
            Minecraft.fontRendererObj.drawString("HurtTime: " + KillAura.target.hurtTime, (sr.getScaledWidth() / 2 + 65) / 0.5D, (sr.getScaledHeight() / 2 + 45) / 0.5D, -1);
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
        }
        if (this.mode.equalsIgnoreCase("Exhibition")) {
            final KillAura aura = CheatManager.getInstance(KillAura.class);
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            final float scaledWidth = sr.getScaledWidth();
            final float scaledHeight = sr.getScaledHeight();
            if (KillAura.target != null && aura.isToggled()) {
                if (KillAura.target instanceof EntityOtherPlayerMP) {
                    float startX = 20;
                    float renderX = (sr.getScaledWidth() / 2) + startX;
                    float renderY = (sr.getScaledHeight() / 2) + 10;
                    int maxX2 = 30;
                    if (KillAura.target.getCurrentArmor(3) != null) {
                        maxX2 += 15;
                    }
                    if (KillAura.target.getCurrentArmor(2) != null) {
                        maxX2 += 15;
                    }
                    if (KillAura.target.getCurrentArmor(1) != null) {
                        maxX2 += 15;
                    }
                    if (KillAura.target.getCurrentArmor(0) != null) {
                        maxX2 += 15;
                    }
                    if (KillAura.target.getHeldItem() != null) {
                        maxX2 += 15;
                    }
                    this.target = (EntityOtherPlayerMP) KillAura.target;
                    final float width = 140.0f;
                    final float height = 40.0f;
                    final float xOffset = 40.0f;
                    final float x = scaledWidth / 2.0f + 30.0f;
                    final float y = scaledHeight / 2.0f + 30.0f;
                    final float health = this.target.getHealth();
                    double hpPercentage = health / this.target.getMaxHealth();
                    hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                    final double hpWidth = 60.0 * hpPercentage;
                    final int healthColor = ColorUtils.getHealthColor(this.target.getHealth(), this.target.getMaxHealth())
                            .getRGB();
                    final String healthStr = String.valueOf((int) this.target.getHealth() / 1.0f);
                    int xAdd = 0;
                    double multiplier = 0.85;
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(multiplier, multiplier, multiplier);
                    if (KillAura.target.getCurrentArmor(3) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(3), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getCurrentArmor(2) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(2), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getCurrentArmor(1) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(1), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getCurrentArmor(0) != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(0), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
                        xAdd += 15;
                    }
                    if (KillAura.target.getHeldItem() != null) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getHeldItem(), (int) ((((sr.getScaledWidth() / 2) + startX + 33) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 56) / multiplier));
                    }
                    GlStateManager.popMatrix();
                    this.healthBarWidth = AnimationUtil.INSTANCE.animate(hpWidth, this.healthBarWidth, 0.1);
                    Gui.drawGradientRect(x - 3.5, y - 3.5, x + 105.5f, y + 42.4f, new Color(10, 10, 10, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
                    // RenderUtils.prepareScissorBox(x, y, x + 140.0f, (float) (y + this.hudHeight));
                    Gui.drawGradientRect(x - 3, y - 3.2, x + 104.8f, y + 41.8f, new Color(40, 40, 40, 255).getRGB(), new Color(40, 40, 40, 255).getRGB());
                    Gui.drawGradientRect(x - 1.4, y - 1.5, x + 103.5f, y + 40.5f, new Color(74, 74, 74, 255).getRGB(), new Color(74, 74, 74, 255).getRGB());
                    Gui.drawGradientRect(x - 1, y - 1, x + 103.0f, y + 40.0f, new Color(32, 32, 32, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
                    Gui.drawRect(x + 25.0f, y + 11.0f, x + 87f, y + 14.29f, new Color(105, 105, 105, 40).getRGB());
                    Gui.drawRect(x + 25.0f, y + 11.0f, x + 27f + this.healthBarWidth, y + 14.29f, RenderUtils.getColorFromPercentage(this.target.getHealth(), this.target.getMaxHealth()));
                    Summer.INSTANCE.fontManager.getFont("ROBO 14").drawStringWithShadow(this.target.getName(), x + 24.8f, y + 1.9f, new Color(255, 255, 255).getRGB());
                    Summer.INSTANCE.fontManager.getFont("TAHOMA 12").drawStringWithShadow("l   " + "l   " + "l   " + "l   " + "l   " + "l   " + "l   " + "l   ", x + 30.0f, y + 10.2f, new Color(50, 50, 50).getRGB());
                    Summer.INSTANCE.fontManager.getFont("ROBO 10").drawStringWithShadow("HP:" + healthStr + "  l   " + "Dist:" + ((int) target.getDistanceToEntity(Minecraft.thePlayer)), x - 11.2f + 44.0f - Summer.INSTANCE.fontManager.getFont("TAHOMA 14").getWidth(healthStr) / 2.0f, y + 17.0f, -1);
                    GuiInventory.drawEntityOnScreen((int) (x + 12f), (int) (y + 34.0f), 15, this.target.rotationYaw, this.target.rotationPitch, this.target);
                }
            } else {
                this.healthBarWidth = 92.0;
                this.hudHeight = 0.0;
                this.target = null;
            }
        }
    }

    static {
        COLOR = new Color(0, 0, 0, 180);
    }
}