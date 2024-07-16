package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.block.WoolBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class FCEntityTEST extends Entity implements EntitySpawnDataProvider {
    public FCEntityTEST(World arg) {
        super(arg);
        setBoundingBoxSpacing(2F, 2F);
    }

    public FCEntityTEST(World world, double x, double y, double z, int type) {
        this(world);
        setPos(x, y, z);
        setColour(type);
    }

    public FCEntityTEST(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    public boolean damage(Entity entity, int i)
    {
        if(world.isRemote || dead)
        {
            return true;
        }
        System.out.println(dataTracker.getByte(16) & 15);
//        remove();
        return true;
    }

    public boolean interact(PlayerEntity entityplayer){
        ItemStack itemstack = entityplayer.inventory.getSelectedItem();
        if(itemstack !=null && itemstack.itemId == Item.DYE.id)
        {
            int var4 = WoolBlock.method_1(itemstack.getDamage());
            setColour(var4);
            return true;
        }
        if(world.isRemote){
//            System.out.println("client: " + y + " " + clientY);
//            y = (double) clientY/32D;
            System.out.println(dataTracker.getByte(16) & 15);
        }else{
//            System.out.println("HOST: " + y);
            System.out.println(dataTracker.getByte(16) & 15);
        }
        return true;
    }


    protected boolean bypassesSteppingEffects()
    {
        return false;
    }

    public Box method_1379(Entity entity)
    {
        return entity.boundingBox;
    }

    public Box getBoundingBox()
    {
        return boundingBox;
    }

    public boolean isPushable()
    {
        return false;
    }

    public boolean isCollidable()
    {
        return !dead;
    }

    public void tick()
    {
        if(dead)
        {
            return;
        }
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0) {
            iFullUpdateTickCount = 20;
            int iCenterI = (int) (x - 0.5D);
            int iCenterJ = (int) (y - 0.5D);
            int iCenterK = (int) (z - 0.5D);
            int iCenterid = world.getBlockId(iCenterI, iCenterJ, iCenterK);
            if (iCenterid != BlockListener.axleBlock.id) {
                if(!world.isRemote){
                    markDead();
                }
                return;
            }
        }
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //Color
    }

    public void writeNbt(NbtCompound arg) {
        arg.putByte("Color", (byte)this.getColour());
    }

    public void readNbt(NbtCompound arg) {
        this.setColour(arg.getByte("Color"));
    }

    public int getColour() {
        return this.dataTracker.getByte(16) & 15;
    }

    public void setColour(int i) {
        byte var2 = this.dataTracker.getByte(16);
        this.dataTracker.set(16, (byte)(var2 & 240 | i & 15));
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(ConfigListener.MOD_ID, "StapiTEST");
    }

    public int iFullUpdateTickCount;
}
