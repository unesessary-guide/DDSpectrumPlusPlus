package com.unesessary_guide.ddspectrum.datagen;

import java.util.Set;

import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.SpectrumBlockType;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

/** Every block drops itself, except stained glass, which only drops with Silk Touch (like vanilla). */
public class SpectrumBlockLoot extends BlockLootSubProvider {
    public SpectrumBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    public void generate() {
        for (SpectrumBlockType type : SpectrumBlockType.values()) {
            for (DeferredBlock<Block> block : ModBlocks.all(type)) {
                if (type == SpectrumBlockType.STAINED_GLASS) {
                    dropWhenSilkTouch(block.get());
                } else {
                    dropSelf(block.get());
                }
            }
        }
        ModBlocks.baskets().forEach(basket -> dropSelf(basket.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.allBlocks().<Block>map(DeferredBlock::get).toList();
    }
}
