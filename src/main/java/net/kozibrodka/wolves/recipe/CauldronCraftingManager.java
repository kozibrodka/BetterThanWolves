package net.kozibrodka.wolves.recipe;

public class CauldronCraftingManager extends MultiInputCraftingManager {

    public static final CauldronCraftingManager getInstance() {
        return instance;
    }

    private CauldronCraftingManager() {
    }

    private static final CauldronCraftingManager instance = new CauldronCraftingManager();

}
