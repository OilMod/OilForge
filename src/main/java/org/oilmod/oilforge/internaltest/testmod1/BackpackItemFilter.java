package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.inventory.ItemFilter;
import org.oilmod.api.items.OilBukkitItemStack;
import org.oilmod.api.rep.itemstack.ItemStackRep;

public class BackpackItemFilter implements ItemFilter {
    public static final BackpackItemFilter INSTANCE = new BackpackItemFilter();

    @Override
    public boolean allowed(ItemStackRep stack) {
        return !(stack instanceof OilBukkitItemStack && ((OilBukkitItemStack)stack).getOilItemStack() instanceof  TestBackpackItemStack);
    }
}
