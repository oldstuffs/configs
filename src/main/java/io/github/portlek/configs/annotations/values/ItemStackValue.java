package io.github.portlek.configs.annotations.values;

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
    XMaterial material();

    int[] data() default {};

    @NotNull
    String[] displayName() default {};

    @NotNull
    String[] lore() default {};

    @NotNull
    String[] enchantments() default {};

}
