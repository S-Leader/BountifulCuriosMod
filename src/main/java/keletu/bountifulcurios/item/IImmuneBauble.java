package keletu.bountifulcurios.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;

public interface IImmuneBauble extends ICurioItem {
    Set<MobEffect> immunities = new HashSet<>();
    List<IImmuneBauble.AttributeEntry> attributes = new ArrayList<>();

    default IImmuneBauble immuneTo(MobEffect... effects) {
        immunities.addAll(List.of(effects));
        return this;
    }

    default IImmuneBauble withAttribute(Attribute attribute, UUID id, String name, double amount, AttributeModifier.Operation operation) {
        attributes.add(new BaubleItem.AttributeEntry(attribute, id, name, amount, operation));
        return this;
    }

    default boolean isImmuneTo(MobEffect effect) {
        return immunities.contains(effect);
    }

    @Override
    default boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!(entity instanceof Player player)) {
            return false;
        }
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(stack.getItem()).isEmpty()).orElse(true);
    }

    @Override
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID slotUuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> result = HashMultimap.create();
        for (AttributeEntry entry : attributes) {
            result.put(entry.attribute(), new AttributeModifier(entry.id(), entry.name(), entry.amount(), entry.operation()));
        }
        return result;
    }

    @Override
    default void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) {
            immunities.forEach(entity::removeEffect);
        }
    }

    record AttributeEntry(Attribute attribute, UUID id, String name, double amount,
                          AttributeModifier.Operation operation) {
    }
}
