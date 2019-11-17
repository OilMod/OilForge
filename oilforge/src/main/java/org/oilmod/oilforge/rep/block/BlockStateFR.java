package org.oilmod.oilforge.rep.block;


import net.minecraft.block.BlockState;

public class BlockStateFR extends BlockStateBase {
    private final BlockState forge;

    public BlockStateFR(BlockState forge) {
        this.forge = forge;
    }

    public BlockState getForge() {
        return forge;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }


}
