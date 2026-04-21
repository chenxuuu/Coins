package me.justeli.coins.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.justeli.coins.Coins;

import java.util.List;

/**
 * @author Eli
 * @since April 21, 2026 (creation)
 */
public final class DisabledCommandPaper extends DisabledCommandLogic implements BasicCommand {
    public DisabledCommandPaper(Coins coins) {
        super(coins);
        coins.getLifecycleManager().registerEventHandler(
            LifecycleEvents.COMMANDS, commands -> commands.registrar().register("coins", List.of("coin"), this)
        );
        coins.getLifecycleManager().registerEventHandler(
            LifecycleEvents.COMMANDS, commands -> commands.registrar().register("withdraw", this)
        );
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        executeCommand(commandSourceStack.getSender());
    }
}
