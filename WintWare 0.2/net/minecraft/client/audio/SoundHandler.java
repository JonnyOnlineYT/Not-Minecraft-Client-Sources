package net.minecraft.client.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler implements IResourceManagerReloadListener, ITickable {
   public static final Sound MISSING_SOUND;
   private static final Logger LOGGER;
   private static final Gson GSON;
   private static final ParameterizedType TYPE;
   private final SoundRegistry soundRegistry = new SoundRegistry();
   private final SoundManager sndManager;
   private final IResourceManager mcResourceManager;

   public SoundHandler(IResourceManager manager, GameSettings gameSettingsIn) {
      this.mcResourceManager = manager;
      this.sndManager = new SoundManager(this, gameSettingsIn);
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      this.soundRegistry.clearMap();
      Iterator var2 = resourceManager.getResourceDomains().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();

         try {
            Iterator var4 = resourceManager.getAllResources(new ResourceLocation(s, "sounds.json")).iterator();

            while(var4.hasNext()) {
               IResource iresource = (IResource)var4.next();

               try {
                  Map<String, SoundList> map = this.getSoundMap(iresource.getInputStream());
                  Iterator var7 = map.entrySet().iterator();

                  while(var7.hasNext()) {
                     Entry<String, SoundList> entry = (Entry)var7.next();
                     this.loadSoundResource(new ResourceLocation(s, (String)entry.getKey()), (SoundList)entry.getValue());
                  }
               } catch (RuntimeException var9) {
                  LOGGER.warn("Invalid sounds.json", var9);
               }
            }
         } catch (IOException var10) {
         }
      }

      var2 = this.soundRegistry.getKeys().iterator();

      ResourceLocation resourcelocation1;
      while(var2.hasNext()) {
         resourcelocation1 = (ResourceLocation)var2.next();
         SoundEventAccessor soundeventaccessor = (SoundEventAccessor)this.soundRegistry.getObject(resourcelocation1);
         if (soundeventaccessor.getSubtitle() instanceof TextComponentTranslation) {
            String s1 = ((TextComponentTranslation)soundeventaccessor.getSubtitle()).getKey();
            if (!I18n.hasKey(s1)) {
               LOGGER.debug("Missing subtitle {} for event: {}", s1, resourcelocation1);
            }
         }
      }

      var2 = this.soundRegistry.getKeys().iterator();

      while(var2.hasNext()) {
         resourcelocation1 = (ResourceLocation)var2.next();
         if (SoundEvent.REGISTRY.getObject(resourcelocation1) == null) {
            LOGGER.debug("Not having sound event for: {}", resourcelocation1);
         }
      }

      this.sndManager.reloadSoundSystem();
   }

   @Nullable
   protected Map<String, SoundList> getSoundMap(InputStream stream) {
      Map map;
      try {
         map = (Map)JsonUtils.func_193841_a(GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), TYPE);
      } finally {
         IOUtils.closeQuietly(stream);
      }

      return map;
   }

   private void loadSoundResource(ResourceLocation location, SoundList sounds) {
      SoundEventAccessor soundeventaccessor = (SoundEventAccessor)this.soundRegistry.getObject(location);
      boolean flag = soundeventaccessor == null;
      if (flag || sounds.canReplaceExisting()) {
         if (!flag) {
            LOGGER.debug("Replaced sound event location {}", location);
         }

         soundeventaccessor = new SoundEventAccessor(location, sounds.getSubtitle());
         this.soundRegistry.add(soundeventaccessor);
      }

      Iterator var5 = sounds.getSounds().iterator();

      while(var5.hasNext()) {
         final Sound sound = (Sound)var5.next();
         final ResourceLocation resourcelocation = sound.getSoundLocation();
         Object isoundeventaccessor;
         switch(sound.getType()) {
         case FILE:
            if (!this.validateSoundResource(sound, location)) {
               continue;
            }

            isoundeventaccessor = sound;
            break;
         case SOUND_EVENT:
            isoundeventaccessor = new ISoundEventAccessor<Sound>() {
               public int getWeight() {
                  SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor)SoundHandler.this.soundRegistry.getObject(resourcelocation);
                  return soundeventaccessor1 == null ? 0 : soundeventaccessor1.getWeight();
               }

               public Sound cloneEntry() {
                  SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor)SoundHandler.this.soundRegistry.getObject(resourcelocation);
                  if (soundeventaccessor1 == null) {
                     return SoundHandler.MISSING_SOUND;
                  } else {
                     Sound sound1 = soundeventaccessor1.cloneEntry();
                     return new Sound(sound1.getSoundLocation().toString(), sound1.getVolume() * sound.getVolume(), sound1.getPitch() * sound.getPitch(), sound.getWeight(), Sound.Type.FILE, sound1.isStreaming() || sound.isStreaming());
                  }
               }
            };
            break;
         default:
            throw new IllegalStateException("Unknown SoundEventRegistration type: " + sound.getType());
         }

         soundeventaccessor.addSound((ISoundEventAccessor)isoundeventaccessor);
      }

   }

   private boolean validateSoundResource(Sound p_184401_1_, ResourceLocation p_184401_2_) {
      ResourceLocation resourcelocation = p_184401_1_.getSoundAsOggLocation();
      IResource iresource = null;

      boolean flag;
      try {
         iresource = this.mcResourceManager.getResource(resourcelocation);
         iresource.getInputStream();
         boolean var6 = true;
         return var6;
      } catch (FileNotFoundException var12) {
         LOGGER.warn("File {} does not exist, cannot add it to event {}", resourcelocation, p_184401_2_);
         flag = false;
      } catch (IOException var13) {
         LOGGER.warn("Could not load sound file {}, cannot add it to event {}", resourcelocation, p_184401_2_, var13);
         flag = false;
         boolean var7 = flag;
         return var7;
      } finally {
         IOUtils.closeQuietly(iresource);
      }

      return flag;
   }

   @Nullable
   public SoundEventAccessor getAccessor(ResourceLocation location) {
      return (SoundEventAccessor)this.soundRegistry.getObject(location);
   }

   public void playSound(ISound sound) {
      this.sndManager.playSound(sound);
   }

   public void playDelayedSound(ISound sound, int delay) {
      this.sndManager.playDelayedSound(sound, delay);
   }

   public void setListener(EntityPlayer player, float p_147691_2_) {
      this.sndManager.setListener(player, p_147691_2_);
   }

   public void pauseSounds() {
      this.sndManager.pauseAllSounds();
   }

   public void stopSounds() {
      this.sndManager.stopAllSounds();
   }

   public void unloadSounds() {
      this.sndManager.unloadSoundSystem();
   }

   public void update() {
      this.sndManager.updateAllSounds();
   }

   public void resumeSounds() {
      this.sndManager.resumeAllSounds();
   }

   public void setSoundLevel(SoundCategory category, float volume) {
      if (category == SoundCategory.MASTER && volume <= 0.0F) {
         this.stopSounds();
      }

      this.sndManager.setVolume(category, volume);
   }

   public void stopSound(ISound soundIn) {
      this.sndManager.stopSound(soundIn);
   }

   public boolean isSoundPlaying(ISound sound) {
      return this.sndManager.isSoundPlaying(sound);
   }

   public void addListener(ISoundEventListener listener) {
      this.sndManager.addListener(listener);
   }

   public void removeListener(ISoundEventListener listener) {
      this.sndManager.removeListener(listener);
   }

   public void stop(String p_189520_1_, SoundCategory p_189520_2_) {
      this.sndManager.stop(p_189520_1_, p_189520_2_);
   }

   static {
      MISSING_SOUND = new Sound("meta:missing_sound", 1.0F, 1.0F, 1, Sound.Type.FILE, false);
      LOGGER = LogManager.getLogger();
      GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
      TYPE = new ParameterizedType() {
         public Type[] getActualTypeArguments() {
            return new Type[]{String.class, SoundList.class};
         }

         public Type getRawType() {
            return Map.class;
         }

         public Type getOwnerType() {
            return null;
         }
      };
   }
}
