package be4rjp.pizzatimebukkit.share;

import be4rjp.pizzatimebukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ShareServer extends Thread{
    private ServerSocket sSocket = null;
    
    private final int port;
    
    public ShareServer(int port){
        this.port = port;
    }
    
    public void run(){
        try{
            //ソケットを作成
            sSocket = new ServerSocket(port);
            
            //クライアントからの要求待ち
            while (true) {
                Socket socket = sSocket.accept();
                new EchoThread(socket).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (sSocket!=null)
                    sSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


//非同期スレッド
class EchoThread extends Thread {
    
    private Socket socket;
    
    private String T = "true";
    private String F = "false";
    
    public EchoThread(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        try {
            //クライアントからの受取用
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            //サーバーからクライアントへの送信用
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            
            String command = reader.readLine();
            
            System.out.println(command);
            
            String args[] = command.split(" ");
            
            switch (args[0]) {
                case "join": {
                    if (args.length == 2) {
                        if(!Main.playerList.contains(args[1]))
                            Main.playerList.add(args[1]);
                        writer.println(T);
                    }
                    break;
                }
                case "quit": {
                    if (args.length == 2) {
                        boolean online = false;
                        try{
                            for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
                                if(player.getName().equals(args[1])){
                                    online = true;
                                    break;
                                }
                            }
                        }catch (Exception e){}
                        if(Main.playerList.contains(args[1]) && !online)
                            Main.playerList.remove(args[1]);
                        writer.println(T);
                    }
                    break;
                }
                case "chat": {
                    if (args.length >= 2) {
                        String chat = command.replace(args[0] + " ", "");
                        try {
                            for (Player player : Main.getPlugin().getServer().getOnlinePlayers())
                                player.sendMessage(chat);
                        }catch (Exception e){
                            writer.println(F);
                        }
                        writer.println(T);
                    }
                    break;
                }
                case "tell": {
                    if(args.length >= 4) {
                        String chat = command.replace(args[0] + " " + args[1] + " " + args[2] + " ", "");
                        try {
                            boolean success = false;
                            for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
                                if(player.getName().equals(args[2])){
                                    player.sendMessage("§7[" + args[1] + " §f-> " + args[2] + "] " + chat);
                                    Main.replyMap.put(args[2], args[1]);
                                    writer.println(T);
                                    success = true;
                                    break;
                                }
                            }
                            if(!success) writer.println(F);
                        } catch (Exception e) {
                            writer.println(F);
                        }
                    }
                    break;
                }
                default: {
                    writer.println(F);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {}
        }
    }
    
}
