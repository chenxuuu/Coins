package me.justeli.coins.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Optional;

/**
 * @author Eli
 * @since February 04, 2022 (creation)
 */
public final class VersionChecker {
    private Version latestVersion;
    public Optional<Version> getLatestVersion() {
        return Optional.of(latestVersion);
    }

    public VersionChecker(String repository) {
        try {
            URL url = new URL("https://api.github.com/repos/" + repository + "/releases/latest");
            URLConnection request = url.openConnection();

            request.setReadTimeout(1000);
            request.setConnectTimeout(1000);
            request.connect();

            try (var reader = new InputStreamReader((InputStream) request.getContent())) {
                JsonElement root = JsonParser.parseReader(reader);
                JsonObject jsonObject = root.getAsJsonObject();
                latestVersion = new Version(
                    jsonObject.get("tag_name").getAsString(),
                    jsonObject.get("prerelease").getAsBoolean(),
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("published_at").getAsString()
                );
            }
        }
        catch (Exception ignored) {}
    }

    public record Version(String tag, boolean preRelease, String name, long time) {
        public Version(String tag, boolean preRelease, String name, String time) {
            this(tag, preRelease, name, Instant.parse(time).toEpochMilli());
        }

        @Override
        public boolean equals(Object o) {
            return tag != null && o instanceof Version version && tag.equals(version.tag());
        }
    }
}
