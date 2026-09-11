package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

import java.util.List;

//@HasTrackingParameters(trackingDistance = 240, updatePeriod = Integer.MAX_VALUE, sendVelocity = TriState.FALSE)
@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2, sendVelocity = TriState.TRUE)
public class FCEntityTest2 extends Entity implements EntitySpawnDataProvider {

    public FCEntityTest2(World world)
    {
        super(world);
        blocksSameBlockSpawning = true;
//        setBoundingBoxSpacing(4.8F, 4.8F);
//        standingEyeHeight = height / 2.0F;
//        standingEyeHeight = height / 1.5F;
        standingEyeHeight = 0;
//        standingEyeHeight = 0;
    }

    public int tile;
    //    public int facing;
    public int timeFalling = 0;
    public boolean receivedP;


    @Override
    public float getEyeHeight() {
        return 2.0F;
    }

    public FCEntityTest2(World world, double x, double y, double z, boolean flag)
    {
        this(world);
        this.tile = 4;
        setFacingMeta(1);
        this.blocksSameBlockSpawning = true;
//        this.setPosition(x, y + (double)this.standingEyeHeight, z);
        this.setPosition(x, y, z); /// KURWA
        this.velocityX = 0.0D;
        this.velocityY = 0.0D;
        this.velocityZ = 0.0D;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        AlignBoundingBoxWithAxis();
    }

    private void AlignBoundingBoxWithAxis()
    {
            boundingBox.set(x - 0.40000000596046448D, y - 2.4000000953674316D, z - 2.4000000953674316D, x + 0.40000000596046448D, y + 2.4000000953674316D, z + 2.4000000953674316D);
    }

//    public FCEntityTest2(World level, double x, double y, double z) {
//        this(level);
//        setPosition(x, y, z);
//    }

    public FCEntityTest2(World level, double x, double y, double z) {
        this(level);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        if(!receivedP) {
            System.out.println("CLIENT POS");
            this.setPosition(x, y, z);
            this.setRotation(pitch, yaw);
            tile = BlockListener.anvil.id;
            receivedP = true;
            AlignBoundingBoxWithAxis();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void setVelocityClient(double x, double y, double z) {
        this.clientVelocityX = this.velocityX = x;
        this.clientVelocityY = this.velocityY = y;
        this.clientVelocityZ = this.velocityZ = z;
    }

    @Override
    public Box getCollisionAgainstShape(Entity other) {
        return other.boundingBox;
    }

    @Override
    public Box getBoundingBox() {
        return null;
    }

    @Override
    public boolean isPushable() {
        return true;
    }


    @Override
    public boolean interact(PlayerEntity entityplayer)
    {
        System.out.println("SIEMA: " );
        return false;
    }

    @Override
    public boolean damage(Entity damageSource, int amount) {
        System.out.println("DAMAGE: " );
        if(!world.isRemote){
            markDead();
        }
        return false;
    }

    @Override
    public boolean isCollidable() {
        return !dead;
    }

    @Override
    protected boolean bypassesSteppingEffects() {
        return false;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //metaData
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void processServerEntityStatus(byte status) {
        if (status == 6) {
            world.playSound(this.x, this.y, this.z, "wolves:anvil_land", 1.0F, 1.2F);
        } else {
            super.processServerEntityStatus(status);
        }
    }

    @Override
    protected void writeNbt(NbtCompound arg) {
        arg.putByte("Tile", (byte) this.tile);
        arg.putInt("Facing", getFacingMeta());
    }

    @Override
    protected void readNbt(NbtCompound arg) {
        this.tile = arg.getByte("Tile") & 255;
        setFacingMeta(arg.getInt("Facing"));
        markDead();
    }

    //MetaData
    public int getFacingMeta()
    {
        return dataTracker.getByte(16);
    }

    public void setFacingMeta(int i)
    {
        dataTracker.set(16, (byte) i);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getShadowRadius() {
        return 0.0F;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(ConfigListener.NAMESPACE, "WaterTEST2");
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }



    @Override
    public void tick() {

//        if(!receivedP && !world.isRemote) {
////            this.velocityY -= 0.5D;
//            this.velocityY -= 0.05D;
//            receivedP = true;
//            this.move(this.velocityX, this.velocityY, this.velocityZ);
//            System.out.println(x + " " + y + " " + z);
//            System.out.println("PRZESUNĄŁEM + " + velocityY);
//        }

        if(true)
        {
            return;
        }

//            this.velcityY -= 0.0005D;
        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;
//            System.out.println(velocityX + " " + velocityY + " " + velocityZ);
            System.out.println(x + " " + y + " " + z);
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityY -= -0D;
//            this.velocityY -= 0.0005D;
            this.velocityY -= -0D;
//            if(true){
//                return;
//            }

        if(!receivedP && !world.isRemote) {
            setPosition(x,y - 0.01D,z);
            receivedP = true;
        }

    }


    @Environment(EnvType.CLIENT)
    private double clientVelocityX;
    @Environment(EnvType.CLIENT)
    private double clientVelocityY;
    @Environment(EnvType.CLIENT)
    private double clientVelocityZ;
}
