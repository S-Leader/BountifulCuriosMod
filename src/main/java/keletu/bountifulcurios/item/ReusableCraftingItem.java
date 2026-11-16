package keletu.bountifulcurios.item;

import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ReusableCraftingItem extends BCItem {
    public ReusableCraftingItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        if (instance instanceof ItemStack stack) return ItemStackTemplate.fromNonEmptyStack(stack.copyWithCount(1));
        if (instance instanceof ItemStackTemplate template) return template.withCount(1);
        return new ItemStackTemplate(this);
    }
}
