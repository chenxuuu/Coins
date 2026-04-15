package me.justeli.coins.item;

import me.justeli.coins.Coins;
import me.justeli.coins.config.Config;
import me.justeli.coins.util.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author Eli
 * @since January 30, 2022 (creation)
 */
public final class BaseCoin {
    private final MetaBuilder withdrawnCoin;
    private final MetaBuilder droppedCoin;
    private final MetaBuilder otherCoin;

    public BaseCoin(Coins coins) {
        String texture = Config.SKULL_TEXTURE;

        ItemStack baseCoin;
        if (texture == null || texture.isEmpty()) {
            baseCoin = new ItemStack(Config.COIN_ITEM);
        }
        else {
            baseCoin = Objects.requireNonNullElseGet(
                Skull.of(texture),
                () -> new ItemStack(Config.COIN_ITEM)
            );
        }

        var baseCoinMeta = baseCoin.getItemMeta();
        if (baseCoinMeta != null) {
            if (Config.CUSTOM_MODEL_DATA > 0) {
                baseCoinMeta.setCustomModelData(Config.CUSTOM_MODEL_DATA);
            }

            if (Config.ENCHANTED_COIN) {
                baseCoinMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                baseCoinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            baseCoin.setItemMeta(baseCoinMeta);
        }

        this.withdrawnCoin = coins.meta(baseCoin.clone())
            .setData(CoinUtil.COINS_TYPE, CoinUtil.TYPE_WITHDRAWN);

        MetaBuilder droppedCoinItem = coins.meta(baseCoin.clone())
            .setName(Config.DROPPED_COIN_NAME)
            .setData(CoinUtil.COINS_TYPE, CoinUtil.TYPE_DROPPED);

        if (Config.DROP_EACH_COIN) {
            droppedCoinItem.setData(CoinUtil.COINS_WORTH, 1D);
        }

        this.droppedCoin = droppedCoinItem;
        this.otherCoin = coins.meta(baseCoin.clone())
            .setName(Config.DROPPED_COIN_NAME)
            .setData(CoinUtil.COINS_TYPE, CoinUtil.TYPE_OTHER);
    }

    public MetaBuilder cloneBaseDropped() {
        return droppedCoin.clone();
    }

    public MetaBuilder cloneBaseWithdrawn() {
        return withdrawnCoin.clone();
    }

    public MetaBuilder cloneBaseOther() {
        return otherCoin.clone();
    }
}
