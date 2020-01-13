package org.oilmod.oilforge.rep.itemstack;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.item.BlockItemRep;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.rep.itemstack.ItemStackFactory;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.oilforge.Util;
import org.oilmod.oilforge.items.capability.OilItemStackHandler;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.state.ItemStackStateFR;

public class RealItemStackFactory extends ItemStackFactory<RealItemStackFactory> {
    public static RealItemStackFactory INSTANCE;
    private ItemStackFR EMPTY = new ItemStackFR(ItemStack.EMPTY);  //todo consider chance of mod trying to modify empty item

    @Override
    public ItemStackFR create(ItemRep item, ItemStackStateRep state, int amount) {
        return new ItemStackFR((ItemFR)item, (ItemStackStateFR) state, amount);
    }

    @Override
    public void onReady() {
        INSTANCE = this;
    }

    public ItemStackFR create(ItemStack stack) {
        return stack.isEmpty()?EMPTY:Util.isModStack(stack)? Util.toReal(stack).asItemStackRep():new ItemStackFR(stack);
        //todo fix dirty hacks
        //return stack instanceof RealItemStack?((RealItemStack) stack).asItemStackRep():new ItemStackFR(stack);
    }

    @Override
    public ItemStackRep empty() {
        if (EMPTY == null) {
            EMPTY = new ItemStackFR(ItemStack.EMPTY);
        }
        return EMPTY;
    }

    @Override
    public ItemStackStateRep createStackState(ItemRep item, ItemStateRep itemState) {
        return null;//throw new NotImplementedException("todo"); //todo
    }

    @Override
    public ItemStackStateRep createStackState(BlockItemRep item, BlockStateRep itemState) {
        throw new NotImplementedException("todo"); //todo
    }
}
