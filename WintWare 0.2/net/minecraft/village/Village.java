package net.minecraft.village;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Village {
   private World worldObj;
   private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
   private BlockPos centerHelper;
   private BlockPos center;
   private int villageRadius;
   private int lastAddDoorTimestamp;
   private int tickCounter;
   private int numVillagers;
   private int noBreedTicks;
   private final Map<String, Integer> playerReputation;
   private final List<Village.VillageAggressor> villageAgressors;
   private int numIronGolems;

   public Village() {
      this.centerHelper = BlockPos.ORIGIN;
      this.center = BlockPos.ORIGIN;
      this.playerReputation = Maps.newHashMap();
      this.villageAgressors = Lists.newArrayList();
   }

   public Village(World worldIn) {
      this.centerHelper = BlockPos.ORIGIN;
      this.center = BlockPos.ORIGIN;
      this.playerReputation = Maps.newHashMap();
      this.villageAgressors = Lists.newArrayList();
      this.worldObj = worldIn;
   }

   public void setWorld(World worldIn) {
      this.worldObj = worldIn;
   }

   public void tick(int tickCounterIn) {
      this.tickCounter = tickCounterIn;
      this.removeDeadAndOutOfRangeDoors();
      this.removeDeadAndOldAgressors();
      if (tickCounterIn % 20 == 0) {
         this.updateNumVillagers();
      }

      if (tickCounterIn % 30 == 0) {
         this.updateNumIronGolems();
      }

      int i = this.numVillagers / 10;
      if (this.numIronGolems < i && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0) {
         Vec3d vec3d = this.findRandomSpawnPos(this.center, 2, 4, 2);
         if (vec3d != null) {
            EntityIronGolem entityirongolem = new EntityIronGolem(this.worldObj);
            entityirongolem.setPosition(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            this.worldObj.spawnEntityInWorld(entityirongolem);
            ++this.numIronGolems;
         }
      }

   }

   private Vec3d findRandomSpawnPos(BlockPos pos, int x, int y, int z) {
      for(int i = 0; i < 10; ++i) {
         BlockPos blockpos = pos.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
         if (this.isBlockPosWithinSqVillageRadius(blockpos) && this.isAreaClearAround(new BlockPos(x, y, z), blockpos)) {
            return new Vec3d((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
         }
      }

      return null;
   }

   private boolean isAreaClearAround(BlockPos blockSize, BlockPos blockLocation) {
      if (!this.worldObj.getBlockState(blockLocation.down()).isFullyOpaque()) {
         return false;
      } else {
         int i = blockLocation.getX() - blockSize.getX() / 2;
         int j = blockLocation.getZ() - blockSize.getZ() / 2;

         for(int k = i; k < i + blockSize.getX(); ++k) {
            for(int l = blockLocation.getY(); l < blockLocation.getY() + blockSize.getY(); ++l) {
               for(int i1 = j; i1 < j + blockSize.getZ(); ++i1) {
                  if (this.worldObj.getBlockState(new BlockPos(k, l, i1)).isNormalCube()) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private void updateNumIronGolems() {
      List<EntityIronGolem> list = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB((double)(this.center.getX() - this.villageRadius), (double)(this.center.getY() - 4), (double)(this.center.getZ() - this.villageRadius), (double)(this.center.getX() + this.villageRadius), (double)(this.center.getY() + 4), (double)(this.center.getZ() + this.villageRadius)));
      this.numIronGolems = list.size();
   }

   private void updateNumVillagers() {
      List<EntityVillager> list = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB((double)(this.center.getX() - this.villageRadius), (double)(this.center.getY() - 4), (double)(this.center.getZ() - this.villageRadius), (double)(this.center.getX() + this.villageRadius), (double)(this.center.getY() + 4), (double)(this.center.getZ() + this.villageRadius)));
      this.numVillagers = list.size();
      if (this.numVillagers == 0) {
         this.playerReputation.clear();
      }

   }

   public BlockPos getCenter() {
      return this.center;
   }

   public int getVillageRadius() {
      return this.villageRadius;
   }

   public int getNumVillageDoors() {
      return this.villageDoorInfoList.size();
   }

   public int getTicksSinceLastDoorAdding() {
      return this.tickCounter - this.lastAddDoorTimestamp;
   }

   public int getNumVillagers() {
      return this.numVillagers;
   }

   public boolean isBlockPosWithinSqVillageRadius(BlockPos pos) {
      return this.center.distanceSq(pos) < (double)(this.villageRadius * this.villageRadius);
   }

   public List<VillageDoorInfo> getVillageDoorInfoList() {
      return this.villageDoorInfoList;
   }

   public VillageDoorInfo getNearestDoor(BlockPos pos) {
      VillageDoorInfo villagedoorinfo = null;
      int i = Integer.MAX_VALUE;
      Iterator var4 = this.villageDoorInfoList.iterator();

      while(var4.hasNext()) {
         VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo)var4.next();
         int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
         if (j < i) {
            villagedoorinfo = villagedoorinfo1;
            i = j;
         }
      }

      return villagedoorinfo;
   }

   public VillageDoorInfo getDoorInfo(BlockPos pos) {
      VillageDoorInfo villagedoorinfo = null;
      int i = Integer.MAX_VALUE;
      Iterator var4 = this.villageDoorInfoList.iterator();

      while(var4.hasNext()) {
         VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo)var4.next();
         int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
         if (j > 256) {
            j *= 1000;
         } else {
            j = villagedoorinfo1.getDoorOpeningRestrictionCounter();
         }

         if (j < i) {
            BlockPos blockpos = villagedoorinfo1.getDoorBlockPos();
            EnumFacing enumfacing = villagedoorinfo1.getInsideDirection();
            if (this.worldObj.getBlockState(blockpos.offset(enumfacing, 1)).getBlock().isPassable(this.worldObj, blockpos.offset(enumfacing, 1)) && this.worldObj.getBlockState(blockpos.offset(enumfacing, -1)).getBlock().isPassable(this.worldObj, blockpos.offset(enumfacing, -1)) && this.worldObj.getBlockState(blockpos.up().offset(enumfacing, 1)).getBlock().isPassable(this.worldObj, blockpos.up().offset(enumfacing, 1)) && this.worldObj.getBlockState(blockpos.up().offset(enumfacing, -1)).getBlock().isPassable(this.worldObj, blockpos.up().offset(enumfacing, -1))) {
               villagedoorinfo = villagedoorinfo1;
               i = j;
            }
         }
      }

      return villagedoorinfo;
   }

   @Nullable
   public VillageDoorInfo getExistedDoor(BlockPos doorBlock) {
      if (this.center.distanceSq(doorBlock) > (double)(this.villageRadius * this.villageRadius)) {
         return null;
      } else {
         Iterator var2 = this.villageDoorInfoList.iterator();

         VillageDoorInfo villagedoorinfo;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            villagedoorinfo = (VillageDoorInfo)var2.next();
         } while(villagedoorinfo.getDoorBlockPos().getX() != doorBlock.getX() || villagedoorinfo.getDoorBlockPos().getZ() != doorBlock.getZ() || Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) > 1);

         return villagedoorinfo;
      }
   }

   public void addVillageDoorInfo(VillageDoorInfo doorInfo) {
      this.villageDoorInfoList.add(doorInfo);
      this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
      this.updateVillageRadiusAndCenter();
      this.lastAddDoorTimestamp = doorInfo.getInsidePosY();
   }

   public boolean isAnnihilated() {
      return this.villageDoorInfoList.isEmpty();
   }

   public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn) {
      Iterator var2 = this.villageAgressors.iterator();

      Village.VillageAggressor village$villageaggressor;
      do {
         if (!var2.hasNext()) {
            this.villageAgressors.add(new Village.VillageAggressor(entitylivingbaseIn, this.tickCounter));
            return;
         }

         village$villageaggressor = (Village.VillageAggressor)var2.next();
      } while(village$villageaggressor.agressor != entitylivingbaseIn);

      village$villageaggressor.agressionTime = this.tickCounter;
   }

   @Nullable
   public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn) {
      double d0 = Double.MAX_VALUE;
      Village.VillageAggressor village$villageaggressor = null;

      for(int i = 0; i < this.villageAgressors.size(); ++i) {
         Village.VillageAggressor village$villageaggressor1 = (Village.VillageAggressor)this.villageAgressors.get(i);
         double d1 = village$villageaggressor1.agressor.getDistanceSqToEntity(entitylivingbaseIn);
         if (d1 <= d0) {
            village$villageaggressor = village$villageaggressor1;
            d0 = d1;
         }
      }

      return village$villageaggressor == null ? null : village$villageaggressor.agressor;
   }

   public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender) {
      double d0 = Double.MAX_VALUE;
      EntityPlayer entityplayer = null;
      Iterator var5 = this.playerReputation.keySet().iterator();

      while(var5.hasNext()) {
         String s = (String)var5.next();
         if (this.isPlayerReputationTooLow(s)) {
            EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(s);
            if (entityplayer1 != null) {
               double d1 = entityplayer1.getDistanceSqToEntity(villageDefender);
               if (d1 <= d0) {
                  entityplayer = entityplayer1;
                  d0 = d1;
               }
            }
         }
      }

      return entityplayer;
   }

   private void removeDeadAndOldAgressors() {
      Iterator iterator = this.villageAgressors.iterator();

      while(true) {
         Village.VillageAggressor village$villageaggressor;
         do {
            if (!iterator.hasNext()) {
               return;
            }

            village$villageaggressor = (Village.VillageAggressor)iterator.next();
         } while(village$villageaggressor.agressor.isEntityAlive() && Math.abs(this.tickCounter - village$villageaggressor.agressionTime) <= 300);

         iterator.remove();
      }
   }

   private void removeDeadAndOutOfRangeDoors() {
      boolean flag = false;
      boolean flag1 = this.worldObj.rand.nextInt(50) == 0;
      Iterator iterator = this.villageDoorInfoList.iterator();

      while(true) {
         VillageDoorInfo villagedoorinfo;
         do {
            if (!iterator.hasNext()) {
               if (flag) {
                  this.updateVillageRadiusAndCenter();
               }

               return;
            }

            villagedoorinfo = (VillageDoorInfo)iterator.next();
            if (flag1) {
               villagedoorinfo.resetDoorOpeningRestrictionCounter();
            }
         } while(this.isWoodDoor(villagedoorinfo.getDoorBlockPos()) && Math.abs(this.tickCounter - villagedoorinfo.getInsidePosY()) <= 1200);

         this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
         flag = true;
         villagedoorinfo.setIsDetachedFromVillageFlag(true);
         iterator.remove();
      }
   }

   private boolean isWoodDoor(BlockPos pos) {
      IBlockState iblockstate = this.worldObj.getBlockState(pos);
      Block block = iblockstate.getBlock();
      if (block instanceof BlockDoor) {
         return iblockstate.getMaterial() == Material.WOOD;
      } else {
         return false;
      }
   }

   private void updateVillageRadiusAndCenter() {
      int i = this.villageDoorInfoList.size();
      if (i == 0) {
         this.center = BlockPos.ORIGIN;
         this.villageRadius = 0;
      } else {
         this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
         int j = 0;

         VillageDoorInfo villagedoorinfo;
         for(Iterator var3 = this.villageDoorInfoList.iterator(); var3.hasNext(); j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j)) {
            villagedoorinfo = (VillageDoorInfo)var3.next();
         }

         this.villageRadius = Math.max(32, (int)Math.sqrt((double)j) + 1);
      }

   }

   public int getPlayerReputation(String playerName) {
      Integer integer = (Integer)this.playerReputation.get(playerName);
      return integer == null ? 0 : integer;
   }

   public int modifyPlayerReputation(String playerName, int reputation) {
      int i = this.getPlayerReputation(playerName);
      int j = MathHelper.clamp(i + reputation, -30, 10);
      this.playerReputation.put(playerName, j);
      return j;
   }

   public boolean isPlayerReputationTooLow(String playerName) {
      return this.getPlayerReputation(playerName) <= -15;
   }

   public void readVillageDataFromNBT(NBTTagCompound compound) {
      this.numVillagers = compound.getInteger("PopSize");
      this.villageRadius = compound.getInteger("Radius");
      this.numIronGolems = compound.getInteger("Golems");
      this.lastAddDoorTimestamp = compound.getInteger("Stable");
      this.tickCounter = compound.getInteger("Tick");
      this.noBreedTicks = compound.getInteger("MTick");
      this.center = new BlockPos(compound.getInteger("CX"), compound.getInteger("CY"), compound.getInteger("CZ"));
      this.centerHelper = new BlockPos(compound.getInteger("ACX"), compound.getInteger("ACY"), compound.getInteger("ACZ"));
      NBTTagList nbttaglist = compound.getTagList("Doors", 10);

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
         VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
         this.villageDoorInfoList.add(villagedoorinfo);
      }

      NBTTagList nbttaglist1 = compound.getTagList("Players", 10);

      for(int j = 0; j < nbttaglist1.tagCount(); ++j) {
         NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);
         if (nbttagcompound1.hasKey("UUID") && this.worldObj != null && this.worldObj.getMinecraftServer() != null) {
            PlayerProfileCache playerprofilecache = this.worldObj.getMinecraftServer().getPlayerProfileCache();
            GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound1.getString("UUID")));
            if (gameprofile != null) {
               this.playerReputation.put(gameprofile.getName(), nbttagcompound1.getInteger("S"));
            }
         } else {
            this.playerReputation.put(nbttagcompound1.getString("Name"), nbttagcompound1.getInteger("S"));
         }
      }

   }

   public void writeVillageDataToNBT(NBTTagCompound compound) {
      compound.setInteger("PopSize", this.numVillagers);
      compound.setInteger("Radius", this.villageRadius);
      compound.setInteger("Golems", this.numIronGolems);
      compound.setInteger("Stable", this.lastAddDoorTimestamp);
      compound.setInteger("Tick", this.tickCounter);
      compound.setInteger("MTick", this.noBreedTicks);
      compound.setInteger("CX", this.center.getX());
      compound.setInteger("CY", this.center.getY());
      compound.setInteger("CZ", this.center.getZ());
      compound.setInteger("ACX", this.centerHelper.getX());
      compound.setInteger("ACY", this.centerHelper.getY());
      compound.setInteger("ACZ", this.centerHelper.getZ());
      NBTTagList nbttaglist = new NBTTagList();
      Iterator var3 = this.villageDoorInfoList.iterator();

      while(var3.hasNext()) {
         VillageDoorInfo villagedoorinfo = (VillageDoorInfo)var3.next();
         NBTTagCompound nbttagcompound = new NBTTagCompound();
         nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
         nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
         nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
         nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
         nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
         nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
         nbttaglist.appendTag(nbttagcompound);
      }

      compound.setTag("Doors", nbttaglist);
      NBTTagList nbttaglist1 = new NBTTagList();
      Iterator var11 = this.playerReputation.keySet().iterator();

      while(var11.hasNext()) {
         String s = (String)var11.next();
         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
         PlayerProfileCache playerprofilecache = this.worldObj.getMinecraftServer().getPlayerProfileCache();

         try {
            GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);
            if (gameprofile != null) {
               nbttagcompound1.setString("UUID", gameprofile.getId().toString());
               nbttagcompound1.setInteger("S", (Integer)this.playerReputation.get(s));
               nbttaglist1.appendTag(nbttagcompound1);
            }
         } catch (RuntimeException var9) {
         }
      }

      compound.setTag("Players", nbttaglist1);
   }

   public void endMatingSeason() {
      this.noBreedTicks = this.tickCounter;
   }

   public boolean isMatingSeason() {
      return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
   }

   public void setDefaultPlayerReputation(int defaultReputation) {
      Iterator var2 = this.playerReputation.keySet().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         this.modifyPlayerReputation(s, defaultReputation);
      }

   }

   class VillageAggressor {
      public EntityLivingBase agressor;
      public int agressionTime;

      VillageAggressor(EntityLivingBase agressorIn, int agressionTimeIn) {
         this.agressor = agressorIn;
         this.agressionTime = agressionTimeIn;
      }
   }
}
