package com.unesessary_guide.ddspectrum.datagen;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.dye.SpectrumDyes;
import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.ModItems;
import com.unesessary_guide.ddspectrum.registry.SpectrumBlockType;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** en_us.json: tab titles, the config screen, "{Name} Dye" for each new dye, and "{Name} Wool" and so on for blocks. */
public class SpectrumLanguageProvider extends LanguageProvider {
    public SpectrumLanguageProvider(PackOutput output) {
        super(output, DDSpectrum.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + DDSpectrum.MODID, "Dye Depot: Spectrum++");
        add("itemGroup." + DDSpectrum.MODID + ".blocks", "Dye Depot: Spectrum++ Blocks");

        SpectrumDyes.newDyes().forEach(dye -> {
            addItem(ModItems.dye(dye), dye.name() + " Dye");
            for (SpectrumBlockType type : SpectrumBlockType.values()) {
                addBlock(ModBlocks.block(type, dye), dye.name() + " " + type.displayName());
            }
            addBlock(ModBlocks.basket(dye), dye.name() + " Dye Basket");
        });

        // Config screen
        String c = DDSpectrum.MODID + ".configuration.";
        add(c + "title", "Dye Depot: Spectrum++ Configs");
        add(c + "section." + DDSpectrum.MODID + ".common.toml", "Dye Depot: Spectrum++ Configs");
        add(c + "section." + DDSpectrum.MODID + ".common.toml.title", "Dye Depot: Spectrum++ Configs");
        add(c + "items", "Item List");
        add(c + "logDirtBlock", "Log Dirt Block");
        add(c + "magicNumberIntroduction", "Magic Number Text");
        add(c + "magicNumber", "Magic Number");
    }
}
