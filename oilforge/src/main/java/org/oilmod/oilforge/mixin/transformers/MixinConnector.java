package org.oilmod.oilforge.mixin.transformers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        System.out.println("Invoking Mixin Connector");
        Mixins.addConfiguration(
                "assets/oilforgeapi/oilforgeapi.mixins.json"
        );
    }
}
