package be4rjp.pizzatimebukkit.share;

import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ShareClient {
    private final String command;
    
    private final String host;
    private final int port;
    
    
    public ShareClient(String host, int port, String command) {
        this.host = host;
        this.port = port;
        this.command = command;
    }
    
    public boolean startClient(){
        Socket cSocket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
    
        try {
            //IPアドレスとポート番号を指定してクライアント側のソケットを作成
            cSocket = new Socket(host, port);
        
            //クライアント側からサーバへの送信用
            writer = new PrintWriter(cSocket.getOutputStream(), true);
        
            //サーバ側からの受取用
            reader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
        
            //送信用の文字を送信
            writer.println(command);
        
            //サーバ側からの受取の結果を表示
            String result = reader.readLine();
            if(result.equals("true"))
                return true;
            else
                return false;
            
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (cSocket != null) {
                    cSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
}
