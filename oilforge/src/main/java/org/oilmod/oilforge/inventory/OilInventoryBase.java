package org.oilmod.oilforge.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.config.Compound;
import org.oilmod.api.inventory.ModInventoryObjectBase;
import org.oilmod.api.rep.inventory.InventoryHolderRep;
import org.oilmod.api.userinterface.IInteractableUIElement;
import org.oilmod.api.util.ITicker;
import org.oilmod.oilforge.items.TempRealItemHelper;
import org.oilmod.oilforge.rep.inventory.InventoryFR;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * Created by sirati97 on 13.02.2016.
 */
public abstract class OilInventoryBase<APIObject extends ModInventoryObjectBase> extends InventoryBasic implements OilIInventory<APIObject> {
    private final WeakReference<InventoryHolderRep> owner;
    private final IItemFilter itemFilter;
    private final NonNullList<ItemStack> items;
    private final List<ItemStack> itemsReadOnly;
    private WeakReference<APIObject> modInventoryObject;
    private ITicker ticker;
    private final boolean needsOwner;
    private World world;
    private final InventoryFR inventoryRep;


    public OilInventoryBase(InventoryHolderRep owner, String title, int size, ITicker ticker, IItemFilter itemFilter, boolean needsOwner) {
        super(new TextComponentString(title), size);
        this.items = extractItems();
        this.itemsReadOnly = Collections.unmodifiableList(this.items);
        this.owner = new WeakReference<>(owner);
        this.inventoryRep = new InventoryFR(this);
        this.itemFilter = itemFilter==null?new NoItemFilter():itemFilter;
        this.ticker = ticker;
        this.needsOwner = needsOwner;
        if (isTickable() && ticker != null) {
            ticker.add(this);
        }
    }

    private static final Field inventoryContentsField;
    static {
        try {
            inventoryContentsField = InventoryBasic.class.getDeclaredField("inventoryContents");
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
        TempRealItemHelper.loadItemsFromCompound(compound, this.items, "Items");
    }

    @Override
    public void save(Compound compound) {
        TempRealItemHelper.saveItemsToCompound(compound, this.items, "Items");
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
        super.setCustomName(new TextComponentString(s));
    }

    @Override
    public InventoryFR getInventoryRep() {
        return inventoryRep;
    }
}