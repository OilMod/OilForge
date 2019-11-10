package org.oilmod.oilforge;

import net.minecraft.util.ResourceLocation;
import org.oilmod.api.rep.IKey;
import org.oilmod.api.util.NMSKey;

public class NMSKeyImpl implements NMSKey, IKey { //todo get rid of this with mixins
    public final ResourceLocation resourceLocation;

    public NMSKeyImpl(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public String getNamespace() {
        return resourceLocation.getNamespace();
    }

    @Override
    public String getKeyString() {
        return resourceLocation.getPath();
    }
}
