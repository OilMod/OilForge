package org.oilmod.oilforge.modloader;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlockProvider;
import org.oilmod.api.rep.providers.minecraft.MinecraftItemProvider;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.internaltest.testmod1.TestMod1;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.items.RealItemStack;
import org.oilmod.oilforge.items.capability.ModInventoryObjectProvider;
import org.oilmod.oilforge.items.capability.OilItemStackHandler;
import org.oilmod.oilforge.items.capability.OilItemStackProvider;
import org.oilmod.oilforge.rep.minecraft.MC113ItemProvider;

import java.util.Map;

import static org.oilmod.oilforge.Util.isModStack;
import static org.oilmod.oilforge.Util.toReal;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oilmodloader")@Mod.EventBusSubscriber
public class OilModLoaderMod
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public OilModLoaderMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        //FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ItemStack.class, this::attackCapabilities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::modelBakeEvent);


        OilMain.init();

        TestMod1 mod1 = new TestMod1(); // registers itself


        RealModHelper.initialiseAll();

        LOGGER.info("DID SETUP DESDTRFYOIHKJLLHGOTRD, 1 mod loaded");
    }


    public void commonSetup(FMLCommonSetupEvent event) {


        OilItemStackHandler.register();
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> attachCapabilitiesEvent) {

        ItemStack stack = attachCapabilitiesEvent.getObject();
        if (isModStack(stack)) {
            ModInventoryObjectProvider pro = new ModInventoryObjectProvider(stack); //do everything lazily, we need to wait for other capability to be initialised
            attachCapabilitiesEvent.addListener(pro::invalidate);
            attachCapabilitiesEvent.addCapability(new ResourceLocation("oilmodloader", "mod_inventory_object_cap_provider"), pro);
        }

        //if (attachCapabilitiesEvent.getObject() instanceof RealItemImplHelper) {
        //
        //}
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    public void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        MinecraftBlockProvider.init();
        // register a new block here
        LOGGER.info("HELLO from Register Block");
    }

    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> itemRegistry = event.getRegistry();
        ((MC113ItemProvider)MinecraftItemProvider.getInstance()).setItemRegistry(itemRegistry);
        MinecraftItemProvider.init();
        RealModHelper.invokeRegisterItemsAll();

        LOGGER.info("111 123e4567-e89b-12d3-a456-426655440000 going to register {} items", OilMain.realItemRegistryHelper.toBeRegistered.size());
        for (Item item:OilMain.realItemRegistryHelper.toBeRegistered) {
            itemRegistry.register(item);
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
