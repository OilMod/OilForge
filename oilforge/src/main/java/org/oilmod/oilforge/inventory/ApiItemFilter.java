package org.oilmod.oilforge.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.oilmod.api.inventory.ItemFilter;

import static org.oilmod.oilforge.Util.toOil;

/**
 * Created by sirati97 on 13.02.2016.
 */
public class ApiItemFilter implements IItemFilter {
    private final ItemFilter bukkitFilter;
    private final ResourceLocation key;

    public ApiItemFilter(ItemFilter bukkitFilter, ResourceLocation key) {
        this.bukkitFilter = bukkitFilter;
        this.key = key;
    }

    @Override
    public boolean allowed(ItemStack itemStack) {
        return bukkitFilter.allowed(toOil(itemStack));
    }

    @Override
    public ResourceLocation getKey() {
        return key;
    }
}
