package net.minecraft.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {
   private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Set<ResourceLocation> UNABLE_TO_PLAY = Sets.newHashSet();
   private final SoundHandler sndHandler;
   private final GameSettings options;
   private SoundManager.SoundSystemStarterThread sndSystem;
   private boolean loaded;
   private int playTime;
   private final Map<String, ISound> playingSounds = HashBiMap.create();
   private final Map<ISound, String> invPlayingSounds;
   private final Multimap<SoundCategory, String> categorySounds;
   private final List<ITickableSound> tickableSounds;
   private final Map<ISound, Integer> delayedSounds;
   private final Map<String, Integer> playingSoundsStopTime;
   private final List<ISoundEventListener> listeners;
   private final List<String> pausedChannels;

   public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_) {
      this.invPlayingSounds = ((BiMap)this.playingSounds).inverse();
      this.categorySounds = HashMultimap.create();
      this.tickableSounds = Lists.newArrayList();
      this.delayedSounds = Maps.newHashMap();
      this.playingSoundsStopTime = Maps.newHashMap();
      this.listeners = Lists.newArrayList();
      this.pausedChannels = Lists.newArrayList();
      this.sndHandler = p_i45119_1_;
      this.options = p_i45119_2_;

      try {
         SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
         SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
      } catch (SoundSystemException var4) {
         LOGGER.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", var4);
      }

   }

   public void reloadSoundSystem() {
      UNABLE_TO_PLAY.clear();
      Iterator var1 = SoundEvent.REGISTRY.iterator();

      while(var1.hasNext()) {
         SoundEvent soundevent = (SoundEvent)var1.next();
         ResourceLocation resourcelocation = soundevent.getSoundName();
         if (this.sndHandler.getAccessor(resourcelocation) == null) {
            LOGGER.warn("Missing sound for event: {}", SoundEvent.REGISTRY.getNameForObject(soundevent));
            UNABLE_TO_PLAY.add(resourcelocation);
         }
      }

      this.unloadSoundSystem();
      this.loadSoundSystem();
   }

   private synchronized void loadSoundSystem() {
      if (!this.loaded) {
         try {
            (new Thread(new Runnable() {
               public void run() {
                  SoundSystemConfig.setLogger(new SoundSystemLogger() {
                     public void message(String p_message_1_, int p_message_2_) {
                        if (!p_message_1_.isEmpty()) {
                           SoundManager.LOGGER.info(p_message_1_);
                        }

                     }

                     public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_) {
                        if (!p_importantMessage_1_.isEmpty()) {
                           SoundManager.LOGGER.warn(p_importantMessage_1_);
                        }

                     }

                     public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_) {
                        if (!p_errorMessage_2_.isEmpty()) {
                           SoundManager.LOGGER.error("Error in class '{}'", p_errorMessage_1_);
                           SoundManager.LOGGER.error(p_errorMessage_2_);
                        }

                     }
                  });
                  SoundManager.this.sndSystem = SoundManager.this.new SoundSystemStarterThread();
                  SoundManager.this.loaded = true;
                  SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                  SoundManager.LOGGER.info(SoundManager.LOG_MARKER, "Sound engine started");
               }
            }, "Sound Library Loader")).start();
         } catch (RuntimeException var2) {
            LOGGER.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", var2);
            this.options.setSoundLevel(SoundCategory.MASTER, 0.0F);
            this.options.saveOptions();
         }
      }

   }

   private float getVolume(SoundCategory category) {
      return category != null && category != SoundCategory.MASTER ? this.options.getSoundLevel(category) : 1.0F;
   }

   public void setVolume(SoundCategory category, float volume) {
      if (this.loaded) {
         if (category == SoundCategory.MASTER) {
            this.sndSystem.setMasterVolume(volume);
         } else {
            Iterator var3 = this.categorySounds.get(category).iterator();

            while(var3.hasNext()) {
               String s = (String)var3.next();
               ISound isound = (ISound)this.playingSounds.get(s);
               float f = this.getClampedVolume(isound);
               if (f <= 0.0F) {
                  this.stopSound(isound);
               } else {
                  this.sndSystem.setVolume(s, f);
               }
            }
         }
      }

   }

   public void unloadSoundSystem() {
      if (this.loaded) {
         this.stopAllSounds();
         this.sndSystem.cleanup();
         this.loaded = false;
      }

   }

   public void stopAllSounds() {
      if (this.loaded) {
         Iterator var1 = this.playingSounds.keySet().iterator();

         while(var1.hasNext()) {
            String s = (String)var1.next();
            this.sndSystem.stop(s);
         }

         this.playingSounds.clear();
         this.delayedSounds.clear();
         this.tickableSounds.clear();
         this.categorySounds.clear();
         this.playingSoundsStopTime.clear();
      }

   }

   public void addListener(ISoundEventListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(ISoundEventListener listener) {
      this.listeners.remove(listener);
   }

   public void updateAllSounds() {
      ++this.playTime;
      Iterator iterator = this.tickableSounds.iterator();

      String s1;
      while(iterator.hasNext()) {
         ITickableSound itickablesound = (ITickableSound)iterator.next();
         itickablesound.update();
         if (itickablesound.isDonePlaying()) {
            this.stopSound(itickablesound);
         } else {
            s1 = (String)this.invPlayingSounds.get(itickablesound);
            this.sndSystem.setVolume(s1, this.getClampedVolume(itickablesound));
            this.sndSystem.setPitch(s1, this.getClampedPitch(itickablesound));
            this.sndSystem.setPosition(s1, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
         }
      }

      iterator = this.playingSounds.entrySet().iterator();

      ISound isound;
      while(iterator.hasNext()) {
         Entry<String, ISound> entry = (Entry)iterator.next();
         s1 = (String)entry.getKey();
         isound = (ISound)entry.getValue();
         if (!this.sndSystem.playing(s1)) {
            int i = (Integer)this.playingSoundsStopTime.get(s1);
            if (i <= this.playTime) {
               int j = isound.getRepeatDelay();
               if (isound.canRepeat() && j > 0) {
                  this.delayedSounds.put(isound, this.playTime + j);
               }

               iterator.remove();
               LOGGER.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore", s1);
               this.sndSystem.removeSource(s1);
               this.playingSoundsStopTime.remove(s1);

               try {
                  this.categorySounds.remove(isound.getCategory(), s1);
               } catch (RuntimeException var8) {
               }

               if (isound instanceof ITickableSound) {
                  this.tickableSounds.remove(isound);
               }
            }
         }
      }

      Iterator iterator1 = this.delayedSounds.entrySet().iterator();

      while(iterator1.hasNext()) {
         Entry<ISound, Integer> entry1 = (Entry)iterator1.next();
         if (this.playTime >= (Integer)entry1.getValue()) {
            isound = (ISound)entry1.getKey();
            if (isound instanceof ITickableSound) {
               ((ITickableSound)isound).update();
            }

            this.playSound(isound);
            iterator1.remove();
         }
      }

   }

   public boolean isSoundPlaying(ISound sound) {
      if (!this.loaded) {
         return false;
      } else {
         String s = (String)this.invPlayingSounds.get(sound);
         if (s == null) {
            return false;
         } else {
            return this.sndSystem.playing(s) || this.playingSoundsStopTime.containsKey(s) && (Integer)this.playingSoundsStopTime.get(s) <= this.playTime;
         }
      }
   }

   public void stopSound(ISound sound) {
      if (this.loaded) {
         String s = (String)this.invPlayingSounds.get(sound);
         if (s != null) {
            this.sndSystem.stop(s);
         }
      }

   }

   public void playSound(ISound p_sound) {
      if (this.loaded) {
         SoundEventAccessor soundeventaccessor = p_sound.createAccessor(this.sndHandler);
         ResourceLocation resourcelocation = p_sound.getSoundLocation();
         if (soundeventaccessor == null) {
            if (UNABLE_TO_PLAY.add(resourcelocation)) {
               LOGGER.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", resourcelocation);
            }
         } else {
            if (!this.listeners.isEmpty()) {
               Iterator var4 = this.listeners.iterator();

               while(var4.hasNext()) {
                  ISoundEventListener isoundeventlistener = (ISoundEventListener)var4.next();
                  isoundeventlistener.soundPlay(p_sound, soundeventaccessor);
               }
            }

            if (this.sndSystem.getMasterVolume() <= 0.0F) {
               LOGGER.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", resourcelocation);
            } else {
               Sound sound = p_sound.getSound();
               if (sound == SoundHandler.MISSING_SOUND) {
                  if (UNABLE_TO_PLAY.add(resourcelocation)) {
                     LOGGER.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", resourcelocation);
                  }
               } else {
                  float f3 = p_sound.getVolume();
                  float f = 16.0F;
                  if (f3 > 1.0F) {
                     f *= f3;
                  }

                  SoundCategory soundcategory = p_sound.getCategory();
                  float f1 = this.getClampedVolume(p_sound);
                  float f2 = this.getClampedPitch(p_sound);
                  if (f1 == 0.0F) {
                     LOGGER.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", sound.getSoundLocation());
                  } else {
                     boolean flag = p_sound.canRepeat() && p_sound.getRepeatDelay() == 0;
                     String s = MathHelper.getRandomUUID(ThreadLocalRandom.current()).toString();
                     ResourceLocation resourcelocation1 = sound.getSoundAsOggLocation();
                     if (sound.isStreaming()) {
                        this.sndSystem.newStreamingSource(false, s, getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
                     } else {
                        this.sndSystem.newSource(false, s, getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
                     }

                     LOGGER.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", sound.getSoundLocation(), resourcelocation, s);
                     this.sndSystem.setPitch(s, f2);
                     this.sndSystem.setVolume(s, f1);
                     this.sndSystem.play(s);
                     this.playingSoundsStopTime.put(s, this.playTime + 20);
                     this.playingSounds.put(s, p_sound);
                     this.categorySounds.put(soundcategory, s);
                     if (p_sound instanceof ITickableSound) {
                        this.tickableSounds.add((ITickableSound)p_sound);
                     }
                  }
               }
            }
         }
      }

   }

   private float getClampedPitch(ISound soundIn) {
      return MathHelper.clamp(soundIn.getPitch(), 0.5F, 2.0F);
   }

   private float getClampedVolume(ISound soundIn) {
      return MathHelper.clamp(soundIn.getVolume() * this.getVolume(soundIn.getCategory()), 0.0F, 1.0F);
   }

   public void pauseAllSounds() {
      Iterator var1 = this.playingSounds.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<String, ISound> entry = (Entry)var1.next();
         String s = (String)entry.getKey();
         boolean flag = this.isSoundPlaying((ISound)entry.getValue());
         if (flag) {
            LOGGER.debug(LOG_MARKER, "Pausing channel {}", s);
            this.sndSystem.pause(s);
            this.pausedChannels.add(s);
         }
      }

   }

   public void resumeAllSounds() {
      Iterator var1 = this.pausedChannels.iterator();

      while(var1.hasNext()) {
         String s = (String)var1.next();
         LOGGER.debug(LOG_MARKER, "Resuming channel {}", s);
         this.sndSystem.play(s);
      }

      this.pausedChannels.clear();
   }

   public void playDelayedSound(ISound sound, int delay) {
      this.delayedSounds.put(sound, this.playTime + delay);
   }

   private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
      String s = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getResourceDomain(), p_148612_0_.getResourcePath());
      URLStreamHandler urlstreamhandler = new URLStreamHandler() {
         protected URLConnection openConnection(URL p_openConnection_1_) {
            return new URLConnection(p_openConnection_1_) {
               public void connect() throws IOException {
               }

               public InputStream getInputStream() throws IOException {
                  return Minecraft.getMinecraft().getResourceManager().getResource(p_148612_0_).getInputStream();
               }
            };
         }
      };

      try {
         return new URL((URL)null, s, urlstreamhandler);
      } catch (MalformedURLException var4) {
         throw new Error("TODO: Sanely handle url exception! :D");
      }
   }

   public void setListener(EntityPlayer player, float p_148615_2_) {
      if (this.loaded && player != null) {
         float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
         float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
         double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)p_148615_2_;
         double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)p_148615_2_ + (double)player.getEyeHeight();
         double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)p_148615_2_;
         float f2 = MathHelper.cos((f1 + 90.0F) * 0.017453292F);
         float f3 = MathHelper.sin((f1 + 90.0F) * 0.017453292F);
         float f4 = MathHelper.cos(-f * 0.017453292F);
         float f5 = MathHelper.sin(-f * 0.017453292F);
         float f6 = MathHelper.cos((-f + 90.0F) * 0.017453292F);
         float f7 = MathHelper.sin((-f + 90.0F) * 0.017453292F);
         float f8 = f2 * f4;
         float f9 = f3 * f4;
         float f10 = f2 * f6;
         float f11 = f3 * f6;
         this.sndSystem.setListenerPosition((float)d0, (float)d1, (float)d2);
         this.sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
      }

   }

   public void stop(String p_189567_1_, SoundCategory p_189567_2_) {
      Iterator var3;
      if (p_189567_2_ != null) {
         var3 = this.categorySounds.get(p_189567_2_).iterator();

         while(var3.hasNext()) {
            String s = (String)var3.next();
            ISound isound = (ISound)this.playingSounds.get(s);
            if (p_189567_1_.isEmpty()) {
               this.stopSound(isound);
            } else if (isound.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) {
               this.stopSound(isound);
            }
         }
      } else if (p_189567_1_.isEmpty()) {
         this.stopAllSounds();
      } else {
         var3 = this.playingSounds.values().iterator();

         while(var3.hasNext()) {
            ISound isound1 = (ISound)var3.next();
            if (isound1.getSoundLocation().equals(new ResourceLocation(p_189567_1_))) {
               this.stopSound(isound1);
            }
         }
      }

   }

   class SoundSystemStarterThread extends SoundSystem {
      private SoundSystemStarterThread() {
      }

      public boolean playing(String p_playing_1_) {
         synchronized(SoundSystemConfig.THREAD_SYNC) {
            if (this.soundLibrary == null) {
               return false;
            } else {
               Source source = (Source)this.soundLibrary.getSources().get(p_playing_1_);
               if (source == null) {
                  return false;
               } else {
                  return source.playing() || source.paused() || source.preLoad;
               }
            }
         }
      }

      // $FF: synthetic method
      SoundSystemStarterThread(Object x1) {
         this();
      }
   }
}
