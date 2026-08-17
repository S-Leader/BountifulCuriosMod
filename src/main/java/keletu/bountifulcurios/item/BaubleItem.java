package keletu.bountifulcurios.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaubleItem extends Item implements IImmuneBauble {

    private final Set<MobEffect> immunities = new HashSet<>();
    private final List<AttributeEntry> attributes = new ArrayList<>();

    public BaubleItem(Properties properties) {
        super(properties.stacksTo(1));
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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId(stack) + ".tooltip.0").withStyle(ChatFormatting.BLUE));
    }
}
