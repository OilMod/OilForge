package org.oilmod.oilforge.rep.item;

import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.item.BlockItemStateRep;
import org.oilmod.oilforge.rep.block.BlockStateFR;

public class BlockItemStateFR extends ItemStateFR implements BlockItemStateRep {
    private final BlockStateFR blockState;

    public BlockItemStateFR(BlockItemFR item, BlockStateFR blockState) {
        super(item, -1);
        this.blockState = blockState;
    }

    @Override
    public BlockItemFR getItem() {
        return (BlockItemFR) super.getItem();
    }

    @Override
    public BlockStateFR getBlockState() {
        return blockState;
    }
}
