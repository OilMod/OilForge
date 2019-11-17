package org.oilmod.oilforge.inventory;

import net.minecraft.item.ItemStack;
import org.oilmod.api.inventory.ItemFilter;

import static org.oilmod.oilforge.Util.toOil;

/**
 * Created by sirati97 on 13.02.2016.
 */
public class ApiItemFilter implements IItemFilter {
    private final ItemFilter bukkitFilter;

    public ApiItemFilter(ItemFilter bukkitFilter) {
        this.bukkitFilter = bukkitFilter;
    }

    @Override
    public boolean allowed(ItemStack itemStack) {
        return bukkitFilter.allowed(toOil(itemStack));
    }
}
