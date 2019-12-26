package io.github.portlek.configs.annotations.values;

import io.github.portlek.configs.MagicConstants;
import io.github.portlek.itemstack.util.XMaterial;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemStackValue {

    @NotNull
    XMaterial material() default XMaterial.AIR;

    int data() default 0;

    @NotNull
    String displayName() default MagicConstants.NULL_STRING;

    @NotNull
    String[] lore() default {};

    @NotNull
    EnchantmentValue[] enchantments() default @EnchantmentValue();

    @NotNull
    String[] enchantmentStrings() default {};

}
