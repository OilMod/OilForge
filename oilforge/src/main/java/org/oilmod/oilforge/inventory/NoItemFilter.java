package org.oilmod.oilforge.inventory;


import net.minecraft.item.ItemStack;

/**
 * Created by sirati97 on 13.02.2016.
 */
public class NoItemFilter implements IItemFilter {
    public static final NoItemFilter INSTANCE = new NoItemFilter();

    @Override
    public boolean allowed(ItemStack itemStack) {
        return true;
    }
}
