package keletu.bountifulcurios.registry;

import keletu.bountifulcurios.BountifulCurios;
import keletu.bountifulcurios.entity.FlareEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BountifulCurios.MODID);

    public static final RegistryObject<EntityType<FlareEntity>> FLARE = ENTITIES.register("flare",
            () -> EntityType.Builder.<FlareEntity>of(FlareEntity::new, MobCategory.MISC)
                    .sized(0.125F, 0.125F)
                    .clientTrackingRange(128)
                    .updateInterval(3)
                    .build(BountifulCurios.MODID + ":flare"));

    private ModEntities() {
    }
}
