package be4rjp.pizzatimebukkit.protocollib;

import be4rjp.pizzatimebukkit.Chat;
import be4rjp.pizzatimebukkit.Main;
import be4rjp.pizzatimebukkit.share.ShareClient;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.ucchyocean.lc3.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PTPacketListener {
    public PTPacketListener(){
        Main.protocolManager.addPacketListener(
        new PacketAdapter(Main.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {//チャットパケットを監視してLunaChatの/tellコマンドを乗っ取る
                final Player player = event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Client.CHAT) {
                    final PacketContainer packet = event.getPacket();
                    
                    final String message = packet.getStrings().readSafely(0);
                    
                    String args[] = message.split(" ");
                    
                    if(args.length == 0) return;
                    
                    String tellCommands[] = {"/tell", "/msg", "/m", "/t", "/w"};
                    if(Arrays.asList(tellCommands).contains(args[0])){
                        event.setCancelled(true);
                        if(args.length >= 3) {
    
                            String chat = Chat.toJapanize(player, message.replace(args[0] + " " + args[1] + " ", ""));
                            
                            if(player.getName().equals(args[1])){
                                player.sendMessage(Messages.errmsgCannotSendPMSelf());
                                return;
                            }
                            
                            for(Player op : Main.getPlugin().getServer().getOnlinePlayers()){
                                if(args[1].equals(op.getName())){
                                    String m = "§7[" + player.getName() + " §f-> " + args[1] + "] " + chat;
                                    player.sendMessage(m);
                                    op.sendMessage(m);
                                    Main.replyMap.put(args[1], player.getName());
                                    return;
                                }
                            }
                            
                            BukkitRunnable task = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    boolean success = false;
                                    for(String server : Main.config.getConfig().getConfigurationSection("servers").getKeys(false)){
                                        try {
                                            String host = Main.config.getConfig().getString("servers." + server + ".host");
                                            int port = Main.config.getConfig().getInt("servers." + server + ".port");
                                            boolean result = new ShareClient(host, port,
                                                    "tell " + player.getName() + " " + args[1] + " " + chat).startClient();
                                            if(result) success = true;
                                        }catch (Exception e){}
                                    }
                                    
                                    if(!success) {
                                        player.sendMessage("§c指定されたプレイヤーが見つからなかったか、間違った引数が指定された可能性があります");
                                        player.sendMessage(Messages.usageMessage(args[0].replace("/", "")));
                                    }
                                    else{
                                        Main.replyMap.put(args[1], player.getName());
                                        player.sendMessage("§7[" + player.getName() + " §f-> " + args[1] + "] " + chat);
                                    }
                                }
                            };
                            task.runTaskAsynchronously(Main.getPlugin());
                            return;
                        }
    
                        player.sendMessage(Messages.usageMessage(args[0].replace("/", "")));
                    }
                    
    
                    String replyCommands[] = {"/reply", "/r"};
                    if(Arrays.asList(replyCommands).contains(args[0])){
                        event.setCancelled(true);
                        if(args.length >= 2) {
                            if(!Main.replyMap.containsKey(player.getName())){
                                player.sendMessage(Messages.errmsgNotfoundPM());
                                return;
                            }
                            String receiver = Main.replyMap.get(player.getName());
                            String chat = Chat.toJapanize(player, message.replace(args[0] + " ", ""));
    
                            for(Player op : Main.getPlugin().getServer().getOnlinePlayers()){
                                if(receiver.equals(op.getName())){
                                    String m = "§7[" + player.getName() + " §f-> " + receiver + "] " + chat;
                                    player.sendMessage(m);
                                    op.sendMessage(m);
                                    Main.replyMap.put(receiver, player.getName());
                                    return;
                                }
                            }
                            
                            BukkitRunnable task = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    boolean success = false;
                                    for(String server : Main.config.getConfig().getConfigurationSection("servers").getKeys(false)){
                                        try {
                                            String host = Main.config.getConfig().getString("servers." + server + ".host");
                                            int port = Main.config.getConfig().getInt("servers." + server + ".port");
                                            boolean result = new ShareClient(host, port,
                                                    "tell " + player.getName() + " " + receiver + " " + chat).startClient();
                                            if(result) success = true;
                                        }catch (Exception e){}
                                    }
                    
                                    if(!success) {
                                        player.sendMessage("§c指定されたプレイヤーが見つからなかったか、間違った引数が指定された可能性があります");
                                        player.sendMessage(Messages.usageMessage(args[0].replace("/", "")));
                                    }
                                    else{
                                        Main.replyMap.put(receiver, player.getName());
                                        player.sendMessage("§7[" + player.getName() + " §f-> " + receiver + "] " + chat);
                                    }
                                }
                            };
                            task.runTaskAsynchronously(Main.getPlugin());
                            return;
                        }
        
                        player.sendMessage(Messages.usageMessage(args[0].replace("/", "")));
                    }
                }
            }
        });
    }
}
