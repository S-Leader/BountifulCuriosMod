package keletu.bountifulcurios;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class LootConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue BOGGED_BEZOAR;
    public static final ModConfigSpec.DoubleValue PARCHED_APPLE;
    public static final ModConfigSpec.DoubleValue WARDEN_VITAMIN_A;
    public static final ModConfigSpec.DoubleValue OMINOUS_SPAWNER;
    public static final ModConfigSpec.DoubleValue OMINOUS_BASE;
    public static final ModConfigSpec.DoubleValue OMINOUS_CANDLE;
    public static final ModConfigSpec.DoubleValue OMINOUS_SILT;
    public static final ModConfigSpec.DoubleValue NORMAL_VAULT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("new_loot");
        BOGGED_BEZOAR = builder.defineInRange("bogged_bezoar", 0.05, 0.0, 1.0);
        PARCHED_APPLE = builder.defineInRange("parched_forbidden_fruit", 0.025, 0.0, 1.0);
        WARDEN_VITAMIN_A = builder.defineInRange("warden_vitamin_a", 0.05, 0.0, 1.0);
        OMINOUS_SPAWNER = builder.defineInRange("ominous_spawner_accessory", 0.20, 0.0, 1.0);
        OMINOUS_BASE = builder.defineInRange("ominous_vault_base_accessory", 0.01, 0.0, 1.0);
        OMINOUS_CANDLE = builder.defineInRange("ominous_vault_water_candle", 0.03, 0.0, 1.0);
        OMINOUS_SILT = builder.defineInRange("ominous_vault_spectral_silt", 0.05, 0.0, 1.0);
        NORMAL_VAULT = builder.defineInRange("normal_vault_accessory", 0.05, 0.0, 1.0);
        builder.pop();
        SPEC = builder.build();
    }

    private LootConfig() {
    }
}
