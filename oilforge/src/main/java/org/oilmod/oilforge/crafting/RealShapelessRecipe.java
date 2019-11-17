package org.oilmod.oilforge.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;

public class RealShapelessRecipe extends RealRecipe{
    private final boolean isSimple;

    public RealShapelessRecipe(ResourceLocation idIn, String groupIn, RealCraftingResult recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
        this.isSimple = recipeItemsIn.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    protected IngredientReference[] getIngredientReferences(CraftingInventory inv) {
        throw new NotImplementedException("todo");
    }


    public IRecipeSerializer<?> getSerializer() {
        return IRecipeSerializer.CRAFTING_SHAPELESS; //todo probably better to make out own
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        if (false) {
            return false;
        } else {
            RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
            java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
            int i = 0;

            for(int j = 0; j < inv.getHeight(); ++j) {
                for(int k = 0; k < inv.getWidth(); ++k) {
                    ItemStack itemstack = inv.getStackInSlot(k + j * inv.getWidth());
                    if (!itemstack.isEmpty()) {
                        ++i;
                        if (isSimple)
                            recipeitemhelper.accountStack(new ItemStack(itemstack.getItem()));
                        else
                            inputs.add(itemstack);
                    }
                }
            }

            return i == this.getSize() && (isSimple ? recipeitemhelper.canCraft(this, null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.getIngredients()) != null);
        }
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return null;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        return null;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height) {
        return width * height >= this.getSize();
    }

}