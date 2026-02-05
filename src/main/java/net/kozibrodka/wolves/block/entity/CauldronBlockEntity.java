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
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class CauldronBlockEntity extends BlockEntity implements Inventory {

    public CauldronBlockEntity() {
        cauldronContents = new ItemStack[27];
        cauldronCookCounter = 0;
        renderCooldownCounter = 0;
        containsValidIngredientsForState = false;
        forceValidateOnUpdate = false;
        fireFactor = 0;
    }

    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        cauldronContents = new ItemStack[size()];
        for (int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound nbttagcompound1 = (NbtCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < cauldronContents.length) {
                cauldronContents[j] = new ItemStack(nbttagcompound1);
            }
        }

        if (nbttagcompound.contains("m_iCauldronCookCounter")) {
            cauldronCookCounter = nbttagcompound.getInt("m_iCauldronCookCounter");
        }
        if (nbttagcompound.contains("m_iRenderCooldownCounter")) {
            renderCooldownCounter = nbttagcompound.getInt("m_iRenderCooldownCounter");
        }
        if (nbttagcompound.contains("m_bContainsValidIngrediantsForState")) {
            containsValidIngredientsForState = nbttagcompound.getBoolean("m_bContainsValidIngrediantsForState");
        } else {
            forceValidateOnUpdate = true;
        }
        if (nbttagcompound.contains("m_iFireFactor")) {
            fireFactor = nbttagcompound.getInt("m_iFireFactor");
        } else {
            forceValidateOnUpdate = true;
        }
    }

    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for (int i = 0; i < cauldronContents.length; i++) {
            if (cauldronContents[i] != null) {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte) i);
                cauldronContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putInt("m_iCauldronCookCounter", cauldronCookCounter);
        nbttagcompound.putInt("m_iRenderCooldownCounter", renderCooldownCounter);
    }

    public void tick() {
        if (world.isRemote) {
            return;
        }
        int meta = world.getBlockMeta(x, y, z);
        if (meta > 0) {
            dropContents(meta);
            return;
        }
        int fireHeat = ((CauldronBlock) BlockListener.cauldron).getFireHeat(world, x, y, z);
        if (fireHeat > 0) {
            if (forceValidateOnUpdate) {
                ValidateContentsForState(fireHeat);
                forceValidateOnUpdate = false;
            }
            ValidateFireFactor(fireHeat);
            if (fireHeat == 2) {
                if (renderCooldownCounter <= 0) {
                    cauldronCookCounter = 0;
                }
                renderCooldownCounter = 20;
                PerformStokedFireUpdate();
            } else if (renderCooldownCounter > 0) {
                renderCooldownCounter--;
                if (renderCooldownCounter <= 0) {
                    cauldronCookCounter = 0;
                }
            } else {
                PerformNormalFireUpdate();
            }
        } else {
            cauldronCookCounter = 0;
        }
    }

    private void dropContents(int meta) {
        switch (meta) {
            case 2:
                InventoryHandler.ejectInventoryContents(world, x, y, z - 1, this);
                break;
            case 3:
                InventoryHandler.ejectInventoryContents(world, x, y, z + 1, this);
                break;
            case 4:
                InventoryHandler.ejectInventoryContents(world, x - 1, y, z, this);
                break;
            case 5:
                InventoryHandler.ejectInventoryContents(world, x + 1, y, z, this);
                break;
        }
    }

    public int size() {
        return 27;
    }

    public ItemStack getStack(int iSlot) {
        return cauldronContents[iSlot];
    }

    public ItemStack removeStack(int iSlot, int iAmount) {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void setStack(int iSlot, ItemStack ItemInstance) {
        cauldronContents[iSlot] = ItemInstance;
        if (ItemInstance != null && ItemInstance.count > getMaxCountPerStack()) {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public String getName() {
        return "Cauldron";
    }

    public int getMaxCountPerStack() {
        return 64;
    }

    public void markDirty() {
        if (world == null) {
            return;
        }
        int iFireUnderState = ((CauldronBlock) BlockListener.cauldron).getFireHeat(world, x, y, z);
        ValidateContentsForState(iFireUnderState);
    }

    public boolean canPlayerUse(PlayerEntity entityPlayer) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return entityPlayer.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    public void NotifyOfChangeInFireUnder(int fireHeat) {
        ValidateContentsForState(fireHeat);
        ValidateFireFactor(fireHeat);
    }

    public void ValidateContentsForState(int iFireUnderState) {
        containsValidIngredientsForState = false;
        if (iFireUnderState == 1) {
            if (CauldronCraftingManager.getInstance().getCraftingResult(this) != null) {
                containsValidIngredientsForState = true;
            } else if (GetUncookedItemInventoryIndex() >= 0) {
                containsValidIngredientsForState = true;
            } else if (InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(ItemListener.dung)) >= 0 && ContainsFood()) {
                containsValidIngredientsForState = true;
            }
        } else if (iFireUnderState == 2) {
            if (InventoryHandler.getFirstOccupiedStackOfItem(this, BlockRegistry.INSTANCE.getId(Block.TNT)) >= 0) {
                containsValidIngredientsForState = true;
            } else if (InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(Item.GUNPOWDER)) >= 0) {
                containsValidIngredientsForState = true;
            } else if (InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(ItemListener.hellfireDust)) >= 0) {
                containsValidIngredientsForState = true;
            } else if (StokedCauldronCraftingManager.getInstance().getCraftingResult(this) != null) {
                containsValidIngredientsForState = true;
            }
        }
    }

    public void ValidateFireFactor(int iFireUnderState) {
        int iFireFactor = 0;
        if (iFireUnderState > 0) {
            iFireFactor = 5;
            int iidToTest = Block.FIRE.id;
            if (iFireUnderState == 2) {
                iidToTest = BlockListener.stokedFire.id;
            }
            int tempY = y - 1;
            for (int tempX = x - 1; tempX <= x + 1; tempX++) {
                for (int tempZ = z - 1; tempZ <= z + 1; tempZ++) {
                    if ((tempX != x || tempZ != z) && world.getBlockId(tempX, tempY, tempZ) == iidToTest) {
                        iFireFactor++;
                    }
                }

            }

        }
        fireFactor = iFireFactor;
    }

    private void PerformNormalFireUpdate() {
        if (containsValidIngredientsForState) {
            cauldronCookCounter += fireFactor;
            if (cauldronCookCounter >= 1950) {
                int iDungIndex = InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(ItemListener.dung));
                if (iDungIndex >= 0 && DestroyAllFoodInInventory()) {
                    cauldronCookCounter = 0;
                    return;
                }
                if (CauldronCraftingManager.getInstance().getCraftingResult(this) != null) {
                    ItemStack cookedStack = CauldronCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
                    if (!$assertionsDisabled && cookedStack == null) {
                        throw new AssertionError();
                    }
                    if (!InventoryHandler.addItemInstanceToInventory(this, cookedStack)) {
                        UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, cookedStack);
                    }
                } else {
                    AttemptToCookFood();
                }
                cauldronCookCounter = 0;
            }
        } else {
            cauldronCookCounter = 0;
        }
    }

    private void PerformStokedFireUpdate() {
        if (containsValidIngredientsForState) {
            cauldronCookCounter += fireFactor;
            if (cauldronCookCounter >= 1950) {
                if (InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(ItemListener.hellfireDust)) >= 0 || InventoryHandler.getFirstOccupiedStackOfItem(this, BlockRegistry.INSTANCE.getId(Block.TNT)) >= 0 || InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(Item.GUNPOWDER)) >= 0) {
                    BlowUpCauldron();
                } else if (StokedCauldronCraftingManager.getInstance().getCraftingResult(this) != null) {
                    ItemStack cookedStack = StokedCauldronCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
                    if (!$assertionsDisabled && cookedStack == null) {
                        throw new AssertionError();
                    }
                    if (!InventoryHandler.addItemInstanceToInventory(this, cookedStack)) {
                        UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, cookedStack);
                    }
                }
                cauldronCookCounter = 0;
            }
        } else {
            cauldronCookCounter = 0;
        }
    }

    private void AttemptToCookFood() {
        int iUncookedFoodIndex = GetUncookedItemInventoryIndex();
        if (iUncookedFoodIndex >= 0) {
            ItemStack tempStack = SmeltingRecipeManager.getInstance().craft(cauldronContents[iUncookedFoodIndex].getItem().id);
            ItemStack cookedStack = tempStack.copy();
            removeStack(iUncookedFoodIndex, 1);
            if (!InventoryHandler.addItemInstanceToInventory(this, cookedStack)) {
                UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, cookedStack);
            }
        }
    }

    public int getCookProgressScaled(int iScale) {
        return (cauldronCookCounter * iScale) / 1950;
    }

    public boolean IsCooking() {
        return cauldronCookCounter > 0;
    }

    public int GetUncookedItemInventoryIndex() {
        for (int tempIndex = 0; tempIndex < 27; tempIndex++) {
            if (cauldronContents[tempIndex] == null) {
                continue;
            }
            Item tempItem = cauldronContents[tempIndex].getItem();
            if (tempItem != null && (tempItem instanceof FoodItem) && SmeltingRecipeManager.getInstance().craft(tempItem.id) != null) {
                return tempIndex;
            }
        }

        return -1;
    }

    private boolean ContainsFood() {
        for (int tempIndex = 0; tempIndex < 27; tempIndex++) {
            if (cauldronContents[tempIndex] == null) {
                continue;
            }
            Item tempItem = cauldronContents[tempIndex].getItem();
            if (tempItem != null && (tempItem instanceof FoodItem)) {
                return true;
            }
        }

        return false;
    }

    private boolean DestroyAllFoodInInventory() {
        boolean bFoodDestroyed = false;
        for (int tempIndex = 0; tempIndex < 27; tempIndex++) {
            if (cauldronContents[tempIndex] == null) {
                continue;
            }
            Item tempItem = cauldronContents[tempIndex].getItem();
            if (tempItem != null && (tempItem instanceof FoodItem)) {
                int stackSize = cauldronContents[tempIndex].count;
                cauldronContents[tempIndex] = null;
                ItemStack spoiledStack = new ItemStack(ItemListener.foulFood, stackSize);
                setStack(tempIndex, spoiledStack);
                bFoodDestroyed = true;
            }
        }

        return bFoodDestroyed;
    }

    private void BlowUpCauldron() {
        int iHellfireCount = InventoryHandler.itemCountInInventory(this, ItemRegistry.INSTANCE.getId(ItemListener.hellfireDust), -1);
        float fExplosionSize = ((float) iHellfireCount * 10F) / 64F;
        fExplosionSize += ((float) InventoryHandler.itemCountInInventory(this, ItemRegistry.INSTANCE.getId(Item.GUNPOWDER), -1) * 10F) / 64F;
        int iTNTCount = InventoryHandler.itemCountInInventory(this, BlockRegistry.INSTANCE.getId(Block.TNT), -1);
        if (iTNTCount > 0) {
            if (fExplosionSize < 4F) {
                fExplosionSize = 4F;
            }
            fExplosionSize += InventoryHandler.itemCountInInventory(this, BlockRegistry.INSTANCE.getId(Block.TNT), -1);
        }
        if (fExplosionSize < 2.0F) {
            fExplosionSize = 2.0F;
        } else if (fExplosionSize > 10F) {
            fExplosionSize = 10F;
        }
        InventoryHandler.clearInventoryContents(this);
        world.setBlock(x, y, z, 0);
        world.createExplosion(null, x, y, z, fExplosionSize);
    }

    static Class _mthclass$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
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
    private ItemStack[] cauldronContents;
    public int cauldronCookCounter;
    private int renderCooldownCounter;
    private boolean containsValidIngredientsForState;
    private boolean forceValidateOnUpdate;
    private int fireFactor;
    static final boolean $assertionsDisabled; /* synthetic field */

    static {
        $assertionsDisabled = !(CauldronBlockEntity.class).desiredAssertionStatus();
    }
}
