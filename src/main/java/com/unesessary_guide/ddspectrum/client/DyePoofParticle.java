package com.unesessary_guide.ddspectrum.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * The dye basket puff: a flat, pure color square (the sprite frames are plain white shapes, tinted to the dye's
 * color), fully bright so it has no shading. Each one steps slowly through the sprite frames over its life, spins at
 * its own random speed (some left, some right, some fast, some slow), falls slowly, and drifts sideways over a wide
 * area.
 */
public class DyePoofParticle extends TextureSheetParticle {
    private static final int FULL_BRIGHT = 0xF000F0;

    private final SpriteSet sprites;
    private final float spin;

    DyePoofParticle(ClientLevel level, double x, double y, double z, float red, float green, float blue, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;

        this.lifetime = 50 + this.random.nextInt(50);          // long life, so the frames change slowly
        this.quadSize = 0.12F + this.random.nextFloat() * 0.10F;
        this.spin = (this.random.nextFloat() - 0.5F) * 0.4F;   // radians per tick, each particle differs
        this.roll = this.random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;

        // a small pop upward, then a wide sideways spread
        this.xd = (this.random.nextFloat() - 0.5F) * 0.14F;
        this.yd = 0.02F + this.random.nextFloat() * 0.05F;
        this.zd = (this.random.nextFloat() - 0.5F) * 0.14F;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return FULL_BRIGHT;
    }

    @Override
    public float getQuadSize(float partialTick) {
        return this.quadSize;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.setSpriteFromAge(this.sprites);
        this.oRoll = this.roll;
        this.roll += this.spin;

        this.yd = Math.max(this.yd - 0.002D, -0.05D);
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.96D;
        this.zd *= 0.96D;
        if (this.onGround) {
            this.xd *= 0.5D;
            this.zd *= 0.5D;
        }
    }

    /** The server sends the dye color as the three "speed" values. */
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double red, double green, double blue) {
            return new DyePoofParticle(level, x, y, z, (float) red, (float) green, (float) blue, this.sprites);
        }
    }
}
