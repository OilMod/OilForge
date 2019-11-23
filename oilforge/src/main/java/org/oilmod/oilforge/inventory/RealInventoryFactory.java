package org.oilmod.oilforge.inventory;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeType;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.data.DataParent;
import org.oilmod.api.data.ItemStackData;
import org.oilmod.api.data.ObjectFactory;
import org.oilmod.api.inventory.*;
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
        return () -> new OilInventoryChest(oilItemStack,size,title,ItemFilterRegistryHelper.get(filter));
    }

    @Override
    protected ObjectFactory<ModNMSIInventory<ModFurnaceInventoryObject>> getFurnaceInventoryFactory(final OilItemStack oilItemStack, final String title, final ITicker ticker, final ItemFilter filter) {
        return () -> new OilInventoryFurnace(ContainerType.FURNACE, IRecipeType.SMELTING, oilItemStack,title, ticker, ItemFilterRegistryHelper.get(filter));
    }

    @Override
    protected ObjectFactory<ModNMSIInventory<ModPortableCraftingInventoryObject>> getPortableCraftingInventoryFactory(final OilItemStack oilItemStack, final int width, final int height, final String title, final ItemFilter filter) {
        return () -> {
            throw new NotImplementedException("todo"); //todo
            //return new OilInventoryPortableCrafting(oilItemStack, width, height,title, createNMSFilter(filter));
        };
    }

}
