package net.minecraft.client.gui.hud;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Formatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.Display;
import org.moonware.client.qol.via.MWVia;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DebugHud extends Gui {
    private final Minecraft minecraft;
    public void draw(ScaledResolution res) {
        Minecraft.profiler.startSection("debug");
        GlStateManager.pushMatrix();
        drawLeft();
        drawRight(res);
        GlStateManager.popMatrix();
        Minecraft.profiler.endSection();
    }

    private void drawLeft() {
        List<String> list = getLeft();
        for (int i = 0; i < list.size(); ++i) {
            String s = list.get(i);
            if (Strings.isNullOrEmpty(s)) continue;
            int j = Minecraft.font.height;
            int k = Minecraft.font.getStringWidth(s);
            int i1 = 2 + j * i;
            Gui.drawRect(1, i1 - 1, 2 + k + 1, i1 + j - 1, -1873784752);
            Minecraft.font.drawStringWithShadow(s, 2, i1, 14737632);
        }
    }

    private void drawRight(ScaledResolution scaledRes) {
        List<String> list = getRight();
        for (int i = 0; i < list.size(); ++i) {
            String s = list.get(i);
            if (Strings.isNullOrEmpty(s)) continue;
            int j = Minecraft.font.height;
            int k = Minecraft.font.getStringWidth(s);
            int l = scaledRes.getScaledWidth() - 2 - k;
            int i1 = 2 + j * i;
            Gui.drawRect(l - 1, i1 - 1, l + k + 1, i1 + j - 1, -1873784752);
            Minecraft.font.drawStringWithShadow(s, l, i1, 14737632);
        }
    }

    private List<String> getLeft() {
        BlockPos pos = new BlockPos(Minecraft.getRenderViewEntity().posX, Minecraft.getRenderViewEntity().getEntityBoundingBox().minY, Minecraft.getRenderViewEntity().posZ);
        if (minecraft.isReducedDebug()) {
            return Arrays.asList("MoonWare (1.12.2/vanilla)",
                    Minecraft.debug,
                    Minecraft.renderGlobal.getDebugInfoRenders(),
                    Minecraft.renderGlobal.getDebugInfoEntities(),
                    "P: " + Minecraft.effectRenderer.getStatistics() + ". T: " + Minecraft.world.getDebugLoadedEntities(),
                    Minecraft.world.getProviderName(),
                    "",
                    String.format("Chunk-relative: %d %d %d", pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15),
                    "",
                    "Debug: Pie [shift]: " + (Minecraft.gameSettings.showDebugProfilerChart ? "visible" : "hidden") + " FPS [alt]: " + (Minecraft.gameSettings.showLagometer ? "visible" : "hidden"),
                    "For help: press F3 + Q",
                    "[MWVia] Protocol: " + MWVia.version,
                    "Codd3r: vk.com/rqbad",
                    "gey: VidTu#4994");

        }
        Entity entity = Minecraft.getRenderViewEntity();
        EnumFacing facing = entity.getHorizontalFacing();
        String facingText = "Invalid";
        if (facing == EnumFacing.NORTH) facingText = "Towards negative Z";
        else if (facing == EnumFacing.SOUTH) facingText = "Towards positive Z";
        else if (facing == EnumFacing.WEST) facingText = "Towards negative X";
        else if (facing == EnumFacing.EAST) facingText = "Towards positive X";
        List<String> list = Lists.newArrayList("MoonWare (1.12.2/vanilla)",
                Minecraft.debug,
                Minecraft.renderGlobal.getDebugInfoRenders(),
                Minecraft.renderGlobal.getDebugInfoEntities(),
                "P: " + Minecraft.effectRenderer.getStatistics() + ". T: " + Minecraft.world.getDebugLoadedEntities(),
                Minecraft.world.getProviderName(),
                "",
                String.format("XYZ: %.3f / %.5f / %.3f", Minecraft.getRenderViewEntity().posX, Minecraft.getRenderViewEntity().getEntityBoundingBox().minY, Minecraft.getRenderViewEntity().posZ),
                String.format("Block: %d %d %d", pos.getX(), pos.getY(), pos.getZ()),
                String.format("Chunk: %d %d %d in %d %d %d", pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4),
                String.format("Facing: %s (%s) (%.1f / %.1f)", facing, facingText, MathHelper.wrapDegrees(entity.rotationYaw), MathHelper.wrapDegrees(entity.rotationPitch)));
        if (Minecraft.world != null) {
            Chunk chunk = Minecraft.world.getChunkFromBlockCoords(pos);
            if (Minecraft.world.isBlockLoaded(pos) && pos.getY() >= 0 && pos.getY() < 256) {
                if (!chunk.isEmpty()) {
                    list.add("Biome: " + chunk.getBiome(pos, Minecraft.world.getBiomeProvider()).getBiomeName());
                    list.add("Light: " + chunk.getLightSubtracted(pos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, pos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, pos) + " block)");
                    DifficultyInstance difficulty = Minecraft.world.getDifficultyForLocation(pos);
                    if (Minecraft.isIntegratedServerRunning() && Minecraft.getIntegratedServer() != null) {
                        EntityPlayerMP entityplayermp = Minecraft.getIntegratedServer().getPlayerList().getPlayerByUUID(Minecraft.player.getUniqueID());
                        if (entityplayermp != null) {
                            difficulty = entityplayermp.world.getDifficultyForLocation(new BlockPos(entityplayermp));
                        }
                    }
                    list.add(String.format("Local Difficulty: %.2f // %.2f (Day %d)", difficulty.getAdditionalDifficulty(), difficulty.getClampedAdditionalDifficulty(), Minecraft.world.getWorldTime() / 24000L));
                } else {
                    list.add("Waiting for chunk...");
                }
            } else {
                list.add("Outside of world...");
            }
        }
        if (Minecraft.gameRenderer != null && Minecraft.gameRenderer.isShaderActive()) {
            list.add("Shader: " + Minecraft.gameRenderer.getShaderGroup().getShaderGroupName());
        }
        if (Minecraft.objectMouseOver != null && Minecraft.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && Minecraft.objectMouseOver.getBlockPos() != null) {
            BlockPos look = Minecraft.objectMouseOver.getBlockPos();
            list.add(String.format("Looking at: %d %d %d", look.getX(), look.getY(), look.getZ()));
        }
        list.add("");
        list.add("Debug: Pie [shift]: " + (Minecraft.gameSettings.showDebugProfilerChart ? "visible" : "hidden") + " FPS [alt]: " + (Minecraft.gameSettings.showLagometer ? "visible" : "hidden"));
        list.add("For help: press F3 + Q");
        list.add("[MWVia] Protocol: " + MWVia.version);
        return list;
    }

    private List<String> getRight() {
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        if (minecraft.isReducedDebug()) {
            return Arrays.asList(String.format("Java: %s", System.getProperty("java.version")),
                    String.format("Mem: % 2d%% %03d/%03dMB", used * 100L / max, bytesToMb(used), bytesToMb(max)),
                    String.format("Allocated: % 2d%% %03dMB", total * 100L / max, bytesToMb(total)),
                    "",
                    String.format("CPU: %s", OpenGlHelper.getCpu()),
                    "",
                    String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GlStateManager.glGetString(7936)), GlStateManager.glGetString(7937), GlStateManager.glGetString(7938));
        }
        List<String> list = Lists.newArrayList(String.format("Java: %s", System.getProperty("java.version")),
                String.format("Mem: % 2d%% %03d/%03dMB", used * 100L / max, bytesToMb(used), bytesToMb(max)),
                String.format("Allocated: % 2d%% %03dMB", total * 100L / max, bytesToMb(total)),
                "",
                String.format("CPU: %s", OpenGlHelper.getCpu()),
                "",
                String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GlStateManager.glGetString(7936)), GlStateManager.glGetString(7937), GlStateManager.glGetString(7938));
        if (Minecraft.objectMouseOver != null && Minecraft.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && Minecraft.objectMouseOver.getBlockPos() != null) {
            BlockPos pos = Minecraft.objectMouseOver.getBlockPos();
            IBlockState state = Minecraft.world.getBlockState(pos).getActualState(Minecraft.world, pos);
            list.add("");
            list.add(String.valueOf(Block.REGISTRY.getNameForObject(state.getBlock())));
            for (Map.Entry<IProperty<?>, Comparable<?>> en : state.getProperties().entrySet()) {
                String v = String.valueOf(en.getValue());
                if (Boolean.TRUE.equals(en.getValue())) v = Formatting.GREEN + v;
                else if (Boolean.FALSE.equals(en.getValue())) v = Formatting.RED + v;
                list.add(en.getKey().getName() + ": " + v);
            }
        }
        return list;
    }

    private static long bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }
}
