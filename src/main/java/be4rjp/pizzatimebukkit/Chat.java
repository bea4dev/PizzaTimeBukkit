package be4rjp.pizzatimebukkit;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.LunaChatBukkit;
import com.github.ucchyocean.lc3.LunaChatConfig;
import com.github.ucchyocean.lc3.bukkit.BukkitNormalChatJapanizeTask;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.member.ChannelMemberBukkit;
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
        ChannelMember player =
                ChannelMember.getChannelMember(sender);
        
        // NGワード発言をマスク
        for ( Pattern pattern : config.getNgwordCompiled() ) {
            Matcher matcher = pattern.matcher(message);
            if ( matcher.find() ) {
                message = matcher.replaceAll(
                        Utility.getAstariskString(matcher.group(0).length()));
            }
        }
    
        // カラーコード置き換え
        // 置き換え設定になっていて、発言者がパーミッションを持っているなら、置き換えする
        if ( config.isEnableNormalChatColorCode() &&
                sender.hasPermission("lunachat.allowcc") ) {
            message = Utility.replaceColorCode(message);
        }
    
        // 一時的にJapanizeスキップ設定かどうかを確認する
        boolean skipJapanize = false;
        String marker = config.getNoneJapanizeMarker();
        if ( !marker.equals("") && message.startsWith(marker) ) {
            skipJapanize = true;
            message = message.substring(marker.length());
        }
    
        // 2byteコードを含む、または、半角カタカナのみなら、Japanize変換は行わない
        String kanaTemp = Utility.stripColorCode(message);
        if ( !skipJapanize &&
                ( kanaTemp.getBytes(StandardCharsets.UTF_8).length > kanaTemp.length() ||
                        kanaTemp.matches("[ \\uFF61-\\uFF9F]+") ) ) {
            skipJapanize = true;
        }
    
        // Japanize変換と、発言処理
        if ( !skipJapanize &&
                LunaChat.getAPI().isPlayerJapanize(player.getName()) &&
                config.getJapanizeType() != JapanizeType.NONE ) {
        
            String taskFormat = Utility.replaceColorCode(config.getJapanizeLine1Format());
        
            String japanized = api.japanize(
                    kanaTemp, config.getJapanizeType());
            if ( japanized != null ) {
                String temp = taskFormat.replace("%msg", message);
                message = temp.replace("%japanize", japanized);
            }
        }
    
        String format = "";
    
        // チャットフォーマット装飾の適用
        if ( config.isEnableNormalChatMessageFormat() ) {
            String f = config.getNormalChatMessageFormat();
            f = ClickableFormat.replaceForNormalChatFormat(
                    f, ChannelMember.getChannelMember(sender));
            format = f
                    .replace("%1$s", "%displayName")
                    .replace("%2$s", "%msg");
        
        }
    
        format = Utility.replaceColorCode(format);
    
        // 発言内容の設定
        return format.replace("%msg", message)
                .replace("%displayName", sender.getDisplayName());
    }
    
    
    public static String toJapanize(Player sender, String message){
        LunaChatConfig config = LunaChat.getConfig();
        LunaChatAPI api = LunaChat.getAPI();
        ChannelMember player =
                ChannelMember.getChannelMember(sender);
        
        // NGワード発言をマスク
        for ( Pattern pattern : config.getNgwordCompiled() ) {
            Matcher matcher = pattern.matcher(message);
            if ( matcher.find() ) {
                message = matcher.replaceAll(
                        Utility.getAstariskString(matcher.group(0).length()));
            }
        }
    
        // カラーコード置き換え
        // 置き換え設定になっていて、発言者がパーミッションを持っているなら、置き換えする
        if ( config.isEnableNormalChatColorCode() &&
                sender.hasPermission("lunachat.allowcc") ) {
            message = Utility.replaceColorCode(message);
        }
    
        // 一時的にJapanizeスキップ設定かどうかを確認する
        boolean skipJapanize = false;
        String marker = config.getNoneJapanizeMarker();
        if ( !marker.equals("") && message.startsWith(marker) ) {
            skipJapanize = true;
            message = message.substring(marker.length());
        }
    
        // 2byteコードを含む、または、半角カタカナのみなら、Japanize変換は行わない
        String kanaTemp = Utility.stripColorCode(message);
        if ( !skipJapanize &&
                ( kanaTemp.getBytes(StandardCharsets.UTF_8).length > kanaTemp.length() ||
                        kanaTemp.matches("[ \\uFF61-\\uFF9F]+") ) ) {
            skipJapanize = true;
        }
    
        // Japanize変換と、発言処理
        if ( !skipJapanize &&
                LunaChat.getAPI().isPlayerJapanize(player.getName()) &&
                config.getJapanizeType() != JapanizeType.NONE ) {
        
            String taskFormat = Utility.replaceColorCode("§f%msg §7%japanize");
        
            String japanized = api.japanize(
                    kanaTemp, config.getJapanizeType());
            if ( japanized != null ) {
                String temp = taskFormat.replace("%msg", message);
                message = temp.replace("%japanize", japanized);
            }
        }
        return message;
    }
}
