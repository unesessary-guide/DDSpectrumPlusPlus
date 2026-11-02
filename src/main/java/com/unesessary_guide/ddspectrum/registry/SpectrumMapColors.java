package com.unesessary_guide.ddspectrum.registry;

import net.minecraft.world.level.material.MapColor;

/** Picks the closest of vanilla's map colors for a dye color, so blocks look right on maps. */
public final class SpectrumMapColors {
    private SpectrumMapColors() {
    }

    public static MapColor nearest(int rgb) {
        MapColor best = MapColor.NONE;
        long bestDistance = Long.MAX_VALUE;
        for (int id = 1; id < 64; id++) {
            MapColor candidate = MapColor.byId(id);
            if (candidate == null || candidate == MapColor.NONE) {
                continue;
            }
            long dr = ((rgb >> 16) & 0xFF) - ((candidate.col >> 16) & 0xFF);
            long dg = ((rgb >> 8) & 0xFF) - ((candidate.col >> 8) & 0xFF);
            long db = (rgb & 0xFF) - (candidate.col & 0xFF);
            long distance = dr * dr + dg * dg + db * db;
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        return best;
    }
}
