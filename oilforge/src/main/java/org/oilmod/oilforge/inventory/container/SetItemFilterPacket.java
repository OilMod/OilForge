package org.oilmod.oilforge.inventory.container;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.inventory.IItemFilter;
import org.oilmod.oilforge.inventory.ItemFilterRegistryHelper;

import java.util.function.Supplier;

public class SetItemFilterPacket {
    public static final Logger LOGGER = LogManager.getLogger();
    public final IItemFilter itemFilter;

    public SetItemFilterPacket(IItemFilter itemFilter) {
        this.itemFilter = itemFilter;
    }

    public static SetItemFilterPacket decode(PacketBuffer buffer) {
        return new SetItemFilterPacket(ItemFilterRegistryHelper.get(buffer.readResourceLocation()));
    }
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(itemFilter.getKey());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.setPacketHandled(true);
        Container c = context.getSender().openContainer;
        LOGGER.debug("Set itemfilter {} for container {}", itemFilter::getKey, c::toString);
        if (c instanceof IOilContainer) {
            ((IOilContainer) c).setItemFilter(itemFilter);
        }
    }

    public static void sent(ServerPlayerEntity player, IItemFilter filter) {
        ContainerPackageHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player), new SetItemFilterPacket(filter));
    }
}
