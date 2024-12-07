// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityCauldron.java

package net.kozibrodka.wolves.block.entity;


// Referenced classes of package net.minecraft.src:
//            TileEntity, InventoryBase, ItemInstance, NBTTagCompound, 
//            ListTag, mod_FCBetterThanWolves, FCBlockCauldron, FCUtilsInventory, 
//            World, EntityPlayer, FCCraftingManagerCauldron, Item, 
//            Block, FCCraftingManagerCauldronStoked, BlockFire, FCUtilsMisc, 
//            FurnaceRecipes, FoodBase

import net.kozibrodka.wolves.block.CauldronBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.recipe.CauldronCraftingManager;
import net.kozibrodka.wolves.recipe.StokedCauldronCraftingManager;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.SmeltingRecipeManager;

public class CauldronBlockEntity extends BlockEntity
    implements Inventory
{

    public CauldronBlockEntity()
    {
        cauldronContents = new ItemStack[27];
        m_iCauldronCookCounter = 0;
        m_iRenderCooldownCounter = 0;
        m_bContainsValidIngrediantsForState = false;
        m_bForceValidateOnUpdate = false;
        m_iFireFactor = 0;
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        cauldronContents = new ItemStack[size()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            NbtCompound nbttagcompound1 = (NbtCompound)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < cauldronContents.length)
            {
                cauldronContents[j] = new ItemStack(nbttagcompound1);
            }
        }

        if(nbttagcompound.contains("m_iCauldronCookCounter"))
        {
            m_iCauldronCookCounter = nbttagcompound.getInt("m_iCauldronCookCounter");
        }
        if(nbttagcompound.contains("m_iRenderCooldownCounter"))
        {
            m_iRenderCooldownCounter = nbttagcompound.getInt("m_iRenderCooldownCounter");
        }
        if(nbttagcompound.contains("m_bContainsValidIngrediantsForState"))
        {
            m_bContainsValidIngrediantsForState = nbttagcompound.getBoolean("m_bContainsValidIngrediantsForState");
        } else
        {
            m_bForceValidateOnUpdate = true;
        }
        if(nbttagcompound.contains("m_iFireFactor"))
        {
            m_iFireFactor = nbttagcompound.getInt("m_iFireFactor");
        } else
        {
            m_bForceValidateOnUpdate = true;
        }
    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for(int i = 0; i < cauldronContents.length; i++)
        {
            if(cauldronContents[i] != null)
            {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte)i);
                cauldronContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putInt("m_iCauldronCookCounter", m_iCauldronCookCounter);
        nbttagcompound.putInt("m_iRenderCooldownCounter", m_iRenderCooldownCounter);
    }

    public void tick()
    {
        if(world.isRemote){
            return;
        }
        int iFireUnderState = ((CauldronBlock) BlockListener.cauldron).GetFireUnderState(world, x, y, z);
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

    public int size()
    {
        return 27;
    }

    public ItemStack getStack(int iSlot)
    {
        return cauldronContents[iSlot];
    }

    public ItemStack removeStack(int iSlot, int iAmount)
    {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void setStack(int iSlot, ItemStack ItemInstance)
    {
        cauldronContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxCountPerStack())
        {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public String getName()
    {
        return "Cauldron";
    }

    public int getMaxCountPerStack()
    {
        return 64;
    }

    public void markDirty() {
        if (world == null) {
            return;
        }
        int iFireUnderState = ((CauldronBlock)BlockListener.cauldron).GetFireUnderState(world, x, y, z);
        ValidateContentsForState(iFireUnderState);
    }

    public boolean canPlayerUse(PlayerEntity entityPlayer)
    {
        if(world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return entityPlayer.getSquaredDistance((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
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
            if(InventoryHandler.getFirstOccupiedStackOfItem(this, Block.TNT.id) >= 0)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(InventoryHandler.getFirstOccupiedStackOfItem(this, Item.GUNPOWDER.id) >= 0)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.hellfireDust.id) >= 0)
            {
                m_bContainsValidIngrediantsForState = true;
            } else
            if(StokedCauldronCraftingManager.getInstance().getCraftingResult(this) != null)
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
            int iidToTest = Block.FIRE.id;
            if(iFireUnderState == 2)
            {
                iidToTest = BlockListener.stokedFire.id;
            }
            int tempY = y - 1;
            for(int tempX = x - 1; tempX <= x + 1; tempX++)
            {
                for(int tempZ = z - 1; tempZ <= z + 1; tempZ++)
                {
                    if((tempX != x || tempZ != z) && world.getBlockId(tempX, tempY, tempZ) == iidToTest)
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
                    ItemStack cookedStack = CauldronCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
                    if(!$assertionsDisabled && cookedStack == null)
                    {
                        throw new AssertionError();
                    }
                    if(!InventoryHandler.addItemInstanceToInventory(this, cookedStack))
                    {
                        UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, cookedStack);
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
                if(InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.hellfireDust.id) >= 0 || InventoryHandler.getFirstOccupiedStackOfItem(this, Block.TNT.id) >= 0 || InventoryHandler.getFirstOccupiedStackOfItem(this, Item.GUNPOWDER.id) >= 0)
                {
                    BlowUpCauldron();
                } else
                if(StokedCauldronCraftingManager.getInstance().getCraftingResult(this) != null)
                {
                    ItemStack cookedStack = StokedCauldronCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
                    if(!$assertionsDisabled && cookedStack == null)
                    {
                        throw new AssertionError();
                    }
                    if(!InventoryHandler.addItemInstanceToInventory(this, cookedStack))
                    {
                        UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, cookedStack);
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
            ItemStack tempStack = SmeltingRecipeManager.getInstance().craft(cauldronContents[iUncookedFoodIndex].getItem().id);
            ItemStack cookedStack = tempStack.copy();
            removeStack(iUncookedFoodIndex, 1);
            if(!InventoryHandler.addItemInstanceToInventory(this, cookedStack))
            {
                UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, cookedStack);
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
            Item tempItem = cauldronContents[tempIndex].getItem();
            if(tempItem != null && (tempItem instanceof FoodItem) && SmeltingRecipeManager.getInstance().craft(tempItem.id) != null)
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
            Item tempItem = cauldronContents[tempIndex].getItem();
            if(tempItem != null && (tempItem instanceof FoodItem))
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
            Item tempItem = cauldronContents[tempIndex].getItem();
            if(tempItem != null && (tempItem instanceof FoodItem))
            {
                int stackSize = cauldronContents[tempIndex].count;
                cauldronContents[tempIndex] = null;
                ItemStack spoiledStack = new ItemStack(ItemListener.foulFood, stackSize);
                setStack(tempIndex, spoiledStack);
                bFoodDestroyed = true;
            }
        }

        return bFoodDestroyed;
    }

    private void BlowUpCauldron()
    {
        int iHellfireCount = InventoryHandler.itemCountInInventory(this, ItemListener.hellfireDust.id, -1);
        float fExplosionSize = ((float)iHellfireCount * 10F) / 64F;
        fExplosionSize += ((float) InventoryHandler.itemCountInInventory(this, Item.GUNPOWDER.id, -1) * 10F) / 64F;
        int iTNTCount = InventoryHandler.itemCountInInventory(this, Block.TNT.id, -1);
        if(iTNTCount > 0)
        {
            if(fExplosionSize < 4F)
            {
                fExplosionSize = 4F;
            }
            fExplosionSize += InventoryHandler.itemCountInInventory(this, Block.TNT.id, -1);
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
        world.setBlock(x, y, z, 0);
        world.createExplosion(null, x, y, z, fExplosionSize);
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
    private ItemStack cauldronContents[];
    public int m_iCauldronCookCounter;
    private int m_iRenderCooldownCounter;
    private boolean m_bContainsValidIngrediantsForState;
    private boolean m_bForceValidateOnUpdate;
    private int m_iFireFactor;
    static final boolean $assertionsDisabled; /* synthetic field */

    static 
    {
        $assertionsDisabled = !(CauldronBlockEntity.class).desiredAssertionStatus();
    }
}
