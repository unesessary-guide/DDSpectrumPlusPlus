package com.unesessary_guide.ddspectrum;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.unesessary_guide.ddspectrum.command.SpectrumCommand;
import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.ModCreativeTabs;
import com.unesessary_guide.ddspectrum.registry.ModItems;
import com.unesessary_guide.ddspectrum.registry.ModParticles;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DDSpectrum.MODID)
public class DDSpectrum {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dd_spectrum";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public DDSpectrum(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // The 108 new dyes, their blocks and the creative tabs are all driven by SpectrumDyes.
        // Blocks go first because they register their block items.
        ModBlocks.register(modEventBus);
        ModParticles.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModBlocks::registerFlammability);
        LOGGER.info("Dye Depot: Spectrum++ loaded with {} new dyes", ModItems.allDyes().size());
        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        SpectrumCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
