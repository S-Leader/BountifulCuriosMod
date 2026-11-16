package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.entity.FlareEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, BountifulCurios.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<FlareEntity>> FLARE = ENTITIES.register("flare", () -> EntityType.Builder.<FlareEntity>of(FlareEntity::new, MobCategory.MISC).sized(0.125F, 0.125F).clientTrackingRange(8).updateInterval(3).build(ResourceKey.create(Registries.ENTITY_TYPE, BountifulCurios.id("flare"))));

    private ModEntities() {
    }
}
