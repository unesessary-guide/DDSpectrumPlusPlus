package com.unesessary_guide.ddspectrum.datagen;

import java.util.concurrent.CompletableFuture;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.dye.SpectrumDye;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;
import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.ModItems;
import com.unesessary_guide.ddspectrum.registry.SpectrumBlockType;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Tags every new dye as {@code c:dyes}, and groups all 140 dyes (anchors included) by
 * {@code dd_spectrum:family/<family>} and {@code dd_spectrum:variant/<variant>}.
 * Anchors are added as optional entries, since they belong to vanilla and Dye Depot.
 */
public class SpectrumItemTagsProvider extends ItemTagsProvider {
    public SpectrumItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                    CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                                    ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, DDSpectrum.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        SpectrumDyes.newDyes().forEach(dye -> tag(Tags.Items.DYES).add(ModItems.dye(dye).get()));
        ModBlocks.all(SpectrumBlockType.WOOL).forEach(block -> tag(ItemTags.WOOL).add(block.asItem()));

        for (SpectrumDye dye : SpectrumDyes.ALL) {
            put(tag(groupTag("family/" + dye.family().id())), dye);
            if (dye.variant() != null) {
                put(tag(groupTag("variant/" + dye.variant().id())), dye);
            }
        }
    }

    private static TagKey<Item> groupTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(DDSpectrum.MODID, path));
    }

    private static void put(IntrinsicTagAppender<Item> tag, SpectrumDye dye) {
        if (dye.isNew()) {
            tag.add(ModItems.dye(dye).get());
        } else {
            tag.addOptional(ResourceLocation.parse(dye.itemId(DDSpectrum.MODID)));
        }
    }
}
