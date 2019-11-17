package org.oilmod.oilforge.rep.block;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class StandardBlockStateFR extends BlockStateBase {
    private final Block block;

    public StandardBlockStateFR(Block block) {
        this.block = block;
    }

    public BlockState getForge() {
        return block.getDefaultState();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
