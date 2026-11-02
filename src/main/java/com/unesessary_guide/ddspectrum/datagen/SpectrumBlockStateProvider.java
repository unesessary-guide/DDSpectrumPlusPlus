package com.unesessary_guide.ddspectrum.datagen;

import java.util.ArrayList;
import java.util.List;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.dye.SpectrumDye;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;
import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.SpectrumBlockType;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Block states, block models and block item models for all 648 blocks.
 * Textures are looked up under textures/block/, in the type's folder first (wool/, concrete/, concrete_powder/,
 * terracotta/, glazed_terracotta/, stained_glass/ or glass/), as either {dye}_{type}.png (neon_lime_wool.png) or
 * {dye}.png (neon_lime.png), and then directly in textures/block/. If none exists (glazed terracotta for now), the
 * model falls back to vanilla's white texture and a warning is logged, so datagen keeps working. Add the texture
 * and run datagen again.
 */
public class SpectrumBlockStateProvider extends BlockStateProvider {
    private static final ExistingFileHelper.ResourceType TEXTURE =
            new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    private final ExistingFileHelper helper;

    public SpectrumBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DDSpectrum.MODID, existingFileHelper);
        this.helper = existingFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        SpectrumDyes.newDyes().forEach(dye -> {
            for (SpectrumBlockType type : SpectrumBlockType.values()) {
                generate(type, dye);
            }
            generateBasket(dye);
        });
    }

    private void generate(SpectrumBlockType type, SpectrumDye dye) {
        Block block = ModBlocks.block(type, dye).get();
        String name = dye.id() + "_" + type.suffix();
        ResourceLocation texture = texture(type, dye, name);

        switch (type) {
            case GLAZED_TERRACOTTA -> {
                ModelFile model = models()
                        .withExistingParent(name, ResourceLocation.withDefaultNamespace("template_glazed_terracotta"))
                        .texture("pattern", texture);
                horizontalBlock(block, model, 180);
                simpleBlockItem(block, model);
            }
            case STAINED_GLASS -> simpleBlockWithItem(block, models().cubeAll(name, texture).renderType("translucent"));
            default -> simpleBlockWithItem(block, models().cubeAll(name, texture));
        }
    }

    /**
     * A full 16 x 16 x 16 cube using one texture on every face, at textures/block/dye_basket/{dye}_dye_basket.png
     * (a plain solid color). Shading and ambient occlusion are switched off, so every face is the same pure color.
     * If that texture is missing, the basket borrows the dye's wool texture.
     */
    private void generateBasket(SpectrumDye dye) {
        Block block = ModBlocks.basket(dye).get();
        String name = dye.id() + "_dye_basket";
        ResourceLocation texture = basketTexture(dye, name);

        BlockModelBuilder model = models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("block/block")))
                .texture("particle", texture)
                .texture("all", texture)
                .ao(false);
        model.element().from(0, 0, 0).to(16, 16, 16).shade(false)
                .allFaces((direction, face) -> face.texture("#all").cullface(direction)).end();

        horizontalBlock(block, model, 180);
        simpleBlockItem(block, model);
    }

    private ResourceLocation basketTexture(SpectrumDye dye, String name) {
        for (String path : List.of("block/dye_basket/" + name, "block/dye_basket/" + dye.id(), "block/" + name)) {
            ResourceLocation candidate = ResourceLocation.fromNamespaceAndPath(DDSpectrum.MODID, path);
            if (helper.exists(candidate, TEXTURE)) {
                return candidate;
            }
        }
        DDSpectrum.LOGGER.warn("No dye basket texture for {}, using its wool texture as a stand-in", name);
        return texture(SpectrumBlockType.WOOL, dye, dye.id() + "_wool");
    }

    private ResourceLocation texture(SpectrumBlockType type, SpectrumDye dye, String name) {
        List<String> folders = type == SpectrumBlockType.STAINED_GLASS
                ? List.of("stained_glass", "glass")
                : List.of(type.suffix());
        List<String> candidates = new ArrayList<>();
        for (String folder : folders) {
            candidates.add("block/" + folder + "/" + name);
            candidates.add("block/" + folder + "/" + dye.id());
        }
        candidates.add("block/" + name);

        for (String path : candidates) {
            ResourceLocation candidate = ResourceLocation.fromNamespaceAndPath(DDSpectrum.MODID, path);
            if (helper.exists(candidate, TEXTURE)) {
                return candidate;
            }
        }
        DDSpectrum.LOGGER.warn("Missing texture for {} (looked for {}), using vanilla {} instead", name, candidates,
                type.vanillaTexture());
        return ResourceLocation.withDefaultNamespace("block/" + type.vanillaTexture());
    }
}
