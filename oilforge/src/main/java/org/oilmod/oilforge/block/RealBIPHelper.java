package org.oilmod.oilforge.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.oilmod.api.blocks.OilBlock;
import org.oilmod.api.blocks.type.BlockImplementationProvider;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.oilforge.items.RealItem;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.items.tools.RealPickaxe;
import org.oilmod.oilforge.items.tools.RealShovel;

import static org.oilmod.oilforge.Util.toOil;

public class RealBIPHelper extends BlockImplementationProvider.Helper<RealBIPHelper> {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    private interface ImplementationDelegate<T extends Block & RealBlockImplHelper>{
        T implement(OilBlock block);
    }

    private static class DIP extends BlockImplementationProvider {
        private final ImplementationDelegate<?> delegate;

        private DIP(TypeEnum typeEnum, ImplementationDelegate<?> delegate) {
            super(typeEnum);
            this.delegate = delegate;
        }

        @Override
        public BlockRep implement(OilBlock oilBlock) {
            return toOil(delegate.implement(oilBlock));
        }
    }
    //todo assign keys to implementation provider

    @Override
    protected BlockImplementationProvider getProvider(BlockImplementationProvider.TypeEnum t) {
        switch (t) {
            case GENERIC:
            case CUSTOM:
            default:
                return new DIP(t, RealBlock::new);
        }
    }
}
