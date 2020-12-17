package org.oilmod.oilforge.rep.block;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.providers.BlockStateProvider;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.oilforge.rep.location.LocationBlockFR;

public abstract class BlockStateBase implements BlockStateRep {
    public abstract BlockState getForge();

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
        BlockState f1 = getForge();
        BlockState f2 = otherBS.getForge();
        //might be bad performace

        ObjectOpenHashSet<Property> s1 = new ObjectOpenHashSet<>(f1.getProperties());
        ObjectOpenHashSet<Property> s2 = new ObjectOpenHashSet<>(f2.getProperties());

        //is this even correct?
        return f1.getBlock() == f2.getBlock() && s1.equals(s2);
    }

    /*@Override
    public BlockType getBlockType() {
        return NMS.getMaterial().getOilBlockType();
    }*/
}
