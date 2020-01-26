package org.oilmod.oilforge.items;

import de.sirati97.minherit.IMapper;
import de.sirati97.minherit.Processor;
import net.minecraft.item.*;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.ItemImplementationProvider;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.oilforge.items.tools.OilItemTier;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import static cpw.mods.modlauncher.api.LamdbaExceptionUtils.*;
import static org.oilmod.api.items.type.ItemImplementationProvider.TypeEnum.CUSTOM;
import static org.oilmod.api.items.type.ItemImplementationProvider.TypeEnum.FOOD;
import static org.oilmod.oilforge.Util.toOil;
import static org.oilmod.oilforge.items.RealItem.createBuilder;

public class RealIIPHelper extends ItemImplementationProvider.Helper<RealIIPHelper> {
    @Override
    protected void apiInit() {
        
    }

    @Override
    protected void apiPostInit() {

    }

    private interface ImplementationDelegate<T extends Item & RealItemImplHelper>{
        T implement(OilItem oilItem);
    }

    private static class DIP extends ItemImplementationProvider {
        private final ImplementationDelegate<?> delegate;

        private DIP(ItemImplementationProvider.TypeEnum typeEnum, ImplementationDelegate<?> delegate) {
            super(typeEnum);
            this.delegate = delegate;
        }

        @Override
        public ItemRep implement(OilItem oilItem) {
            return toOil(delegate.implement(oilItem));
        }
    }


    private static class DIPTransforming extends ItemImplementationProvider {
        private final Class<? extends Item> parentClass;
        private final Supplier<IMapper> mapperSupplier;
        private ImplementationDelegate<?> delegate;

        private DIPTransforming(ItemImplementationProvider.TypeEnum typeEnum, Class<? extends Item> parentClass, Supplier<IMapper> mapperSupplier) {
            super(typeEnum);

            this.parentClass = parentClass;
            this.mapperSupplier = mapperSupplier;
        }

        private void init() {
            try {
                Class<RealItemImplHelper> transformedClass = Processor.unify(RealItemImplHelper.class, parentClass, RealItem.class, mapperSupplier);
                Constructor<RealItemImplHelper> ctor = transformedClass.getConstructor(OilItem.class);
                //noinspection rawtypes
                delegate = (ImplementationDelegate)((oilItem) -> (Item) rethrowFunction(ctor::newInstance).apply(oilItem));
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public ItemRep implement(OilItem oilItem) {
            if (delegate == null) {
                init();
            }
            return toOil(delegate.implement(oilItem));
        }
    }
    //todo assign keys to implementation provider
    @SuppressWarnings("unchecked")
    private static class ToolMapper implements IMapper {
        private OilItem oilItem;

        @Override
        public <T> IMapper inObj(int argIndex, T argObj) {
            oilItem = (OilItem) argObj;
            return this;
        }

        @Override
        public <T> T outObj(int argIndex) {
            if (argIndex == 0) {
                return (T) new OilItemTier(oilItem);
            } else if (argIndex == 3) {
               return (T) createBuilder(oilItem);
            }
            return IMapper.notImpl();
        }

        @Override
        public int outInt(int argIndex) {
            return (int) outFloat(argIndex); //PickaxeItem for some reason uses int
        }

        @Override
        public float outFloat(int argIndex) {
            if (argIndex == 1) {
                return 0; //todo
            } else if (argIndex == 2) {
                return 1; //todo
            }
            return IMapper.notImpl();
        }
    }

    @Override
    protected ItemImplementationProvider getProvider(ItemImplementationProvider.TypeEnum t) {
        if (t == CUSTOM || t == FOOD) {
            return new DIP(t, RealItem::new);
        } else {
            try {
                return new DIPTransforming(t, getImplBase(t), getMapper(t));
            } catch (NotImplementedException e) { //todo get rid of this
                return new DIP(t, RealItem::new);
            }
        }
    }

    private Class<? extends Item> getImplBase(ItemImplementationProvider.TypeEnum t) {
        switch (t) {
            case PICKAXE:
                return  PickaxeItem.class;
            case SHOVEL:
                return ShovelItem.class;
            case AXE:
                return AxeItem.class;
            case SHEARS:
                return ShearsItem.class;
            case HOE:
                return HoeItem.class;
            case TOOL_CUSTOM:
                return ToolItem.class;
            case SWORD:
                return SwordItem.class;
            case MELEE_WEAPON_CUSTOM:
                return TieredItem.class;
            case BOW:
                return BowItem.class;
            case BOW_CUSTOM:
                return ShootableItem.class;
            case ARMOR_HELMET:
            case ARMOR_CHESTPLATE:
            case ARMOR_LEGGINGS:
            case ARMOR_SHOES:
            case ARMOR_CUSTOM:
                return ArmorItem.class;
            case FOOD:
            case CUSTOM:
            default:
                throw new IllegalStateException(t + "does not require special impl");
        }
    }
    private Supplier<IMapper> getMapper(ItemImplementationProvider.TypeEnum t) {
        switch (t) {
            case PICKAXE:
            case SHOVEL:
            case AXE:
            case SWORD:
                return ToolMapper::new;
            case SHEARS:
            case HOE:
            case TOOL_CUSTOM:
            case MELEE_WEAPON_CUSTOM:
            case BOW:
            case BOW_CUSTOM:
            case ARMOR_HELMET:
            case ARMOR_CHESTPLATE:
            case ARMOR_LEGGINGS:
            case ARMOR_SHOES:
            case ARMOR_CUSTOM:
                throw new NotImplementedException("todo"); //todo
            case FOOD:
            case CUSTOM:
            default:
                throw new IllegalStateException(t + "does not require special impl");
        }
    }
}
