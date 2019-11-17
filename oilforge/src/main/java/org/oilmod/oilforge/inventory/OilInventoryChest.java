package org.oilmod.oilforge.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.HopperContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.rep.inventory.InventoryHolderRep;

import javax.annotation.Nullable;

import static net.minecraft.inventory.container.ContainerType.*;

/**
 * Created by sirati97 on 17.01.2016.
 */
public class OilInventoryChest extends OilInventoryBase<ModInventoryObject> {

    public OilInventoryChest(InventoryHolderRep owner, int size, String title, IItemFilter itemFilter) {
        super(owner, title, size, null, itemFilter, true);
    }

    public interface IFactory<T extends Container> {
        @OnlyIn(Dist.CLIENT)
        T create(int id, PlayerInventory player, IInventory inv);
    }
    private static final IFactory HOPPER = HopperContainer::new;
    private static final IFactory Chest1Row = (id, p, i)->new ChestContainer(GENERIC_9X1, id, p, i, 1);
    private static final IFactory Chest2Row = (id, p, i)->new ChestContainer(GENERIC_9X2, id, p, i, 2);
    private static final IFactory Chest3Row = (id, p, i)->new ChestContainer(GENERIC_9X3, id, p, i, 3);
    private static final IFactory Chest4Row = (id, p, i)->new ChestContainer(GENERIC_9X4, id, p, i, 4);
    private static final IFactory Chest5Row = (id, p, i)->new ChestContainer(GENERIC_9X5, id, p, i, 5);
    private static final IFactory Chest6Row = (id, p, i)->new ChestContainer(GENERIC_9X6, id, p, i, 6);


    private IFactory resolveContainer() {
        if (getSizeInventory() <= 5) {
            return HopperContainer::new;
        } else if (getSizeInventory() <= 9) {
            return Chest1Row;
        } else if (getSizeInventory() <= 18) {
            return Chest2Row;
        } else if (getSizeInventory() <= 27) {
            return Chest3Row;
        } else if (getSizeInventory() <= 36) {
            return Chest4Row;
        } else if (getSizeInventory() <= 45) {
            return Chest5Row;
        } else if (getSizeInventory() <= 56) {
            return Chest6Row;
        } else {
            return (id, p, i)->new ChestContainer(GENERIC_9X6, id, p, i, id/9);
        }

        //todo write inv ui api and make this automatic in standard case
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return resolveContainer().create(id, inv, this);
    }

    //@Override
    //public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
    //    return getSize()==5?new OilContainerHopper(entityhuman.inventory, this, entityhuman):new OilContainerChest(entityhuman.inventory, this, entityhuman);
    //}

    //@Override
    //public String getContainerName() {
    //    return getSize()==5?"minecraft:hopper":"minecraft:chest";
    //}
}
