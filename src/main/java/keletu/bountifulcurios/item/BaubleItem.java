package keletu.bountifulcurios.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.*;

public class BaubleItem extends Item implements ICurioItem {
    private final Set<MobEffect> immunities = new HashSet<>();
    private final List<AttributeEntry> attributes = new ArrayList<>();

    public BaubleItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public BaubleItem immuneTo(MobEffect... effects) {
        immunities.addAll(List.of(effects));
        return this;
    }

    public BaubleItem withAttribute(Attribute attribute, UUID id, String name, double amount,
                                    AttributeModifier.Operation operation) {
        attributes.add(new AttributeEntry(attribute, id, name, amount, operation));
        return this;
    }

    public boolean isImmuneTo(MobEffect effect) {
        return immunities.contains(effect);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!(entity instanceof Player player)) {
            return false;
        }
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(stack.getItem()).isEmpty())
                .orElse(true);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            SlotContext slotContext, UUID slotUuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> result = HashMultimap.create();
        for (AttributeEntry entry : attributes) {
            result.put(entry.attribute(), new AttributeModifier(entry.id(), entry.name(),
                    entry.amount(), entry.operation()));
        }
        return result;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) {
            immunities.forEach(entity::removeEffect);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
                                TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId(stack) + ".tooltip.0")
                .withStyle(ChatFormatting.BLUE));
    }

    private record AttributeEntry(Attribute attribute, UUID id, String name, double amount,
                                  AttributeModifier.Operation operation) {
    }
}
