package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 1, sendVelocity = TriState.TRUE)
public class SteelMinecartEntity extends MinecartEntity implements EntitySpawnDataProvider {
    public SteelMinecartEntity(World world) {
        super(world);
        setMillRockDirection(1);
    }

    public SteelMinecartEntity(World world, double x, double y, double z, int type) {
        super(world, x, y, z, type);
        setMillRockDirection(1);
    }

    public SteelMinecartEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    public boolean receivedP;

    protected void initDataTracker()
    {
        dataTracker.startTracking(16, (byte) 0); //currentDamage
        dataTracker.startTracking(17, (byte) 0); //timeSinceHit
        dataTracker.startTracking(18, (byte) 0); //rockDirection
    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putInt("currentDamage", getMillCurrentDamage());
        nbttagcompound.putInt("timeSinceHit", getMillTimeSinceHit());
        nbttagcompound.putInt("rockDirection", getMillRockDirection());
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        setMillCurrentDamage(nbttagcompound.getInt("currentDamage"));
        setMillTimeSinceHit(nbttagcompound.getInt("timeSinceHit"));
        setMillRockDirection(nbttagcompound.getInt("rockDirection"));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        if(!receivedP) {
            this.setPosition(x, y, z);
            this.setRotation(pitch, yaw);
            receivedP = true;
        }
//        super.setPositionAndAnglesAvoidEntities(x,y,z,pitch,yaw,interpolationSteps);
    }
    @Environment(EnvType.CLIENT)
    public void animateHurt() {
//        this.damageWobbleSide = -this.damageWobbleSide;
//        this.damageWobbleTicks = 10;
//        this.damageWobbleStrength += this.damageWobbleStrength * 10;
        setMillRockDirection(-getMillRockDirection());
        setMillTimeSinceHit(10);
        setMillCurrentDamage(getMillCurrentDamage()*10);
    }

    @Override
    public void tick() {
//        if (this.damageWobbleTicks > 0) {
//            --this.damageWobbleTicks;
//        }
//
//        if (this.damageWobbleStrength > 0) {
//            --this.damageWobbleStrength;
//        }
        if(getMillTimeSinceHit() > 0)
        {
            setMillTimeSinceHit(getMillTimeSinceHit()-1);
        }
        if(getMillCurrentDamage() > 0)
        {
            setMillCurrentDamage(getMillCurrentDamage()-1);
        }

        if (this.world.isRemote && this.clientInterpolationSteps > 0) {
            if (this.clientInterpolationSteps > 0) {
                double var46 = this.x + (this.clientX - this.x) / (double)this.clientInterpolationSteps;
                double var47 = this.y + (this.clientY - this.y) / (double)this.clientInterpolationSteps;
                double var5 = this.z + (this.clientZ - this.z) / (double)this.clientInterpolationSteps;

                double var48;
                for(var48 = this.clientPitch - (double)this.yaw; var48 < (double)-180.0F; var48 += (double)360.0F) {
                }

                while(var48 >= (double)180.0F) {
                    var48 -= (double)360.0F;
                }

                this.yaw = (float)((double)this.yaw + var48 / (double)this.clientInterpolationSteps);
                this.pitch = (float)((double)this.pitch + (this.clientYaw - (double)this.pitch) / (double)this.clientInterpolationSteps);
                --this.clientInterpolationSteps;
                this.setPosition(var46, var47, var5);
                this.setRotation(this.yaw, this.pitch);
            } else {
                this.setPosition(this.x, this.y, this.z);
                this.setRotation(this.yaw, this.pitch);
            }

        } else {
            this.prevX = this.x;
            this.prevY = this.y;
            this.prevZ = this.z;
            this.velocityY -= (double)0.04F;
            int var1 = MathHelper.floor(this.x);
            int var2 = MathHelper.floor(this.y);
            int var3 = MathHelper.floor(this.z);
            if (RailBlock.isRail(this.world, var1, var2 - 1, var3)) {
                --var2;
            }

//            double var4 = 0.4; //
            double var4 = 0.5; //TODO how much 0.5D, dlaczego na > się zatrzymuje na podjeździe?
            boolean var6 = false;
            double var7 = (double)0.0078125F;
            int var9 = this.world.getBlockId(var1, var2, var3);
            if (RailBlock.isRail(var9)) {
                Vec3d var10 = this.snapPositionToRail(this.x, this.y, this.z);
                int var11 = this.world.getBlockMeta(var1, var2, var3);
                this.y = (double)var2;
                boolean var12 = false;
                boolean var13 = false;
                if (var9 == Block.POWERED_RAIL.id) {
                    var12 = (var11 & 8) != 0;
                    var13 = !var12;
                }

                if (((RailBlock)Block.BLOCKS[var9]).isAlwaysStraight()) {
                    var11 &= 7;
                }

                if (var11 >= 2 && var11 <= 5) {
                    this.y = (double)(var2 + 1);
                }

                if (var11 == 2) {
                    this.velocityX -= var7;
                }

                if (var11 == 3) {
                    this.velocityX += var7;
                }

                if (var11 == 4) {
                    this.velocityZ += var7;
                }

                if (var11 == 5) {
                    this.velocityZ -= var7;
                }

                int[][] var14 = ADJACENT_RAIL_POSITIONS_BY_SHAPE[var11];
                double var15 = (double)(var14[1][0] - var14[0][0]);
                double var17 = (double)(var14[1][2] - var14[0][2]);
                double var19 = Math.sqrt(var15 * var15 + var17 * var17);
                double var21 = this.velocityX * var15 + this.velocityZ * var17;
                if (var21 < (double)0.0F) {
                    var15 = -var15;
                    var17 = -var17;
                }

                double var23 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                this.velocityX = var23 * var15 / var19;
                this.velocityZ = var23 * var17 / var19;
                if (var13) {
                    double var25 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                    if (var25 < 0.03) {
                        this.velocityX *= (double)0.0F;
                        this.velocityY *= (double)0.0F;
                        this.velocityZ *= (double)0.0F;
                    } else {
                        this.velocityX *= (double)0.5F; // 0.5F
                        this.velocityY *= (double)0.0F; // 0.0F
                        this.velocityZ *= (double)0.5F; // 0.5F
                    }
                }

                double var57 = (double)0.0F;
                double var27 = (double)var1 + (double)0.5F + (double)var14[0][0] * (double)0.5F;
                double var29 = (double)var3 + (double)0.5F + (double)var14[0][2] * (double)0.5F;
                double var31 = (double)var1 + (double)0.5F + (double)var14[1][0] * (double)0.5F;
                double var33 = (double)var3 + (double)0.5F + (double)var14[1][2] * (double)0.5F;
                var15 = var31 - var27;
                var17 = var33 - var29;
                if (var15 == (double)0.0F) {
                    this.x = (double)var1 + (double)0.5F;
                    var57 = this.z - (double)var3;
                } else if (var17 == (double)0.0F) {
                    this.z = (double)var3 + (double)0.5F;
                    var57 = this.x - (double)var1;
                } else {
                    double var35 = this.x - var27;
                    double var37 = this.z - var29;
                    double var39 = (var35 * var15 + var37 * var17) * (double)2.0F;
                    var57 = var39;
                }

                this.x = var27 + var15 * var57;
                this.z = var29 + var17 * var57;
                this.setPosition(this.x, this.y + (double)this.standingEyeHeight, this.z);
                double var59 = this.velocityX;
                double var60 = this.velocityZ;
                if (this.passenger != null) {
                    var59 *= (double)0.75F;
                    var60 *= (double)0.75F;
                }

                if (var59 < -var4) {
                    var59 = -var4;
                }

                if (var59 > var4) {
                    var59 = var4;
                }

                if (var60 < -var4) {
                    var60 = -var4;
                }

                if (var60 > var4) {
                    var60 = var4;
                }

                this.move(var59, (double)0.0F, var60);
                if (var14[0][1] != 0 && MathHelper.floor(this.x) - var1 == var14[0][0] && MathHelper.floor(this.z) - var3 == var14[0][2]) {
                    this.setPosition(this.x, this.y + (double)var14[0][1], this.z);
                } else if (var14[1][1] != 0 && MathHelper.floor(this.x) - var1 == var14[1][0] && MathHelper.floor(this.z) - var3 == var14[1][2]) {
                    this.setPosition(this.x, this.y + (double)var14[1][1], this.z);
                }

                if (this.passenger != null) {
                    this.velocityX *= (double)0.997F;
                    this.velocityY *= (double)0.0F;
                    this.velocityZ *= (double)0.997F;
                } else {
                    if (this.type == 2) {
                        double var61 = (double)MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
                        if (var61 > 0.01) {
                            var6 = true;
                            this.pushX /= var61;
                            this.pushZ /= var61;
                            double var41 = 0.04;
                            this.velocityX *= (double)0.8F;
                            this.velocityY *= (double)0.0F;
                            this.velocityZ *= (double)0.8F;
                            this.velocityX += this.pushX * var41;
                            this.velocityZ += this.pushZ * var41;
                        } else {
                            this.velocityX *= (double)0.9F;
                            this.velocityY *= (double)0.0F;
                            this.velocityZ *= (double)0.9F;
                        }
                    }

                    this.velocityX *= (double)0.96F;
                    this.velocityY *= (double)0.0F;
                    this.velocityZ *= (double)0.96F;
                }

                Vec3d var62 = this.snapPositionToRail(this.x, this.y, this.z);
                if (var62 != null && var10 != null) {
                    double var40 = (var10.y - var62.y) * 0.05;
                    var23 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                    if (var23 > (double)0.0F) {
                        this.velocityX = this.velocityX / var23 * (var23 + var40);
                        this.velocityZ = this.velocityZ / var23 * (var23 + var40);
                    }

                    this.setPosition(this.x, var62.y, this.z);
                }

                int var63 = MathHelper.floor(this.x);
                int var64 = MathHelper.floor(this.z);
                if (var63 != var1 || var64 != var3) {
                    var23 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                    this.velocityX = var23 * (double)(var63 - var1);
                    this.velocityZ = var23 * (double)(var64 - var3);
                }

                if (this.type == 2) {
                    double var42 = (double)MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
                    if (var42 > 0.01 && this.velocityX * this.velocityX + this.velocityZ * this.velocityZ > 0.001) {
                        this.pushX /= var42;
                        this.pushZ /= var42;
                        if (this.pushX * this.velocityX + this.pushZ * this.velocityZ < (double)0.0F) {
                            this.pushX = (double)0.0F;
                            this.pushZ = (double)0.0F;
                        } else {
                            this.pushX = this.velocityX;
                            this.pushZ = this.velocityZ;
                        }
                    }
                }

                if (var12) {
                    double var65 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                    if (var65 > 0.01) {
                        double var44 = 0.06;
                        this.velocityX += this.velocityX / var65 * var44;
                        this.velocityZ += this.velocityZ / var65 * var44;
                    } else if (var11 == 1) {
                        if (this.world.shouldSuffocate(var1 - 1, var2, var3)) {
                            this.velocityX = 0.02;
                        } else if (this.world.shouldSuffocate(var1 + 1, var2, var3)) {
                            this.velocityX = -0.02;
                        }
                    } else if (var11 == 0) {
                        if (this.world.shouldSuffocate(var1, var2, var3 - 1)) {
                            this.velocityZ = 0.02;
                        } else if (this.world.shouldSuffocate(var1, var2, var3 + 1)) {
                            this.velocityZ = -0.02;
                        }
                    }
                }
            } else {
                if (this.velocityX < -var4) {
                    this.velocityX = -var4;
                }

                if (this.velocityX > var4) {
                    this.velocityX = var4;
                }

                if (this.velocityZ < -var4) {
                    this.velocityZ = -var4;
                }

                if (this.velocityZ > var4) {
                    this.velocityZ = var4;
                }

                if (this.onGround) {
                    this.velocityX *= (double)0.5F;
                    this.velocityY *= (double)0.5F;
                    this.velocityZ *= (double)0.5F;
                }

                this.move(this.velocityX, this.velocityY, this.velocityZ);
                if (!this.onGround) {
                    this.velocityX *= (double)0.95F;
                    this.velocityY *= (double)0.95F;
                    this.velocityZ *= (double)0.95F;
                }
            }

            this.pitch = 0.0F;
            double var49 = this.prevX - this.x;
            double var50 = this.prevZ - this.z;
            if (var49 * var49 + var50 * var50 > 0.001) {
                this.yaw = (float)(Math.atan2(var50, var49) * (double)180.0F / Math.PI);
                if (this.yawFlipped) {
                    this.yaw += 180.0F;
                }
            }

            double var51;
            for(var51 = (double)(this.yaw - this.prevYaw); var51 >= (double)180.0F; var51 -= (double)360.0F) {
            }

            while(var51 < (double)-180.0F) {
                var51 += (double)360.0F;
            }

            if (var51 < (double)-170.0F || var51 >= (double)170.0F) {
                this.yaw += 180.0F;
                this.yawFlipped = !this.yawFlipped;
            }

            this.setRotation(this.yaw, this.pitch);
            List var16 = this.world.getEntities(this, this.boundingBox.expand((double)0.2F, (double)0.0F, (double)0.2F));
            if (var16 != null && var16.size() > 0) {
                for(int var54 = 0; var54 < var16.size(); ++var54) {
                    Entity var18 = (Entity)var16.get(var54);
                    if (var18 != this.passenger && var18.isPushable() && var18 instanceof MinecartEntity) {
                        var18.onCollision(this);
                    }
                }
            }

            if (this.passenger != null && this.passenger.dead) {
                this.passenger = null;
            }

            if (var6 && this.random.nextInt(4) == 0) {
                --this.fuel;
                if (this.fuel < 0) {
                    this.pushX = this.pushZ = (double)0.0F;
                }

                this.world.addParticle("largesmoke", this.x, this.y + 0.8, this.z, (double)0.0F, (double)0.0F, (double)0.0F);
            }

        }
    }

    public boolean damage(Entity damageSource, int amount) {
        if (!this.world.isRemote && !this.dead) {
//            this.damageWobbleSide = -this.damageWobbleSide;
//            this.damageWobbleTicks = 10;
//            this.scheduleVelocityUpdate();
//            this.damageWobbleStrength += amount * 10;
            setMillRockDirection(-getMillRockDirection());
            setMillTimeSinceHit(10);
            this.scheduleVelocityUpdate();
            setMillCurrentDamage(getMillCurrentDamage()+(amount * 10));
            if (getMillCurrentDamage() > 40) { //getMillCurrentDamage() //this.damageWobbleStrength
                if (this.passenger != null) {
                    this.passenger.setVehicle(this);
                }

                this.markDead();
                this.dropItem(ItemListener.steelMinecart.id, 1, 0.0F);
                if (this.type == 1) {
                    MinecartEntity var3 = this;

                    for(int var4 = 0; var4 < var3.size(); ++var4) {
                        ItemStack var5 = var3.getStack(var4);
                        if (var5 != null) {
                            float var6 = this.random.nextFloat() * 0.8F + 0.1F;
                            float var7 = this.random.nextFloat() * 0.8F + 0.1F;
                            float var8 = this.random.nextFloat() * 0.8F + 0.1F;

                            while(var5.count > 0) {
                                int var9 = this.random.nextInt(21) + 10;
                                if (var9 > var5.count) {
                                    var9 = var5.count;
                                }

                                var5.count -= var9;
                                ItemEntity var10 = new ItemEntity(this.world, this.x + (double)var6, this.y + (double)var7, this.z + (double)var8, new ItemStack(var5.itemId, var9, var5.getDamage()));
                                float var11 = 0.05F;
                                var10.velocityX = (double)((float)this.random.nextGaussian() * var11);
                                var10.velocityY = (double)((float)this.random.nextGaussian() * var11 + 0.2F);
                                var10.velocityZ = (double)((float)this.random.nextGaussian() * var11);
                                this.world.spawnEntity(var10);
                            }
                        }
                    }

                    this.dropItem(Block.CHEST.id, 1, 0.0F);
                } else if (this.type == 2) {
                    this.dropItem(Block.FURNACE.id, 1, 0.0F);
                }
            }

            return true;
        } else {
            return true;
        }
    }

    //CurrentDamage
    public int getMillCurrentDamage()
    {
        return dataTracker.getByte(16);
    }

    public void setMillCurrentDamage(int i)
    {
        dataTracker.set(16, (byte) i);
    }
    //TimeSinceHit
    public int getMillTimeSinceHit()
    {
        return dataTracker.getByte(17);
    }

    public void setMillTimeSinceHit(int i)
    {
        dataTracker.set(17, (byte) i);
    }
    //RockDirection
    public int getMillRockDirection()
    {
        return dataTracker.getByte(18);
    }

    public void setMillRockDirection(int i)
    {
        dataTracker.set(18, (byte) i);
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "SteelMinecart");
    }
}
