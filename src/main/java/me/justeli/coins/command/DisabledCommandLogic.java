package me.justeli.coins.command;

import me.justeli.coins.Coins;
import me.justeli.coins.config.Message;
import me.justeli.coins.util.Util;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eli
 * @since August 2, 2021 (creation)
 */
public abstract class DisabledCommandLogic {
    private final Coins coins;
    public DisabledCommandLogic(Coins coins) {
        this.coins = coins;
    }

    public void executeCommand(@NotNull CommandSender sender) {
        sender.sendMessage(Message.DISABLED_REASONS.toString());
        for (String message : coins.getDisabledReasons()) {
            sender.sendMessage(Util.color("- &c" + message));
        }
    }
}
