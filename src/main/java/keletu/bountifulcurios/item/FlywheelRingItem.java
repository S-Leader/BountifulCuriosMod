package keletu.bountifulcurios.item;

import java.util.List;
import java.util.function.Consumer;
import keletu.bountifulcurios.registry.ModDataComponents;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public final class FlywheelRingItem extends BaubleItem {
    private final int capacity;

    public FlywheelRingItem(Properties properties, int capacity) {
        super(properties);
        this.capacity = capacity;
    }

    public static void registerEnergy(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.Energy.ITEM, (stack, access) -> {
            FlywheelRingItem ring = (FlywheelRingItem) stack.getItem();
            return new ItemAccessEnergyHandler(access, ModDataComponents.ENERGY.get(), ring.capacity, ring.capacity / 100) {
                @Override
                protected ItemResource update(ItemResource resource, int amount) {
                    CustomModelData modelData = new CustomModelData(List.of((float) amount / ring.capacity), List.of(), List.of(), List.of());
                    return super.update(resource, amount).with(DataComponents.CUSTOM_MODEL_DATA, modelData);
                }
            };
        }, ModItems.RING_FLYWHEEL.get(), ModItems.RING_FLYWHEEL_ADVANCED.get());
    }

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        super.curioTick(context, stack);
        if (!(context.entity() instanceof Player player) || player.level().isClientSide()) return;
        ItemAccess access = context.getItemAccess();
        if (access == null) return;
        EnergyHandler source = access.getCapability(Capabilities.Energy.ITEM);
        if (source == null || source.getAmountAsLong() <= 0) return;
        for (int slot = 0; slot < player.getInventory().getContainerSize() && source.getAmountAsLong() > 0; slot++) {
            transfer(source, ItemAccess.forPlayerSlot(player, slot));
        }
        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> inventory.getCurios().forEach((id, handler) -> {
            for (int slot = 0; slot < handler.getStacks().getSlots() && source.getAmountAsLong() > 0; slot++) {
                ItemAccess target = new SlotContext(id, player, slot, false, true).getItemAccess();
                if (target != null) transfer(source, target);
            }
        }));
    }

    private void transfer(EnergyHandler source, ItemAccess target) {
        if (target.getResource().isEmpty() || target.getResource().getItem() instanceof FlywheelRingItem) return;
        EnergyHandlerUtil.move(source, target.getCapability(Capabilities.Energy.ITEM), capacity / 100, null);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.ENERGY.get(), 0) < capacity;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.clamp(Math.round(13.0F * stack.getOrDefault(ModDataComponents.ENERGY.get(), 0) / capacity), 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) { return 0xD01010; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.literal(stack.getOrDefault(ModDataComponents.ENERGY.get(), 0) + "/" + capacity + " FE").withStyle(ChatFormatting.RED));
    }
}
