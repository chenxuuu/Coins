package me.justeli.coins.handler;

import me.justeli.coins.Coins;
import me.justeli.coins.config.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Eli
 * @since December 13, 2016 (creation)
 */
public final class HopperHandler implements Listener {
    private final Coins coins;
    public HopperHandler(Coins coins) {
        this.coins = coins;
    }

    @EventHandler(ignoreCancelled = true)
    void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        if (!Config.DISABLE_HOPPERS) {
            return;
        }

        if (event.getInventory().getType() != InventoryType.HOPPER) {
            return;
        }

        ItemStack item = event.getItem().getItemStack();
        if (!coins.getCoinUtil().isDroppedCoin(item)) {
            return;
        }

        event.setCancelled(true);
    }
}
