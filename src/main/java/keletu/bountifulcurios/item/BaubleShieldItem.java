package keletu.bountifulcurios.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BaubleShieldItem extends ShieldItem implements ICurioItem {
    private static final UUID KNOCKBACK_UUID =
            UUID.fromString("9016ba1d-70dd-46c4-b0b4-fc4ea39886c1");
    private final Item repairItem;

    public BaubleShieldItem(Properties properties, Item repairItem) {
        super(properties);
        this.repairItem = repairItem;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        return entity instanceof Player player && CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(stack.getItem()).isEmpty()).orElse(true);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            SlotContext slotContext, UUID slotUuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> result = HashMultimap.create();
        result.put(Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier(KNOCKBACK_UUID, "Bountiful shield knockback resistance",
                        1.0D, AttributeModifier.Operation.ADDITION));
        return result;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(repairItem) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
                                TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId(stack) + ".tooltip.0")
                .withStyle(ChatFormatting.BLUE));
    }
}
