package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.OilMod;
import org.oilmod.api.items.ItemRegistry;

public class TestMod1 {

    public void init() {
        OilMod mod = new OilMod("testmod1", "Internal Test Mod1");
        ItemRegistry itemRegistry = new ItemRegistry(mod);
        itemRegistry.register(new TestItem1(mod.createKey("testitem1")));
        itemRegistry.register(new TestItem2(mod.createKey("testitem2")));
        itemRegistry.register(new TestBackpackItem(mod.createKey("testbackpack")));
        itemRegistry.register(new TestPickaxe(mod.createKey("testpickaxe")));
        itemRegistry.register(new TestShovel(mod.createKey("testshovel")));
    }
}
