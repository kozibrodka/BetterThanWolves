package net.kozibrodka.wolves.recipe;


public class FCCraftingManagerCauldron extends FCCraftingManagerBulk
{

    public static final FCCraftingManagerCauldron getInstance()
    {
        return instance;
    }

    private FCCraftingManagerCauldron()
    {
    }

    private static final FCCraftingManagerCauldron instance = new FCCraftingManagerCauldron();

}
