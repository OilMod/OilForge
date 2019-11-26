package org.oilmod.oilforge;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtil {


    public static void setFinal(Field field, Object on, Object newValue){
        try {
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            int old;
            modifiersField.setInt(field, (old=field.getModifiers()) & ~Modifier.FINAL);

            field.set(on, newValue);


            modifiersField.setInt(field, old);
            field.setAccessible(false);
            modifiersField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

    }
}
