package org.oilmod.oilforge.block;

import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.blocks.BlockType;
import net.minecraft.block.state.IBlockState;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.oilforge.rep.location.WorldFR;

import static org.oilmod.oilforge.Util.toOil;


public class RealOilBlockState implements org.oilmod.api.blocks.IBlockState { //consider moving this to rep
    private final IBlockState nms;

    public RealOilBlockState(IBlockState nms) {
        this.nms = nms;
    }

    @Override
    public BlockType getBlockType() {
        throw new NotImplementedException("to be implemented"); //todo: implement with mixins or otherwise
        //return nms.getMaterial().getOilBlockType();
    }

    @Override
    public BlockRep getBlock() {
        return toOil(nms.getBlock());
    }

    @Override
    public float getBlockHardness(LocationBlockRep location) {
        return nms.getBlockHardness(((WorldFR)location.getWorld()).getForge(), new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Override
    public IBlockState getNMS() {
        return nms;
    }
}
