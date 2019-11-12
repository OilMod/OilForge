package org.oilmod.oilforge.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class IngredientReference {
    private final int left;
    private final int top;
    private final boolean mirror;
    private final IInventory inventory;
    private final int recipeWidth;

    public IngredientReference(int left, int top, boolean mirror, IInventory inventory, int recipeWidth) {
        this.left = left;
        this.top = top;
        this.mirror = mirror;
        this.inventory = inventory;
        this.recipeWidth = recipeWidth;
    }

    public int getIndex() {
        return mirror?recipeWidth - left - 1 + top * recipeWidth:left + top * recipeWidth;
    }

    public ItemStack get() {
        return inventory.getStackInSlot(getIndex() );
    }

    public void set(ItemStack itemStack, boolean update) {
        inventory.setInventorySlotContents(getIndex(), itemStack);
        if (update)inventory.markDirty();
    }
}