package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.recipe.AnvilCraftingManager;
import net.minecraft.container.ContainerBase;
import net.minecraft.container.slot.CraftingResult;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Chest;
import net.minecraft.inventory.Crafting;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.recipe.RecipeRegistry;


public class AnvilContainer extends ContainerBase
{

    public AnvilContainer(PlayerInventory inventoryplayer, Level world, int x, int y, int z) {
        craftMatrix = new Crafting(this, 5, 5);
        craftResult = new Chest();
        localWorld = world;
        anvilX = x;
        anvilY = y;
        anvilZ = z;
        addSlot(new CraftingResult(inventoryplayer.player, craftMatrix, craftResult, 0, 142, 71));
        for(int l = 0; l < 5; l++) {
            for(int k1 = 0; k1 < 5; k1++) {
                addSlot(new Slot(craftMatrix, k1 + l * 5, 12 + k1 * 18, 17 + l * 18));
            }
        }
        for(int i1 = 0; i1 < 3; i1++) {
            for(int l1 = 0; l1 < 9; l1++) {
                addSlot(new Slot(inventoryplayer, l1 + i1 * 9 + 9, 8 + l1 * 18, 120 + i1 * 18));
            }
        }
        for(int j1 = 0; j1 < 9; j1++) {
            addSlot(new Slot(inventoryplayer, j1, 8 + j1 * 18, 178));
        }
        onContentsChanged(craftMatrix);
    }

    public void onContentsChanged(InventoryBase iinventory) {
        ItemInstance craftedStack = RecipeRegistry.getInstance().getCraftingOutput(craftMatrix);
        if(craftedStack == null) {
            craftedStack = AnvilCraftingManager.getInstance().findMatchingRecipe(craftMatrix);
        }
        craftResult.setInventoryItem(0, craftedStack);
        //TODO: in my opinion anvil should not do regular recipes
//        ItemInstance craftedStac2k = AnvilCraftingManager.getInstance().findMatchingRecipe(craftMatrix);
//        craftResult.setInventoryItem(0, craftedStac2k);
    }

    public void onClosed(PlayerBase entityplayer) {
        super.onClosed(entityplayer);
        if(localWorld.isServerSide) {
            return;
        }
        for(int i = 0; i < 25; i++) {
            ItemInstance ItemInstance = craftMatrix.getInventoryItem(i);
            if(ItemInstance != null) {
                entityplayer.dropItem(ItemInstance);
            }
        }

    }

    public boolean canUse(PlayerBase entityplayer) {
//        System.out.println(anvilX + "  " + anvilY + " " + anvilZ);
        if(localWorld.getTileId(anvilX, anvilY, anvilZ) != BlockListener.anvil.id) {
            return false;
        } else {
            return entityplayer.squaredDistanceTo((double) anvilX + 0.5D, (double) anvilY + 0.5D, (double) anvilZ + 0.5D) <= 64D;
        }
//        return true;
    }

    public ItemInstance transferSlot(int i) {
        ItemInstance ItemInstance = null;
        Slot slot = (Slot)slots.get(i);
        if(slot != null && slot.hasItem()) {
            ItemInstance ItemInstance1 = slot.getItem();
            ItemInstance = ItemInstance1.copy();
            if(i == 0) {
                insertItem(ItemInstance1, 10, 46, true);
            } else
            if(i >= 10 && i < 37) {
                insertItem(ItemInstance1, 37, 46, false);
            } else
            if(i >= 37 && i < 46) {
                insertItem(ItemInstance1, 10, 37, false);
            } else {
                insertItem(ItemInstance1, 10, 46, false);
            }
            if(ItemInstance1.count == 0) {
                slot.setStack(null);
            } else {
                slot.markDirty();
            }
            if(ItemInstance1.count != ItemInstance.count) {
                slot.onCrafted(ItemInstance1);
            } else {
                return null;
            }
        }
        return ItemInstance;
    }

    public Crafting craftMatrix;
    public InventoryBase craftResult;
    private Level localWorld;
    private int anvilX;
    private int anvilY;
    private int anvilZ;
}
