package org.oilmod.oilforge.items.tickable;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.World;

import java.util.Set;

public abstract class Ticker<T> {
    public final Set<T> providers = new ObjectOpenHashSet<>();

    public abstract void processAll(World w, long now);
}
