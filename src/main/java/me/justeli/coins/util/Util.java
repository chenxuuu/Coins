package me.justeli.coins.util;

import me.justeli.coins.config.Config;
import me.justeli.coins.config.Message;
import me.justeli.coins.config.MessagePosition;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.SplittableRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eli
 * @since January 6, 2020 (creation)
 */
public final class Util {
    private static final Pattern HEX_PATTERN = Pattern.compile("(?<!\\\\)(&#[a-fA-F\\d]{6})");
    private static final SplittableRandom RANDOM = new SplittableRandom();

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(Message.NO_PERMISSION.toString());
    }

    // todo new way of color parsing
    public static String color(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', parseRgb(message));
    }

    public static String formatAmountAndCurrency(String text, double amount) {
        String displayAmount = doubleToString(amount);
        return formatCurrency(text.replaceAll("(%amount%|\\{amount})", Matcher.quoteReplacement(displayAmount)));
    }

    public static String formatCurrency(String text) {
        // {currency} or {$}
        return text.replaceAll("(\\{currency}|\\{\\$})", Matcher.quoteReplacement(Config.CURRENCY_SYMBOL));
    }

    // todo new way of color parsing
    private static String parseRgb(@NotNull String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            String hex = color.replace("&", "").toUpperCase();

            message = message.replace(color, ChatColor.of(hex).toString());
            matcher = HEX_PATTERN.matcher(message);
        }

        return message;
    }

    public static boolean isHostile(Entity entity) {
        return entity instanceof Monster
            || entity instanceof Flying
            || entity instanceof Slime
            || (entity instanceof Golem && !(entity instanceof Snowman))
            || (entity instanceof Wolf && ((Wolf) entity).isAngry())
            || entity instanceof Boss;
    }

    public static boolean isPassive(Entity entity) {
        return !isHostile(entity)
            && !(entity instanceof Player)
            && entity instanceof LivingEntity
            && !(entity instanceof ArmorStand);
    }

    public static Optional<Player> getOnlinePlayer(String incomplete) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String lowercaseName = player.getName().toLowerCase();
            String lowercaseIncomplete = incomplete.toLowerCase();
            if (lowercaseName.startsWith(lowercaseIncomplete)) {
                return Optional.of(player);
            }
            else if (player.getDisplayName().toLowerCase().contains(lowercaseIncomplete)) {
                return Optional.of(player);
            }
            else if (lowercaseName.contains(lowercaseIncomplete)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public static void playCoinPickupSound(Player player) {
        float volume = Config.SOUND_VOLUME;
        float pitch = Config.SOUND_PITCH;

        player.playSound(
            player.getEyeLocation(),
            Config.SOUND_NAME.toString(),
            volume <= 0? .3F : volume,
            pitch <= 0? .3F : pitch
        );
    }

    public static boolean isDisabledHere(@Nullable World world) {
        if (world == null) {
            return true;
        }

        return Config.DISABLED_WORLDS.contains(world.getName());
    }

    public static double getRandomMoneyAmount() {
        double second = Config.MONEY_AMOUNT_FROM;
        double first = Config.MONEY_AMOUNT_TO - second;

        return RANDOM.nextDouble() * first + second;
    }

    public static double getRandomTakeAmount() {
        double second = Config.MONEY_TAKEN_FROM;
        double first = Config.MONEY_TAKEN_TO - second;

        return RANDOM.nextDouble() * first + second;
    }

    public static double round(double value) {
        return BigDecimal.valueOf(value).setScale(Config.MONEY_DECIMALS, RoundingMode.HALF_UP).doubleValue();
    }

    public static String doubleToString(double input) {
        return Config.DECIMAL_FORMATTER.format(round(input));
    }

    public static Optional<Integer> parseInt(String arg) {
        try {
            return Optional.of(Integer.parseInt(arg));
        }
        catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Double> parseDouble(String arg) {
        try {
            return Optional.of(Util.round(Double.parseDouble(arg)));
        }
        catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM d, yyyy");

    /// page starts at 1
    public static ArrayList<String> page(ArrayList<String> items, int pageSize, int pageNumber) {
        if (pageNumber <= 0) {
            return new ArrayList<>();
        }

        ArrayList<String> pages = new ArrayList<>();
        for (int i = (pageNumber - 1) * pageSize; i < pageNumber * pageSize; i++) {
            if (items.size() <= i) {
                continue;
            }

            pages.add(items.get(i));
        }

        return pages;
    }

    public static Optional<Player> getRootDamage(LivingEntity dead) {
        if (dead.getKiller() != null) {
            return Optional.of(dead.getKiller());
        }

        EntityDamageEvent damageCause = dead.getLastDamageCause();
        if (damageCause instanceof EntityDamageByEntityEvent) {
            return getRootDamage((EntityDamageByEntityEvent) damageCause);
        }

        return Optional.empty();
    }

    public static Optional<Player> getRootDamage(EntityDamageByEntityEvent damageEvent) {
        Entity attacker = damageEvent.getDamager();
        if (attacker instanceof Player player) {
            return Optional.of(player);
        }

        if (!(attacker instanceof Projectile projectile)) {
            return Optional.empty();
        }

        ProjectileSource shooter = projectile.getShooter();
        if (shooter instanceof Player player) {
            return Optional.of(player);
        }

        return Optional.empty();
    }

    public static void sendMessage(Player player, String message, MessagePosition position, double amount) {
        switch (position) {
            case ACTIONBAR -> player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(Util.color(Util.formatAmountAndCurrency(message, amount)))
            );
            case TITLE -> player.sendTitle(
                Util.color(Util.formatAmountAndCurrency(message, amount)), ChatColor.RESET.toString(), 10, 60, 10
            );
            case SUBTITLE -> player.sendTitle(
                ChatColor.RESET.toString(), Util.color(Util.formatAmountAndCurrency(message, amount)), 10, 60, 10
            );
            case CHAT -> player.sendMessage(Util.color(Util.formatAmountAndCurrency(message, amount)));
        }
    }
}
