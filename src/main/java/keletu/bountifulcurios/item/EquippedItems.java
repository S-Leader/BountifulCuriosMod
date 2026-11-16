package keletu.bountifulcurios.item;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BlocksAttacks;

public final class EquippedItems {
    public static Item.Properties shield(Item.Properties properties) {
        return properties.delayedComponent(DataComponents.BLOCKS_ATTACKS, context -> new BlocksAttacks(
                0.25F, 1.0F,
                List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                Optional.of(SoundEvents.SHIELD_BLOCK), Optional.of(SoundEvents.SHIELD_BREAK)));
    }

    private EquippedItems() {
    }
}
