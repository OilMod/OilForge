package org.oilmod.oilforge.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraftforge.common.ToolType;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.oilforge.NMSKeyImpl;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.block.RealBlockTypeHelper.toForge;


public class RealBlock extends Block implements RealBlockImplHelper
{

    private final OilBlock oilBlock;

    private static Properties createProperties(OilBlock block) {
        Properties result=  Properties.create(toForge(block.getBlockType()));
        result.hardnessAndResistance(block.getHardness(), block.getResistance());
        result.harvestLevel(block.getHarvestLevel());
        result.harvestTool((ToolType) block.getHarvestTool().getNMS());
        if (!block.isBlocksMovement()) result.doesNotBlockMovement();
        result.lightValue(block.getLightSourceValue());
        result.slipperiness(block.getSlipperiness());
        return result;
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return toForge(oilBlock.getPistonReaction());
    }

    public RealBlock(OilBlock oilBlock) {
        super(createProperties(oilBlock));
        this.oilBlock = oilBlock;
        setRegistryName(((NMSKeyImpl) oilBlock.getOilKey().getNmsKey()).resourceLocation);
    }

    @Override
    public OilBlock getOilBlock() {
        return oilBlock;
    }
}

