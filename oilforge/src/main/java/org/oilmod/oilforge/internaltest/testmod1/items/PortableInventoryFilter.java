package org.oilmod.oilforge.internaltest.testmod1.items;

import org.oilmod.api.inventory.ItemFilter;
import org.oilmod.api.items.OilBukkitItemStack;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.itemstack.ItemStackRep;

public class PortableInventoryFilter implements ItemFilter {
    public final static PortableInventoryFilter INSTANCE = new PortableInventoryFilter();

    @Override
    public boolean allowed(ItemStackRep itemStack) {
        if (itemStack instanceof OilBukkitItemStack) {
            OilItemStack oilItemStack = ((OilBukkitItemStack) itemStack).getOilItemStack();
            return oilItemStack.getInventory() == null;
            //Will disallow you to add an Backpack to inventories using this filter
        }
        return true;
    }
}