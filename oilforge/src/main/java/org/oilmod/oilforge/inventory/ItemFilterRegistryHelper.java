package org.oilmod.oilforge.inventory;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.inventory.ItemFilter;
import org.oilmod.api.inventory.ItemFilterRegistry;
import org.oilmod.api.registry.InitRegisterCallback;
import org.oilmod.api.util.OilKey;

import java.util.Map;

import static org.oilmod.oilforge.Util.toForge;

public class ItemFilterRegistryHelper extends ItemFilterRegistry.RegistryHelper<ItemFilterRegistryHelper> {
    private static Map<ResourceLocation, IItemFilter> map = new Object2ObjectOpenHashMap<>();
    private static Map<ItemFilter, IItemFilter> filterMap = new Object2ObjectOpenHashMap<>();

    @Override
    public <T extends ItemFilter> void register(OilKey key, ItemFilterRegistry register, T entry) {
        IItemFilter forge = new ApiItemFilter(entry, toForge(key));
        map.put(forge.getKey(), forge);
        filterMap.put(entry,forge);
    }


    public static IItemFilter get(ItemFilter bukkitFilter) {
        if (bukkitFilter == null)return NoItemFilter.INSTANCE;
        IItemFilter result = filterMap.get(bukkitFilter);
        Validate.notNull(result, "filter %s has not been registered!", bukkitFilter);
        return result;
    }


    public static IItemFilter get(ResourceLocation key) {
        return map.getOrDefault(key, NoItemFilter.INSTANCE);
    }
}
