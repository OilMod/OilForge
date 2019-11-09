package org.oilmod.oilforge.items;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.internal.ItemClassMap;

import java.lang.reflect.Array;
import java.util.Set;

public class RealItemClassMap extends ItemClassMap {
    private TMap<Class<? extends OilItem>,Set<? extends OilItem>> itemClassMap = new THashMap<>();

    @Override
    public <T extends OilItem> T[] getOilItemsByClass(Class<T> clazz) {
        Set<T> set = (Set<T>) itemClassMap.get(clazz);
        return set==null||set.size()==0?array(clazz, 0):set.toArray(array(clazz, set.size()));
    }

    public <T extends OilItem> void register(T item) {
        Class<T> clazz = (Class<T>) item.getClass();
        Set<T> newSet = new THashSet<>();
        Set<T> set = (Set<T>) itemClassMap.putIfAbsent(clazz, newSet);
        set = set==null?newSet:set;
        set.add(item);
    }

    private <T extends OilItem> T[] array(Class<T> clazz, int capacity) {
        return (T[]) Array.newInstance(clazz, capacity);
    }
}