package me.justeli.coins.command;

import me.justeli.coins.Coins;
import me.justeli.coins.config.Config;
import me.justeli.coins.config.Message;
import me.justeli.coins.util.ComponentUtil;
import me.justeli.coins.util.Permissions;
import me.justeli.coins.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Eli
 * @since December 26, 2018 (creation)
 */
public abstract class WithdrawCommandLogic {
    private final Coins coins;
    public WithdrawCommandLogic(Coins coins) {
        this.coins = coins;
    }

    public void executeCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (coins.isDisabled()) {
            sender.sendMessage(Message.COINS_DISABLED.toString());
            return;
        }

        if (!Config.ENABLE_WITHDRAW) {
            sender.sendMessage(Message.WITHDRAWING_DISABLED.toString());
            return;
        }

        if (!Permissions.hasWithdraw(sender) || !(sender instanceof Player player)) {
            coins.getMessenger().sendNoPermission(sender);
            return;
        }

        if (Util.isDisabledHere(player.getWorld())) {
            sender.sendMessage(Message.COINS_DISABLED.toString());
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Message.INVENTORY_FULL.toString());
            return;
        }

        if (args.length == 0) {
            player.sendMessage(Message.WITHDRAW_USAGE.toString());
            return;
        }

        double worth = Util.toRoundedMoneyDecimals(Util.parseDouble(args[0]).orElse(0D));
        int amount = args.length >= 2? Util.parseInt(args[1]).orElse(0) : 1;
        double total = worth * amount;

        if (worth <= 0 || amount < 1 || total <= 0 || amount > 64) {
            sender.sendMessage(Message.INVALID_AMOUNT.toString());
            return;
        }

        if (worth > Config.MAX_WITHDRAW_AMOUNT) {
            player.sendMessage(Message.NOT_THAT_MUCH.toString());
            return;
        }

        coins.getEconomy().canAfford(player.getUniqueId(), total, canAfford -> {
            if (canAfford) {
                coins.getEconomy().withdraw(player.getUniqueId(), total, () -> {
                    ItemStack coin = coins.getCreateCoin().createWithdrawn(worth);
                    coin.setAmount(amount);

                    player.getInventory().addItem(coin);
                    player.sendMessage(Message.WITHDRAW_COINS.replace(Util.toFormattedMoneyDecimals(total)));

                    if (!Config.WITHDRAW_MESSAGE.equals(Component.empty())) {
                        coins.getMessenger().sendMessage(
                            player, Config.WITHDRAW_MESSAGE_POSITION,
                            ComponentUtil.replaceAmount(Config.WITHDRAW_MESSAGE, total)
                        );
                    }
                });
            }
            else {
                player.sendMessage(Message.NOT_THAT_MUCH.toString());
            }
        });
    }

    private static final List<String> VALUE_ARGUMENT = List.of("<value>"); // todo language
    private static final List<String> AMOUNT_ARGUMENT = List.of("[amount]");

    public List<String> getTabCompletions(@NotNull String[] args) {
        if (args.length == 1) {
            return VALUE_ARGUMENT;
        }
        else if (args.length == 2) {
            return AMOUNT_ARGUMENT;
        }
        return List.of();
    }
}
