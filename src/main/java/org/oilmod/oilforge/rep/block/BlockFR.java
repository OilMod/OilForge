package org.oilmod.oilforge.rep.block;

import net.minecraft.block.Block;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.item.ItemRep;

public class BlockFR implements BlockRep {
    private final Block forge;

    public BlockFR(Block forge) {
        this.forge = forge;
    }

    public Block getForge() {
        return forge;
    }

    @Override
    public boolean hasItem() {
        throw new NotImplementedException("to be implemented"); //todo: implement with mixins or otherwise
    }

    @Override
    public ItemRep getItem() {
        throw new NotImplementedException("to be implemented"); //todo: implement with mixins or otherwise
    }
}
