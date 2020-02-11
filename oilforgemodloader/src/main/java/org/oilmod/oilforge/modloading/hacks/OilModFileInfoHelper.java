package org.oilmod.oilforge.modloading.hacks;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.fml.loading.moddiscovery.InvalidModFileException;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import org.oilmod.oilforge.modloading.OilModFile;
import org.oilmod.oilforge.modloading.OilModInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.minecraft.world.biome.Biome.LOGGER;

public class OilModFileInfoHelper {
    public static final String OilModIdentifier = "OilModIdentifierClasspath";
    public static final String OilTestModIdentifier = "OilTestModIdentifierClasspath";
    private static final Config fakeModFileConfig; //needed because of forge stupidity, casts all IModFileInfo to ModFileInfo for no reason whatsoever, like just for fun
    private static final Config fakeModConfig;
    static {
        fakeModConfig = Config.inMemory();
        fakeModConfig.add("modId", "fucku");


        fakeModFileConfig = Config.inMemory();
        fakeModFileConfig.add("modLoader", "oilmod");
        fakeModFileConfig.add("loaderVersion", "1");
        fakeModFileConfig.add("mods", new ArrayList<>(Collections.singletonList(fakeModConfig)));
    }

    private static final Constructor<ModFileInfo> ctor;
    private static final Field fieldConfig;
    //private static final Field fieldModLoaderVersion;
    private static final Field fieldMods;
    static {
        try {
            ctor = ModFileInfo.class.getDeclaredConstructor(ModFile.class, UnmodifiableConfig.class);
            ctor.setAccessible(true);
            fieldConfig = ModFileInfo.class.getDeclaredField("config");
            fieldMods = ModFileInfo.class.getDeclaredField("mods");
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
            throw new IllegalStateException("LamdbaExceptionUtils not working", e);
        }
    }

    //copied from OilModFileInfo
    public static ModFileInfo createFileInfo(ModFile modFile, UnmodifiableConfig config) {
        try {
            ModFileInfo result = ctor.newInstance(modFile, fakeModFileConfig);

            set(fieldConfig, result, config);

            if (!config.contains("logoFile")) {
                ((Config)config).add("logoFile", "oilmodlogo.png");
            }

            if (config.getIntOrElse("configVersion", 0) !=1) {
                throw new InvalidModFileException("configVersion entry missing or not set to 1", result);
            }

            ArrayList<UnmodifiableConfig> modConfigs = config.getOrElse("mods", ArrayList::new);
            if (modConfigs.isEmpty()) {
                throw new InvalidModFileException("Missing mods list", result);
            } else {
                set(fieldMods, result, modConfigs.stream().map((mi) -> createModInfo(result, mi)).collect(Collectors.toList()));
            }

            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
            throw new IllegalStateException("LamdbaExceptionUtils not working", e);
        }
    }


    private static final Constructor<ModInfo> ctorModInfo;
    private static final Field fieldModConfig;
    private static final Field fieldModId;
    private static final Field fieldDisplayName;
    private static final Field fieldDescription;
    private static final Field fieldProperties;
    private static final Field fieldDependencies;
    static {
        try {
            ctorModInfo = ModInfo.class.getDeclaredConstructor(ModFileInfo.class, UnmodifiableConfig.class);
            ctorModInfo.setAccessible(true);
            fieldModConfig = ModInfo.class.getDeclaredField("modConfig");
            fieldModId = ModInfo.class.getDeclaredField("modId");
            fieldDisplayName = ModInfo.class.getDeclaredField("displayName");
            fieldDescription = ModInfo.class.getDeclaredField("description");
            fieldProperties = ModInfo.class.getDeclaredField("properties");
            fieldDependencies = ModInfo.class.getDeclaredField("dependencies");
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
            throw new IllegalStateException("LamdbaExceptionUtils not working", e);
        }
    }
    public static ModInfo createModInfo(ModFileInfo owningFile, UnmodifiableConfig modConfig) {
        //todo remove stuff we wont support (e.g. update service)
        try {
            ModInfo result = ctorModInfo.newInstance(owningFile, fakeModConfig);

            set(fieldModConfig, result, modConfig);

            String modId = (String)modConfig.getOptional("modId").orElseThrow(() -> new InvalidModFileException("Missing modId entry", owningFile));
            if (!OilModInfo.VALID_LABEL.matcher(modId).matches()) {
                LOGGER.fatal("Invalid modId found in file {} - {} does not match the standard: {}", owningFile.getFile().getFilePath(), modId, OilModInfo.VALID_LABEL.pattern());
                throw new InvalidModFileException("Invalid modId found : " + modId, owningFile);
            }

            set(fieldModId, result, modId);

            String classpath = (String)modConfig.getOptional("classpath").orElseThrow(() -> new InvalidModFileException("Missing classpath entry", owningFile));
            if (!OilModInfo.CLASSPATH_PATTERN.matcher(classpath).matches()) {
                LOGGER.fatal("Invalid classpath found in file {} for mod {} - {} does not match the standard: {}", owningFile.getFile().getFilePath(), modId, classpath, OilModInfo.CLASSPATH_PATTERN.pattern());
                throw new InvalidModFileException("Invalid classpath found : " + modId, owningFile);
            }

            String displayName = (String)modConfig.getOptional("displayName").orElse(null);
            String description = modConfig.getOrElse("description", "An oilmod loaded by OilForge");

            set(fieldDisplayName, result, displayName);
            set(fieldDescription, result, description);

            List<IModInfo.ModVersion> dependencies = new ObjectArrayList<>();
            Map<String, Object> properties = new Object2ObjectOpenHashMap<>();
            properties.put(OilModIdentifier, classpath);

            Config depOilModLoader = Config.inMemory(); //maybe do by included resource file instead
            depOilModLoader.add("modId", "oilforgeapi");
            depOilModLoader.add("mandatory", true);
            depOilModLoader.add("versionRange", "${file.jarVersion}");
            depOilModLoader.add("ordering", IModInfo.Ordering.AFTER.toString());
            //adds implied dependencies oilforge (oilforgeapi)!
            dependencies.add(new IModInfo.ModVersion(result, depOilModLoader));
            /*depOilModLoader = Config.inMemory();
            depOilModLoader.add("modId", "oilforgemodloader"); apparently providers do not appear as a mod so you cannot depend on them
            depOilModLoader.add("mandatory", true);
            depOilModLoader.add("versionRange", "${file.jarVersion}");
            depOilModLoader.add("ordering", IModInfo.Ordering.AFTER.toString());
            //adds implied dependencies OilModLoader (oilforgemodloader)!
            dependencies.add(new IModInfo.ModVersion(result, depOilModLoader));*/

            set(fieldDependencies, result, dependencies);
            set(fieldProperties, result, properties);

            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            LamdbaExceptionUtils.uncheck(() -> e);
            throw new IllegalStateException("LamdbaExceptionUtils not working", e);
        }
    }

    static void set(Field field, Object on, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        int old;
        modifiersField.setInt(field, (old=field.getModifiers()) & ~Modifier.FINAL);

        field.set(on, newValue);


        modifiersField.setInt(field, old);
        field.setAccessible(false);
        modifiersField.setAccessible(false);
    }

    public static String getClasspath(IModInfo info) {
        return (String) info.getModProperties().get(OilModIdentifier);
    }
    public static boolean isOilModInfo(IModInfo info) {
        return info.getModProperties().containsKey(OilModIdentifier);
    }
}
