package me.pujolitoo.timershow.core;

import me.pujolitoo.timershow.TimerShow;
import me.pujolitoo.timershow.enums.TitleShowPlayers;
import me.pujolitoo.timershow.enums.TitleShowType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;

public class CounterInfo {

    public CounterInfo(JavaPlugin plugin, String name){this.name = name; this.plugin = plugin;}

    public JavaPlugin getPlugin() {
        return plugin;
    }

    JavaPlugin plugin;
    String name;
    int seconds = 10;
    String title = "%{seconds}";
    String subtitle = "";
    String chat;
    TitleShowType showType = TitleShowType.TITLE_ONLY;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    String command = "";

    public TitleShowPlayers getPlayersShow() {
        return playersShow;
    }

    public void setPlayersShow(TitleShowPlayers playersShow) {
        this.playersShow = playersShow;
    }

    TitleShowPlayers playersShow = TitleShowPlayers.ALL_PLAYERS;
    ArrayList<Integer> custom = new ArrayList<Integer>();
    ArrayList<Player> players = new ArrayList<Player>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public TitleShowType getShowType() {
        return showType;
    }

    public void setShowType(TitleShowType showType) {
        this.showType = showType;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

}
