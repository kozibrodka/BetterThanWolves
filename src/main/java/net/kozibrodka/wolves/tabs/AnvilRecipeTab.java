package net.kozibrodka.wolves.tabs;

import net.glasslauncher.hmifabric.Utils;
import net.glasslauncher.hmifabric.tabs.TabWithTexture;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.gui.FCGuiCraftingAnvil;
import net.kozibrodka.wolves.recipe.AnvilShapelessRecipe;
import net.kozibrodka.wolves.recipe.FCCraftingManagerAnvil;
import net.kozibrodka.wolves.utils.FCRecipeAnvil;
import net.minecraft.block.BlockBase;
import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.gui.screen.container.Crafting;
import net.minecraft.client.gui.screen.container.PlayerInventory;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.ModID;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnvilRecipeTab extends TabWithTexture {

    protected List<Object> recipesComplete;
    protected List<Object> recipes;
    private final BlockBase tabBlock;
    private boolean isVanillaWorkbench = false; //THIS IS LAZY
    public ArrayList<Class<? extends ContainerBase>> guiCraftingStations = new ArrayList<>();
    public int recipeIndex;

    public AnvilRecipeTab(ModID tabCreator) {
        this(tabCreator, new ArrayList<Object>(FCCraftingManagerAnvil.getInstance().getRecipeList()), mod_FCBetterThanWolves.fcAnvil);
        isVanillaWorkbench = true;
        guiCraftingStations.add(Crafting.class);
    }

    public AnvilRecipeTab(ModID tabCreator, List<Object> recipesComplete, BlockBase tabBlock) {
        this(tabCreator, 10, recipesComplete, tabBlock, "/gui/crafting.png", 118, 56, 28, 15, 56, 46, 3);
        slots[0] = new Integer[]{96, 23};
    }

    public AnvilRecipeTab(ModID tabCreator, int slotsPerRecipe, List<Object> recipesComplete, BlockBase tabBlock, String texturePath, int width, int height, int textureX, int textureY, int buttonX, int buttonY, int slotsWidth) {
        super(tabCreator, slotsPerRecipe, texturePath, width, height, 3, 4, textureX, textureY, buttonX, buttonY);
        this.recipesComplete = recipesComplete;
        this.tabBlock = tabBlock;
        recipes = recipesComplete;
        int i = 1;
        for (int l = 0; l < 3; l++) {
            for (int i1 = 0; i1 < slotsWidth; i1++) {
                slots[i++] = new Integer[]{2 + i1 * 18, 5 + l * 18};
            }
        }
        equivalentCraftingStations.add(getTabItem());
    }

    @Override
    public void draw(int x, int y, int recipeOnThisPageIndex, int cursorX, int cursorY) {
        super.draw(x, y, recipeOnThisPageIndex, cursorX, cursorY);
        if (recipeIndex < recipes.size() && recipes.get(recipeIndex) instanceof AnvilShapelessRecipe) {
            Utils.bindTexture("/assets/hmifabric/textures/shapeless_icon.png");
            double size = 8;
            x += 80;
            y += 16;
            Tessellator tess = Tessellator.INSTANCE;
            tess.start();
            tess.vertex(x, y + size, 0, 0, 1);
            tess.vertex(x + size, y + size, 0, 1, 1);
            tess.vertex(x + size, y, 0, 1, 0);
            tess.vertex(x, y, 0, 0, 0);
            tess.draw();
        }
    }

    @Override
    public Class<? extends ContainerBase> getGuiClass() {
        return FCGuiCraftingAnvil.class;
    }

    @Override
    public ItemInstance[][] getItems(int index, ItemInstance filter) {
        recipeIndex = index;
        ItemInstance[][] items = new ItemInstance[recipesPerPage][];
        for (int j = 0; j < recipesPerPage; j++) {
            items[j] = new ItemInstance[slots.length];
            int k = index + j;
            if (k < recipes.size()) {
                try {
                    Object recipeObj = recipes.get(k);
                    if (recipeObj instanceof StationRecipe) {
                        StationRecipe recipe = (StationRecipe) recipes.get(k);
                        ItemInstance[] list = recipe.getIngredients();
                        ItemInstance[] outputArray = recipe.getOutputs();
                        System.arraycopy(outputArray, 0, items[j], 0, outputArray.length);
                        for (int j1 = 0; j1 < list.length; j1++) {
                            ItemInstance item = list[j1];
                            items[j][j1 + 1] = item;
                            if (item != null && item.getDamage() == -1) {
                                if (item.usesMeta()) {
                                    if (filter != null && item.itemId == filter.itemId) {
                                        items[j][j1 + 1] = new ItemInstance(item.getType(), 0, filter.getDamage());
                                    } else {
                                        items[j][j1 + 1] = new ItemInstance(item.getType());
                                    }
                                } else if (filter != null && item.itemId == filter.itemId) {
                                    items[j][j1 + 1] = new ItemInstance(item.getType(), 0, filter.getDamage());
                                }
                            }
                        }

                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            if (items[j][0] == null && recipesOnThisPage > j) {
                recipesOnThisPage = j;
                redrawSlots = true;
                break;
            }
            if (items[j][0] != null && recipesOnThisPage == j) {
                recipesOnThisPage = j + 1;
                redrawSlots = true;
            }
        }
        return items;
    }


    @Override
    public void updateRecipes(ItemInstance filter, Boolean getUses) {
        List<Object> arraylist = new ArrayList<>();
        if (filter == null) {
            recipes = recipesComplete;
        } else {
            for (Object o : recipesComplete) {
                if (o instanceof FCRecipeAnvil) {
                    StationRecipe recipe = (StationRecipe) o;
                    if (!getUses) {
                        if (Arrays.stream(recipe.getOutputs()).anyMatch(itemInstance -> filter.itemId == itemInstance.itemId && (itemInstance.getDamage() == filter.getDamage() || itemInstance.getDamage() < 0 || !itemInstance.usesMeta()))) {
                            arraylist.add(o);
                        }
                    } else {
                        try {
                            ItemInstance[] aitemstack = recipe.getIngredients();
                            for (ItemInstance itemstack1 : aitemstack) {
                                if (itemstack1 == null || filter.itemId != itemstack1.itemId || (itemstack1.usesMeta() && itemstack1.getDamage() != filter.getDamage()) && itemstack1.getDamage() >= 0) {
                                    continue;
                                }
                                arraylist.add(o);
                                break;
                            }

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
            recipes = arraylist;
        }
        size = recipes.size();
        super.updateRecipes(filter, getUses);
        size = recipes.size();
    }

    @Override
    public ItemInstance getTabItem() {
        return new ItemInstance(tabBlock);
    }

    @Override
    public Boolean drawSetupRecipeButton(ScreenBase parent, ItemInstance[] recipeItems) {
        for (Class<? extends ContainerBase> gui : guiCraftingStations) {
            if (gui.isInstance(parent)) return true;
        }
        if (isVanillaWorkbench && (parent == null || isInv(parent))) {
            for (int i = 3; i < 10; i++) {
                if (i != 4 && i != 5 && recipeItems[i] != null)
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean[] itemsInInventory(ScreenBase parent, ItemInstance[] recipeItems) {
        Boolean[] itemsInInv = new Boolean[slots.length - 1];
        List<Object> list;
        if (parent instanceof ContainerBase)
            //noinspection unchecked
            list = ((ContainerBase) parent).container.slots;
        else
            //noinspection unchecked
            list = Utils.getMC().player.container.slots;
        ItemInstance[] aslot = new ItemInstance[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (((Slot) list.get(i)).hasItem())
                aslot[i] = ((Slot) list.get(i)).getItem().copy();
        }

        aslot[0] = null;
        recipe:
        for (int i = 1; i < recipeItems.length; i++) {
            ItemInstance item = recipeItems[i];
            if (item == null) {
                itemsInInv[i - 1] = true;
                continue;
            }

            for (ItemInstance slot : aslot) {
                if (slot != null && slot.count > 0 && slot.itemId == item.itemId && (slot.getDamage() == item.getDamage() || item.getDamage() < 0 || !item.usesMeta())) {
                    slot.count -= 1;
                    itemsInInv[i - 1] = true;
                    continue recipe;
                }
            }
            itemsInInv[i - 1] = false;
        }
        return itemsInInv;
    }

    private int recipeStackSize(List<Object> list, ItemInstance[] recipeItems) {

        int[] itemStackSize = new int[recipeItems.length - 1];

        for (int i = 1; i < recipeItems.length; i++) {
            ItemInstance[] aslot = new ItemInstance[list.size()];
            for (int k = 0; k < list.size(); k++) {
                if (((Slot) list.get(k)).hasItem())
                    aslot[k] = ((Slot) list.get(k)).getItem().copy();
            }
            aslot[0] = null;

            ItemInstance item = recipeItems[i];
            itemStackSize[i - 1] = 0;
            if (item == null) {
                itemStackSize[i - 1] = -1;
                continue;
            }
            int count = 0;
            for (ItemInstance slot : aslot) {
                if (slot != null && slot.count > 0 && slot.itemId == item.itemId && (slot.getDamage() == item.getDamage() || item.getDamage() < 0 || !item.usesMeta())) {
                    count += slot.count;
                    slot.count = 0;
                }
            }
            int prevEqualItemCount = 1;
            for (int j = 1; j < i; j++) {
                if (recipeItems[j] != null && recipeItems[j].isDamageAndIDIdentical(item)) {
                    prevEqualItemCount++;
                }
            }
            for (int j = 1; j < recipeItems.length; j++) {
                if (recipeItems[j] != null && recipeItems[j].isDamageAndIDIdentical(item)) {
                    itemStackSize[j - 1] = count / prevEqualItemCount;
                }
            }
        }
        int finalItemStackSize = -1;
        for (int i = 0; i < itemStackSize.length; i++) {
            ItemInstance item = recipeItems[i + 1];
            if (itemStackSize[i] == -1 || item.getMaxStackSize() == 1) continue;
            if (finalItemStackSize == -1 || itemStackSize[i] < finalItemStackSize || finalItemStackSize > item.getMaxStackSize()) {
                finalItemStackSize = Math.min(itemStackSize[i], item.getMaxStackSize());
            }
        }
        if (finalItemStackSize > 0) return finalItemStackSize;
        return 1;
    }

    @Override
    public void setupRecipe(ScreenBase parent, ItemInstance[] recipeItems) {
        if (parent == null) {
            Utils.getMC().method_2134();
            ScreenScaler scaledresolution = new ScreenScaler(Utils.getMC().options, Utils.getMC().actualWidth, Utils.getMC().actualHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            parent = new PlayerInventory(Utils.getMC().player);
            Utils.getMC().currentScreen = parent;
            parent.init(Utils.getMC(), i, j);
            Utils.getMC().skipGameRender = false;
        }
        ContainerBase container = ((ContainerBase) parent);
        //noinspection unchecked
        List<Object> inventorySlots = container.container.slots;

        int recipeStackSize = 1;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            recipeStackSize = recipeStackSize(inventorySlots, recipeItems);
        }

        this.player = Utils.getMC().player;
        this.inv = Utils.getMC().interactionManager;
        this.windowId = container.container.currentContainerId;
        for (int recipeSlotIndex = 1; recipeSlotIndex < recipeItems.length; recipeSlotIndex++) {
            if (isInv(parent) && recipeSlotIndex > 5)
                break;
            int slotid = recipeSlotIndex;
            if (isInv(parent) && recipeSlotIndex > 3) {
                slotid--;
            }
            Slot recipeSlot = (Slot) inventorySlots.get(slotid);
            //clear recipe slot
            if (recipeSlot.hasItem()) {
                this.clickSlot(slotid, true, true);

                if (recipeSlot.hasItem()) {
                    this.clickSlot(slotid, true, false);
                    if (player.inventory.getCursorItem() != null) {
                        for (int j = slotid + 1; j < inventorySlots.size(); j++) {
                            Slot slot = (Slot) inventorySlots.get(j);
                            if (!slot.hasItem()) {
                                this.clickSlot(j, true, false);
                                break;
                            }
                        }
                        if (player.inventory.getCursorItem() != null) {
                            this.clickSlot(-999, true, false);
                        }
                    }
                }
            }

            //if recipe slot should be empty, continue
            ItemInstance item = recipeItems[recipeSlotIndex];
            if (item == null) {
                continue;
            }

            //locate correct item and put in recipe slot
            while (!recipeSlot.hasItem() || (recipeSlot.getItem().count < recipeStackSize && recipeSlot.getItem().getMaxStackSize() > 1))
                for (int inventorySlotIndex = recipeSlotIndex + 1; inventorySlotIndex < inventorySlots.size(); inventorySlotIndex++) {
                    Slot inventorySlot = (Slot) inventorySlots.get(inventorySlotIndex);
                    if (inventorySlot.hasItem() && inventorySlot.getItem().itemId == item.itemId && (inventorySlot.getItem().getDamage() == item.getDamage() || item.getDamage() < 0 || !item.usesMeta())) {
                        this.clickSlot(inventorySlotIndex, true, false);
                        if (isInv(parent) && recipeSlotIndex > 3) {
                            this.clickSlot(recipeSlotIndex - 1, false, false);
                        } else
                            this.clickSlot(recipeSlotIndex, false, false);
                        this.clickSlot(inventorySlotIndex, true, false);
                        break;
                    }
                }

        }

    }

    BaseClientInteractionManager inv;
    AbstractClientPlayer player;
    int windowId;

    void clickSlot(int slotIndex, boolean leftClick, boolean shiftClick) {
        inv.clickSlot(windowId, slotIndex, leftClick ? 0 : 1, shiftClick, player);
    }

    boolean isInv(ScreenBase screen) {
        return screen instanceof InventoryBase;
    }

}
