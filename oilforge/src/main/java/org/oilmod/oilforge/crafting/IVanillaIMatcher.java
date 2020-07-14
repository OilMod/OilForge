package org.oilmod.oilforge.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import org.oilmod.api.rep.crafting.IMatcher;
import org.oilmod.api.util.checkstate.StateHolderFactory;
import org.oilmod.api.util.checkstate.immutable.ImmutableState;

public interface IVanillaIMatcher<C extends IInventory> extends IMatcher {
    StateHolderFactory<ImmutableState<SupplierCraftingInventory>, IVanillaIMatcher<?>> SUPPLIER_CRAFTING_INVENTORY =  (currentBackup, maxBackup, key) -> new ImmutableState<>(SupplierCraftingInventory.class);
    IRecipe<C> getRecipe();
}
