package org.oilmod.oilforge.crafting;

import net.minecraft.inventory.IInventory;
import org.oilmod.api.rep.crafting.ICraftingState;
import org.oilmod.api.rep.crafting.IResult;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.util.checkstate.ICheckState;

import static org.oilmod.oilforge.Util.toOil;

public class VanillaRecipeResult<C extends IInventory> implements IResult {
    private final IVanillaIMatcher<C> matcher;

    public VanillaRecipeResult(IVanillaIMatcher<C> matcher) {
        this.matcher = matcher;
    }

    @Override
    public ItemStackRep getResult(ICraftingState state, ICheckState checkState) {
        SupplierCraftingInventory craftInv = checkState.getTag(matcher, matcher.SUPPLIER_CRAFTING_INVENTORY).get();
        return toOil(matcher.getRecipe().getCraftingResult((C) craftInv));
    }
}
