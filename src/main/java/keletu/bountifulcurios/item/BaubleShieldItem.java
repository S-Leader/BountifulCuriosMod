package keletu.bountifulcurios.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaubleShieldItem extends ShieldItem implements IImmuneBauble {
    private final Item repairItem;
    private final Set<MobEffect> immunities = new HashSet<>();
    private final List<AttributeEntry> attributes = new ArrayList<>();

    public BaubleShieldItem(Properties properties, Item repairItem) {
        super(properties);
        this.repairItem = repairItem;
    }

    @Override
    public Set<MobEffect> getImmunities() {
        return immunities;
    }

    @Override
    public List<AttributeEntry> getBaubleAttributes() {
        return attributes;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> result = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            for (AttributeEntry entry : getBaubleAttributes()) {
                result.put(entry.attribute(), new AttributeModifier(entry.id(), entry.name(), entry.amount(), entry.operation()));
            }
        }

        return result;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!level.isClientSide && entity instanceof LivingEntity player) {
            if (player.getMainHandItem() == stack || player.getOffhandItem() == stack) {
                getImmunities().forEach(player::removeEffect);
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(repairItem) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId(stack) + ".tooltip.0").withStyle(ChatFormatting.BLUE));
    }
}
