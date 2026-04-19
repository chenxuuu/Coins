package me.justeli.coins.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eli
 * @since April 19, 2026 (creation)
 */
public final class EntityCoinDropEvent extends Event implements Cancellable {
    private final Player attacker;
    private final Entity dead;

    public EntityCoinDropEvent(@Nullable Player attacker, @NotNull Entity dead) {
        this.attacker = attacker;
        this.dead = dead;
    }

    public @Nullable Player getAttacker() {
        return attacker;
    }

    public @NotNull Entity getDead() {
        return dead;
    }

    // -- Cancellable --

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    // -- HandlerList --

    private static final HandlerList HANDLERS = new HandlerList();

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
