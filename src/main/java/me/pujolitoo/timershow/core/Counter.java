package me.pujolitoo.timershow.core;

import me.pujolitoo.timershow.TimerShow;
import me.pujolitoo.timershow.enums.TitleShowPlayers;
import me.pujolitoo.timershow.enums.TitleShowType;
import me.pujolitoo.timershow.event.CounterEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Counter implements CounterEvents{

    JavaPlugin plugin;
    Player sender;
    CounterInfo info;
    BukkitTask task;
    String name;
    int seconds;
    String title;
    String subtitle;
    String chat;
    TitleShowType showType;
    int current = 0;
    TitleShowPlayers playersShow;
    ArrayList<Integer> custom = new ArrayList<Integer>();
    ArrayList<Player> players = new ArrayList<Player>();
    boolean secondsModifierTitle = false;
    boolean secondsModifierSubtitle = false;
    boolean secondsModifierChat = false;
    String modifier = "%{seconds}";
    String command = "";

    private boolean hasInfo = false;

    public Counter(){}

    public Counter(TimerShow plugin, String name, int seconds, String title, String subtitle, ArrayList<Player> players){
        this.plugin = plugin;
        this.name = name;
        this.seconds = seconds;
        this.title = title;
        this.subtitle = subtitle;
        this.showType = TitleShowType.TITLE_ONLY;
        this.players = players;
        this.playersShow = TitleShowPlayers.SPECIFIC;
        this.hasInfo = true;
    }

    public Counter(TimerShow plugin, String name, int seconds, String chat, ArrayList<Player> players){
        this.plugin = plugin;
        this.name = name;
        this.seconds = seconds;
        this.chat = chat;
        this.showType = TitleShowType.CHAT_ONLY;
        this.players = players;
        this.playersShow = TitleShowPlayers.SPECIFIC;
        this.hasInfo = true;
    }

    public Counter(TimerShow plugin, String name, int seconds, String title, String subtitle){
        this.plugin = plugin;
        this.name = name;
        this.seconds = seconds;
        this.title = title;
        this.subtitle = subtitle;
        this.showType = TitleShowType.TITLE_ONLY;
        this.playersShow = TitleShowPlayers.ALL_PLAYERS;
        this.hasInfo = true;
    }

    public Counter(TimerShow plugin, String name, int seconds, String chat){
        this.plugin = plugin;
        this.name = name;
        this.seconds = seconds;
        this.chat = chat;
        this.showType = TitleShowType.CHAT_ONLY;
        this.playersShow = TitleShowPlayers.ALL_PLAYERS;
        this.hasInfo = true;
    }

    public void setInfo(CounterInfo info){
        this.name = info.getName();
        this.chat = info.getChat();
        this.playersShow = info.getPlayersShow();
        this.seconds = info.getSeconds();
        this.subtitle = info.getSubtitle();
        this.title = info.getTitle();
        this.showType = info.getShowType();
        this.players = info.getPlayers();
        this.plugin = info.getPlugin();
        this.command = info.getCommand();
        this.hasInfo = true;
    }



    public CounterInfo getInfo(){
        CounterInfo info = new CounterInfo(this.plugin, this.name);
        info.setChat(chat);
        info.setPlayersShow(playersShow);
        info.setSeconds(seconds);
        info.setSubtitle(subtitle);
        info.setTitle(title);
        info.setShowType(showType);
        info.setPlayers(players);
        return info;
    }


    public boolean isCounting() {
        return counting;
    }

    public void setCounting(boolean counting){
        this.counting = counting;
    }

    boolean counting = false;

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        this.playersShow = TitleShowPlayers.SPECIFIC;
    }



    public void setSender(CommandSender sender){
        this.sender = (Player) sender;
    }

    public boolean countDown(){
        if(!this.counting && hasInfo){
            this.counting = true;
            task = new BukkitRunnable(){
                @Override
                public void run() {
                    int secondsA = seconds;
                    int currentA = current;
                    String title2 = "";
                    String subtitle2 = "";
                    String chat2 = "";
                    if(current < seconds){
                        switch(showType){
                            case ALL:
                                if(title.contains(modifier) || secondsModifierTitle){
                                    title2 = title.replace(modifier, Integer.toString(secondsA - currentA));
                                    secondsModifierTitle = true;
                                }else {
                                    title2 = title;
                                }
                                if(subtitle.contains(modifier) || secondsModifierSubtitle){
                                    subtitle2 = subtitle.replace(modifier, Integer.toString(secondsA - currentA));
                                    secondsModifierSubtitle = true;
                                }else {
                                    subtitle2 = subtitle;
                                }
                                if(chat.contains(modifier) || secondsModifierChat){
                                    chat2 = chat.replace(modifier, Integer.toString(secondsA - currentA));
                                    secondsModifierChat = true;
                                }else {
                                    chat2 = chat;
                                }
                                break;
                            case TITLE_ONLY:
                                if(title.contains(modifier) || secondsModifierTitle){
                                    title2 = title.replace(modifier, Integer.toString(secondsA - currentA));
                                    secondsModifierTitle = true;
                                }else {
                                    title2 = title;
                                }
                                if(subtitle.contains(modifier) || secondsModifierSubtitle){
                                    subtitle2 = subtitle.replace(modifier, Integer.toString(secondsA - currentA));
                                    secondsModifierSubtitle = true;
                                }else {
                                    subtitle2 = subtitle;
                                }
                                break;

                            case CHAT_ONLY:
                                if(chat.contains(modifier) || secondsModifierChat){
                                    chat2 = chat.replace(modifier, Integer.toString(secondsA - currentA));
                                    secondsModifierChat = true;
                                }else {
                                    chat2 = chat;
                                }
                                break;
                        }
                        onSecondChange();
                        switch(playersShow){
                            case ALL_PLAYERS:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(title2, subtitle);
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, SoundCategory.AMBIENT, 100, 1);
                                }
                                break;

                            case SPECIFIC:
                                if(!players.isEmpty()) {
                                    for (Player p : players) {
                                        p.sendTitle(title2, subtitle);
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, SoundCategory.AMBIENT, 100, 1);
                                    }
                                }else{
                                    sender.sendMessage("No players assigned to this counter.");
                                    this.cancel();
                                }
                                break;

                            case SENDER_ONLY:
                                sender.sendTitle(title2, subtitle);
                                sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, SoundCategory.AMBIENT, 100, 1);
                                break;

                            default:
                                sender.sendMessage(ChatColor.RED + "ERROR: Player target is not specified");
                                this.cancel();
                        }
                        current++;
                    }else{
                        onFinish();
                        cancel();
                    }
                }
            }.runTaskTimer(this.plugin, 0, 20);
            return true;
        }else{
            sender.sendMessage(ChatColor.RED +"Error: Another counter is executing.");
            return false;
        }
    }

    public void stopCount(){
        if(this.counting){
            this.counting = false;
            this.task.cancel();
            this.task = null;
            this.current = 0;
        }else{
            sender.sendMessage("No countdown is being executed.");
        }

    }

    @Override
    public void onFinish() {
        runCommand();
        stopCount();
    }

    @Override
    public void onSecondChange() {
    }

    private void runCommand(){
        if(!this.command.equals("")){
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.dispatchCommand(console, this.command);
        }
    }

}
