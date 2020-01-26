package org.oilmod.oilforge.internaltest.testmod1.blocks;

import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.rep.providers.BlockStateProvider;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlock;

public class TestBlock extends OilBlock {
    public TestBlock() {
        super(MinecraftBlock.GLASS, "TestBlock");
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.GLASS.getValue();
    }
}
