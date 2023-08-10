package org.oilmod.oilforge.modloader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.SimpleBakedModel;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.OilMod;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.registry.RegistryHelperBase;
import org.oilmod.api.rep.providers.minecraft.MinecraftBlockProvider;
import org.oilmod.api.rep.providers.minecraft.MinecraftItemProvider;
import org.oilmod.oilforge.OilAPIInitEvent;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.block.RealBlockImplHelper;
import org.oilmod.oilforge.block.RealBlockRegistryHelper;
import org.oilmod.oilforge.internaltest.testmod1.TestMod1;
import org.oilmod.oilforge.inventory.container.OilContainerType;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.items.RealItemRegistryHelper;
import org.oilmod.oilforge.items.capability.ModInventoryObjectProvider;
import org.oilmod.oilforge.items.capability.OilItemStackHandler;
import org.oilmod.oilforge.loottable.RealBlockLootEntry;
import org.oilmod.oilforge.modloader.client.ClientSetup;
import org.oilmod.oilforge.modloader.server.ServerSetup;
import org.oilmod.oilforge.modloading.OilEvents;
import org.oilmod.oilforge.rep.minecraft.MC113ItemProvider;
import org.oilmod.oilforge.resource.OilPackFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.loot.LootTable.EMPTY_LOOT_TABLE;
import static org.oilmod.oilforge.Util.isModStack;
import static org.oilmod.oilforge.Util.toReal;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oilforgeapi")@Mod.EventBusSubscriber
public class OilForgeMod
{
    public static ServerWorld serverWorldDimOverworld; //this is just here temporary for debugging purposes


    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    private TestMod1 mod1;
    private OilEvents mod1ForgeEvents;
    private Setup setup;

    public OilForgeMod() {
        setup = DistExecutor.runForDist(() -> ClientSetup::new, () -> ServerSetup::new);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, EventPriority.HIGHEST, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, EventPriority.HIGHEST, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainerType);
        //FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ItemStack.class, this::attackCapabilities); //i love how this is called attackCapabilities and not attachCapabilities
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.HIGHEST,  this::freezeMods);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modelBakeEvent);
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::lootTableLoadEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetupEvent);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        OilAPIInitEvent.addListener(this::onAPIInit);

        Minecraft.getInstance().getResourcePackList().addPackFinder(new OilPackFinder());

        OilMain.init();

        mod1 =  OilMod.ModHelper.createInstance(TestMod1.class,OilMod.ModHelper.getDefaultContext(),"testmod1", "Internal Test Mod1"); // registers itself
        mod1ForgeEvents = new OilModForgeAdapterBase();
        mod1ForgeEvents.setOilMod(mod1);

        Minecraft.getInstance().getResourcePackList().addPackFinder(new OilPackFinder());

        //Minecraft.getInstance().getResourceManager().addResourcePack(new OilDummyResourcePack());
        LOGGER.info("DID SETUP DESDTRFYOIHKJLLHGOTRD, 1 mod manually loaded");
    }

    public void onAPIInit() {
        LOGGER.info("oilforgeapi received API init event");
    }


    public void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("OilForgeApi received FMLCommonSetupEvent");

        OilItemStackHandler.register();

        LOGGER.info("OilForgeApi registered Capability {}", OilItemStackHandler.CAPABILITY);


        DeferredWorkQueue.runLater(RegistryHelperBase::assertAllEventsFired);
    }




    //abuse newregistry event at highest to finish mod creation phase
    public void freezeMods(RegistryEvent.NewRegistry event) {
        ((RealModHelper)RealModHelper.getInstance()).freeze(); //wooo i love this --- not
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
    }
    public void registerItems(RegistryEvent.Register<Item> event) {
        LOGGER.debug("OilForgeApi received RegistryEvent.Register<Item>, initialising item providers");
        IForgeRegistry<Item> itemRegistry = event.getRegistry();
        ((MC113ItemProvider)MinecraftItemProvider.getInstance()).setItemRegistry(itemRegistry);
        MinecraftItemProvider.init();
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
        Map<ResourceLocation, List<ModelResourceLocation>> modelsNoState = new HashMap<>();


        for (Map.Entry<ResourceLocation, IBakedModel> x:models.entrySet()) {
            modelsNoState.computeIfAbsent(new ResourceLocation(x.getKey().getNamespace(), x.getKey().getPath()), (r)-> new ArrayList<>()).add((ModelResourceLocation) x.getKey());
            if (x.getValue().getOverrides() != ItemOverrideList.EMPTY) {
                LOGGER.debug("{} -> {}", x.getKey()::toString, x.getValue().getClass()::getSimpleName);
            }
        }

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


        for (Block block: RealBlockRegistryHelper.INSTANCE.allRegistered) {
            BlockState from = ((RealBlockImplHelper)block).getVanillaFakeBlock();

            List<ModelResourceLocation> to3 = modelsNoState.get(block.getRegistryName());
            LOGGER.debug(to3);

            ModelResourceLocation from4 = BlockModelShapes.getModelLocation(from);
            for (int i = 0; i < to3.size(); i++) {
                ModelResourceLocation to4 = to3.get(i);

                IBakedModel proviced =models.get(to4);
                if (!(proviced instanceof SimpleBakedModel && ((SimpleBakedModel)proviced).getParticleTexture().getName().equals(missingNo))) {
                    LOGGER.debug("skipped adding copying model for {} as model was provided: {}", to4, proviced);
                    continue;
                }

                IBakedModel model = models.get(from4);

                models.put(to4, model);
                LOGGER.debug("tried to copy model from {} to {}, copied model: {}", from4, to4, model);

            }


             }

    }


    @SubscribeEvent
    public void lootTableLoadEvent(LootTableLoadEvent event) {
        if (!event.getName().toString().toLowerCase().contains("minecraft")) {
            //todo do better detection, only replace loottable for when none is provided
            OilMod mod = OilMod.getByName(event.getName().getNamespace());
            if (mod == null) {
                LOGGER.debug("Loottable is for a forge mod {}, this is not an oilmod", event.getName().getNamespace());
                return;
            }
            String path =event.getName().getPath();
            path = path.substring(path.lastIndexOf('/') + 1);
            OilBlock block = OilMod.ModHelper.getRegistry(mod, BlockRegistry.class).get(path);
            if (block == null) {
                LOGGER.debug("Could not find block for mod {} with name {} given path {}", mod.getInternalName(), path, event.getName());
                return;
            }

            LootPool pool = LootPool.builder().addEntry(RealBlockLootEntry.builder(block)).name("OilDropPool").build();

            event.getTable().addPool(pool);
        }
        if (event.getTable() == EMPTY_LOOT_TABLE) {
            ResourceLocation key = event.getName();
            if (key.getNamespace().equals("testmod1")) {
                LOGGER.debug("event.getTable() == EMPTY_LOOT_TABLE for {}", key);
            }
        }

    }

    public void clientSetupEvent(FMLClientSetupEvent event) {
        setup.clientSetup(event);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class
    @Mod.EventBusSubscriber
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
            event.getServer().getResourcePacks().addPackFinder(new OilPackFinder());
        }
        @SubscribeEvent
        public static void onServerStarting(FMLServerStartingEvent event) {
            // do something when the server starts
            LOGGER.info("HELLO from server starting");
            serverWorldDimOverworld = event.getServer().getWorld(World.OVERWORLD);
        }
        @SubscribeEvent
        public static void onServerStarting(FMLServerStoppedEvent event) {
            // do something when the server starts
            LOGGER.info("BYE from server stopped");
            serverWorldDimOverworld = null;
        }
    }
}
