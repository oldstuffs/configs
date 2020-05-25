package io.github.portlek.configs.obj;

import io.github.portlek.configs.annotations.ConfigSerializable;
import io.github.portlek.configs.annotations.Property;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ConfigSerializable
@ToString
@Getter
public final class ProvidedObject extends ProvidedObjectAbs {

    @Property
    private final String asşdjasi;

    @Property
    private final String asdasdasd;

    @Property
    private final String dasdas;

    @Property
    private final String asdasdadq;

    @Property
    private final String qweqadsa;

    public ProvidedObject(final UUID uuid, final String name, final int age, final Test test, final String asşdjasi,
                          final String asdasdasd, final String dasdas, final String asdasdadq, final String qweqadsa) {
        super(uuid, name, age, test);
        this.asşdjasi = asşdjasi;
        this.asdasdasd = asdasdasd;
        this.dasdas = dasdas;
        this.asdasdadq = asdasdadq;
        this.qweqadsa = qweqadsa;
    }

}
