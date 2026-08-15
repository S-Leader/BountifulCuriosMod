package keletu.bountifulcurios.compat;

import keletu.bountifulcurios.item.BaubleItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;

public final class CurioUtil {
    public static boolean isEquipped(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player).map(handler -> handler.findFirstCurio(item).isPresent()).orElse(false);
    }

    public static boolean isAnyEquipped(Player player, Item... items) {
        return Arrays.stream(items).anyMatch(item -> isEquipped(player, item));
    }

    public static boolean hasImmunity(Player player, MobEffect effect) {
        return CuriosApi.getCuriosInventory(player).map(handler -> !handler.findCurios(stack -> stack.getItem() instanceof BaubleItem bauble && bauble.isImmuneTo(effect)).isEmpty()).orElse(false);
    }

    public static boolean isHeld(Player player, Item... items) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        return Arrays.stream(items).anyMatch(item -> main.is(item) || off.is(item));
    }

    private CurioUtil() {
    }
}
