package org.oilmod.oilforge.rep.block;

import gnu.trove.set.hash.THashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.providers.BlockStateProvider;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.oilforge.rep.location.LocationBlockFR;
import org.oilmod.oilforge.rep.location.WorldFR;

public abstract class BlockStateBase implements BlockStateRep {
    public abstract IBlockState getForge();

    @Override
    public abstract boolean isReadOnly();

    @Override
    public BlockRep getBlock() {
        return new BlockFR(getForge().getBlock());  //todo mixins store BlockFR
    }

    @Override
    public BlockStateRep copy() {
        //possible as IBlockData is immutable
        return new BlockStateFR(getForge());
    }


    @Override
    public float getBlockHardness(LocationBlockRep loc) {
        LocationBlockFR locFR = (LocationBlockFR)loc;
        return getForge().getBlockHardness(locFR.getWorld().getForge(), locFR.getPos());
    }

    @Override
    public boolean isSame(BlockStateProvider other) {
        BlockStateBase otherBS = (BlockStateBase)other.getProvidedBlockState();
        IBlockState f1 = getForge();
        IBlockState f2 = otherBS.getForge();
        //might be bad performace

        THashSet<IProperty> s1 = new THashSet<>(f1.getProperties());
        THashSet<IProperty> s2 = new THashSet<>(f2.getProperties());

        //is this even correct?
        return f1.getBlock() == f2.getBlock() && s1.equals(s2);
    }

    /*@Override
    public BlockType getBlockType() {
        return NMS.getMaterial().getOilBlockType();
    }*/
}
