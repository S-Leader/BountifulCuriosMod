package keletu.bountifulcurios.item;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CurioAttributeModifiers;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public interface IImmuneBauble extends ICurioItem {
    Set<Holder<MobEffect>> getImmunities();
    List<AttributeEntry> getBaubleAttributes();

    default IImmuneBauble immuneTo(Holder<MobEffect>... effects) {
        Collections.addAll(getImmunities(), effects);
        return this;
    }

    default IImmuneBauble withAttribute(Holder<Attribute> attribute, Identifier id, String name, double amount, AttributeModifier.Operation operation) {
        getBaubleAttributes().add(new AttributeEntry(attribute, id, amount, operation));
        return this;
    }

    default boolean isImmuneTo(Holder<MobEffect> effect) {
        return getImmunities().contains(effect);
    }

    @Override
    default boolean canEquip(SlotContext context, ItemStack stack) {
        return context.entity() instanceof Player player && CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(stack.getItem()).isEmpty()).orElse(true);
    }

    @Override
    default CurioAttributeModifiers getDefaultCurioAttributeModifiers(ItemStack stack) {
        var builder = CurioAttributeModifiers.builder();
        for (AttributeEntry entry : getBaubleAttributes()) {
            builder.addModifier(entry.attribute(), new AttributeModifier(entry.id(), entry.amount(), entry.operation()));
        }
        return builder.build();
    }

    @Override
    default void curioTick(SlotContext context, ItemStack stack) {
        if (!context.entity().level().isClientSide()) getImmunities().forEach(context.entity()::removeEffect);
    }

    record AttributeEntry(Holder<Attribute> attribute, Identifier id, double amount, AttributeModifier.Operation operation) {
    }
}
