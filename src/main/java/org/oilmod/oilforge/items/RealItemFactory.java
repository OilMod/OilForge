package org.oilmod.oilforge.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.internal.ItemFactory;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;

import static org.oilmod.oilforge.Util.toOil;

public class RealItemFactory extends ItemFactory {
    @Override
    public ItemStackRep createStack(OilItem oilItem, EntityHumanRep entityHumanRep, int count, int data) {
        ItemStack itemStack = new ItemStack((Item)oilItem.getNmsItem(), count);
        itemStack.setDamage(data);
        return toOil(itemStack);
    }
}
