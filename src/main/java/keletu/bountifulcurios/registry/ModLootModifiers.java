package keletu.bountifulcurios.registry;

import com.mojang.serialization.Codec;
import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.loot.OriginalLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS,
                    BountifulCurios.MODID);

    public static final RegistryObject<Codec<OriginalLootModifier>> ORIGINAL_LOOT =
            SERIALIZERS.register("original_loot", () -> OriginalLootModifier.CODEC);

    private ModLootModifiers() {
    }
}
