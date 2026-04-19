package me.justeli.coins.hook.mythicmobs;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.adapters.item.ItemComponentBukkitItemStack;
import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import io.lumine.mythic.core.drops.droppables.VanillaItemDrop;
import me.justeli.coins.Coins;
import me.justeli.coins.config.Config;
import me.justeli.coins.event.EntityCoinDropEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * works for MythicMobs 5.7 and up
 * @author Eli
 * @since April 02, 2022 (creation)
 */
public final class MythicMobsHook implements Listener {
    private final Coins coins;

    // todo move mythic mobs hook to separate plugin
    public MythicMobsHook(Coins coins) {
        this.coins = coins;
        coins.parseEventHandlers(this);
    }

    private static final BukkitAPIHelper BUKKIT_API_HELPER = new BukkitAPIHelper();

    @EventHandler
    void onEntityCoinDropEvent(EntityCoinDropEvent event) {
        if (Config.DISABLE_MYTHIC_MOB_HANDLING && BUKKIT_API_HELPER.isMythicMob(event.getDead())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onMythicDropLoadEvent(MythicDropLoadEvent event) {
        if (!event.getDropName().equalsIgnoreCase("coins")) {
            return;
        }

        VanillaItemDrop drop = new VanillaItemDrop(
            event.getConfig().getLine(),
            event.getConfig(),
            new ItemComponentBukkitItemStack(coins.getCreateCoin().createDropped())
        );

        event.register(drop);
    }
}
