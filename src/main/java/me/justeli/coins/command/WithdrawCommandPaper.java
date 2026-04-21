package me.justeli.coins.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.justeli.coins.Coins;

import java.util.Collection;

/**
 * @author Eli
 * @since April 21, 2026 (creation)
 */
public final class WithdrawCommandPaper extends WithdrawCommandLogic implements BasicCommand {
    public WithdrawCommandPaper(Coins coins) {
        super(coins);
        coins.getLifecycleManager().registerEventHandler(
            LifecycleEvents.COMMANDS,
            commands -> commands.registrar().register(
                "withdraw",
                "Withdraw money from your balance into physical coins.",
                this
            )
        );
    }

    @Override
    public String permission() {
        return "coins.withdraw";
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        executeCommand(source.getSender(), args);
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return getTabCompletions(args);
    }
}
