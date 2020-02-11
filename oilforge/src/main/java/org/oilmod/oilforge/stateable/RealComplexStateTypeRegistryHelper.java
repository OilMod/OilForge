package org.oilmod.oilforge.stateable;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.blocks.BlockRegistry;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.registry.InitRegisterCallback;
import org.oilmod.api.stateable.complex.ComplexStateTypeRegistry;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.complex.IComplexStateType;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.OilModContext;
import org.oilmod.oilforge.block.tileentity.RealTileEntity;
import org.oilmod.oilforge.block.tileentity.RealTileEntityType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.util.LamdbaCastUtils.cast;

public class RealComplexStateTypeRegistryHelper extends ComplexStateTypeRegistry.RegistryHelper<RealComplexStateTypeRegistryHelper> {
    public static RealComplexStateTypeRegistryHelper INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger();
    


    @Override
    public void allDepResolved() {
        super.allDepResolved();
        INSTANCE = this;
    }

    @Override
    protected <T extends IComplexStateType<?>> void onRegister(OilKey key, ComplexStateTypeRegistry registry, T entry) {
        onRegisterGeneric(key, registry, cast(entry));
    }

    protected <T extends IComplexStateType<TComplexState>, TComplexState extends IComplexState> void onRegisterGeneric(OilKey key, ComplexStateTypeRegistry registry, T entry) {
        RealTileEntityType<TComplexState> tileEntityType = new RealTileEntityType<>(entry, null); //todo blocks and datafixer
        tileEntityType.setRegistryName(toForge(key));

        OilModContext context = (OilModContext) registry.getMod().getContext();
        Validate.notNull(context.tileEntityTypeRegistry, "TileEntityTypeRegister not set for modcontext, out of order registration?");
        context.tileEntityTypeRegistry.register(tileEntityType);

    }
}
