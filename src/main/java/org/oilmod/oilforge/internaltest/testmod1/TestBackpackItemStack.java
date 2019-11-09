package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.inventory.InventoryHelper;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilBukkitItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;

public class TestBackpackItemStack extends OilItemStack {
    private ModInventoryObject inventory;

    public TestBackpackItemStack(NMSItemStack nmsItemStack, OilItem item) {
        super(nmsItemStack, item);
        inventory = InventoryFactory.getInstance().createBasicInventory("items",this, 16, item.getDisplayName(), BackpackItemFilter.INSTANCE, true);
    }


}
