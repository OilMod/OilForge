package org.oilmod.oilforge.rep.item;

import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.item.BlockItemStateRep;

public class StandardBlockItemStateFR extends StandardItemStateFR implements BlockItemStateRep {
    public StandardBlockItemStateFR(BlockItemFR item) {
        super(item);
    }

    @Override
    public BlockItemFR getItem() {
        return (BlockItemFR) super.getItem();
    }

    @Override
    public BlockStateRep getBlockState() {
        return getItem().getBlock().getStandardState();
    }
}
