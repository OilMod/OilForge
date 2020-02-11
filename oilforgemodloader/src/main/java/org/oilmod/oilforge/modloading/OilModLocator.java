package org.oilmod.oilforge.modloading;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.ModDirTransformerDiscoverer;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.forgespi.locating.IModFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.stream.Collectors;

import static cpw.mods.modlauncher.api.LamdbaExceptionUtils.uncheck;

public class OilModLocator extends AbstractJarFileLocator {
    public static boolean CREATED = false;
    private static final Set<String> DELEGATED_RESOURCES = new ObjectOpenHashSet<>(Arrays.asList("pack.mcmeta", "oilmodlogo.png"));
    private static final String SUFFIX = ".jar";
    private static final String OILMODS_FOLDER = "oilmods";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final FileSystem JAR_FILESYSTEM;
    public static OilModLocator INSTANCE;

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
        INSTANCE = this;
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

    public List<IModFile> scanMods() {
        LOGGER.debug(LogMarkers.SCAN, "Scanning mods dir {} for mods", this.modFolder);
        List<IModFile> result = uncheck(() -> Files.list(this.modFolder))
                .sorted(Comparator.comparing(path-> StringUtils.toLowerCase(path.getFileName().toString())))
                .filter(p->StringUtils.toLowerCase(p.getFileName().toString()).endsWith(SUFFIX))
                .peek(path -> LOGGER.trace("checking out oilmod candidate {}", path::toString))
                .map(p -> new OilModFile(p, this))
                .peek(f -> this.modJars.compute(f, (mf, fs) -> this.createFileSystem(mf)))
                .collect(Collectors.toList());

        //Lets be nasty and just load ourselves, otherwise we need another jar for the LanguageProvider and we both care about the same dependencies making things awkward
        List<Path> excluded = ModDirTransformerDiscoverer.allExcluded();
        uncheck(excluded::stream)
                .sorted(Comparator.comparing(path-> StringUtils.toLowerCase(path.getFileName().toString())))
                .filter(p->StringUtils.toLowerCase(p.getFileName().toString()).endsWith(SUFFIX))
                .filter(p->uncheck(()->Files.exists(FileSystems.newFileSystem(p, getClass().getClassLoader()).getPath("META-INF/OilModLoaderId"))))
                .map(p->new ModFile(p, this))
                .peek(f->modJars.compute(f, (mf, fs)->createFileSystem(mf)))
                .forEach(result::add);
        return result;
    }

    @Override
    public Path findPath(IModFile modFile, String... path) {
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
            } else {
                if (path.length > 0 && path[0].equals("assets")) {
                    LOGGER.debug("Could not find file: {} for oilmod {}", () -> Arrays.toString(path), modFile::getFileName);
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
