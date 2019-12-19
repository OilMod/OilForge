package org.oilmod.oilforge.rep.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.ChestTileEntity;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.inventory.InventoryView;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.oilforge.inventory.OilIInventory;
import org.oilmod.oilforge.inventory.OilInventoryChest;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;

import static org.oilmod.oilforge.Util.toOil;

public class InventoryFR implements InventoryRep {
    private final IInventory forge;
    private final int height;

    public static InventoryFR createInventory(OilIInventory<?> forge) {
        if (forge instanceof OilInventoryChest) {
            return new InventoryFR(forge, ((OilInventoryChest) forge).getRows());
        }
        return new InventoryFR(forge, 1); //now a 2d oil inventory!
    }

    public static InventoryFR createInventory(IInventory forge) {
        if (forge instanceof OilIInventory) {
            throw new IllegalArgumentException("please get inventory directly from OilIInventory");
        }

        //todo see if we can do better than this. this obviously does not support 2d modded invs of different proportions and has no protection against false positives!
        int size = forge.getSizeInventory();
        if (size%9 == 0) {
            return new InventoryFR(forge, size/9);
        } else if (size%5 == 0) {
            return new InventoryFR(forge, size/5);
        } else if (size%3 == 0) {
            return new InventoryFR(forge, size/3);
        } else {
            return new InventoryFR(forge, 0); //we dont know, lets make it 1d
        }
    }

    InventoryFR(IInventory forge, int height) {
        this.forge = forge;
        this.height = height;
    }

    public IInventory getForge() {
        return forge;
    }

    @Override
    public int getWidth() {
        return is2d()?getSize()/height:getSize();
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getSize() {
        return getForge().getSizeInventory();
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
