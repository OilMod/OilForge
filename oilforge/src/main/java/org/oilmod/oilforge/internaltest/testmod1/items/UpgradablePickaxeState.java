package org.oilmod.oilforge.internaltest.testmod1.items;

import org.oilmod.api.data.IntegerData;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;

public class UpgradablePickaxeState extends OilItemStack {
    public IntegerData intData;

    public UpgradablePickaxeState(NMSItemStack nmsItemStack, OilItem item) {
        super(nmsItemStack, item);
        intData = new IntegerData("upgrade", this);
    }


}
