package org.oilmod.oilforge.modloading;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.oilmod.api.OilMod;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.rep.providers.minecraft.MinecraftItemProvider;
import org.oilmod.api.stateable.complex.ComplexStateTypeRegistry;
import org.oilmod.oilforge.OilModContext;
import org.oilmod.oilforge.block.IBlockItemRegistry;

public class OilEvents {
    private OilMod mod;

    public OilEvents(IEventBus eventbus) {
        eventbus.addGenericListener(Block.class, this::registerBlocks);
        eventbus.addGenericListener(Item.class, this::registerItems);
        eventbus.addGenericListener(TileEntityType.class, this::registerTileEntityType);
        eventbus.addListener(EventPriority.HIGHEST,this::commonSetup);
    }
    public void setOilMod(OilMod oilMod) {
        this.mod = oilMod;
    }

    public OilMod getOilMod() {
        return mod;
    }

    public void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        OilModContext context = (OilModContext) getOilMod().getContext();
        context.blockRegistry = blockRegistryEvent.getRegistry();

        ModUtil.invokeRegisterBlocks(getOilMod());

        context.blockRegistry = null;
    }
    public void registerItems(RegistryEvent.Register<Item> event) {
        OilModContext context = (OilModContext) getOilMod().getContext();
        context.itemRegistry = event.getRegistry();

        ModUtil.invokeRegisterItemFilters(getOilMod());
        ModUtil.invokeRegisterItems(getOilMod());
        ((IBlockItemRegistry) OilMod.ModHelper.getRegistry(getOilMod(), BlockRegistry.class)).registerBlockItems(); //we register blocks here because registerItems is called before registerBlocks and some blocks have an item version
        context.itemRegistry = null;
    }

    public void registerTileEntityType(RegistryEvent.Register<TileEntityType<?>> event) {
        OilModContext context = (OilModContext) getOilMod().getContext();
        context.tileEntityTypeRegistry =  event.getRegistry();

        ModUtil.invokeRegister(getOilMod(), ComplexStateTypeRegistry.class);

        context.blockRegistry = null;
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        ModUtil.invokeMissingRegistries(getOilMod());
    }

}
