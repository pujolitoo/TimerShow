package me.pujolitoo.timershow.util;

import org.bukkit.ChatColor;

public class Logger {
    private static final String pluginString = "[" + ChatColor.GREEN + "Timer" + ChatColor.YELLOW + "Show" + ChatColor.RESET + "]: ";
    public static final String INFO = pluginString + ChatColor.DARK_GRAY;
    public static final String SUCCESS = pluginString + ChatColor.GREEN;
    public static final String WARN = pluginString + ChatColor.YELLOW;
    public static final String ERROR = pluginString + ChatColor.RED;
}
