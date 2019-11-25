package org.oilmod.oilforge.ui;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.UI.UIFactory;
import org.oilmod.api.UI.UIRegistry;
import org.oilmod.api.inventory.ItemFilter;
import org.oilmod.api.inventory.ItemFilterRegistry;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.inventory.ApiItemFilter;
import org.oilmod.oilforge.inventory.IItemFilter;
import org.oilmod.oilforge.inventory.NoItemFilter;

import java.util.Map;

import static org.oilmod.oilforge.Util.toForge;

public class UIRegistryHelper extends UIRegistry.RegistryHelper<UIRegistryHelper> {
    private static Map<ResourceLocation, UIFactory> map = new Object2ObjectOpenHashMap<>();

    @Override
    public <T extends UIFactory> void register(OilKey key, UIRegistry register, T entry) {
        map.put(toForge(entry.getOilKey()), entry);
    }

    public static UIFactory get(ResourceLocation key) {
        return map.computeIfAbsent(key, (k)->{throw new IllegalStateException("There is no UI with key " + k.toString());});
    }
}
