package org.oilmod.oilforge.resource;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;

import java.util.Map;

public class OilPackFinder implements IPackFinder {
    @Override
    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {

        final T packInfo = ResourcePackInfo.createResourcePack("OilDummy", true, OilDummyResourcePack::new, packInfoFactory, ResourcePackInfo.Priority.BOTTOM);
        nameToPackMap.put("OilDummy", packInfo);
    }
}
