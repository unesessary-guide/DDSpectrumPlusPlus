package com.unesessary_guide.ddspectrum.command;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.unesessary_guide.ddspectrum.dye.SpectrumDye;
import com.unesessary_guide.ddspectrum.dye.SpectrumSheet;
import com.unesessary_guide.ddspectrum.registry.ModBlocks;
import com.unesessary_guide.ddspectrum.registry.SpectrumBlockType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * {@code /ddspectrum <type> [pos]}, for example {@code /ddspectrum wool}.
 *
 * Builds the swatch sheet (see {@link SpectrumSheet}) out of that block type, flat on the ground in front of you,
 * with the Key row nearest to you and the Red family at the far left of the top row. It covers all 140 dyes: the new
 * Spectrum++ blocks, and the existing vanilla and Dye Depot blocks for the 32 anchors. It replaces whatever blocks
 * are in the area, so it needs permission level 2.
 *
 * Without {@code pos} the floor layer is the block under your feet. With {@code pos}, that is the block position
 * the sheet is built around (centered, starting 2 blocks in front of it).
 */
public final class SpectrumCommand {
    /** How far in front of the origin the Key row starts, so you are not standing in the sheet. */
    private static final int FRONT_OFFSET = 2;

    private SpectrumCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("ddspectrum")
                .requires(source -> source.hasPermission(2));
        for (SpectrumBlockType type : SpectrumBlockType.values()) {
            root.then(Commands.literal(type.suffix())
                    .executes(context -> build(context, type, null))
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                            .executes(context -> build(context, type, BlockPosArgument.getBlockPos(context, "pos")))));
        }
        dispatcher.register(root);
    }

    private static int build(CommandContext<CommandSourceStack> context, SpectrumBlockType type, BlockPos explicit) {
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();
        Direction forward = source.getEntity() != null ? source.getEntity().getDirection() : Direction.NORTH;
        Direction right = forward.getClockWise();
        BlockPos origin = explicit != null ? explicit : BlockPos.containing(source.getPosition()).below();

        int placed = 0;
        Set<String> missing = new LinkedHashSet<>();
        for (SpectrumSheet.Swatch swatch : SpectrumSheet.swatches()) {
            Optional<Block> block = blockFor(type, swatch.dye());
            if (block.isEmpty()) {
                missing.add(swatch.dye().anchor() + "_" + type.suffix()); // only anchors can be missing
                continue;
            }
            BlockState state = block.get().defaultBlockState();
            if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
                // glazed terracotta: face the pattern toward the viewer, like placing it by hand
                state = state.setValue(HorizontalDirectionalBlock.FACING, forward.getOpposite());
            }
            for (int du = 0; du < SpectrumSheet.SWATCH; du++) {
                for (int dv = 0; dv < SpectrumSheet.SWATCH; dv++) {
                    BlockPos pos = origin
                            .relative(forward, FRONT_OFFSET + swatch.v() + dv)
                            .relative(right, swatch.u() + du - SpectrumSheet.WIDTH / 2);
                    level.setBlock(pos, state, Block.UPDATE_CLIENTS);
                    placed++;
                }
            }
        }

        int count = placed;
        source.sendSuccess(() -> Component.literal("Built the " + type.displayName() + " sheet: " + count + " blocks, "
                + SpectrumSheet.WIDTH + " wide and " + SpectrumSheet.DEPTH + " deep."), true);
        if (!missing.isEmpty()) {
            source.sendFailure(Component.literal("Skipped " + missing.size() + " swatch(es) with no block: "
                    + String.join(", ", missing)));
        }
        return count;
    }

    /** New dyes use Spectrum++'s own blocks. Anchors use the vanilla or Dye Depot block, e.g. minecraft:red_wool. */
    private static Optional<Block> blockFor(SpectrumBlockType type, SpectrumDye dye) {
        if (dye.isNew()) {
            return Optional.of(ModBlocks.block(type, dye).get());
        }
        return BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(dye.anchor() + "_" + type.suffix()));
    }
}
