package org.oilmod.oilforge.modloading;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.NightConfigWrapper;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.forgespi.locating.ModFileFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.modloading.hacks.OilModFileInfoHelper;

import java.nio.file.Files;
import java.nio.file.Path;

public class OilModFileParser implements ModFileFactory.ModFileInfoParser {
    public static final OilModFileParser INSTANCE = new OilModFileParser();

    private static final Logger LOGGER = LogManager.getLogger();
    public static IModFileInfo readModList(OilModFile modFile) {
        LOGGER.debug(LogMarkers.LOADING, "Parsing mod file candidate {}", modFile.getFilePath());
        Path modsjson = modFile.getLocator().findPath(modFile, "META-INF", "oilmod.json");
        if (!Files.exists(modsjson)) {
            LOGGER.warn(LogMarkers.LOADING, "Mod file {} is missing oilmod.json", modFile);
            return null;
        } else {
            return loadModFile(modFile, modsjson);
        }
    }

    public static IModFileInfo loadModFile(OilModFile file, Path modsjson) {
        FileConfig fileConfig = FileConfig.builder(modsjson).build();
        fileConfig.load();
        fileConfig.close();
        return OilModFileInfoHelper.createFileInfo(file, fileConfig);
    }

    @Override
    public IModFileInfo build(IModFile file) {
        return readModList((OilModFile)file);
    }
}
