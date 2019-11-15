package org.oilmod.oilforge.modloading;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.moddiscovery.CoreModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OilModFileParser {
    private static final Logger LOGGER = LogManager.getLogger();
    public static OilModFileInfo readModList(OilModFile modFile) {
        LOGGER.debug(LogMarkers.LOADING, "Parsing mod file candidate {}", modFile.getFilePath());
        Path modsjson = modFile.getLocator().findPath(modFile, "META-INF", "oilmod.json");
        if (!Files.exists(modsjson)) {
            LOGGER.warn(LogMarkers.LOADING, "Mod file {} is missing oilmod.json", modFile);
            return null;
        } else {
            return loadModFile(modFile, modsjson);
        }
    }

    public static OilModFileInfo loadModFile(OilModFile file, Path modsjson) {
        FileConfig fileConfig = FileConfig.builder(modsjson).build();
        fileConfig.load();
        fileConfig.close();
        return new OilModFileInfo(file, fileConfig);
    }

}
