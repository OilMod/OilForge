package org.oilmod.oilforge.rep.item;

import net.minecraft.item.BlockItem;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.item.BlockItemRep;
import org.oilmod.api.rep.item.BlockItemStateRep;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.oilforge.rep.block.BlockFR;

public class BlockItemFR extends ItemFR implements BlockItemRep {
    public BlockItemFR(BlockItem forge) {
        super(forge);
    }

    @Override
    public BlockItem getForge() {
        return (BlockItem) super.getForge();
    }

    @Override
    public BlockItemStateRep getStandardState() {
        return new StandardBlockItemStateFR(this);
    }

    @Override
    public BlockRep getBlock() {
        return new BlockFR(getForge().getBlock()); //todo mixins store BlockFR
    }
}
