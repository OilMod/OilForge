package org.oilmod.oilforge.multipart;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.multipart.client.MultiPartRender;
import org.oilmod.oilforge.multipart.client.TestMultiBlockRender;

import java.util.Collections;

@Mod("multiparttest")@Mod.EventBusSubscriber
public class MultiPartMod {
    public static final TestMultiBlock testBlock;
    public static final BlockItem testBlockItem;
    public static final TileEntityType<TestMultiBlockTileEntity> testBlockTileEntity;

    static {
        testBlock = new TestMultiBlock(Block.Properties.create(Material.ROCK));
        testBlock.setRegistryName("test_part");
        testBlockItem = new BlockItem(testBlock, new Item.Properties());
        testBlockItem.setRegistryName("test_part");
        testBlockTileEntity = new TileEntityType<>(() -> new TestMultiBlockTileEntity(MultiPartMod.testBlockTileEntity), Collections.singleton(testBlock), null);
        testBlockTileEntity.setRegistryName("test_part");
    }

    public static final MultiPartBlock block;
    public static final BlockItem item;
    public static final TileEntityType<MultiPartTileEntity> tileEntity;

    static {
        block = new MultiPartBlock(Block.Properties.create(Material.ROCK));
        block.setRegistryName("multipart");
        item = new BlockItem(block, new Item.Properties());
        item.setRegistryName("multipart");
        tileEntity = new TileEntityType<>(() -> new MultiPartTileEntity(MultiPartMod.tileEntity), Collections.singleton(block), null);
        tileEntity.setRegistryName("multipart");
    }

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public MultiPartMod() {
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        eventbus.addGenericListener(Block.class, this::registerBlocks);
        eventbus.addGenericListener(Item.class, this::registerItems);
        eventbus.addGenericListener(TileEntityType.class, this::registerTileEntityType);
        eventbus.addListener(this::clientSetup);
    }


    public void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        blockRegistryEvent.getRegistry().register(testBlock);
        blockRegistryEvent.getRegistry().register(block);
    }
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(testBlockItem);
        event.getRegistry().register(item);
    }

    public void registerTileEntityType(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(testBlockTileEntity);
        event.getRegistry().register(tileEntity);
    }

    public void clientSetup(FMLClientSetupEvent event) {

        ClientRegistry.bindTileEntityRenderer(tileEntity, MultiPartRender::new);
        ClientRegistry.bindTileEntityRenderer(testBlockTileEntity, TestMultiBlockRender::new);
    }

}
