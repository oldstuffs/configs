package io.github.portlek.configs.processors;

import io.github.portlek.configs.ConfigSection;
import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Value;
import java.lang.reflect.Field;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class FieldsProceed implements Proceed<Managed> {

    @NotNull
    private final ConfigSection parent;

    public FieldsProceed(@NotNull final ConfigSection cnfsctn) {
        this.parent = cnfsctn;
    }

    @Override
    public void load(@NotNull final Managed managed) {
        for (final Field field : this.parent.getClass().getDeclaredFields()) {
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Optional.ofNullable(field.getDeclaredAnnotation(Instance.class)).map(instance ->
                Optional.of(
                    (Proceed<Field>) new InstanceProceed(
                        managed,
                        this.parent
                    )
                )
            ).orElseGet(() ->
                Optional.ofNullable(field.getDeclaredAnnotation(Value.class)).flatMap(value ->
                    Optional.of(
                        new ValueProceed(
                            managed,
                            this.parent,
                            value
                        )
                    )
                )
            ).ifPresent(proceed -> proceed.load(field));
            field.setAccessible(accessible);
        }
    }

}
