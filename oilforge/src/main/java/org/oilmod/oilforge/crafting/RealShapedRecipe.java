package org.oilmod.oilforge.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.oilmod.api.items.crafting.DataHolder;

public class RealShapedRecipe extends RealRecipe {
    private final static DataHolder dataHolder = new DataHolder(); //really only need one per threat

    /** How many horizontal slots this recipe is wide. */
    private final int recipeWidth;
    /** How many vertical slots this recipe uses. */
    private final int recipeHeight;

    public RealShapedRecipe(ResourceLocation idIn, String groupIn, NonNullList<Ingredient> recipeItemsIn, int recipeWidth, int recipeHeight,  RealCraftingResult recipeOut) {
        super(idIn, groupIn, recipeOut, recipeItemsIn);
        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
    }


    public IRecipeSerializer<?> getSerializer() {
        return IRecipeSerializer.CRAFTING_SHAPED; //todo probably better to make out own
    }


    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CraftingInventory inv, World worldIn) {
        if (false) {
            return false;
        } else {
            for(int i = 0; i <= inv.getWidth() - this.recipeWidth; ++i) {
                for(int j = 0; j <= inv.getHeight() - this.recipeHeight; ++j) {
                    if (this.checkMatch(inv, i, j, true)) {
                        return true;
                    }

                    if (this.checkMatch(inv, i, j, false)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkMatch(CraftingInventory craftingInventory, int offX, int offY, boolean mirror) {
        dataHolder.use();
        try {
            getIngredients().stream().filter(i -> i instanceof OilIngredient)
                    .forEach(i ->((OilIngredient) i).setCurrentDataHolder(dataHolder));

            //completelely normal matching from now on
            for(int i = 0; i < craftingInventory.getWidth(); ++i) {
                for(int j = 0; j < craftingInventory.getHeight(); ++j) {
                    int x = i - offX;
                    int y = j - offY;
                    Ingredient ingredient = Ingredient.EMPTY;
                    if (x >= 0 && y >= 0 && x < this.recipeWidth && y < this.recipeHeight) {
                        if (mirror) {
                            ingredient = this.getIngredients().get(this.recipeWidth - x - 1 + y * this.recipeWidth);
                        } else {
                            ingredient = this.getIngredients().get(x + y * this.recipeWidth);
                        }
                    }

                    if (!ingredient.test(craftingInventory.getStackInSlot(i + j * craftingInventory.getWidth()))) {
                        return false;
                    }
                }
            }

            return true;
        } finally {
            getIngredients().stream().filter(i -> i instanceof OilIngredient)
                    .forEach(i ->((OilIngredient) i).setCurrentDataHolder(null));
            dataHolder.dispose();
        }

    }


    //ItemCraftedEvent


    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        NonNullList<ItemStack> ingredients = getIngredientItemStacks(inv);
        ItemStack result = getRecipeResult().preCraftResult(ingredients, true, recipeWidth, recipeHeight);

        //no real clue how all this works with autocrafters etc. they might not have a player object etc, so lets just do it now
        //this was originally done because i did not see itemstacks as a mere data holder but as an actual object, in retrospect this might be a wrong design choice based on how most systems work but it allowed for a persistant oop instance to come with every itemstack
        //todo clone OilItemStack here or find reliant onCraft event
        getRecipeResult().craftResult(result, ingredients, true, recipeWidth, recipeHeight);

        //are those fake normal looking recipes still needed
        //NMS_OilShapedRecipe shapedRecipe = new NMS_OilShapedRecipe(width, height, ingredients, result, this);
        //inventoryCrafting.setCurrentRecipe(shapedRecipe); //set bukkit compatible recipe




        return result;


    }

    protected IngredientReference[] getIngredientReferences(CraftingInventory inv) {
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        for (int i = inv.getWidth()-1/*width*/; i >= 0 ; i--) {
            for (int j = inv.getHeight()-1/*height*/; j >= 0; j--) {
                if (!inv.getStackInSlot(i + j * inv.getWidth()).isEmpty()) {
                    if (left > i) {
                        left = i;
                    }
                    if (top > j) {
                        top = j;
                    }
                }
            }
        }
        int leftEnd = left + recipeHeight;
        int topEnd = top + recipeHeight;
        boolean mirror = checkMatch(inv, leftEnd, topEnd, true);
        int counter = 0;
        IngredientReference[] result = new IngredientReference[getSize()];
        for (int i = top; i <topEnd; ++i) {
            for (int j = left; j < leftEnd; ++j) {
                result[counter++] = new IngredientReference(j, i, mirror, inv, recipeWidth);
            }
        }
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= this.recipeWidth && height >= this.recipeHeight;
    }
}
