package io.github.portlek.configs.annotations.values;

import io.github.portlek.itemstack.util.XEnchantment;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnchantmentValue {

    @NotNull
    XEnchantment enchantment() default XEnchantment.DAMAGE_ALL;

    int level() default 0;

}
