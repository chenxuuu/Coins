package me.justeli.coins.util;

import java.time.Instant;

/**
 * @author Eli
 * @since April 15, 2026
 */
public record PluginVersion(String tag, boolean preRelease, String name, long time) {
    public PluginVersion(String tag, boolean preRelease, String name, String time) {
        this(tag, preRelease, name, Instant.parse(time).toEpochMilli());
    }

    @Override
    public boolean equals(Object o) {
        return tag != null && o instanceof PluginVersion version && tag.equals(version.tag());
    }
}
