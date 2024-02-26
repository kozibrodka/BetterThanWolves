package net.kozibrodka.wolves.tabs;

import net.glasslauncher.hmifabric.Utils;
import net.glasslauncher.hmifabric.tabs.TabWithTexture;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.gui.CauldronGUI;
import net.kozibrodka.wolves.recipe.CrucibleCraftingManager;
import net.minecraft.block.BlockBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.kozibrodka.wolves.utils.ItemUtil.compare;

public class CrucibleTripleRecipeTab extends TabWithTexture {

    private static final Random RANDOM = new Random();
    protected List<ItemInstance[]> recipes;
    private final BlockBase tabBlock;
    private final List<ItemInstance[]> recipesReady;

    public CrucibleTripleRecipeTab(Namespace tabCreator) {
        this(tabCreator, new ArrayList<ItemInstance[]>(CrucibleCraftingManager.getInstance().getTripleRecipes()), BlockListener.crucible);
    }

    public CrucibleTripleRecipeTab(Namespace tabCreator, List<ItemInstance[]> recipesReady, BlockBase tabBlock) {
        this(tabCreator, 4, recipesReady, tabBlock, "/assets/wolves/stationapi/gui/hmi_tabs/three_in_one_out.png", 140, 56, 22, 15);
    }

    public CrucibleTripleRecipeTab(Namespace tabCreator, int slotsPerRecipe, List<ItemInstance[]> recipesReady, BlockBase tabBlock, String texturePath, int width, int height, int textureX, int textureY) {
        super(tabCreator, slotsPerRecipe, texturePath, width, height, 3, 4, textureX, textureY);
        this.recipesReady = recipesReady;
        this.tabBlock = tabBlock;
        recipes = new ArrayList<>();

        int xOffset = -28;
        int yOffset = -12;

        slots[0] = new Integer[] {32 + xOffset, 17 + yOffset};
        slots[1] = new Integer[] {32 + xOffset, 35 + yOffset};
        slots[2] = new Integer[] {32 + xOffset, 53 + yOffset};
        slots[3] = new Integer[] {148 + xOffset, 35 + yOffset};
    }

    @Override
    public void draw(int x, int y, int recipeOnThisPageIndex, int cursorX, int cursorY) {
        super.draw(x, y, recipeOnThisPageIndex, cursorX, cursorY);
        Utils.drawScaledItem(new ItemInstance(BlockListener.bellows), x + 60, y + 36, 21);
        Utils.drawScaledItem(new ItemInstance(BlockListener.crucible), x + 54, y + 12, 34);
    }

    @Override
    public ItemInstance[][] getItems(int index, ItemInstance filter) {
        ItemInstance[][] items = new ItemInstance[recipesPerPage][];
        for (int j = 0; j < recipesPerPage; j++) {
            items[j] = new ItemInstance[slots.length];
            int k = index + j;
            if (k < recipes.size()) try {
                ItemInstance[] recipe = recipes.get(k);
                items[j][3] = recipe[0];
                items[j][2] = recipe[1];
                items[j][1] = recipe[2];
                items[j][0] = recipe[3];
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
    public void updateRecipes(ItemInstance filter, Boolean getUses) {
        recipes.clear();
        updateRecipesWithoutClear(filter, getUses);
    }

    public void updateRecipesWithoutClear(ItemInstance filter, Boolean getUses) {
        lastIndex = 0;
        recipesReady.forEach(recipe -> {
            ItemInstance firstInput = recipe[1];
            ItemInstance secondInput = recipe[2];
            ItemInstance thirdInput = recipe[3];
            ItemInstance output = recipe[0];
            if (filter == null || (!getUses && compare(filter, output)) || (getUses && compare(filter, firstInput)) || (getUses && compare(filter, secondInput)) || (getUses && compare(filter, thirdInput)) || filter.isDamageAndIDIdentical(new ItemInstance(tabBlock))) {
                recipes.add(recipe);
            }
        });
        size = recipes.size();
    }

    @Override
    public Class<? extends ContainerBase> getGuiClass() {
        return CauldronGUI.class;
    }

    @Override
    public ItemInstance getTabItem() {
        return new ItemInstance(tabBlock);
    }
}