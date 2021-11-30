/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet;

import FacebootNet.Engine.PacketBuffer;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivy
 */
public class FacebootNetClientListenerThread extends Thread {
    
    private Socket socket;
    private boolean isRunning;
    private PacketBuffer packet;
    private int packetSize;
    private FacebootNetClientThread client;
    
    public FacebootNetClientListenerThread(FacebootNetClientThread c){
        client = c;
    }
    
    public void setSocket(Socket s){
        this.socket = s;
        this.packetSize = 0;
    }
    
    public boolean IsRunning(){
        return isRunning == true;
    }
    
    @Override
    public void run(){
        if (IsRunning())
            return;
        
        isRunning = true;
        BufferedInputStream instream = null;
        DataOutputStream outstream = null;
        try{
            instream = new BufferedInputStream(socket.getInputStream());
        }catch(Exception e){}
        byte[] buff = new byte[1024];
        while(isRunning){
            try {
                Arrays.fill(buff, (byte)0);
                int dwBytesRead = instream.read(buff, 0, buff.length); // instream.read(buff, 0, buff.length);
                if (dwBytesRead <= 0)
                    continue;
                
                packetSize += dwBytesRead;
                
                if (packet == null){
                    packet = new PacketBuffer(buff);
                }else{
                    packet.Write(buff, dwBytesRead);
                }
                
                if (packet != null && packetSize == packet.getSize()){
                        // execute packet
                        client.ProcessResponse(packet);
                        packet = null;
                        packetSize = 0;
                    }
                
                
                /*System.arraycopy(tempBuffer, 0, packetBuffer, currentOffset, dwBytesRead);
                currentOffset += dwBytesRead;
                System.out.println(packetBuffer); */
                
            } catch (Exception ex) {
                Logger.getLogger(FacebootNetClientListenerThread.class.getName()).log(Level.SEVERE, null, ex);
                this.kill();
            }
            
            
        }
    }
    
    public void kill(){
        try {
            if (!IsRunning())
                return;
            
            isRunning = false;
            Thread.sleep(1000L);
        } catch (InterruptedException ex) {
            Logger.getLogger(FacebootNetClientListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
