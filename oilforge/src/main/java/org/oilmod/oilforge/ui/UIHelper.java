package org.oilmod.oilforge.ui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.oilmod.api.UI.IItemInteractionHandler;
import org.oilmod.api.UI.IItemRef;
import org.oilmod.api.UI.UI;
import org.oilmod.api.UI.UIMPI;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.inventory.InventoryView;
import org.oilmod.oilforge.inventory.container.OilContainerType;
import org.oilmod.oilforge.ui.container.SetUIPacket;
import org.oilmod.oilforge.ui.container.UIContainer;

import javax.annotation.Nullable;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;
import static org.oilmod.oilforge.inventory.container.ContainerUtil.*;

public class UIHelper extends UIMPI.Helper<UIHelper> {
    public final static VanillaItemInteractionHandler nativeHandler = new VanillaItemInteractionHandler();

    @Override
    protected void openUI(EntityPlayerRep playerRep, UI ui) {
        SetUIPacket packet = new SetUIPacket(ui.getFactory(), ui.getContext());
        PlayerEntity player = toForge(playerRep);
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                @Nullable
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new UIContainer(OilContainerType.CUSTOM_UI, id, playerInventory, ui);
                }

                @Override
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("Test");
                }
            }, packet::encode);
        }
        //otherwise we are clientside maybe



    }

    @Override
    protected void handleNative(IItemRef handle, InventoryRep inv) {
        RealItemRef ref = (RealItemRef) handle;
        if (inv instanceof InventoryView) {
            InventoryView view = (InventoryView) inv;
            ref.fixIndexForView(view);
            inv = view.getRoot();
        }
        ref.setNative(toForge(inv));
    }

    @Override
    protected void handleCustom(IItemRef handle, InventoryRep inv) {
        ((RealItemRef)handle).setCustom(inv);

    }

    @Override
    protected void handleCustom(IItemRef handle, InventoryRep inv, IItemInteractionHandler handler) {
        ((RealItemRef)handle).setCustom(inv, handler);

    }

    @Override
    protected int getSizeSlots() {
        return GuiSlotSize;
    }

    @Override
    protected int getSizeBorder() {
        return GuiOffSide;
    }

    @Override
    protected int getSizeText() {
        return GuiOffTop-GuiOffSide;
    }

    @Override
    protected int getSizeItemRender() {
        return 16;
    }
}
