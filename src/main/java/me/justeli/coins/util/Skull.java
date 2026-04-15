package me.justeli.coins.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Eli
 * @since January 6, 2020 (creation)
 */
public final class Skull {
    private static final Map<String, ItemStack> COINS = new HashMap<>();
    private static final UUID SKULL_UUID = UUID.fromString("00000001-0001-0001-0001-000000000002");
    private static final ItemStack SKULL_ITEM = new ItemStack(Material.PLAYER_HEAD);
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    public static @Nullable ItemStack of(String texture) {
        if (texture == null || texture.isEmpty()) {
            return null;
        }

        if (COINS.containsKey(texture)) {
            return COINS.get(texture);
        }

        if (!(SKULL_ITEM.getItemMeta() instanceof SkullMeta skullMeta)) {
            return null;
        }

        String url;
        if (texture.startsWith("http://textures.minecraft.net/texture/")) {
            // is already in the right format
            url = texture;
        }
        else if (texture.length() > 60 && texture.length() <= 70) {
            // is probably the id without the url
            url = "http://textures.minecraft.net/texture/" + texture;
        }
        else {
            // is probably base64 texture
            try {
                String decoded = new String(DECODER.decode(texture));
                url = decoded.split("\"url\":\"")[1].split("\"")[0].strip();
            }
            catch (Throwable throwable) {
                return null;
            }
        }

        var profile = Bukkit.getServer().createPlayerProfile(SKULL_UUID, "randomCoin");
        try {
            profile.getTextures().setSkin(URI.create(url).toURL());
        }
        catch (MalformedURLException exception) {
            return null;
        }

        skullMeta.setOwnerProfile(profile);
        SKULL_ITEM.setItemMeta(skullMeta);

        COINS.put(texture, SKULL_ITEM);
        return SKULL_ITEM;
    }
}
