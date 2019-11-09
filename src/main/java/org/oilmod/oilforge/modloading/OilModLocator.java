package org.oilmod.oilforge.modloading;

import net.minecraftforge.fml.loading.moddiscovery.IModLocator;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.jar.Manifest;

public class OilModLocator implements IModLocator {
    @Override
    public List<ModFile> scanMods() {
        return null;
    }

    @Override
    public String name() {
        return "OilModLocator";
    }

    @Override
    public Path findPath(ModFile modFile, String... strings) {
        return null;
    }

    @Override
    public void scanFile(ModFile modFile, Consumer<Path> consumer) {

    }

    @Override
    public Optional<Manifest> findManifest(Path path) {
        return Optional.empty();
    }

    @Override
    public void initArguments(Map<String, ?> map) {

    }

    @Override
    public boolean isValid(ModFile modFile) {
        return false;
    }
}
