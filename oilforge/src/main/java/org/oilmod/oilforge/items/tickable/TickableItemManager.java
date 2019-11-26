package org.oilmod.oilforge.items.tickable;


import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.ITickable;
import org.oilmod.oilforge.items.RealItemImplHelper;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import static org.oilmod.oilforge.Util.toReal;

public class TickableItemManager {
    //we need a better solution probably best if tickable item stacks register themselves! todo
    private static final Map<World, Map<Class<? extends Ticker<?>>, Ticker<?>>> providers = new Object2ObjectOpenHashMap<>();
    private static final Set<Ticker> uniTickers = new ObjectOpenHashSet<>();

    static {
        uniTickers.add(new PlayerTicker());
    }

    public static void dealWith(ItemStack stack, long now) {
        if (stack.getItem() instanceof RealItemImplHelper) {
            OilItem item = ((RealItemImplHelper) stack.getItem()).getApiItem();
            if (item instanceof ITickable) {
                ITickable.processTicks(toReal(stack).getOilItemStack(), now, true);
            }
        }
    }



    public static <T> void add(World w, T t, Class<? extends Ticker<T>> clazz, Supplier<Ticker<T>> tickerSupplier) {
        Map<Class<? extends Ticker<?>>, Ticker<?>> tickers = providers.computeIfAbsent(w, (world)->new Object2ObjectOpenHashMap<>());
        //noinspection unchecked
        Ticker<T> ticker = (Ticker<T>) tickers.computeIfAbsent(clazz, (c)->tickerSupplier.get());
        ticker.providers.add(t);
    }



    public static <T> void remove(World w, T t, Class<? extends Ticker<T>> clazz) {
        Map<Class<? extends Ticker<?>>, Ticker<?>> tickers = providers.get(w);
        if (tickers==null)return;
        //noinspection unchecked
        Ticker<T> ticker = (Ticker<T>) tickers.get(clazz);
        if (ticker==null)return;
        ticker.providers.remove(t);
    }

    public static void onTick(World w) {
        long time = w.getGameTime();
        Map<Class<? extends Ticker<?>>, Ticker<?>> tickers = providers.get(w);
        if (tickers != null) {
            tickers.values().forEach((ticker -> ticker.processAll(w, time)));
            uniTickers.forEach((ticker -> ticker.processAll(w, time)));
        }
    }


}
