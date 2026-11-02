package com.unesessary_guide.ddspectrum.dye;

import static com.unesessary_guide.ddspectrum.dye.DyeFamily.RED;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.ORANGE;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.YELLOW;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.LIME;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.FOREST;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.MINT;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.CYAN;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.SKY;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.SAPPHIRE;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.BLUE;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.VIOLET;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.PURPLE;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.MAGENTA;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.FUCHSIA;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.ROSE;
import static com.unesessary_guide.ddspectrum.dye.DyeFamily.KEY;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.VIBRANT;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.NEON;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.RICH;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.LIGHT;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.BASE;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.DARK;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.PASTEL;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.MUTED;
import static com.unesessary_guide.ddspectrum.dye.DyeVariant.MURKY;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The common list of Spectrum++ dyes: 15 families x 9 variants, plus the Key row (140 dyes).
 * 28 family slots and 4 Key slots are anchors (existing vanilla or Dye Depot dyes), so 108 dyes are new.
 *
 * This is the single source of truth: registration, tinting, lang, recipes and tags should all be driven by it.
 * It is loader-neutral, with no Minecraft or loader imports, so it can move into a shared "common" module as-is.
 *
 * Order: family by family, variants in swatch-grid order, then the Key row.
 */
public final class SpectrumDyes {

    public static final List<SpectrumDye> ALL = List.of(
        // Red
        dye("vibrant_red", "Scarlet", RED, VIBRANT, 0xF2322A, 0x9B3D2B, null),
        dye("neon_red", "Crimson", RED, NEON, 0xCC0D11, 0x852D1D, null),
        dye("rich_red", "Burgundy", RED, RICH, 0x7B0A1F, 0x581A14, null),
        dye("light_red", "Coral", RED, LIGHT, 0xDF7758, 0x975640, "dye_depot:coral"),
        dye("red", "Red", RED, BASE, 0xB02E26, 0x8E3C2E, "minecraft:red"),
        dye("dark_red", "Maroon", RED, DARK, 0x7B2713, 0x5A2311, "dye_depot:maroon"),
        dye("pastel_red", "Salmon", RED, PASTEL, 0xD98880, 0x975F51, null),
        dye("muted_red", "Brick", RED, MUTED, 0xA0503F, 0x723C2B, null),
        dye("murky_red", "Mahogany", RED, MURKY, 0x602D27, 0x4C2217, null),
        // Orange
        dye("vibrant_orange", "Marigold", ORANGE, VIBRANT, 0xFFAE42, 0xAE7545, null),
        dye("neon_orange", "Blaze", ORANGE, NEON, 0xFF6208, 0xA5512D, null),
        dye("rich_orange", "Rust", ORANGE, RICH, 0xB5501A, 0x7C3E21, null),
        dye("light_orange", "Apricot", ORANGE, LIGHT, 0xFFBB91, 0xB17D62, null),
        dye("orange", "Orange", ORANGE, BASE, 0xF9801D, 0x9F5224, "minecraft:orange"),
        dye("dark_orange", "Ginger", ORANGE, DARK, 0xCF6121, 0x8C4929, "dye_depot:ginger"),
        dye("pastel_orange", "Fawn", ORANGE, PASTEL, 0xF0CDA8, 0xA66B49, null),
        dye("muted_orange", "Tan", ORANGE, MUTED, 0xF49C5D, 0x916649, "dye_depot:tan"),
        dye("murky_orange", "Brown", ORANGE, MURKY, 0x835432, 0x4C3223, "minecraft:brown"),
        // Yellow
        dye("vibrant_yellow", "Lemon", YELLOW, VIBRANT, 0xFFF04D, 0xBA9955, null),
        dye("neon_yellow", "Mimosa", YELLOW, NEON, 0xEAC41C, 0xA88040, null),
        dye("rich_yellow", "Mustard", YELLOW, RICH, 0xBE9F13, 0x8D6831, null),
        dye("light_yellow", "Canary", YELLOW, LIGHT, 0xFFE98A, 0xB99666, null),
        dye("yellow", "Yellow", YELLOW, BASE, 0xFED83D, 0xBA8524, "minecraft:yellow"),
        dye("dark_yellow", "Amber", YELLOW, DARK, 0xD7AF00, 0x9B7336, "dye_depot:amber"),
        dye("pastel_yellow", "Flax", YELLOW, PASTEL, 0xF7EFC9, 0xB89A7F, null),
        dye("muted_yellow", "Beige", YELLOW, MUTED, 0xE1D5A3, 0xA9896B, "dye_depot:beige"),
        dye("murky_yellow", "Khaki", YELLOW, MURKY, 0xBDAE77, 0x907151, null),
        // Lime
        dye("vibrant_lime", "Chartreuse", LIME, VIBRANT, 0x9FFA0A, 0x979B47, null),
        dye("neon_lime", "Kiwi", LIME, NEON, 0x8CDD04, 0x88893C, null),
        dye("rich_lime", "Fern", LIME, RICH, 0x5B900C, 0x625B23, null),
        dye("light_lime", "Spring", LIME, LIGHT, 0xA5EF55, 0x969550, null),
        dye("lime", "Lime", LIME, BASE, 0x80C71F, 0x677535, "minecraft:lime"),
        dye("dark_lime", "Green", LIME, DARK, 0x5E7C16, 0x4C522A, "minecraft:green"),
        dye("pastel_lime", "Pistachio", LIME, PASTEL, 0xC1E2A5, 0x9E8F6C, null),
        dye("muted_lime", "Sage", LIME, MUTED, 0x9BBB80, 0x867754, null),
        dye("murky_lime", "Olive", LIME, MURKY, 0x8C8F2A, 0x745C2C, "dye_depot:olive"),
        // Forest
        dye("vibrant_forest", "Shamrock", FOREST, VIBRANT, 0x27D10C, 0x6E8135, null),
        dye("neon_forest", "Clover", FOREST, NEON, 0x24BA0F, 0x65732E, null),
        dye("rich_forest", "Evergreen", FOREST, RICH, 0x0A6B35, 0x444422, null),
        dye("light_forest", "Meadow", FOREST, LIGHT, 0x6AD75E, 0x7D854B, null),
        dye("forest", "Forest", FOREST, BASE, 0x32A326, 0x5E652C, "dye_depot:forest"),
        dye("dark_forest", "Verdant", FOREST, DARK, 0x255714, 0x3F3812, "dye_depot:verdant"),
        dye("pastel_forest", "Celadon", FOREST, PASTEL, 0xA2CF9C, 0x8E8363, null),
        dye("muted_forest", "Moss", FOREST, MUTED, 0x7E9B5E, 0x726340, null),
        dye("murky_forest", "Spruce", FOREST, MURKY, 0x436C3E, 0x4F4528, null),
        // Mint
        dye("vibrant_mint", "Emerald", MINT, VIBRANT, 0x3BE38A, 0x85957A, null),
        dye("neon_mint", "Spearmint", MINT, NEON, 0x00E784, 0x768E5A, null),
        dye("rich_mint", "Jade", MINT, RICH, 0x06894D, 0x505632, null),
        dye("light_mint", "Seafoam", MINT, LIGHT, 0x69F6A2, 0x88986B, null),
        dye("mint", "Mint", MINT, BASE, 0x38CE7D, 0x718053, "dye_depot:mint"),
        dye("dark_mint", "Teal", MINT, DARK, 0x2F7B67, 0x59633B, "dye_depot:teal"),
        dye("pastel_mint", "Honeydew", MINT, PASTEL, 0xBBF3CD, 0xA1997E, null),
        dye("muted_mint", "Eucalyptus", MINT, MUTED, 0x76AB88, 0x746C53, null),
        dye("murky_mint", "Juniper", MINT, MURKY, 0x538665, 0x5D563D, null),
        // Cyan
        dye("vibrant_cyan", "Aqua", CYAN, VIBRANT, 0x5EF0CC, 0x728578, "dye_depot:aqua"),
        dye("neon_cyan", "Lagoon", CYAN, NEON, 0x09B1B1, 0x637064, null),
        dye("rich_cyan", "Viridian", CYAN, RICH, 0x026262, 0x414035, null),
        dye("light_cyan", "Surf", CYAN, LIGHT, 0x4EC2C2, 0x727A6F, null),
        dye("cyan", "Cyan", CYAN, BASE, 0x169C9C, 0x575C5C, "minecraft:cyan"),
        dye("dark_cyan", "Verdigris", CYAN, DARK, 0x2A7F7A, 0x504F3B, null),
        dye("pastel_cyan", "Frost", CYAN, PASTEL, 0x8CB7B7, 0x80756A, null),
        dye("muted_cyan", "Patina", CYAN, MUTED, 0x679291, 0x685E53, null),
        dye("murky_cyan", "Harbor", CYAN, MURKY, 0x3F6968, 0x4E443A, null),
        // Sky
        dye("vibrant_sky", "Turquoise", SKY, VIBRANT, 0x4DE3DC, 0x85938F, null),
        dye("neon_sky", "Celeste", SKY, NEON, 0x08C9FB, 0x6F8088, null),
        dye("rich_sky", "Ocean", SKY, RICH, 0x0A7390, 0x4A4B4C, null),
        dye("light_sky", "Breeze", SKY, LIGHT, 0x70D9FF, 0x848A8D, null),
        dye("sky", "Sky", SKY, BASE, 0x3AB3DA, 0x706C8A, "minecraft:light_blue"),
        dye("dark_sky", "Cerulean", SKY, DARK, 0x0586A8, 0x525759, null),
        dye("pastel_sky", "Pearl", SKY, PASTEL, 0xA5D2E3, 0x918782, null),
        dye("muted_sky", "Fog", SKY, MUTED, 0x77A2B3, 0x736965, null),
        dye("murky_sky", "Storm", SKY, MURKY, 0x568191, 0x5E5450, null),
        // Sapphire
        dye("vibrant_sapphire", "Azure", SAPPHIRE, VIBRANT, 0x369BFE, 0x636883, null),
        dye("neon_sapphire", "Cobalt", SAPPHIRE, NEON, 0x0E7BD9, 0x51556E, null),
        dye("rich_sapphire", "Admiral", SAPPHIRE, RICH, 0x012E58, 0x2A222A, null),
        dye("light_sapphire", "Cornflower", SAPPHIRE, LIGHT, 0x3D8FE3, 0x5F6075, null),
        dye("sapphire", "Sapphire", SAPPHIRE, BASE, 0x0D6ABB, 0x494A5E, null),
        dye("dark_sapphire", "Lapis", SAPPHIRE, DARK, 0x034884, 0x383441, null),
        dye("pastel_sapphire", "Denim", SAPPHIRE, PASTEL, 0x6E8FB4, 0x6B5E62, null),
        dye("muted_sapphire", "Slate", SAPPHIRE, MUTED, 0x4C5E86, 0x504046, "dye_depot:slate"),
        dye("murky_sapphire", "Navy", SAPPHIRE, MURKY, 0x153D64, 0x342B31, "dye_depot:navy"),
        // Blue
        dye("vibrant_blue", "Ultramarine", BLUE, VIBRANT, 0x555EF1, 0x584B76, null),
        dye("neon_blue", "Klein", BLUE, NEON, 0x2A3CCB, 0x3F3861, null),
        dye("rich_blue", "Midnight", BLUE, RICH, 0x140165, 0x23152D, null),
        dye("light_blue", "Periwinkle", BLUE, LIGHT, 0x5B69D2, 0x5C4C6A, null),
        dye("blue", "Blue", BLUE, BASE, 0x3C44AA, 0x4C3E5C, "minecraft:blue"),
        dye("dark_blue", "Fjord", BLUE, DARK, 0x1E167D, 0x2D1F39, null),
        dye("pastel_blue", "Steel", BLUE, PASTEL, 0x6872A0, 0x614D55, null),
        dye("muted_blue", "Aegean", BLUE, MUTED, 0x3F4872, 0x45333A, null),
        dye("murky_blue", "Abyss", BLUE, MURKY, 0x1B2047, 0x2A1A21, null),
        // Violet
        dye("vibrant_violet", "Amethyst", VIOLET, VIBRANT, 0x815FD6, 0x69496D, null),
        dye("neon_violet", "Iris", VIOLET, NEON, 0x5A33A7, 0x4E3052, null),
        dye("rich_violet", "Eclipse", VIOLET, RICH, 0x370176, 0x351937, null),
        dye("light_violet", "Wisteria", VIOLET, LIGHT, 0x7863B9, 0x654860, null),
        dye("violet", "Violet", VIOLET, BASE, 0x574092, 0x4E3349, null),
        dye("dark_violet", "Indigo", VIOLET, DARK, 0x331E57, 0x351B29, "dye_depot:indigo"),
        dye("pastel_violet", "Heather", VIOLET, PASTEL, 0x817AA0, 0x6D5258, null),
        dye("muted_violet", "Shadow", VIOLET, MUTED, 0x3C3456, 0x3D262B, null),
        dye("murky_violet", "Nightshade", VIOLET, MURKY, 0x1C1433, 0x261117, null),
        // Purple
        dye("vibrant_purple", "Grape", PURPLE, VIBRANT, 0xBB38FE, 0x834480, null),
        dye("neon_purple", "Phlox", PURPLE, NEON, 0x9108CA, 0x693064, null),
        dye("rich_purple", "Aubergine", PURPLE, RICH, 0x470066, 0x3D1630, null),
        dye("light_purple", "Lilac", PURPLE, LIGHT, 0xAE5AE0, 0x7E4B74, null),
        dye("purple", "Purple", PURPLE, BASE, 0x8932B8, 0x7A4958, "minecraft:purple"),
        dye("dark_purple", "Tyrian", PURPLE, DARK, 0x5C0482, 0x491E3F, null),
        dye("pastel_purple", "Lavender", PURPLE, PASTEL, 0x9A78B1, 0x785460, null),
        dye("muted_purple", "Thistle", PURPLE, MUTED, 0x76558C, 0x603E4A, null),
        dye("murky_purple", "Plum", PURPLE, MURKY, 0x533468, 0x492935, null),
        // Magenta
        dye("vibrant_magenta", "Orchid", MAGENTA, VIBRANT, 0xFE61F1, 0xA85782, null),
        dye("neon_magenta", "Fandango", MAGENTA, NEON, 0xDA1CCF, 0x903C6D, null),
        dye("rich_magenta", "Mulberry", MAGENTA, RICH, 0x82067B, 0x5E213F, null),
        dye("light_magenta", "Peony", MAGENTA, LIGHT, 0xF075E5, 0xA25D7D, null),
        dye("magenta", "Magenta", MAGENTA, BASE, 0xC74EBD, 0x95576C, "minecraft:magenta"),
        dye("dark_magenta", "Cassis", MAGENTA, DARK, 0x981B90, 0x6B2B4B, null),
        dye("pastel_magenta", "Petunia", MAGENTA, PASTEL, 0xCD97C6, 0x956870, null),
        dye("muted_magenta", "Mauve", MAGENTA, MUTED, 0x9D6A97, 0x764B53, null),
        dye("murky_magenta", "Damson", MAGENTA, MURKY, 0x754570, 0x5C343C, null),
        // Fuchsia
        dye("vibrant_fuchsia", "Taffy", FUCHSIA, VIBRANT, 0xFD3FA8, 0xA34760, null),
        dye("neon_fuchsia", "Cerise", FUCHSIA, NEON, 0xEC1C75, 0x983947, null),
        dye("rich_fuchsia", "Beet", FUCHSIA, RICH, 0x820551, 0x5D1D2C, null),
        dye("light_fuchsia", "Flamingo", FUCHSIA, LIGHT, 0xFD64B2, 0xA65466, null),
        dye("fuchsia", "Fuchsia", FUCHSIA, BASE, 0xD92D8E, 0x8F3A50, null),
        dye("dark_fuchsia", "Raspberry", FUCHSIA, DARK, 0x9E0263, 0x6C2436, null),
        dye("pastel_fuchsia", "Cameo", FUCHSIA, PASTEL, 0xCE84A5, 0x925D60, null),
        dye("muted_fuchsia", "Lotus", FUCHSIA, MUTED, 0xB76F90, 0x835053, null),
        dye("murky_fuchsia", "Merlot", FUCHSIA, MURKY, 0x6F2D45, 0x552526, null),
        // Rose
        dye("vibrant_rose", "Rouge", ROSE, VIBRANT, 0xFF7487, 0xA85A55, null),
        dye("neon_rose", "Roseate", ROSE, NEON, 0xFF3D6E, 0xA34447, null),
        dye("rich_rose", "Garnet", ROSE, RICH, 0xB40648, 0x78282C, null),
        dye("light_rose", "Pink", ROSE, LIGHT, 0xF38BAA, 0xA04D4E, "minecraft:pink"),
        dye("rose", "Rose", ROSE, BASE, 0xFF5E64, 0xA54F45, "dye_depot:rose"),
        dye("dark_rose", "Cranberry", ROSE, DARK, 0xCA2958, 0x853337, null),
        dye("pastel_rose", "Carnation", ROSE, PASTEL, 0xF5ABB5, 0xAB756E, null),
        dye("muted_rose", "Dahlia", ROSE, MUTED, 0xC37D87, 0x8B5751, null),
        dye("murky_rose", "Rosewood", ROSE, MURKY, 0xA5626D, 0x784641, null),
        // Key
        key("white", "White", 0xF9FFFE, 0xD1B1A1, "minecraft:white"),
        key("ash", "Ash", 0x9D9D97, 0x876B62, "minecraft:light_gray"),
        key("gray", "Gray", 0x474F52, 0x392923, "minecraft:gray"),
        key("charcoal", "Charcoal", 0x323639, 0x39251D, null),
        key("black", "Black", 0x1D1D21, 0x251610, "minecraft:black")
    );

    private static final Map<String, SpectrumDye> BY_ID = index();

    private SpectrumDyes() {
    }

    /** Looks up a dye by its id path (e.g. "neon_lime"), or returns {@code null}. */
    public static SpectrumDye byId(String id) {
        return BY_ID.get(id);
    }

    /** Dyes that Spectrum++ must register (108). */
    public static Stream<SpectrumDye> newDyes() {
        return ALL.stream().filter(SpectrumDye::isNew);
    }

    /** Existing vanilla or Dye Depot dyes that keep their id (32). */
    public static Stream<SpectrumDye> anchors() {
        return ALL.stream().filter(SpectrumDye::isAnchor);
    }

    /** All dyes in one family, in grid order. */
    public static Stream<SpectrumDye> inFamily(DyeFamily family) {
        return ALL.stream().filter(d -> d.family() == family);
    }

    /** All dyes of one variant across the families. */
    public static Stream<SpectrumDye> ofVariant(DyeVariant variant) {
        return ALL.stream().filter(d -> d.variant() == variant);
    }

    private static SpectrumDye dye(String id, String name, DyeFamily family, DyeVariant variant, int color, int terracotta,
                                   String anchor) {
        return new SpectrumDye(id, name, family, variant, color, terracotta, anchor);
    }

    private static SpectrumDye key(String id, String name, int color, int terracotta, String anchor) {
        return new SpectrumDye(id, name, KEY, null, color, terracotta, anchor);
    }

    private static Map<String, SpectrumDye> index() {
        Map<String, SpectrumDye> map = new LinkedHashMap<>();
        for (SpectrumDye dye : ALL) {
            if (map.put(dye.id(), dye) != null) {
                throw new IllegalStateException("Duplicate dye id: " + dye.id());
            }
        }
        return Map.copyOf(map);
    }
}
