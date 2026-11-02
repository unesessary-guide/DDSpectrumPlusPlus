package com.unesessary_guide.ddspectrum.registry;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.dye.SpectrumDye;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DDSpectrum.MODID);

    /**
     * Shows all 140 dyes in swatch-grid order: family by family, variants left to right, then the Key row.
     * Anchors are the existing vanilla and Dye Depot items, so they are looked up instead of registered.
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPECTRUM = TABS.register("spectrum",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + DDSpectrum.MODID))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.dye(SpectrumDyes.byId("fuchsia")).get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        for (SpectrumDye dye : SpectrumDyes.ALL) {
                            Item item = dye.isNew()
                                    ? ModItems.dye(dye).get()
                                    : BuiltInRegistries.ITEM
                                            .getOptional(ResourceLocation.parse(dye.itemId(DDSpectrum.MODID)))
                                            .orElse(null);
                            if (item != null) {
                                output.accept(item);
                            }
                        }
                    })
                    .build());

    /** All blocks, grouped by type (wool, concrete, ...) and then in dye grid order, with the dye baskets last. */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = TABS.register("blocks",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + DDSpectrum.MODID + ".blocks"))
                    .withTabsBefore(SPECTRUM.getKey())
                    .icon(() -> ModBlocks.block(SpectrumBlockType.WOOL, SpectrumDyes.byId("fuchsia")).get().asItem().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        for (SpectrumBlockType type : SpectrumBlockType.values()) {
                            ModBlocks.all(type).forEach(block -> output.accept(block.get()));
                        }
                        ModBlocks.baskets().forEach(basket -> output.accept(basket.get()));
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
