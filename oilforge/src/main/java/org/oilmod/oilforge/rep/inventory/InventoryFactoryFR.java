package org.oilmod.oilforge.rep.inventory;

import net.minecraft.inventory.Inventory;
import org.oilmod.api.rep.inventory.InventoryFactory;
import org.oilmod.api.rep.inventory.InventoryRep;

public class InventoryFactoryFR extends InventoryFactory<InventoryFactoryFR> {
    @Override
    public InventoryRep createHeadlessInventory(int size) {
        return new InventoryFR(new Inventory(size),1 );
    }

    @Override
    public InventoryRep createHeadlessInventory(int height, int width) {
        return new InventoryFR(new Inventory(height*width),height);
    }
}
