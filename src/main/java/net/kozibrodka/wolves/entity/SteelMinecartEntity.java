package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.compat.nfc.MinecartLogicExpander;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.utils.BetterThanWolves;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
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

public class SteelMinecartEntity extends MinecartEntity implements EntitySpawnDataProvider {
    public SteelMinecartEntity(World world) {
        super(world);
    }

    public SteelMinecartEntity(World world, double x, double y, double z, int type) {
        super(world, x, y, z, type);
    }

    public SteelMinecartEntity(World level, double x, double y, double z) {
        this(level);
        this.setPosition(x, y + (double)this.standingEyeHeight, z);
    }

    /// Szybsza wersja waganika niż iron. W piekle boost nie działa

    @Override
    @Environment(EnvType.CLIENT)
    public void processServerEntityStatus(byte status) {
        if (status >= 6 && status < 11) {
            int dmg = status - 6;
            this.damageWobbleSide = -this.damageWobbleSide;
            this.damageWobbleTicks = 10;
            this.scheduleVelocityUpdate();
            this.damageWobbleStrength += dmg * 10;
        }  else {
            super.processServerEntityStatus(status);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateHurt() {
        this.damageWobbleSide = -this.damageWobbleSide;
        this.damageWobbleTicks = 10;
        this.damageWobbleStrength += this.damageWobbleStrength * 10;
    }


    @Override
    public void tick() {
        if (BetterThanWolves.isNewFrontierCraftPresent) {
            handleSteering();
        }
        /// Hurt Animation
        if (this.damageWobbleTicks > 0) {
            --this.damageWobbleTicks;
        }
        if (this.damageWobbleStrength > 0) {
            --this.damageWobbleStrength;
        }
        if (this.world.isRemote && this.clientInterpolationSteps > 0) {  /// Client Position Interpolation
            if (this.clientInterpolationSteps > 0) {
                double lerpX = this.x + (this.clientX - this.x) / (double)this.clientInterpolationSteps;
                double lerpY = this.y + (this.clientY - this.y) / (double)this.clientInterpolationSteps;
                double lerpZ = this.z + (this.clientZ - this.z) / (double)this.clientInterpolationSteps;

                double lerpYaw;
                for(lerpYaw = this.clientPitch - (double)this.yaw; lerpYaw < (double)-180.0F; lerpYaw += 360.0F) {
                }

                while(lerpYaw >= (double)180.0F) {
                    lerpYaw -= 360.0F;
                }

                this.yaw = (float)((double)this.yaw + lerpYaw / (double)this.clientInterpolationSteps);
                this.pitch = (float)((double)this.pitch + (this.clientYaw - (double)this.pitch) / (double)this.clientInterpolationSteps);
                --this.clientInterpolationSteps;
                this.setPosition(lerpX, lerpY, lerpZ);
                this.setRotation(this.yaw, this.pitch);
            } else {
                this.setPosition(this.x, this.y, this.z);
                this.setRotation(this.yaw, this.pitch);
            }

        } else {
            this.prevX = this.x;
            this.prevY = this.y;
            this.prevZ = this.z;
            this.velocityY -= 0.04F;
            int floorX = MathHelper.floor(this.x);
            int floorY = MathHelper.floor(this.y);
            int floorZ = MathHelper.floor(this.z);
            if (RailBlock.isRail(this.world, floorX, floorY - 1, floorZ)) {
                --floorY;
            }
            double maxSpeedLimit = changeSpeedLimit(0.5); /// Zwiększenie Max prędkości, > 0.5D zatrzymania podjazd, >0.65D wypadanie na zakręcie
            if (world.dimension.isNether) {
                maxSpeedLimit = 0.4;
            }
            boolean var6 = false;
            double gravitySpeedUP = 0.0078125F; /// Spowolnienie GRAWITACYJNE, og = 0.0078125F
            double gravitySpeedDOWN = 0.0078125F; /// Przyspieszenie GRAWITACYJNE, og = 0.0078125F
            int floorID = this.world.getBlockId(floorX, floorY, floorZ);
            if (RailBlock.isRail(floorID)) {
                Vec3d var10 = this.snapPositionToRail(this.x, this.y, this.z);
                int floorMETA = this.world.getBlockMeta(floorX, floorY, floorZ);
                this.y = floorY;
                boolean goldRail_ON = false;
                boolean goldRail_OFF = false;
                if (floorID == Block.POWERED_RAIL.id) {
                    goldRail_ON = (floorMETA & 8) != 0;
                    goldRail_OFF = !goldRail_ON;
                }

                if (((RailBlock)Block.BLOCKS[floorID]).isAlwaysStraight()) {
                    floorMETA &= 7;
                }

                if (floorMETA >= 2 && floorMETA <= 5) {
                    this.y = floorY + 1;
                }
                /// Gwawitacja, najechanie na POCHYŁE wariacje torów
//                if (floorMETA == 2) {
//                    this.velocityX -= gravitySpeedUP;
//                }
//                if (floorMETA == 3) {
//                    this.velocityX += gravitySpeedUP;
//                }
//                if (floorMETA == 4) {
//                    this.velocityZ += gravitySpeedUP;
//                }
//                if (floorMETA == 5) {
//                    this.velocityZ -= gravitySpeedUP;
//                }
                /// Grawitacja, najechanie na POCHYŁE wariacje torów
                if (floorMETA == 2) {
                    if (this.velocityX > 0) {
                        this.velocityX -= gravitySpeedUP;
                    } else {
                        this.velocityX -= gravitySpeedDOWN;
                    }
                }
                if (floorMETA == 3) {
                    if (this.velocityX < 0) {
                        this.velocityX += gravitySpeedUP;
                    } else {
                        this.velocityX += gravitySpeedDOWN;
                    }
                }
                if (floorMETA == 4) {
                    if (this.velocityZ < 0) {
                        this.velocityZ += gravitySpeedUP;
                    } else {
                        this.velocityZ += gravitySpeedDOWN;
                    }
                }
                if (floorMETA == 5) {
                    if (this.velocityZ > 0) {
                        this.velocityZ -= gravitySpeedUP;
                    } else {
                        this.velocityZ -= gravitySpeedDOWN;
                    }
                }
                /// ---
                int[][] var14 = ADJACENT_RAIL_POSITIONS_BY_SHAPE[floorMETA];
                double var15 = var14[1][0] - var14[0][0];
                double var17 = var14[1][2] - var14[0][2];
                double var19 = Math.sqrt(var15 * var15 + var17 * var17);
                double var21 = this.velocityX * var15 + this.velocityZ * var17;
                if (var21 < (double)0.0F) {
                    var15 = -var15;
                    var17 = -var17;
                }
                /// EXPERIMENTAL - Minecart Momentum Fix - nie działa
//                double var23;
//                this.velocityX = var21 * var15 / (var19 * var19);
//                this.velocityZ = var21 * var17 / (var19 * var19);
                /// stary kod
                double var23 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                this.velocityX = var23 * var15 / var19;
                this.velocityZ = var23 * var17 / var19;
                /// ---
                if (goldRail_OFF) {
                    double var25 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                    /// Spowolnienie Wyłączonym Redstone_Gold_Rail
                    if (var25 < 0.03) { /// jedzie wolno, to zatrzymuje go do zera.
                        this.velocityX *= 0.0F;
                        this.velocityY *= 0.0F;
                        this.velocityZ *= 0.0F;
                    } else {
                        this.velocityX *= 0.5F;
                        this.velocityY *= 0.0F;
                        this.velocityZ *= 0.5F;
                    }
                }

                double var57 = 0.0F;
                double var27 = (double)floorX + (double)0.5F + (double)var14[0][0] * (double)0.5F;
                double var29 = (double)floorZ + (double)0.5F + (double)var14[0][2] * (double)0.5F;
                double var31 = (double)floorX + (double)0.5F + (double)var14[1][0] * (double)0.5F;
                double var33 = (double)floorZ + (double)0.5F + (double)var14[1][2] * (double)0.5F;
                var15 = var31 - var27;
                var17 = var33 - var29;
                if (var15 == (double)0.0F) {
                    this.x = (double)floorX + (double)0.5F;
                    var57 = this.z - (double)floorZ;
                } else if (var17 == (double)0.0F) {
                    this.z = (double)floorZ + (double)0.5F;
                    var57 = this.x - (double)floorX;
                } else {
                    double var35 = this.x - var27;
                    double var37 = this.z - var29;
                    double var39 = (var35 * var15 + var37 * var17) * (double)2.0F;
                    var57 = var39;
                }

                this.x = var27 + var15 * var57;
                this.z = var29 + var17 * var57;
                this.setPosition(this.x, this.y + (double)this.standingEyeHeight, this.z);
                double newVelX = this.velocityX;
                double newVelZ = this.velocityZ;
                if (this.passenger != null) {
                    newVelX *= 0.75F;
                    newVelZ *= 0.75F;
                }
                /// Aplikowanie Limitów prędkości.
                if (newVelX < -maxSpeedLimit) {
                    newVelX = -maxSpeedLimit;
                }
                if (newVelX > maxSpeedLimit) {
                    newVelX = maxSpeedLimit;
                }
                if (newVelZ < -maxSpeedLimit) {
                    newVelZ = -maxSpeedLimit;
                }
                if (newVelZ > maxSpeedLimit) {
                    newVelZ = maxSpeedLimit;
                }
                /// ---
                /// MOVE - SUBTICKING - nie działa
//                this.move(newVelX, 0.0F, newVelZ);

//                /// Aplikowanie ruchu metodą SUB-TICKINGU (Jak w nowym MC)
//                double totalX = newVelX;
//                double totalZ = newVelZ;
//                double totalMotion = Math.sqrt(totalX * totalX + totalZ * totalZ);
//
//                // Ustalamy bezpieczny krok ruchu (max 0.3 bloku na jedno przesunięcie)
//                double maxStep = 0.3D;
//
//                if (totalMotion > maxStep) {
//                    // Obliczamy ile mikro-kroków musimy wykonać w tym jednym ticku
//                    int steps = (int) Math.ceil(totalMotion / maxStep);
//                    double stepX = totalX / steps;
//                    double stepZ = totalZ / steps;
//
//                    for (int i = 0; i < steps; i++) {
//                        // Wykonujemy mały, bezpieczny krok ruchu
//                        this.move(stepX, 0.0F, stepZ);
//
//                        // Po każdym mikro-kroku natychmiastowo dociągamy pozycję do toru,
//                        // aby gra w następnym mikro-kroku wiedziała gdzie dokładnie skręcić.
//                        int currentX = MathHelper.floor(this.x);
//                        int currentZ = MathHelper.floor(this.z);
//
//                            // Ten kawałek kodu dociąga wózek do linii szyn w locie
//                            double trainX = (double)currentX + 0.5D;
//                            double trainZ = (double)currentZ + 0.5D;
//                            this.x = trainX + (this.x - trainX) * 0.4D; // delikatne dociąganie (0.4D)
//                            this.z = trainZ + (this.z - trainZ) * 0.4D;
//                    }
//                } else {
//                    // Prędkość jest mała (< 0.3D), wykonujemy standardowy, pojedynczy ruch
//                    this.move(newVelX, 0.0F, newVelZ);
//                }

                /// --- ///
                if (var14[0][1] != 0 && MathHelper.floor(this.x) - floorX == var14[0][0] && MathHelper.floor(this.z) - floorZ == var14[0][2]) {
                    this.setPosition(this.x, this.y + (double)var14[0][1], this.z);
                } else if (var14[1][1] != 0 && MathHelper.floor(this.x) - floorX == var14[1][0] && MathHelper.floor(this.z) - floorZ == var14[1][2]) {
                    this.setPosition(this.x, this.y + (double)var14[1][1], this.z);
                }

                if (this.passenger != null) {
                    boolean DEV_DISABLE = true;
                    /// Aplikacja spowolnienia Oporem Tarcia i Powietrza - na prostym poziomym podłożu
                    if (world.dimension.isNether || DEV_DISABLE) { /// nether = og steel minecart speed
                        this.velocityX *= 0.997F;
                        this.velocityY *= 0.0F;
                        this.velocityZ *= 0.997F;
                    }else{
                        this.velocityX *= 0.999F;
                        this.velocityY *= 0.0F;
                        this.velocityZ *= 0.999F;
                    }
                } else {
                    if (this.type == 2) {
                        double var61 = MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
                        if (var61 > 0.01) {
                            var6 = true;
                            this.pushX /= var61;
                            this.pushZ /= var61;
                            double var41 = 0.04;
                            this.velocityX *= 0.8F;
                            this.velocityY *= 0.0F;
                            this.velocityZ *= 0.8F;
                            this.velocityX += this.pushX * var41;
                            this.velocityZ += this.pushZ * var41;
                        } else {
                            this.velocityX *= 0.9F;
                            this.velocityY *= 0.0F;
                            this.velocityZ *= 0.9F;
                        }
                    }

                    this.velocityX *= 0.96F;
                    this.velocityY *= 0.0F;
                    this.velocityZ *= 0.96F;
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
                if (var63 != floorX || var64 != floorZ) {
                    var23 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                    this.velocityX = var23 * (double)(var63 - floorX);
                    this.velocityZ = var23 * (double)(var64 - floorZ);
                }

                if (this.type == 2) {
                    double var42 = MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
                    if (var42 > 0.01 && this.velocityX * this.velocityX + this.velocityZ * this.velocityZ > 0.001) {
                        this.pushX /= var42;
                        this.pushZ /= var42;
                        if (this.pushX * this.velocityX + this.pushZ * this.velocityZ < (double)0.0F) {
                            this.pushX = 0.0F;
                            this.pushZ = 0.0F;
                        } else {
                            this.pushX = this.velocityX;
                            this.pushZ = this.velocityZ;
                        }
                    }
                }

                if (goldRail_ON) {
                    double var65 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
                    double movingSpeedBoost = 0.06D; /// GOLD_RAIL Przyspieszenie JADĄCEGO wózka, og = 0.06D
                    double startingSpeedBoost = 0.02D; /// GOLD_RAIL Przyspieszenie STOJĄCEGO wózka, START z miejsca, og = 0.02D
                    if (var65 > 0.01) {
                        this.velocityX += this.velocityX / var65 * movingSpeedBoost;
                        this.velocityZ += this.velocityZ / var65 * movingSpeedBoost;
                    } else if (floorMETA == 1) {
                        if (this.world.shouldSuffocate(floorX - 1, floorY, floorZ)) {
                            this.velocityX = startingSpeedBoost;
                        } else if (this.world.shouldSuffocate(floorX + 1, floorY, floorZ)) {
                            this.velocityX = -startingSpeedBoost;
                        }
                    } else if (floorMETA == 0) {
                        if (this.world.shouldSuffocate(floorX, floorY, floorZ - 1)) {
                            this.velocityZ = startingSpeedBoost;
                        } else if (this.world.shouldSuffocate(floorX, floorY, floorZ + 1)) {
                            this.velocityZ = -startingSpeedBoost;
                        }
                    }
                }
            } else { /// wózek nie jest na torach
                /// Aplikowanie Limitów prędkości.
                if (this.velocityX < -maxSpeedLimit) {
                    this.velocityX = -maxSpeedLimit;
                }
                if (this.velocityX > maxSpeedLimit) {
                    this.velocityX = maxSpeedLimit;
                }
                if (this.velocityZ < -maxSpeedLimit) {
                    this.velocityZ = -maxSpeedLimit;
                }
                if (this.velocityZ > maxSpeedLimit) {
                    this.velocityZ = maxSpeedLimit;
                }
                /// ---
                if (this.onGround) {
                    this.velocityX *= getGroundSlidingFactor();
                    this.velocityY *= 0.5F;
                    this.velocityZ *= getGroundSlidingFactor();
                }

                this.move(this.velocityX, this.velocityY, this.velocityZ);
                if (!this.onGround) {
                    this.velocityX *= 0.95F;
                    this.velocityY *= 0.95F;
                    this.velocityZ *= 0.95F;
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
            for(var51 = this.yaw - this.prevYaw; var51 >= (double)180.0F; var51 -= 360.0F) {
            }

            while(var51 < (double)-180.0F) {
                var51 += 360.0F;
            }

            if (var51 < (double)-170.0F || var51 >= (double)170.0F) {
                this.yaw += 180.0F;
                this.yawFlipped = !this.yawFlipped;
            }

            this.setRotation(this.yaw, this.pitch);
            List var16 = this.world.getEntities(this, this.boundingBox.expand(0.2F, 0.0F, 0.2F));
            if (var16 != null && !var16.isEmpty()) {
                for (Object o : var16) {
                    Entity var18 = (Entity) o;
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
                    this.pushX = this.pushZ = 0.0F;
                }

                this.world.addParticle("largesmoke", this.x, this.y + 0.8, this.z, 0.0F, 0.0F, 0.0F);
            }

        }
    }

    private void handleSteering() {
        if (this.world.isRemote) {
            return;
        }
        int flooredX = MathHelper.floor(this.x);
        int flooredY = MathHelper.floor(this.y);
        int flooredZ = MathHelper.floor(this.z);
        if (RailBlock.isRail(this.world, flooredX, flooredY - 1, flooredZ)) {
            --flooredY;
        }
        int currentBlockId = this.world.getBlockId(flooredX, flooredY, flooredZ);
        if (!RailBlock.isRail(currentBlockId)) {
            return;
        }
        if (passenger == null) {
            return;
        }
        if (passenger.isSneaking()) {
            velocityX *= 0.9;
            velocityZ *= 0.9;
        } else if (velocityX < 0.1 && velocityX > -0.1 && velocityZ < 0.1 && velocityZ > -0.1) {
            velocityX += passenger.velocityX * 0.6D;
            velocityZ += passenger.velocityZ * 0.6D;
        }
    }

    @Override
    public boolean damage(Entity damageSource, int amount) {
        if (!this.world.isRemote && !this.dead) {
            this.damageWobbleSide = -this.damageWobbleSide;
            this.damageWobbleTicks = 10;
            this.scheduleVelocityUpdate();
            this.damageWobbleStrength += amount * 10;
            if(amount < 5 && this.damageWobbleStrength <= 40){
                world.broadcastEntityEvent(this, (byte)(amount+6));
            }
            if (this.damageWobbleStrength > 40) {
                if (this.passenger != null) {
                    this.passenger.setVehicle(this);
                }

                this.markDead();
                this.dropItem(ItemListener.steelMinecart.id, 1, 0.0F);
                if (this.type == 1) {
                    MinecartEntity var3 = this;

                    for (int var4 = 0; var4 < var3.size(); ++var4) {
                        ItemStack var5 = var3.getStack(var4);
                        if (var5 != null) {
                            float var6 = this.random.nextFloat() * 0.8F + 0.1F;
                            float var7 = this.random.nextFloat() * 0.8F + 0.1F;
                            float var8 = this.random.nextFloat() * 0.8F + 0.1F;

                            while (var5.count > 0) {
                                int var9 = this.random.nextInt(21) + 10;
                                if (var9 > var5.count) {
                                    var9 = var5.count;
                                }

                                var5.count -= var9;
                                ItemEntity var10 = new ItemEntity(this.world, this.x + (double) var6, this.y + (double) var7, this.z + (double) var8, new ItemStack(var5.itemId, var9, var5.getDamage()));
                                float var11 = 0.05F;
                                var10.velocityX = (float) this.random.nextGaussian() * var11;
                                var10.velocityY = (float) this.random.nextGaussian() * var11 + 0.2F;
                                var10.velocityZ = (float) this.random.nextGaussian() * var11;
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

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "SteelMinecart");
    }

    private double changeSpeedLimit(double constant) {
        if (BetterThanWolves.isNewFrontierCraftPresent) {
            return MinecartLogicExpander.changeSpeedLimit(constant, getGroundId());
        }
        return constant;
    }

    private double getGroundSlidingFactor() {
        if (BetterThanWolves.isNewFrontierCraftPresent) {
            return MinecartLogicExpander.getGroundSlidingFactor(getGroundId());
        }
        return 0.5;
    }

    private int getGroundId() {
        return world.getBlockId(MathHelper.floor(x), MathHelper.floor(y) - 1, MathHelper.floor(z));
    }
}
