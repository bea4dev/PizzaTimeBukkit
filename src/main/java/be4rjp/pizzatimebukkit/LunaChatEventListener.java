package be4rjp.pizzatimebukkit;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.bukkit.event.LunaChatBukkitPreChatEvent;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.util.ClickableFormat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LunaChatEventListener implements Listener {
    @EventHandler
    public void onChat(LunaChatBukkitPreChatEvent event){
        Player sender = null;
        LunaChatAPI api = LunaChat.getAPI();
        
        for(Player player : Main.getPlugin().getServer().getOnlinePlayers()){
            if(player.getName().equals(event.getMember().getName())){
                sender = player;
            }
        }
        if(sender != null){
        }
    }
}
