package org.oilmod.oilforge.items.tickable;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.oilmod.oilforge.items.RealItem;
import org.oilmod.oilforge.items.RealItemImplHelper;

public class ContainerTicker extends Ticker<Container> {
    @Override
    public void processAll(World w, long now) {
        for(Container c:providers) {
            for(Slot s:c.inventorySlots) {
                TickableItemManager.dealWith(s.getStack(), now);
            }
        }
    }
}
