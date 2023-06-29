package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.IChunkGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final ExtendedBlockStorage NULL_BLOCK_STORAGE = null;
   private final ExtendedBlockStorage[] storageArrays;
   private final byte[] blockBiomeArray;
   private final int[] precipitationHeightMap;
   private final boolean[] updateSkylightColumns;
   private boolean isChunkLoaded;
   private final World worldObj;
   private final int[] heightMap;
   public final int xPosition;
   public final int zPosition;
   private boolean isGapLightingUpdated;
   private final Map<BlockPos, TileEntity> chunkTileEntityMap;
   private final ClassInheritanceMultiMap<Entity>[] entityLists;
   private boolean isTerrainPopulated;
   private boolean isLightPopulated;
   private boolean chunkTicked;
   private boolean isModified;
   private boolean hasEntities;
   private long lastSaveTime;
   private int heightMapMinimum;
   private long inhabitedTime;
   private int queuedLightChecks;
   private final ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue;
   public boolean unloaded;

   public Chunk(World worldIn, int x, int z) {
      this.storageArrays = new ExtendedBlockStorage[16];
      this.blockBiomeArray = new byte[256];
      this.precipitationHeightMap = new int[256];
      this.updateSkylightColumns = new boolean[256];
      this.chunkTileEntityMap = Maps.newHashMap();
      this.queuedLightChecks = 4096;
      this.tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
      this.entityLists = (ClassInheritanceMultiMap[])(new ClassInheritanceMultiMap[16]);
      this.worldObj = worldIn;
      this.xPosition = x;
      this.zPosition = z;
      this.heightMap = new int[256];

      for(int i = 0; i < this.entityLists.length; ++i) {
         this.entityLists[i] = new ClassInheritanceMultiMap(Entity.class);
      }

      Arrays.fill(this.precipitationHeightMap, -999);
      Arrays.fill(this.blockBiomeArray, (byte)-1);
   }

   public Chunk(World worldIn, ChunkPrimer primer, int x, int z) {
      this(worldIn, x, z);
      int i = true;
      boolean flag = worldIn.provider.func_191066_m();

      for(int j = 0; j < 16; ++j) {
         for(int k = 0; k < 16; ++k) {
            for(int l = 0; l < 256; ++l) {
               IBlockState iblockstate = primer.getBlockState(j, l, k);
               if (iblockstate.getMaterial() != Material.AIR) {
                  int i1 = l >> 4;
                  if (this.storageArrays[i1] == NULL_BLOCK_STORAGE) {
                     this.storageArrays[i1] = new ExtendedBlockStorage(i1 << 4, flag);
                  }

                  this.storageArrays[i1].set(j, l & 15, k, iblockstate);
               }
            }
         }
      }

   }

   public boolean isAtLocation(int x, int z) {
      return x == this.xPosition && z == this.zPosition;
   }

   public int getHeight(BlockPos pos) {
      return this.getHeightValue(pos.getX() & 15, pos.getZ() & 15);
   }

   public int getHeightValue(int x, int z) {
      return this.heightMap[z << 4 | x];
   }

   @Nullable
   private ExtendedBlockStorage getLastExtendedBlockStorage() {
      for(int i = this.storageArrays.length - 1; i >= 0; --i) {
         if (this.storageArrays[i] != NULL_BLOCK_STORAGE) {
            return this.storageArrays[i];
         }
      }

      return null;
   }

   public int getTopFilledSegment() {
      ExtendedBlockStorage extendedblockstorage = this.getLastExtendedBlockStorage();
      return extendedblockstorage == null ? 0 : extendedblockstorage.getYLocation();
   }

   public ExtendedBlockStorage[] getBlockStorageArray() {
      return this.storageArrays;
   }

   protected void generateHeightMap() {
      int i = this.getTopFilledSegment();
      this.heightMapMinimum = Integer.MAX_VALUE;

      for(int j = 0; j < 16; ++j) {
         for(int k = 0; k < 16; ++k) {
            this.precipitationHeightMap[j + (k << 4)] = -999;

            for(int l = i + 16; l > 0; --l) {
               IBlockState iblockstate = this.getBlockState(j, l - 1, k);
               if (iblockstate.getLightOpacity() != 0) {
                  this.heightMap[k << 4 | j] = l;
                  if (l < this.heightMapMinimum) {
                     this.heightMapMinimum = l;
                  }
                  break;
               }
            }
         }
      }

      this.isModified = true;
   }

   public void generateSkylightMap() {
      int i = this.getTopFilledSegment();
      this.heightMapMinimum = Integer.MAX_VALUE;

      for(int j = 0; j < 16; ++j) {
         for(int k = 0; k < 16; ++k) {
            this.precipitationHeightMap[j + (k << 4)] = -999;

            int k1;
            for(k1 = i + 16; k1 > 0; --k1) {
               if (this.getBlockLightOpacity(j, k1 - 1, k) != 0) {
                  this.heightMap[k << 4 | j] = k1;
                  if (k1 < this.heightMapMinimum) {
                     this.heightMapMinimum = k1;
                  }
                  break;
               }
            }

            if (this.worldObj.provider.func_191066_m()) {
               k1 = 15;
               int i1 = i + 16 - 1;

               do {
                  int j1 = this.getBlockLightOpacity(j, i1, k);
                  if (j1 == 0 && k1 != 15) {
                     j1 = 1;
                  }

                  k1 -= j1;
                  if (k1 > 0) {
                     ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];
                     if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                        extendedblockstorage.setExtSkylightValue(j, i1 & 15, k, k1);
                        this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + j, i1, (this.zPosition << 4) + k));
                     }
                  }

                  --i1;
               } while(i1 > 0 && k1 > 0);
            }
         }
      }

      this.isModified = true;
   }

   private void propagateSkylightOcclusion(int x, int z) {
      this.updateSkylightColumns[x + z * 16] = true;
      this.isGapLightingUpdated = true;
   }

   private void recheckGaps(boolean p_150803_1_) {
      this.worldObj.theProfiler.startSection("recheckGaps");
      if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
         for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
               if (this.updateSkylightColumns[i + j * 16]) {
                  this.updateSkylightColumns[i + j * 16] = false;
                  int k = this.getHeightValue(i, j);
                  int l = this.xPosition * 16 + i;
                  int i1 = this.zPosition * 16 + j;
                  int j1 = Integer.MAX_VALUE;

                  Iterator var8;
                  EnumFacing enumfacing1;
                  for(var8 = EnumFacing.Plane.HORIZONTAL.iterator(); var8.hasNext(); j1 = Math.min(j1, this.worldObj.getChunksLowestHorizon(l + enumfacing1.getFrontOffsetX(), i1 + enumfacing1.getFrontOffsetZ()))) {
                     enumfacing1 = (EnumFacing)var8.next();
                  }

                  this.checkSkylightNeighborHeight(l, i1, j1);
                  var8 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var8.hasNext()) {
                     enumfacing1 = (EnumFacing)var8.next();
                     this.checkSkylightNeighborHeight(l + enumfacing1.getFrontOffsetX(), i1 + enumfacing1.getFrontOffsetZ(), k);
                  }

                  if (p_150803_1_) {
                     this.worldObj.theProfiler.endSection();
                     return;
                  }
               }
            }
         }

         this.isGapLightingUpdated = false;
      }

      this.worldObj.theProfiler.endSection();
   }

   private void checkSkylightNeighborHeight(int x, int z, int maxValue) {
      int i = this.worldObj.getHeight(new BlockPos(x, 0, z)).getY();
      if (i > maxValue) {
         this.updateSkylightNeighborHeight(x, z, maxValue, i + 1);
      } else if (i < maxValue) {
         this.updateSkylightNeighborHeight(x, z, i, maxValue + 1);
      }

   }

   private void updateSkylightNeighborHeight(int x, int z, int startY, int endY) {
      if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) {
         for(int i = startY; i < endY; ++i) {
            this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i, z));
         }

         this.isModified = true;
      }

   }

   private void relightBlock(int x, int y, int z) {
      int i = this.heightMap[z << 4 | x] & 255;
      int j = i;
      if (y > i) {
         j = y;
      }

      while(j > 0 && this.getBlockLightOpacity(x, j - 1, z) == 0) {
         --j;
      }

      if (j != i) {
         this.worldObj.markBlocksDirtyVertical(x + this.xPosition * 16, z + this.zPosition * 16, j, i);
         this.heightMap[z << 4 | x] = j;
         int k = this.xPosition * 16 + x;
         int l = this.zPosition * 16 + z;
         int k1;
         int j2;
         if (this.worldObj.provider.func_191066_m()) {
            ExtendedBlockStorage extendedblockstorage2;
            if (j < i) {
               for(k1 = j; k1 < i; ++k1) {
                  extendedblockstorage2 = this.storageArrays[k1 >> 4];
                  if (extendedblockstorage2 != NULL_BLOCK_STORAGE) {
                     extendedblockstorage2.setExtSkylightValue(x, k1 & 15, z, 15);
                     this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, k1, (this.zPosition << 4) + z));
                  }
               }
            } else {
               for(k1 = i; k1 < j; ++k1) {
                  extendedblockstorage2 = this.storageArrays[k1 >> 4];
                  if (extendedblockstorage2 != NULL_BLOCK_STORAGE) {
                     extendedblockstorage2.setExtSkylightValue(x, k1 & 15, z, 0);
                     this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, k1, (this.zPosition << 4) + z));
                  }
               }
            }

            k1 = 15;

            while(j > 0 && k1 > 0) {
               --j;
               j2 = this.getBlockLightOpacity(x, j, z);
               if (j2 == 0) {
                  j2 = 1;
               }

               k1 -= j2;
               if (k1 < 0) {
                  k1 = 0;
               }

               ExtendedBlockStorage extendedblockstorage1 = this.storageArrays[j >> 4];
               if (extendedblockstorage1 != NULL_BLOCK_STORAGE) {
                  extendedblockstorage1.setExtSkylightValue(x, j & 15, z, k1);
               }
            }
         }

         k1 = this.heightMap[z << 4 | x];
         j2 = i;
         int k2 = k1;
         if (k1 < i) {
            j2 = k1;
            k2 = i;
         }

         if (k1 < this.heightMapMinimum) {
            this.heightMapMinimum = k1;
         }

         if (this.worldObj.provider.func_191066_m()) {
            Iterator var11 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(var11.hasNext()) {
               EnumFacing enumfacing = (EnumFacing)var11.next();
               this.updateSkylightNeighborHeight(k + enumfacing.getFrontOffsetX(), l + enumfacing.getFrontOffsetZ(), j2, k2);
            }

            this.updateSkylightNeighborHeight(k, l, j2, k2);
         }

         this.isModified = true;
      }

   }

   public int getBlockLightOpacity(BlockPos pos) {
      return this.getBlockState(pos).getLightOpacity();
   }

   private int getBlockLightOpacity(int x, int y, int z) {
      return this.getBlockState(x, y, z).getLightOpacity();
   }

   public IBlockState getBlockState(BlockPos pos) {
      return this.getBlockState(pos.getX(), pos.getY(), pos.getZ());
   }

   public IBlockState getBlockState(final int x, final int y, final int z) {
      if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
         IBlockState iblockstate = null;
         if (y == 60) {
            iblockstate = Blocks.BARRIER.getDefaultState();
         }

         if (y == 70) {
            iblockstate = ChunkGeneratorDebug.getBlockStateFor(x, z);
         }

         return iblockstate == null ? Blocks.AIR.getDefaultState() : iblockstate;
      } else {
         try {
            if (y >= 0 && y >> 4 < this.storageArrays.length) {
               ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
               if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                  return extendedblockstorage.get(x & 15, y & 15, z & 15);
               }
            }

            return Blocks.AIR.getDefaultState();
         } catch (Throwable var7) {
            CrashReport crashreport = CrashReport.makeCrashReport(var7, "Getting block state");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
            crashreportcategory.setDetail("Location", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return CrashReportCategory.getCoordinateInfo(x, y, z);
               }
            });
            throw new ReportedException(crashreport);
         }
      }
   }

   @Nullable
   public IBlockState setBlockState(BlockPos pos, IBlockState state) {
      int i = pos.getX() & 15;
      int j = pos.getY();
      int k = pos.getZ() & 15;
      int l = k << 4 | i;
      if (j >= this.precipitationHeightMap[l] - 1) {
         this.precipitationHeightMap[l] = -999;
      }

      int i1 = this.heightMap[l];
      IBlockState iblockstate = this.getBlockState(pos);
      if (iblockstate == state) {
         return null;
      } else {
         Block block = state.getBlock();
         Block block1 = iblockstate.getBlock();
         ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
         boolean flag = false;
         if (extendedblockstorage == NULL_BLOCK_STORAGE) {
            if (block == Blocks.AIR) {
               return null;
            }

            extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, this.worldObj.provider.func_191066_m());
            this.storageArrays[j >> 4] = extendedblockstorage;
            flag = j >= i1;
         }

         extendedblockstorage.set(i, j & 15, k, state);
         if (block1 != block) {
            if (!this.worldObj.isRemote) {
               block1.breakBlock(this.worldObj, pos, iblockstate);
            } else if (block1 instanceof ITileEntityProvider) {
               this.worldObj.removeTileEntity(pos);
            }
         }

         if (extendedblockstorage.get(i, j & 15, k).getBlock() != block) {
            return null;
         } else {
            if (flag) {
               this.generateSkylightMap();
            } else {
               int j1 = state.getLightOpacity();
               int k1 = iblockstate.getLightOpacity();
               if (j1 > 0) {
                  if (j >= i1) {
                     this.relightBlock(i, j + 1, k);
                  }
               } else if (j == i1 - 1) {
                  this.relightBlock(i, j, k);
               }

               if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0)) {
                  this.propagateSkylightOcclusion(i, k);
               }
            }

            TileEntity tileentity1;
            if (block1 instanceof ITileEntityProvider) {
               tileentity1 = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
               if (tileentity1 != null) {
                  tileentity1.updateContainingBlockInfo();
               }
            }

            if (!this.worldObj.isRemote && block1 != block) {
               block.onBlockAdded(this.worldObj, pos, state);
            }

            if (block instanceof ITileEntityProvider) {
               tileentity1 = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
               if (tileentity1 == null) {
                  tileentity1 = ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, block.getMetaFromState(state));
                  this.worldObj.setTileEntity(pos, tileentity1);
               }

               if (tileentity1 != null) {
                  tileentity1.updateContainingBlockInfo();
               }
            }

            this.isModified = true;
            return iblockstate;
         }
      }
   }

   public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos) {
      int i = pos.getX() & 15;
      int j = pos.getY();
      int k = pos.getZ() & 15;
      ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
      if (extendedblockstorage == NULL_BLOCK_STORAGE) {
         return this.canSeeSky(pos) ? p_177413_1_.defaultLightValue : 0;
      } else if (p_177413_1_ == EnumSkyBlock.SKY) {
         return !this.worldObj.provider.func_191066_m() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 15, k);
      } else {
         return p_177413_1_ == EnumSkyBlock.BLOCK ? extendedblockstorage.getExtBlocklightValue(i, j & 15, k) : p_177413_1_.defaultLightValue;
      }
   }

   public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value) {
      int i = pos.getX() & 15;
      int j = pos.getY();
      int k = pos.getZ() & 15;
      ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
      if (extendedblockstorage == NULL_BLOCK_STORAGE) {
         extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, this.worldObj.provider.func_191066_m());
         this.storageArrays[j >> 4] = extendedblockstorage;
         this.generateSkylightMap();
      }

      this.isModified = true;
      if (p_177431_1_ == EnumSkyBlock.SKY) {
         if (this.worldObj.provider.func_191066_m()) {
            extendedblockstorage.setExtSkylightValue(i, j & 15, k, value);
         }
      } else if (p_177431_1_ == EnumSkyBlock.BLOCK) {
         extendedblockstorage.setExtBlocklightValue(i, j & 15, k, value);
      }

   }

   public int getLightSubtracted(BlockPos pos, int amount) {
      int i = pos.getX() & 15;
      int j = pos.getY();
      int k = pos.getZ() & 15;
      ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
      if (extendedblockstorage != NULL_BLOCK_STORAGE) {
         int l = !this.worldObj.provider.func_191066_m() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 15, k);
         l -= amount;
         int i1 = extendedblockstorage.getExtBlocklightValue(i, j & 15, k);
         if (i1 > l) {
            l = i1;
         }

         return l;
      } else {
         return this.worldObj.provider.func_191066_m() && amount < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - amount : 0;
      }
   }

   public void addEntity(Entity entityIn) {
      this.hasEntities = true;
      int i = MathHelper.floor(entityIn.posX / 16.0D);
      int j = MathHelper.floor(entityIn.posZ / 16.0D);
      if (i != this.xPosition || j != this.zPosition) {
         LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", i, j, this.xPosition, this.zPosition, entityIn);
         entityIn.setDead();
      }

      int k = MathHelper.floor(entityIn.posY / 16.0D);
      if (k < 0) {
         k = 0;
      }

      if (k >= this.entityLists.length) {
         k = this.entityLists.length - 1;
      }

      entityIn.addedToChunk = true;
      entityIn.chunkCoordX = this.xPosition;
      entityIn.chunkCoordY = k;
      entityIn.chunkCoordZ = this.zPosition;
      this.entityLists[k].add(entityIn);
   }

   public void removeEntity(Entity entityIn) {
      this.removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
   }

   public void removeEntityAtIndex(Entity entityIn, int index) {
      if (index < 0) {
         index = 0;
      }

      if (index >= this.entityLists.length) {
         index = this.entityLists.length - 1;
      }

      this.entityLists[index].remove(entityIn);
   }

   public boolean canSeeSky(BlockPos pos) {
      int i = pos.getX() & 15;
      int j = pos.getY();
      int k = pos.getZ() & 15;
      return j >= this.heightMap[k << 4 | i];
   }

   @Nullable
   private TileEntity createNewTileEntity(BlockPos pos) {
      IBlockState iblockstate = this.getBlockState(pos);
      Block block = iblockstate.getBlock();
      return !block.hasTileEntity() ? null : ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, iblockstate.getBlock().getMetaFromState(iblockstate));
   }

   @Nullable
   public TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType p_177424_2_) {
      TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.get(pos);
      if (tileentity == null) {
         if (p_177424_2_ == Chunk.EnumCreateEntityType.IMMEDIATE) {
            tileentity = this.createNewTileEntity(pos);
            this.worldObj.setTileEntity(pos, tileentity);
         } else if (p_177424_2_ == Chunk.EnumCreateEntityType.QUEUED) {
            this.tileEntityPosQueue.add(pos);
         }
      } else if (tileentity.isInvalid()) {
         this.chunkTileEntityMap.remove(pos);
         return null;
      }

      return tileentity;
   }

   public void addTileEntity(TileEntity tileEntityIn) {
      this.addTileEntity(tileEntityIn.getPos(), tileEntityIn);
      if (this.isChunkLoaded) {
         this.worldObj.addTileEntity(tileEntityIn);
      }

   }

   public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
      tileEntityIn.setWorldObj(this.worldObj);
      tileEntityIn.setPos(pos);
      if (this.getBlockState(pos).getBlock() instanceof ITileEntityProvider) {
         if (this.chunkTileEntityMap.containsKey(pos)) {
            ((TileEntity)this.chunkTileEntityMap.get(pos)).invalidate();
         }

         tileEntityIn.validate();
         this.chunkTileEntityMap.put(pos, tileEntityIn);
      }

   }

   public void removeTileEntity(BlockPos pos) {
      if (this.isChunkLoaded) {
         TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.remove(pos);
         if (tileentity != null) {
            tileentity.invalidate();
         }
      }

   }

   public void onChunkLoad() {
      this.isChunkLoaded = true;
      this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
      ClassInheritanceMultiMap[] var1 = this.entityLists;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ClassInheritanceMultiMap<Entity> classinheritancemultimap = var1[var3];
         this.worldObj.loadEntities(classinheritancemultimap);
      }

   }

   public void onChunkUnload() {
      this.isChunkLoaded = false;
      Iterator var1 = this.chunkTileEntityMap.values().iterator();

      while(var1.hasNext()) {
         TileEntity tileentity = (TileEntity)var1.next();
         this.worldObj.markTileEntityForRemoval(tileentity);
      }

      ClassInheritanceMultiMap[] var5 = this.entityLists;
      int var6 = var5.length;

      for(int var3 = 0; var3 < var6; ++var3) {
         ClassInheritanceMultiMap<Entity> classinheritancemultimap = var5[var3];
         this.worldObj.unloadEntities(classinheritancemultimap);
      }

   }

   public void setChunkModified() {
      this.isModified = true;
   }

   public void getEntitiesWithinAABBForEntity(@Nullable Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> p_177414_4_) {
      int i = MathHelper.floor((aabb.minY - 2.0D) / 16.0D);
      int j = MathHelper.floor((aabb.maxY + 2.0D) / 16.0D);
      i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
      j = MathHelper.clamp(j, 0, this.entityLists.length - 1);

      label67:
      for(int k = i; k <= j; ++k) {
         if (!this.entityLists[k].isEmpty()) {
            Iterator var8 = this.entityLists[k].iterator();

            while(true) {
               Entity[] aentity;
               do {
                  Entity entity;
                  do {
                     do {
                        if (!var8.hasNext()) {
                           continue label67;
                        }

                        entity = (Entity)var8.next();
                     } while(!entity.getEntityBoundingBox().intersectsWith(aabb));
                  } while(entity == entityIn);

                  if (p_177414_4_ == null || p_177414_4_.apply(entity)) {
                     listToFill.add(entity);
                  }

                  aentity = entity.getParts();
               } while(aentity == null);

               Entity[] var11 = aentity;
               int var12 = aentity.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  Entity entity1 = var11[var13];
                  if (entity1 != entityIn && entity1.getEntityBoundingBox().intersectsWith(aabb) && (p_177414_4_ == null || p_177414_4_.apply(entity1))) {
                     listToFill.add(entity1);
                  }
               }
            }
         }
      }

   }

   public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> filter) {
      int i = MathHelper.floor((aabb.minY - 2.0D) / 16.0D);
      int j = MathHelper.floor((aabb.maxY + 2.0D) / 16.0D);
      i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
      j = MathHelper.clamp(j, 0, this.entityLists.length - 1);

      label33:
      for(int k = i; k <= j; ++k) {
         Iterator var8 = this.entityLists[k].getByClass(entityClass).iterator();

         while(true) {
            Entity t;
            do {
               do {
                  if (!var8.hasNext()) {
                     continue label33;
                  }

                  t = (Entity)var8.next();
               } while(!t.getEntityBoundingBox().intersectsWith(aabb));
            } while(filter != null && !filter.apply(t));

            listToFill.add(t);
         }
      }

   }

   public boolean needsSaving(boolean p_76601_1_) {
      if (p_76601_1_) {
         if (this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified) {
            return true;
         }
      } else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
         return true;
      }

      return this.isModified;
   }

   public Random getRandomWithSeed(long seed) {
      return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ seed);
   }

   public boolean isEmpty() {
      return false;
   }

   public void populateChunk(IChunkProvider chunkProvider, IChunkGenerator chunkGenrator) {
      Chunk chunk = chunkProvider.getLoadedChunk(this.xPosition, this.zPosition - 1);
      Chunk chunk1 = chunkProvider.getLoadedChunk(this.xPosition + 1, this.zPosition);
      Chunk chunk2 = chunkProvider.getLoadedChunk(this.xPosition, this.zPosition + 1);
      Chunk chunk3 = chunkProvider.getLoadedChunk(this.xPosition - 1, this.zPosition);
      if (chunk1 != null && chunk2 != null && chunkProvider.getLoadedChunk(this.xPosition + 1, this.zPosition + 1) != null) {
         this.populateChunk(chunkGenrator);
      }

      if (chunk3 != null && chunk2 != null && chunkProvider.getLoadedChunk(this.xPosition - 1, this.zPosition + 1) != null) {
         chunk3.populateChunk(chunkGenrator);
      }

      if (chunk != null && chunk1 != null && chunkProvider.getLoadedChunk(this.xPosition + 1, this.zPosition - 1) != null) {
         chunk.populateChunk(chunkGenrator);
      }

      if (chunk != null && chunk3 != null) {
         Chunk chunk4 = chunkProvider.getLoadedChunk(this.xPosition - 1, this.zPosition - 1);
         if (chunk4 != null) {
            chunk4.populateChunk(chunkGenrator);
         }
      }

   }

   protected void populateChunk(IChunkGenerator generator) {
      if (this.isTerrainPopulated()) {
         if (generator.generateStructures(this, this.xPosition, this.zPosition)) {
            this.setChunkModified();
         }
      } else {
         this.checkLight();
         generator.populate(this.xPosition, this.zPosition);
         this.setChunkModified();
      }

   }

   public BlockPos getPrecipitationHeight(BlockPos pos) {
      int i = pos.getX() & 15;
      int j = pos.getZ() & 15;
      int k = i | j << 4;
      BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
      if (blockpos.getY() == -999) {
         int l = this.getTopFilledSegment() + 15;
         blockpos = new BlockPos(pos.getX(), l, pos.getZ());
         int i1 = -1;

         while(true) {
            while(blockpos.getY() > 0 && i1 == -1) {
               IBlockState iblockstate = this.getBlockState(blockpos);
               Material material = iblockstate.getMaterial();
               if (!material.blocksMovement() && !material.isLiquid()) {
                  blockpos = blockpos.down();
               } else {
                  i1 = blockpos.getY() + 1;
               }
            }

            this.precipitationHeightMap[k] = i1;
            break;
         }
      }

      return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
   }

   public void onTick(boolean p_150804_1_) {
      if (this.isGapLightingUpdated && this.worldObj.provider.func_191066_m() && !p_150804_1_) {
         this.recheckGaps(this.worldObj.isRemote);
      }

      this.chunkTicked = true;
      if (!this.isLightPopulated && this.isTerrainPopulated) {
         this.checkLight();
      }

      while(!this.tileEntityPosQueue.isEmpty()) {
         BlockPos blockpos = (BlockPos)this.tileEntityPosQueue.poll();
         if (this.getTileEntity(blockpos, Chunk.EnumCreateEntityType.CHECK) == null && this.getBlockState(blockpos).getBlock().hasTileEntity()) {
            TileEntity tileentity = this.createNewTileEntity(blockpos);
            this.worldObj.setTileEntity(blockpos, tileentity);
            this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
         }
      }

   }

   public boolean isPopulated() {
      return this.chunkTicked && this.isTerrainPopulated && this.isLightPopulated;
   }

   public boolean isChunkTicked() {
      return this.chunkTicked;
   }

   public ChunkPos getChunkCoordIntPair() {
      return new ChunkPos(this.xPosition, this.zPosition);
   }

   public boolean getAreLevelsEmpty(int startY, int endY) {
      if (startY < 0) {
         startY = 0;
      }

      if (endY >= 256) {
         endY = 255;
      }

      for(int i = startY; i <= endY; i += 16) {
         ExtendedBlockStorage extendedblockstorage = this.storageArrays[i >> 4];
         if (extendedblockstorage != NULL_BLOCK_STORAGE && !extendedblockstorage.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays) {
      if (this.storageArrays.length != newStorageArrays.length) {
         LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", newStorageArrays.length, this.storageArrays.length);
      } else {
         System.arraycopy(newStorageArrays, 0, this.storageArrays, 0, this.storageArrays.length);
      }

   }

   public void fillChunk(PacketBuffer buf, int p_186033_2_, boolean p_186033_3_) {
      boolean flag = this.worldObj.provider.func_191066_m();

      int j;
      for(j = 0; j < this.storageArrays.length; ++j) {
         ExtendedBlockStorage extendedblockstorage = this.storageArrays[j];
         if ((p_186033_2_ & 1 << j) == 0) {
            if (p_186033_3_ && extendedblockstorage != NULL_BLOCK_STORAGE) {
               this.storageArrays[j] = NULL_BLOCK_STORAGE;
            }
         } else {
            if (extendedblockstorage == NULL_BLOCK_STORAGE) {
               extendedblockstorage = new ExtendedBlockStorage(j << 4, flag);
               this.storageArrays[j] = extendedblockstorage;
            }

            extendedblockstorage.getData().read(buf);
            buf.readBytes(extendedblockstorage.getBlocklightArray().getData());
            if (flag) {
               buf.readBytes(extendedblockstorage.getSkylightArray().getData());
            }
         }
      }

      if (p_186033_3_) {
         buf.readBytes(this.blockBiomeArray);
      }

      for(j = 0; j < this.storageArrays.length; ++j) {
         if (this.storageArrays[j] != NULL_BLOCK_STORAGE && (p_186033_2_ & 1 << j) != 0) {
            this.storageArrays[j].removeInvalidBlocks();
         }
      }

      this.isLightPopulated = true;
      this.isTerrainPopulated = true;
      this.generateHeightMap();
      Iterator var7 = this.chunkTileEntityMap.values().iterator();

      while(var7.hasNext()) {
         TileEntity tileentity = (TileEntity)var7.next();
         tileentity.updateContainingBlockInfo();
      }

   }

   public Biome getBiome(BlockPos pos, BiomeProvider provider) {
      int i = pos.getX() & 15;
      int j = pos.getZ() & 15;
      int k = this.blockBiomeArray[j << 4 | i] & 255;
      Biome biome1;
      if (k == 255) {
         biome1 = provider.getBiome(pos, Biomes.PLAINS);
         k = Biome.getIdForBiome(biome1);
         this.blockBiomeArray[j << 4 | i] = (byte)(k & 255);
      }

      biome1 = Biome.getBiome(k);
      return biome1 == null ? Biomes.PLAINS : biome1;
   }

   public byte[] getBiomeArray() {
      return this.blockBiomeArray;
   }

   public void setBiomeArray(byte[] biomeArray) {
      if (this.blockBiomeArray.length != biomeArray.length) {
         LOGGER.warn("Could not set level chunk biomes, array length is {} instead of {}", biomeArray.length, this.blockBiomeArray.length);
      } else {
         System.arraycopy(biomeArray, 0, this.blockBiomeArray, 0, this.blockBiomeArray.length);
      }

   }

   public void resetRelightChecks() {
      this.queuedLightChecks = 0;
   }

   public void enqueueRelightChecks() {
      if (this.queuedLightChecks < 4096) {
         BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);

         for(int i = 0; i < 8; ++i) {
            if (this.queuedLightChecks >= 4096) {
               return;
            }

            int j = this.queuedLightChecks % 16;
            int k = this.queuedLightChecks / 16 % 16;
            int l = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;

            for(int i1 = 0; i1 < 16; ++i1) {
               BlockPos blockpos1 = blockpos.add(k, (j << 4) + i1, l);
               boolean flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;
               if (this.storageArrays[j] == NULL_BLOCK_STORAGE && flag || this.storageArrays[j] != NULL_BLOCK_STORAGE && this.storageArrays[j].get(k, i1, l).getMaterial() == Material.AIR) {
                  EnumFacing[] var9 = EnumFacing.values();
                  int var10 = var9.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     EnumFacing enumfacing = var9[var11];
                     BlockPos blockpos2 = blockpos1.offset(enumfacing);
                     if (this.worldObj.getBlockState(blockpos2).getLightValue() > 0) {
                        this.worldObj.checkLight(blockpos2);
                     }
                  }

                  this.worldObj.checkLight(blockpos1);
               }
            }
         }
      }

   }

   public void checkLight() {
      this.isTerrainPopulated = true;
      this.isLightPopulated = true;
      BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
      if (this.worldObj.provider.func_191066_m()) {
         if (this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.worldObj.getSeaLevel(), 16))) {
            label44:
            for(int i = 0; i < 16; ++i) {
               for(int j = 0; j < 16; ++j) {
                  if (!this.checkLight(i, j)) {
                     this.isLightPopulated = false;
                     break label44;
                  }
               }
            }

            if (this.isLightPopulated) {
               Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

               while(var5.hasNext()) {
                  EnumFacing enumfacing = (EnumFacing)var5.next();
                  int k = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                  this.worldObj.getChunkFromBlockCoords(blockpos.offset(enumfacing, k)).checkLightSide(enumfacing.getOpposite());
               }

               this.setSkylightUpdated();
            }
         } else {
            this.isLightPopulated = false;
         }
      }

   }

   private void setSkylightUpdated() {
      for(int i = 0; i < this.updateSkylightColumns.length; ++i) {
         this.updateSkylightColumns[i] = true;
      }

      this.recheckGaps(false);
   }

   private void checkLightSide(EnumFacing facing) {
      if (this.isTerrainPopulated) {
         int l;
         if (facing == EnumFacing.EAST) {
            for(l = 0; l < 16; ++l) {
               this.checkLight(15, l);
            }
         } else if (facing == EnumFacing.WEST) {
            for(l = 0; l < 16; ++l) {
               this.checkLight(0, l);
            }
         } else if (facing == EnumFacing.SOUTH) {
            for(l = 0; l < 16; ++l) {
               this.checkLight(l, 15);
            }
         } else if (facing == EnumFacing.NORTH) {
            for(l = 0; l < 16; ++l) {
               this.checkLight(l, 0);
            }
         }
      }

   }

   private boolean checkLight(int x, int z) {
      int i = this.getTopFilledSegment();
      boolean flag = false;
      boolean flag1 = false;
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.xPosition << 4) + x, 0, (this.zPosition << 4) + z);

      int l;
      for(l = i + 16 - 1; l > this.worldObj.getSeaLevel() || l > 0 && !flag1; --l) {
         blockpos$mutableblockpos.setPos(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());
         int k = this.getBlockLightOpacity(blockpos$mutableblockpos);
         if (k == 255 && blockpos$mutableblockpos.getY() < this.worldObj.getSeaLevel()) {
            flag1 = true;
         }

         if (!flag && k > 0) {
            flag = true;
         } else if (flag && k == 0 && !this.worldObj.checkLight(blockpos$mutableblockpos)) {
            return false;
         }
      }

      for(l = blockpos$mutableblockpos.getY(); l > 0; --l) {
         blockpos$mutableblockpos.setPos(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());
         if (this.getBlockState(blockpos$mutableblockpos).getLightValue() > 0) {
            this.worldObj.checkLight(blockpos$mutableblockpos);
         }
      }

      return true;
   }

   public boolean isLoaded() {
      return this.isChunkLoaded;
   }

   public void setChunkLoaded(boolean loaded) {
      this.isChunkLoaded = loaded;
   }

   public World getWorld() {
      return this.worldObj;
   }

   public int[] getHeightMap() {
      return this.heightMap;
   }

   public void setHeightMap(int[] newHeightMap) {
      if (this.heightMap.length != newHeightMap.length) {
         LOGGER.warn("Could not set level chunk heightmap, array length is {} instead of {}", newHeightMap.length, this.heightMap.length);
      } else {
         System.arraycopy(newHeightMap, 0, this.heightMap, 0, this.heightMap.length);
      }

   }

   public Map<BlockPos, TileEntity> getTileEntityMap() {
      return this.chunkTileEntityMap;
   }

   public ClassInheritanceMultiMap<Entity>[] getEntityLists() {
      return this.entityLists;
   }

   public boolean isTerrainPopulated() {
      return this.isTerrainPopulated;
   }

   public void setTerrainPopulated(boolean terrainPopulated) {
      this.isTerrainPopulated = terrainPopulated;
   }

   public boolean isLightPopulated() {
      return this.isLightPopulated;
   }

   public void setLightPopulated(boolean lightPopulated) {
      this.isLightPopulated = lightPopulated;
   }

   public void setModified(boolean modified) {
      this.isModified = modified;
   }

   public void setHasEntities(boolean hasEntitiesIn) {
      this.hasEntities = hasEntitiesIn;
   }

   public void setLastSaveTime(long saveTime) {
      this.lastSaveTime = saveTime;
   }

   public int getLowestHeight() {
      return this.heightMapMinimum;
   }

   public long getInhabitedTime() {
      return this.inhabitedTime;
   }

   public void setInhabitedTime(long newInhabitedTime) {
      this.inhabitedTime = newInhabitedTime;
   }

   public static enum EnumCreateEntityType {
      IMMEDIATE,
      QUEUED,
      CHECK;
   }
}
