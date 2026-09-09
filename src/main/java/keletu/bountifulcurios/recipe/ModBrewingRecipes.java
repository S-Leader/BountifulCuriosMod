package keletu.bountifulcurios.recipe;

import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

public final class ModBrewingRecipes {
    public static void register() {
        ItemStack mundanePotion = PotionUtils.setPotion(
                new ItemStack(Items.POTION), Potions.MUNDANE);
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                StrictNBTIngredient.of(mundanePotion),
                Ingredient.of(Items.QUARTZ),
                new ItemStack(ModItems.POTION_RECALL.get())));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                Ingredient.of(ModItems.POTION_RECALL.get()),
                Ingredient.of(Items.ENDER_PEARL),
                new ItemStack(ModItems.POTION_WORMHOLE.get())));
    }

    private ModBrewingRecipes() {
    }
}
