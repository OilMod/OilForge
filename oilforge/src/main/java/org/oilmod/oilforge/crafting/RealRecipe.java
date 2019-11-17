package org.oilmod.oilforge.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public abstract class RealRecipe implements IRecipe {
    private final ResourceLocation id;
    private final String group;
    /** Is the ItemStack that you get when craft the recipe. */
    private final RealCraftingResult recipeOutput;
    private ItemStack recipeOutputStack = ItemStack.EMPTY; //must not be null
    /** Is a List of ItemStack that composes the recipe. */
    private final NonNullList<Ingredient> recipeItems;
    private final boolean isSimple;

    public RealRecipe(ResourceLocation idIn, String groupIn, RealCraftingResult recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        this.id = idIn;
        this.group = groupIn;
        this.recipeOutput = recipeOutputIn;
        this.recipeItems = recipeItemsIn;
        this.isSimple = recipeItemsIn.stream().allMatch(Ingredient::isSimple);
    }

    public int getSize() {
        return recipeItems.size();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializers.CRAFTING_SHAPELESS;
    }

    /**
     * Recipes with equal group are combined into one button in the recipe book
     */
    public String getGroup() {
        return this.group;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getRecipeOutput() {
        return this.recipeOutputStack;
    }

    public RealCraftingResult getRecipeResult() {
        return recipeOutput;
    }

    public void setRecipeOutput(ItemStack recipeOutputStack) {
        this.recipeOutputStack = recipeOutputStack;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

//Oil stuff

    protected abstract IngredientReference[] getIngredientReferences(IInventory inv);

    protected final NonNullList<ItemStack> getIngredientItemStacks(IInventory inv) {
        return refToStack(getIngredientReferences(inv));
    }
    protected final NonNullList<ItemStack> refToStack(IngredientReference[] refs) {
        NonNullList<ItemStack> result = NonNullList.withSize(refs.length, ItemStack.EMPTY);
        for (int i=0;i<refs.length;i++) {
            result.set(i, refs[i]==null?ItemStack.EMPTY:refs[i].get());
        }
        return result;
    }

}
