package org.oilmod.oilforge.internaltest.testmod1;

import org.oilmod.api.OilMod;
import org.oilmod.api.inventory.ItemFilterRegistry;
import org.oilmod.api.items.ItemRegistry;

public class TestMod1 extends OilMod {

    @Override
    public void onRegisterItems(ItemRegistry itemRegistry) {
        itemRegistry.register("testitem1", new TestItem1());
        itemRegistry.register("testitem2", new TestItem2());
        itemRegistry.register("testbackpack", new TestBackpackItem());
        itemRegistry.register("kaban", new TestKabanItem());
        itemRegistry.register("testportablefurnance", new TestPortableFurnaceItem());
        itemRegistry.register("testpickaxe", new TestPickaxe());
        itemRegistry.register("testshovel", new TestShovel());
        itemRegistry.register("gods_flint", new GodsFlintItem());
    }

    @Override
    protected void onRegisterItemFilter(ItemFilterRegistry registry) {
        registry.register("backpack_item_filter", BackpackItemFilter.INSTANCE);
    }
}
