package optifine;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import javax.vecmath.Matrix4f;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.model.ModelEvokerFangs;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelVex;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderEvokerFangs;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.entity.RenderShulkerBullet;
import net.minecraft.client.renderer.entity.RenderWitherSkull;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityShulkerBoxRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IUnlistedProperty;

public class Reflector {
   private static boolean logForge = logEntry("*** Reflector Forge ***");
   public static ReflectorClass Attributes = new ReflectorClass("net.minecraftforge.client.model.Attributes");
   public static ReflectorField Attributes_DEFAULT_BAKED_FORMAT;
   public static ReflectorClass BetterFoliageClient;
   public static ReflectorClass BlamingTransformer;
   public static ReflectorMethod BlamingTransformer_onCrash;
   public static ReflectorClass ChunkWatchEvent_UnWatch;
   public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor;
   public static ReflectorClass CoreModManager;
   public static ReflectorMethod CoreModManager_onCrash;
   public static ReflectorClass DimensionManager;
   public static ReflectorMethod DimensionManager_createProviderFor;
   public static ReflectorMethod DimensionManager_getStaticDimensionIDs;
   public static ReflectorClass DrawScreenEvent_Pre;
   public static ReflectorConstructor DrawScreenEvent_Pre_Constructor;
   public static ReflectorClass DrawScreenEvent_Post;
   public static ReflectorConstructor DrawScreenEvent_Post_Constructor;
   public static ReflectorClass EntityViewRenderEvent_CameraSetup;
   public static ReflectorConstructor EntityViewRenderEvent_CameraSetup_Constructor;
   public static ReflectorMethod EntityViewRenderEvent_CameraSetup_getRoll;
   public static ReflectorMethod EntityViewRenderEvent_CameraSetup_getPitch;
   public static ReflectorMethod EntityViewRenderEvent_CameraSetup_getYaw;
   public static ReflectorClass EntityViewRenderEvent_FogColors;
   public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor;
   public static ReflectorMethod EntityViewRenderEvent_FogColors_getRed;
   public static ReflectorMethod EntityViewRenderEvent_FogColors_getGreen;
   public static ReflectorMethod EntityViewRenderEvent_FogColors_getBlue;
   public static ReflectorClass EntityViewRenderEvent_RenderFogEvent;
   public static ReflectorConstructor EntityViewRenderEvent_RenderFogEvent_Constructor;
   public static ReflectorClass Event;
   public static ReflectorMethod Event_isCanceled;
   public static ReflectorClass EventBus;
   public static ReflectorMethod EventBus_post;
   public static ReflectorClass Event_Result;
   public static ReflectorField Event_Result_DENY;
   public static ReflectorField Event_Result_ALLOW;
   public static ReflectorField Event_Result_DEFAULT;
   public static ReflectorClass ExtendedBlockState;
   public static ReflectorConstructor ExtendedBlockState_Constructor;
   public static ReflectorClass FMLClientHandler;
   public static ReflectorMethod FMLClientHandler_instance;
   public static ReflectorMethod FMLClientHandler_isLoading;
   public static ReflectorMethod FMLClientHandler_trackBrokenTexture;
   public static ReflectorMethod FMLClientHandler_trackMissingTexture;
   public static ReflectorClass FMLCommonHandler;
   public static ReflectorMethod FMLCommonHandler_callFuture;
   public static ReflectorMethod FMLCommonHandler_enhanceCrashReport;
   public static ReflectorMethod FMLCommonHandler_getBrandings;
   public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart;
   public static ReflectorMethod FMLCommonHandler_handleServerStarting;
   public static ReflectorMethod FMLCommonHandler_instance;
   public static ReflectorClass ForgeBiome;
   public static ReflectorMethod ForgeBiome_getWaterColorMultiplier;
   public static ReflectorClass ForgeBlock;
   public static ReflectorMethod ForgeBlock_addDestroyEffects;
   public static ReflectorMethod ForgeBlock_addHitEffects;
   public static ReflectorMethod ForgeBlock_canCreatureSpawn;
   public static ReflectorMethod ForgeBlock_canRenderInLayer;
   public static ReflectorMethod ForgeBlock_doesSideBlockRendering;
   public static ReflectorMethod ForgeBlock_getBedDirection;
   public static ReflectorMethod ForgeBlock_getExtendedState;
   public static ReflectorMethod ForgeBlock_getFogColor;
   public static ReflectorMethod ForgeBlock_getLightOpacity;
   public static ReflectorMethod ForgeBlock_getLightValue;
   public static ReflectorMethod ForgeBlock_getSoundType;
   public static ReflectorMethod ForgeBlock_hasTileEntity;
   public static ReflectorMethod ForgeBlock_isAir;
   public static ReflectorMethod ForgeBlock_isBed;
   public static ReflectorMethod ForgeBlock_isBedFoot;
   public static ReflectorMethod ForgeBlock_isSideSolid;
   public static ReflectorClass ForgeIBlockProperties;
   public static ReflectorMethod ForgeIBlockProperties_getLightValue2;
   public static ReflectorClass ForgeChunkCache;
   public static ReflectorMethod ForgeChunkCache_isSideSolid;
   public static ReflectorClass ForgeEntity;
   public static ReflectorMethod ForgeEntity_canRiderInteract;
   public static ReflectorField ForgeEntity_captureDrops;
   public static ReflectorField ForgeEntity_capturedDrops;
   public static ReflectorMethod ForgeEntity_shouldRenderInPass;
   public static ReflectorMethod ForgeEntity_shouldRiderSit;
   public static ReflectorClass ForgeEventFactory;
   public static ReflectorMethod ForgeEventFactory_canEntityDespawn;
   public static ReflectorMethod ForgeEventFactory_renderBlockOverlay;
   public static ReflectorMethod ForgeEventFactory_renderFireOverlay;
   public static ReflectorMethod ForgeEventFactory_renderWaterOverlay;
   public static ReflectorClass ForgeHooks;
   public static ReflectorMethod ForgeHooks_onLivingAttack;
   public static ReflectorMethod ForgeHooks_onLivingDeath;
   public static ReflectorMethod ForgeHooks_onLivingDrops;
   public static ReflectorMethod ForgeHooks_onLivingFall;
   public static ReflectorMethod ForgeHooks_onLivingHurt;
   public static ReflectorMethod ForgeHooks_onLivingJump;
   public static ReflectorMethod ForgeHooks_onLivingSetAttackTarget;
   public static ReflectorMethod ForgeHooks_onLivingUpdate;
   public static ReflectorClass ForgeHooksClient;
   public static ReflectorMethod ForgeHooksClient_applyTransform;
   public static ReflectorMethod ForgeHooksClient_applyUVLock;
   public static ReflectorMethod ForgeHooksClient_dispatchRenderLast;
   public static ReflectorMethod ForgeHooksClient_drawScreen;
   public static ReflectorMethod ForgeHooksClient_fillNormal;
   public static ReflectorMethod ForgeHooksClient_handleCameraTransforms;
   public static ReflectorMethod ForgeHooksClient_getArmorModel;
   public static ReflectorMethod ForgeHooksClient_getArmorTexture;
   public static ReflectorMethod ForgeHooksClient_getFogDensity;
   public static ReflectorMethod ForgeHooksClient_getFOVModifier;
   public static ReflectorMethod ForgeHooksClient_getMatrix;
   public static ReflectorMethod ForgeHooksClient_getOffsetFOV;
   public static ReflectorMethod ForgeHooksClient_loadEntityShader;
   public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight;
   public static ReflectorMethod ForgeHooksClient_onFogRender;
   public static ReflectorMethod ForgeHooksClient_onScreenshot;
   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre;
   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost;
   public static ReflectorMethod ForgeHooksClient_orientBedCamera;
   public static ReflectorMethod ForgeHooksClient_putQuadColor;
   public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand;
   public static ReflectorMethod ForgeHooksClient_renderMainMenu;
   public static ReflectorMethod ForgeHooksClient_renderSpecificFirstPersonHand;
   public static ReflectorMethod ForgeHooksClient_setRenderLayer;
   public static ReflectorMethod ForgeHooksClient_setRenderPass;
   public static ReflectorMethod ForgeHooksClient_shouldCauseReequipAnimation;
   public static ReflectorMethod ForgeHooksClient_transform;
   public static ReflectorClass ForgeItem;
   public static ReflectorField ForgeItem_delegate;
   public static ReflectorMethod ForgeItem_getDurabilityForDisplay;
   public static ReflectorMethod ForgeItem_getRGBDurabilityForDisplay;
   public static ReflectorMethod ForgeItem_isShield;
   public static ReflectorMethod ForgeItem_onEntitySwing;
   public static ReflectorMethod ForgeItem_shouldCauseReequipAnimation;
   public static ReflectorMethod ForgeItem_showDurabilityBar;
   public static ReflectorClass ForgeItemOverrideList;
   public static ReflectorMethod ForgeItemOverrideList_handleItemState;
   public static ReflectorClass ForgeItemArmor;
   public static ReflectorMethod ForgeItemArmor_hasOverlay;
   public static ReflectorClass ForgeKeyBinding;
   public static ReflectorMethod ForgeKeyBinding_setKeyConflictContext;
   public static ReflectorMethod ForgeKeyBinding_setKeyModifierAndCode;
   public static ReflectorMethod ForgeKeyBinding_getKeyModifier;
   public static ReflectorClass ForgeModContainer;
   public static ReflectorField ForgeModContainer_forgeLightPipelineEnabled;
   public static ReflectorClass ForgeModelBlockDefinition;
   public static ReflectorMethod ForgeModelBlockDefinition_parseFromReader2;
   public static ReflectorClass ForgePotion;
   public static ReflectorMethod ForgePotion_shouldRenderHUD;
   public static ReflectorMethod ForgePotion_renderHUDEffect;
   public static ReflectorClass ForgePotionEffect;
   public static ReflectorMethod ForgePotionEffect_isCurativeItem;
   public static ReflectorClass ForgeTileEntity;
   public static ReflectorMethod ForgeTileEntity_canRenderBreaking;
   public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox;
   public static ReflectorMethod ForgeTileEntity_hasFastRenderer;
   public static ReflectorMethod ForgeTileEntity_shouldRenderInPass;
   public static ReflectorClass ForgeVertexFormatElementEnumUseage;
   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_preDraw;
   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_postDraw;
   public static ReflectorClass ForgeWorld;
   public static ReflectorMethod ForgeWorld_countEntities;
   public static ReflectorMethod ForgeWorld_getPerWorldStorage;
   public static ReflectorMethod ForgeWorld_initCapabilities;
   public static ReflectorClass ForgeWorldProvider;
   public static ReflectorMethod ForgeWorldProvider_getCloudRenderer;
   public static ReflectorMethod ForgeWorldProvider_getSkyRenderer;
   public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer;
   public static ReflectorMethod ForgeWorldProvider_getLightmapColors;
   public static ReflectorClass GuiModList;
   public static ReflectorConstructor GuiModList_Constructor;
   public static ReflectorClass IExtendedBlockState;
   public static ReflectorMethod IExtendedBlockState_getClean;
   public static ReflectorClass IModel;
   public static ReflectorMethod IModel_getTextures;
   public static ReflectorClass IRenderHandler;
   public static ReflectorMethod IRenderHandler_render;
   public static ReflectorClass ItemModelMesherForge;
   public static ReflectorConstructor ItemModelMesherForge_Constructor;
   public static ReflectorClass KeyConflictContext;
   public static ReflectorField KeyConflictContext_IN_GAME;
   public static ReflectorClass KeyModifier;
   public static ReflectorMethod KeyModifier_valueFromString;
   public static ReflectorField KeyModifier_NONE;
   public static ReflectorClass Launch;
   public static ReflectorField Launch_blackboard;
   public static ReflectorClass LightUtil;
   public static ReflectorField LightUtil_itemConsumer;
   public static ReflectorMethod LightUtil_putBakedQuad;
   public static ReflectorMethod LightUtil_renderQuadColor;
   public static ReflectorField LightUtil_tessellator;
   public static ReflectorClass MinecraftForge;
   public static ReflectorField MinecraftForge_EVENT_BUS;
   public static ReflectorClass MinecraftForgeClient;
   public static ReflectorMethod MinecraftForgeClient_getRenderPass;
   public static ReflectorMethod MinecraftForgeClient_onRebuildChunk;
   public static ReflectorClass ModelLoader;
   public static ReflectorField ModelLoader_stateModels;
   public static ReflectorMethod ModelLoader_onRegisterItems;
   public static ReflectorMethod ModelLoader_getInventoryVariant;
   public static ReflectorClass ModelLoader_VanillaLoader;
   public static ReflectorField ModelLoader_VanillaLoader_INSTANCE;
   public static ReflectorMethod ModelLoader_VanillaLoader_loadModel;
   public static ReflectorClass ModelLoaderRegistry;
   public static ReflectorField ModelLoaderRegistry_textures;
   public static ReflectorClass NotificationModUpdateScreen;
   public static ReflectorMethod NotificationModUpdateScreen_init;
   public static ReflectorClass RenderBlockOverlayEvent_OverlayType;
   public static ReflectorField RenderBlockOverlayEvent_OverlayType_BLOCK;
   public static ReflectorClass RenderingRegistry;
   public static ReflectorMethod RenderingRegistry_loadEntityRenderers;
   public static ReflectorClass RenderItemInFrameEvent;
   public static ReflectorConstructor RenderItemInFrameEvent_Constructor;
   public static ReflectorClass RenderLivingEvent_Pre;
   public static ReflectorConstructor RenderLivingEvent_Pre_Constructor;
   public static ReflectorClass RenderLivingEvent_Post;
   public static ReflectorConstructor RenderLivingEvent_Post_Constructor;
   public static ReflectorClass RenderLivingEvent_Specials_Pre;
   public static ReflectorConstructor RenderLivingEvent_Specials_Pre_Constructor;
   public static ReflectorClass RenderLivingEvent_Specials_Post;
   public static ReflectorConstructor RenderLivingEvent_Specials_Post_Constructor;
   public static ReflectorClass ScreenshotEvent;
   public static ReflectorMethod ScreenshotEvent_getCancelMessage;
   public static ReflectorMethod ScreenshotEvent_getScreenshotFile;
   public static ReflectorMethod ScreenshotEvent_getResultMessage;
   public static ReflectorClass SplashScreen;
   public static ReflectorClass WorldEvent_Load;
   public static ReflectorConstructor WorldEvent_Load_Constructor;
   private static boolean logVanilla;
   public static ReflectorClass ChunkProviderClient;
   public static ReflectorField ChunkProviderClient_chunkMapping;
   public static ReflectorClass GuiMainMenu;
   public static ReflectorField GuiMainMenu_splashText;
   public static ReflectorClass LegacyV2Adapter;
   public static ReflectorField LegacyV2Adapter_pack;
   public static ReflectorClass Minecraft;
   public static ReflectorField Minecraft_defaultResourcePack;
   public static ReflectorField Minecraft_actionKeyF3;
   public static ReflectorClass ModelHumanoidHead;
   public static ReflectorField ModelHumanoidHead_head;
   public static ReflectorClass ModelBat;
   public static ReflectorFields ModelBat_ModelRenderers;
   public static ReflectorClass ModelBlaze;
   public static ReflectorField ModelBlaze_blazeHead;
   public static ReflectorField ModelBlaze_blazeSticks;
   public static ReflectorClass ModelDragon;
   public static ReflectorFields ModelDragon_ModelRenderers;
   public static ReflectorClass ModelEnderCrystal;
   public static ReflectorFields ModelEnderCrystal_ModelRenderers;
   public static ReflectorClass RenderEnderCrystal;
   public static ReflectorField RenderEnderCrystal_modelEnderCrystal;
   public static ReflectorField RenderEnderCrystal_modelEnderCrystalNoBase;
   public static ReflectorClass ModelEnderMite;
   public static ReflectorField ModelEnderMite_bodyParts;
   public static ReflectorClass ModelEvokerFangs;
   public static ReflectorFields ModelEvokerFangs_ModelRenderers;
   public static ReflectorClass ModelGhast;
   public static ReflectorField ModelGhast_body;
   public static ReflectorField ModelGhast_tentacles;
   public static ReflectorClass ModelGuardian;
   public static ReflectorField ModelGuardian_body;
   public static ReflectorField ModelGuardian_eye;
   public static ReflectorField ModelGuardian_spines;
   public static ReflectorField ModelGuardian_tail;
   public static ReflectorClass ModelDragonHead;
   public static ReflectorField ModelDragonHead_head;
   public static ReflectorField ModelDragonHead_jaw;
   public static ReflectorClass ModelHorse;
   public static ReflectorFields ModelHorse_ModelRenderers;
   public static ReflectorClass RenderLeashKnot;
   public static ReflectorField RenderLeashKnot_leashKnotModel;
   public static ReflectorClass ModelMagmaCube;
   public static ReflectorField ModelMagmaCube_core;
   public static ReflectorField ModelMagmaCube_segments;
   public static ReflectorClass ModelOcelot;
   public static ReflectorFields ModelOcelot_ModelRenderers;
   public static ReflectorClass ModelRabbit;
   public static ReflectorFields ModelRabbit_renderers;
   public static ReflectorClass ModelSilverfish;
   public static ReflectorField ModelSilverfish_bodyParts;
   public static ReflectorField ModelSilverfish_wingParts;
   public static ReflectorClass ModelSlime;
   public static ReflectorFields ModelSlime_ModelRenderers;
   public static ReflectorClass ModelSquid;
   public static ReflectorField ModelSquid_body;
   public static ReflectorField ModelSquid_tentacles;
   public static ReflectorClass ModelVex;
   public static ReflectorField ModelVex_leftWing;
   public static ReflectorField ModelVex_rightWing;
   public static ReflectorClass ModelWitch;
   public static ReflectorField ModelWitch_mole;
   public static ReflectorField ModelWitch_hat;
   public static ReflectorClass ModelWither;
   public static ReflectorField ModelWither_bodyParts;
   public static ReflectorField ModelWither_heads;
   public static ReflectorClass ModelWolf;
   public static ReflectorField ModelWolf_tail;
   public static ReflectorField ModelWolf_mane;
   public static ReflectorClass OptiFineClassTransformer;
   public static ReflectorField OptiFineClassTransformer_instance;
   public static ReflectorMethod OptiFineClassTransformer_getOptiFineResource;
   public static ReflectorClass RenderBoat;
   public static ReflectorField RenderBoat_modelBoat;
   public static ReflectorClass RenderEvokerFangs;
   public static ReflectorField RenderEvokerFangs_model;
   public static ReflectorClass RenderMinecart;
   public static ReflectorField RenderMinecart_modelMinecart;
   public static ReflectorClass RenderShulkerBullet;
   public static ReflectorField RenderShulkerBullet_model;
   public static ReflectorClass RenderWitherSkull;
   public static ReflectorField RenderWitherSkull_model;
   public static ReflectorClass TileEntityBannerRenderer;
   public static ReflectorField TileEntityBannerRenderer_bannerModel;
   public static ReflectorClass TileEntityChestRenderer;
   public static ReflectorField TileEntityChestRenderer_simpleChest;
   public static ReflectorField TileEntityChestRenderer_largeChest;
   public static ReflectorClass TileEntityEnchantmentTableRenderer;
   public static ReflectorField TileEntityEnchantmentTableRenderer_modelBook;
   public static ReflectorClass TileEntityEnderChestRenderer;
   public static ReflectorField TileEntityEnderChestRenderer_modelChest;
   public static ReflectorClass TileEntityShulkerBoxRenderer;
   public static ReflectorField TileEntityShulkerBoxRenderer_model;
   public static ReflectorClass TileEntitySignRenderer;
   public static ReflectorField TileEntitySignRenderer_model;
   public static ReflectorClass TileEntitySkullRenderer;
   public static ReflectorField TileEntitySkullRenderer_dragonHead;
   public static ReflectorField TileEntitySkullRenderer_skeletonHead;
   public static ReflectorField TileEntitySkullRenderer_humanoidHead;

   public static void callVoid(ReflectorMethod p_callVoid_0_, Object... p_callVoid_1_) {
      try {
         Method method = p_callVoid_0_.getTargetMethod();
         if (method == null) {
            return;
         }

         method.invoke((Object)null, p_callVoid_1_);
      } catch (Throwable var3) {
         handleException(var3, (Object)null, p_callVoid_0_, p_callVoid_1_);
      }

   }

   public static boolean callBoolean(ReflectorMethod p_callBoolean_0_, Object... p_callBoolean_1_) {
      try {
         Method method = p_callBoolean_0_.getTargetMethod();
         if (method == null) {
            return false;
         } else {
            Boolean obool = (Boolean)method.invoke((Object)null, p_callBoolean_1_);
            return obool;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callBoolean_0_, p_callBoolean_1_);
         return false;
      }
   }

   public static int callInt(ReflectorMethod p_callInt_0_, Object... p_callInt_1_) {
      try {
         Method method = p_callInt_0_.getTargetMethod();
         if (method == null) {
            return 0;
         } else {
            Integer integer = (Integer)method.invoke((Object)null, p_callInt_1_);
            return integer;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callInt_0_, p_callInt_1_);
         return 0;
      }
   }

   public static float callFloat(ReflectorMethod p_callFloat_0_, Object... p_callFloat_1_) {
      try {
         Method method = p_callFloat_0_.getTargetMethod();
         if (method == null) {
            return 0.0F;
         } else {
            Float f = (Float)method.invoke((Object)null, p_callFloat_1_);
            return f;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callFloat_0_, p_callFloat_1_);
         return 0.0F;
      }
   }

   public static double callDouble(ReflectorMethod p_callDouble_0_, Object... p_callDouble_1_) {
      try {
         Method method = p_callDouble_0_.getTargetMethod();
         if (method == null) {
            return 0.0D;
         } else {
            Double d0 = (Double)method.invoke((Object)null, p_callDouble_1_);
            return d0;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callDouble_0_, p_callDouble_1_);
         return 0.0D;
      }
   }

   public static String callString(ReflectorMethod p_callString_0_, Object... p_callString_1_) {
      try {
         Method method = p_callString_0_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            String s = (String)method.invoke((Object)null, p_callString_1_);
            return s;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_callString_0_, p_callString_1_);
         return null;
      }
   }

   public static Object call(ReflectorMethod p_call_0_, Object... p_call_1_) {
      try {
         Method method = p_call_0_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            Object object = method.invoke((Object)null, p_call_1_);
            return object;
         }
      } catch (Throwable var4) {
         handleException(var4, (Object)null, p_call_0_, p_call_1_);
         return null;
      }
   }

   public static void callVoid(Object p_callVoid_0_, ReflectorMethod p_callVoid_1_, Object... p_callVoid_2_) {
      try {
         if (p_callVoid_0_ == null) {
            return;
         }

         Method method = p_callVoid_1_.getTargetMethod();
         if (method == null) {
            return;
         }

         method.invoke(p_callVoid_0_, p_callVoid_2_);
      } catch (Throwable var4) {
         handleException(var4, p_callVoid_0_, p_callVoid_1_, p_callVoid_2_);
      }

   }

   public static boolean callBoolean(Object p_callBoolean_0_, ReflectorMethod p_callBoolean_1_, Object... p_callBoolean_2_) {
      try {
         Method method = p_callBoolean_1_.getTargetMethod();
         if (method == null) {
            return false;
         } else {
            Boolean obool = (Boolean)method.invoke(p_callBoolean_0_, p_callBoolean_2_);
            return obool;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callBoolean_0_, p_callBoolean_1_, p_callBoolean_2_);
         return false;
      }
   }

   public static int callInt(Object p_callInt_0_, ReflectorMethod p_callInt_1_, Object... p_callInt_2_) {
      try {
         Method method = p_callInt_1_.getTargetMethod();
         if (method == null) {
            return 0;
         } else {
            Integer integer = (Integer)method.invoke(p_callInt_0_, p_callInt_2_);
            return integer;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callInt_0_, p_callInt_1_, p_callInt_2_);
         return 0;
      }
   }

   public static float callFloat(Object p_callFloat_0_, ReflectorMethod p_callFloat_1_, Object... p_callFloat_2_) {
      try {
         Method method = p_callFloat_1_.getTargetMethod();
         if (method == null) {
            return 0.0F;
         } else {
            Float f = (Float)method.invoke(p_callFloat_0_, p_callFloat_2_);
            return f;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callFloat_0_, p_callFloat_1_, p_callFloat_2_);
         return 0.0F;
      }
   }

   public static double callDouble(Object p_callDouble_0_, ReflectorMethod p_callDouble_1_, Object... p_callDouble_2_) {
      try {
         Method method = p_callDouble_1_.getTargetMethod();
         if (method == null) {
            return 0.0D;
         } else {
            Double d0 = (Double)method.invoke(p_callDouble_0_, p_callDouble_2_);
            return d0;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callDouble_0_, p_callDouble_1_, p_callDouble_2_);
         return 0.0D;
      }
   }

   public static String callString(Object p_callString_0_, ReflectorMethod p_callString_1_, Object... p_callString_2_) {
      try {
         Method method = p_callString_1_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            String s = (String)method.invoke(p_callString_0_, p_callString_2_);
            return s;
         }
      } catch (Throwable var5) {
         handleException(var5, p_callString_0_, p_callString_1_, p_callString_2_);
         return null;
      }
   }

   public static Object call(Object p_call_0_, ReflectorMethod p_call_1_, Object... p_call_2_) {
      try {
         Method method = p_call_1_.getTargetMethod();
         if (method == null) {
            return null;
         } else {
            Object object = method.invoke(p_call_0_, p_call_2_);
            return object;
         }
      } catch (Throwable var5) {
         handleException(var5, p_call_0_, p_call_1_, p_call_2_);
         return null;
      }
   }

   public static Object getFieldValue(ReflectorField p_getFieldValue_0_) {
      return getFieldValue((Object)null, p_getFieldValue_0_);
   }

   public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorField p_getFieldValue_1_) {
      try {
         Field field = p_getFieldValue_1_.getTargetField();
         if (field == null) {
            return null;
         } else {
            Object object = field.get(p_getFieldValue_0_);
            return object;
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static Object getFieldValue(ReflectorFields p_getFieldValue_0_, int p_getFieldValue_1_) {
      ReflectorField reflectorfield = p_getFieldValue_0_.getReflectorField(p_getFieldValue_1_);
      return reflectorfield == null ? null : getFieldValue(reflectorfield);
   }

   public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorFields p_getFieldValue_1_, int p_getFieldValue_2_) {
      ReflectorField reflectorfield = p_getFieldValue_1_.getReflectorField(p_getFieldValue_2_);
      return reflectorfield == null ? null : getFieldValue(p_getFieldValue_0_, reflectorfield);
   }

   public static float getFieldValueFloat(Object p_getFieldValueFloat_0_, ReflectorField p_getFieldValueFloat_1_, float p_getFieldValueFloat_2_) {
      Object object = getFieldValue(p_getFieldValueFloat_0_, p_getFieldValueFloat_1_);
      if (!(object instanceof Float)) {
         return p_getFieldValueFloat_2_;
      } else {
         Float f = (Float)object;
         return f;
      }
   }

   public static boolean setFieldValue(ReflectorField p_setFieldValue_0_, Object p_setFieldValue_1_) {
      return setFieldValue((Object)null, p_setFieldValue_0_, p_setFieldValue_1_);
   }

   public static boolean setFieldValue(Object p_setFieldValue_0_, ReflectorField p_setFieldValue_1_, Object p_setFieldValue_2_) {
      try {
         Field field = p_setFieldValue_1_.getTargetField();
         if (field == null) {
            return false;
         } else {
            field.set(p_setFieldValue_0_, p_setFieldValue_2_);
            return true;
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public static boolean postForgeBusEvent(ReflectorConstructor p_postForgeBusEvent_0_, Object... p_postForgeBusEvent_1_) {
      Object object = newInstance(p_postForgeBusEvent_0_, p_postForgeBusEvent_1_);
      return object == null ? false : postForgeBusEvent(object);
   }

   public static boolean postForgeBusEvent(Object p_postForgeBusEvent_0_) {
      if (p_postForgeBusEvent_0_ == null) {
         return false;
      } else {
         Object object = getFieldValue(MinecraftForge_EVENT_BUS);
         if (object == null) {
            return false;
         } else {
            Object object1 = call(object, EventBus_post, p_postForgeBusEvent_0_);
            if (!(object1 instanceof Boolean)) {
               return false;
            } else {
               Boolean obool = (Boolean)object1;
               return obool;
            }
         }
      }
   }

   public static Object newInstance(ReflectorConstructor p_newInstance_0_, Object... p_newInstance_1_) {
      Constructor constructor = p_newInstance_0_.getTargetConstructor();
      if (constructor == null) {
         return null;
      } else {
         try {
            Object object = constructor.newInstance(p_newInstance_1_);
            return object;
         } catch (Throwable var4) {
            handleException(var4, p_newInstance_0_, p_newInstance_1_);
            return null;
         }
      }
   }

   public static boolean matchesTypes(Class[] p_matchesTypes_0_, Class[] p_matchesTypes_1_) {
      if (p_matchesTypes_0_.length != p_matchesTypes_1_.length) {
         return false;
      } else {
         for(int i = 0; i < p_matchesTypes_1_.length; ++i) {
            Class oclass = p_matchesTypes_0_[i];
            Class oclass1 = p_matchesTypes_1_[i];
            if (oclass != oclass1) {
               return false;
            }
         }

         return true;
      }
   }

   private static void dbgCall(boolean p_dbgCall_0_, String p_dbgCall_1_, ReflectorMethod p_dbgCall_2_, Object[] p_dbgCall_3_, Object p_dbgCall_4_) {
      String s = p_dbgCall_2_.getTargetMethod().getDeclaringClass().getName();
      String s1 = p_dbgCall_2_.getTargetMethod().getName();
      String s2 = "";
      if (p_dbgCall_0_) {
         s2 = " static";
      }

      Config.dbg(p_dbgCall_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCall_3_) + ") => " + p_dbgCall_4_);
   }

   private static void dbgCallVoid(boolean p_dbgCallVoid_0_, String p_dbgCallVoid_1_, ReflectorMethod p_dbgCallVoid_2_, Object[] p_dbgCallVoid_3_) {
      String s = p_dbgCallVoid_2_.getTargetMethod().getDeclaringClass().getName();
      String s1 = p_dbgCallVoid_2_.getTargetMethod().getName();
      String s2 = "";
      if (p_dbgCallVoid_0_) {
         s2 = " static";
      }

      Config.dbg(p_dbgCallVoid_1_ + s2 + " " + s + "." + s1 + "(" + Config.arrayToString(p_dbgCallVoid_3_) + ")");
   }

   private static void dbgFieldValue(boolean p_dbgFieldValue_0_, String p_dbgFieldValue_1_, ReflectorField p_dbgFieldValue_2_, Object p_dbgFieldValue_3_) {
      String s = p_dbgFieldValue_2_.getTargetField().getDeclaringClass().getName();
      String s1 = p_dbgFieldValue_2_.getTargetField().getName();
      String s2 = "";
      if (p_dbgFieldValue_0_) {
         s2 = " static";
      }

      Config.dbg(p_dbgFieldValue_1_ + s2 + " " + s + "." + s1 + " => " + p_dbgFieldValue_3_);
   }

   private static void handleException(Throwable p_handleException_0_, Object p_handleException_1_, ReflectorMethod p_handleException_2_, Object[] p_handleException_3_) {
      if (p_handleException_0_ instanceof InvocationTargetException) {
         Throwable throwable = p_handleException_0_.getCause();
         if (throwable instanceof RuntimeException) {
            RuntimeException runtimeexception = (RuntimeException)throwable;
            throw runtimeexception;
         }

         p_handleException_0_.printStackTrace();
      } else {
         if (p_handleException_0_ instanceof IllegalArgumentException) {
            Config.warn("*** IllegalArgumentException ***");
            Config.warn("Method: " + p_handleException_2_.getTargetMethod());
            Config.warn("Object: " + p_handleException_1_);
            Config.warn("Parameter classes: " + Config.arrayToString(getClasses(p_handleException_3_)));
            Config.warn("Parameters: " + Config.arrayToString(p_handleException_3_));
         }

         Config.warn("*** Exception outside of method ***");
         Config.warn("Method deactivated: " + p_handleException_2_.getTargetMethod());
         p_handleException_2_.deactivate();
         p_handleException_0_.printStackTrace();
      }

   }

   private static void handleException(Throwable p_handleException_0_, ReflectorConstructor p_handleException_1_, Object[] p_handleException_2_) {
      if (p_handleException_0_ instanceof InvocationTargetException) {
         p_handleException_0_.printStackTrace();
      } else {
         if (p_handleException_0_ instanceof IllegalArgumentException) {
            Config.warn("*** IllegalArgumentException ***");
            Config.warn("Constructor: " + p_handleException_1_.getTargetConstructor());
            Config.warn("Parameter classes: " + Config.arrayToString(getClasses(p_handleException_2_)));
            Config.warn("Parameters: " + Config.arrayToString(p_handleException_2_));
         }

         Config.warn("*** Exception outside of constructor ***");
         Config.warn("Constructor deactivated: " + p_handleException_1_.getTargetConstructor());
         p_handleException_1_.deactivate();
         p_handleException_0_.printStackTrace();
      }

   }

   private static Object[] getClasses(Object[] p_getClasses_0_) {
      if (p_getClasses_0_ == null) {
         return new Class[0];
      } else {
         Class[] aclass = new Class[p_getClasses_0_.length];

         for(int i = 0; i < aclass.length; ++i) {
            Object object = p_getClasses_0_[i];
            if (object != null) {
               aclass[i] = object.getClass();
            }
         }

         return aclass;
      }
   }

   private static ReflectorField[] getReflectorFields(ReflectorClass p_getReflectorFields_0_, Class p_getReflectorFields_1_, int p_getReflectorFields_2_) {
      ReflectorField[] areflectorfield = new ReflectorField[p_getReflectorFields_2_];

      for(int i = 0; i < areflectorfield.length; ++i) {
         areflectorfield[i] = new ReflectorField(p_getReflectorFields_0_, p_getReflectorFields_1_, i);
      }

      return areflectorfield;
   }

   private static boolean logEntry(String p_logEntry_0_) {
      Config.dbg(p_logEntry_0_);
      return true;
   }

   static {
      Attributes_DEFAULT_BAKED_FORMAT = new ReflectorField(Attributes, "DEFAULT_BAKED_FORMAT");
      BetterFoliageClient = new ReflectorClass("mods.betterfoliage.client.BetterFoliageClient");
      BlamingTransformer = new ReflectorClass("net.minecraftforge.fml.common.asm.transformers.BlamingTransformer");
      BlamingTransformer_onCrash = new ReflectorMethod(BlamingTransformer, "onCrash");
      ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
      ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(ChunkWatchEvent_UnWatch, new Class[]{ChunkPos.class, EntityPlayerMP.class});
      CoreModManager = new ReflectorClass("net.minecraftforge.fml.relauncher.CoreModManager");
      CoreModManager_onCrash = new ReflectorMethod(CoreModManager, "onCrash");
      DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
      DimensionManager_createProviderFor = new ReflectorMethod(DimensionManager, "createProviderFor");
      DimensionManager_getStaticDimensionIDs = new ReflectorMethod(DimensionManager, "getStaticDimensionIDs");
      DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
      DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(DrawScreenEvent_Pre, new Class[]{GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE});
      DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
      DrawScreenEvent_Post_Constructor = new ReflectorConstructor(DrawScreenEvent_Post, new Class[]{GuiScreen.class, Integer.TYPE, Integer.TYPE, Float.TYPE});
      EntityViewRenderEvent_CameraSetup = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup");
      EntityViewRenderEvent_CameraSetup_Constructor = new ReflectorConstructor(EntityViewRenderEvent_CameraSetup, new Class[]{EntityRenderer.class, Entity.class, IBlockState.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
      EntityViewRenderEvent_CameraSetup_getRoll = new ReflectorMethod(EntityViewRenderEvent_CameraSetup, "getRoll");
      EntityViewRenderEvent_CameraSetup_getPitch = new ReflectorMethod(EntityViewRenderEvent_CameraSetup, "getPitch");
      EntityViewRenderEvent_CameraSetup_getYaw = new ReflectorMethod(EntityViewRenderEvent_CameraSetup, "getYaw");
      EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
      EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogColors, new Class[]{EntityRenderer.class, Entity.class, IBlockState.class, Double.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
      EntityViewRenderEvent_FogColors_getRed = new ReflectorMethod(EntityViewRenderEvent_FogColors, "getRed");
      EntityViewRenderEvent_FogColors_getGreen = new ReflectorMethod(EntityViewRenderEvent_FogColors, "getGreen");
      EntityViewRenderEvent_FogColors_getBlue = new ReflectorMethod(EntityViewRenderEvent_FogColors, "getBlue");
      EntityViewRenderEvent_RenderFogEvent = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$RenderFogEvent");
      EntityViewRenderEvent_RenderFogEvent_Constructor = new ReflectorConstructor(EntityViewRenderEvent_RenderFogEvent, new Class[]{EntityRenderer.class, Entity.class, IBlockState.class, Double.TYPE, Integer.TYPE, Float.TYPE});
      Event = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event");
      Event_isCanceled = new ReflectorMethod(Event, "isCanceled");
      EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
      EventBus_post = new ReflectorMethod(EventBus, "post");
      Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
      Event_Result_DENY = new ReflectorField(Event_Result, "DENY");
      Event_Result_ALLOW = new ReflectorField(Event_Result, "ALLOW");
      Event_Result_DEFAULT = new ReflectorField(Event_Result, "DEFAULT");
      ExtendedBlockState = new ReflectorClass("net.minecraftforge.common.property.ExtendedBlockState");
      ExtendedBlockState_Constructor = new ReflectorConstructor(ExtendedBlockState, new Class[]{Block.class, IProperty[].class, IUnlistedProperty[].class});
      FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
      FMLClientHandler_instance = new ReflectorMethod(FMLClientHandler, "instance");
      FMLClientHandler_isLoading = new ReflectorMethod(FMLClientHandler, "isLoading");
      FMLClientHandler_trackBrokenTexture = new ReflectorMethod(FMLClientHandler, "trackBrokenTexture");
      FMLClientHandler_trackMissingTexture = new ReflectorMethod(FMLClientHandler, "trackMissingTexture");
      FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
      FMLCommonHandler_callFuture = new ReflectorMethod(FMLCommonHandler, "callFuture");
      FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(FMLCommonHandler, "enhanceCrashReport");
      FMLCommonHandler_getBrandings = new ReflectorMethod(FMLCommonHandler, "getBrandings");
      FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(FMLCommonHandler, "handleServerAboutToStart");
      FMLCommonHandler_handleServerStarting = new ReflectorMethod(FMLCommonHandler, "handleServerStarting");
      FMLCommonHandler_instance = new ReflectorMethod(FMLCommonHandler, "instance");
      ForgeBiome = new ReflectorClass(Biome.class);
      ForgeBiome_getWaterColorMultiplier = new ReflectorMethod(ForgeBiome, "getWaterColorMultiplier");
      ForgeBlock = new ReflectorClass(Block.class);
      ForgeBlock_addDestroyEffects = new ReflectorMethod(ForgeBlock, "addDestroyEffects");
      ForgeBlock_addHitEffects = new ReflectorMethod(ForgeBlock, "addHitEffects");
      ForgeBlock_canCreatureSpawn = new ReflectorMethod(ForgeBlock, "canCreatureSpawn");
      ForgeBlock_canRenderInLayer = new ReflectorMethod(ForgeBlock, "canRenderInLayer", new Class[]{IBlockState.class, BlockRenderLayer.class});
      ForgeBlock_doesSideBlockRendering = new ReflectorMethod(ForgeBlock, "doesSideBlockRendering");
      ForgeBlock_getBedDirection = new ReflectorMethod(ForgeBlock, "getBedDirection");
      ForgeBlock_getExtendedState = new ReflectorMethod(ForgeBlock, "getExtendedState");
      ForgeBlock_getFogColor = new ReflectorMethod(ForgeBlock, "getFogColor");
      ForgeBlock_getLightOpacity = new ReflectorMethod(ForgeBlock, "getLightOpacity", new Class[]{IBlockState.class, IBlockAccess.class, BlockPos.class});
      ForgeBlock_getLightValue = new ReflectorMethod(ForgeBlock, "getLightValue", new Class[]{IBlockState.class, IBlockAccess.class, BlockPos.class});
      ForgeBlock_getSoundType = new ReflectorMethod(ForgeBlock, "getSoundType", new Class[]{IBlockState.class, World.class, BlockPos.class, Entity.class});
      ForgeBlock_hasTileEntity = new ReflectorMethod(ForgeBlock, "hasTileEntity", new Class[]{IBlockState.class});
      ForgeBlock_isAir = new ReflectorMethod(ForgeBlock, "isAir");
      ForgeBlock_isBed = new ReflectorMethod(ForgeBlock, "isBed");
      ForgeBlock_isBedFoot = new ReflectorMethod(ForgeBlock, "isBedFoot");
      ForgeBlock_isSideSolid = new ReflectorMethod(ForgeBlock, "isSideSolid");
      ForgeIBlockProperties = new ReflectorClass(IBlockProperties.class);
      ForgeIBlockProperties_getLightValue2 = new ReflectorMethod(ForgeIBlockProperties, "getLightValue", new Class[]{IBlockAccess.class, BlockPos.class});
      ForgeChunkCache = new ReflectorClass(ChunkCache.class);
      ForgeChunkCache_isSideSolid = new ReflectorMethod(ForgeChunkCache, "isSideSolid");
      ForgeEntity = new ReflectorClass(Entity.class);
      ForgeEntity_canRiderInteract = new ReflectorMethod(ForgeEntity, "canRiderInteract");
      ForgeEntity_captureDrops = new ReflectorField(ForgeEntity, "captureDrops");
      ForgeEntity_capturedDrops = new ReflectorField(ForgeEntity, "capturedDrops");
      ForgeEntity_shouldRenderInPass = new ReflectorMethod(ForgeEntity, "shouldRenderInPass");
      ForgeEntity_shouldRiderSit = new ReflectorMethod(ForgeEntity, "shouldRiderSit");
      ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
      ForgeEventFactory_canEntityDespawn = new ReflectorMethod(ForgeEventFactory, "canEntityDespawn");
      ForgeEventFactory_renderBlockOverlay = new ReflectorMethod(ForgeEventFactory, "renderBlockOverlay");
      ForgeEventFactory_renderFireOverlay = new ReflectorMethod(ForgeEventFactory, "renderFireOverlay");
      ForgeEventFactory_renderWaterOverlay = new ReflectorMethod(ForgeEventFactory, "renderWaterOverlay");
      ForgeHooks = new ReflectorClass("net.minecraftforge.common.ForgeHooks");
      ForgeHooks_onLivingAttack = new ReflectorMethod(ForgeHooks, "onLivingAttack");
      ForgeHooks_onLivingDeath = new ReflectorMethod(ForgeHooks, "onLivingDeath");
      ForgeHooks_onLivingDrops = new ReflectorMethod(ForgeHooks, "onLivingDrops");
      ForgeHooks_onLivingFall = new ReflectorMethod(ForgeHooks, "onLivingFall");
      ForgeHooks_onLivingHurt = new ReflectorMethod(ForgeHooks, "onLivingHurt");
      ForgeHooks_onLivingJump = new ReflectorMethod(ForgeHooks, "onLivingJump");
      ForgeHooks_onLivingSetAttackTarget = new ReflectorMethod(ForgeHooks, "onLivingSetAttackTarget");
      ForgeHooks_onLivingUpdate = new ReflectorMethod(ForgeHooks, "onLivingUpdate");
      ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
      ForgeHooksClient_applyTransform = new ReflectorMethod(ForgeHooksClient, "applyTransform", new Class[]{Matrix4f.class, Optional.class});
      ForgeHooksClient_applyUVLock = new ReflectorMethod(ForgeHooksClient, "applyUVLock");
      ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(ForgeHooksClient, "dispatchRenderLast");
      ForgeHooksClient_drawScreen = new ReflectorMethod(ForgeHooksClient, "drawScreen");
      ForgeHooksClient_fillNormal = new ReflectorMethod(ForgeHooksClient, "fillNormal");
      ForgeHooksClient_handleCameraTransforms = new ReflectorMethod(ForgeHooksClient, "handleCameraTransforms");
      ForgeHooksClient_getArmorModel = new ReflectorMethod(ForgeHooksClient, "getArmorModel");
      ForgeHooksClient_getArmorTexture = new ReflectorMethod(ForgeHooksClient, "getArmorTexture");
      ForgeHooksClient_getFogDensity = new ReflectorMethod(ForgeHooksClient, "getFogDensity");
      ForgeHooksClient_getFOVModifier = new ReflectorMethod(ForgeHooksClient, "getFOVModifier");
      ForgeHooksClient_getMatrix = new ReflectorMethod(ForgeHooksClient, "getMatrix", new Class[]{ModelRotation.class});
      ForgeHooksClient_getOffsetFOV = new ReflectorMethod(ForgeHooksClient, "getOffsetFOV");
      ForgeHooksClient_loadEntityShader = new ReflectorMethod(ForgeHooksClient, "loadEntityShader");
      ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(ForgeHooksClient, "onDrawBlockHighlight");
      ForgeHooksClient_onFogRender = new ReflectorMethod(ForgeHooksClient, "onFogRender");
      ForgeHooksClient_onScreenshot = new ReflectorMethod(ForgeHooksClient, "onScreenshot");
      ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPre");
      ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPost");
      ForgeHooksClient_orientBedCamera = new ReflectorMethod(ForgeHooksClient, "orientBedCamera");
      ForgeHooksClient_putQuadColor = new ReflectorMethod(ForgeHooksClient, "putQuadColor");
      ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(ForgeHooksClient, "renderFirstPersonHand");
      ForgeHooksClient_renderMainMenu = new ReflectorMethod(ForgeHooksClient, "renderMainMenu");
      ForgeHooksClient_renderSpecificFirstPersonHand = new ReflectorMethod(ForgeHooksClient, "renderSpecificFirstPersonHand");
      ForgeHooksClient_setRenderLayer = new ReflectorMethod(ForgeHooksClient, "setRenderLayer");
      ForgeHooksClient_setRenderPass = new ReflectorMethod(ForgeHooksClient, "setRenderPass");
      ForgeHooksClient_shouldCauseReequipAnimation = new ReflectorMethod(ForgeHooksClient, "shouldCauseReequipAnimation");
      ForgeHooksClient_transform = new ReflectorMethod(ForgeHooksClient, "transform");
      ForgeItem = new ReflectorClass(Item.class);
      ForgeItem_delegate = new ReflectorField(ForgeItem, "delegate");
      ForgeItem_getDurabilityForDisplay = new ReflectorMethod(ForgeItem, "getDurabilityForDisplay");
      ForgeItem_getRGBDurabilityForDisplay = new ReflectorMethod(ForgeItem, "getRGBDurabilityForDisplay");
      ForgeItem_isShield = new ReflectorMethod(ForgeItem, "isShield");
      ForgeItem_onEntitySwing = new ReflectorMethod(ForgeItem, "onEntitySwing");
      ForgeItem_shouldCauseReequipAnimation = new ReflectorMethod(ForgeItem, "shouldCauseReequipAnimation");
      ForgeItem_showDurabilityBar = new ReflectorMethod(ForgeItem, "showDurabilityBar");
      ForgeItemOverrideList = new ReflectorClass(ItemOverrideList.class);
      ForgeItemOverrideList_handleItemState = new ReflectorMethod(ForgeItemOverrideList, "handleItemState");
      ForgeItemArmor = new ReflectorClass(ItemArmor.class);
      ForgeItemArmor_hasOverlay = new ReflectorMethod(ForgeItemArmor, "hasOverlay");
      ForgeKeyBinding = new ReflectorClass(KeyBinding.class);
      ForgeKeyBinding_setKeyConflictContext = new ReflectorMethod(ForgeKeyBinding, "setKeyConflictContext");
      ForgeKeyBinding_setKeyModifierAndCode = new ReflectorMethod(ForgeKeyBinding, "setKeyModifierAndCode");
      ForgeKeyBinding_getKeyModifier = new ReflectorMethod(ForgeKeyBinding, "getKeyModifier");
      ForgeModContainer = new ReflectorClass("net.minecraftforge.common.ForgeModContainer");
      ForgeModContainer_forgeLightPipelineEnabled = new ReflectorField(ForgeModContainer, "forgeLightPipelineEnabled");
      ForgeModelBlockDefinition = new ReflectorClass(ModelBlockDefinition.class);
      ForgeModelBlockDefinition_parseFromReader2 = new ReflectorMethod(ForgeModelBlockDefinition, "parseFromReader", new Class[]{Reader.class, ResourceLocation.class});
      ForgePotion = new ReflectorClass(Potion.class);
      ForgePotion_shouldRenderHUD = ForgePotion.makeMethod("shouldRenderHUD");
      ForgePotion_renderHUDEffect = ForgePotion.makeMethod("renderHUDEffect");
      ForgePotionEffect = new ReflectorClass(PotionEffect.class);
      ForgePotionEffect_isCurativeItem = new ReflectorMethod(ForgePotionEffect, "isCurativeItem");
      ForgeTileEntity = new ReflectorClass(TileEntity.class);
      ForgeTileEntity_canRenderBreaking = new ReflectorMethod(ForgeTileEntity, "canRenderBreaking");
      ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(ForgeTileEntity, "getRenderBoundingBox");
      ForgeTileEntity_hasFastRenderer = new ReflectorMethod(ForgeTileEntity, "hasFastRenderer");
      ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(ForgeTileEntity, "shouldRenderInPass");
      ForgeVertexFormatElementEnumUseage = new ReflectorClass(VertexFormatElement.EnumUsage.class);
      ForgeVertexFormatElementEnumUseage_preDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "preDraw");
      ForgeVertexFormatElementEnumUseage_postDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "postDraw");
      ForgeWorld = new ReflectorClass(World.class);
      ForgeWorld_countEntities = new ReflectorMethod(ForgeWorld, "countEntities", new Class[]{EnumCreatureType.class, Boolean.TYPE});
      ForgeWorld_getPerWorldStorage = new ReflectorMethod(ForgeWorld, "getPerWorldStorage");
      ForgeWorld_initCapabilities = new ReflectorMethod(ForgeWorld, "initCapabilities");
      ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
      ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(ForgeWorldProvider, "getCloudRenderer");
      ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(ForgeWorldProvider, "getSkyRenderer");
      ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(ForgeWorldProvider, "getWeatherRenderer");
      ForgeWorldProvider_getLightmapColors = new ReflectorMethod(ForgeWorldProvider, "getLightmapColors");
      GuiModList = new ReflectorClass("net.minecraftforge.fml.client.GuiModList");
      GuiModList_Constructor = new ReflectorConstructor(GuiModList, new Class[]{GuiScreen.class});
      IExtendedBlockState = new ReflectorClass("net.minecraftforge.common.property.IExtendedBlockState");
      IExtendedBlockState_getClean = new ReflectorMethod(IExtendedBlockState, "getClean");
      IModel = new ReflectorClass("net.minecraftforge.client.model.IModel");
      IModel_getTextures = new ReflectorMethod(IModel, "getTextures");
      IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
      IRenderHandler_render = new ReflectorMethod(IRenderHandler, "render");
      ItemModelMesherForge = new ReflectorClass("net.minecraftforge.client.ItemModelMesherForge");
      ItemModelMesherForge_Constructor = new ReflectorConstructor(ItemModelMesherForge, new Class[]{ModelManager.class});
      KeyConflictContext = new ReflectorClass("net.minecraftforge.client.settings.KeyConflictContext");
      KeyConflictContext_IN_GAME = new ReflectorField(KeyConflictContext, "IN_GAME");
      KeyModifier = new ReflectorClass("net.minecraftforge.client.settings.KeyModifier");
      KeyModifier_valueFromString = new ReflectorMethod(KeyModifier, "valueFromString");
      KeyModifier_NONE = new ReflectorField(KeyModifier, "NONE");
      Launch = new ReflectorClass("net.minecraft.launchwrapper.Launch");
      Launch_blackboard = new ReflectorField(Launch, "blackboard");
      LightUtil = new ReflectorClass("net.minecraftforge.client.model.pipeline.LightUtil");
      LightUtil_itemConsumer = new ReflectorField(LightUtil, "itemConsumer");
      LightUtil_putBakedQuad = new ReflectorMethod(LightUtil, "putBakedQuad");
      LightUtil_renderQuadColor = new ReflectorMethod(LightUtil, "renderQuadColor");
      LightUtil_tessellator = new ReflectorField(LightUtil, "tessellator");
      MinecraftForge = new ReflectorClass("net.minecraftforge.common.MinecraftForge");
      MinecraftForge_EVENT_BUS = new ReflectorField(MinecraftForge, "EVENT_BUS");
      MinecraftForgeClient = new ReflectorClass("net.minecraftforge.client.MinecraftForgeClient");
      MinecraftForgeClient_getRenderPass = new ReflectorMethod(MinecraftForgeClient, "getRenderPass");
      MinecraftForgeClient_onRebuildChunk = new ReflectorMethod(MinecraftForgeClient, "onRebuildChunk");
      ModelLoader = new ReflectorClass("net.minecraftforge.client.model.ModelLoader");
      ModelLoader_stateModels = new ReflectorField(ModelLoader, "stateModels");
      ModelLoader_onRegisterItems = new ReflectorMethod(ModelLoader, "onRegisterItems");
      ModelLoader_getInventoryVariant = new ReflectorMethod(ModelLoader, "getInventoryVariant");
      ModelLoader_VanillaLoader = new ReflectorClass("net.minecraftforge.client.model.ModelLoader$VanillaLoader", true);
      ModelLoader_VanillaLoader_INSTANCE = new ReflectorField(ModelLoader_VanillaLoader, "INSTANCE", true);
      ModelLoader_VanillaLoader_loadModel = new ReflectorMethod(ModelLoader_VanillaLoader, "loadModel", (Class[])null, true);
      ModelLoaderRegistry = new ReflectorClass("net.minecraftforge.client.model.ModelLoaderRegistry", true);
      ModelLoaderRegistry_textures = new ReflectorField(ModelLoaderRegistry, "textures", true);
      NotificationModUpdateScreen = new ReflectorClass("net.minecraftforge.client.gui.NotificationModUpdateScreen");
      NotificationModUpdateScreen_init = new ReflectorMethod(NotificationModUpdateScreen, "init");
      RenderBlockOverlayEvent_OverlayType = new ReflectorClass("net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType");
      RenderBlockOverlayEvent_OverlayType_BLOCK = new ReflectorField(RenderBlockOverlayEvent_OverlayType, "BLOCK");
      RenderingRegistry = new ReflectorClass("net.minecraftforge.fml.client.registry.RenderingRegistry");
      RenderingRegistry_loadEntityRenderers = new ReflectorMethod(RenderingRegistry, "loadEntityRenderers", new Class[]{RenderManager.class, Map.class});
      RenderItemInFrameEvent = new ReflectorClass("net.minecraftforge.client.event.RenderItemInFrameEvent");
      RenderItemInFrameEvent_Constructor = new ReflectorConstructor(RenderItemInFrameEvent, new Class[]{EntityItemFrame.class, RenderItemFrame.class});
      RenderLivingEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Pre");
      RenderLivingEvent_Pre_Constructor = new ReflectorConstructor(RenderLivingEvent_Pre, new Class[]{EntityLivingBase.class, RenderLivingBase.class, Float.TYPE, Double.TYPE, Double.TYPE, Double.TYPE});
      RenderLivingEvent_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Post");
      RenderLivingEvent_Post_Constructor = new ReflectorConstructor(RenderLivingEvent_Post, new Class[]{EntityLivingBase.class, RenderLivingBase.class, Float.TYPE, Double.TYPE, Double.TYPE, Double.TYPE});
      RenderLivingEvent_Specials_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre");
      RenderLivingEvent_Specials_Pre_Constructor = new ReflectorConstructor(RenderLivingEvent_Specials_Pre, new Class[]{EntityLivingBase.class, RenderLivingBase.class, Double.TYPE, Double.TYPE, Double.TYPE});
      RenderLivingEvent_Specials_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Post");
      RenderLivingEvent_Specials_Post_Constructor = new ReflectorConstructor(RenderLivingEvent_Specials_Post, new Class[]{EntityLivingBase.class, RenderLivingBase.class, Double.TYPE, Double.TYPE, Double.TYPE});
      ScreenshotEvent = new ReflectorClass("net.minecraftforge.client.event.ScreenshotEvent");
      ScreenshotEvent_getCancelMessage = new ReflectorMethod(ScreenshotEvent, "getCancelMessage");
      ScreenshotEvent_getScreenshotFile = new ReflectorMethod(ScreenshotEvent, "getScreenshotFile");
      ScreenshotEvent_getResultMessage = new ReflectorMethod(ScreenshotEvent, "getResultMessage");
      SplashScreen = new ReflectorClass("net.minecraftforge.fml.client.SplashProgress");
      WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
      WorldEvent_Load_Constructor = new ReflectorConstructor(WorldEvent_Load, new Class[]{World.class});
      logVanilla = logEntry("*** Reflector Vanilla ***");
      ChunkProviderClient = new ReflectorClass(ChunkProviderClient.class);
      ChunkProviderClient_chunkMapping = new ReflectorField(ChunkProviderClient, Long2ObjectMap.class);
      GuiMainMenu = new ReflectorClass(GuiMainMenu.class);
      GuiMainMenu_splashText = new ReflectorField(GuiMainMenu, String.class);
      LegacyV2Adapter = new ReflectorClass(LegacyV2Adapter.class);
      LegacyV2Adapter_pack = new ReflectorField(LegacyV2Adapter, IResourcePack.class);
      Minecraft = new ReflectorClass(Minecraft.class);
      Minecraft_defaultResourcePack = new ReflectorField(Minecraft, DefaultResourcePack.class);
      Minecraft_actionKeyF3 = new ReflectorField(new FieldLocatorActionKeyF3());
      ModelHumanoidHead = new ReflectorClass(ModelHumanoidHead.class);
      ModelHumanoidHead_head = new ReflectorField(ModelHumanoidHead, ModelRenderer.class);
      ModelBat = new ReflectorClass(ModelBat.class);
      ModelBat_ModelRenderers = new ReflectorFields(ModelBat, ModelRenderer.class, 6);
      ModelBlaze = new ReflectorClass(ModelBlaze.class);
      ModelBlaze_blazeHead = new ReflectorField(ModelBlaze, ModelRenderer.class);
      ModelBlaze_blazeSticks = new ReflectorField(ModelBlaze, ModelRenderer[].class);
      ModelDragon = new ReflectorClass(ModelDragon.class);
      ModelDragon_ModelRenderers = new ReflectorFields(ModelDragon, ModelRenderer.class, 12);
      ModelEnderCrystal = new ReflectorClass(ModelEnderCrystal.class);
      ModelEnderCrystal_ModelRenderers = new ReflectorFields(ModelEnderCrystal, ModelRenderer.class, 3);
      RenderEnderCrystal = new ReflectorClass(RenderEnderCrystal.class);
      RenderEnderCrystal_modelEnderCrystal = new ReflectorField(RenderEnderCrystal, ModelBase.class, 0);
      RenderEnderCrystal_modelEnderCrystalNoBase = new ReflectorField(RenderEnderCrystal, ModelBase.class, 1);
      ModelEnderMite = new ReflectorClass(ModelEnderMite.class);
      ModelEnderMite_bodyParts = new ReflectorField(ModelEnderMite, ModelRenderer[].class);
      ModelEvokerFangs = new ReflectorClass(ModelEvokerFangs.class);
      ModelEvokerFangs_ModelRenderers = new ReflectorFields(ModelEvokerFangs, ModelRenderer.class, 3);
      ModelGhast = new ReflectorClass(ModelGhast.class);
      ModelGhast_body = new ReflectorField(ModelGhast, ModelRenderer.class);
      ModelGhast_tentacles = new ReflectorField(ModelGhast, ModelRenderer[].class);
      ModelGuardian = new ReflectorClass(ModelGuardian.class);
      ModelGuardian_body = new ReflectorField(ModelGuardian, ModelRenderer.class, 0);
      ModelGuardian_eye = new ReflectorField(ModelGuardian, ModelRenderer.class, 1);
      ModelGuardian_spines = new ReflectorField(ModelGuardian, ModelRenderer[].class, 0);
      ModelGuardian_tail = new ReflectorField(ModelGuardian, ModelRenderer[].class, 1);
      ModelDragonHead = new ReflectorClass(ModelDragonHead.class);
      ModelDragonHead_head = new ReflectorField(ModelDragonHead, ModelRenderer.class, 0);
      ModelDragonHead_jaw = new ReflectorField(ModelDragonHead, ModelRenderer.class, 1);
      ModelHorse = new ReflectorClass(ModelHorse.class);
      ModelHorse_ModelRenderers = new ReflectorFields(ModelHorse, ModelRenderer.class, 39);
      RenderLeashKnot = new ReflectorClass(RenderLeashKnot.class);
      RenderLeashKnot_leashKnotModel = new ReflectorField(RenderLeashKnot, ModelLeashKnot.class);
      ModelMagmaCube = new ReflectorClass(ModelMagmaCube.class);
      ModelMagmaCube_core = new ReflectorField(ModelMagmaCube, ModelRenderer.class);
      ModelMagmaCube_segments = new ReflectorField(ModelMagmaCube, ModelRenderer[].class);
      ModelOcelot = new ReflectorClass(ModelOcelot.class);
      ModelOcelot_ModelRenderers = new ReflectorFields(ModelOcelot, ModelRenderer.class, 8);
      ModelRabbit = new ReflectorClass(ModelRabbit.class);
      ModelRabbit_renderers = new ReflectorFields(ModelRabbit, ModelRenderer.class, 12);
      ModelSilverfish = new ReflectorClass(ModelSilverfish.class);
      ModelSilverfish_bodyParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 0);
      ModelSilverfish_wingParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 1);
      ModelSlime = new ReflectorClass(ModelSlime.class);
      ModelSlime_ModelRenderers = new ReflectorFields(ModelSlime, ModelRenderer.class, 4);
      ModelSquid = new ReflectorClass(ModelSquid.class);
      ModelSquid_body = new ReflectorField(ModelSquid, ModelRenderer.class);
      ModelSquid_tentacles = new ReflectorField(ModelSquid, ModelRenderer[].class);
      ModelVex = new ReflectorClass(ModelVex.class);
      ModelVex_leftWing = new ReflectorField(ModelVex, ModelRenderer.class, 0);
      ModelVex_rightWing = new ReflectorField(ModelVex, ModelRenderer.class, 1);
      ModelWitch = new ReflectorClass(ModelWitch.class);
      ModelWitch_mole = new ReflectorField(ModelWitch, ModelRenderer.class, 0);
      ModelWitch_hat = new ReflectorField(ModelWitch, ModelRenderer.class, 1);
      ModelWither = new ReflectorClass(ModelWither.class);
      ModelWither_bodyParts = new ReflectorField(ModelWither, ModelRenderer[].class, 0);
      ModelWither_heads = new ReflectorField(ModelWither, ModelRenderer[].class, 1);
      ModelWolf = new ReflectorClass(ModelWolf.class);
      ModelWolf_tail = new ReflectorField(ModelWolf, ModelRenderer.class, 6);
      ModelWolf_mane = new ReflectorField(ModelWolf, ModelRenderer.class, 7);
      OptiFineClassTransformer = new ReflectorClass("optifine.OptiFineClassTransformer");
      OptiFineClassTransformer_instance = new ReflectorField(OptiFineClassTransformer, "instance");
      OptiFineClassTransformer_getOptiFineResource = new ReflectorMethod(OptiFineClassTransformer, "getOptiFineResource");
      RenderBoat = new ReflectorClass(RenderBoat.class);
      RenderBoat_modelBoat = new ReflectorField(RenderBoat, ModelBase.class);
      RenderEvokerFangs = new ReflectorClass(RenderEvokerFangs.class);
      RenderEvokerFangs_model = new ReflectorField(RenderEvokerFangs, ModelEvokerFangs.class);
      RenderMinecart = new ReflectorClass(RenderMinecart.class);
      RenderMinecart_modelMinecart = new ReflectorField(RenderMinecart, ModelBase.class);
      RenderShulkerBullet = new ReflectorClass(RenderShulkerBullet.class);
      RenderShulkerBullet_model = new ReflectorField(RenderShulkerBullet, ModelShulkerBullet.class);
      RenderWitherSkull = new ReflectorClass(RenderWitherSkull.class);
      RenderWitherSkull_model = new ReflectorField(RenderWitherSkull, ModelSkeletonHead.class);
      TileEntityBannerRenderer = new ReflectorClass(TileEntityBannerRenderer.class);
      TileEntityBannerRenderer_bannerModel = new ReflectorField(TileEntityBannerRenderer, ModelBanner.class);
      TileEntityChestRenderer = new ReflectorClass(TileEntityChestRenderer.class);
      TileEntityChestRenderer_simpleChest = new ReflectorField(TileEntityChestRenderer, ModelChest.class, 0);
      TileEntityChestRenderer_largeChest = new ReflectorField(TileEntityChestRenderer, ModelChest.class, 1);
      TileEntityEnchantmentTableRenderer = new ReflectorClass(TileEntityEnchantmentTableRenderer.class);
      TileEntityEnchantmentTableRenderer_modelBook = new ReflectorField(TileEntityEnchantmentTableRenderer, ModelBook.class);
      TileEntityEnderChestRenderer = new ReflectorClass(TileEntityEnderChestRenderer.class);
      TileEntityEnderChestRenderer_modelChest = new ReflectorField(TileEntityEnderChestRenderer, ModelChest.class);
      TileEntityShulkerBoxRenderer = new ReflectorClass(TileEntityShulkerBoxRenderer.class);
      TileEntityShulkerBoxRenderer_model = new ReflectorField(TileEntityShulkerBoxRenderer, ModelShulker.class);
      TileEntitySignRenderer = new ReflectorClass(TileEntitySignRenderer.class);
      TileEntitySignRenderer_model = new ReflectorField(TileEntitySignRenderer, ModelSign.class);
      TileEntitySkullRenderer = new ReflectorClass(TileEntitySkullRenderer.class);
      TileEntitySkullRenderer_dragonHead = new ReflectorField(TileEntitySkullRenderer, ModelDragonHead.class, 0);
      TileEntitySkullRenderer_skeletonHead = new ReflectorField(TileEntitySkullRenderer, ModelSkeletonHead.class, 0);
      TileEntitySkullRenderer_humanoidHead = new ReflectorField(TileEntitySkullRenderer, ModelSkeletonHead.class, 1);
   }
}
