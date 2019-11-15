package org.oilmod.oilforge.modloading;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.ModDirTransformerDiscoverer;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OilModLocator extends AbstractJarFileLocator {
    private static final String SUFFIX = ".jar";
    private static final String OILMODS_FOLDER = "oilmods";
    private static final Logger LOGGER = LogManager.getLogger();
    private final Path modFolder;

    public OilModLocator() {
        this(FMLPaths.MODSDIR.get().resolveSibling(OILMODS_FOLDER));
    }

    OilModLocator(Path modFolder) {
        this.modFolder = modFolder;
        LOGGER.debug("Created new OilModLocator {}", this::toString);
    }

    public List<ModFile> scanMods() {
        LOGGER.debug(LogMarkers.SCAN, "Scanning mods dir {} for mods", this.modFolder);
        List<Path> excluded = (new ModDirTransformerDiscoverer()).candidates(FMLPaths.GAMEDIR.get());
        return LamdbaExceptionUtils.uncheck(() -> {
            return Files.list(this.modFolder);
        }).filter((p) -> {
            return !excluded.contains(p);
        }).sorted(Comparator.comparing((path) -> {
            return StringUtils.toLowerCase(path.getFileName().toString());
        })).filter((p) -> {
            return StringUtils.toLowerCase(p.getFileName().toString()).endsWith(SUFFIX);
        }).map((p) -> {
            return new OilModFile(p, this);
        }).peek((f) -> {
            FileSystem var10000 = (FileSystem)this.modJars.compute(f, (mf, fs) -> {
                return this.createFileSystem(mf);
            });
        }).collect(Collectors.toList());
    }

    @Override
    public String name() {
        return "OilModLocator";
    }


    public String toString() {
        return "{OilModJarsFolder locator at " + this.modFolder + "}";
    }

    @Override
    public void initArguments(Map<String, ?> map) {

    }
}
