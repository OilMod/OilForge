package org.oilmod.oilforge.modloading;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.fml.loading.StringSubstitutor;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.InvalidModFileException;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.fml.loading.moddiscovery.NightConfigWrapper;
import net.minecraftforge.forgespi.language.IConfigurable;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.MavenVersionAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.oilmod.api.util.Util;
import org.oilmod.oilforge.modloading.hacks.OilModFileInfoHelper;

import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OilModInfo extends ModInfo implements IModInfo, IConfigurable {
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
    private final Optional<String> logoFile;
    private final boolean logoBlur;
    private final URL updateJSONURL;
    private final List<ModVersion> dependencies;
    private final Map<String, Object> properties;
    private final IConfigurable config;

    public OilModInfo(ModFileInfo _owningFile, IConfigurable config) {
        super(_owningFile, new NightConfigWrapper(OilModFileInfoHelper.fakeModConfig));
        Optional<ModFileInfo> ownFile = Optional.ofNullable(_owningFile);
        //todo remove stuff we wont support (e.g. update service)
        this.owningFile = _owningFile;
        this.config = config;
        this.modId = config.<String>getConfigElement("modId")
                .orElseThrow(() -> new InvalidModFileException("Missing modId", owningFile));
        if (!VALID_LABEL.matcher(this.modId).matches()) {
            LOGGER.fatal("Invalid modId found in file {} - {} does not match the standard: {}", this.owningFile.getFile().getFilePath(), this.modId, VALID_LABEL.pattern());
            throw new InvalidModFileException("Invalid modId found : " + this.modId, owningFile);
        }

        this.classpath = config.<String>getConfigElement("classpath").orElseThrow(() -> new InvalidModFileException("Missing classpath entry", owningFile));
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
        this.version = config.<String>getConfigElement("version")
                .map(s -> StringSubstitutor.replace(s, ownFile.map(ModFileInfo::getFile).orElse(null)))
                .map(DefaultArtifactVersion::new).orElse(DEFAULT_VERSION);
        this.displayName = config.<String>getConfigElement("displayName").orElse(this.modId);
        this.description = config.<String>getConfigElement("description").orElse("An oilmod loaded by OilForge");

        this.logoFile = Optional.ofNullable(config.<String>getConfigElement("logoFile")
                .orElseGet(() -> ownFile.flatMap(mf -> mf.<String>getConfigElement("logoFile")).orElse(null)));
        this.logoBlur = config.<Boolean>getConfigElement("logoBlur")
                .orElseGet(() -> ownFile.flatMap(f -> f.<Boolean>getConfigElement("logoBlur"))
                        .orElse(true));

        this.updateJSONURL = config.<String>getConfigElement("updateJSONURL")
                .map(StringUtils::toURL)
                .orElse(null);
        if (owningFile != null) {
            this.dependencies = new ObjectArrayList<>(ownFile.map(mfi -> mfi.getConfigList("dependencies", this.modId))
                    .map(l -> l.stream()
                    .map(dep -> new ModVersion(this, dep))
                    .collect(Collectors.toCollection(ObjectArrayList<ModVersion>::new)))
                    .orElseGet(ObjectArrayList::new));

            this.properties = ownFile.map(mfi -> mfi.<Map<String, Object>>getConfigElement("modproperties", this.modId)
                    .orElse(Collections.emptyMap()))
                    .orElse(Collections.emptyMap());
        } else {
            this.dependencies = new ObjectArrayList<>();
            this.properties = Collections.emptyMap();
        }
        //}
        //lets add implied dependencies e.g. OilModLoader!
        dependencies.add(new OilModInfo.ModVersion(this, "oilforgeapi", "${file.jarVersion}", Ordering.AFTER));
        dependencies.add(new OilModInfo.ModVersion(this, "oilmod", "${file.jarVersion}", Ordering.AFTER));
        dependencies.add(new OilModInfo.ModVersion(this, "oiluni", "${file.jarVersion}", Ordering.AFTER));
    }

    public ModFileInfo getOwningFile() {
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

    public IConfigurable getConfig() {
        return this.config;
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

    public Optional<String> getLogoFile()
    {
        return this.logoFile;
    }

    public boolean getLogoBlur()
    {
        return this.logoBlur;
    }


    /**
     * This is no longer used. The Mods List GUI currently directly checks whether there is an EntryPoint registered.
     */
    @Deprecated
    public boolean hasConfigUI()
    {
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

    class ModVersion implements net.minecraftforge.forgespi.language.IModInfo.ModVersion {
        private IModInfo owner;
        private final String modId;
        private final VersionRange versionRange;
        private final boolean mandatory;
        private final Ordering ordering;
        private final DependencySide side;

        public ModVersion(IModInfo owner, IConfigurable config) {
            this.owner = owner;
            this.modId = config.<String>getConfigElement("modId")
                    .orElseThrow(()->new InvalidModFileException("Missing required field modid in dependency", getOwningFile()));
            this.mandatory = config.<Boolean>getConfigElement("mandatory")
                    .orElseThrow(()->new InvalidModFileException("Missing required field mandatory in dependency", getOwningFile()));
            this.versionRange = config.<String>getConfigElement("versionRange")
                    .map(MavenVersionAdapter::createFromVersionSpec)
                    .orElse(UNBOUNDED);
            this.ordering = config.<String>getConfigElement("ordering")
                    .map(Ordering::valueOf)
                    .orElse(Ordering.NONE);
            this.side = config.<String>getConfigElement("side")
                    .map(DependencySide::valueOf)
                    .orElse(DependencySide.BOTH);
        }

        public ModVersion(IModInfo owner, String modId, String version, Ordering ordering) {
            this.owner = owner;
            this.modId = modId;
            this.mandatory = true;
            this.versionRange = MavenVersionAdapter.createFromVersionSpec(version);
            this.ordering = ordering;
            this.side = DependencySide.BOTH;
        }

        public String getModId() {
            return this.modId;
        }

        public VersionRange getVersionRange() {
            return this.versionRange;
        }

        public boolean isMandatory() {
            return this.mandatory;
        }

        public Ordering getOrdering() {
            return this.ordering;
        }

        public DependencySide getSide() {
            return this.side;
        }

        public void setOwner(IModInfo owner) {
            this.owner = owner;
        }

        public IModInfo getOwner() {
            return this.owner;
        }
    }
}
