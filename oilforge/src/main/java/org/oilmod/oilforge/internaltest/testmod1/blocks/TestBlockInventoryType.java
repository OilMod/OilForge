package org.oilmod.oilforge.internaltest.testmod1.blocks;

import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.stateable.complex.ComplexStateTypeBase;
import org.oilmod.oilforge.internaltest.testmod1.items.PortableInventoryFilter;

public class TestBlockInventoryType extends ComplexStateTypeBase<TestBlockInventory> {

    private final InventoryFactory.Builder<ModInventoryObject> invBuilder;

    public TestBlockInventoryType() {
        super(TestBlockInventory.class);
        invBuilder = InventoryFactory
                .builder("items")
                .standardTitle("TestBlock")
                .size(7, 11)
                .filter(PortableInventoryFilter.INSTANCE)
                .mainInventory()
                .dropAll()
                .basic();
    }

    @Override
    public TestBlockInventory create() {
        return new TestBlockInventory(invBuilder);
    }
}
