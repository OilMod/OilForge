package org.oilmod.oilforge.inventory.container;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.IContainerFactory;
import org.oilmod.oilforge.ui.container.UIContainer;

import java.util.Set;

public class OilContainerType<T extends Container & IOilContainer> extends ContainerType<T> {
    public static final Set<OilContainerType> toBeRegistered = new ObjectOpenHashSet<>();
    public static final OilContainerType<OilChestLikeContainer> CHESS_LIKE = register("chess_like", OilChestLikeContainer::new);
    public static final OilContainerType<OilFurnaceContainer> FURNACE = register("furnace", (c, id, p, e)->new OilFurnaceContainer(c, IRecipeType.SMELTING, id, p, e));
    public static final OilContainerType<OilFurnaceContainer> BLAST_FURNACE = register("blast_furnace", (c, id, p, e)->new OilFurnaceContainer(c, IRecipeType.BLASTING, id, p, e));
    public static final OilContainerType<OilFurnaceContainer> SMOKER = register("smoker", (c, id, p, e)->new OilFurnaceContainer(c, IRecipeType.SMOKING, id, p, e));
    public static final OilContainerType<UIContainer> CUSTOM_UI = register("custom_ui", UIContainer::new);


    private static <T extends Container & IOilContainer> OilContainerType<T> register(String key, IOilFactory<T> factory) {
        //noinspection unchecked
        OilContainerType<T>[] finalHelper = new OilContainerType[1];
        finalHelper[0] = new OilContainerType<>(key, (id, p, extra)->factory.create(finalHelper[0], id, p, extra));
        toBeRegistered.add(finalHelper[0]);
        return finalHelper[0];
    }


    public OilContainerType(String key , IContainerFactory<T> factory) {
        super(factory);
        setRegistryName(new ResourceLocation("oilmod", key)); //oilmod as implementation independent
    }

    public interface IOilFactory<T extends Container & IOilContainer> {
        @OnlyIn(Dist.CLIENT)
        T create(OilContainerType<T> containerType, int id, PlayerInventory player, PacketBuffer extra);
    }


}
