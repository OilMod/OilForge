package org.oilmod.oilforge.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import org.oilmod.api.inventory.ModInventoryObject;
import org.oilmod.api.rep.inventory.InventoryHolderRep;
import org.oilmod.oilforge.inventory.container.FlexChestPacket;
import org.oilmod.oilforge.inventory.container.OilChestLikeContainer;

import javax.annotation.Nullable;

import static org.oilmod.oilforge.inventory.container.OilContainerType.*;


/**
 * Created by sirati97 on 17.01.2016.
 */
public class OilInventoryChest extends OilInventoryBase<ModInventoryObject> {
    private final int rows;
    private final int columns;

    public OilInventoryChest(InventoryHolderRep owner, int rows, int columns, String title, IItemFilter itemFilter) {
        super(owner, title, rows * columns, null, itemFilter, true);
        this.rows = rows;
        this.columns = columns;
    }



    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new OilChestLikeContainer(CHESS_LIKE, id, inv, this, rows, columns);
    }

    @Override
    public void writeExtraData(PacketBuffer buffer) {
        FlexChestPacket packet = new FlexChestPacket(getItemFilter(), rows, columns);
        packet.encode(buffer);
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
