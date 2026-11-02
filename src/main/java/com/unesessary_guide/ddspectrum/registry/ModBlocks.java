package com.unesessary_guide.ddspectrum.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.block.DyeBasketBlock;
import com.unesessary_guide.ddspectrum.dye.SpectrumDye;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers every block type for each of the 108 new dyes (6 x 108 = 648 blocks), plus a dye basket for each dye
 * (108 more), and all their block items.
 * Properties are copied from vanilla's white block of the same type, so hardness, sounds, tool requirements and
 * the like match vanilla. Only the map color changes.
 */
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DDSpectrum.MODID);

    private static final Map<SpectrumBlockType, Map<String, DeferredBlock<Block>>> BY_TYPE =
            new EnumMap<>(SpectrumBlockType.class);

    /** Dye baskets, keyed by dye id. They are not a SpectrumBlockType: they have their own block class and recipe. */
    private static final Map<String, DeferredBlock<Block>> BASKETS = new LinkedHashMap<>();

    static {
        for (SpectrumBlockType type : SpectrumBlockType.values()) {
            BY_TYPE.put(type, new LinkedHashMap<>());
        }
        SpectrumDyes.newDyes().forEach(dye -> {
            for (SpectrumBlockType type : SpectrumBlockType.values()) {
                register(type, dye);
            }
            registerBasket(dye);
        });
    }

    private ModBlocks() {
    }

    private static void register(SpectrumBlockType type, SpectrumDye dye) {
        String name = dye.id() + "_" + type.suffix();
        // Terracotta has its own, duller palette; everything else follows the dye color.
        MapColor mapColor = SpectrumMapColors.nearest(type == SpectrumBlockType.TERRACOTTA ? dye.terracotta() : dye.color());

        DeferredBlock<Block> block = switch (type) {
            case WOOL -> BLOCKS.register(name, () -> new Block(props(Blocks.WHITE_WOOL, mapColor)));
            case CONCRETE -> BLOCKS.register(name, () -> new Block(props(Blocks.WHITE_CONCRETE, mapColor)));
            case CONCRETE_POWDER -> BLOCKS.register(name, () ->
                    new ConcretePowderBlock(block(SpectrumBlockType.CONCRETE, dye).get(), props(Blocks.WHITE_CONCRETE_POWDER, mapColor)));
            case TERRACOTTA -> BLOCKS.register(name, () -> new Block(props(Blocks.WHITE_TERRACOTTA, mapColor)));
            case GLAZED_TERRACOTTA -> BLOCKS.register(name, () -> new GlazedTerracottaBlock(props(Blocks.WHITE_GLAZED_TERRACOTTA, mapColor)));
            case STAINED_GLASS -> BLOCKS.register(name, () -> new TransparentBlock(props(Blocks.WHITE_STAINED_GLASS, mapColor)));
        };
        BY_TYPE.get(type).put(dye.id(), block);
        ModItems.ITEMS.registerSimpleBlockItem(block);
    }

    private static void registerBasket(SpectrumDye dye) {
        MapColor mapColor = SpectrumMapColors.nearest(dye.color());
        DeferredBlock<Block> basket = BLOCKS.register(dye.id() + "_dye_basket",
                () -> new DyeBasketBlock(dye.color(), props(Blocks.WHITE_WOOL, mapColor)));
        BASKETS.put(dye.id(), basket);
        ModItems.ITEMS.registerSimpleBlockItem(basket);
    }

    private static BlockBehaviour.Properties props(Block vanillaWhite, MapColor mapColor) {
        return BlockBehaviour.Properties.ofFullCopy(vanillaWhite).mapColor(mapColor);
    }

    /** The block of this type for a new dye. Throws if the dye is an anchor. */
    public static DeferredBlock<Block> block(SpectrumBlockType type, SpectrumDye dye) {
        DeferredBlock<Block> block = BY_TYPE.get(type).get(dye.id());
        if (block == null) {
            throw new IllegalArgumentException(dye.id() + " is an anchor dye and has no Spectrum++ " + type.suffix());
        }
        return block;
    }

    public static Collection<DeferredBlock<Block>> all(SpectrumBlockType type) {
        return Collections.unmodifiableCollection(BY_TYPE.get(type).values());
    }

    /** The dye basket for a new dye. Throws if the dye is an anchor. */
    public static DeferredBlock<Block> basket(SpectrumDye dye) {
        DeferredBlock<Block> basket = BASKETS.get(dye.id());
        if (basket == null) {
            throw new IllegalArgumentException(dye.id() + " is an anchor dye and has no Spectrum++ dye basket");
        }
        return basket;
    }

    public static Collection<DeferredBlock<Block>> baskets() {
        return Collections.unmodifiableCollection(BASKETS.values());
    }

    /** Every block Spectrum++ registers: all block types plus the dye baskets. */
    public static Stream<DeferredBlock<Block>> allBlocks() {
        return Stream.concat(BY_TYPE.values().stream().flatMap(m -> m.values().stream()), BASKETS.values().stream());
    }

    /** Wool burns like vanilla wool (spread 30, flammability 60). Call from common setup, inside enqueueWork. */
    public static void registerFlammability() {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        all(SpectrumBlockType.WOOL).forEach(block -> fire.setFlammable(block.get(), 30, 60));
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
