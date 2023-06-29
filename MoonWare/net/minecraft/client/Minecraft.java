package net.minecraft.client;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.BlockInteractEvent;
import baritone.api.event.events.TickEvent;
import baritone.api.event.events.WorldEvent;
import baritone.api.event.events.type.EventState;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.IngameHud;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.toasts.ToastHud;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.*;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.Timer;
import net.minecraft.util.*;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.MoonWare;
import org.moonware.client.components.LoadingScreen;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.game.EventShutdownClient;
import org.moonware.client.event.events.impl.input.EventInputKey;
import org.moonware.client.event.events.impl.input.EventMouse;
import org.moonware.client.event.events.impl.packet.EventAttackClient;
import org.moonware.client.feature.impl.misc.CPUStabilization;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.BiFunction;

public class Minecraft implements IThreadListener {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Random RANDOM = new Random();

    private static Minecraft minecraft;

    public static File gameDir;
    private static File assetsDir;
    private static File resourcePacksDir;

    @Getter private static boolean fullscreen;
    public static int width;
    public static int height;
    @Getter private static double scaledWidth;
    @Getter private static double scaledHeight;
    @Getter private static int scaledRoundedWidth;
    @Getter private static int scaledRoundedHeight;
    @Getter private static int scaleFactor;

    public static Font font;
    public static Font galacticFont;

    private static ServerData serverData;
    @Getter private static TextureManager textureManager;
    @Getter private static DataFixer dataFixer;
    public static PlayerControllerMP playerController;
    public static final Timer timer = new Timer(20.0F);
    public static ClientLevel world;
    public static RenderGlobal renderGlobal;
    @Getter private static RenderManager renderManager;
    @Getter private static RenderItem renderItem;
    @Getter public static ItemRenderer itemRenderer;
    public static EntityPlayerSP player;
    @Getter public static Entity renderViewEntity;
    public static Entity pointedEntity;
    public static ParticleManager effectRenderer;
    @Getter public static Session session;
    private static boolean isGamePaused;
    private static float field_193996_ah;

    public static Screen screen;
    public static LoadingScreenRenderer loadingScreen;
    public static GameRenderer gameRenderer;
    public static DebugRenderer debugRenderer;
    public static int leftClickCounter;
    public static IntegratedServer integratedServer;
    public static IngameHud ingameGUI;
    public static RayTraceResult objectMouseOver;
    public static GameSettings gameSettings;
    public static CreativeSettings creativeSettings;
    public static MouseHelper mouseHelper;
    private static ISaveFormat saveLoader;
    @Getter public static int debugFPS;
    public static int rightClickDelayTimer;
    public static boolean inGameHasFocus;
    static long systemTime = getSystemTime();
    public static FrameTimer frameTimer = new FrameTimer();
    static long startNanoTime = System.nanoTime();
    private static NetworkManager myNetworkManager;
    private static boolean integratedServerIsRunning;
    public static final Profiler profiler = new Profiler();
    @Getter public static IReloadableResourceManager resourceManager;
    private static final MetadataSerializer metadataSerializer_ = new MetadataSerializer();
    private static final List<IResourcePack> defaultResourcePacks = Lists.newArrayList();
    private static  DefaultResourcePack mcDefaultResourcePack;
    @Getter private static ResourcePackRepository resourcePackRepository;
    @Getter public static LanguageManager languageManager;
    @Getter private static BlockColors blockColors;
    private static ItemColors itemColors;
    private static Framebuffer framebuffer;
    @Getter private static TextureMap textureMapBlocks;
    @Getter private static SoundHandler soundHandler;
    private static MusicTicker mcMusicTicker;
    @Getter private static MinecraftSessionService sessionService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()).createMinecraftSessionService();
    @Getter private static SkinManager skinManager;
    private static final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
    private static final Thread mcThread = Thread.currentThread();
    private static ModelManager modelManager;
    @Getter private static BlockRendererDispatcher blockRenderDispatcher;
    @Getter private static ToastHud toastHud = new ToastHud();
    static volatile boolean running = true;
    public static String debug = "";
    private static long debugUpdateTime = getSystemTime();
    private static int fpsCounter;
    public static boolean actionKeyF3;
    static long prevFrameTime = -1L;
    private static String debugProfilerName = "root";

    public static boolean isLoadedConfig = false;
    public static void start(Session session, File gameDir, int width, int height, boolean fullscreen) {
        minecraft = new Minecraft();
        Minecraft.session = session;
        Minecraft.gameDir = gameDir;
        Minecraft.width = Math.max(width, 1);
        Minecraft.height = Math.max(height, 1);
        Minecraft.fullscreen = fullscreen;
        assetsDir = new File(gameDir, "assets");
        resourcePacksDir = new File(gameDir, "resourcepacks");
        mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(assetsDir, "1.12"));
        ImageIO.setUseCache(false);
        Locale.setDefault(Locale.ROOT);
        Bootstrap.register();
        KeybindComponent.supplierFunction = KeyBinding::func_193626_b;
        LOGGER.info("Setting user: {}", session.getUsername());
        try {
            dataFixer = DataFixesManager.createFixer();
            gameSettings = new GameSettings();
            creativeSettings = new CreativeSettings(minecraft, gameDir);
            defaultResourcePacks.add(mcDefaultResourcePack);
            if (gameSettings.overrideHeight > 0 && gameSettings.overrideWidth > 0) {
                Minecraft.width = gameSettings.overrideWidth;
                Minecraft.height = gameSettings.overrideHeight;
            }
            OS os = OS.getCurrent();
            if (os != OS.OSX) {
                InputStream inputstream = null;
                InputStream inputstream1 = null;
                try {
                    inputstream = mcDefaultResourcePack.getInputStreamAssets(new Namespaced("icons/icon_16x16.png"));
                    inputstream1 = mcDefaultResourcePack.getInputStreamAssets(new Namespaced("icons/icon_32x32.png"));
                    if (inputstream != null && inputstream1 != null) {
                        Display.setIcon(new ByteBuffer[]{readImageToBuffer(inputstream), readImageToBuffer(inputstream1)});
                    }
                } catch (IOException ioexception) {
                    LOGGER.error("Couldn't set icon", ioexception);
                } finally {
                    IOUtils.closeQuietly(inputstream);
                    IOUtils.closeQuietly(inputstream1);
                }
            }
            if (fullscreen) {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                Minecraft.width = Math.max(1, displaymode.getWidth());
                Minecraft.height = Math.max(1, displaymode.getHeight());
            } else {
                Display.setDisplayMode(new DisplayMode(Minecraft.width, Minecraft.height));
            }
            Display.setResizable(true);
            Display.setTitle("MoonWare 1.12.2");
            try {
                Display.create((new PixelFormat()).withDepthBits(24));
            } catch (LWJGLException e) {
                LOGGER.error("Couldn't set pixel format", e);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ignored) {}
                if (Minecraft.fullscreen) updateDisplayMode();
                Display.create();
            }
            LoadingScreen.setState("§cMinecraft: Загрузка OpenGL");
            OpenGlHelper.initializeTextures();
            framebuffer = new Framebuffer(Minecraft.width, Minecraft.height, true);
            framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
            registerMetadataSerializers();
            LoadingScreen.setState("§cMinecraft: Загрузка ресурсов");
            resourcePackRepository = new ResourcePackRepository(resourcePacksDir, new File(gameDir, "server-resource-packs"), mcDefaultResourcePack, metadataSerializer_, gameSettings);
            resourceManager = new SimpleReloadableResourceManager(metadataSerializer_);
            languageManager = new LanguageManager(metadataSerializer_, gameSettings.language);
            resourceManager.registerReloadListener(languageManager);
            refreshResources();
            textureManager = new TextureManager(resourceManager);
            resourceManager.registerReloadListener(textureManager);
            skinManager = new SkinManager(textureManager, new File(assetsDir, "skins"), sessionService);
            saveLoader = new AnvilSaveConverter(new File(gameDir, "saves"), dataFixer);
            soundHandler = new SoundHandler(resourceManager, gameSettings);
            resourceManager.registerReloadListener(soundHandler);
            mcMusicTicker = new MusicTicker(minecraft);
            font = new Font(gameSettings, new Namespaced("textures/font/ascii.png"), textureManager, false);
            LoadingScreen.setState("§dMoonWare: Загрузка шрифтов");
            FontStorage.init();
            if (gameSettings.language != null) {
                font.setUnicodeFlag(isUnicode());
                font.setBidiFlag(languageManager.isCurrentLanguageBidirectional());
            }
            LoadingScreen.setState("§dMoonWare: Загрузка ядра");
            try {
                MoonWare.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
            galacticFont = new Font(gameSettings, new Namespaced("textures/font/ascii_sga.png"), textureManager, false);
            LoadingScreen.setState("§cMinecraft: Проверка OpenGL");
            resourceManager.registerReloadListener(font);
            resourceManager.registerReloadListener(galacticFont);
            resourceManager.registerReloadListener(new GrassColorReloadListener());
            resourceManager.registerReloadListener(new FoliageColorReloadListener());
            mouseHelper = new MouseHelper();
            checkGLError("Pre startup");
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.clearDepth(1.0D);
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(515);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            checkGLError("Startup");
            LoadingScreen.setState("§cMinecraft: Загрузка текстур");
            textureMapBlocks = new TextureMap("textures");
            textureMapBlocks.setMipmapLevels(gameSettings.mipmapLevels);
            textureManager.loadTickableTexture(TextureMap.LOCATION_BLOCKS_TEXTURE, textureMapBlocks);
            textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            textureMapBlocks.setBlurMipmapDirect(false, gameSettings.mipmapLevels > 0);
            LoadingScreen.setState("§cMinecraft: Загрузка моделей");
            modelManager = new ModelManager(textureMapBlocks);
            LoadingScreen.setState("§cMinecraft: Загрузка текстур моделей");
            resourceManager.registerReloadListener(modelManager);
            LoadingScreen.setState("§cMinecraft: Загрузка цветов");
            blockColors = BlockColors.init();
            itemColors = ItemColors.init(blockColors);
            LoadingScreen.setState("§cMinecraft: Загрузка рендереров");
            renderItem = new RenderItem(textureManager, modelManager, itemColors);
            renderManager = new RenderManager(textureManager, renderItem);
            itemRenderer = new ItemRenderer(minecraft);
            resourceManager.registerReloadListener(renderItem);
            gameRenderer = new GameRenderer(minecraft, resourceManager);
            resourceManager.registerReloadListener(gameRenderer);
            blockRenderDispatcher = new BlockRendererDispatcher(modelManager.getBlockModelShapes(), blockColors);
            resourceManager.registerReloadListener(blockRenderDispatcher);
            renderGlobal = new RenderGlobal(minecraft);
            resourceManager.registerReloadListener(renderGlobal);
            GlStateManager.viewport(0, 0, Minecraft.width, Minecraft.height);
            effectRenderer = new ParticleManager(world, textureManager);
            LoadingScreen.setState("§cMinecraft: Завершение");
            checkGLError("Post startup");
            updateScaled();
            ingameGUI = new IngameHud(minecraft);
            openScreen(new TitleScreen());
            loadingScreen = new LoadingScreenRenderer(minecraft);
            debugRenderer = new DebugRenderer(minecraft);
            if (gameSettings.fullScreen && !Minecraft.fullscreen) {
                toggleFullscreen();
            }
            try {
                Display.setVSyncEnabled(gameSettings.enableVsync);
            } catch (OpenGLException ignored) {
                gameSettings.enableVsync = false;
                gameSettings.saveOptions();
            }
            renderGlobal.makeEntityOutlineShader();
        } catch (Throwable e) {
            displayCrashReport(populateReport(new CrashReport("Game startup", e)));
            return;
        }
        try {
            while (running) {
                minecraft.update();
            }
            shutdownMinecraftApplet();
        } catch (ReportedException e) {
            populateReport(e.getCrashReport());
            displayCrashReport(e.getCrashReport());
        } catch (Throwable e) {
            displayCrashReport(populateReport(new CrashReport("Unexpected error", e)));
        }
    }

    private static void registerMetadataSerializers() {
        metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }

    public static Framebuffer getFramebuffer() {
        return framebuffer;
    }

    /**
     * Wrapper around displayCrashReportInternal
     */
    public static void displayCrashReport(CrashReport crashReportIn) {
        File file1 = new File(gameDir, "crash-reports");
        File file2 = new File(file1, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        System.out.println(crashReportIn.getCompleteReport());
        if (crashReportIn.getFile() != null) {
            System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            Sys.alert("MoonWare", "Minecraft вылетел. Краш-репорт сохранен в: " + crashReportIn.getFile().getAbsolutePath());
            System.exit(-1);
        } else if (crashReportIn.saveToFile(file2)) {
            System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            Sys.alert("MoonWare", "Minecraft вылетел. Краш-репорт сохранен в: " + file2.getAbsolutePath());
            System.exit(-1);
        } else {
            System.out.println("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            Sys.alert("MoonWare", "Minecraft вылетел. Не удалось сохранить краш-репорт.");
            System.exit(-2);
        }
    }

    public static boolean isUnicode() {
        return languageManager.isCurrentLocaleUnicode() || gameSettings.forceUnicodeFont;
    }

    public static void refreshResources() {
        List<IResourcePack> list = Lists.newArrayList(defaultResourcePacks);

        if (integratedServer != null) {
            integratedServer.func_193031_aM();
        }

        for (ResourcePackRepository.Entry resourcepackrepository$entry : resourcePackRepository.getRepositoryEntries()) {
            list.add(resourcepackrepository$entry.getResourcePack());
        }

        if (resourcePackRepository.getResourcePackInstance() != null) {
            list.add(resourcePackRepository.getResourcePackInstance());
        }

        try {
            resourceManager.reloadResources(list);
        } catch (RuntimeException runtimeexception) {
            LOGGER.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
            list.clear();
            list.addAll(defaultResourcePacks);
            resourcePackRepository.setRepositories(Collections.emptyList());
            resourceManager.reloadResources(list);
            gameSettings.resourcePacks.clear();
            gameSettings.incompatibleResourcePacks.clear();
            gameSettings.saveOptions();
        }

        languageManager.parseLanguageMetadata(list);

        if (renderGlobal != null) {
            renderGlobal.loadRenderers();
        }
    }

    private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(imageStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

        for (int i : aint) {
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }

    private static void updateDisplayMode() throws LWJGLException {
        DisplayMode displaymode = Display.getDesktopDisplayMode();
        Display.setDisplayMode(displaymode);
        width = displaymode.getWidth();
        height = displaymode.getHeight();
    }

    /**
     * Draw with the WorldRenderer
     */
    public void draw(int posX, int posY, int texU, int texV, int width, int height, int red, int green,
                     int blue, int alpha) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        bufferbuilder.pos(posX, posY + height, 0.0D).tex((float) texU * 0.00390625F, (float) (texV + height) * 0.00390625F).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY + height, 0.0D).tex((float) (texU + width) * 0.00390625F, (float) (texV + height) * 0.00390625F).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY, 0.0D).tex((float) (texU + width) * 0.00390625F, (float) texV * 0.00390625F).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX, posY, 0.0D).tex((float) texU * 0.00390625F, (float) texV * 0.00390625F).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    /**
     * Returns the save loader that is currently being used
     */
    public ISaveFormat getSaveLoader() {
        return saveLoader;
    }

    /**
     * Sets the argument GuiScreen as the main (topmost visible) screen.
     */
    public static void openScreen(@Nullable Screen screen) {
        if (Minecraft.screen != null) Minecraft.screen.onClosed();

        if (screen == null && world == null) {
            screen = new TitleScreen();
        } else if (screen == null && player.getHealth() <= 0.0F) {
            screen = new DeathScreen(null);
        }
        Minecraft.screen = screen;
        if (screen != null) {
            minecraft.setIngameNotInFocus();
            KeyBinding.unPressAllKeys();
            while (Mouse.next()) {}
            while (Keyboard.next()) {}
            screen.init(getScaledRoundedWidth(), getScaledRoundedHeight());
        } else {
            soundHandler.resumeSounds();
            minecraft.setIngameFocus();
        }
    }

    /**
     * Checks for an OpenGL error. If there is one, prints the error ID and error string.
     */
    private static void checkGLError(String message) {
        int i = GlStateManager.glGetError();

        if (i != 0) {
            String s = GLU.gluErrorString(i);
            LOGGER.error("########## GL ERROR ##########");
            LOGGER.error("@ {}", message);
            LOGGER.error("{}: {}", i, s);
        }
    }

    /**
     * Shuts down the minecraft applet by stopping the resource downloads, and clearing up GL stuff; called when the
     * application (or web page) is exited.
     */
    public static void shutdownMinecraftApplet() {
        try {
            LOGGER.info("Stopping!");
            try {
                minecraft.loadWorld(null);
            } catch (Throwable var5) {
            }
            soundHandler.unloadSounds();
        } finally {
            Display.destroy();
            System.exit(0);
        }
    }

    /**
     * Called repeatedly from run()
     */

    public static double frameTime;

    private void update() {
        long i = System.nanoTime();
        profiler.startSection("root");
        if (Display.isCreated() && Display.isCloseRequested()) shutdown();
        timer.updateTimer();
        profiler.startSection("scheduledExecutables");
        synchronized (scheduledTasks) {
            while (!scheduledTasks.isEmpty()) {
                try {
                    scheduledTasks.poll().run();
                } catch (Exception ignored) {}
            }
        }
        profiler.endSection();
        long l = System.nanoTime();
        profiler.startSection("tick");
        for (int j = 0; j < Math.min(10, timer.elapsedTicks); ++j) {
            tick();
        }
        profiler.endStartSection("preRenderErrors");
        long i1 = System.nanoTime() - l;
        checkGLError("Pre render");
        profiler.endStartSection("sound");
        profiler.endStartSection("scaledResolution");
        updateScaled();
        soundHandler.setListener(player, timer.renderPartialTicks);
        profiler.endSection();
        profiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        framebuffer.bindFramebuffer(true);
        profiler.startSection("display");
        GlStateManager.enableTexture2D();
        profiler.endSection();
        profiler.endStartSection("gameRenderer");
        gameRenderer.updateCameraAndRender(isGamePaused ? field_193996_ah : timer.renderPartialTicks, i);
        profiler.endStartSection("toasts");
        toastHud.draw();
        profiler.endSection();
        profiler.endSection();
        if (gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart && !gameSettings.hideGUI) {
            if (!profiler.profilingEnabled) {
                profiler.clearProfiling();
            }
            profiler.profilingEnabled = true;
            displayDebugInfo(i1);
        } else {
            profiler.profilingEnabled = false;
            prevFrameTime = System.nanoTime();
        }
        framebuffer.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        framebuffer.framebufferRender(width, height);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        gameRenderer.renderStreamIndicator(timer.renderPartialTicks);
        GlStateManager.popMatrix();
        profiler.startSection("root");
        updateDisplay();
        Thread.yield();
        checkGLError("Post render");
        ++fpsCounter;
        boolean flag = isSingleplayer() && screen != null && screen.pauses() && !integratedServer.getPublic();
        if (isGamePaused != flag) {
            if (isGamePaused) {
                field_193996_ah = timer.renderPartialTicks;
            } else {
                timer.renderPartialTicks = field_193996_ah;
            }

            isGamePaused = flag;
        }
        long k = System.nanoTime();
        frameTimer.addFrame(k - startNanoTime);
        startNanoTime = k;
        while (getSystemTime() >= debugUpdateTime + 1000L) {
            debugFPS = fpsCounter;
            debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", debugFPS, RenderChunk.renderChunksUpdated, RenderChunk.renderChunksUpdated == 1 ? "" : "s", (float) gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : gameSettings.limitFramerate, gameSettings.enableVsync ? " vsync" : "", gameSettings.fancyGraphics ? "" : " fast", gameSettings.clouds == 0 ? "" : (gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : "");
            RenderChunk.renderChunksUpdated = 0;
            debugUpdateTime += 1000L;
            fpsCounter = 0;
        }
        profiler.startSection("fpslimit_wait");
        if (MoonWare.featureManager.getFeatureByClass(CPUStabilization.class).getState()) Display.sync(5);
        else if (world == null) Display.sync(120);
        else if (gameSettings.limitFramerate < GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) Display.sync(gameSettings.limitFramerate);
        profiler.endSection();
        profiler.endSection();
        frameTime = (System.nanoTime() - i) / 1000000D;
    }

    public static void updateScaled() {
        scaleFactor = 1;
        int scale = gameSettings.scale;
        if (scale == 0) scale = 1000;
        while (scaleFactor < scale && width / (scaleFactor + 1) >= 320 && height / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        scaledWidth = (double) width / (double) scaleFactor;
        scaledHeight = (double) height / (double) scaleFactor;
        scaledRoundedWidth = MathHelper.ceil(scaledWidth);
        scaledRoundedHeight = MathHelper.ceil(scaledHeight);
    }

    public static void updateDisplay() {
        profiler.startSection("display_update");
        Display.update();
        profiler.endSection();
        checkWindowResize();
    }

    protected static void checkWindowResize() {
        if (!fullscreen && Display.wasResized()) {
            int i = width;
            int j = height;
            width = Display.getWidth();
            height = Display.getHeight();
            if (width != i || height != j) {
                if (width <= 0) {
                    width = 1;
                }
                if (height <= 0) {
                    height = 1;
                }

                resize(width, height);
            }
        }
    }

    /**
     * Update debugProfilerName in response to number keys in debug screen
     */
    private void updateDebugProfilerName(int keyCount) {
        List<Profiler.Result> list = profiler.getProfilingData(debugProfilerName);

        if (!list.isEmpty()) {
            Profiler.Result profiler$result = list.remove(0);

            if (keyCount == 0) {
                if (!profiler$result.profilerName.isEmpty()) {
                    int i = debugProfilerName.lastIndexOf(46);

                    if (i >= 0) {
                        debugProfilerName = debugProfilerName.substring(0, i);
                    }
                }
            } else {
                --keyCount;

                if (keyCount < list.size() && !"unspecified".equals((list.get(keyCount)).profilerName)) {
                    if (!debugProfilerName.isEmpty()) {
                        debugProfilerName = debugProfilerName + ".";
                    }

                    debugProfilerName = debugProfilerName + (list.get(keyCount)).profilerName;
                }
            }
        }
    }

    /**
     * Parameter appears to be unused
     */
    private void displayDebugInfo(long elapsedTicksTime) {
        if (profiler.profilingEnabled) {
            List<Profiler.Result> list = profiler.getProfilingData(debugProfilerName);
            Profiler.Result profiler$result = list.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            int i = 160;
            int j = width - 160 - 10;
            int k = height - 320;
            GlStateManager.enableBlend();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos((float) j - 176.0F, (float) k - 96.0F - 16.0F, 0.0D).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos((float) j - 176.0F, k + 320, 0.0D).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos((float) j + 176.0F, k + 320, 0.0D).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos((float) j + 176.0F, (float) k - 96.0F - 16.0F, 0.0D).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d0 = 0.0D;

            for (int l = 0; l < list.size(); ++l) {
                Profiler.Result profiler$result1 = list.get(l);
                int i1 = MathHelper.floor(profiler$result1.usePercentage / 4.0D) + 1;
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                int j1 = profiler$result1.getColor();
                int k1 = j1 >> 16 & 255;
                int l1 = j1 >> 8 & 255;
                int i2 = j1 & 255;
                bufferbuilder.pos(j, k, 0.0D).color(k1, l1, i2, 255).endVertex();

                for (int j2 = i1; j2 >= 0; --j2) {
                    float f = (float) ((d0 + profiler$result1.usePercentage * (double) j2 / (double) i1) * (Math.PI * 2D) / 100.0D);
                    float f1 = MathHelper.sin(f) * 160.0F;
                    float f2 = MathHelper.cos(f) * 160.0F * 0.5F;
                    bufferbuilder.pos((float) j + f1, (float) k - f2, 0.0D).color(k1, l1, i2, 255).endVertex();
                }

                tessellator.draw();
                bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

                for (int i3 = i1; i3 >= 0; --i3) {
                    float f3 = (float) ((d0 + profiler$result1.usePercentage * (double) i3 / (double) i1) * (Math.PI * 2D) / 100.0D);
                    float f4 = MathHelper.sin(f3) * 160.0F;
                    float f5 = MathHelper.cos(f3) * 160.0F * 0.5F;
                    bufferbuilder.pos((float) j + f4, (float) k - f5, 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                    bufferbuilder.pos((float) j + f4, (float) k - f5 + 10.0F, 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                }

                tessellator.draw();
                d0 += profiler$result1.usePercentage;
            }

            DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String s = "";

            if (!"unspecified".equals(profiler$result.profilerName)) {
                s = s + "[0] ";
            }

            if (profiler$result.profilerName.isEmpty()) {
                s = s + "ROOT ";
            } else {
                s = s + profiler$result.profilerName + ' ';
            }

            int l2 = 16777215;
            font.drawStringWithShadow(s, (float) (j - 160), (float) (k - 80 - 16), 16777215);
            s = decimalformat.format(profiler$result.totalUsePercentage) + "%";
            font.drawStringWithShadow(s, (float) (j + 160 - font.getStringWidth(s)), (float) (k - 80 - 16), 16777215);

            for (int k2 = 0; k2 < list.size(); ++k2) {
                Profiler.Result profiler$result2 = list.get(k2);
                StringBuilder stringbuilder = new StringBuilder();

                if ("unspecified".equals(profiler$result2.profilerName)) {
                    stringbuilder.append("[?] ");
                } else {
                    stringbuilder.append("[").append(k2 + 1).append("] ");
                }

                String s1 = stringbuilder.append(profiler$result2.profilerName).toString();
                font.drawStringWithShadow(s1, (float) (j - 160), (float) (k + 80 + k2 * 8 + 20), profiler$result2.getColor());
                s1 = decimalformat.format(profiler$result2.usePercentage) + "%";
                font.drawStringWithShadow(s1, (float) (j + 160 - 50 - font.getStringWidth(s1)), (float) (k + 80 + k2 * 8 + 20), profiler$result2.getColor());
                s1 = decimalformat.format(profiler$result2.totalUsePercentage) + "%";
                font.drawStringWithShadow(s1, (float) (j + 160 - font.getStringWidth(s1)), (float) (k + 80 + k2 * 8 + 20), profiler$result2.getColor());
            }
        }
    }

    /**
     * Called when the window is closing. Sets 'running' to false which allows the game loop to exit cleanly.
     */
    public static void shutdown() {
        EventManager.call(new EventShutdownClient());
        running = false;
    }

    /**
     * Will set the focus to ingame if the Minecraft window is the active with focus. Also clears any GUI screen
     * currently displayed
     */
    public void setIngameFocus() {
        if (Display.isActive()) {
            if (!inGameHasFocus) {
                KeyBinding.updateKeyBindState();
                inGameHasFocus = true;
                mouseHelper.grabMouseCursor();
                openScreen(null);
                leftClickCounter = 10000;
            }
        }
    }

    /**
     * Resets the player keystate, disables the ingame focus, and ungrabs the mouse cursor.
     */
    public void setIngameNotInFocus() {
        if (inGameHasFocus) {
            inGameHasFocus = false;
            mouseHelper.ungrabMouseCursor();
        }
    }

    /**
     * Displays the ingame menu
     */
    public void displayInGameMenu() {
        if (screen == null) {
            openScreen(new PauseScreen());
            if (isSingleplayer() && !integratedServer.getPublic()) {
                soundHandler.pauseSounds();
            }
        }
    }

    private void sendClickBlockToController(boolean leftClick) {
        if (!leftClick) {
            leftClickCounter = 0;
        }

        if (leftClickCounter <= 0 && !player.isHandActive()) {
            if (leftClick && objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = objectMouseOver.getBlockPos();

                if (world.getBlockState(blockpos).getMaterial() != Material.AIR && playerController.onPlayerDamageBlock(blockpos, objectMouseOver.sideHit)) {
                    effectRenderer.addBlockHitEffects(blockpos, objectMouseOver.sideHit);
                    player.swingArm(EnumHand.MAIN_HAND);
                }
            } else {
                playerController.resetBlockRemoving();
            }
        }
    }

    public void clickMouse() {
        if (leftClickCounter <= 0) {

            try {
                EventAttackClient mouseAttackEvent = new EventAttackClient(objectMouseOver.entityHit);

                if (objectMouseOver != null) {
                    EventManager.call(mouseAttackEvent);
                }
            } catch (Exception ex) {
            }

            if (objectMouseOver == null) {
                LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");

                if (playerController.isNotCreative()) {
                    leftClickCounter = 10;
                }
            } else if (!player.isRowingBoat()) {
                switch (objectMouseOver.typeOfHit) {
                    case ENTITY:
                        playerController.attackEntity(player, objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = objectMouseOver.getBlockPos();

                        if (world.getBlockState(blockpos).getMaterial() != Material.AIR) {

                            BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onBlockInteract(new BlockInteractEvent(blockpos, BlockInteractEvent.Type.START_BREAK));

                            playerController.clickBlock(blockpos, objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                        if (playerController.isNotCreative()) {
                            leftClickCounter = 10;
                        }

                        player.resetCooldown();
                }

                player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Called when user clicked he's mouse right button (place)
     */
    public void rightClickMouse() {
        if (!playerController.getIsHittingBlock()) {
            rightClickDelayTimer = 4;

            if (!player.isRowingBoat()) {
                if (objectMouseOver == null) {
                    LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }

                for (EnumHand enumhand : EnumHand.values()) {
                    ItemStack itemstack = player.getHeldItem(enumhand);

                    if (objectMouseOver != null) {
                        switch (objectMouseOver.typeOfHit) {
                            case ENTITY:
                                if (playerController.interactWithEntity(player, objectMouseOver.entityHit, objectMouseOver, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }

                                if (playerController.interactWithEntity(player, objectMouseOver.entityHit, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }

                                break;

                            case BLOCK:
                                BlockPos blockpos = objectMouseOver.getBlockPos();
                                if (world.getBlockState(blockpos).getMaterial() != Material.AIR) {
                                    int i = itemstack.getCount();
                                    EnumActionResult enumactionresult = playerController.processRightClickBlock(player, world, blockpos, objectMouseOver.sideHit, objectMouseOver.hitVec, enumhand);

                                    if (enumactionresult == EnumActionResult.SUCCESS) {

                                        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onBlockInteract(new BlockInteractEvent(blockpos, BlockInteractEvent.Type.USE));

                                        player.swingArm(enumhand);

                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || playerController.isInCreativeMode())) {
                                            gameRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                        }

                                        return;
                                    }
                                }
                        }
                    }

                    if (!itemstack.isEmpty() && playerController.processRightClick(player, world, enumhand) == EnumActionResult.SUCCESS) {
                        gameRenderer.itemRenderer.resetEquippedProgress(enumhand);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Toggles fullscreen mode.
     */
    public static void toggleFullscreen() {
        try {
            fullscreen = !fullscreen;
            gameSettings.fullScreen = fullscreen;
            if (fullscreen) {
                updateDisplayMode();
                width = Display.getDisplayMode().getWidth();
                height = Display.getDisplayMode().getHeight();
            } else {
                Display.setDisplayMode(new DisplayMode(width = 854, height = 480));
            }
            if (screen != null) {
                minecraft.resize(width, height);
            } else {
                minecraft.updateFramebufferSize();
            }
            Display.setFullscreen(fullscreen);
            Display.setVSyncEnabled(gameSettings.enableVsync);
            minecraft.updateDisplay();
        } catch (Exception exception) {
            LOGGER.error("Couldn't toggle fullscreen", exception);
        }
    }

    /**
     * Called to resize the current screen.
     */
    private static void resize(int width, int height) {
        Minecraft.width = Math.max(1, width);
        Minecraft.height = Math.max(1, height);
        updateScaled();
        if (screen != null) {
            screen.init(getScaledRoundedWidth(), getScaledRoundedHeight());
        }
        loadingScreen = new LoadingScreenRenderer(minecraft);
        updateFramebufferSize();
    }

    private static void updateFramebufferSize() {
        framebuffer.createBindFramebuffer(width, height);

        if (gameRenderer != null) {
            gameRenderer.updateShaderGroupSize(width, height);
        }
    }

    /**
     * Return the musicTicker's instance
     */
    public MusicTicker getMusicTicker() {
        return mcMusicTicker;
    }

    /**
     * Runs the current tick.
     */
    public void tick() {
        if (rightClickDelayTimer > 0) {
            --rightClickDelayTimer;
        }

        profiler.startSection("gui");

        if (!isGamePaused) {
            ingameGUI.updateTick();
        }

        profiler.endSection();
        gameRenderer.getMouseOver(1.0F);
        profiler.startSection("gameMode");

        if (!isGamePaused && world != null) {
            playerController.updateController();
        }

        profiler.endStartSection("textures");

        if (world != null) {
            textureManager.tick();
        }

        if (screen == null && player != null) {

            BiFunction<EventState, TickEvent.Type, TickEvent> tickProvider = TickEvent.createNextProvider();

            for (IBaritone baritone : BaritoneAPI.getProvider().getAllBaritones()) {

                TickEvent.Type type = baritone.getPlayerContext().player() != null && baritone.getPlayerContext().world() != null
                        ? TickEvent.Type.IN
                        : TickEvent.Type.OUT;

                baritone.getGameEventHandler().onTick(tickProvider.apply(EventState.PRE, type));
            }


            if (player.getHealth() <= 0.0F && !(screen instanceof DeathScreen)) {
                openScreen(null);
            } else if (player.isPlayerSleeping() && world != null) {
                openScreen(new SleepMPScreen());
            }
        } else if (screen != null && screen instanceof SleepMPScreen && !player.isPlayerSleeping()) {
            openScreen(null);
        }

        if (screen != null) {
            leftClickCounter = 10000;
        }

        if (screen != null) {
            try {
                screen.handleInput();
            } catch (Throwable throwable1) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.setDetail("Screen name", new ICrashReportDetail<String>() {
                    public String call() throws Exception {
                        return screen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }

            if (screen != null) {
                try {
                    screen.update();
                } catch (Throwable throwable) {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Ticking screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
                    crashreportcategory1.setDetail("Screen name", new ICrashReportDetail<String>() {
                        public String call() throws Exception {
                            return screen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(crashreport1);
                }
            }
        }

        if (screen == null || screen.allowUserInput) {

            profiler.endStartSection("mouse");
            runTickMouse();

            if (leftClickCounter > 0) {
                --leftClickCounter;
            }

            profiler.endStartSection("keyboard");
            runTickKeyboard();
        }

        if (world != null) {
            if (player != null) {
                world.joinEntityInSurroundings(player);
            }

            profiler.endStartSection("gameRenderer");

            if (!isGamePaused) {
                gameRenderer.updateRenderer();
            }

            profiler.endStartSection("levelRenderer");

            if (!isGamePaused) {
                renderGlobal.updateClouds();
            }

            profiler.endStartSection("level");

            if (!isGamePaused) {
                if (world.getLastLightningBolt() > 0) {
                    world.setLastLightningBolt(world.getLastLightningBolt() - 1);
                }

                world.updateEntities();
            }
        } else if (gameRenderer.isShaderActive()) {
            gameRenderer.stopUseShader();
        }

        if (!isGamePaused) {
            mcMusicTicker.update();
            soundHandler.update();
        }

        if (world != null) {
            if (!isGamePaused) {
                world.setAllowedSpawnTypes(world.getDifficulty() != EnumDifficulty.PEACEFUL, true);

                try {
                    world.tick();
                } catch (Throwable throwable2) {
                    CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception in world tick");

                    if (world == null) {
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
                        crashreportcategory2.addCrashSection("Problem", "Level is null!");
                    } else {
                        world.addWorldInfoToCrashReport(crashreport2);
                    }

                    throw new ReportedException(crashreport2);
                }
            }

            profiler.endStartSection("animateTick");

            if (!isGamePaused && world != null) {
                world.doVoidFogParticles(MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
            }

            profiler.endStartSection("particles");

            if (!isGamePaused) {
                effectRenderer.updateEffects();
            }
        } else if (myNetworkManager != null) {
            profiler.endStartSection("pendingConnection");
            myNetworkManager.processReceivedPackets();
        }

        profiler.endSection();
        systemTime = getSystemTime();
    }

    private void runTickKeyboard() {
        while (Keyboard.next()) {
            int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
            dispatchKeypresses();
            if (screen != null) screen.handleKeyboardInput();
            boolean flag = Keyboard.getEventKeyState();
            if (flag) {
                if (i == 62 && gameRenderer != null) {
                    gameRenderer.switchUseShader();
                }
                boolean flag1 = false;
                if (screen == null) {
                    EventManager.call(new EventInputKey(i));
                    if (i == Keyboard.KEY_ESCAPE) displayInGameMenu();
                    flag1 = Keyboard.isKeyDown(Keyboard.KEY_F3) && processKeyF3(i);
                    actionKeyF3 |= flag1;
                    if (i == Keyboard.KEY_F1) gameSettings.hideGUI = !gameSettings.hideGUI;
                }
                if (flag1) {
                    KeyBinding.setKeyBindState(i, false);
                } else {
                    KeyBinding.setKeyBindState(i, true);
                    KeyBinding.onTick(i);
                }
                if (gameSettings.showDebugProfilerChart) {
                    if (i == 11) {
                        updateDebugProfilerName(0);
                    }
                    for (int j = 0; j < 9; ++j) {
                        if (i == 2 + j) {
                            updateDebugProfilerName(j + 1);
                        }
                    }
                }
            } else {
                KeyBinding.setKeyBindState(i, false);
                if (i == 61) {
                    if (actionKeyF3) {
                        actionKeyF3 = false;
                    } else {
                        gameSettings.showDebugInfo = !gameSettings.showDebugInfo;
                        gameSettings.showDebugProfilerChart = gameSettings.showDebugInfo && Screen.hasShiftDown();
                        gameSettings.showLagometer = gameSettings.showDebugInfo && Screen.hasAltDown();
                    }
                }
            }
        }
        processKeyBinds();
    }

    private boolean processKeyF3(int key) {
        if (key == Keyboard.KEY_A) {
            renderGlobal.loadRenderers();
            printDebug("debug.reload_chunks.message");
            return true;
        }
        if (key == Keyboard.KEY_B) {
            boolean hitboxes = !renderManager.isDebugBoundingBox();
            renderManager.setDebugBoundingBox(hitboxes);
            printDebug(hitboxes ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
            return true;
        }
        if (key == Keyboard.KEY_D) {
            ingameGUI.getChatGUI().clearChatMessages(false);
            return true;
        }
        if (key == Keyboard.KEY_F) {
            gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, Screen.hasShiftDown() ? -1 : 1);
            printDebug("debug.cycle_renderdistance.message", gameSettings.renderDistance);
            return true;
        }
        if (key == Keyboard.KEY_G) {
            boolean chunkBounds = debugRenderer.toggleChunkBounds();
            printDebug(chunkBounds ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
            return true;
        }
        if (key == Keyboard.KEY_H) {
            gameSettings.advancedItemTooltips = !gameSettings.advancedItemTooltips;
            printDebug(gameSettings.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
            gameSettings.saveOptions();
            return true;
        }
        if (key == Keyboard.KEY_N) {
            if (!player.canCommandSenderUseCommand(2, "")) {
                printDebug("debug.creative_spectator.error");
                return true;
            }
            if (player.isCreative()) {
                player.sendChatMessage("/gamemode spectator");
                return true;
            }
            player.sendChatMessage("/gamemode creative");
            return true;
        }
        if (key == Keyboard.KEY_P) {
            gameSettings.pauseOnLostFocus = !gameSettings.pauseOnLostFocus;
            gameSettings.saveOptions();
            printDebug(gameSettings.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
            return true;
        }
        if (key == Keyboard.KEY_Q) {
            printDebug("debug.help.message");
            ChatHud chat = ingameGUI.getChatGUI();
            chat.printChatMessage(new TranslatableComponent("debug.reload_chunks.help"));
            chat.printChatMessage(new TranslatableComponent("debug.show_hitboxes.help"));
            chat.printChatMessage(new TranslatableComponent("debug.clear_chat.help"));
            chat.printChatMessage(new TranslatableComponent("debug.cycle_renderdistance.help"));
            chat.printChatMessage(new TranslatableComponent("debug.chunk_boundaries.help"));
            chat.printChatMessage(new TranslatableComponent("debug.advanced_tooltips.help"));
            chat.printChatMessage(new TranslatableComponent("debug.creative_spectator.help"));
            chat.printChatMessage(new TranslatableComponent("debug.pause_focus.help"));
            chat.printChatMessage(new TranslatableComponent("debug.help.help"));
            chat.printChatMessage(new TranslatableComponent("debug.reload_resourcepacks.help"));
            return true;
        }
        if (key == Keyboard.KEY_T) {
            printDebug("debug.reload_resourcepacks.message");
            refreshResources();
            return true;
        }
        if (key == Keyboard.KEY_C) {
            openScreen(new ConfirmScreen(b -> {
                if (b) throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                openScreen(null);
            }, "Вы уверены что хотите " + Formatting.RED + "крашнуть" + Formatting.RESET +
                    " игру?", "Сочетание клавиш " + Formatting.YELLOW + "F3 + C" +
                    Formatting.RESET + " крашит игру при нормальных обстоятельствах.",
                    Formatting.RED + "Крашнуть", I18n.format("gui.cancel")));
            return true;
        }
        return false;
    }

    private void processKeyBinds() {
        for (; gameSettings.keyBindTogglePerspective.isPressed(); renderGlobal.setDisplayListEntitiesDirty()) {
            ++gameSettings.thirdPersonView;

            if (gameSettings.thirdPersonView > 2) {
                gameSettings.thirdPersonView = 0;
            }

            if (gameSettings.thirdPersonView == 0) {
                gameRenderer.loadEntityShader(renderViewEntity);
            } else if (gameSettings.thirdPersonView == 1) {
                gameRenderer.loadEntityShader(null);
            }
        }

        while (gameSettings.keyBindSmoothCamera.isPressed()) {
            gameSettings.smoothCamera = !gameSettings.smoothCamera;
        }

        for (int i = 0; i < 9; ++i) {
            boolean flag = gameSettings.field_193629_ap.isKeyDown();
            boolean flag1 = gameSettings.field_193630_aq.isKeyDown();

            if (gameSettings.keyBindsHotbar[i].isPressed()) {
                if (player.isSpectator()) {
                    ingameGUI.getSpectatorGui().onHotbarSelected(i);
                } else if (!player.isCreative() || screen != null || !flag1 && !flag) {
                    player.inventory.currentItem = i;
                } else {
                    GuiContainerCreative.func_192044_a(this, i, flag1, flag);
                }
            }
        }

        while (gameSettings.keyBindInventory.isPressed()) {
            if (playerController.isRidingHorse()) {
                player.sendHorseInventory();
            } else {
                openScreen(new GuiInventory(player));
            }
        }

        while (gameSettings.field_194146_ao.isPressed()) {
            openScreen(new GuiScreenAdvancements(player.connection.func_191982_f()));
        }

        while (gameSettings.keyBindSwapHands.isPressed()) {
            if (!player.isSpectator()) {
                getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }

        while (gameSettings.keyBindDrop.isPressed()) {
            if (!player.isSpectator()) {
                player.dropItem(Screen.hasControlDown());
            }
        }

        boolean flag2 = gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

        if (flag2) {
            while (gameSettings.keyBindChat.isPressed()) {
                openScreen(new ChatScreen());
            }

            if (screen == null && gameSettings.keyBindCommand.isPressed()) {
                openScreen(new ChatScreen("/"));
            }
        }

        if (player.isHandActive()) {
            if (!gameSettings.keyBindUseItem.isKeyDown()) {
                playerController.onStoppedUsingItem(player);
            }

            label109:

            while (true) {
                if (!gameSettings.keyBindAttack.isPressed()) {
                    while (gameSettings.keyBindUseItem.isPressed()) {
                    }

                    while (true) {
                        if (gameSettings.keyBindPickBlock.isPressed()) {
                            continue;
                        }

                        break label109;
                    }
                }
            }
        } else {
            while (gameSettings.keyBindAttack.isPressed()) {
                clickMouse();
            }

            while (gameSettings.keyBindUseItem.isPressed()) {
                rightClickMouse();
            }

            while (gameSettings.keyBindPickBlock.isPressed()) {
                middleClickMouse();
            }
        }

        if (gameSettings.keyBindUseItem.isKeyDown() && rightClickDelayTimer == 0 && !player.isHandActive()) {
            rightClickMouse();
        }

        sendClickBlockToController(screen == null && gameSettings.keyBindAttack.isKeyDown() && inGameHasFocus);
    }

    private void runTickMouse() {
        while (Mouse.next()) {
            int i = Mouse.getEventButton();
            KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());

            if (Mouse.getEventButtonState()) {

                EventMouse eventMouse = new EventMouse(i);
                EventManager.call(eventMouse);

                if (player.isSpectator() && i == 2) {
                    ingameGUI.getSpectatorGui().onMiddleClick();
                } else {
                    KeyBinding.onTick(i - 100);
                }
            }

            long j = getSystemTime() - systemTime;

            if (j <= 200L) {
                int k = Mouse.getEventDWheel();

                if (k != 0) {
                    if (player.isSpectator()) {
                        k = k < 0 ? -1 : 1;

                        if (ingameGUI.getSpectatorGui().isMenuActive()) {
                            ingameGUI.getSpectatorGui().onMouseScroll(-k);
                        } else {
                            float f = MathHelper.clamp(player.capabilities.getFlySpeed() + (float) k * 0.005F, 0.0F, 0.2F);
                            player.capabilities.setFlySpeed(f);
                        }
                    } else {
                        player.inventory.changeCurrentItem(k);
                    }
                }

                if (screen == null) {
                    if (!inGameHasFocus && Mouse.getEventButtonState()) {
                        setIngameFocus();
                    }
                } else {
                    screen.handleMouseInput();
                }
            }
        }
    }

    private void printDebug(String key, Object... args) {
        ingameGUI.getChatGUI().printChatMessage(new TextComponent("")
                .append(new TranslatableComponent("debug.prefix").setStyle(new Style()
                        .setColor(Formatting.YELLOW).setBold(true)))
                .append(" ").append(new TranslatableComponent(key, args)));
    }

    /**
     * Arguments: World foldername,  World ingame name, WorldSettings
     */
    public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn) {
        ISaveHandler isavehandler = saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && worldSettingsIn != null) {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }

        if (worldSettingsIn == null) {
            worldSettingsIn = new WorldSettings(worldinfo);
        }

        try {
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(playerprofilecache);
            TileEntitySkull.setSessionService(minecraftsessionservice);
            PlayerProfileCache.setOnlineMode(false);
            integratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache);
            integratedServer.startServerThread();
            integratedServerIsRunning = true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }

        loadingScreen.setTitle(I18n.format("menu.loadingLevel"));
        loadingScreen.setText("");
        loadingScreen.setProgress(-1);
        loadingScreen.draw();

        while (!integratedServer.serverIsInRunLoop()) {
            String s = integratedServer.getUserMessage();
            if (s != null) {
                loadingScreen.setText(I18n.format(s));
            } else {
                loadingScreen.setText("");
            }
            loadingScreen.setProgress(integratedServer.percentDone);
            loadingScreen.draw();
        }

        openScreen(new WorkingScreen());
        SocketAddress socketaddress = integratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.local(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
        networkmanager.sendPacket(new C00Handshake(socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket(new CPacketLoginStart(session.getProfile()));
        myNetworkManager = networkmanager;
    }

    /**
     * unloads the current world first
     */
    public void loadWorld(@Nullable ClientLevel worldClientIn) {
        loadWorld(worldClientIn, "");
    }

    /**
     * par2Str is displayed on the loading screen to the user unloads the current world first
     */
    public void loadWorld(@Nullable ClientLevel worldClientIn, String loadingMessage) {


        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onWorldEvent(
                new WorldEvent(
                        world,
                        EventState.PRE
                )
        );

        if (worldClientIn == null) {

            NetHandlerPlayClient nethandlerplayclient = getConnection();

            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }

            if (integratedServer != null && integratedServer.isAnvilFileSet()) {
                integratedServer.initiateShutdown();
            }

            integratedServer = null;
            gameRenderer.func_190564_k();
            gameSettings.showDebugInfo = false;
            ingameGUI.getChatGUI().clearChatMessages(true);
            playerController = null;
        }

        renderViewEntity = null;
        myNetworkManager = null;

        if (loadingScreen != null) {
            loadingScreen.setTitle("");
            loadingScreen.setText(loadingMessage);
            loadingScreen.setProgress(-1);
            loadingScreen.draw();
        }

        if (worldClientIn == null && world != null) {
            resourcePackRepository.clearResourcePack();
            ingameGUI.cleanup();
            setServerData(null);
            integratedServerIsRunning = false;
        }

        soundHandler.stopSounds();
        world = worldClientIn;

        if (renderGlobal != null) {
            renderGlobal.setWorldAndLoadRenderers(worldClientIn);
        }

        if (effectRenderer != null) {
            effectRenderer.clearEffects(worldClientIn);
        }

        TileEntityRendererDispatcher.instance.setWorld(worldClientIn);

        if (worldClientIn != null) {
            if (!integratedServerIsRunning) {
                AuthenticationService authenticationservice = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
                MinecraftSessionService minecraftsessionservice = authenticationservice.createMinecraftSessionService();
                GameProfileRepository gameprofilerepository = authenticationservice.createProfileRepository();
                PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(Minecraft.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
                TileEntitySkull.setProfileCache(playerprofilecache);
                TileEntitySkull.setSessionService(minecraftsessionservice);
                PlayerProfileCache.setOnlineMode(false);
            }

            if (player == null) {
                player = playerController.func_192830_a(worldClientIn, new StatisticsManager(), new RecipeBookClient());
                playerController.flipPlayer(player);
            }

            player.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(player);
            player.movementInput = new MovementInputFromOptions(gameSettings);
            playerController.setPlayerCapabilities(player);
            renderViewEntity = player;
        } else {
            saveLoader.flushCache();
            player = null;
        }

        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onWorldEvent(
                new WorldEvent(
                        world,
                        EventState.POST
                )
        );

        System.gc();

        systemTime = 0L;
    }

    public void setDimensionAndSpawnPlayer(int dimension) {
        world.setInitialSpawnLocation();
        world.removeAllEntities();
        int i = 0;
        String s = null;

        if (player != null) {
            i = player.getEntityId();
            world.removeEntity(player);
            s = player.getServerBrand();
        }

        renderViewEntity = null;
        EntityPlayerSP entityplayersp = player;
        player = playerController.func_192830_a(world, player == null ? new StatisticsManager() : player.getStatFileWriter(), player == null ? new RecipeBook() : player.func_192035_E());
        player.getDataManager().setEntryValues(entityplayersp.getDataManager().getAll());
        player.dimension = dimension;
        renderViewEntity = player;
        player.preparePlayerToSpawn();
        player.setServerBrand(s);
        world.spawnEntityInWorld(player);
        playerController.flipPlayer(player);
        player.movementInput = new MovementInputFromOptions(gameSettings);
        player.setEntityId(i);
        playerController.setPlayerCapabilities(player);
        player.setReducedDebug(entityplayersp.hasReducedDebug());

        if (screen instanceof DeathScreen) {
            openScreen(null);
        }
    }

    @Nullable
    public static NetHandlerPlayClient getConnection() {
        return player == null ? null : player.connection;
    }

    public static boolean isGuiEnabled() {
        return minecraft == null || !gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled() {
        return minecraft != null && gameSettings.fancyGraphics;
    }

    /**
     * Returns if ambient occlusion is enabled
     */
    public static boolean isAmbientOcclusionEnabled() {
        return minecraft != null && gameSettings.ambientOcclusion != 0;
    }

    /**
     * Called when user clicked he's mouse middle button (pick block)
     */
    private void middleClickMouse() {
        if (objectMouseOver != null && objectMouseOver.typeOfHit != RayTraceResult.Type.MISS) {
            boolean flag = player.capabilities.isCreativeMode;
            TileEntity tileentity = null;
            ItemStack itemstack;

            if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = objectMouseOver.getBlockPos();
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (iblockstate.getMaterial() == Material.AIR) {
                    return;
                }

                itemstack = block.getItem(world, blockpos, iblockstate);

                if (itemstack.isEmpty()) {
                    return;
                }

                if (flag && Screen.hasControlDown() && block.hasTileEntity()) {
                    tileentity = world.getTileEntity(blockpos);
                }
            } else {
                if (objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY || objectMouseOver.entityHit == null || !flag) {
                    return;
                }

                if (objectMouseOver.entityHit instanceof EntityPainting) {
                    itemstack = new ItemStack(Items.PAINTING);
                } else if (objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    itemstack = new ItemStack(Items.LEAD);
                } else if (objectMouseOver.entityHit instanceof EntityItemFrame) {
                    EntityItemFrame entityitemframe = (EntityItemFrame) objectMouseOver.entityHit;
                    ItemStack itemstack1 = entityitemframe.getDisplayedItem();

                    if (itemstack1.isEmpty()) {
                        itemstack = new ItemStack(Items.ITEM_FRAME);
                    } else {
                        itemstack = itemstack1.copy();
                    }
                } else if (objectMouseOver.entityHit instanceof EntityMinecart) {
                    EntityMinecart entityminecart = (EntityMinecart) objectMouseOver.entityHit;
                    Item item1;

                    switch (entityminecart.getType()) {
                        case FURNACE:
                            item1 = Items.FURNACE_MINECART;
                            break;

                        case CHEST:
                            item1 = Items.CHEST_MINECART;
                            break;

                        case TNT:
                            item1 = Items.TNT_MINECART;
                            break;

                        case HOPPER:
                            item1 = Items.HOPPER_MINECART;
                            break;

                        case COMMAND_BLOCK:
                            item1 = Items.COMMAND_BLOCK_MINECART;
                            break;

                        default:
                            item1 = Items.MINECART;
                    }

                    itemstack = new ItemStack(item1);
                } else if (objectMouseOver.entityHit instanceof EntityBoat) {
                    itemstack = new ItemStack(((EntityBoat) objectMouseOver.entityHit).getItemBoat());
                } else if (objectMouseOver.entityHit instanceof EntityArmorStand) {
                    itemstack = new ItemStack(Items.ARMOR_STAND);
                } else if (objectMouseOver.entityHit instanceof EntityEnderCrystal) {
                    itemstack = new ItemStack(Items.END_CRYSTAL);
                } else {
                    Namespaced resourcelocation = EntityList.func_191301_a(objectMouseOver.entityHit);

                    if (resourcelocation == null || !EntityList.ENTITY_EGGS.containsKey(resourcelocation)) {
                        return;
                    }

                    itemstack = new ItemStack(Items.SPAWN_EGG);
                    ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, resourcelocation);
                }
            }

            if (itemstack.isEmpty()) {
                String s = "";

                if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    s = Block.REGISTRY.getNameForObject(world.getBlockState(objectMouseOver.getBlockPos()).getBlock()).toString();
                } else if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                    s = EntityList.func_191301_a(objectMouseOver.entityHit).toString();
                }

                LOGGER.warn("Picking on: [{}] {} gave null item", objectMouseOver.typeOfHit, s);
            } else {
                InventoryPlayer inventoryplayer = player.inventory;

                if (tileentity != null) {
                    storeTEInStack(itemstack, tileentity);
                }

                int i = inventoryplayer.getSlotFor(itemstack);

                if (flag) {
                    inventoryplayer.setPickedItemStack(itemstack);
                    playerController.sendSlotPacket(player.getHeldItem(EnumHand.MAIN_HAND), 36 + inventoryplayer.currentItem);
                } else if (i != -1) {
                    if (InventoryPlayer.isHotbar(i)) {
                        inventoryplayer.currentItem = i;
                    } else {
                        playerController.pickItem(i);
                    }
                }
            }
        }
    }

    private ItemStack storeTEInStack(ItemStack stack, TileEntity te) {
        NBTTagCompound nbttagcompound = te.writeToNBT(new NBTTagCompound());

        if (stack.getItem() == Items.SKULL && nbttagcompound.hasKey("Owner")) {
            NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            nbttagcompound3.setTag("SkullOwner", nbttagcompound2);
            stack.setTagCompound(nbttagcompound3);
            return stack;
        } else {
            stack.setTagInfo("BlockEntityTag", nbttagcompound);
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            NBTTagList nbttaglist = new NBTTagList();
            nbttaglist.appendTag(new NBTTagString("(+NBT)"));
            nbttagcompound1.setTag("Lore", nbttaglist);
            stack.setTagInfo("display", nbttagcompound1);
            return stack;
        }
    }

    /**
     * adds core server Info (GL version , Texture pack, isModded, type), and the worldInfo to the crash report
     */
    public static CrashReport populateReport(CrashReport crash) {
        crash.getCategory().setDetail("Launched Version", () -> "1.12.2");
        crash.getCategory().setDetail("LWJGL", Sys::getVersion);
        crash.getCategory().setDetail("OpenGL", () -> GlStateManager.glGetString(7937) + " GL version " + GlStateManager.glGetString(7938) + ", " + GlStateManager.glGetString(7936));
        crash.getCategory().setDetail("GL Caps", OpenGlHelper::getLogText);
        crash.getCategory().setDetail("Using VBOs", () -> gameSettings.useVbo ? "Yes" : "No");
        crash.getCategory().setDetail("Is Modded", () -> "Definitely; Client brand changed to 'MoonWare'");
        crash.getCategory().setDetail("Type", () -> "Client (map_client.txt)");
        crash.getCategory().setDetail("Resource Packs", () -> {
            StringBuilder sb = new StringBuilder();
            for (String s : gameSettings.resourcePacks) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(s);
                if (gameSettings.incompatibleResourcePacks.contains(s)) sb.append(" (incompatible)");
            }
            return sb.toString();
        });
        crash.getCategory().setDetail("Current Language", () -> languageManager.getCurrentLanguage().toString());
        crash.getCategory().setDetail("Profiler Position", () -> profiler.profilingEnabled ? profiler.getNameOfLastSection() : "N/A (disabled)");
        crash.getCategory().setDetail("CPU", OpenGlHelper::getCpu);
        if (world != null) world.addWorldInfoToCrashReport(crash);
        return crash;
    }

    public void setServerData(ServerData serverData) {
        Minecraft.serverData = serverData;
    }

    public static ServerData getServerData() {
        return serverData;
    }

    public static boolean isIntegratedServerRunning() {
        return integratedServerIsRunning;
    }

    /**
     * Returns true if there is only one player playing, and the current server is the integrated one.
     */
    public static boolean isSingleplayer() {
        return integratedServerIsRunning && integratedServer != null;
    }

    @Nullable

    /**
     * Returns the currently running integrated server
     */
    public static IntegratedServer getIntegratedServer() {
        return integratedServer;
    }

    /**
     * Gets the system time in milliseconds.
     */
    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    public MusicTicker.MusicType getAmbientMusicType() {
        if (screen instanceof GuiWinGame) {
            return MusicTicker.MusicType.CREDITS;
        } else if (player != null) {
            if (player.world.provider instanceof WorldProviderHell) {
                return MusicTicker.MusicType.NETHER;
            } else if (player.world.provider instanceof WorldProviderEnd) {
                return ingameGUI.getBossbarHud().shouldPlayEndBossMusic() ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END;
            } else {
                return player.capabilities.isCreativeMode && player.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME;
            }
        } else {
            return MusicTicker.MusicType.MENU;
        }
    }

    public void dispatchKeypresses() {
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if (i != 0 && !Keyboard.isRepeatEvent()) {
            if (!(screen instanceof GuiControls) || ((GuiControls) screen).time <= getSystemTime() - 20L) {
                if (Keyboard.getEventKeyState()) {
                    if (i == gameSettings.keyBindFullscreen.getKeyCode()) {
                        toggleFullscreen();
                    } else if (i == gameSettings.keyBindScreenshot.getKeyCode()) {
                        Screenshot.saveScreenshot(this);
                    }
                }
            }
        }
    }

    public void setRenderViewEntity(Entity viewingEntity) {
        renderViewEntity = viewingEntity;
        gameRenderer.loadEntityShader(viewingEntity);
    }

    public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule) {
        if (isThisThread()) {
            try {
                return Futures.immediateFuture(callableToSchedule.call());
            } catch (Exception exception) {
                return Futures.immediateFailedFuture(exception);
            }
        } else {
            ListenableFutureTask<V> task = ListenableFutureTask.create(callableToSchedule);
            synchronized (scheduledTasks) {
                scheduledTasks.add(task);
                return task;
            }
        }
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
        Preconditions.checkNotNull(runnableToSchedule);
        return addScheduledTask(Executors.callable(runnableToSchedule));
    }

    public boolean isThisThread() {
        return Thread.currentThread() == mcThread;
    }

    public float getRenderPartialTicks() {
        return timer.renderPartialTicks;
    }

    public float func_193989_ak() {
        return timer.field_194148_c;
    }

    public boolean isReducedDebug() {
        return player != null && player.hasReducedDebug() || gameSettings.reducedDebugInfo;
    }

    public static Minecraft getMinecraft() {
        return minecraft;
    }

    public static int getScaledMouseX() {
        return (int) ((double)Mouse.getX() / (double)getScaleFactor());
    }

    public static int getScaledMouseY() {
        return (int) (getScaledHeight() - (double)Mouse.getY() / (double) getScaleFactor());
    }

}
