package org.oilmod.oilforge.internaltest.testmod1;

import net.minecraft.init.Items;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.api.util.OilKey;

import static org.oilmod.oilforge.Util.toOil;

public class TestItem1 extends OilItem {
    public TestItem1(OilKey key) {
        super(key, MinecraftItem.STICK, "Test Item 1");
    }
}
