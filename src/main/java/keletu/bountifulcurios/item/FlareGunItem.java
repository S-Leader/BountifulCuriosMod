package keletu.bountifulcurios.item;

import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            ItemStack rocket = createRedRocket();
            FireworkRocketEntity flare = new FireworkRocketEntity(level, rocket, player, player.getX(), player.getEyeY() - 0.15D, player.getZ(), true);
            flare.setDeltaMovement(player.getLookAngle().scale(1.35D));
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

    private static ItemStack createRedRocket() {
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        CompoundTag fireworks = rocket.getOrCreateTagElement("Fireworks");
        fireworks.putByte("Flight", (byte) 1);
        CompoundTag explosion = new CompoundTag();
        explosion.putByte("Type", (byte) 0);
        explosion.putBoolean("Trail", true);
        explosion.putIntArray("Colors", new int[]{0xB02E26});
        ListTag explosions = new ListTag();
        explosions.add(explosion);
        fireworks.put("Explosions", explosions);
        return rocket;
    }
}
