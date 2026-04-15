package me.justeli.coins.item;

import me.justeli.coins.Coins;
import me.justeli.coins.config.Config;
import me.justeli.coins.util.Util;
import org.bukkit.inventory.ItemStack;

import java.util.SplittableRandom;

/**
 * @author Eli
 * @since January 30, 2022 (creation)
 */
public final class CreateCoin {
    private final Coins coins;
    public CreateCoin(Coins coins) {
        this.coins = coins;
    }

    private static final SplittableRandom RANDOM = new SplittableRandom();

    public ItemStack createWithdrawn(double worth) {
        var plural = worth == 1? Config.WITHDRAWN_COIN_NAME_SINGULAR : Config.WITHDRAWN_COIN_NAME_PLURAL;
        String name = Util.formatAmountAndCurrency(plural, worth);

        return coins.getBaseCoin().cloneBaseWithdrawn()
            .setData(CoinMeta.COINS_WORTH, worth)
            .setName(name).build();
    }

    private MetaBuilder createDropBuilder() {
        MetaBuilder coin = coins.getBaseCoin().cloneBaseDropped();
        if (Config.DROP_EACH_COIN || !Config.STACK_COINS) {
            return coin.setData(CoinMeta.COINS_RANDOM, RANDOM.nextInt());
        }
        return coin;
    }

    public ItemStack createDropped() {
        return createDropBuilder().build();
    }

    public ItemStack createDropped(double increment) {
        if (increment == 1) {
            return createDropped();
        }

        MetaBuilder coin = createDropBuilder()
            .setData(CoinMeta.COINS_INCREMENT, increment);

        return coin.build();
    }

    public MetaBuilder createOther() {
        return coins.getBaseCoin().cloneBaseOther();
    }
}
