package keletu.bountifulcurios.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.core.Holder;

import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaubleItem extends Item implements IImmuneBauble {

    private final Set<Holder<MobEffect>> immunities = new HashSet<>();
    private final List<AttributeEntry> attributes = new ArrayList<>();

    public BaubleItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public Set<Holder<MobEffect>> getImmunities() {
        return immunities;
    }

    @Override
    public List<AttributeEntry> getBaubleAttributes() {
        return attributes;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable(getDescriptionId() + ".tooltip.0").withStyle(ChatFormatting.BLUE));
    }
}
