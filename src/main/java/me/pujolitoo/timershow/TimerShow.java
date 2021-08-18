package me.pujolitoo.timershow;

import me.pujolitoo.timershow.command.TlmCommand;
import org.bukkit.ChatColor;
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
        System.out.println("PLUGIN STARTED!!!a");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
