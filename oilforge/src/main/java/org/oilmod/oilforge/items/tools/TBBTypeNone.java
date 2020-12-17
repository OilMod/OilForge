package org.oilmod.oilforge.items.tools;

import net.minecraft.block.BlockState;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.oilforge.rep.block.BlockStateFR;

public class TBBTypeNone extends RealTBBTool {
    protected TBBTypeNone() {
        super(TBBEnum.NONE, 0, 1, null);
    }

    @Override
    protected boolean canHarvestBlock(IToolBlockBreaking item, OilItemStack stack, BlockStateRep blockState, BlockType blockType) {
        BlockState blockIn = ((BlockStateFR)blockState).getForge();
        return !blockIn.getMaterial().isReplaceable();
    }

    @Override
    protected ItemImplementationProvider getImplementationProvider() {
        return ItemImplementationProvider.CUSTOM.getValue();
    }

    @Override
    public ToolType getForge() {
        return null;
    }
}
