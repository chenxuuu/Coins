package me.justeli.coins.command;

import me.justeli.coins.Coins;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Eli
 * @since April 21, 2026 (creation)
 */
public final class WithdrawCommandSpigot extends WithdrawCommandLogic implements CommandExecutor, TabCompleter {
    public WithdrawCommandSpigot(Coins coins) {
        super(coins);

        var command = coins.getCommand("withdraw");
        if (command == null) {
            return;
        }

        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        executeCommand(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return getTabCompletions(args);
    }
}
