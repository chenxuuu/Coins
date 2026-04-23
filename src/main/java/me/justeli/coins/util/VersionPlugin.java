package me.justeli.coins.util;

import java.time.Instant;

/**
 * @author Eli
 * @since April 15, 2026
 */
public final class VersionPlugin {
    private final String tag;
    private final boolean preRelease;
    private final String name;
    private final long time;

    public VersionPlugin(String tag, boolean preRelease, String name, String time) {
        this.tag = tag;
        this.preRelease = preRelease;
        this.name = name;
        this.time = Instant.parse(time).toEpochMilli();
    }

    @Override
    public boolean equals(Object o) {
        return tag != null && o instanceof VersionPlugin version && tag.equals(version.tag);
    }

    public String getTag() {
        return tag;
    }

    public boolean isPreRelease() {
        return preRelease;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
}
