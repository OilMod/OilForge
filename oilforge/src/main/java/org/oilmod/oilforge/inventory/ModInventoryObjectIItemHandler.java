package org.oilmod.oilforge.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.oilmod.api.inventory.ModInventoryObjectBase;

import javax.annotation.Nonnull;

import static org.oilmod.oilforge.Util.toForge;

public class ModInventoryObjectIItemHandler implements IItemHandler {
    private final ModInventoryObjectBase inv;

    public ModInventoryObjectIItemHandler(ModInventoryObjectBase inv) {
        this.inv = inv;
    }

    @Override
    public int getSlots() {
        return inv.getBukkitInventory().getStorageSize();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return toForge(inv.getBukkitInventory().getStored(slot));
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }
}
