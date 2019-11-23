package org.oilmod.oilforge.inventory.container;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.IContainerFactory;
import org.oilmod.oilforge.inventory.OilInventoryChest;

import java.util.Set;

import static net.minecraft.inventory.container.ContainerType.GENERIC_9X1;

public class OilContainerType<T extends Container & IOilContainer> extends ContainerType<T> {
    public static final Set<OilContainerType> toBeRegistered = new ObjectOpenHashSet<>();
    public static final OilContainerType<OilChestContainer> GENERIC_9X1 = register("generic_9x1", (c, id, p, e)->new OilChestContainer(c, id, p, 1, e));
    public static final OilContainerType<OilChestContainer> GENERIC_9X2 = register("generic_9x2", (c, id, p, e)->new OilChestContainer(c, id, p, 2, e));
    public static final OilContainerType<OilChestContainer> GENERIC_9X3 = register("generic_9x3", (c, id, p, e)->new OilChestContainer(c, id, p, 3, e));
    public static final OilContainerType<OilChestContainer> GENERIC_9X4 = register("generic_9x4", (c, id, p, e)->new OilChestContainer(c, id, p, 4, e));
    public static final OilContainerType<OilChestContainer> GENERIC_9X5 = register("generic_9x5", (c, id, p, e)->new OilChestContainer(c, id, p, 5, e));
    public static final OilContainerType<OilChestContainer> GENERIC_9X6 = register("generic_9x6", (c, id, p, e)->new OilChestContainer(c, id, p, 6, e));


    private static <T extends Container & IOilContainer> OilContainerType<T> register(String key, IOilFactory<T> factory) {
        //noinspection unchecked
        OilContainerType<T>[] finalHelper = new OilContainerType[1];
        finalHelper[0] = new OilContainerType<>(key, (id, p, extra)->factory.create(finalHelper[0], id, p, extra));
        toBeRegistered.add(finalHelper[0]);
        return finalHelper[0];
    }


    public OilContainerType(String key , IContainerFactory<T> factory) {
        super(factory);
        setRegistryName(new ResourceLocation("oilforgeapi", key));
    }

    public interface IOilFactory<T extends Container & IOilContainer> {
        @OnlyIn(Dist.CLIENT)
        T create(OilContainerType<T> containerType, int id, PlayerInventory player, PacketBuffer extra);
    }


}
