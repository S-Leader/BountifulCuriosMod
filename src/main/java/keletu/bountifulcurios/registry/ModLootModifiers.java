package keletu.bountifulcurios.registry;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.loot.OriginalLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BountifulCurios.MODID);
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ORIGINAL_LOOT = SERIALIZERS.register("original_loot", () -> OriginalLootModifier.CODEC);

    private ModLootModifiers() {
    }
}
