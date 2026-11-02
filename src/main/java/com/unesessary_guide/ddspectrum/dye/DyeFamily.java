package com.unesessary_guide.ddspectrum.dye;

/**
 * The 15 color families plus the Key row of neutrals.
 *
 * Loader-neutral: no Minecraft or loader imports.
 */
public enum DyeFamily {

    RED("red", "Red"),
    ORANGE("orange", "Orange"),
    YELLOW("yellow", "Yellow"),
    LIME("lime", "Lime"),
    FOREST("forest", "Forest"),
    MINT("mint", "Mint"),
    CYAN("cyan", "Cyan"),
    SKY("sky", "Sky"),
    SAPPHIRE("sapphire", "Sapphire"),
    BLUE("blue", "Blue"),
    VIOLET("violet", "Violet"),
    PURPLE("purple", "Purple"),
    MAGENTA("magenta", "Magenta"),
    FUCHSIA("fuchsia", "Fuchsia"),
    ROSE("rose", "Rose"),
    KEY("key", "Key");

    private final String id;
    private final String displayName;

    DyeFamily(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /** Lowercase id used in dye ids and tags, e.g. "lime". */
    public String id() {
        return id;
    }

    /** Capitalized name, e.g. "Lime". */
    public String displayName() {
        return displayName;
    }

    /** True for the Key row, whose dyes have no variant. */
    public boolean isKey() {
        return this == KEY;
    }
}
