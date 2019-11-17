package org.oilmod.oilforge.rep.item;

import net.minecraft.item.ItemBlock;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.item.BlockItemRep;
import org.oilmod.api.rep.item.BlockItemStateRep;
import org.oilmod.oilforge.rep.block.BlockFR;

public class BlockItemFR extends ItemFR implements BlockItemRep {
    public BlockItemFR(ItemBlock forge) {
        super(forge);
    }

    @Override
    public ItemBlock getForge() {
        return (ItemBlock) super.getForge();
    }

    @Override
    public BlockItemStateRep getStandardState() {
        return (BlockItemStateRep) super.getStandardState();
    }

    @Override
    public BlockRep getBlock() {
        return new BlockFR(getForge().getBlock()); //todo mixins store BlockFR
    }
}
