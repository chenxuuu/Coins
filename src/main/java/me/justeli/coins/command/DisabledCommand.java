package me.justeli.coins.command;

import me.justeli.coins.Coins;
import me.justeli.coins.config.Message;
import me.justeli.coins.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eli
 * @since August 2, 2021 (creation)
 */
public final class DisabledCommand implements CommandExecutor {
    private final Coins coins;
    public DisabledCommand(Coins coins) {
        this.coins = coins;

        var coinsCommand = coins.getCommand("coins");
        var withdrawCommand = coins.getCommand("withdraw");
        if (coinsCommand == null || withdrawCommand == null) {
            return;
        }

        coinsCommand.setExecutor(this);
        withdrawCommand.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(Message.DISABLED_REASONS.toString());
        for (String message : coins.getDisabledReasons()) {
            sender.sendMessage(Util.color("- &c" + message));
        }
        return true;
    }
}
