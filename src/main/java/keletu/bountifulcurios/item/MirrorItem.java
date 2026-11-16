package keletu.bountifulcurios.item;

import keletu.bountifulcurios.wormhole.WormholeTeleportManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;
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
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        boolean wantsOtherPlayer = destination == Destination.OTHER_PLAYER
                || destination == Destination.SPAWN_OR_OTHER_WHEN_CROUCHING
                && player.isCrouching();
        if (wantsOtherPlayer && !hasAnotherPlayer(player)) {
            player.sendOverlayMessage(Component.translatable("item.bountifulcurios.potion_wormhole.nootherplayers"));
            return InteractionResult.FAIL;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            boolean toOther = destination == Destination.OTHER_PLAYER || (destination == Destination.SPAWN_OR_OTHER_WHEN_CROUCHING && player.isCrouching());
            if (toOther) {
                WormholeTeleportManager.openSelection(player);
                return stack;
            }
            boolean teleported = TeleportUtil.teleportToSpawn(player);
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
        return !(player instanceof ServerPlayer serverPlayer)
                || WormholeTeleportManager.hasTarget(serverPlayer);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return reusable ? 20 : 15;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return reusable ? ItemUseAnimation.NONE : ItemUseAnimation.DRINK;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable(getDescriptionId() + ".tooltip.0").withStyle(ChatFormatting.BLUE));
    }
}
