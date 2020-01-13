package org.oilmod.oilforge.items;

import net.minecraft.item.Item;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.oilforge.items.tools.RealPickaxe;
import org.oilmod.oilforge.items.tools.RealShovel;

import static org.oilmod.oilforge.Util.toOil;

public class RealIIPHelper extends ItemImplementationProvider.Helper<RealIIPHelper> {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    private interface ImplementationDelegate<T extends Item & RealItemImplHelper>{
        T implement(OilItem oilItem);
    }

    private static class DIP extends ItemImplementationProvider {
        private final ImplementationDelegate<?> delegate;

        private DIP(ItemImplementationProvider.TypeEnum typeEnum, ImplementationDelegate<?> delegate) {
            super(typeEnum);
            this.delegate = delegate;
        }

        @Override
        public ItemRep implement(OilItem oilItem) {
            return toOil(delegate.implement(oilItem));
        }
    }
    //todo assign keys to implementation provider

    @Override
    protected ItemImplementationProvider getProvider(ItemImplementationProvider.TypeEnum t) {
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
