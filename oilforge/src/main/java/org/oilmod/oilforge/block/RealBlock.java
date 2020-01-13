package org.oilmod.oilforge.block;

import net.minecraft.block.Block;
import org.oilmod.api.blocks.OilBlock;

import static org.oilmod.oilforge.Util.toForge;

public class RealBlock extends Block implements RealBlockImplHelper
{
    private final OilBlock oilBlock;

    private static Properties createProperties(OilBlock block) {
        return Properties.create(toForge(block.getBlockType()));
    }

    public RealBlock(OilBlock oilBlock) {
        super(createProperties(oilBlock));
        this.oilBlock = oilBlock;
    }

    public OilBlock getOilBlock() {
        return oilBlock;
    }
}
