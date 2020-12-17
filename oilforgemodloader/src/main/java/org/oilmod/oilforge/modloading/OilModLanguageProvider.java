package org.oilmod.oilforge.modloading;

import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.Logging.LOADING;
import static net.minecraftforge.fml.Logging.SCAN;

public class OilModLanguageProvider implements IModLanguageProvider {
    //TODO: get inspired by FMLJavaModLanguageProvider

    public OilModLanguageProvider() {
        LOGGER.info("Instance of {} was created", this.getClass()::getSimpleName);
    }

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String name() {
        return "oilmod";
    }

    @Override
    public Consumer<ModFileScanData> getFileVisitor() {
        return scanResult -> {
            scanResult.getIModInfoData().forEach(modFileInfo -> LOGGER.debug(SCAN, "found modFileInfo: {}", modFileInfo));
            //Original, broken due to forge not allowing other implementations of IModFileInfo
            /*final Map<String, OilLoader> modTargetMap = scanResult.getIModInfoData().stream().filter(fileInfo -> fileInfo instanceof OilModFileInfo)
                    .map(fileInfo -> (OilModFileInfo)fileInfo)
                    .flatMap(fileInfo -> fileInfo.getOilMods().stream())
                    .peek(modInfo -> LOGGER.debug(SCAN, "Found oilmod-classpath {} with id {}", modInfo::getClasspath, modInfo::getModId))
                    .map(modInfo -> new OilLoader(modInfo.getClasspath(), modInfo.getModId()))
                    .collect(Collectors.toMap(OilLoader::getModId, Function.identity(), (a, b)->a));*/
            final Map<String, OilLoader> modTargetMap = scanResult.getIModInfoData().stream()
                    .flatMap(fileInfo -> fileInfo.getMods().stream())
                    .peek(oilmod -> LOGGER.debug(SCAN, "Found oilmod: {}", oilmod::getModId))
                    .peek(modInfo -> LOGGER.debug(SCAN, "Found oilmod-classpath {} with id {}", () -> OilModLanguageProvider.getClasspath(modInfo), modInfo::getModId))
                    .map(modInfo -> new OilLoader(OilModLanguageProvider.getClasspath(modInfo), modInfo.getModId()))
                    .collect(Collectors.toMap(OilLoader::getModId, Function.identity(), (a, b)->a));

            /*Optional<IModFileInfo> apiImplOptional = scanResult.getIModInfoData().stream()
                    .filter(fileInfo -> fileInfo.getMods().stream().anyMatch(modInfo -> modInfo.getModId().equals("oilforgeapi")))
                    .findAny();
            if (apiImplOptional.isPresent()) {
                ModFileInfo apiImpl = (ModFileInfo) apiImplOptional.get();
                ModInfo modInfo = OilModFileInfoHelper.createModInfo(apiImpl, OilModFileParser.readModList(apiImpl.getFile()).getConfig());
                apiImpl.getMods().add(modInfo);

                OilLoader oilLoader = new OilLoader(getClasspath(modInfo), modInfo.getModId());
                modTargetMap.put(oilLoader.getModId(), oilLoader);

            }*/

            scanResult.addLanguageLoader(modTargetMap);
        };
    }

    private static MethodHandle method;
    private static String getClasspath(IModInfo modInfo) {
        if (method == null) {
            MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
            MethodType mt = MethodType.methodType(String.class);
            try {
                method = publicLookup.findVirtual(modInfo.getClass(), "getClasspath", mt);
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        try {
            return (String) method.invoke(modInfo);
        } catch (Throwable throwable) {
            throw new IllegalStateException(throwable);
        }
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
                final Class<?> oilContainer = Class.forName("org.oilmod.oilforge.modloading.OilModContainer", true, Thread.currentThread().getContextClassLoader());
                LOGGER.debug(LOADING, "Loading OilModContainer from classloader {} - got {}", Thread.currentThread().getContextClassLoader(), oilContainer.getClassLoader());
                final Constructor<?> constructor = oilContainer.getConstructor(IModInfo.class, String.class, ClassLoader.class, ModFileScanData.class);
                return (T)constructor.newInstance(info, className, modClassLoader, modFileScanResults);
            }
            catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e)
            {
                LOGGER.fatal(LOADING,"Unable to load OilModContainer, wut?", e);
                throw new RuntimeException(e);
            }
        }
    }
}
