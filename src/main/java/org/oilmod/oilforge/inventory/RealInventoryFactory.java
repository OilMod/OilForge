package org.oilmod.oilforge.inventory;

import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.data.DataParent;
import org.oilmod.api.data.ItemStackData;
import org.oilmod.api.data.ObjectFactory;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.inventory.ItemFilter;
import org.oilmod.api.inventory.ModFurnaceInventoryObject;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.inventory.ModNMSIInventory;
import org.oilmod.api.inventory.ModPortableCraftingInventoryObject;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.util.ITicker;

/**
 * Created by sirati97 on 12.02.2016.
 */
public class RealInventoryFactory extends InventoryFactory {
    @Override
    public ItemStackData createItemStackData(String name, DataParent dataParent) {
        throw new NotImplementedException("todo"); //todo
        //return new ItemStackDataImpl(name, dataParent);
    }

    @Override
    protected ObjectFactory<ModNMSIInventory<ModInventoryObject>> getBasicInventoryFactory(final OilItemStack oilItemStack, final int size, final String title, final ItemFilter filter) {
        return () -> new OilInventoryChest(oilItemStack,size,title,createNMSFilter(filter));
    }

    @Override
    protected ObjectFactory<ModNMSIInventory<ModFurnaceInventoryObject>> getFurnaceInventoryFactory(final OilItemStack oilItemStack, final String title, final ITicker ticker, final ItemFilter filter) {
        return () -> new OilInventoryFurnace(oilItemStack,title, ticker, createNMSFilter(filter));
    }

    @Override
    protected ObjectFactory<ModNMSIInventory<ModPortableCraftingInventoryObject>> getPortableCraftingInventoryFactory(final OilItemStack oilItemStack, final int width, final int height, final String title, final ItemFilter filter) {
        return () -> {
            throw new NotImplementedException("todo"); //todo
            //return new OilInventoryPortableCrafting(oilItemStack, width, height,title, createNMSFilter(filter));
        };
    }

    public IItemFilter createNMSFilter(ItemFilter bukkitFilter) {
        return bukkitFilter==null?NoItemFilter.INSTANCE:new ApiItemFilter(bukkitFilter);
    }
}
