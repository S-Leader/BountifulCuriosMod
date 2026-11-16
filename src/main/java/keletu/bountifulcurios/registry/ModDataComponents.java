package keletu.bountifulcurios.registry;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import keletu.bountifulcurios.BountifulCurios;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, BountifulCurios.MODID);
    public static final Supplier<DataComponentType<Integer>> ENERGY = COMPONENTS.register("energy", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());

    private ModDataComponents() {
    }
}
