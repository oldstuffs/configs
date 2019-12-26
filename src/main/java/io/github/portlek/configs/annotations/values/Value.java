package io.github.portlek.configs.annotations.values;

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
    String[] stringValue() default {};

    @NotNull
    String[] stringArrayValue() default {};

    int[] intValue() default {};

    int[] intArrayValue() default {};

    @NotNull
    String[] enchantmentValue() default {};

    @NotNull
    String[] enchantmentArrayValue() default {};

    @NotNull
    ItemStackValue[] itemStackValue() default {};

    @NotNull
    TitleValue[] titleValue() default {};

    @NotNull
    XMaterial[] materialValue() default {};

    @NotNull
    XMaterial[] materialArrayValue() default {};

}
