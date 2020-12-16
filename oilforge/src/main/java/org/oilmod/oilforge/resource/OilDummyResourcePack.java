package org.oilmod.oilforge.resource;

import net.minecraft.block.Block;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.packs.DelegatableResourcePack;
import net.minecraftforge.forgespi.locating.IModFile;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.OilMod;
import org.oilmod.oilforge.block.RealBlockRegistryHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OilDummyResourcePack extends DelegatableResourcePack {
    private ResourcePackInfo packInfo;
    private IModFile modFile = null;
    public static final Logger LOGGER = LogManager.getLogger();

    public OilDummyResourcePack()
    {
        super(new File("dummy"));
    }


    @Override
    public String getName()
    {
        return "Default";
    }

    @Override
    public InputStream getInputStream(String name) throws IOException
    {
        LOGGER.debug("OilDummyResourcePack getInputStream {}", name);
        if (name.equals("pack.mcmeta")) {
            return getClass().getResourceAsStream("/pack.mcmeta");
        }

        if (name.endsWith(".mcdata"))throw new FileNotFoundException(String.format("The file %s does not exist", name));
        String[] path = name.split("/");
        if (path.length > 2 && path[2].equals("loot_tables")) return getClass().getResourceAsStream("/assets/blockloottable.json");
        throw new IOException(new NotImplementedException("not implemented!"));
        //final Path path = modFile.getLocator().findPath(modFile, name);
        //return Files.newInputStream(path, StandardOpenOption.READ);
    }

    @Override
    public boolean resourceExists(String name)
    {
        if (name.endsWith(".mcdata"))return false;
        LOGGER.debug("OilDummyResourcePack was requested to check if {}(string) exist", name);
        String[] path = name.split("/");
        if (path.length > 2 && path[2].equals("loot_tables")) return true;
        return false;// Files.exists(modFile.getLocator().findPath(modFile, name));
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String pathIn, int maxDepth, Predicate<String> filter)
    {
        LOGGER.debug("OilDummyResourcePack was requested all resource locations for {} {} {} {}", type, pathIn, maxDepth, filter);

        if (pathIn.equals("loot_tables")) {
            return RealBlockRegistryHelper.INSTANCE.allRegistered.stream()
                    .map(Block::getLootTable)
                    .map(resourceLocation -> new ResourceLocation(resourceLocation.getNamespace(), pathIn + "/" + resourceLocation.getPath() + ".json"))
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }

    }

    @Override
    public Set<String> getResourceNamespaces(ResourcePackType type)
    {
        return OilMod.getAll().stream().filter(OilMod::isMod).map(OilMod::getInternalName).collect(Collectors.toSet());
    }

    public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {

        LOGGER.debug("OilDummyResourcePack was requested {}", location);
        if (location.getPath().startsWith("lang/")) {
            return super.getResourceStream(ResourcePackType.CLIENT_RESOURCES, location);
        } else {
            return super.getResourceStream(type, location);
        }
    }

    public boolean resourceExists(ResourcePackType type, ResourceLocation location) {

        LOGGER.debug("OilDummyResourcePack was requested to check if {} exist", location);
        if (location.getPath().startsWith("lang/")) {
            return super.resourceExists(ResourcePackType.CLIENT_RESOURCES, location);
        } else {
            return super.resourceExists(type, location);
        }
    }

    @Override
    public void close() throws IOException
    {

    }

    <T extends ResourcePackInfo> void setPackInfo(final T packInfo) {
        this.packInfo = packInfo;
    }

    <T extends ResourcePackInfo> T getPackInfo() {
        return (T)this.packInfo;
    }

    @Override
    public boolean isHidden() {
        return true;
    }


}
