package org.oilmod.oilforge.internaltest.testmod1.items;

import org.oilmod.api.items.OilItem;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.util.OilKey;

public class TestItem1 extends OilItem {
    public TestItem1() {
        super(MinecraftItem.STICK, "Test Item 1");
    }
}
