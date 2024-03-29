// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityCauldron.java

package net.kozibrodka.wolves.tileentity;


// Referenced classes of package net.minecraft.src:
//            TileEntity, InventoryBase, ItemInstance, NBTTagCompound, 
//            ListTag, mod_FCBetterThanWolves, FCBlockCauldron, FCUtilsInventory, 
//            World, EntityPlayer, FCCraftingManagerCauldron, Item, 
//            Block, FCCraftingManagerCauldronStoked, BlockFire, FCUtilsMisc, 
//            FurnaceRecipes, FoodBase

import net.kozibrodka.wolves.blocks.Cauldron;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.recipe.CauldronCraftingManager;
import net.kozibrodka.wolves.recipe.CauldronStokedCraftingManager;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.food.FoodBase;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;

public class CauldronTileEntity extends TileEntityBase
    implements InventoryBase
{

    public CauldronTileEntity()
    {
        cauldronContents = new ItemInstance[27];
        m_iCauldronCookCounter = 0;
        m_iRenderCooldownCounter = 0;
        m_bContainsValidIngrediantsForState = false;
        m_bForceValidateOnUpdate = false;
        m_iFireFactor = 0;
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        cauldronContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < cauldronContents.length)
            {
                cauldronContents[j] = new ItemInstance(nbttagcompound1);
            }
        }

        if(nbttagcompound.containsKey("m_iCauldronCookCounter"))
        {
            m_iCauldronCookCounter = nbttagcompound.getInt("m_iCauldronCookCounter");
        }
        if(nbttagcompound.containsKey("m_iRenderCooldownCounter"))
        {
            m_iRenderCooldownCounter = nbttagcompound.getInt("m_iRenderCooldownCounter");
        }
        if(nbttagcompound.containsKey("m_bContainsValidIngrediantsForState"))
        {
            m_bContainsValidIngrediantsForState = nbttagcompound.getBoolean("m_bContainsValidIngrediantsForState");
        } else
        {
            m_bForceValidateOnUpdate = true;
        }
        if(nbttagcompound.containsKey("m_iFireFactor"))
        {
            m_iFireFactor = nbttagcompound.getInt("m_iFireFactor");
        } else
        {
            m_bForceValidateOnUpdate = true;
        }
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < cauldronContents.length; i++)
        {
            if(cauldronContents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                cauldronContents[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.put("m_iCauldronCookCounter", m_iCauldronCookCounter);
        nbttagcompound.put("m_iRenderCooldownCounter", m_iRenderCooldownCounter);
    }

    public void tick()
    {
        if(level.isServerSide){
            return;
        }
        int iFireUnderState = ((Cauldron) BlockListener.cauldron).GetFireUnderState(level, x, y, z);
        if(iFireUnderState > 0)
        {
            if(m_bForceValidateOnUpdate)
            {
                ValidateContentsForState(iFireUnderState);
                m_bForceValidateOnUpdate = false;
            }
            ValidateFireFactor(iFireUnderState);
            if(iFireUnderState == 2)
            {
                if(m_iRenderCooldownCounter <= 0)
                {
                    m_iCauldronCookCounter = 0;
                }
                m_iRenderCooldownCounter = 20;
                PerformStokedFireUpdate();
            } else
            if(m_iRenderCooldownCounter > 0)
            {
                m_iRenderCooldownCounter--;
                if(m_iRenderCooldownCounter <= 0)
                {
                    m_iCauldronCookCounter = 0;
                }
            } else
            {
                PerformNormalFireUpdate();
            }
        } else
        {
            m_iCauldronCookCounter = 0;
        }
    }

    public int getInventorySize()
    {
        return 27;
    }

    public ItemInstance getInventoryItem(int iSlot)
    {
        return cauldronContents[iSlot];
    }

    public ItemInstance takeInventoryItem(int iSlot, int iAmount)
    {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        cauldronContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
        markDirty();
    }

    public String getContainerName()
    {
        return "Cauldron";
    }

    public int getMaxItemCount()
    {
        return 64;
    }

    public void markDirty() {
        if (level == null) {
            return;
        }
        int iFireUnderState = ((Cauldron)BlockListener.cauldron).GetFireUnderState(level, x, y, z);
        ValidateContentsForState(iFireUnderState);
    }

    public boolean canPlayerUse(PlayerBase entityPlayer)
    {
        if(level.getTileEntity(x, y, z) != this) {
            return false;
        } else {
            return entityPlayer.squaredDistanceTo((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public void NotifyOfChangeInFireUnder(int iFireUnderState)
    {
        ValidateContentsForState(iFireUnderState);
        ValidateFireFactor(iFireUnderState);
    }

    public void ValidateContentsForState(int iFireUnderState)
    {
        m_bContainsValidIngrediantsForState = false;
        if(iFireUnderState == 1)
        {
            if(CauldronCraftingManager.getInstance().getCraftingResult(this) != null)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(GetUncookedItemInventoryIndex() >= 0)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.dung.id) >= 0 && ContainsFood())
            {
                m_bContainsValidIngrediantsForState = true;
            }
        } else
        if(iFireUnderState == 2)
        {
            if(InventoryHandler.getFirstOccupiedStackOfItem(this, BlockBase.TNT.id) >= 0)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(InventoryHandler.getFirstOccupiedStackOfItem(this, ItemBase.gunpowder.id) >= 0)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.hellfireDust.id) >= 0)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(CauldronStokedCraftingManager.getInstance().getCraftingResult(this) != null)
            {
                m_bContainsValidIngrediantsForState = true;
            }
        }
    }

    public void ValidateFireFactor(int iFireUnderState)
    {
        int iFireFactor = 0;
        if(iFireUnderState > 0)
        {
            iFireFactor = 5;
            int iidToTest = BlockBase.FIRE.id;
            if(iFireUnderState == 2)
            {
                iidToTest = BlockListener.stokedFire.id;
            }
            int tempY = y - 1;
            for(int tempX = x - 1; tempX <= x + 1; tempX++)
            {
                for(int tempZ = z - 1; tempZ <= z + 1; tempZ++)
                {
                    if((tempX != x || tempZ != z) && level.getTileId(tempX, tempY, tempZ) == iidToTest)
                    {
                        iFireFactor++;
                    }
                }

            }

        }
        m_iFireFactor = iFireFactor;
    }

    private void PerformNormalFireUpdate()
    {
        if(m_bContainsValidIngrediantsForState)
        {
            m_iCauldronCookCounter += m_iFireFactor;
            if(m_iCauldronCookCounter >= 1950)
            {
                int iDungIndex = InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.dung.id);
                if(iDungIndex >= 0 && DestroyAllFoodInInventory())
                {
                    m_iCauldronCookCounter = 0;
                    return;
                }
                if(CauldronCraftingManager.getInstance().getCraftingResult(this) != null)
                {
                    ItemInstance cookedStack = CauldronCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
                    if(!$assertionsDisabled && cookedStack == null)
                    {
                        throw new AssertionError();
                    }
                    if(!InventoryHandler.addItemInstanceToInventory(this, cookedStack))
                    {
                        UnsortedUtils.ejectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
                    }
                } else
                {
                    AttemptToCookFood();
                }
                m_iCauldronCookCounter = 0;
            }
        } else
        {
            m_iCauldronCookCounter = 0;
        }
    }

    private void PerformStokedFireUpdate()
    {
        if(m_bContainsValidIngrediantsForState)
        {
            m_iCauldronCookCounter += m_iFireFactor;
            if(m_iCauldronCookCounter >= 1950)
            {
                if(InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.hellfireDust.id) >= 0 || InventoryHandler.getFirstOccupiedStackOfItem(this, BlockBase.TNT.id) >= 0 || InventoryHandler.getFirstOccupiedStackOfItem(this, ItemBase.gunpowder.id) >= 0)
                {
                    BlowUpCauldron();
                } else
                if(CauldronStokedCraftingManager.getInstance().getCraftingResult(this) != null)
                {
                    ItemInstance cookedStack = CauldronStokedCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
                    if(!$assertionsDisabled && cookedStack == null)
                    {
                        throw new AssertionError();
                    }
                    if(!InventoryHandler.addItemInstanceToInventory(this, cookedStack))
                    {
                        UnsortedUtils.ejectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
                    }
                }
                m_iCauldronCookCounter = 0;
            }
        } else
        {
            m_iCauldronCookCounter = 0;
        }
    }

    private void AttemptToCookFood()
    {
        int iUncookedFoodIndex = GetUncookedItemInventoryIndex();
        if(iUncookedFoodIndex >= 0)
        {
            ItemInstance tempStack = SmeltingRecipeRegistry.getInstance().getResult(cauldronContents[iUncookedFoodIndex].getType().id);
            ItemInstance cookedStack = tempStack.copy();
            takeInventoryItem(iUncookedFoodIndex, 1);
            if(!InventoryHandler.addItemInstanceToInventory(this, cookedStack))
            {
                UnsortedUtils.ejectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
            }
        }
    }

    public int getCookProgressScaled(int iScale)
    {
        return (m_iCauldronCookCounter * iScale) / 1950;
    }

    public boolean IsCooking()
    {
        return m_iCauldronCookCounter > 0;
    }

    public int GetUncookedItemInventoryIndex()
    {
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(cauldronContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = cauldronContents[tempIndex].getType();
            if(tempItem != null && (tempItem instanceof FoodBase) && SmeltingRecipeRegistry.getInstance().getResult(tempItem.id) != null)
            {
                return tempIndex;
            }
        }

        return -1;
    }

    private boolean ContainsFood()
    {
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(cauldronContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = cauldronContents[tempIndex].getType();
            if(tempItem != null && (tempItem instanceof FoodBase))
            {
                return true;
            }
        }

        return false;
    }

    private boolean DestroyAllFoodInInventory()
    {
        boolean bFoodDestroyed = false;
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(cauldronContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = cauldronContents[tempIndex].getType();
            if(tempItem != null && (tempItem instanceof FoodBase))
            {
                int stackSize = cauldronContents[tempIndex].count;
                cauldronContents[tempIndex] = null;
                ItemInstance spoiledStack = new ItemInstance(ItemListener.foulFood, stackSize);
                setInventoryItem(tempIndex, spoiledStack);
                bFoodDestroyed = true;
            }
        }

        return bFoodDestroyed;
    }

    private void BlowUpCauldron()
    {
        int iHellfireCount = InventoryHandler.itemCountInInventory(this, ItemListener.hellfireDust.id, -1);
        float fExplosionSize = ((float)iHellfireCount * 10F) / 64F;
        fExplosionSize += ((float) InventoryHandler.itemCountInInventory(this, ItemBase.gunpowder.id, -1) * 10F) / 64F;
        int iTNTCount = InventoryHandler.itemCountInInventory(this, BlockBase.TNT.id, -1);
        if(iTNTCount > 0)
        {
            if(fExplosionSize < 4F)
            {
                fExplosionSize = 4F;
            }
            fExplosionSize += InventoryHandler.itemCountInInventory(this, BlockBase.TNT.id, -1);
        }
        if(fExplosionSize < 2.0F)
        {
            fExplosionSize = 2.0F;
        } else
        if(fExplosionSize > 10F)
        {
            fExplosionSize = 10F;
        }
        InventoryHandler.clearInventoryContents(this);
        level.setTile(x, y, z, 0);
        level.createExplosion(null, x, y, z, fExplosionSize);
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private final int iCauldronInventorySize = 27;
    private final int iCauldronStackSizeLimit = 64;
    private final double dCauldronMaxPlayerInteractionDist = 64D;
    private final int iPrimaryFireFactor = 5;
    private final int iSecondaryFireFactor = 1;
    private final int iRenderingTicksToCooldown = 20;
    private final int iHellfireDustRequiredToConcentrate = 8;
    private final int iCauldronTimeToCook = 1950;
    private ItemInstance cauldronContents[];
    public int m_iCauldronCookCounter;
    private int m_iRenderCooldownCounter;
    private boolean m_bContainsValidIngrediantsForState;
    private boolean m_bForceValidateOnUpdate;
    private int m_iFireFactor;
    static final boolean $assertionsDisabled; /* synthetic field */

    static 
    {
        $assertionsDisabled = !(CauldronTileEntity.class).desiredAssertionStatus();
    }
}
