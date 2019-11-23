package org.oilmod.oilforge.inventory.container;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ContainerPackageHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("oilforgeapi", "oilmod"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int packetId = 0;
        INSTANCE.registerMessage(packetId++, SetItemFilterPacket.class, SetItemFilterPacket::encode, SetItemFilterPacket::decode, SetItemFilterPacket::handle);

    }
}
