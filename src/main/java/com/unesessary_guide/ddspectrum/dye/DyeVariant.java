package com.unesessary_guide.ddspectrum.dye;

/**
 * The nine variants of a color family, in the same order as the 3x3 swatch grid
 * (left to right, top to bottom). {@link #BASE} is the center cell.
 *
 * Loader-neutral: no Minecraft or loader imports.
 */
public enum DyeVariant {

    VIBRANT("vibrant", "Vibrant"),
    NEON("neon", "Neon"),
    RICH("rich", "Rich"),
    LIGHT("light", "Light"),
    BASE("base", "Base"),
    DARK("dark", "Dark"),
    PASTEL("pastel", "Pastel"),
    MUTED("muted", "Muted"),
    MURKY("murky", "Murky");

    private final String id;
    private final String displayName;

    DyeVariant(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /** Lowercase id used in dye ids and tags, e.g. "neon". */
    public String id() {
        return id;
    }

    /** Capitalized name, e.g. "Neon". */
    public String displayName() {
        return displayName;
    }
}
