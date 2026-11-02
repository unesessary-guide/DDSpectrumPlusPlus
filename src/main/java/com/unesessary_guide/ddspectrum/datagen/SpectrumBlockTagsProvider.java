package com.unesessary_guide.ddspectrum.datagen;

import java.util.concurrent.CompletableFuture;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.SpectrumBlockType;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Wool joins minecraft:wool. Concrete, terracotta and glazed terracotta are mined with a pickaxe, and concrete powder
 * with a shovel, like their vanilla counterparts.
 */
public class SpectrumBlockTagsProvider extends BlockTagsProvider {
    public SpectrumBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                     ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DDSpectrum.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ModBlocks.all(SpectrumBlockType.WOOL).forEach(block -> tag(BlockTags.WOOL).add(block.get()));
        for (SpectrumBlockType type : new SpectrumBlockType[] {SpectrumBlockType.CONCRETE, SpectrumBlockType.TERRACOTTA,
                SpectrumBlockType.GLAZED_TERRACOTTA}) {
            ModBlocks.all(type).forEach(block -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get()));
        }
        ModBlocks.all(SpectrumBlockType.CONCRETE_POWDER).forEach(block -> tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block.get()));
    }
}
