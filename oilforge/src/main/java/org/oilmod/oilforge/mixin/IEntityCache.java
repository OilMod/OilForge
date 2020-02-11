package org.oilmod.oilforge.mixin;

import org.oilmod.oilforge.rep.entity.EntityFR;

public interface IEntityCache {
    <E extends EntityFR> E getOilEntityRep();
}
