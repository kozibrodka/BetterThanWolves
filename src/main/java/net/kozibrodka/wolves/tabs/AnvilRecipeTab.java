package net.kozibrodka.wolves.tabs;

import net.glasslauncher.hmifabric.Utils;
import net.glasslauncher.hmifabric.tabs.TabWithTexture;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.gui.AnvilScreen;
import net.kozibrodka.wolves.recipe.AnvilShapedRecipe;
import net.kozibrodka.wolves.recipe.AnvilShapelessRecipe;
import net.kozibrodka.wolves.recipe.AnvilCraftingManager;
import net.kozibrodka.wolves.recipe.AnvilRecipeTemplate;
import net.minecraft.block.Block;
import net.minecraft.class_564;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.util.Namespace;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnvilRecipeTab extends TabWithTexture {

    protected List<Object> recipesComplete;
    protected List<Object> recipes;
    private final Block tabBlock;
    private boolean isVanillaWorkbench = false; //THIS IS LAZY
    public ArrayList<Class<? extends HandledScreen>> guiCraftingStations = new ArrayList<>();
    public int recipeIndex;

    public AnvilRecipeTab(Namespace tabCreator) {
        this(tabCreator, new ArrayList<Object>(AnvilCraftingManager.getInstance().getRecipeList()), BlockListener.anvil);
        isVanillaWorkbench = true;
        guiCraftingStations.add(CraftingScreen.class);
    }

    public AnvilRecipeTab(Namespace tabCreator, List<Object> recipesComplete, Block tabBlock) {
        this(tabCreator, 26, recipesComplete, tabBlock, "/assets/wolves/stationapi/gui/hmi_tabs/giant_grid.png", 154, 92, 10, 15, 56, 46, 5);
        slots[0] = new Integer[]{132, 41};
    }

    public AnvilRecipeTab(Namespace tabCreator, int slotsPerRecipe, List<Object> recipesComplete, Block tabBlock, String texturePath, int width, int height, int textureX, int textureY, int buttonX, int buttonY, int slotsWidth) {
        super(tabCreator, slotsPerRecipe, texturePath, width, height, 3, 4, textureX, textureY, buttonX, buttonY);
        this.recipesComplete = recipesComplete;
        this.tabBlock = tabBlock;
        recipes = recipesComplete;
        int i = 1;
        for (int l = 0; l < 5; l++) {
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
            tess.startQuads();
            tess.vertex(x, y + size, 0, 0, 1);
            tess.vertex(x + size, y + size, 0, 1, 1);
            tess.vertex(x + size, y, 0, 1, 0);
            tess.vertex(x, y, 0, 0, 0);
            tess.draw();
        }
    }

    @Override
    public Class<? extends HandledScreen> getGuiClass() {
        return AnvilScreen.class;
    }

    @Override
    public ItemStack[][] getItems(int index, ItemStack filter) {
        recipeIndex = index;
        ItemStack[][] items = new ItemStack[recipesPerPage][];
        for (int j = 0; j < recipesPerPage; j++) {
            items[j] = new ItemStack[slots.length];
            int k = index + j;
            if (k < recipes.size()) {
                try {
                    Object recipeObj = recipes.get(k);
                    if (recipeObj instanceof StationRecipe) {
                        StationRecipe recipe = (StationRecipe) recipes.get(k);
                        ItemStack[] list = recipe.getIngredients();
                        ItemStack[] outputArray = recipe.getOutputs();
                        System.arraycopy(outputArray, 0, items[j], 0, outputArray.length);
                        int width = 5;
                        int skippedSlots = 0;
                        if (recipeObj instanceof AnvilShapedRecipe) {
                            width = Math.max(((AnvilShapedRecipe) recipeObj).getWidth(), 1); // Using max to prevent division by zero
                        }
                        for (int j1 = 0; j1 < list.length; j1++) {
                            if ((j1 + 1 + skippedSlots) % 5 > width || (j1 + skippedSlots) % 5 >= width && width == 4) { // Hideous condition that needs to handle an edge case for 4 width recipes
                                skippedSlots += 5 - width;
                            }
                            ItemStack item = list[j1];
                            items[j][skippedSlots + j1 + 1] = item;
                            if (item != null && item.getDamage() == -1) {
                                if (item.hasSubtypes()) {
                                    if (filter != null && item.itemId == filter.itemId) {
                                        items[j][skippedSlots + j1 + 1] = new ItemStack(item.getItem(), 0, filter.getDamage());
                                    } else {
                                        items[j][skippedSlots + j1 + 1] = new ItemStack(item.getItem());
                                    }
                                } else if (filter != null && item.itemId == filter.itemId) {
                                    items[j][skippedSlots + j1 + 1] = new ItemStack(item.getItem(), 0, filter.getDamage());
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
    public void updateRecipes(ItemStack filter, Boolean getUses) {
        List<Object> arraylist = new ArrayList<>();
        if (filter == null) {
            recipes = recipesComplete;
        } else {
            for (Object o : recipesComplete) {
                if (o instanceof AnvilRecipeTemplate) {
                    StationRecipe recipe = (StationRecipe) o;
                    if (!getUses) {
                        if (Arrays.stream(recipe.getOutputs()).anyMatch(itemInstance -> filter.itemId == itemInstance.itemId && (itemInstance.getDamage() == filter.getDamage() || itemInstance.getDamage() < 0 || !itemInstance.hasSubtypes()))) {
                            arraylist.add(o);
                        }
                    } else {
                        try {
                            ItemStack[] aitemstack = recipe.getIngredients();
                            for (ItemStack itemstack1 : aitemstack) {
                                if (itemstack1 == null || filter.itemId != itemstack1.itemId || (itemstack1.hasSubtypes() && itemstack1.getDamage() != filter.getDamage()) && itemstack1.getDamage() >= 0) {
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
    public ItemStack getTabItem() {
        return new ItemStack(tabBlock);
    }

    @Override
    public Boolean drawSetupRecipeButton(Screen parent, ItemStack[] recipeItems) {
        for (Class<? extends HandledScreen> gui : guiCraftingStations) {
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
    public Boolean[] itemsInInventory(Screen parent, ItemStack[] recipeItems) {
        Boolean[] itemsInInv = new Boolean[slots.length - 1];
        List<Object> list;
        if (parent instanceof HandledScreen)
            //noinspection unchecked
            list = ((HandledScreen) parent).container.slots;
        else
            //noinspection unchecked
            list = Utils.getMC().player.container.slots;
        ItemStack[] aslot = new ItemStack[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (((Slot) list.get(i)).hasStack())
                aslot[i] = ((Slot) list.get(i)).getStack().copy();
        }

        aslot[0] = null;
        recipe:
        for (int i = 1; i < recipeItems.length; i++) {
            ItemStack item = recipeItems[i];
            if (item == null) {
                itemsInInv[i - 1] = true;
                continue;
            }

            for (ItemStack slot : aslot) {
                if (slot != null && slot.count > 0 && slot.itemId == item.itemId && (slot.getDamage() == item.getDamage() || item.getDamage() < 0 || !item.hasSubtypes())) {
                    slot.count -= 1;
                    itemsInInv[i - 1] = true;
                    continue recipe;
                }
            }
            itemsInInv[i - 1] = false;
        }
        return itemsInInv;
    }

    private int recipeStackSize(List<Object> list, ItemStack[] recipeItems) {

        int[] itemStackSize = new int[recipeItems.length - 1];

        for (int i = 1; i < recipeItems.length; i++) {
            ItemStack[] aslot = new ItemStack[list.size()];
            for (int k = 0; k < list.size(); k++) {
                if (((Slot) list.get(k)).hasStack())
                    aslot[k] = ((Slot) list.get(k)).getStack().copy();
            }
            aslot[0] = null;

            ItemStack item = recipeItems[i];
            itemStackSize[i - 1] = 0;
            if (item == null) {
                itemStackSize[i - 1] = -1;
                continue;
            }
            int count = 0;
            for (ItemStack slot : aslot) {
                if (slot != null && slot.count > 0 && slot.itemId == item.itemId && (slot.getDamage() == item.getDamage() || item.getDamage() < 0 || !item.hasSubtypes())) {
                    count += slot.count;
                    slot.count = 0;
                }
            }
            int prevEqualItemCount = 1;
            for (int j = 1; j < i; j++) {
                if (recipeItems[j] != null && recipeItems[j].isItemEqual(item)) {
                    prevEqualItemCount++;
                }
            }
            for (int j = 1; j < recipeItems.length; j++) {
                if (recipeItems[j] != null && recipeItems[j].isItemEqual(item)) {
                    itemStackSize[j - 1] = count / prevEqualItemCount;
                }
            }
        }
        int finalItemStackSize = -1;
        for (int i = 0; i < itemStackSize.length; i++) {
            ItemStack item = recipeItems[i + 1];
            if (itemStackSize[i] == -1 || item.getMaxCount() == 1) continue;
            if (finalItemStackSize == -1 || itemStackSize[i] < finalItemStackSize || finalItemStackSize > item.getMaxCount()) {
                finalItemStackSize = Math.min(itemStackSize[i], item.getMaxCount());
            }
        }
        if (finalItemStackSize > 0) return finalItemStackSize;
        return 1;
    }

    @Override
    public void setupRecipe(Screen parent, ItemStack[] recipeItems) {
        if (parent == null) {
            Utils.getMC().method_2134();
            class_564 scaledresolution = new class_564(Utils.getMC().options, Utils.getMC().displayWidth, Utils.getMC().displayHeight);
            int i = scaledresolution.method_1857();
            int j = scaledresolution.method_1858();
            parent = new InventoryScreen(Utils.getMC().player);
            Utils.getMC().currentScreen = parent;
            parent.init(Utils.getMC(), i, j);
            Utils.getMC().field_2821 = false;
        }
        HandledScreen container = ((HandledScreen) parent);
        //noinspection unchecked
        List<Object> inventorySlots = container.container.slots;

        int recipeStackSize = 1;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            recipeStackSize = recipeStackSize(inventorySlots, recipeItems);
        }

        this.player = Utils.getMC().player;
        this.inv = Utils.getMC().interactionManager;
        this.windowId = container.container.syncId;
        for (int recipeSlotIndex = 1; recipeSlotIndex < recipeItems.length; recipeSlotIndex++) {
            if (isInv(parent) && recipeSlotIndex > 5)
                break;
            int slotid = recipeSlotIndex;
            if (isInv(parent) && recipeSlotIndex > 3) {
                slotid--;
            }
            Slot recipeSlot = (Slot) inventorySlots.get(slotid);
            //clear recipe slot
            if (recipeSlot.hasStack()) {
                this.clickSlot(slotid, true, true);

                if (recipeSlot.hasStack()) {
                    this.clickSlot(slotid, true, false);
                    if (player.inventory.getCursorStack() != null) {
                        for (int j = slotid + 1; j < inventorySlots.size(); j++) {
                            Slot slot = (Slot) inventorySlots.get(j);
                            if (!slot.hasStack()) {
                                this.clickSlot(j, true, false);
                                break;
                            }
                        }
                        if (player.inventory.getCursorStack() != null) {
                            this.clickSlot(-999, true, false);
                        }
                    }
                }
            }

            //if recipe slot should be empty, continue
            ItemStack item = recipeItems[recipeSlotIndex];
            if (item == null) {
                continue;
            }

            //locate correct item and put in recipe slot
            while (!recipeSlot.hasStack() || (recipeSlot.getStack().count < recipeStackSize && recipeSlot.getStack().getMaxCount() > 1))
                for (int inventorySlotIndex = recipeSlotIndex + 1; inventorySlotIndex < inventorySlots.size(); inventorySlotIndex++) {
                    Slot inventorySlot = (Slot) inventorySlots.get(inventorySlotIndex);
                    if (inventorySlot.hasStack() && inventorySlot.getStack().itemId == item.itemId && (inventorySlot.getStack().getDamage() == item.getDamage() || item.getDamage() < 0 || !item.hasSubtypes())) {
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

    InteractionManager inv;
    ClientPlayerEntity player;
    int windowId;

    void clickSlot(int slotIndex, boolean leftClick, boolean shiftClick) {
        inv.clickSlot(windowId, slotIndex, leftClick ? 0 : 1, shiftClick, player);
    }

    boolean isInv(Screen screen) {
        return screen instanceof Inventory;
    }

}
