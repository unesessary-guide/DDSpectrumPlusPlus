package com.unesessary_guide.ddspectrum.datagen;

import java.util.concurrent.CompletableFuture;

import com.unesessary_guide.ddspectrum.dye.SpectrumDye;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;
import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.ModItems;
import com.unesessary_guide.ddspectrum.registry.SpectrumBlockType;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Block recipes for the new dyes, following vanilla's recipes. These use the dye items as ingredients; the recipes
 * that craft the dyes themselves are not here yet.
 *
 * Wool: any wool + dye. Concrete powder: dye + 4 sand + 4 gravel (concrete itself is made by water, so no recipe).
 * Terracotta and stained glass: 8 plain terracotta or glass around a dye. Glazed terracotta: smelt the terracotta.
 * Dye basket: 9 of the same dye in a 3 x 3.
 */
public class SpectrumRecipeProvider extends RecipeProvider {
    public SpectrumRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        SpectrumDyes.newDyes().forEach(dye -> recipes(output, dye));
    }

    private void recipes(RecipeOutput output, SpectrumDye dye) {
        Item dyeItem = ModItems.dye(dye).get();
        Block wool = ModBlocks.block(SpectrumBlockType.WOOL, dye).get();
        Block powder = ModBlocks.block(SpectrumBlockType.CONCRETE_POWDER, dye).get();
        Block terracotta = ModBlocks.block(SpectrumBlockType.TERRACOTTA, dye).get();
        Block glazed = ModBlocks.block(SpectrumBlockType.GLAZED_TERRACOTTA, dye).get();
        Block glass = ModBlocks.block(SpectrumBlockType.STAINED_GLASS, dye).get();
        Block basket = ModBlocks.basket(dye).get();

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, wool)
                .requires(dyeItem).requires(ItemTags.WOOL)
                .group("wool").unlockedBy("has_dye", has(dyeItem)).save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, powder, 8)
                .requires(dyeItem).requires(Items.SAND, 4).requires(Items.GRAVEL, 4)
                .group("concrete_powder").unlockedBy("has_dye", has(dyeItem)).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, terracotta, 8)
                .define('#', Items.TERRACOTTA).define('D', dyeItem)
                .pattern("###").pattern("#D#").pattern("###")
                .group("stained_terracotta").unlockedBy("has_terracotta", has(Items.TERRACOTTA)).save(output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(terracotta), RecipeCategory.DECORATIONS, glazed, 0.1F, 200)
                .unlockedBy("has_terracotta", has(terracotta)).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, glass, 8)
                .define('#', Items.GLASS).define('D', dyeItem)
                .pattern("###").pattern("#D#").pattern("###")
                .group("stained_glass").unlockedBy("has_glass", has(Items.GLASS)).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, basket)
                .define('#', dyeItem)
                .pattern("###").pattern("###").pattern("###")
                .unlockedBy("has_dye", has(dyeItem)).save(output);
    }
}
