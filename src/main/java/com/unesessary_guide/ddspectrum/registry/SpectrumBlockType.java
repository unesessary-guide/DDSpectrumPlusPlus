package com.unesessary_guide.ddspectrum.registry;

/**
 * The block families Spectrum++ adds for each new dye. Declaration order matters: concrete is registered before
 * concrete powder, and it is also the order blocks appear in the creative tab.
 */
public enum SpectrumBlockType {
    WOOL("wool", "Wool"),
    CONCRETE("concrete", "Concrete"),
    CONCRETE_POWDER("concrete_powder", "Concrete Powder"),
    TERRACOTTA("terracotta", "Terracotta"),
    GLAZED_TERRACOTTA("glazed_terracotta", "Glazed Terracotta"),
    STAINED_GLASS("stained_glass", "Stained Glass");

    private final String suffix;
    private final String displayName;

    SpectrumBlockType(String suffix, String displayName) {
        this.suffix = suffix;
        this.displayName = displayName;
    }

    /** Registry and texture suffix, e.g. "wool" gives "neon_lime_wool". */
    public String suffix() {
        return suffix;
    }

    /** English name suffix, e.g. "Glazed Terracotta". */
    public String displayName() {
        return displayName;
    }

    /** Vanilla's white version of this block's texture, used as a stand-in while a texture is missing. */
    public String vanillaTexture() {
        return "white_" + suffix;
    }
}
