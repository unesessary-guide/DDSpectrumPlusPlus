package com.unesessary_guide.ddspectrum.dye;

import java.util.ArrayList;
import java.util.List;

/**
 * The layout of the in-world swatch sheet, as plain numbers (no Minecraft imports).
 *
 * <pre>
 *   - 15 color families in a 5 x 3 grid, each family a 3 x 3 grid of swatches in variant order
 *   - every swatch is 2 x 2 blocks, so a family is 6 x 6 blocks
 *   - families are 1 block apart, and the Key row sits 1 block below the families, centered, as one line
 * </pre>
 *
 * Coordinates: {@code u} counts blocks from the left edge as seen by the viewer, {@code v} counts blocks away from
 * the viewer, starting at the Key row (v = 0) and running up to the top family row.
 */
public final class SpectrumSheet {
    public static final int SWATCH = 2;
    public static final int GAP = 1;
    public static final int FAMILY_COLUMNS = 5;
    public static final int FAMILY_ROWS = 3;
    public static final int FAMILY_GRID = 3;

    /** A family is a 3 x 3 grid of 2 x 2 swatches: 6 x 6 blocks. */
    public static final int FAMILY_SIZE = FAMILY_GRID * SWATCH;
    public static final int WIDTH = FAMILY_COLUMNS * FAMILY_SIZE + (FAMILY_COLUMNS - 1) * GAP;
    public static final int DEPTH = SWATCH + GAP + FAMILY_ROWS * FAMILY_SIZE + (FAMILY_ROWS - 1) * GAP;

    /** One 2 x 2 swatch: its dye and its corner (nearest, leftmost block) in sheet coordinates. */
    public record Swatch(SpectrumDye dye, int u, int v) {
    }

    private SpectrumSheet() {
    }

    public static List<Swatch> swatches() {
        List<Swatch> out = new ArrayList<>();
        int family = 0;
        for (DyeFamily f : DyeFamily.values()) {
            if (f.isKey()) {
                continue;
            }
            int row = family / FAMILY_COLUMNS;
            int column = family % FAMILY_COLUMNS;
            List<SpectrumDye> dyes = SpectrumDyes.inFamily(f).toList();
            for (int i = 0; i < dyes.size(); i++) {
                int swatchRow = i / FAMILY_GRID;   // 0 = top of the family
                int swatchColumn = i % FAMILY_GRID;
                int u = column * (FAMILY_SIZE + GAP) + swatchColumn * SWATCH;
                int v = SWATCH + GAP + (FAMILY_ROWS - 1 - row) * (FAMILY_SIZE + GAP) + (FAMILY_GRID - 1 - swatchRow) * SWATCH;
                out.add(new Swatch(dyes.get(i), u, v));
            }
            family++;
        }
        List<SpectrumDye> key = SpectrumDyes.inFamily(DyeFamily.KEY).toList();
        int keyStart = (WIDTH - key.size() * SWATCH) / 2;
        for (int i = 0; i < key.size(); i++) {
            out.add(new Swatch(key.get(i), keyStart + i * SWATCH, 0));
        }
        return out;
    }
}
