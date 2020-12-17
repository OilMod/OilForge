package org.oilmod.oilforge.resource;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;

import java.util.Map;
import java.util.function.Consumer;

public class OilPackFinder implements IPackFinder {
    @Override
    public void findPacks(Consumer<ResourcePackInfo> infoConsumer, ResourcePackInfo.IFactory infoFactory) {
        infoConsumer.accept(ResourcePackInfo.createResourcePack("OilDummy", true, OilDummyResourcePack::new, infoFactory, ResourcePackInfo.Priority.BOTTOM, IPackNameDecorator.PLAIN));
    }
}
