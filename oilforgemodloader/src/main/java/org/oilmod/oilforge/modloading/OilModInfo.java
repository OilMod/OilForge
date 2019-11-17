package org.oilmod.oilforge.modloading;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.fml.loading.StringSubstitutor;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.InvalidModFileException;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.oilmod.api.OilMod;
import org.oilmod.api.util.OilKey;
import org.oilmod.api.util.Util;

import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OilModInfo implements IModInfo {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DefaultArtifactVersion DEFAULT_VERSION = new DefaultArtifactVersion("1");
    public static final Pattern VALID_LABEL = Util.alphanumericalPattern;
    public static final Pattern CLASSPATH_PATTERN = Pattern.compile("([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*");
    private final ModFileInfo owningFile;
    private final String modId;
    private final String classpath;
    private final String namespace;
    private final ArtifactVersion version;
    private final String displayName;
    private final String description;
    private final URL updateJSONURL;
    private final List<ModVersion> dependencies;
    private final Map<String, Object> properties;
    private final UnmodifiableConfig modConfig;

    public OilModInfo(IModFileInfo _owningFile, UnmodifiableConfig modConfig) {
        //todo remove stuff we wont support (e.g. update service)
        this.owningFile = (ModFileInfo) _owningFile;
        this.modConfig = modConfig;
        this.modId = (String)modConfig.getOptional("modId").orElseThrow(() -> new InvalidModFileException("Missing modId entry", owningFile));
        if (!VALID_LABEL.matcher(this.modId).matches()) {
            LOGGER.fatal("Invalid modId found in file {} - {} does not match the standard: {}", this.owningFile.getFile().getFilePath(), this.modId, VALID_LABEL.pattern());
            throw new InvalidModFileException("Invalid modId found : " + this.modId, owningFile);
        }

        this.classpath = (String)modConfig.getOptional("classpath").orElseThrow(() -> new InvalidModFileException("Missing classpath entry", owningFile));
        if (!CLASSPATH_PATTERN.matcher(this.modId).matches()) {
            LOGGER.fatal("Invalid classpath found in file {} for mod {} - {} does not match the standard: {}", this.owningFile.getFile().getFilePath(), this.modId, this.classpath, CLASSPATH_PATTERN.pattern());
            throw new InvalidModFileException("Invalid classpath found : " + this.modId, owningFile);
        }

        //no namespace support!
        namespace = modId;
            /*this.namespace = (String)modConfig.getOptional("namespace").orElse(this.modId);
            if (!VALID_LABEL.matcher(this.namespace).matches()) {
                LOGGER.fatal("Invalid override namespace found in file {} - {} does not match the standard: {}", this.owningFile.getFile().getFilePath(), this.namespace, VALID_LABEL.pattern());
                throw new InvalidModFileException("Invalid override namespace found : " + this.namespace, owningFile);
            } else {*/
        this.version = modConfig.<String>getOptional("version").map((s) -> StringSubstitutor.replace(s, owningFile != null ? owningFile.getFile() : null)).map(DefaultArtifactVersion::new).orElse(DEFAULT_VERSION);
        this.displayName = (String)modConfig.getOptional("displayName").orElse((Object)null);
        this.description = modConfig.get("description");
        this.updateJSONURL = (URL)modConfig.<String>getOptional("updateJSONURL").map(StringUtils::toURL).orElse(null);
        if (owningFile != null) {
            this.dependencies = (owningFile.getConfig().<List<UnmodifiableConfig>>getOptional(Arrays.asList("dependencies", this.modId)).orElse(Collections.emptyList())).stream().map((dep) -> new ModVersion(this, dep)).collect(Collectors.toList());
            this.properties = owningFile.getConfig().<UnmodifiableConfig>getOptional(Arrays.asList("modproperties", this.modId)).map(UnmodifiableConfig::valueMap).orElse(Collections.emptyMap());
        } else {
            this.dependencies = new ObjectArrayList<>();
            this.properties = Collections.emptyMap();
        }
        //}

        Config depOilModLoader = Config.inMemory(); //maybe do by included resource file instead
        depOilModLoader.add("modId", "OilModLoader");
        depOilModLoader.add("mandatory", true);
        depOilModLoader.add("ordering", Ordering.AFTER.toString());

        //lets add implied dependencies e.g. OilModLoader!
        dependencies.add(new ModVersion(this, depOilModLoader));

    }

    public IModFileInfo getOwningFile() {
        return this.owningFile;
    }

    public String getModId() {
        return this.modId;
    }

    public String getClasspath() {
        return classpath;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDescription() {
        return this.description;
    }

    public ArtifactVersion getVersion() {
        return this.version;
    }

    public List<ModVersion> getDependencies() {
        return this.dependencies;
    }

    public UnmodifiableConfig getModConfig() {
        return this.modConfig;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public Map<String, Object> getModProperties() {
        return this.properties;
    }

    public URL getUpdateURL() {
        return this.updateJSONURL;
    }

    public Optional<String> getLogoFile() {
        return this.owningFile != null ? this.owningFile.getConfig().getOptional("logoFile") : this.modConfig.getOptional("logoFile");
    }

    public boolean hasConfigUI() {
        return false;
    }
}
