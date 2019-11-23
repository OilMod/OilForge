package org.oilmod.oilforge.modloader;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.SimpleBakedModel;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.OilMod;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlockProvider;
import org.oilmod.api.rep.providers.minecraft.MinecraftItemProvider;
import org.oilmod.oilforge.OilAPIInitEvent;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.internaltest.testmod1.TestMod1;
import org.oilmod.oilforge.inventory.container.OilContainerType;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.items.RealItemRegistryHelper;
import org.oilmod.oilforge.items.capability.ModInventoryObjectProvider;
import org.oilmod.oilforge.items.capability.OilItemStackHandler;
import org.oilmod.oilforge.OilModContext;
import org.oilmod.oilforge.modloading.ModUtil;
import org.oilmod.oilforge.rep.minecraft.MC113ItemProvider;

import java.util.Map;

import static org.oilmod.oilforge.Util.isModStack;
import static org.oilmod.oilforge.Util.toReal;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oilforgeapi")@Mod.EventBusSubscriber
public class OilModLoaderMod
{
    public static ServerWorld serverWorldDimOverworld; //this is just here temporary for debugging purposes

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    private TestMod1 mod1;

    public OilModLoaderMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, EventPriority.HIGHEST, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, EventPriority.HIGHEST, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainerType);
        //FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ItemStack.class, this::attackCapabilities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modelBakeEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::attachCapabilities);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        OilAPIInitEvent.addListener(this::onAPIInit);



        OilMain.init();

        mod1 =  OilMod.ModHelper.createInstance(TestMod1.class,OilMod.ModHelper.getDefaultContext(),"testmod1", "Internal Test Mod1"); // registers itself

        LOGGER.info("DID SETUP DESDTRFYOIHKJLLHGOTRD, 1 mod manually loaded");
    }

    public void onAPIInit() {
        LOGGER.info("oilforgeapi received API init event");
    }


    public void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("OilForgeApi received FMLCommonSetupEvent");

        ModUtil.invokeMissingRegistries(mod1);
        OilItemStackHandler.register();


        LOGGER.info("OilForgeApi registered Capability {}", OilItemStackHandler.CAPABILITY);
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> attachCapabilitiesEvent) {

        ItemStack stack = attachCapabilitiesEvent.getObject();
        if (isModStack(stack)) {
            ModInventoryObjectProvider pro = new ModInventoryObjectProvider(stack); //do everything lazily, we need to wait for other capability to be initialised
            attachCapabilitiesEvent.addListener(pro::invalidate);
            attachCapabilitiesEvent.addCapability(new ResourceLocation("oilforgeapi", "mod_inventory_object_cap_provider"), pro);
        }

        //if (attachCapabilitiesEvent.getObject() instanceof RealItemImplHelper) {
        //
        //}
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    public void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent) {
        LOGGER.debug("OilForgeApi received RegistryEvent.Register<Block>, initialising block providers");
        MinecraftBlockProvider.init();
        // register a new block here
    }
    public void registerItems(RegistryEvent.Register<Item> event) {
        LOGGER.debug("OilForgeApi received RegistryEvent.Register<Item>, initialising item providers");
        IForgeRegistry<Item> itemRegistry = event.getRegistry();
        ((MC113ItemProvider)MinecraftItemProvider.getInstance()).setItemRegistry(itemRegistry);
        MinecraftItemProvider.init();

        //
        OilModContext context = (OilModContext) mod1.getContext();
        context.itemRegistry = itemRegistry;

        RealModHelper.invokeRegisterItemFilters(mod1);
        RealModHelper.invokeRegisterItems(mod1);

        context.itemRegistry = null;

    }



    public void registerContainerType(RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> reg = event.getRegistry();
        OilContainerType.toBeRegistered.forEach(reg::register);
    }

    /*public void registerOilMod(OilModContainer container) {
        LOGGER.debug("registerOilMod was called for container {} containing {}", container::toString, container::getModId);
    }*/

    public void modelBakeEvent(ModelBakeEvent event) {
        LOGGER.debug("OilForgeApi received ModelBakeEvent");
        //TODO: move to client only class


        Map<ResourceLocation, IBakedModel> models = event.getModelRegistry();

        IBakedModel missing = event.getModelManager().getMissingModel();
        ResourceLocation missingNo = ((SimpleBakedModel)missing).getParticleTexture().getName();

        for (Item item: RealItemRegistryHelper.INSTANCE.allRegistered) {
            Item from = ((RealItemImplHelper)item).getVanillaFakeItem(toReal(item.getDefaultInstance()));
            ModelResourceLocation to3 = new ModelResourceLocation(item.getRegistryName(), "inventory");

            IBakedModel proviced =models.get(to3);
            if (!(proviced instanceof SimpleBakedModel && ((SimpleBakedModel)proviced).getParticleTexture().getName().equals(missingNo))) {
                LOGGER.debug("skipped adding copying model for {} as texture was provided: {}", item::getRegistryName, ()->proviced);
                continue;
            }


            ModelResourceLocation form3 = new ModelResourceLocation(from.getRegistryName(), "inventory");

            IBakedModel model = models.get(form3);
            models.put(to3, model);


            LOGGER.debug("tried to copy model from {} to {}, copied model: {}", from::getRegistryName, item::getRegistryName, model::toString);
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class
    @Mod.EventBusSubscriber
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarting(FMLServerStartingEvent event) {
            // do something when the server starts
            LOGGER.info("HELLO from server starting");
            serverWorldDimOverworld = event.getServer().getWorld(DimensionType.OVERWORLD);
        }
        @SubscribeEvent
        public static void onServerStarting(FMLServerStoppedEvent event) {
            // do something when the server starts
            LOGGER.info("BYE from server stopped");
            serverWorldDimOverworld = null;
        }
    }
}
