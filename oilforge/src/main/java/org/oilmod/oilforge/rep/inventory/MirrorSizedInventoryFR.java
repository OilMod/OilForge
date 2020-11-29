package org.oilmod.oilforge.rep.inventory;

import net.minecraft.inventory.IInventory;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.inventory.MirrorSizedInventoryRep;

public class MirrorSizedInventoryFR extends InventoryFR implements MirrorSizedInventoryRep {
    private final InventoryRep mirroree;

    MirrorSizedInventoryFR(IInventory forge, int height, InventoryRep mirroree) {
        super(forge, height);
        this.mirroree = mirroree;
    }

    @Override
    public InventoryRep getMirroree() {
        return mirroree;
    }
}
