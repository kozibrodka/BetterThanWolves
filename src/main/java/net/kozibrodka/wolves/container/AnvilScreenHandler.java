package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.events.BlockListener;
//import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.recipe.AnvilCraftingManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;


public class AnvilScreenHandler extends ScreenHandler
{

    public AnvilScreenHandler(PlayerInventory inventoryplayer, World world, int x, int y, int z) {
        craftMatrix = new CraftingInventory(this, 5, 5);
        craftResult = new CraftingResultInventory();
        localWorld = world;
        anvilX = x;
        anvilY = y;
        anvilZ = z;
        addSlot(new CraftingResultSlot(inventoryplayer.player, craftMatrix, craftResult, 0, 142, 71));
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
        onSlotUpdate(craftMatrix);
    }

    @Override
    public void onSlotUpdate(Inventory iinventory) {

        if (ConfigListener.wolvesGlass.gameplay_settings.anvilVanillaRecipes) {
            ItemStack craftedStack = CraftingRecipeManager.getInstance().craft(craftMatrix);
            if (craftedStack == null) {
                craftedStack = AnvilCraftingManager.getInstance().findMatchingRecipe(craftMatrix);
            }
            craftResult.setStack(0, craftedStack);
        } else {
            ItemStack craftedStac2k = AnvilCraftingManager.getInstance().findMatchingRecipe(craftMatrix);
            craftResult.setStack(0, craftedStac2k);
        }
    }

    public void onClosed(PlayerEntity entityplayer) {
        super.onClosed(entityplayer);
        if(localWorld.isRemote) {
            return;
        }
        for(int i = 0; i < 25; i++) {
            ItemStack ItemInstance = craftMatrix.getStack(i);
            if(ItemInstance != null) {
                entityplayer.dropItem(ItemInstance);
            }
        }

    }

    public boolean canUse(PlayerEntity entityplayer) {
//        System.out.println(anvilX + "  " + anvilY + " " + anvilZ);
        if(localWorld.getBlockId(anvilX, anvilY, anvilZ) != BlockListener.anvil.id) {
            return false;
        } else {
            return entityplayer.getSquaredDistance((double) anvilX + 0.5D, (double) anvilY + 0.5D, (double) anvilZ + 0.5D) <= 64D;
        }
//        return true;
    }

    public ItemStack getStackInSlot(int i) {
        ItemStack ItemInstance = null;
        Slot slot = (Slot)slots.get(i);
        if(slot != null && slot.hasStack()) {
            ItemStack ItemInstance1 = slot.getStack();
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
                slot.onTakeItem(ItemInstance1);
            } else {
                return null;
            }
        }
        return ItemInstance;
    }

    public CraftingInventory craftMatrix;
    public Inventory craftResult;
    private World localWorld;
    private int anvilX;
    private int anvilY;
    private int anvilZ;
}
