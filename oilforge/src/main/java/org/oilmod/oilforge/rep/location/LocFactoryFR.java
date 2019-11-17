package org.oilmod.oilforge.rep.location;

import net.minecraft.util.math.BlockPos;
import org.oilmod.api.rep.stdimpl.world.LocFactoryImpl;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.rep.world.WorldRep;

public class LocFactoryFR extends LocFactoryImpl {

    @Override
    public LocationBlockRep createBlockLocation(int x, int y, int z, WorldRep w) {
        return new LocationBlockFR(((WorldFR)w).getForge(), new BlockPos(x, y, z));
    }
}
