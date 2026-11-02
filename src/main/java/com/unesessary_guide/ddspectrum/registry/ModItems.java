package com.unesessary_guide.ddspectrum.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.dye.SpectrumDye;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers the 108 new Spectrum++ dyes, driven entirely by {@link SpectrumDyes}.
 * Anchors (existing vanilla and Dye Depot dyes) are not registered here.
 */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DDSpectrum.MODID);

    private static final Map<String, DeferredItem<Item>> DYES = new LinkedHashMap<>();

    static {
        SpectrumDyes.newDyes().forEach(dye ->
                DYES.put(dye.id(), ITEMS.registerSimpleItem(dye.itemPath(), new Item.Properties())));
    }

    private ModItems() {
    }

    /** The registered item for a new dye. Throws if the dye is an anchor. */
    public static DeferredItem<Item> dye(SpectrumDye dye) {
        DeferredItem<Item> item = DYES.get(dye.id());
        if (item == null) {
            throw new IllegalArgumentException(dye.id() + " is an anchor dye and is not registered by Spectrum++");
        }
        return item;
    }

    /** All registered Spectrum++ dye items, in grid order. */
    public static Collection<DeferredItem<Item>> allDyes() {
        return Collections.unmodifiableCollection(DYES.values());
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
