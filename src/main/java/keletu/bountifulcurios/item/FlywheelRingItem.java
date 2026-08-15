package keletu.bountifulcurios.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FlywheelRingItem extends BaubleItem {
    private final int capacity;

    public FlywheelRingItem(Properties properties, int capacity) {
        super(properties);
        this.capacity = capacity;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player && !player.level().isClientSide) {
            chargeInventory(stack, player);
        }
    }

    private void chargeInventory(ItemStack flywheel, Player player) {
        flywheel.getCapability(ForgeCapabilities.ENERGY).ifPresent(source -> {
            for (int slot = 0; slot < player.getInventory().getContainerSize()
                    && source.getEnergyStored() > 0; slot++) {
                transfer(source, player.getInventory().getItem(slot), flywheel);
            }
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                var curios = handler.getEquippedCurios();
                for (int slot = 0; slot < curios.getSlots() && source.getEnergyStored() > 0;
                     slot++) {
                    transfer(source, curios.getStackInSlot(slot), flywheel);
                }
            });
        });
    }

    private static void transfer(IEnergyStorage source, ItemStack targetStack,
                                 ItemStack flywheel) {
        if (targetStack.isEmpty() || targetStack == flywheel
                || targetStack.getItem() instanceof FlywheelRingItem) {
            return;
        }
        targetStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(target -> {
            int available = source.extractEnergy(Integer.MAX_VALUE, true);
            int accepted = target.receiveEnergy(available, false);
            if (accepted > 0) {
                source.extractEnergy(accepted, false);
            }
        });
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
                .map(storage -> storage.getEnergyStored() < storage.getMaxEnergyStored())
                .orElse(false);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
                .map(storage -> Math.round(13.0F * storage.getEnergyStored()
                        / storage.getMaxEnergyStored())).orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xD01010;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(storage -> tooltip.add(
                Component.literal(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored()
                        + " FE").withStyle(ChatFormatting.RED)));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnergyProvider(capacity);
    }

    private static final class EnergyProvider implements ICapabilitySerializable<Tag> {
        private final EnergyStorage storage;
        private final LazyOptional<IEnergyStorage> holder;

        private EnergyProvider(int capacity) {
            int transfer = Math.max(1, capacity / 100);
            storage = new EnergyStorage(capacity, transfer, transfer);
            holder = LazyOptional.of(() -> storage);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability,
                                                 @Nullable Direction side) {
            return capability == ForgeCapabilities.ENERGY ? holder.cast()
                    : LazyOptional.empty();
        }

        @Override
        public Tag serializeNBT() {
            return storage.serializeNBT();
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            storage.deserializeNBT(nbt);
        }
    }
}
