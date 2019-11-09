package org.oilmod.oilforge.items;

import org.oilmod.api.items.NMSItem;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.ImplementationProvider;
import org.oilmod.oilforge.items.tools.RealPickaxe;

public class RealIPHelper extends ImplementationProvider.IPHelper {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    @Override
    protected ImplementationProvider getProvider(ImplementationProvider.TypeEnum typeEnum) {
        switch (typeEnum) {
            case PICKAXE:
                return new ImplementationProvider(typeEnum) {
                    @Override
                    public NMSItem implement(OilItem oilItem) {
                        return new RealPickaxe(oilItem);
                    }
                };
            case AXE:
            case SHOVEL:
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
                return new ImplementationProvider(typeEnum) {
                    @Override
                    public NMSItem implement(OilItem oilItem) {
                        return new RealItem(oilItem);
                    }
                };
        }
    }
}
