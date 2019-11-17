package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.items.OilItem;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.util.OilKey;

public class TestItem1 extends OilItem {
    public TestItem1(OilKey key) {
        super(key, MinecraftItem.STICK, "Test Item 1");
    }
}
