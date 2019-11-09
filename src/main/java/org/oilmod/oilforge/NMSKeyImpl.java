package org.oilmod.oilforge;

import net.minecraft.util.ResourceLocation;
import org.oilmod.api.util.NMSKey;

public class NMSKeyImpl implements NMSKey { //todo get rid of this with mixins
    public final ResourceLocation resourceLocation;

    public NMSKeyImpl(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
}
