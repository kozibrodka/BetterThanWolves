package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.block.Wool;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class FCEntityTEST extends EntityBase implements EntitySpawnDataProvider {
    public FCEntityTEST(Level arg) {
        super(arg);
        setSize(2F, 2F);
    }

    public FCEntityTEST(Level world, double x, double y, double z, int type) {
        this(world);
        setPosition(x, y, z);
        setColour(type);
    }

    public FCEntityTEST(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    public boolean damage(EntityBase entity, int i)
    {
        if(level.isServerSide || removed)
        {
            return true;
        }
        System.out.println(dataTracker.getByte(16) & 15);
//        remove();
        return true;
    }

    public boolean interact(PlayerBase entityplayer){
        ItemInstance itemstack = entityplayer.inventory.getHeldItem();
        if(itemstack !=null && itemstack.itemId == ItemBase.dyePowder.id)
        {
            int var4 = Wool.getColour(itemstack.getDamage());
            setColour(var4);
            return true;
        }
        if(level.isServerSide){
//            System.out.println("client: " + y + " " + clientY);
//            y = (double) clientY/32D;
            System.out.println(dataTracker.getByte(16) & 15);
        }else{
//            System.out.println("HOST: " + y);
            System.out.println(dataTracker.getByte(16) & 15);
        }
        return true;
    }


    protected boolean canClimb()
    {
        return false;
    }

    public Box getBoundingBox(EntityBase entity)
    {
        return entity.boundingBox;
    }

    public Box method_1381()
    {
        return boundingBox;
    }

    public boolean method_1380()
    {
        return false;
    }

    public boolean method_1356()
    {
        return !removed;
    }

    public void tick()
    {
        if(removed)
        {
            return;
        }
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0) {
            iFullUpdateTickCount = 20;
            int iCenterI = (int) (x - 0.5D);
            int iCenterJ = (int) (y - 0.5D);
            int iCenterK = (int) (z - 0.5D);
            int iCenterid = level.getTileId(iCenterI, iCenterJ, iCenterK);
            if (iCenterid != BlockListener.axleBlock.id) {
                if(!level.isServerSide){
                    remove();
                }
                return;
            }
        }
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //Color
    }

    public void writeCustomDataToTag(CompoundTag arg) {
        arg.put("Color", (byte)this.getColour());
    }

    public void readCustomDataFromTag(CompoundTag arg) {
        this.setColour(arg.getByte("Color"));
    }

    public int getColour() {
        return this.dataTracker.getByte(16) & 15;
    }

    public void setColour(int i) {
        byte var2 = this.dataTracker.getByte(16);
        this.dataTracker.setInt(16, (byte)(var2 & 240 | i & 15));
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(ConfigListener.MOD_ID, "StapiTEST");
    }

    public int iFullUpdateTickCount;
}
