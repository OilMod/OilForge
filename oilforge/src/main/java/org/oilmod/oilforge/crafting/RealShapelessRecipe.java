package org.oilmod.oilforge.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RealShapelessRecipe extends RealRecipe{
    private final boolean isSimple;

    public RealShapelessRecipe(ResourceLocation idIn, String groupIn, RealCraftingResult recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
        this.isSimple = recipeItemsIn.stream().allMatch(Ingredient::isSimple);
    }

    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializers.CRAFTING_SHAPELESS;
    }

    @Override
    protected IngredientReference[] getIngredientReferences(IInventory inv) {
        return new IngredientReference[0];
    }


    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(IInventory inv, World worldIn) {
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

            return i == this.getSize() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.getIngredients()) != null);
        }
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
        return null;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height) {
        return width * height >= this.getSize();
    }

}