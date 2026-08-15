package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.effect.SinfulEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BountifulCurios.MODID);

    public static final RegistryObject<MobEffect> SINFUL =
            EFFECTS.register("sinful", SinfulEffect::new);

    private ModEffects() {
    }
}
