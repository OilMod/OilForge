package org.oilmod.oilforge.rep.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.inventory.InventoryUIRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.oilforge.rep.inventory.InventoryFR;

import static org.oilmod.oilforge.Util.toOil;

public class EntityHumanFR extends LivingEntityBaseFR implements EntityHumanRep {


    public EntityHumanFR(PlayerEntity forge) {
        super(forge);
    }

    @Override
    public PlayerEntity getForge() {
        return (PlayerEntity) super.getForge();
    }

    @Override
    public String getName() {
        return getForge().getName().getUnformattedComponentText();
    }

    @Override
    public InventoryUIRep openInventory(InventoryRep inventory) {
        IInventory inv = ((InventoryFR)inventory).getForge();
        PlayerEntity player = getForge();
        if (inv instanceof INamedContainerProvider) {
            player.openContainer((INamedContainerProvider) inv);
            return null; //todo
        }
        throw new NotImplementedException("Does not know how to display inventory without associated UI"); //todo fix maybe
    }

    @Override
    public ItemStackRep getItemInHand() {
        return toOil(getForge().getHeldItem(getForge().getActiveHand()));
    }

    @Override
    public ItemStackRep getItemOnCursor() {
        throw new NotImplementedException("to be implemented"); //todo implement
        //return toOil(getForge().);
    }

    @Override
    public void setItemOnCursor(ItemStackRep item) {
        throw new NotImplementedException("to be implemented"); //todo implement
    }

    @Override
    public boolean isSleeping() {
        return getForge().isSleeping();
    }

    @Override
    public int getSleepTicks() {
        return getForge().getSleepTimer();
    }

    @Override
    public boolean isBlocking() {
        return getForge().isActiveItemStackBlocking();
    }

    @Override
    public int getExpToLevel() {//todo: consider removing, add xp api
        return getForge().xpBarCap();
    }

    @Override
    public boolean isSneaking() {
        return getForge().isSneaking();
    }

    @Override
    public void setSneaking(boolean sneak) {
        getForge().setSneaking(sneak);
    }

    @Override
    public boolean isSprinting() {
        return getForge().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getForge().setSprinting(sprinting);
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        throw new NotImplementedException("not implementable"); //todo remove
    }

    @Override
    public boolean getCanPickupItems() {
        throw new NotImplementedException("not implementable");  //todo remove
    }

    @Override
    public void setAI(boolean ai) {
        throw new NotImplementedException("not implementable");  //todo remove
    }

    @Override
    public boolean hasAI() {
        throw new NotImplementedException("not implementable");  //todo remove
    }

}
