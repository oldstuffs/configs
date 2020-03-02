package io.github.portlek.configs;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.processors.ConfigProceed;
import io.github.portlek.configs.util.BukkitVersion;
import io.github.portlek.configs.util.ColorUtil;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitManaged extends ManagedBase {

    private static final BukkitVersion BUKKIT_VERSION = new BukkitVersion();
    @NotNull
    protected final BiFunction<Object, String, Optional<?>> get = (o, s) -> {
        if (o instanceof ItemStack) {
            return getItemStack(s);
        }

        return Optional.empty();
    };
    @NotNull
    protected final BiPredicate<Object, String> set = (o, s) -> {
        if (o instanceof ItemStack) {
            setItemStack(s, (ItemStack) o);

            return true;
        }

        return false;
    };

    @SafeVarargs
    public BukkitManaged(@NotNull Map.Entry<String, Object>... objects) {
        super(objects);
    }

    @Override
    public void load() {
        final Config config = getClass().getDeclaredAnnotation(Config.class);

        if (config != null) {
            try {
                new ConfigProceed(
                    config,
                    get,
                    set
                ).load(this);

                return;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        throw new UnsupportedOperationException();
    }

    @NotNull
    public Optional<ItemStack> getItemStack(@NotNull String path) {
        if (!getString(path).isPresent()) {
            return Optional.empty();
        }
        final Optional<String> materialString = getString(path + ".material");
        if (!materialString.isPresent()) {
            return Optional.empty();
        }
        final Optional<XMaterial> xMaterialOptional = XMaterial.matchXMaterial(materialString.get());
        if (!xMaterialOptional.isPresent()) {
            return Optional.empty();
        }
        final Optional<Material> materialOptional = Optional.ofNullable(xMaterialOptional.get().parseMaterial());
        if (!materialOptional.isPresent()) {
            return Optional.empty();
        }
        final int amount = getInt(path + ".amount");
        final ItemStack itemStack;
        if (BUKKIT_VERSION.minor() < 13) {
            itemStack = new ItemStack(
                materialOptional.get(),
                amount == 0 ? 1 : amount,
                (short) getInt(path + ".damage"),
                (byte) getInt(path + ".data")
            );
        } else {
            itemStack = new ItemStack(
                materialOptional.get(),
                amount == 0 ? 1 : amount,
                (short) getInt(path + ".damage")
            );
        }
        final Optional<ItemMeta> itemMetaOptional = Optional.ofNullable(itemStack.getItemMeta());
        if (!itemMetaOptional.isPresent()) {
            return Optional.empty();
        }
        final ItemMeta itemMeta = itemMetaOptional.get();
        getString(path + ".display-name").ifPresent(s ->
            itemMeta.setDisplayName(
                ColorUtil.colored(s)
            )
        );
        itemMeta.setLore(
            ColorUtil.colored(
                getStringList(path + ".lore")
            )
        );
        getSection(path + ".enchants").map(section -> section.getKeys(false)).ifPresent(set ->
            set.forEach(s ->
                XEnchantment.matchXEnchantment(s).flatMap(xEnchantment ->
                    Optional.ofNullable(xEnchantment.parseEnchantment())
                ).ifPresent(enchantment ->
                    itemMeta.addEnchant(enchantment, getInt(path + ".enchants." + s), true)
                )
            )
        );
        itemStack.setItemMeta(itemMeta);
        return Optional.of(itemStack);
    }

    public void setItemStack(@NotNull String path, @NotNull ItemStack itemStack) {
        set(path + ".material", itemStack.getType().name());
        set(path + ".amount", itemStack.getAmount());
        if (BUKKIT_VERSION.minor() < 13) {
            Optional.ofNullable(itemStack.getData()).ifPresent(materialData ->
                set(path + ".data", materialData.getData())
            );
        }
        if (itemStack.getDurability() != 0) {
            set(path + ".damage", itemStack.getDurability());
        }
        Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
            if (itemMeta.hasDisplayName()) {
                set(path + ".display-name", itemMeta.getDisplayName().replace("§", "&"));
            }
            Optional.ofNullable(itemMeta.getLore()).ifPresent(lore ->
                set(path + ".lore", lore.stream().map(s -> s.replace("§", "&")).collect(Collectors.toList()))
            );
        });
        itemStack.getEnchantments().forEach((enchantment, integer) ->
            set(path + ".enchants." + enchantment.getName(), integer)
        );
    }

}
