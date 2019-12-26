package io.github.portlek.configs.annotations.values;

import io.github.portlek.configs.MagicConstants;
import io.github.portlek.itemstack.util.XMaterial;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {

    @NotNull
    String path() default "";

    @NotNull
    String separator() default "-";

    @NotNull
    String stringValue() default MagicConstants.NULL_STRING;

    @NotNull
    String[] stringArrayValue() default MagicConstants.NULL_STRING;

    int intValue() default 0;

    @NotNull
    EnchantmentValue[] enchantmentValue() default @EnchantmentValue();

    @NotNull
    ItemStackValue itemStackValue() default @ItemStackValue();

    @NotNull
    TitleValue titleValue() default @TitleValue();

    @NotNull
    XMaterial materialValue() default XMaterial.AIR;

}
