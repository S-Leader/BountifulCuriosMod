package keletu.bountifulcurios.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BaubleShieldItem extends ShieldItem implements IImmuneBauble {
    private final Item repairItem;

    public BaubleShieldItem(Properties properties, Item repairItem) {
        super(properties);
        this.repairItem = repairItem;
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
