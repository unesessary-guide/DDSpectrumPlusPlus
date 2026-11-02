package com.unesessary_guide.ddspectrum.datagen;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * One flat "item/generated" model per new dye, using textures/item/dye/{id}_dye.png.
 * Dyes without finished art use the 4x4 swatch texture from the sheet, so every dye has a texture.
 */
public class SpectrumItemModelProvider extends ItemModelProvider {
    public SpectrumItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DDSpectrum.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        SpectrumDyes.newDyes().forEach(dye ->
                withExistingParent(dye.itemPath(), mcLoc("item/generated"))
                        .texture("layer0", modLoc("item/dye/" + dye.itemPath())));
    }
}
