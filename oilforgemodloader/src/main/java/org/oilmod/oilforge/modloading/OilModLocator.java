package org.oilmod.oilforge.modloading;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraftforge.fml.loading.*;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.fml.loading.LogMarkers.CORE;

public class OilModLocator extends AbstractJarFileLocator {
    public static boolean CREATED = false;
    private static final Set<String> DELEGATED_RESOURCES = new ObjectOpenHashSet<>(Arrays.asList("pack.mcmeta", "oilmodlogo.png"));
    private static final String SUFFIX = ".jar";
    private static final String OILMODS_FOLDER = "oilmods";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final FileSystem JAR_FILESYSTEM;

    static {
        try {
            ProtectionDomain protectionDomain= OilModLocator.class.getProtectionDomain();
            Path path =Paths.get(protectionDomain.getCodeSource()
                            .getLocation()
                            .toURI());
            JAR_FILESYSTEM = null;//FileSystems.newFileSystem(path, OilModLocator.class.getClassLoader());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private final Path modFolder;

    public OilModLocator() {
        this(FMLPaths.MODSDIR.get().resolveSibling(OILMODS_FOLDER));
    }

    OilModLocator(Path modFolder) {
        LOGGER.info("Instance of {} was created", this.getClass()::getSimpleName);
        CREATED = true;
        this.modFolder = modFolder;
        if (Files.notExists(modFolder)) {
            try {
                Files.createDirectories(modFolder);
            } catch (IOException e) {
                throw new IllegalStateException("oilmods folder missing and could not be created", e);
            }
        }
        LOGGER.debug("Created new OilModLocator {}", this::toString);
    }

    public List<ModFile> scanMods() {
        LOGGER.debug(LogMarkers.SCAN, "Scanning mods dir {} for mods", this.modFolder);
        List<Path> excluded = (new ModDirTransformerDiscoverer()).candidates(FMLPaths.GAMEDIR.get());
        return LamdbaExceptionUtils.uncheck(() -> Files.list(this.modFolder))
                .filter((p) -> !excluded.contains(p))
                .sorted(Comparator.comparing((path) -> StringUtils.toLowerCase(path.getFileName().toString())))
                .filter((p) -> StringUtils.toLowerCase(p.getFileName().toString()).endsWith(SUFFIX))
                .peek(path -> LOGGER.trace("checking out oilmod candidate {}", path::toString))
                .map((p) -> new OilModFile(p, this))
                .peek((f) -> this.modJars.compute(f, (mf, fs) -> this.createFileSystem(mf))).collect(Collectors.toList());
    }

    @Override
    public Path findPath(ModFile modFile, String... path) {
        Path result = super.findPath(modFile, path);
        if (Files.notExists(result)) {
            if (Arrays.stream(path).anyMatch(DELEGATED_RESOURCES::contains)) {
                try {
                    Path delegate = Paths.get(OilModLocator.class.getResource('/' + String.join("/", path)).toURI());
                    if (Files.notExists(delegate)) {
                        LOGGER.error("Delegate file {} could not be found", delegate::toString);
                        return result;
                    }
                    result = delegate;
                    LOGGER.debug("modfile {} requested {} delegating to API jar resources ({})", modFile::getFileName, () -> Arrays.toString(path), result::toString);
                } catch (URISyntaxException | FileSystemNotFoundException e) {
                    LOGGER.error("Delegate file {} could not be found, threw exception: {}", result::toString, e::toString);
                }
            }
        }
        return result;
    }

    @Override
    public String name() {
        return "OilModLocator";
    }


    public String toString() {
        return "{OilModJarsFolder locator at " + this.modFolder + "}";
    }

    private static final String OILMODLOADER_ID_FILE = "META-INF/OilModLoaderId";
    @Override
    public void initArguments(Map<String, ?> map) {

    }
}
