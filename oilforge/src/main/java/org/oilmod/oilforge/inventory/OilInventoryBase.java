package org.oilmod.oilforge.inventory;

import io.netty.buffer.Unpooled;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.config.Compound;
import org.oilmod.api.inventory.ModInventoryObjectBase;
import org.oilmod.api.rep.inventory.InventoryHolderRep;
import org.oilmod.api.userinterface.IInteractableUIElement;
import org.oilmod.api.util.ITicker;
import org.oilmod.oilforge.inventory.container.SetItemFilterPacket;
import org.oilmod.oilforge.items.OilForgeItemHelper;
import org.oilmod.oilforge.rep.inventory.InventoryFR;
import org.oilmod.oilforge.rep.location.WorldFR;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * Created by sirati97 on 13.02.2016.
 */
public abstract class OilInventoryBase<APIObject extends ModInventoryObjectBase> extends Inventory implements OilIInventory<APIObject> {
    private final WeakReference<InventoryHolderRep> owner;
    private final IItemFilter itemFilter;
    private final NonNullList<ItemStack> items;
    private final List<ItemStack> itemsReadOnly;
    private WeakReference<APIObject> modInventoryObject;
    private ITicker ticker;
    private final boolean needsOwner;
    private final InventoryFR inventoryRep;
    private ITextComponent displayName;


    public OilInventoryBase(InventoryHolderRep owner, String title, int size, ITicker ticker, IItemFilter itemFilter, boolean needsOwner) {
        super(size);
        setTitle(title);
        this.items = extractItems();
        this.itemsReadOnly = Collections.unmodifiableList(this.items);
        this.owner = new WeakReference<>(owner);
        this.inventoryRep = new InventoryFR(this);
        Validate.notNull(itemFilter);
        this.itemFilter = itemFilter;
        this.ticker = ticker;
        this.needsOwner = needsOwner;
        if (isTickable() && ticker != null) {
            ticker.add(this);
        }
    }

    public World getWorld() {
        return ((WorldFR)ticker.getMainWorld()).getForge();
    }

    private static final Field inventoryContentsField;
    static {
        try {
            inventoryContentsField = Inventory.class.getDeclaredField("inventoryContents");
            inventoryContentsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    private NonNullList<ItemStack> extractItems() {
        try {
            //noinspection unchecked
            return (NonNullList<ItemStack>) inventoryContentsField.get(this);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public InventoryHolderRep getOwner() {
        return owner.get();
    }

    @Override
    public IItemFilter getItemFilter() {
        return itemFilter;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return super.isItemValidForSlot(index, stack) && getItemFilter().allowed(stack);
    }

    @Override
    public boolean isValid() {
        return !needsOwner || getOwner()!=null;
    }

    @Override
    public void load(Compound compound) {
        OilForgeItemHelper.loadItemsFromCompound(compound, this.items, "Items");
    }

    @Override
    public void save(Compound compound) {
        OilForgeItemHelper.saveItemsToCompound(compound, this.items, "Items");
    }

    @Override
    public void setOilApiMirror(APIObject modInventoryObject) {
        this.modInventoryObject = new WeakReference<>(modInventoryObject);
    }

    public APIObject getModInventoryObject() {
        return modInventoryObject.get();
    }

    @Override
    public IInteractableUIElement createUIElement() {
        throw new NotImplementedException("todo"); //todo
        //return new OilInventoryViewSlot(this);
    }

    @Override
    public void clear() {
        items.clear();
    }

    public List<ItemStack> getItems() {
        return itemsReadOnly;
    }

    @Override
    public void setTitle(String s) {
        this.displayName = new StringTextComponent(s);
    }

    @Override
    public ITextComponent getDisplayName() {
        return displayName;
    }

    @Override
    public InventoryFR getInventoryRep() {
        return inventoryRep;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
    }

    @Override
    public void writeExtraData(PacketBuffer buffer) {
        SetItemFilterPacket packet = new SetItemFilterPacket(getItemFilter());
        packet.encode(buffer);
    }
}
