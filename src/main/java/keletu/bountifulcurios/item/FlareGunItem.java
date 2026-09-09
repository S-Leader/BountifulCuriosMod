package keletu.bountifulcurios.item;

import keletu.bountifulcurios.entity.FlareEntity;
import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlareGunItem extends BCItem {
    public FlareGunItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gun = player.getItemInHand(hand);
        ItemStack ammo = findAmmo(player);
        if (ammo.isEmpty() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(gun);
        }
        if (!level.isClientSide) {
            FlareEntity flare = new FlareEntity(level, player);
            flare.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    0.0F, 1.05F, 0.0F);
            level.addFreshEntity(flare);
            level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 0.8F, 1.1F);
            if (!player.getAbilities().instabuild) {
                ammo.shrink(1);
            }
            player.getCooldowns().addCooldown(this, 6);
        }
        return InteractionResultHolder.sidedSuccess(gun, level.isClientSide);
    }

    private static ItemStack findAmmo(Player player) {
        if (player.getOffhandItem().is(ModItems.FLARE_RED.get())) {
            return player.getOffhandItem();
        }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.FLARE_RED.get())) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
