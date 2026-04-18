package me.justeli.coins.config;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;

/**
 * @author Eli
 * @since April 18, 2026
 */
public final class SoundKey {
    private final String key;

    public SoundKey(String sound) {
        this.key = sound;
    }

    public SoundKey(NamespacedKey key) {
        this(key.toString());
    }

    public SoundKey(Sound sound) {
        this(sound.getKey());
    }

    @Override
    public String toString() {
        return key;
    }
}
