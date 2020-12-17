package org.oilmod.oilforge.modloading;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.EventBusErrorMessage;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.OilMod;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.stateable.complex.ComplexStateTypeRegistry;
import org.oilmod.oilforge.OilAPIInitEvent;
import org.oilmod.oilforge.OilModContext;
import org.oilmod.oilforge.block.IBlockItemRegistry;

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
    private final OilEvents oilEvents;

    public OilModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults)
    {
        super(info);
        LOGGER.debug(LOADING,"Creating OilModContainer instance for {} with classLoader {} & {}", className, modClassLoader, getClass().getClassLoader());
        this.scanResults = modFileScanResults;

        activityMap.put(ModLoadingStage.CONSTRUCT, this::constructMod);
        this.eventBus = BusBuilder.builder().setExceptionHandler(this::onEventFailed).setTrackPhases(false).markerType(IModBusEvent.class).build();
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
        this.oilEvents = new OilEvents(eventBus); //if this fails, its indicated that classloader stuff went wrong (this class cannot be classloaded by e.g. AppClassLoader etc must be TransformingClassLoader)
    }

    private void checkConstructState() {
        Validate.notNull(modInstance, "Previous stage did not complete, as OilAPIInitEvent was not fired");
    }


    @Override
    public IModInfo getModInfo() {
        return super.getModInfo();
    }


    private void onEventFailed(IEventBus iEventBus, Event event, IEventListener[] iEventListeners, int i, Throwable throwable)
    {
        LOGGER.error(new EventBusErrorMessage(event, i, iEventListeners, throwable));
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



    private void constructMod()
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
                this.oilEvents.setOilMod(modInstance);



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

    @Override
    protected <T extends Event & IModBusEvent> void acceptEvent(final T e) {
        if (!(e instanceof FMLConstructModEvent)) {
            checkConstructState();
        }
        try {
            LOGGER.debug(LOADING, "Firing event for modid {} : {}", this.getModId(), e);
            this.eventBus.post(e);
            LOGGER.debug(LOADING, "Fired event for modid {} : {}", this.getModId(), e);
        } catch (Throwable t) {
            LOGGER.error(LOADING,"Caught exception during event {} dispatch for modid {}", e, this.getModId(), t);
            throw new ModLoadingException(modInfo, modLoadingStage, "fml.modloading.errorduringevent", t);
        }
    }
}
