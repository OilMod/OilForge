package org.oilmod.oilforge.modloading;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.moddiscovery.CoreModFile;
import net.minecraftforge.fml.loading.moddiscovery.IModLocator;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileParser;
import net.minecraftforge.forgespi.language.IModFileInfo;
import org.apache.logging.log4j.util.LambdaUtil;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OilModFile extends ModFile {
    public OilModFile(Path file, IModLocator locator) {
        super(file, locator);
    }

    private static final Field modFileInfoField;
    private static final Field coreModsField;

    static {
        try {
            modFileInfoField = ModFile.class.getDeclaredField("modFileInfo");
            modFileInfoField.setAccessible(true);
            coreModsField = ModFile.class.getDeclaredField("coreMods");
            coreModsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
            throw new IllegalStateException("should have benn thrown by LamdbaExceptionUtils", e);
        }
    }


    private void setModFileInfo(IModFileInfo modFileInfo) {
        try {
            modFileInfoField.set(this, modFileInfo);
        } catch (IllegalAccessException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
        }
    }
    private void setCoreMods(List<CoreModFile> coreModFiles) {
        try {
            coreModsField.set(this, coreModFiles);
        } catch (IllegalAccessException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
        }
    }

    @Override
    public boolean identifyMods() {
        setModFileInfo(OilModFileParser.readModList(this));
        setCoreMods(new ArrayList<>());
        return true;

    }


}
