package net.kozibrodka.wolves.tabs;

import net.glasslauncher.hmifabric.Utils;
import net.glasslauncher.hmifabric.tabs.TabWithTexture;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.gui.MillStoneScreen;
import net.kozibrodka.wolves.recipe.SawingRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.kozibrodka.wolves.utils.ItemUtil.compare;

public class SawingRecipeTab extends TabWithTexture {

    private static final Random RANDOM = new Random();
    protected List<ItemStack[]> recipes;
    private final Block tabBlock;
    private final List<ItemStack[]> recipesReady;

    public SawingRecipeTab(Namespace tabCreator) {
        this(tabCreator, new ArrayList<ItemStack[]>(SawingRecipeRegistry.getInstance().getRecipes()), BlockListener.saw);
    }

    public SawingRecipeTab(Namespace tabCreator, List<ItemStack[]> recipesReady, Block tabBlock) {
        this(tabCreator, 2, recipesReady, tabBlock, "/assets/wolves/stationapi/gui/hmi_tabs/one_in_one_out.png", 140, 56, 22, 15);
    }

    public SawingRecipeTab(Namespace tabCreator, int slotsPerRecipe, List<ItemStack[]> recipesReady, Block tabBlock, String texturePath, int width, int height, int textureX, int textureY) {
        super(tabCreator, slotsPerRecipe, texturePath, width, height, 3, 4, textureX, textureY);
        this.recipesReady = recipesReady;
        this.tabBlock = tabBlock;
        recipes = new ArrayList<>();

        int xOffset = -28;
        int yOffset = -12;

        slots[0] = new Integer[] {32 + xOffset, 35 + yOffset};
        slots[1] = new Integer[] {148 + xOffset, 35 + yOffset};
    }

    @Override
    public void draw(int x, int y, int recipeOnThisPageIndex, int cursorX, int cursorY) {
        super.draw(x, y, recipeOnThisPageIndex, cursorX, cursorY);
        Utils.drawScaledItem(new ItemStack(BlockListener.saw), x + 54, y + 12, 34);
    }

    @Override
    public ItemStack[][] getItems(int index, ItemStack filter) {
        ItemStack[][] items = new ItemStack[recipesPerPage][];
        for (int j = 0; j < recipesPerPage; j++) {
            items[j] = new ItemStack[slots.length];
            int k = index + j;
            if (k < recipes.size()) try {
                ItemStack[] recipe = recipes.get(k);
                if (recipe[0].itemId == BlockListener.omniSlab.id) recipe[0] = new ItemStack(BlockListener.omniSlab, 1, 1); // This is a very cursed workaround to display the wooden slab and not the stone variant
                items[j][0] = recipe[0];
                items[j][1] = recipe[1];
            } catch (Throwable throwable) {
                throwable.printStackTrace();
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
        recipes.clear();
        updateRecipesWithoutClear(filter, getUses);
    }

    public void updateRecipesWithoutClear(ItemStack filter, Boolean getUses) {
        lastIndex = 0;
        recipesReady.forEach(recipe -> {
            ItemStack input = recipe[0];
            ItemStack output = recipe[1];
            if (filter == null || (!getUses && compare(filter, output)) || (getUses && compare(filter, input)) || filter.isItemEqual(new ItemStack(tabBlock))) {
                recipes.add(recipe);
            }
        });
        size = recipes.size();
    }

    @Override
    public Class<? extends HandledScreen> getGuiClass() {
        return MillStoneScreen.class;
    }

    @Override
    public ItemStack getTabItem() {
        return new ItemStack(tabBlock);
    }
}
