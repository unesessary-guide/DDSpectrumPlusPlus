package com.unesessary_guide.ddspectrum.registry;

import com.unesessary_guide.ddspectrum.DDSpectrum;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, DDSpectrum.MODID);

    /**
     * The puff a dye basket makes. One type serves every dye: the server sends the dye's color as the particle's
     * "velocity" (count 0, speed 1), the way vanilla's note particle sends its color, and the client reads it back.
     */
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DYE_POOF =
            PARTICLES.register("dye_poof", () -> new SimpleParticleType(false));

    private ModParticles() {
    }

    public static void register(IEventBus modEventBus) {
        PARTICLES.register(modEventBus);
    }
}
