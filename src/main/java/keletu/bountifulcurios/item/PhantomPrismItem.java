package keletu.bountifulcurios.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicBoolean;

public class PhantomPrismItem extends BCItem {
    public PhantomPrismItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player,
                                                  InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                AtomicBoolean anyRendered = new AtomicBoolean(false);
                handler.getCurios().values().forEach(curio -> {
                    for (int slot = 0; slot < curio.getSlots(); slot++) {
                        if (!curio.getStacks().getStackInSlot(slot).isEmpty()
                                && curio.getRenders().get(slot)) {
                            anyRendered.set(true);
                        }
                    }
                });
                boolean render = !anyRendered.get();
                handler.getCurios().values().forEach(curio -> {
                    for (int slot = 0; slot < curio.getSlots(); slot++) {
                        if (!curio.getStacks().getStackInSlot(slot).isEmpty()) {
                            curio.getRenders().set(slot, render);
                        }
                    }
                    handler.getUpdatingInventories().add(curio);
                });
                player.displayClientMessage(Component.translatable(render
                                ? "message.bountifulcurios.prism.visible"
                                : "message.bountifulcurios.prism.hidden").withStyle(ChatFormatting.LIGHT_PURPLE),
                        true);
            });
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
