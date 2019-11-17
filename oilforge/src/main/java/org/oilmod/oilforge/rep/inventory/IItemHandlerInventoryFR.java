package org.oilmod.oilforge.rep.inventory;

import net.minecraftforge.items.IItemHandler;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;

import static org.oilmod.oilforge.Util.*;

public class IItemHandlerInventoryFR implements InventoryRep {
    private final IItemHandler forge;

    public IItemHandlerInventoryFR(IItemHandler forge) {
        this.forge = forge;
    }

    public IItemHandler getForge() {
        return forge;
    }

    @Override
    public int getStorageSize() {
        return forge.getSlots();
    }

    @Override
    public int getTotalSize() {
        return forge.getSlots();
    }

    @Override
    public ItemStackRep getStored(int slot) {
        return toOil(forge.getStackInSlot(slot));
    }

    @Override
    public void setStored(int slot, ItemStackRep stack) {
        forge.insertItem(slot, toForge(stack), false);
    }
}
