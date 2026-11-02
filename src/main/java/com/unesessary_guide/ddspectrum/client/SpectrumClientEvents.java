package com.unesessary_guide.ddspectrum.client;

import com.unesessary_guide.ddspectrum.DDSpectrum;
import com.unesessary_guide.ddspectrum.registry.ModParticles;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = DDSpectrum.MODID, value = Dist.CLIENT)
public final class SpectrumClientEvents {
    private SpectrumClientEvents() {
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.DYE_POOF.get(), DyePoofParticle.Provider::new);
    }
}
