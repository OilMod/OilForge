package org.oilmod.oilforge.rep.inventory;

import net.minecraftforge.items.IItemHandler;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;

public class IItemHandlerInventoryFR implements InventoryRep {
    private final IItemHandler forge;

    public IItemHandlerInventoryFR(IItemHandler forge) {
        this.forge = forge;
    }

    public IItemHandler getForge() {
        return forge;
    }

    @Override
    public int getWidth() {
        return forge.getSlots();
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public ItemStackRep getStored(int slot) {
        return toOil(forge.getStackInSlot(slot));
    }

    @Override
    public void setStored(int slot, ItemStackRep stack) {
        forge.insertItem(slot, toForge(stack), false);
    }

    @Override
    public boolean isEmpty(int slot) {
        return forge.getStackInSlot(slot).isEmpty();
    }

    @Override
    public int getStack(int slot) {
        return forge.getStackInSlot(slot).getCount();
    }

    @Override
    public boolean isNative() {
        return false;
    }

    @Override
    public int getMaxStack(int slot) {
        return forge.getSlotLimit(slot);
    }
}
