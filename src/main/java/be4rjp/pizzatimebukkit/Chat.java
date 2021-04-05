package be4rjp.pizzatimebukkit;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.LunaChatConfig;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.util.ClickableFormat;
import com.github.ucchyocean.lc3.util.Utility;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
    public static String toLunaChat(Player sender, String message){
        LunaChatConfig config = LunaChat.getConfig();
        LunaChatAPI api = LunaChat.getAPI();
        ChannelMember player = ChannelMember.getChannelMember(sender);
        
        for(Pattern pattern : config.getNgwordCompiled()) {
            Matcher matcher = pattern.matcher(message);
            if (matcher.find())
                message = matcher.replaceAll(Utility.getAstariskString(matcher.group(0).length()));
        }
        
        if(config.isEnableNormalChatColorCode() && sender.hasPermission("lunachat.allowcc"))
            message = Utility.replaceColorCode(message);
        
        boolean skip = false;
        String marker = config.getNoneJapanizeMarker();
        if(!marker.equals("") && message.startsWith(marker)){
            skip = true;
            message = message.substring(marker.length());
        }
        
        String kanaTemp = Utility.stripColorCode(message);
        if(!skip && (kanaTemp.getBytes(StandardCharsets.UTF_8).length > kanaTemp.length() || kanaTemp.matches("[ \\uFF61-\\uFF9F]+")))
            skip = true;
        
        if(!skip && LunaChat.getAPI().isPlayerJapanize(player.getName()) && config.getJapanizeType() != JapanizeType.NONE){
            String taskFormat = Utility.replaceColorCode(config.getJapanizeLine1Format());
        
            String japanized = api.japanize(kanaTemp, config.getJapanizeType());
            if(japanized != null){
                String temp = taskFormat.replace("%msg", message);
                message = temp.replace("%japanize", japanized);
            }
        }
    
        String format = "";
        
        if(config.isEnableNormalChatMessageFormat()){
            String f = config.getNormalChatMessageFormat();
            f = ClickableFormat.replaceForNormalChatFormat(f, ChannelMember.getChannelMember(sender));
            format = f.replace("%1$s", "%displayName").replace("%2$s", "%msg");
        }
    
        format = Utility.replaceColorCode(format);
        
        return format.replace("%msg", message).replace("%displayName", sender.getDisplayName());
    }
    
    
    public static String toJapanize(Player sender, String message){
        LunaChatConfig config = LunaChat.getConfig();
        LunaChatAPI api = LunaChat.getAPI();
        ChannelMember player = ChannelMember.getChannelMember(sender);
        
        for(Pattern pattern : config.getNgwordCompiled()){
            Matcher matcher = pattern.matcher(message);
            if (matcher.find())
                message = matcher.replaceAll(Utility.getAstariskString(matcher.group(0).length()));
        }
        
        if(config.isEnableNormalChatColorCode() && sender.hasPermission("lunachat.allowcc"))
            message = Utility.replaceColorCode(message);
        
        boolean skip = false;
        String marker = config.getNoneJapanizeMarker();
        if(!marker.equals("") && message.startsWith(marker)){
            skip = true;
            message = message.substring(marker.length());
        }
        
        String kanaTemp = Utility.stripColorCode(message);
        if(!skip && (kanaTemp.getBytes(StandardCharsets.UTF_8).length > kanaTemp.length() || kanaTemp.matches("[ \\uFF61-\\uFF9F]+")))
            skip = true;
        
        if(!skip && LunaChat.getAPI().isPlayerJapanize(player.getName()) && config.getJapanizeType() != JapanizeType.NONE){
            String taskFormat = Utility.replaceColorCode("§f%msg §7%japanize");
        
            String japanized = api.japanize(kanaTemp, config.getJapanizeType());
            if(japanized != null){
                String temp = taskFormat.replace("%msg", message);
                message = temp.replace("%japanize", japanized);
            }
        }
        return message;
    }
}
