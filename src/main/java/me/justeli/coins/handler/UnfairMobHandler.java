package me.justeli.coins.handler;

import me.justeli.coins.Coins;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author Eli
 * @since December 13, 2016 (creation)
 */
public final class UnfairMobHandler implements Listener {
    private final NamespacedKey fromSplitKey;
    private final NamespacedKey fromSpawnerKey;

    public UnfairMobHandler(Coins coins) {
        this.fromSplitKey = new NamespacedKey(coins, "coins-slime-split");
        this.fromSpawnerKey = new NamespacedKey(coins, "coins-spawner-mob");
    }

    @EventHandler
    void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == SpawnReason.SPAWNER || event.getEntityType() == EntityType.CAVE_SPIDER) {
            event.getEntity().getPersistentDataContainer().set(fromSpawnerKey, PersistentDataType.INTEGER, 1);
        }
        else if (event.getSpawnReason() == SpawnReason.SLIME_SPLIT) {
            event.getEntity().getPersistentDataContainer().set(fromSplitKey, PersistentDataType.INTEGER, 1);
        }
    }

    @EventHandler
    void onEntityTransformEvent(EntityTransformEvent event) {
        if (isFromSpawner(event.getEntity())) {
            for (Entity entity : event.getTransformedEntities()) {
                entity.getPersistentDataContainer().set(fromSpawnerKey, PersistentDataType.INTEGER, 1);
            }
        }
    }

    public boolean isFromSplit(Entity entity) {
        return entity.getPersistentDataContainer().has(fromSplitKey, PersistentDataType.INTEGER);
    }

    public boolean isFromSpawner(Entity entity) {
        return entity.getPersistentDataContainer().has(fromSpawnerKey, PersistentDataType.INTEGER);
    }
}
