package org.oilmod.oilforge;


import cpw.mods.modlauncher.TransformingClassLoader;
import de.sirati97.minherit.Processor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.oilmod.api.OilMod;
import org.oilmod.api.inventory.InventoryFactory;
import org.oilmod.api.items.EnchantmentType;
import org.oilmod.api.items.internal.ItemClassMap;
import org.oilmod.api.items.internal.ItemFactory;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.items.type.ItemTypeHelper;
import org.oilmod.api.items.type.TBBType;
import org.oilmod.api.rep.RepAPI;
import org.oilmod.oilforge.enchantments.RealEnchantmentTypeHelper;
import org.oilmod.oilforge.inventory.RealInventoryFactory;
import org.oilmod.oilforge.inventory.container.ContainerPackageHandler;
import org.oilmod.oilforge.items.*;
import org.oilmod.oilforge.items.tools.RealTBBHelper;
import org.oilmod.oilforge.rep.RepAPIImpl;
import org.oilmod.spi.MPILoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;

public class OilMain {
    public static final Logger LOGGER = LogManager.getLogger();
    public static OilMod ModMinecraft;
    public static OilMod ModOilMod;

    private static final Method m;

    static {
        try {
            m = TransformingClassLoader.class.getDeclaredMethod("buildTransformedClassNodeFor", String.class, String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void init() {

        Optional<Consumer<String>> statusConsumer = StartupMessageManager.modLoaderConsumer();
        statusConsumer.ifPresent(c->c.accept("OilMod: performing preprocessing"));


        //todo cache
        Instant start = Instant.now();
        Processor.readClass = (clazz) -> {
            try {
                ClassLoader cl = clazz.getClassLoader();
                if (cl instanceof TransformingClassLoader) {
                    m.setAccessible(true);
                    byte[] bytes = (byte[]) m.invoke(cl, clazz.getName(), "classloading");
                    m.setAccessible(false);
                    return new ClassReader(bytes);
                }
                return new ClassReader(clazz.getResourceAsStream(clazz.getSimpleName() + ".class"));
            } catch (IOException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        };



        statusConsumer.ifPresent(c->c.accept("OilMod: init MPIs"));
        ContainerPackageHandler.registerPackets();
        MPILoader.init(); //this gonna get more complicated in the future but its okay for now
        RepAPI.installImplementation(new RepAPIImpl());


        RealItemClassMap itemClassMap = new RealItemClassMap();
        MPILoader.commitDependency(itemClassMap); //for RealItemRegistryHelper
        Validate.notNull(RealItemRegistryHelper.INSTANCE);



        ItemFactory.setInstance(new RealItemFactory());
        ItemClassMap.setInstance(itemClassMap);
        InventoryFactory.setInstance(new RealInventoryFactory());

        EnchantmentType.EnchantmentTypeHelper.setInstance(new RealEnchantmentTypeHelper());
        ItemTypeHelper.setInstance(new RealItemTypeHelper());
        initReflection();
        //YBase.registerYAMLClasses();

        //todo by here API should be completely initialised, i.e. no hanging dependencies MPILoader.xXX()

        long timeElapsed = Duration.between(start, Instant.now()).toMillis();  //in millis
        statusConsumer.ifPresent(c->c.accept(String.format("OilMod: init took %d ms", timeElapsed)));
        LOGGER.info("Initialising OilMod-API took {} ms", timeElapsed);

        statusConsumer.ifPresent(c->c.accept(String.format("OilMod: Constructing %d delayed OilMods", OilAPIInitEvent.getWaiting())));
        OilAPIInitEvent.fire();
    }

    public static void printTrace(String text) {
        System.out.println("Printing stack trace for " + text + ":");
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < elements.length; i++) {
            StackTraceElement s = elements[i];
            System.out.println("\tat " + s.getClassName() + "." + s.getMethodName()
                    + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
        }
    }


    public static void initReflection() {
        //should i ever use reflection - check it here to fire missing fields exceptions early
    }
}
