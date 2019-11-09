package org.oilmod.oilforge.modloading;

import net.minecraftforge.forgespi.language.ILifecycleEvent;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
            //LOGGER.info(scanResult.getAnnotations().get(0).getMemberName());
        };
    }

    @Override
    public <R extends ILifecycleEvent<R>> void consumeLifecycleEvent(Supplier<R> consumeEvent) {

    }

    private static class OilLoader implements IModLanguageLoader {

        @Override
        public <T> T loadMod(IModInfo info, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
            return (T)"Hallo";
        }
    }
}
