package org.oilmod.oilforge.enchantments;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.items.EnchantmentType;
import org.oilmod.api.items.nms.NMSEnchantmentType;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.item.ItemStateRep;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.OilMain;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;

public class RealEnchantmentTypeHelper extends EnchantmentType.EnchantmentTypeHelper {
    private final Object2ObjectMap<net.minecraft.enchantment.EnchantmentType, EnchantmentType> customTypeMap = new Object2ObjectOpenHashMap<>();

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
                return impl(net.minecraft.enchantment.EnchantmentType.VANISHABLE, e);//todo
            case ARMOR:
                return impl(net.minecraft.enchantment.EnchantmentType.ARMOR, e);
            case ARMOR_BOOTS:
                return impl(net.minecraft.enchantment.EnchantmentType.ARMOR_FEET, e);
            case ARMOR_LEGGINGS:
                return impl(net.minecraft.enchantment.EnchantmentType.ARMOR_LEGS, e);
            case ARMOR_CHEST:
                return impl(net.minecraft.enchantment.EnchantmentType.ARMOR_CHEST, e);
            case ARMOR_HELMET:
                return impl(net.minecraft.enchantment.EnchantmentType.ARMOR_HEAD, e);
            case WEAPON:
                return impl(net.minecraft.enchantment.EnchantmentType.WEAPON, e);
            case DIGGER:
                return impl(net.minecraft.enchantment.EnchantmentType.DIGGER, e);
            case FISHING_ROD:
                return impl(net.minecraft.enchantment.EnchantmentType.FISHING_ROD, e);
            case TRIDENT:
                return impl(net.minecraft.enchantment.EnchantmentType.TRIDENT, e);
            case BREAKABLE:
                return impl(net.minecraft.enchantment.EnchantmentType.BREAKABLE, e);
            case BOW:
                return impl(net.minecraft.enchantment.EnchantmentType.BOW, e);
            case WEARABLE:
                return impl(net.minecraft.enchantment.EnchantmentType.WEARABLE, e);
            case NONE:
                return implNONE();
            default:
                throw new  IllegalStateException("Cannot call with argument " + e);
        }
    }

    public EnchantmentType convertToOil(net.minecraft.enchantment.EnchantmentType forge) {
        switch (forge) {
            case VANISHABLE:
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
            case CROSSBOW:
            default:
                return customTypeMap.get(forge);
        }
    }

    @Override
    protected NMSEnchantmentType registerCustom(EnchantmentType enchantmentType) {
        return nms(net.minecraft.enchantment.EnchantmentType.create(enchantmentType.getOilKey().toString(), item -> enchantmentType.canEnchant(toOil(item).getStandardState())));
    }

    private NMSEnchantmentType nms(net.minecraft.enchantment.EnchantmentType forgeEnum) {
        return new NMSEnchantmentTypeImpl(forgeEnum);
    }
    private EnchantmentTypeImpl impl(net.minecraft.enchantment.EnchantmentType forge, EnchantmentType.EnchantmentTypeEnum blockTypeEnum, EnchantmentType... subtypes) {
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
        private final net.minecraft.enchantment.EnchantmentType forge;
        private EnchantmentType oil;

        public NMSEnchantmentTypeImpl(net.minecraft.enchantment.EnchantmentType forge) {
            this(forge, null);
        }

        public void setOil(EnchantmentType oil) {
            Validate.isTrue(this.oil==null);
            this.oil = oil;
        }

        public NMSEnchantmentTypeImpl(net.minecraft.enchantment.EnchantmentType forge, EnchantmentType oil) {
            this.forge = forge;
            this.oil = oil;
        }

        public net.minecraft.enchantment.EnchantmentType getForge() {
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
            ref.value = new NMSEnchantmentTypeImpl(net.minecraft.enchantment.EnchantmentType.create(key.toString(),item -> ref.value.oil.canEnchant(toOil(item).getStandardState())));
            return ref.value;
        }

        private EnchantmentTypeImpl(EnchantmentTypeEnum blockTypeEnum, OilKey key, EnchantmentType... subtypes) {
            this(createNMS(blockTypeEnum, key), key, blockTypeEnum , subtypes);
        }

        protected EnchantmentTypeImpl(net.minecraft.enchantment.EnchantmentType forge, EnchantmentTypeEnum blockTypeEnum, EnchantmentType... subtypes) {
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
