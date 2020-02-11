package org.oilmod.oilforge.internaltest.testmod1.blocks;

import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.blocks.type.IBlockComplexStateable;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlock;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.stateable.complex.ComplexStateFactoryStore;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.enumerable.IEnumerableState;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.internaltest.testmod1.TestMod1;

public class TestBlock2 extends OilBlock {

    public TestBlock2() {
        super(MinecraftBlock.BIRCH_STAIRS, "TestBlock2");
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.WOOD.getValue();
    }


    @Override
    public float getHardness() {
        return 0.5f;
    }

}
