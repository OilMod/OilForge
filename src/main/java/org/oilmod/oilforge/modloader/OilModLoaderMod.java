package org.oilmod.oilforge.modloader;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.internaltest.testmod1.TestMod1;
import org.oilmod.oilforge.items.RealItem;
import org.oilmod.oilforge.items.RealItemImplHelper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

import static org.oilmod.oilforge.dirtyhacks.RealItemStackHelper.toReal;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oilmodloader")@Mod.EventBusSubscriber
public class OilModLoaderMod
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public OilModLoaderMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::modelBakeEvent);


        OilMain.init();

        TestMod1 mod1 = new TestMod1();
        mod1.init();


        LOGGER.info("DID SETUP DESDTRFYOIHKJLLHGOTRD, 1 mod loaded");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    public void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        // register a new block here
        LOGGER.info("HELLO from Register Block");
    }

    public void registerItems(RegistryEvent.Register<Item> event) {

        LOGGER.info("111 123e4567-e89b-12d3-a456-426655440000 going to register {} items", OilMain.realItemRegistryHelper.toBeRegistered.size());
        for (Item item:OilMain.realItemRegistryHelper.toBeRegistered) {
            event.getRegistry().register(item);
            LOGGER.info("registered {}", item.getRegistryName().toString());
        }
    }

    public void modelBakeEvent(ModelBakeEvent event) {
        LOGGER.warn("modelBakeEvent was called");
        //TODO: move to client only class


        Map<ModelResourceLocation, IBakedModel> models = event.getModelRegistry();

        for (Item item:OilMain.realItemRegistryHelper.toBeRegistered) {
            Item from = ((RealItemImplHelper)item).getVanillaFakeItem(toReal(item.getDefaultInstance()));
            ModelResourceLocation form3 = new ModelResourceLocation(from.getRegistryName(), "inventory");
            ModelResourceLocation to3 = new ModelResourceLocation(item.getRegistryName(), "inventory");

            IBakedModel model = models.get(form3);
            models.put(to3, model);


            LOGGER.info("tries to copy model from {} to {}, copied model: {}", from.getRegistryName(), item.getRegistryName(), model);
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class
    @Mod.EventBusSubscriber
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarting(FMLServerStartingEvent event) {
            // do something when the server starts
            LOGGER.info("HELLO from server starting");
        }
    }
}
