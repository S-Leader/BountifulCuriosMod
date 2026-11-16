package keletu.bountifulcurios.recipe;

import keletu.bountifulcurios.registry.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

public final class ModBrewingRecipes {
    public static void register(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack stack) {
                return stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.MUNDANE);
            }

            @Override
            public boolean isIngredient(ItemStack stack) { return stack.is(Items.QUARTZ); }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                return isInput(input) && isIngredient(ingredient) ? ModItems.POTION_RECALL.toStack() : ItemStack.EMPTY;
            }
        });
        event.getBuilder().addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack stack) { return stack.is(ModItems.POTION_RECALL.get()); }

            @Override
            public boolean isIngredient(ItemStack stack) { return stack.is(Items.ENDER_PEARL); }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                return isInput(input) && isIngredient(ingredient) ? ModItems.POTION_WORMHOLE.toStack() : ItemStack.EMPTY;
            }
        });
    }

    private ModBrewingRecipes() {
    }
}
