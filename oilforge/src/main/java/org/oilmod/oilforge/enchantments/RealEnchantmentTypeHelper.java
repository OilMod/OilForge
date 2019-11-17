package org.oilmod.oilforge.enchantments;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.enchantment.EnumEnchantmentType;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.items.EnchantmentType;
import org.oilmod.api.items.nms.NMSEnchantmentType;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.OilMain;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;

public class RealEnchantmentTypeHelper extends EnchantmentType.EnchantmentTypeHelper {
    private final Object2ObjectMap<EnumEnchantmentType, EnchantmentType> customTypeMap = new Object2ObjectOpenHashMap<>();

    @Override
    protected void apiInit() {

    }

    @Override
    protected void apiPostInit() {
        ((EnchantmentTypeImplAllVanilla)EnchantmentType.ALL_VANILLA).init();
    }

    @Override
    protected EnchantmentType getVanillaEnchantmentType(EnchantmentType.EnchantmentTypeEnum e) {
        switch (e) {
            case ALL_VANILLA:
                return implAllVanilla();
            case ALL:
                return impl(EnumEnchantmentType.ALL, e);
            case ARMOR:
                return impl(EnumEnchantmentType.ARMOR, e);
            case ARMOR_BOOTS:
                return impl(EnumEnchantmentType.ARMOR_FEET, e);
            case ARMOR_LEGGINGS:
                return impl(EnumEnchantmentType.ARMOR_LEGS, e);
            case ARMOR_CHEST:
                return impl(EnumEnchantmentType.ARMOR_CHEST, e);
            case ARMOR_HELMET:
                return impl(EnumEnchantmentType.ARMOR_HEAD, e);
            case WEAPON:
                return impl(EnumEnchantmentType.WEAPON, e);
            case DIGGER:
                return impl(EnumEnchantmentType.DIGGER, e);
            case FISHING_ROD:
                return impl(EnumEnchantmentType.FISHING_ROD, e);
            case TRIDENT:
                return impl(EnumEnchantmentType.TRIDENT, e);
            case BREAKABLE:
                return impl(EnumEnchantmentType.BREAKABLE, e);
            case BOW:
                return impl(EnumEnchantmentType.BOW, e);
            case WEARABLE:
                return impl(EnumEnchantmentType.WEARABLE, e);
            case NONE:
                return implNONE();
            default:
                throw new  IllegalStateException("Cannot call with argument " + e);
        }
    }

    public EnchantmentType convertToOil(EnumEnchantmentType forge) {
        switch (forge) {
            case ALL:
                return EnchantmentType.ALL;
            case ARMOR:
                return EnchantmentType.ARMOR;
            case ARMOR_FEET:
                return EnchantmentType.ARMOR_BOOTS;
            case ARMOR_LEGS:
                return EnchantmentType.ARMOR_LEGGINGS;
            case ARMOR_CHEST:
                return EnchantmentType.ARMOR_CHEST;
            case ARMOR_HEAD:
                return EnchantmentType.ARMOR_HELMET;
            case WEAPON:
                return EnchantmentType.WEAPON;
            case DIGGER:
                return EnchantmentType.DIGGER;
            case FISHING_ROD:
                return EnchantmentType.FISHING_ROD;
            case TRIDENT:
                return EnchantmentType.TRIDENT;
            case BREAKABLE:
                return EnchantmentType.BREAKABLE;
            case BOW:
                return EnchantmentType.BOW;
            case WEARABLE:
                return EnchantmentType.WEARABLE;
            default:
                return customTypeMap.get(forge);
        }
    }

    @Override
    protected NMSEnchantmentType registerCustom(EnchantmentType enchantmentType) {
        EnumEnchantmentType.create(enchantmentType.getOilKey().toString(), item -> enchantmentType.canEnchant(toOil(item).getStandardState()));
        return null;
    }

    private NMSEnchantmentType nms(EnumEnchantmentType forgeEnum) {
        return new NMSEnchantmentTypeImpl(forgeEnum);
    }
    private EnchantmentTypeImpl impl(EnumEnchantmentType forge, EnchantmentType.EnchantmentTypeEnum blockTypeEnum, EnchantmentType... subtypes) {
        EnchantmentTypeImpl impl = new EnchantmentTypeImpl(forge, blockTypeEnum, subtypes);
        switch (blockTypeEnum) {
            case ALL_VANILLA:
            case NONE:
            case ENUM_MISSING:
            case CUSTOM:
                throw new IllegalStateException("Cannot call with argument " + blockTypeEnum);
        }
        return impl;
    }
    private EnchantmentTypeImpl implNONE() {
        EnchantmentTypeImpl impl = new EnchantmentTypeImpl(EnchantmentType.EnchantmentTypeEnum.NONE);
        customTypeMap.put(impl.getNmsEnchantmentType().forge, impl);
        return impl;
    }

    private EnchantmentTypeImpl implAllVanilla() {
        EnchantmentTypeImpl impl = new EnchantmentTypeImplAllVanilla(EnchantmentType.EnchantmentTypeEnum.ALL_VANILLA, Arrays.stream(EnchantmentType.EnchantmentTypeEnum.values()).filter(e -> !e.special).map(EnchantmentType::getStandard));
        customTypeMap.put(impl.getNmsEnchantmentType().forge, impl);
        return impl;
    }

    public static class NMSEnchantmentTypeImpl implements NMSEnchantmentType {
        private final EnumEnchantmentType forge;
        private EnchantmentType oil;

        public NMSEnchantmentTypeImpl(EnumEnchantmentType forge) {
            this(forge, null);
        }

        public void setOil(EnchantmentType oil) {
            Validate.isTrue(this.oil==null);
            this.oil = oil;
        }

        public NMSEnchantmentTypeImpl(EnumEnchantmentType forge, EnchantmentType oil) {
            this.forge = forge;
            this.oil = oil;
        }

        public EnumEnchantmentType getForge() {
            return forge;
        }

        public EnchantmentType getOil() {
            return oil;
        }

        @Override
        public boolean canEnchantNMS(ItemStateRep item) {
            return forge.canEnchantItem(toForge(item.getItem()));
        }

        @Override
        public boolean containsEnchantmentNMS(EnchantmentRep ench) {
            return oil.containsSubtype(toOil(toForge(ench).type));
        }
    }

    public static class EnchantmentTypeImpl extends EnchantmentType {
        private static OilKey key(String idenitifer) {
            return OilMain.ModMinecraft.createKey("enchant_type_" + idenitifer.toLowerCase());
        }

        protected EnchantmentTypeImpl(EnchantmentTypeEnum blockTypeEnum, EnchantmentType... subtypes) {
            this(blockTypeEnum, key(blockTypeEnum.toString()), subtypes);
        }

        private static class Ref {
            NMSEnchantmentTypeImpl value;
        }

        private static NMSEnchantmentTypeImpl createNMS(EnchantmentTypeEnum blockTypeEnum, OilKey key) {
            Ref ref = new Ref();
            ref.value = new NMSEnchantmentTypeImpl(EnumEnchantmentType.create(key.toString(),item -> ref.value.oil.canEnchant(toOil(item).getStandardState())));
            return ref.value;
        }

        private EnchantmentTypeImpl(EnchantmentTypeEnum blockTypeEnum, OilKey key, EnchantmentType... subtypes) {
            this(createNMS(blockTypeEnum, key), key, blockTypeEnum , subtypes);
        }

        protected EnchantmentTypeImpl(EnumEnchantmentType forge, EnchantmentTypeEnum blockTypeEnum, EnchantmentType... subtypes) {
            this(new NMSEnchantmentTypeImpl(forge), key(forge.toString()) , blockTypeEnum, subtypes);
        }

        private EnchantmentTypeImpl(NMSEnchantmentTypeImpl nms, OilKey key, EnchantmentTypeEnum blockTypeEnum, EnchantmentType... subtypes) {
            super(nms, key, blockTypeEnum, subtypes);
            nms.setOil(this);
        }

        @Override
        public NMSEnchantmentTypeImpl getNmsEnchantmentType() {
            return (NMSEnchantmentTypeImpl) super.getNmsEnchantmentType();
        }

        @Override
        public void addSubtype(EnchantmentType enchantmentType) {
            throw new IllegalStateException("Cannot modify native enchantment types");
        }

        void _addSubtype(EnchantmentType enchantmentType) {
            super.addSubtype(enchantmentType);
        }
    }


    public static class EnchantmentTypeImplAllVanilla extends EnchantmentTypeImpl {
        Stream<EnchantmentType> future;

        void init() {
            Validate.isTrue(future!=null, "already initialised");
            future.forEach(this::addSubtype);
            future = null;
        }

        protected EnchantmentTypeImplAllVanilla(EnchantmentTypeEnum blockTypeEnum, Stream<EnchantmentType> subtypes) {
            super(blockTypeEnum);
            future =subtypes;
        }

        @Override
        public void addSubtype(EnchantmentType enchantmentType) {
            if (future==null)super.addSubtype(enchantmentType);
            else _addSubtype(enchantmentType);
        }
    }


}
