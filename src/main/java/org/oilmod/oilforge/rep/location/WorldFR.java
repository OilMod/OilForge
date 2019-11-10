package org.oilmod.oilforge.rep.location;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.api.rep.providers.BlockStateProvider;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.rep.world.VectorRep;
import org.oilmod.api.rep.world.WorldRep;
import org.oilmod.oilforge.rep.block.BlockStateFR;

public class WorldFR implements WorldRep {
    private final World forge;

    public WorldFR(World forge) {
        this.forge = forge;
    }

    public World getForge() {
        return forge;
    }

    @Override
    public BlockStateRep getBlockAt(LocationBlockRep loc) {
        return new BlockStateFR(forge.getBlockState(((LocationBlockFR)loc).getPos()));
    }

    @Override
    public void setBlockAt(LocationBlockRep loc, BlockStateProvider bsPro) {
        forge.setBlockState(((LocationBlockFR)loc).getPos(), ((BlockStateFR)bsPro.getProvidedBlockState()).getForge(), 3);
    }
}
