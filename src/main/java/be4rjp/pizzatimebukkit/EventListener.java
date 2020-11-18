package be4rjp.pizzatimebukkit;

import be4rjp.pizzatimebukkit.share.ShareClient;
import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.LunaChatBukkit;
import com.github.ucchyocean.lc3.LunaChatConfig;
import com.github.ucchyocean.lc3.bukkit.BukkitNormalChatJapanizeTask;
import com.github.ucchyocean.lc3.channel.Channel;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.member.ChannelMemberBukkit;
import com.github.ucchyocean.lc3.util.ClickableFormat;
import com.github.ucchyocean.lc3.util.Utility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventListener implements Listener {
    @EventHandler
    public void onTabComplete(TabCompleteEvent event){
        if(Main.config.getConfig().getBoolean("hijack-tell-commands")) {
            CommandSender sender = event.getSender();
    
            String commands[] = {"/tell ", "/msg ", "/m ", "/t ", "/w "};
    
            for (String buffer : Arrays.asList(commands)) {
                if (event.getBuffer().contains(buffer)) {
                    event.setCompletions(Main.playerList);
                    break;
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String chat = Chat.toLunaChat(player, event.getMessage());
    
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for(String server : Main.config.getConfig().getConfigurationSection("servers").getKeys(false)){
                    try {
                        String host = Main.config.getConfig().getString("servers." + server + ".host");
                        int port = Main.config.getConfig().getInt("servers." + server + ".port");
                        new ShareClient(host, port, "chat " + chat).startClient();
                    }catch (Exception e){}
                }
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!Main.playerList.contains(player.getName()))
            Main.playerList.add(player.getName());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for(String server : Main.config.getConfig().getConfigurationSection("servers").getKeys(false)){
                    try {
                        String host = Main.config.getConfig().getString("servers." + server + ".host");
                        int port = Main.config.getConfig().getInt("servers." + server + ".port");
                        new ShareClient(host, port, "join " + player.getName()).startClient();
                    }catch (Exception e){}
                }
            }
        };
        task.runTaskLaterAsynchronously(Main.getPlugin(), 60);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(Main.playerList.contains(player.getName()))
            Main.playerList.remove(player.getName());
        if(Main.replyMap.containsKey(player.getName()))
            Main.replyMap.remove(player.getName());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for(String server : Main.config.getConfig().getConfigurationSection("servers").getKeys(false)){
                    try {
                        String host = Main.config.getConfig().getString("servers." + server + ".host");
                        int port = Main.config.getConfig().getInt("servers." + server + ".port");
                        new ShareClient(host, port, "quit " + player.getName()).startClient();
                    }catch (Exception e){}
                }
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
}
