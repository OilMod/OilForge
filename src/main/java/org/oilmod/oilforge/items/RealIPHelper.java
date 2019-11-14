package org.oilmod.oilforge.items;

import org.oilmod.api.items.NMSItem;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.oilforge.items.tools.RealPickaxe;
import org.oilmod.oilforge.items.tools.RealShovel;

import static org.oilmod.oilforge.Util.toOil;

public class RealIPHelper extends ImplementationProvider.IPHelper {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    private interface ImplementationDelegate{
        RealItemImplHelper implement(OilItem oilItem);
    }

    private static class DIP extends ImplementationProvider {
        private final ImplementationDelegate delegate;

        private DIP(ImplementationProvider.TypeEnum typeEnum, ImplementationDelegate delegate) {
            super(typeEnum);
            this.delegate = delegate;
        }

        @Override
        public ItemRep implement(OilItem oilItem) {
            return toOil(delegate.implement(oilItem).getItem());
        }
    }

    @Override
    protected ImplementationProvider getProvider(ImplementationProvider.TypeEnum t) {
        switch (t) {
            case PICKAXE:
                return new DIP(t, RealPickaxe::new);
            case SHOVEL:
                return new DIP(t, RealShovel::new);
            case AXE:
            case SHEARS:
            case HOE:
            case TOOL_CUSTOM:
            case SWORD:
            case MELEE_WEAPON_CUSTOM:
            case BOW:
            case BOW_CUSTOM:
            case FOOD:
            case ARMOR_HELMET:
            case ARMOR_CHESTPLATE:
            case ARMOR_LEGGINGS:
            case ARMOR_SHOES:
            case ARMOR_CUSTOM:
            case CUSTOM:
            default:
                return new DIP(t, RealItem::new);
        }
    }
}
