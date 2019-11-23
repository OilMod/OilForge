package org.oilmod.oilforge.modloading;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.EventBusErrorMessage;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.fml.*;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.OilMod;
import org.oilmod.oilforge.OilAPIInitEvent;
import org.oilmod.oilforge.OilModContext;

import java.util.Optional;
import java.util.function.Consumer;

import static net.minecraftforge.fml.Logging.LOADING;
import static org.oilmod.api.OilMod.ModHelper.getDefaultContext;

public class OilModContainer extends ModContainer { //would like to overwrite fmlmodcontainer, but cannot overwrite private methods :(
    private static final Logger LOGGER = LogManager.getLogger();
    private final ModFileScanData scanResults;
    private final IEventBus eventBus;
    private OilMod modInstance;
    private final Class<? extends OilMod> modClass;

    public OilModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults)
    {
        super(info);
        LOGGER.debug(LOADING,"Creating OilModContainer instance for {} with classLoader {} & {}", className, modClassLoader, getClass().getClassLoader());
        this.scanResults = modFileScanResults;
        triggerMap.put(ModLoadingStage.CONSTRUCT, dummy().andThen(this::beforeEvent).andThen(this::constructMod).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.CREATE_REGISTRIES, dummy().andThen(this::checkConstructState).andThen(this::beforeEvent).andThen(this::fireEvent).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.LOAD_REGISTRIES, dummy().andThen(this::beforeEvent).andThen(this::fireEvent).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.COMMON_SETUP, dummy().andThen(this::beforeEvent).andThen(this::preinitMod).andThen(this::fireEvent).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.SIDED_SETUP, dummy().andThen(this::beforeEvent).andThen(this::fireEvent).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.ENQUEUE_IMC, dummy().andThen(this::beforeEvent).andThen(this::initMod).andThen(this::fireEvent).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.PROCESS_IMC, dummy().andThen(this::beforeEvent).andThen(this::fireEvent).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.COMPLETE, dummy().andThen(this::beforeEvent).andThen(this::completeLoading).andThen(this::fireEvent).andThen(this::afterEvent));
        triggerMap.put(ModLoadingStage.GATHERDATA, dummy().andThen(this::beforeEvent).andThen(this::fireEvent).andThen(this::afterEvent));
        this.eventBus = BusBuilder.builder().setExceptionHandler(this::onEventFailed).setTrackPhases(false).build();
        this.configHandler = Optional.of(this.eventBus::post);
        //OilMod
        final OilJavaModLoadingContext contextExtension = new OilJavaModLoadingContext(this);
        this.contextExtension = () -> contextExtension;
        try
        {            //OilMod

            //noinspection unchecked
            modClass = (Class<? extends OilMod>) Class.forName(className, true, modClassLoader);
            LOGGER.debug(LOADING,"Loaded modclass {} with {}", modClass.getName(), modClass.getClassLoader());
        }
        catch (Throwable e)
        {
            LOGGER.error(LOADING, "Failed to load class {}", className, e);
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
        }

        //OilMod
        //register events


        OilAPIInitEvent.addListener(this::onOilAPIInitEvent);
        eventBus.addGenericListener(Item.class, this::registerItems); //if this fails, its indicated that classloader stuff went wrong (this class cannot be classloaded by AppClassLoader etc must be e.g. TransformingClassLoader)
    }

    private void checkConstructState(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {
        Validate.notNull(modInstance, "Previous stage did not complete, as OilAPIInitEvent was not fired");
    }

    //OilMod
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> itemRegistry = event.getRegistry();
        OilModContext context = (OilModContext) modInstance.getContext();
        context.itemRegistry = itemRegistry;

        ModUtil.invokeRegisterItemFilters(modInstance);
        ModUtil.invokeRegisterItems(modInstance);

        context.itemRegistry = null;

    }


    @Override
    public IModInfo getModInfo() {
        return super.getModInfo();
    }

    private void completeLoading(LifecycleEventProvider.LifecycleEvent lifecycleEvent)
    {

    }

    private void initMod(LifecycleEventProvider.LifecycleEvent lifecycleEvent)
    {

    }

    private Consumer<LifecycleEventProvider.LifecycleEvent> dummy() { return (s) -> {}; }

    private void onEventFailed(IEventBus iEventBus, Event event, IEventListener[] iEventListeners, int i, Throwable throwable)
    {
        LOGGER.error(new EventBusErrorMessage(event, i, iEventListeners, throwable));
    }

    private void beforeEvent(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {
    }

    private void fireEvent(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {
        final Event event = lifecycleEvent.getOrBuildEvent(this);
        LOGGER.debug(LOADING, "Firing event for modid {} : {}", this.getModId(), event);
        try
        {
            eventBus.post(event);
            LOGGER.debug(LOADING, "Fired event for modid {} : {}", this.getModId(), event);
        }
        catch (Throwable e)
        {
            LOGGER.error(LOADING,"Caught exception during event {} dispatch for modid {}", event, this.getModId(), e);
            throw new ModLoadingException(modInfo, lifecycleEvent.fromStage(), "fml.modloading.errorduringevent", e);
        }
    }

    private void afterEvent(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {
        if (getCurrentState() == ModLoadingStage.ERROR) {
            LOGGER.error(LOADING,"An error occurred while dispatching event {} to {}", lifecycleEvent.fromStage(), getModId());
        }
    }

    private void preinitMod(LifecycleEventProvider.LifecycleEvent lifecycleEvent)
    {
        ModUtil.invokeMissingRegistries(modInstance);
    }

    private boolean oilAPIinit = false;
    private boolean observedPhaseConstruct = false;
    private void onOilAPIInitEvent() {
        LOGGER.debug("Received OilAPI init event for {}", getModId());
        synchronized (apiInitMutex) {
            oilAPIinit = true;
            _constructMod();
        }
    }



    private void constructMod(LifecycleEventProvider.LifecycleEvent event)
    {

        synchronized (apiInitMutex) {
            observedPhaseConstruct = true;
            _constructMod();
        }
    }


    private final Object apiInitMutex = new Object();
    private void _constructMod()
    {
        synchronized (apiInitMutex) {
            if (!observedPhaseConstruct) {
                LOGGER.debug("Received OilAPI init event before construct phase");
                return;
            }
            if (!oilAPIinit) {
                LOGGER.warn("Async-initialisation-order unfavorable, delegating mod construction until after OIL-API is initialised");
                return;
            }
            //basically whichever method onOilAPIInitEvent/constructMod runs last does the job (lazy init)
            try
            {
                LOGGER.debug(LOADING, "Loading mod instance {} of type {}", getModId(), modClass.getName());
                //OilMod
                this.modInstance = OilMod.ModHelper.createInstance(modClass, getDefaultContext(), getModId(), getModInfo().getDisplayName());



                LOGGER.debug(LOADING, "Loaded mod instance {} of type {}", getModId(), modClass.getName());
            }
            catch (Throwable e)
            {
                LOGGER.error(LOADING,"Failed to create mod instance. ModID: {}, class {}", getModId(), modClass.getName(), e);
                //todo try to somehow get the stage (should always be CONSTRUCT)
                throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, modClass);
            }
            LOGGER.debug(LOADING, "Injecting Automatic event subscribers for {}", getModId());
            AutomaticEventSubscriber.inject(this, this.scanResults, this.modClass.getClassLoader());
            LOGGER.debug(LOADING, "Completed Automatic event subscribers for {}", getModId());
        }

    }

    @Override
    public boolean matches(Object mod)
    {
        return mod == modInstance;
    }

    @Override
    public OilMod getMod()
    {
        return modInstance;
    }

    public IEventBus getEventBus()
    {
        return this.eventBus;
    }
}
