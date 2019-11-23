package org.oilmod.oilforge.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.oilmod.oilforge.inventory.IItemFilter;

public class OilSlot extends Slot {
    private final IItemFilter filter;

    public OilSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, IItemFilter filter) {
        super(inventoryIn, index, xPosition, yPosition);
        this.filter = filter;
    }


    @Override
    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack) && filter.allowed(stack);
    }
}
