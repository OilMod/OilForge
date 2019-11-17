package org.oilmod.oilforge.rep.block;


import net.minecraft.block.state.IBlockState;

public class BlockStateFR extends BlockStateBase {
    private final IBlockState forge;

    public BlockStateFR(IBlockState forge) {
        this.forge = forge;
    }

    public IBlockState getForge() {
        return forge;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }


}
