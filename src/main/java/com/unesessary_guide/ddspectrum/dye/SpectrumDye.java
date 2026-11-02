package com.unesessary_guide.ddspectrum.dye;

import java.util.Locale;

/**
 * One dye in the Spectrum++ grid.
 *
 * @param id       stable id path, e.g. "neon_lime", "fuchsia" (Base variant) or "charcoal" (Key). Used for tags,
 *                 textures and for registering new dyes (the item is "{id}_dye"). Anchors keep their existing item.
 * @param name     evocative display name, e.g. "Chartreuse"
 * @param family   the color family
 * @param variant  the variant, or {@code null} for Key dyes
 * @param color    RGB color, 0xRRGGBB
 * @param terracotta  RGB color of this dye on terracotta (a duller palette), 0xRRGGBB
 * @param anchor   the existing dye color id of a vanilla or Dye Depot dye whose name, color and id are unchanged
 *                 (e.g. "minecraft:red", "dye_depot:coral"), or {@code null} for a new dye. The dye's item is
 *                 this id plus "_dye" (e.g. "minecraft:red_dye").
 *
 * Loader-neutral: no Minecraft or loader imports.
 */
public record SpectrumDye(String id, String name, DyeFamily family, DyeVariant variant, int color, int terracotta,
                          String anchor) {

    /** True if this slot is an existing vanilla or Dye Depot dye. */
    public boolean isAnchor() {
        return anchor != null;
    }

    /** True if Spectrum++ has to register this dye. */
    public boolean isNew() {
        return anchor == null;
    }

    /** Systematic name, e.g. "Neon Lime". The Base variant and Key dyes use the family name alone. */
    public String systematicName() {
        if (variant == null) {
            return name;
        }
        return variant == DyeVariant.BASE ? family.displayName() : variant.displayName() + " " + family.displayName();
    }

    /** The terracotta color as "RRGGBB". */
    public String terracottaHex() {
        return String.format(Locale.ROOT, "%06X", terracotta);
    }

    /** The hex color as "RRGGBB". */
    public String hex() {
        return String.format(Locale.ROOT, "%06X", color);
    }

    /** Item path of a new dye, e.g. "neon_lime_dye". */
    public String itemPath() {
        return id + "_dye";
    }

    /**
     * The id of this dye's item in the game: the existing dye item for an anchor (e.g. "minecraft:red_dye"),
     * or {@code modId:id_dye} for a new dye.
     */
    public String itemId(String modId) {
        return anchor != null ? anchor + "_dye" : modId + ":" + itemPath();
    }
}
