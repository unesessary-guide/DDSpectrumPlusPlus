package com.unesessary_guide.ddspectrum.datagen;

import java.util.concurrent.CompletableFuture;

import com.unesessary_guide.ddspectrum.DDSpectrum;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import java.util.List;
import java.util.Set;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/** Run the "data" run configuration (./gradlew runData) to write src/generated/resources. */
@EventBusSubscriber(modid = DDSpectrum.MODID)
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Client assets
        generator.addProvider(event.includeClient(), new SpectrumItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new SpectrumBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new SpectrumLanguageProvider(output));

        // Server data
        generator.addProvider(event.includeServer(), new SpectrumRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(SpectrumBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider));
        SpectrumBlockTagsProvider blockTags = new SpectrumBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(),
                new SpectrumItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
    }
}
