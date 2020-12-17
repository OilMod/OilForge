package org.oilmod.oilforge.modloading;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraftforge.fml.loading.moddiscovery.CoreModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModLocator;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OilModFile extends ModFile {
    public OilModFile(Path file, IModLocator locator) {
        super(file, locator, OilModFileParser.INSTANCE);
    }

    private static final Field modFileInfoField;
    private static final Field coreModsField;
    private static final Field accessTransformerField;

    static {
        try {
            modFileInfoField = ModFile.class.getDeclaredField("modFileInfo");
            modFileInfoField.setAccessible(true);
            coreModsField = ModFile.class.getDeclaredField("coreMods");
            coreModsField.setAccessible(true);
            accessTransformerField = ModFile.class.getDeclaredField("accessTransformer");
            accessTransformerField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
            throw new IllegalStateException("should have been thrown by LamdbaExceptionUtils", e);
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
    private void setAccessTransformer(Path accessTransformer) {
        try {
            accessTransformerField.set(this, accessTransformer);
        } catch (IllegalAccessException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
        }
    }

    @Override
    public boolean identifyMods() {
        IModFileInfo fileInfo = OilModFileParser.readModList(this);
        setModFileInfo(fileInfo);
        setCoreMods(new ArrayList<>());
        setAccessTransformer(this.getLocator().findPath(this, "META-INF", "accesstransformer.cfg"));
        getModInfos(); //debug
        return true;

    }


}
