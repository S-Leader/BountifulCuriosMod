package keletu.bountifulcurios.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MirrorItem extends Item {
    public enum Destination {SPAWN, OTHER_PLAYER, SPAWN_OR_OTHER_WHEN_CROUCHING}

    private final Destination destination;
    private final boolean reusable;

    public MirrorItem(Properties properties, Destination destination, boolean reusable) {
        super(properties.stacksTo(reusable ? 1 : 16));
        this.destination = destination;
        this.reusable = reusable;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (destination == Destination.OTHER_PLAYER && !hasAnotherPlayer(player)) {
            player.displayClientMessage(Component.translatable("item.bountifulcurios.potion_wormhole.nootherplayers"), true);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            boolean toOther = destination == Destination.OTHER_PLAYER || (destination == Destination.SPAWN_OR_OTHER_WHEN_CROUCHING && player.isCrouching());
            boolean teleported = toOther ? TeleportUtil.teleportToAnotherPlayer(player) : TeleportUtil.teleportToSpawn(player);
            if (teleported && !reusable && !player.getAbilities().instabuild) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                    player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
                }
            }
        }
        return stack;
    }

    private boolean hasAnotherPlayer(Player player) {
        return !(player instanceof ServerPlayer serverPlayer) || serverPlayer.server.getPlayerList().getPlayerCount() > 1;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return reusable ? 20 : 15;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return reusable ? UseAnim.NONE : UseAnim.DRINK;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId(stack) + ".tooltip.0").withStyle(ChatFormatting.BLUE));
    }
}
