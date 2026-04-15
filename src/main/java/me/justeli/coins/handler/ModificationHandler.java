package me.justeli.coins.handler;

import me.justeli.coins.Coins;
import me.justeli.coins.config.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Eli
 * @since January 30, 2022 (creation)
 */
public final class ModificationHandler implements Listener {
    private final Coins coins;
    public ModificationHandler(Coins coins) {
        this.coins = coins;
        coins.parseEventHandlers(this);
    }

    @EventHandler
    void onCraftItemEvent(CraftItemEvent event) {
        if (Config.ALLOW_MODIFICATION) {
            return;
        }

        for (ItemStack stack : event.getInventory().getContents()) {
            if (!coins.getCoinMeta().isCoin(stack)) {
                continue;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {
        if (Config.ALLOW_MODIFICATION) {
            return;
        }

        for (ItemStack stack : event.getInventory().getContents()) {
            if (coins.getCoinMeta().isCoin(stack)) {
                event.getInventory().setResult(null);
                break;
            }
        }
    }

    @EventHandler
    void onPrepareAnvilEvent(PrepareAnvilEvent event) {
        if (Config.ALLOW_NAME_CHANGE) {
            return;
        }

        if (event.getResult() != null && coins.getCoinMeta().isCoin(event.getResult())) {
            event.setResult(null);
        }
    }

    @EventHandler
    void onFurnaceSmeltEvent(FurnaceSmeltEvent event) {
        if (Config.ALLOW_MODIFICATION) {
            return;
        }

        if (coins.getCoinMeta().isCoin(event.getSource())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onFurnaceBurnEvent(FurnaceBurnEvent event) {
        if (Config.ALLOW_MODIFICATION) {
            return;
        }

        if (coins.getCoinMeta().isCoin(event.getFuel())) {
            event.setBurnTime(0);
            event.setBurning(false);
        }
    }
}
