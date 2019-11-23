package org.oilmod.oilforge.inventory;


import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by sirati97 on 13.02.2016.
 */
public interface IItemFilter {
    boolean allowed(ItemStack itemStack);
    ResourceLocation getKey();
}
