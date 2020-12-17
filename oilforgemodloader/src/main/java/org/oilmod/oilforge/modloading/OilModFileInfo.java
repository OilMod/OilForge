package org.oilmod.oilforge.modloading;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraftforge.fml.loading.moddiscovery.InvalidModFileException;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IConfigurable;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.MavenVersionAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.*;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class OilModFileInfo implements IModFileInfo, IConfigurable {
    private static Logger LOGGER = LogManager.getLogger();
    private final IConfigurable config;
    private final OilModFile modFile;
    private final VersionRange modLoaderVersion = VersionRange.createFromVersion("1");
    private final List<OilModInfo> mods;
    private final Map<String, Object> properties = new HashMap<>();



    public OilModFileInfo(OilModFile modFile, IConfigurable config) {
        //super(modFile);
        this.config = config;
        this.modFile = modFile;
        this.modFile.setFileProperties(this.properties);

        if (config.<Integer>getConfigElement("configVersion").orElse(0) !=1) {
            throw new InvalidModFileException("configVersion entry missing or not set to 1", this);
        }


        final List<? extends IConfigurable> modConfigs = config.getConfigList("mods");
        if (modConfigs.isEmpty())
        {
            throw new InvalidModFileException("Missing mods list", this);
        }
        this.mods = null;/*modConfigs.stream()
                .map(mi-> new OilModInfo(this, mi))
                .collect(Collectors.toList());*/
        //find them
    }

    public List<IModInfo> getMods() {
        //noinspection unchecked
        return (List)this.mods;
    }
    public List<OilModInfo> getOilMods() {
        return this.mods;
    }

    public ModFile getFile() {
        return this.modFile;
    }

    public IConfigurable getConfig() {
        return this.config;
    }

    public String getModLoader() {
        return "oilmod";
    }

    public VersionRange getModLoaderVersion() {
        return this.modLoaderVersion;
    }

    public Map<String, Object> getFileProperties() {
        return this.properties;
    }

    @Override
    public String getLicense() {
        return "";//todo
    }

    public Optional<Manifest> getManifest() {
        return this.modFile.getLocator().findManifest(this.modFile.getFilePath());
    }

    public boolean showAsResourcePack() {
        return false;
    }


    @Override
    public <T> Optional<T> getConfigElement(final String... key) {
        return this.config.getConfigElement(key);
    }

    @Override
    public List<? extends IConfigurable> getConfigList(final String... key) {
        return null;
    }
}
