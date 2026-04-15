package me.justeli.coins.item;

import me.justeli.coins.Coins;
import me.justeli.coins.util.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.logging.Level;

/**
 * @author Eli
 * @since January 30, 2022 (creation)
 */
public final class MetaBuilder implements Cloneable {
    private final Coins coins;
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public MetaBuilder(Coins coins, ItemStack itemStack) {
        this.coins = coins;
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public MetaBuilder setName(String name) {
        itemMeta.setDisplayName(Util.color(name));
        return this;
    }

    public MetaBuilder setData(String key, Integer value) {
        itemMeta.getPersistentDataContainer().set(
            new NamespacedKey(coins, key),
            PersistentDataType.INTEGER, value
        );
        return this;
    }

    public MetaBuilder setData(String key, Double value) {
        itemMeta.getPersistentDataContainer().set(
            new NamespacedKey(coins, key),
            PersistentDataType.DOUBLE, value
        );
        return this;
    }

    public <T> Optional<T> setData(String key, @NotNull PersistentDataType<T, T> type) {
        if (itemMeta == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(itemMeta.getPersistentDataContainer().get(
            new NamespacedKey(coins, key), type
        ));
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public MetaBuilder clone() {
        final MetaBuilder clone;
        try {
            clone = (MetaBuilder) super.clone();
        }
        catch (CloneNotSupportedException exception) {
            coins.getLogger().log(Level.WARNING, exception.getMessage());
            return new MetaBuilder(coins, build().clone());
        }
        return clone;
    }
}
