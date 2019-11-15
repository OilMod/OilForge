package org.oilmod.oilforge.modloading;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.utils.FakeCommentedConfig;
import com.electronwill.nightconfig.core.utils.FakeUnmodifiableCommentedConfig;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.*;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.MavenVersionAdapter;
import org.apache.maven.artifact.versioning.VersionRange;

import java.net.URL;
import java.util.*;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class OilModFileInfo /*extends StupidModFileInfo*/ implements IModFileInfo {
    private final UnmodifiableConfig config;
    private final OilModFile modFile;
    private final VersionRange modLoaderVersion = VersionRange.createFromVersion("1");
    private final List<OilModInfo> mods;
    private final Map<String, Object> properties = new HashMap<>();



    private OilModFileInfo(OilModFile modFile, UnmodifiableConfig config) {
        //super(modFile);
        this.config = config;
        this.modFile = modFile;
        this.modFile.setFileProperties(this.properties);

        if (config.getIntOrElse("configVersion", 0) !=1) {
            throw new InvalidModFileException("configVersion entry missing or not set to 1", this);
        }

        ArrayList<UnmodifiableConfig> modConfigs = config.getOrElse("mods", ArrayList::new);
        if (modConfigs.isEmpty()) {
            throw new InvalidModFileException("Missing mods list", this);
        } else {
            this.mods = modConfigs.stream().map((mi) -> new OilModInfo(this, mi)).collect(Collectors.toList());
        }
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

    public UnmodifiableConfig getConfig() {
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

    public Optional<Manifest> getManifest() {
        return this.modFile.getLocator().findManifest(this.modFile.getFilePath());
    }

    public boolean showAsResourcePack() {
        return false;
    }
}
