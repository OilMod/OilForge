package org.oilmod.oilforge.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipe;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.crafting.IIngredientSupplier;
import org.oilmod.api.rep.crafting.IMatcher;
import org.oilmod.api.rep.crafting.IPartialCheckState;
import org.oilmod.api.rep.itemstack.ItemStackConsumerRep;
import org.oilmod.api.util.checkstate.ICheckState;
import org.oilmod.api.util.checkstate.StateHolderFactory;
import org.oilmod.api.util.checkstate.immutable.ImmutableState;

public class VanillaShapedMatcher implements IVanillaIMatcher<CraftingInventory> {
    private ShapedRecipe recipe;


    @Override
    public boolean isShaped() {
        return true;
    }

    @Override
    public boolean check(IIngredientSupplier supplier, ICheckState checkState) {
        SupplierCraftingInventory craftInv;
        if (supplier.getNMS() == null) {
            craftInv = new SupplierCraftingInventory(supplier);
            supplier.setNMS(craftInv);
        } else {
            craftInv = (SupplierCraftingInventory) supplier.getNMS();
        }
        boolean result = recipe.matches(craftInv, null);
        if (result) {
            checkState.getTag(this, SUPPLIER_CRAFTING_INVENTORY).set(craftInv);
            checkState.confirmState();
        }
        return result;
    }

    @Override
    public boolean checkPartial(IIngredientSupplier supplier, IPartialCheckState checkState) {
        throw new NotImplementedException("todo");
    }

    @Override
    public int getInputWidth() {
        return recipe.getRecipeWidth();
    }

    @Override
    public int getInputHeight() {
        return recipe.getRecipeHeight();
    }

    @Override
    public int getInputSize() {
        return getInputHeight()*getInputWidth();
    }

    @Override
    public int process(IIngredientSupplier supplier, ICheckState checkState, ItemStackConsumerRep stackConsumer, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
