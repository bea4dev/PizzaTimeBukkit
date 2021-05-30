package be4rjp.pizzatimebukkit;

import be4rjp.pizzatimebukkit.commands.ptCommandExecutor;
import be4rjp.pizzatimebukkit.protocollib.PTPacketListener;
import be4rjp.pizzatimebukkit.share.ShareServer;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin {
    
    private static Main plugin;
    public static Config config;
    
    public static List<String> playerList = new ArrayList<>();
    
    public static Map<String, String> replyMap = new HashMap<>();
    
    //for ProtocolLib
    public static ProtocolManager protocolManager;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        
        //---------------------------Load config-----------------------------
        getLogger().info("Loading config files...");
        config = new Config(this, "config.yml");
        config.saveDefaultConfig();
        config.getConfig();
        //-------------------------------------------------------------------
        
        
        //----------------------------APICheck-------------------------------
        //LunaChat
        if(config.getConfig().getBoolean("hijack-tell-commands")) {
            if (!Bukkit.getPluginManager().isPluginEnabled("LunaChat")) {
                getLogger().severe("*** LunaChat is not installed or not enabled. ***");
                return;
            }
        }
        
        //ProtocolLib
        if(config.getConfig().getBoolean("hijack-tell-commands")) {
            if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                getLogger().severe("*** ProtocolLib is not installed or not enabled. ***");
                return;
            } else {
                protocolManager = ProtocolLibrary.getProtocolManager();
                new PTPacketListener();
            }
        }
        //-------------------------------------------------------------------
    
        
        //------------------------RegisteringEvents--------------------------
        getLogger().info("Registering events...");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EventListener(), this);
        if(config.getConfig().getBoolean("hijack-tell-commands"))
            pm.registerEvents(new LunaChatEventListener(), this);
        //-------------------------------------------------------------------
        
        
        //------------------------RegisteringCommands------------------------
        getLogger().info("Registering commands...");
        getCommand("pt").setExecutor(new ptCommandExecutor());
        getCommand("pt").setTabCompleter(new ptCommandExecutor());
        //-------------------------------------------------------------------
        
        
        //-------------------Chat share server and client--------------------
        ShareServer server = new ShareServer(config.getConfig().getInt("receive-port"));
        server.setDaemon(true);
        server.start();
        //-------------------------------------------------------------------
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    
    public static Main getPlugin(){return plugin;}
    
}
