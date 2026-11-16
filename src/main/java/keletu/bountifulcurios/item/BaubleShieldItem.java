package keletu.bountifulcurios.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

public final class BaubleShieldItem extends ShieldItem implements IImmuneBauble {
    private final Set<Holder<MobEffect>> immunities = new HashSet<>();
    private final List<AttributeEntry> attributes = new ArrayList<>();

    public BaubleShieldItem(Properties properties, Item repairItem) {
        super(properties.repairable(repairItem));
    }

    @Override
    public Set<Holder<MobEffect>> getImmunities() { return immunities; }

    @Override
    public List<AttributeEntry> getBaubleAttributes() { return attributes; }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);
        if (owner instanceof LivingEntity entity && (entity.getMainHandItem() == stack || entity.getOffhandItem() == stack)) {
            immunities.forEach(entity::removeEffect);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable(getDescriptionId() + ".tooltip.0").withStyle(ChatFormatting.BLUE));
    }
}
