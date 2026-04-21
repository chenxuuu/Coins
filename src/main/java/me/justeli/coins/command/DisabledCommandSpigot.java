package me.justeli.coins.command;

import me.justeli.coins.Coins;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eli
 * @since April 21, 2026 (creation)
 */
public final class DisabledCommandSpigot extends DisabledCommandLogic implements CommandExecutor {
    public DisabledCommandSpigot(Coins coins) {
        super(coins);

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
        executeCommand(sender);
        return true;
    }
}
