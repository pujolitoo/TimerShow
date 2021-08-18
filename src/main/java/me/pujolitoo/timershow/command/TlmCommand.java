package me.pujolitoo.timershow.command;

import me.pujolitoo.timershow.core.Counter;
import me.pujolitoo.timershow.TimerShow;
import me.pujolitoo.timershow.core.CounterInfo;
import me.pujolitoo.timershow.enums.TitleShowPlayers;
import me.pujolitoo.timershow.enums.TitleShowType;
import me.pujolitoo.timershow.event.CounterEvents;
import me.pujolitoo.timershow.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TlmCommand implements CommandExecutor, CounterEvents {
    TimerShow plugin;
    Counter counter;
    String usage = "Usage: /tlm <start/stop>";
    Player sender;
    ArrayList<CounterInfo> infos = new ArrayList<CounterInfo>();

    public TlmCommand(TimerShow plugin){
        this.plugin = plugin;
        counter = new Counter();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        this.sender = p;
        counter.setSender(p);
        if (args.length == 0) {
            p.sendMessage(usage);
        }else{
            parse(args);
        }
        return true;
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onSecondChange() {

    }

    private boolean checkRequieredArgs(String[] args, int argc){
        if(args.length < argc){
            sender.sendMessage(usage);
            return false;
        }
        return true;
    }

    private CounterInfo findInfo(ArrayList<CounterInfo> infos, String name){
        for(CounterInfo i : infos){
            if(i.getName().equals(name) && !infos.isEmpty()){
                return i;
            }
        }
        return null;
    }

    private void parse(String[] args){
        switch(args[0]){
            case "info":
                if(!checkRequieredArgs(args, 2))
                    break;
                CounterInfo showInfo = findInfo(infos, args[1]);
                if(!(showInfo == null)){
                    sender.sendMessage(ChatColor.BOLD + showInfo.getName() + ":");
                    sender.sendMessage(ChatColor.YELLOW + "    Name: " + ChatColor.GREEN + showInfo.getName());
                    sender.sendMessage(ChatColor.YELLOW + "    Seconds: " + ChatColor.GREEN + showInfo.getSeconds() + " seconds");
                    if(!showInfo.getTitle().equals("")){
                        sender.sendMessage(ChatColor.YELLOW +"    Title: " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', showInfo.getTitle()));
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "    Title: " + ChatColor.RED + "None");
                    }

                    if(!showInfo.getSubtitle().equals("")){
                        sender.sendMessage(ChatColor.YELLOW + "    Subtitle: " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', showInfo.getSubtitle()));
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "    Subtitle: " + ChatColor.RED + "None");
                    }

                    if(!(showInfo.getChat() == null)){
                        sender.sendMessage(ChatColor.YELLOW + "    Chat: " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', showInfo.getChat()));
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "    Chat: " + ChatColor.RED + "None");
                    }

                    sender.sendMessage(ChatColor.YELLOW + "    ShowType: " + ChatColor.GREEN + showInfo.getShowType());
                    if(showInfo.getPlayersShow() == TitleShowPlayers.SPECIFIC){
                        sender.sendMessage(ChatColor.YELLOW + "    Player Visibility: " + ChatColor.GREEN + "SPECIFIC");

                    }else if(showInfo.getPlayersShow() == TitleShowPlayers.ALL_PLAYERS){
                        sender.sendMessage(ChatColor.YELLOW + "    Player Visibility: " + ChatColor.GREEN + "ALL_PLAYERS");
                    }else if(showInfo.getPlayersShow() == TitleShowPlayers.SENDER_ONLY){
                        sender.sendMessage(ChatColor.YELLOW + "    Player Visibility: " + ChatColor.GREEN + "SENDER_ONLY");
                    }

                    if(!showInfo.getPlayers().isEmpty()){
                        sender.sendMessage(ChatColor.YELLOW + "    Specific players:");
                        for(Player p : showInfo.getPlayers()){
                            sender.sendMessage(ChatColor.YELLOW + "        - " + ChatColor.GREEN + p.getName());
                        }
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "    Specific players: " + ChatColor.RED + "None");
                    }

                    if(!showInfo.getCommand().equals("")){
                        sender.sendMessage(ChatColor.YELLOW + "    Command: " + ChatColor.GREEN + showInfo.getCommand());
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "    Command: " + ChatColor.RED + "None");
                    }

                }else{
                    sender.sendMessage(Logger.ERROR + "Counter not found.");
                }
                break;
            case "create":
            case "add":
                if(!checkRequieredArgs(args, 2))
                    break;
                if(!infos.contains(findInfo(infos, args[1]))){
                    CounterInfo info = new CounterInfo(this.plugin, args[1]);
                    infos.add(info);
                    sender.sendMessage(Logger.SUCCESS + "Counter " + args[1] + " was created successfully");
                }else{
                    sender.sendMessage(Logger.WARN + "This counter already exists.");
                }
                break;

            case "set":
                if(!checkRequieredArgs(args, 2))
                    break;
                parseSet(args, findInfo(infos, args[1]));
                break;

            case "start":
                if(!checkRequieredArgs(args, 2))
                    break;
                CounterInfo i = findInfo(infos, args[1]);
                if(!(i == null)){
                    counter.setInfo(i);
                    if(!counter.countDown()){
                        sender.sendMessage(Logger.ERROR + "There was an unexpected error!");
                    }
                }else{
                    sender.sendMessage(Logger.ERROR + "Counter not found.");
                }
                break;

            case "stop":
                counter.stopCount();
                break;

            case "list":
                if(!infos.isEmpty()){
                    sender.sendMessage(ChatColor.YELLOW + "Counters available:");
                    for(CounterInfo list : infos){
                        sender.sendMessage(ChatColor.YELLOW + "- " + ChatColor.GREEN + list.getName());
                    }
                }else{
                    sender.sendMessage(Logger.WARN + "There are no counters available.");
                }
                break;

            case "remove":
                if(!checkRequieredArgs(args, 2))
                    break;
                CounterInfo current = findInfo(infos, args[1]);
                if(!(current == null)){
                    String name = current.getName();
                    if(!infos.remove(current)){
                        sender.sendMessage(Logger.ERROR + "Counter not found.");
                    } else{
                        sender.sendMessage(Logger.SUCCESS + "Counter " + name + " was removed successfully.");
                    }
                }else{
                    sender.sendMessage(Logger.ERROR + "Counter not found.");
                }
                break;

            case "removeAll":
                if(args.length < 2) {
                    sender.sendMessage(Logger.WARN + "To confirm, please write [/tlm removeAll sure]");
                    break;
                }

                if(args[1].equals("sure")){
                    this.infos = new ArrayList<CounterInfo>();
                    sender.sendMessage(Logger.SUCCESS + "Counters removed successfully");
                }
                break;

            default:
                sender.sendMessage(usage);
        }
    }

    private void parseSet(String[] args, CounterInfo info){
        if(!checkRequieredArgs(args, 3))
            return;

        switch(args[2]){
            case "setTitle":
                if(!checkRequieredArgs(args, 4))
                    break;
                StringBuilder builder2 = new StringBuilder();
                for(int i = 3; i < args.length; i++){
                    builder2.append(args[i]);
                    builder2.append(" ");
                }
                info.setTitle(ChatColor.translateAlternateColorCodes('&', builder2.toString()));
                break;

            case "setSubtitle":
                if(!checkRequieredArgs(args, 4))
                    break;
                StringBuilder builder3 = new StringBuilder();
                for(int i = 3; i < args.length; i++){
                    builder3.append(args[i]);
                    builder3.append(" ");
                }
                info.setSubtitle(ChatColor.translateAlternateColorCodes('&', builder3.toString()));
                break;

            case "setPlayerVisibility":
                if(!checkRequieredArgs(args, 4))
                    break;
                switch (args[3]) {
                    case "ALL":
                        info.setPlayersShow(TitleShowPlayers.ALL_PLAYERS);
                        break;
                    case "SPECIFIC":
                        info.setPlayersShow(TitleShowPlayers.SPECIFIC);
                        break;
                    case "SENDER":
                        info.setPlayersShow(TitleShowPlayers.SENDER_ONLY);
                        break;

                    default:
                        sender.sendMessage(Logger.ERROR + "This is not a valid flag!");
                }
                break;

            case "setChat":
                if(!checkRequieredArgs(args, 4))
                    break;
                StringBuilder builder4 = new StringBuilder();
                for(int i = 3; i < args.length; i++){
                    builder4.append(args[i]);
                    builder4.append(" ");
                }
                info.setChat(ChatColor.translateAlternateColorCodes('&', builder4.toString()));
                break;

            case "setPlayers":
                if(!checkRequieredArgs(args, 4))
                    break;
                ArrayList<Player> players = info.getPlayers();
                if(args[3].equals("add")){
                    if(!checkRequieredArgs(args, 5))
                        break;
                    Player addPlayer = Bukkit.getPlayer(args[4]);
                    if(addPlayer == null){
                        sender.sendMessage(Logger.ERROR + "Player not found, player must be on server to be added.");
                        break;
                    }
                    players.add(addPlayer);
                    info.setPlayers(players);
                }else if(args[3].equals("remove")){
                    if(!checkRequieredArgs(args, 5))
                        break;
                    players.remove(Bukkit.getPlayer(args[4]));
                    info.setPlayers(players);
                }else{
                    sender.sendMessage(usage);
                }
                break;

            case "setSeconds":
                if(!checkRequieredArgs(args, 4))
                    break;
                try{info.setSeconds(Integer.parseInt(args[3]));}catch(NumberFormatException e){sender.sendMessage(usage);}
                break;

            case "setName":
                if(!checkRequieredArgs(args, 4))
                    break;
                info.setName(args[3]);
                break;

            case "setCounterVisibility":
                if(!checkRequieredArgs(args, 4))
                    break;
                switch(args[3]){
                    case "TITLE":
                        info.setShowType(TitleShowType.TITLE_ONLY);
                        break;
                    case "CHAT":
                        info.setShowType(TitleShowType.CHAT_ONLY);
                        break;

                    case "ALL":
                        info.setShowType(TitleShowType.ALL);
                        break;

                    default:
                        sender.sendMessage(Logger.ERROR + "This is not a valid flag!");
                }
                break;

            case "setCommand":
                if(!checkRequieredArgs(args, 4))
                    break;
                StringBuilder builder = new StringBuilder();
                for(int i = 3; i < args.length; i++){
                    builder.append(args[i]);
                    builder.append(" ");
                }
                info.setCommand(builder.toString());
                break;

            default:
                sender.sendMessage(usage);
        }
    }

}
