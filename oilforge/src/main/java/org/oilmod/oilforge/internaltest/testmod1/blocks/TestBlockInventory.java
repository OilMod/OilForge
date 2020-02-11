package org.oilmod.oilforge.internaltest.testmod1.blocks;

import org.oilmod.api.data.DataParentImpl;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.rep.inventory.InventoryHolderRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.complex.IComplexStateType;
import org.oilmod.api.stateable.complex.IInventoryState;
import org.oilmod.oilforge.internaltest.testmod1.TestMod1;

public class TestBlockInventory extends DataParentImpl implements IInventoryState {
    private ModInventoryObject inventory;


    public TestBlockInventory(InventoryFactory.Builder<ModInventoryObject> invBuilder) {
        inventory = invBuilder.create(this);
    }



    @Override
    public IComplexStateType<TestBlockInventory> getComplexStateType() {
        return TestMod1.TestBlockInventoryType.get();
    }

    @Override
    public InventoryRep getInventory() {
        return inventory.getBukkitInventory();
    }
}
