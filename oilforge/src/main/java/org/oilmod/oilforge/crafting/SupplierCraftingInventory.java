package org.oilmod.oilforge.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.crafting.IIngredientSupplier;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

public class SupplierCraftingInventory extends CraftingInventory {
    private final IIngredientSupplier ingredientSupplier;

    public SupplierCraftingInventory(IIngredientSupplier ingredientSupplier) {
        super(null, 0, 0); //fake values
        this.ingredientSupplier = ingredientSupplier;
    }

    public int getSizeInventory() {
        return ingredientSupplier.getSuppliedHeight() * ingredientSupplier.getSuppliedWidth();
    }

    public boolean isEmpty() {
        for (int top = 0; top < getHeight(); top++) {
            for (int left = 0; left < getWidth(); left++) {
                if(!ingredientSupplier.getSupplied(left, top).isEmpty()) return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : ((ItemStackFR)ingredientSupplier.getSupplied(index)).getForge();
    }

    public ItemStack removeStackFromSlot(int index) {

        throw new NotImplementedException("todo");
        //return ItemStackHelper.getAndRemove(this.stackList, index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return getStackInSlot(index).split(count);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        throw new NotImplementedException("todo");
        //this.stackList.set(index, stack);
    }

    public void markDirty() {
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    public void clear() {
        throw new NotImplementedException("todo");
    }

    public int getHeight() {
        return ingredientSupplier.getSuppliedHeight() ;
    }

    public int getWidth() {
        return ingredientSupplier.getSuppliedWidth();
    }

    public void fillStackedContents(RecipeItemHelper helper) {
        /*for(ItemStack itemstack : this.stackList) {
            helper.accountPlainStack(itemstack);
        }*/

    }
}
