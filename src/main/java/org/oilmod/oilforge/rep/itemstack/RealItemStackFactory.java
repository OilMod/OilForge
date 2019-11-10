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
import org.oilmod.oilforge.dirtyhacks.RealItemStackHelper;
import org.oilmod.oilforge.rep.item.ItemFR;

public class RealItemStackFactory extends ItemStackFactory {
    public static RealItemStackFactory INSTANCE;
    private final ItemStackFR EMPTY = create(ItemStack.EMPTY);

    @Override
    public ItemStackFR create(ItemRep item, ItemStackStateRep state, int amount) {
        return new ItemStackFR((ItemFR)item, (ItemStackStateFR) state, amount);
    }


    public ItemStackFR create(ItemStack stack) {
        return RealItemStackHelper.hasReal(stack)?RealItemStackHelper.toReal(stack).asItemStackRep():new ItemStackFR(stack);
        //todo fix dirty hacks
        //return stack instanceof RealItemStack?((RealItemStack) stack).asItemStackRep():new ItemStackFR(stack);
    }

    @Override
    public ItemStackRep empty() {
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
