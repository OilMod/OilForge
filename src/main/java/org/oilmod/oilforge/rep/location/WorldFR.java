package org.oilmod.oilforge.rep.location;

import net.minecraft.world.World;
import org.oilmod.api.rep.world.WorldRep;

public class WorldFR implements WorldRep {
    private final World forge;

    public WorldFR(World forge) {
        this.forge = forge;
    }

    public World getForge() {
        return forge;
    }
}
