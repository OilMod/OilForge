package org.oilmod.oilforge.inventory;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeType;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.crafting.ICraftingProcessor;
import org.oilmod.api.data.IDataParent;
import org.oilmod.api.data.ItemStackData;
import org.oilmod.api.data.ObjectFactory;
import org.oilmod.api.inventory.*;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.inventory.InventoryHolderRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.complex.IInventoryState;
import org.oilmod.api.util.ITicker;
import org.oilmod.oilforge.inventory.container.OilContainerType;

import java.util.List;
import java.util.function.Function;

/**
 * Created by sirati97 on 12.02.2016.
 */
public class RealInventoryFactory extends InventoryFactory {
    @Override
    public ItemStackData createItemStackData(String name, IDataParent dataParent) {
        throw new NotImplementedException("todo"); //todo
        //return new ItemStackDataImpl(name, dataParent);
    }

    @Override
    protected Function<IInventoryState, ModNMSIInventory<ModInventoryObject>> getBasicInventoryFactory(final int rows, final int columns, final String title, final ItemFilter filter, Function<InventoryRep, ICraftingProcessor[]> processorFactory, DropPredicate dropPredicate) {
        return (stack) -> new OilInventoryChest(stack,Math.max(1, rows), Math.max(1, columns), title, ItemFilterRegistryHelper.get(filter), processorFactory, dropPredicate);
    }

    @Override
    protected Function<IInventoryState, ModNMSIInventory<ModFurnaceInventoryObject>> getFurnaceInventoryFactory(final String title, final ITicker ticker, final ItemFilter filter, Function<InventoryRep, ICraftingProcessor[]> processorFactory, DropPredicate dropPredicate) {
        return (stack) -> new OilInventoryFurnace(OilContainerType.FURNACE, IRecipeType.SMELTING, stack, title, ticker, ItemFilterRegistryHelper.get(filter), processorFactory, dropPredicate);
    }

    @Override
    protected Function<IInventoryState, ModNMSIInventory<ModPortableCraftingInventoryObject>> getPortableCraftingInventoryFactory(final int width, final int height, final String title, final ItemFilter filter, Function<InventoryRep, ICraftingProcessor[]> processorFactory, DropPredicate dropPredicate) {
        return (stack) -> {
            throw new NotImplementedException("todo"); //todo
            //return new OilInventoryPortableCrafting(oilItemStack, width, height,title, createNMSFilter(filter));
        };
    }

}
