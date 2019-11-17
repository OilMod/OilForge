package org.oilmod.oilforge.inventory.container.slot;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.oilmod.oilforge.inventory.OilIInventory;

public class OilSlot extends Slot {
    public OilSlot(OilIInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack) && inventory.isItemValidForSlot(slotNumber, stack);
    }
}
