package org.oilmod.oilforge.rep.inventory;

import net.minecraft.inventory.IInventory;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

import static org.oilmod.oilforge.Util.toOil;

public class InventoryFR implements InventoryRep {
    private final IInventory forge;

    public InventoryFR(IInventory forge) {
        this.forge = forge;
    }

    public IInventory getForge() {
        return forge;
    }

    @Override
    public int getStorageSize() {
        return getForge().getSizeInventory();
    }

    @Override
    public int getTotalSize() {
        return getTotalSize();
    }

    @Override
    public ItemStackRep getStored(int i) {
        return toOil(getForge().getStackInSlot(i));
    }

    @Override
    public void setStored(int i, ItemStackRep stack) {
        getForge().setInventorySlotContents(i, ((ItemStackFR)stack).getForge());
    }

    @Override
    public boolean isNative() {
        return true;
    }
}
