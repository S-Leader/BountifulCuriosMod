package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.effect.SinfulEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, BountifulCurios.MODID);
    public static final DeferredHolder<MobEffect, SinfulEffect> SINFUL = EFFECTS.register("sinful", SinfulEffect::new);

    private ModEffects() {
    }
}
