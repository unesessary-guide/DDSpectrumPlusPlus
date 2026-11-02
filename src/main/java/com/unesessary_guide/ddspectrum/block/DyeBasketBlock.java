package com.unesessary_guide.ddspectrum.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unesessary_guide.ddspectrum.registry.ModParticles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A basket of dye: a solid 16 x 16 x 16 block of one pure color that faces the player who places it. When something
 * lands on it or a projectile hits its top, it puffs out dye-colored particles (see DyePoofParticle). The color is the
 * dye's own RGB value, so it works for any Spectrum++ dye without needing a vanilla DyeColor.
 */
public class DyeBasketBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<DyeBasketBlock> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf("color").forGetter(DyeBasketBlock::getColor),
            propertiesCodec()
    ).apply(builder, DyeBasketBlock::new));

    private final int color;

    /** @param color the dye's RGB color, 0xRRGGBB */
    public DyeBasketBlock(int color, Properties properties) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public int getColor() {
        return color;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (level instanceof ServerLevel server && server.random.nextInt(10) == 0) {
            puff(server, entity.getX(), entity.getY() + 0.2, entity.getZ(), 2, 0.2);
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        super.onProjectileHit(level, state, hit, projectile);
        if (hit.getDirection() == Direction.UP) {
            poof(level, hit.getBlockPos());
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
        poof(level, pos);
    }

    private void poof(Level level, BlockPos pos) {
        if (level instanceof ServerLevel server) {
            server.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.5F, 0.8F);
            puff(server, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 28, 0.6);
        }
    }

    /**
     * Sends {@code count} particles scattered up to {@code spread} blocks around a point. Each is sent with count 0 so
     * that the three "speed" values are delivered as-is: they carry the dye color (0 to 1 each) to the client.
     */
    private void puff(ServerLevel level, double x, double y, double z, int count, double spread) {
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        for (int i = 0; i < count; i++) {
            level.sendParticles(ModParticles.DYE_POOF.get(),
                    x + (level.random.nextDouble() - 0.5) * 2.0 * spread,
                    y + level.random.nextDouble() * 0.3,
                    z + (level.random.nextDouble() - 0.5) * 2.0 * spread,
                    0, red, green, blue, 1.0);
        }
    }
}
