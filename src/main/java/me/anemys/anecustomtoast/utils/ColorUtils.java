package me.anemys.anecustomtoast.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    public static String translateHexColorCodes(String message) {
        final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group();
            matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + group.charAt(1)
                    + ChatColor.COLOR_CHAR + group.charAt(2)
                    + ChatColor.COLOR_CHAR + group.charAt(3)
                    + ChatColor.COLOR_CHAR + group.charAt(4)
                    + ChatColor.COLOR_CHAR + group.charAt(5)
                    + ChatColor.COLOR_CHAR + group.charAt(6)
            );
        }
        String processed = matcher.appendTail(buffer).toString();
        return ChatColor.translateAlternateColorCodes('&', processed);
    }
}