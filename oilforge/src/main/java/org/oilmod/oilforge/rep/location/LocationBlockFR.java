package org.oilmod.oilforge.rep.location;


import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.oilmod.api.rep.world.LocationBlockRep;

public class LocationBlockFR implements LocationBlockRep {
    private final World w;
    private final BlockPos pos;

    public LocationBlockFR(World w, BlockPos pos) {
        this.w = w;
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public World getWorldForge() {
        return w;
    }

    @Override
    public int getBlockX() {
        return pos.getX();
    }

    @Override
    public int getBlockY() {
        return pos.getY();
    }

    @Override
    public int getBlockZ() {
        return pos.getZ();
    }

    @Override
    public WorldFR getWorld() {
        return new WorldFR(getWorldForge()); //todo mixins store WorldFR
    }
}
