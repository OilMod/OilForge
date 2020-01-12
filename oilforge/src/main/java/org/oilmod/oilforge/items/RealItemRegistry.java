package org.oilmod.oilforge.items;

import org.oilmod.api.OilMod;
import org.oilmod.api.items.ItemRegistry;

public class RealItemRegistry extends ItemRegistry {

    /**
     * Creates new instance of Registry
     *
     * @param mod            associated mod with this item registry
     * @param registryHelper
     */
    protected RealItemRegistry(OilMod mod, Helper<?> registryHelper) {
        super(mod, registryHelper);
    }
}
