package org.oilmod.oilforge.ui.container;

import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.UI.UI;
import org.oilmod.api.UI.UIFactory;
import org.oilmod.api.data.IDataParent;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.oilforge.config.nbttag.OilNBTCompound;
import org.oilmod.oilforge.ui.UIRegistryHelper;

import static org.oilmod.oilforge.Util.toForge;

public class SetUIPacket {
    public static final Logger LOGGER = LogManager.getLogger();
    private final UIFactory uiFactory;
    private final Object context;

    public SetUIPacket(UIFactory uiFactory, Object context) {
        this.uiFactory = uiFactory;
        this.context = context;
    }

    public SetUIPacket(EntityPlayerRep player,  PacketBuffer buffer) {
        this.uiFactory = UIRegistryHelper.get(buffer.readResourceLocation());
        IDataParent data = uiFactory.createDataParent(player);
        OilNBTCompound compound = new OilNBTCompound(buffer.readCompoundTag());
        data.getRegisteredIData().forEach((s, iData) -> iData.loadFrom(compound, s));
        this.context = uiFactory.getContext(data);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(toForge(uiFactory.getOilKey()));
        //noinspection unchecked
        IDataParent data = uiFactory.getDataParent(context);
        OilNBTCompound compound = new OilNBTCompound();
        data.getRegisteredIData().forEach((s, iData) -> iData.saveTo(compound, s));
        buffer.writeCompoundTag(compound.getCompoundNBT());
    }

    public UI<?> createUI() {
        //noinspection unchecked
        return uiFactory.create(context);
    }

}
