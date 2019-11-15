package org.oilmod.oilforge.modloading;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider;
import net.minecraftforge.forgespi.language.ILifecycleEvent;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.Logging.LOADING;
import static net.minecraftforge.fml.Logging.SCAN;

public class OilModLanguageProvider implements IModLanguageProvider {
    //TODO: get inspired by FMLJavaModLanguageProvider


    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String name() {
        return "oilmod";
    }

    @Override
    public Consumer<ModFileScanData> getFileVisitor() {
        return scanResult -> {
            scanResult.getIModInfoData().forEach(modFileInfo -> LOGGER.debug("found modFileInfo: {}", modFileInfo));
            final Map<String, OilLoader> modTargetMap = scanResult.getIModInfoData().stream().filter(fileInfo -> fileInfo instanceof OilModFileInfo)
                    .map(fileInfo -> (OilModFileInfo)fileInfo)
                    .flatMap(fileInfo -> fileInfo.getOilMods().stream())
                    .peek(modInfo -> LOGGER.debug(SCAN, "Found oilmod-classpath {} with id {}", modInfo::getClasspath, modInfo::getModId))
                    .map(modInfo -> new OilLoader(modInfo.getClasspath(), modInfo.getModId()))
                    .collect(Collectors.toMap(OilLoader::getModId, Function.identity(), (a, b)->a));
        };
    }

    @Override
    public <R extends ILifecycleEvent<R>> void consumeLifecycleEvent(Supplier<R> consumeEvent) {

    }

    private static class OilLoader implements IModLanguageLoader {
        private static final Logger LOGGER = OilModLanguageProvider.LOGGER;
        private final String className;
        private final String modId;

        private OilLoader(String className, String modId)
        {
            this.className = className;
            this.modId = modId;
        }

        public String getModId()
        {
            return modId;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T loadMod(IModInfo info, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
            // This language class is loaded in the system level classloader - before the game even starts
            // So we must treat container construction as an arms length operation, and load the container
            // in the classloader of the game - the context classloader is appropriate here.
            try
            {
                final Class<?> fmlContainer = Class.forName("net.minecraftforge.fml.javafmlmod.FMLModContainer", true, Thread.currentThread().getContextClassLoader());
                LOGGER.debug(LOADING, "Loading FMLModContainer from classloader {} - got {}", Thread.currentThread().getContextClassLoader(), fmlContainer.getClassLoader());
                final Constructor<?> constructor = fmlContainer.getConstructor(IModInfo.class, String.class, ClassLoader.class, ModFileScanData.class);
                return (T)constructor.newInstance(info, className, modClassLoader, modFileScanResults);
            }
            catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e)
            {
                LOGGER.fatal(LOADING,"Unable to load FMLModContainer, wut?", e);
                throw new RuntimeException(e);
            }
        }
    }
}
