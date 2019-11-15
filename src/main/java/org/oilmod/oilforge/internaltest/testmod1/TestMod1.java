package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.OilMod;
import org.oilmod.api.items.ItemRegistry;

public class TestMod1 extends OilMod {

    public TestMod1() {
        super();
    }

    @Override
    public void onRegisterItems(ItemRegistry itemRegistry) {
        itemRegistry.register(new TestItem1(createKey("testitem1")));
        itemRegistry.register(new TestItem2(createKey("testitem2")));
        itemRegistry.register(new TestBackpackItem(createKey("testbackpack")));
        itemRegistry.register(new TestPickaxe(createKey("testpickaxe")));
        itemRegistry.register(new TestShovel(createKey("testshovel")));
        itemRegistry.register(new GodsFlintItem(createKey("gods_flint")));
    }
}
