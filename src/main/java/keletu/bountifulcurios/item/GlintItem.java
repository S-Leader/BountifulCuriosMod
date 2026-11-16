package keletu.bountifulcurios.item;

import net.minecraft.world.item.ItemStack;

public class GlintItem extends BCItem {
    public GlintItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
