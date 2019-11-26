package org.oilmod.oilforge.inventory.container.screen;

import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public class CustomRecipeBookGui extends RecipeBookGui {


    public void setupGhostRecipe(IRecipe<?> p_193951_1_, List<Slot> slots) {
        ItemStack itemstack = p_193951_1_.getRecipeOutput();
        this.ghostRecipe.setRecipe(p_193951_1_);
        //changed to use correct output slot and not a hardcoded one!
        this.ghostRecipe.addIngredient(Ingredient.fromStacks(itemstack), (slots.get(this.field_201522_g.getOutputSlot())).xPos, (slots.get(this.field_201522_g.getOutputSlot())).yPos);
        this.placeRecipe(this.field_201522_g.getWidth(), this.field_201522_g.getHeight(), this.field_201522_g.getOutputSlot(), p_193951_1_, p_193951_1_.getIngredients().iterator(), 0);
    }
}
