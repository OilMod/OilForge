package org.oilmod.oilforge.inventory;


import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by sirati97 on 13.02.2016.
 */
public class NoItemFilter implements IItemFilter {
    public static final NoItemFilter INSTANCE = new NoItemFilter();


    private final ResourceLocation key;

    private NoItemFilter() {
        this.key = new ResourceLocation("oilmod", "nofilter");//has to be oilmod as this is platform independent
    }

    @Override
    public boolean allowed(ItemStack itemStack) {
        return true;
    }

    @Override
    public ResourceLocation getKey() {
        return key;
    }
}
