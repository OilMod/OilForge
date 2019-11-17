package org.oilmod.oilforge.inventory;


import net.minecraft.item.ItemStack;

/**
 * Created by sirati97 on 13.02.2016.
 */
public interface FilteredInventory {
    boolean itemstackAddable(ItemStack itemStack);
}
