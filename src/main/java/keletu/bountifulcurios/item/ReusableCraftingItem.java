package keletu.bountifulcurios.item;

import net.minecraft.world.item.ItemStack;

public class ReusableCraftingItem extends BCItem {
    public ReusableCraftingItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.copyWithCount(1);
    }
}
