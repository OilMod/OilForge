package org.oilmod.oilforge.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.world.IInteractionObject;
import org.oilmod.api.config.CompoundSerializable;
import org.oilmod.api.inventory.ModInventoryObjectBase;
import org.oilmod.api.inventory.ModNMSIInventory;
import org.oilmod.api.rep.inventory.InventoryRep;

import static org.oilmod.oilforge.Util.toOil;

/**
 * Created by sirati97 on 13.02.2016.
 */
public interface OilIInventory<APIObject extends ModInventoryObjectBase> extends IInventory, CompoundSerializable, ModNMSIInventory<APIObject>, FilteredInventory, ITickable, IInteractionObject {
    boolean isValid();
    IItemFilter getItemFilter();

    @Override
    default CompoundSerializable cloneIfCloneable() {
        return this;
    } //todo: HUH?

    @Override
    default Object getNMSInventory() {
        return this;
    }

    @Override
    default Object getOilModInventory() {
        return this;
    }

    @Override
    default boolean itemstackAddable(ItemStack itemStack) {
        return getItemFilter().allowed(itemStack);
    }



    @Override
    default void tick(int i) {

    }

    @Override
    default void tick() {
        tick(1);
    }

    @Override
    default boolean isTickable() {
        return false;
    }

    //@Override
    default InventoryRep getBukkitInventory() {
        return toOil(this);
    }
}
