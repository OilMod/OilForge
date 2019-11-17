package org.oilmod.oilforge.rep.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.item.BlockItemRep;
import org.oilmod.oilforge.rep.item.BlockItemFR;

public class BlockFR implements BlockRep {
    private final Block forge;
    private final BlockItemRep itemRep;

    public BlockFR(Block forge) {
        this.forge = forge;

        Item item = Item.getItemFromBlock(forge);

        itemRep = item instanceof BlockItem ? new BlockItemFR((BlockItem) item) : null;
    }

    public Block getForge() {
        return forge;
    }

    @Override
    public boolean hasItem() {
        return itemRep != null;
    }

    @Override
    public BlockItemRep getItem() {
        return itemRep;
    }

    @Override
    public StandardBlockStateFR getStandardState() {
        return new StandardBlockStateFR(forge);
        //todo: implement with mixins or otherwise
    }
}
