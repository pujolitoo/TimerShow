package me.pujolitoo.timershow;

import me.pujolitoo.timershow.command.TlmCommand;
import me.pujolitoo.timershow.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TimerShow extends JavaPlugin{
    private void setupCommands(){
        getCommand("tlm").setExecutor(new TlmCommand(this));
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        setupCommands();
        getServer().getConsoleSender().sendMessage(Logger.SUCCESS + "TimerShow has started correctly!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
