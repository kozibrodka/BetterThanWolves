package net.kozibrodka.wolves.compat.nfc;

import net.kozibrodka.wolves.events.RecipeListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.newfrontiercraft.nfc.events.init.BlockListener;
import net.newfrontiercraft.nfc.events.init.ItemListener;

public class NFCRecipes {

    public static void addCrucibleRecipes() {
        RecipeListener.addCrucibleRecipe(new ItemStack(net.kozibrodka.wolves.events.ItemListener.steel, 4), new ItemStack[]{new ItemStack(ItemListener.steelIngot, 2), new ItemStack(net.kozibrodka.wolves.events.ItemListener.concentratedHellfire), new ItemStack(net.kozibrodka.wolves.events.ItemListener.coalDust)});
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.anthracite, 2), new ItemStack[]{new ItemStack(ItemListener.anthracite), new ItemStack(net.kozibrodka.wolves.events.ItemListener.netherCoal, 4)});

        // Recycling (aluminium)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 4), new ItemStack[] { new ItemStack(BlockListener.aluminiumBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 8), new ItemStack[] { new ItemStack(ItemListener.aluminiumChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 7), new ItemStack[] { new ItemStack(ItemListener.aluminiumLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 5), new ItemStack[] { new ItemStack(ItemListener.aluminiumHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 4), new ItemStack[] { new ItemStack(ItemListener.aluminiumBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.aluminiumPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.aluminiumAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.aluminiumSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.aluminiumHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.aluminiumIngot, 1), new ItemStack[] { new ItemStack(ItemListener.aluminiumShovel, 1, -1) });

        // Recycling (bismuth)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 4), new ItemStack[] { new ItemStack(BlockListener.bismuthBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 8), new ItemStack[] { new ItemStack(ItemListener.bismuthChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 7), new ItemStack[] { new ItemStack(ItemListener.bismuthLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 5), new ItemStack[] { new ItemStack(ItemListener.bismuthHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 4), new ItemStack[] { new ItemStack(ItemListener.bismuthBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 3), new ItemStack[] { new ItemStack(ItemListener.bismuthPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 3), new ItemStack[] { new ItemStack(ItemListener.bismuthAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 2), new ItemStack[] { new ItemStack(ItemListener.bismuthSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 2), new ItemStack[] { new ItemStack(ItemListener.bismuthHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bismuthIngot, 1), new ItemStack[] { new ItemStack(ItemListener.bismuthShovel, 1, -1) });

        // Recycling (copper)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 4), new ItemStack[] { new ItemStack(BlockListener.copperBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 8), new ItemStack[] { new ItemStack(ItemListener.copperChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 7), new ItemStack[] { new ItemStack(ItemListener.copperLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 5), new ItemStack[] { new ItemStack(ItemListener.copperHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 4), new ItemStack[] { new ItemStack(ItemListener.copperBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 3), new ItemStack[] { new ItemStack(ItemListener.copperPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 3), new ItemStack[] { new ItemStack(ItemListener.copperAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 2), new ItemStack[] { new ItemStack(ItemListener.copperSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 2), new ItemStack[] { new ItemStack(ItemListener.copperHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.copperIngot, 1), new ItemStack[] { new ItemStack(ItemListener.copperShovel, 1, -1) });

        // Recycling (lead)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.leadIngot, 4), new ItemStack[] { new ItemStack(BlockListener.leadBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.leadIngot, 3), new ItemStack[] { new ItemStack(ItemListener.leadPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.leadIngot, 3), new ItemStack[] { new ItemStack(ItemListener.leadAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.leadIngot, 2), new ItemStack[] { new ItemStack(ItemListener.leadSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.leadIngot, 2), new ItemStack[] { new ItemStack(ItemListener.leadHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.leadIngot, 1), new ItemStack[] { new ItemStack(ItemListener.leadShovel, 1, -1) });

        // Recycling (tin)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 4), new ItemStack[] { new ItemStack(BlockListener.tinBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 8), new ItemStack[] { new ItemStack(ItemListener.tinChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 7), new ItemStack[] { new ItemStack(ItemListener.tinLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 5), new ItemStack[] { new ItemStack(ItemListener.tinHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 4), new ItemStack[] { new ItemStack(ItemListener.tinBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 3), new ItemStack[] { new ItemStack(ItemListener.tinPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 3), new ItemStack[] { new ItemStack(ItemListener.tinAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 2), new ItemStack[] { new ItemStack(ItemListener.tinSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 2), new ItemStack[] { new ItemStack(ItemListener.tinHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tinIngot, 1), new ItemStack[] { new ItemStack(ItemListener.tinShovel, 1, -1) });

        // Recycling (zinc)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 4), new ItemStack[] { new ItemStack(BlockListener.zincBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 8), new ItemStack[] { new ItemStack(ItemListener.zincChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 7), new ItemStack[] { new ItemStack(ItemListener.zincLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 5), new ItemStack[] { new ItemStack(ItemListener.zincHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 4), new ItemStack[] { new ItemStack(ItemListener.zincBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 3), new ItemStack[] { new ItemStack(ItemListener.zincPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 3), new ItemStack[] { new ItemStack(ItemListener.zincAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 2), new ItemStack[] { new ItemStack(ItemListener.zincSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 2), new ItemStack[] { new ItemStack(ItemListener.zincHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.zincIngot, 1), new ItemStack[] { new ItemStack(ItemListener.zincShovel, 1, -1) });

        // Recycling (boron)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 4), new ItemStack[] { new ItemStack(BlockListener.boronBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 8), new ItemStack[] { new ItemStack(ItemListener.boronChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 7), new ItemStack[] { new ItemStack(ItemListener.boronLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 5), new ItemStack[] { new ItemStack(ItemListener.boronHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 4), new ItemStack[] { new ItemStack(ItemListener.boronBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 3), new ItemStack[] { new ItemStack(ItemListener.boronPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 3), new ItemStack[] { new ItemStack(ItemListener.boronAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 2), new ItemStack[] { new ItemStack(ItemListener.boronSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 2), new ItemStack[] { new ItemStack(ItemListener.boronHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.boronIngot, 1), new ItemStack[] { new ItemStack(ItemListener.boronShovel, 1, -1) });

        // Recycling (brass)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 4), new ItemStack[] { new ItemStack(BlockListener.brassBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 8), new ItemStack[] { new ItemStack(ItemListener.brassChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 7), new ItemStack[] { new ItemStack(ItemListener.brassLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 5), new ItemStack[] { new ItemStack(ItemListener.brassHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 4), new ItemStack[] { new ItemStack(ItemListener.brassBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 3), new ItemStack[] { new ItemStack(ItemListener.brassPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 3), new ItemStack[] { new ItemStack(ItemListener.brassAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 2), new ItemStack[] { new ItemStack(ItemListener.brassSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 2), new ItemStack[] { new ItemStack(ItemListener.brassHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.brassIngot, 1), new ItemStack[] { new ItemStack(ItemListener.brassShovel, 1, -1) });

        // Recycling (bronze)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 4), new ItemStack[] { new ItemStack(BlockListener.bronzeBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 8), new ItemStack[] { new ItemStack(ItemListener.bronzeChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 7), new ItemStack[] { new ItemStack(ItemListener.bronzeLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 5), new ItemStack[] { new ItemStack(ItemListener.bronzeHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 4), new ItemStack[] { new ItemStack(ItemListener.bronzeBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 3), new ItemStack[] { new ItemStack(ItemListener.bronzePickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 3), new ItemStack[] { new ItemStack(ItemListener.bronzeAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 2), new ItemStack[] { new ItemStack(ItemListener.bronzeSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 2), new ItemStack[] { new ItemStack(ItemListener.bronzeHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.bronzeIngot, 1), new ItemStack[] { new ItemStack(ItemListener.bronzeShovel, 1, -1) });

        // Recycling (nickel)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 4), new ItemStack[] { new ItemStack(BlockListener.nickelBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 8), new ItemStack[] { new ItemStack(ItemListener.nickelChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 7), new ItemStack[] { new ItemStack(ItemListener.nickelLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 5), new ItemStack[] { new ItemStack(ItemListener.nickelHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 4), new ItemStack[] { new ItemStack(ItemListener.nickelBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 3), new ItemStack[] { new ItemStack(ItemListener.nickelPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 3), new ItemStack[] { new ItemStack(ItemListener.nickelAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 2), new ItemStack[] { new ItemStack(ItemListener.nickelSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 2), new ItemStack[] { new ItemStack(ItemListener.nickelHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.nickelIngot, 1), new ItemStack[] { new ItemStack(ItemListener.nickelShovel, 1, -1) });

        // Recycling (platinum)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 4), new ItemStack[] { new ItemStack(BlockListener.platinumBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 8), new ItemStack[] { new ItemStack(ItemListener.platinumChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 7), new ItemStack[] { new ItemStack(ItemListener.platinumLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 5), new ItemStack[] { new ItemStack(ItemListener.platinumHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 4), new ItemStack[] { new ItemStack(ItemListener.platinumBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.platinumPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.platinumAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.platinumSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.platinumHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.platinumIngot, 1), new ItemStack[] { new ItemStack(ItemListener.platinumShovel, 1, -1) });

        // Recycling (silver)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 4), new ItemStack[] { new ItemStack(BlockListener.silverBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 8), new ItemStack[] { new ItemStack(ItemListener.silverChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 7), new ItemStack[] { new ItemStack(ItemListener.silverLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 5), new ItemStack[] { new ItemStack(ItemListener.silverHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 4), new ItemStack[] { new ItemStack(ItemListener.silverBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 3), new ItemStack[] { new ItemStack(ItemListener.silverPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 3), new ItemStack[] { new ItemStack(ItemListener.silverAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 2), new ItemStack[] { new ItemStack(ItemListener.silverSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 2), new ItemStack[] { new ItemStack(ItemListener.silverHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.silverIngot, 1), new ItemStack[] { new ItemStack(ItemListener.silverShovel, 1, -1) });

        // Recycling (chrome)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 4), new ItemStack[] { new ItemStack(BlockListener.chromeBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 8), new ItemStack[] { new ItemStack(ItemListener.chromeChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 7), new ItemStack[] { new ItemStack(ItemListener.chromeLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 5), new ItemStack[] { new ItemStack(ItemListener.chromeHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 4), new ItemStack[] { new ItemStack(ItemListener.chromeBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 3), new ItemStack[] { new ItemStack(ItemListener.chromePickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 3), new ItemStack[] { new ItemStack(ItemListener.chromeAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 2), new ItemStack[] { new ItemStack(ItemListener.chromeSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 2), new ItemStack[] { new ItemStack(ItemListener.chromeHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.chromeIngot, 1), new ItemStack[] { new ItemStack(ItemListener.chromeShovel, 1, -1) });

        // Recycling (cobalt)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 4), new ItemStack[] { new ItemStack(BlockListener.cobaltBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 8), new ItemStack[] { new ItemStack(ItemListener.cobaltChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 7), new ItemStack[] { new ItemStack(ItemListener.cobaltLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 5), new ItemStack[] { new ItemStack(ItemListener.cobaltHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 4), new ItemStack[] { new ItemStack(ItemListener.cobaltBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 3), new ItemStack[] { new ItemStack(ItemListener.cobaltPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 3), new ItemStack[] { new ItemStack(ItemListener.cobaltAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 2), new ItemStack[] { new ItemStack(ItemListener.cobaltSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 2), new ItemStack[] { new ItemStack(ItemListener.cobaltHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.cobaltIngot, 1), new ItemStack[] { new ItemStack(ItemListener.cobaltShovel, 1, -1) });

        // Recycling (silicon)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 4), new ItemStack[] { new ItemStack(BlockListener.siliconBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 8), new ItemStack[] { new ItemStack(ItemListener.siliconChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 7), new ItemStack[] { new ItemStack(ItemListener.siliconLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 5), new ItemStack[] { new ItemStack(ItemListener.siliconHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 4), new ItemStack[] { new ItemStack(ItemListener.siliconBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 3), new ItemStack[] { new ItemStack(ItemListener.siliconPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 3), new ItemStack[] { new ItemStack(ItemListener.siliconAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 2), new ItemStack[] { new ItemStack(ItemListener.siliconSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 2), new ItemStack[] { new ItemStack(ItemListener.siliconHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.siliconIngot, 1), new ItemStack[] { new ItemStack(ItemListener.siliconShovel, 1, -1) });

        // Recycling (magnetite)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.magnetiteIngot, 4), new ItemStack[] { new ItemStack(BlockListener.magnetiteBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.magnetiteIngot, 3), new ItemStack[] { new ItemStack(ItemListener.magnetPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.magnetiteIngot, 3), new ItemStack[] { new ItemStack(ItemListener.magnetAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.magnetiteIngot, 2), new ItemStack[] { new ItemStack(ItemListener.magnetSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.magnetiteIngot, 2), new ItemStack[] { new ItemStack(ItemListener.magnetHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.magnetiteIngot, 1), new ItemStack[] { new ItemStack(ItemListener.magnetShovel, 1, -1) });

        // Recycling (steel)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 4), new ItemStack[] { new ItemStack(BlockListener.steelBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 8), new ItemStack[] { new ItemStack(ItemListener.steelChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 7), new ItemStack[] { new ItemStack(ItemListener.steelLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 5), new ItemStack[] { new ItemStack(ItemListener.steelHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 4), new ItemStack[] { new ItemStack(ItemListener.steelBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 3), new ItemStack[] { new ItemStack(ItemListener.steelPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 3), new ItemStack[] { new ItemStack(ItemListener.steelAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 2), new ItemStack[] { new ItemStack(ItemListener.steelSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 2), new ItemStack[] { new ItemStack(ItemListener.steelHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steelIngot, 1), new ItemStack[] { new ItemStack(ItemListener.steelShovel, 1, -1) });

        // Recycling (titanium)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 4), new ItemStack[] { new ItemStack(BlockListener.titaniumBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 8), new ItemStack[] { new ItemStack(ItemListener.titaniumChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 7), new ItemStack[] { new ItemStack(ItemListener.titaniumLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 5), new ItemStack[] { new ItemStack(ItemListener.titaniumHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 4), new ItemStack[] { new ItemStack(ItemListener.titaniumBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.titaniumPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.titaniumAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.titaniumSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.titaniumHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.titaniumIngot, 1), new ItemStack[] { new ItemStack(ItemListener.titaniumShovel, 1, -1) });

        // Recycling (tungsten)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 4), new ItemStack[] { new ItemStack(BlockListener.tungstenBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 8), new ItemStack[] { new ItemStack(ItemListener.tungstenChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 7), new ItemStack[] { new ItemStack(ItemListener.tungstenLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 5), new ItemStack[] { new ItemStack(ItemListener.tungstenHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 4), new ItemStack[] { new ItemStack(ItemListener.tungstenBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 3), new ItemStack[] { new ItemStack(ItemListener.tungstenPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 3), new ItemStack[] { new ItemStack(ItemListener.tungstenAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 2), new ItemStack[] { new ItemStack(ItemListener.tungstenSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 2), new ItemStack[] { new ItemStack(ItemListener.tungstenHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.tungstenIngot, 1), new ItemStack[] { new ItemStack(ItemListener.tungstenShovel, 1, -1) });

        // Recycling (osmium)
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 4), new ItemStack[] { new ItemStack(BlockListener.osmiumBlock) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 8), new ItemStack[] { new ItemStack(ItemListener.osmiumChestplate, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 7), new ItemStack[] { new ItemStack(ItemListener.osmiumLeggings, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 5), new ItemStack[] { new ItemStack(ItemListener.osmiumHelmet, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 4), new ItemStack[] { new ItemStack(ItemListener.osmiumBoots, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.osmiumPickaxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 3), new ItemStack[] { new ItemStack(ItemListener.osmiumAxe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.osmiumSword, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 2), new ItemStack[] { new ItemStack(ItemListener.osmiumHoe, 1, -1) });
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.osmiumIngot, 1), new ItemStack[] { new ItemStack(ItemListener.osmiumShovel, 1, -1) });
    }

    public static void addCauldronRecipes() {
        RecipeListener.addCauldronRecipe(new ItemStack(ItemListener.netherAsh, 2), new ItemStack[] {
                new ItemStack(BlockListener.petrifiedLog, 1), new ItemStack(ItemListener.netherAsh, 1), new ItemStack(Item.COAL, 2, 1)
        });
        RecipeListener.addStokedCauldronRecipe(new ItemStack(Item.COAL, 2), new ItemStack[] {
                new ItemStack(net.kozibrodka.wolves.events.ItemListener.coalDust, 1), new ItemStack(ItemListener.netherAsh, 2)
        });
    }
}
