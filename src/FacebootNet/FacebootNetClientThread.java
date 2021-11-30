/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Client.CHandshakePacket;
import FacebootNet.Packets.Client.CLoginPacket;
import FacebootNet.Packets.Server.EPostStruct;
import FacebootNet.Packets.Server.SConnectionErrorPacket;
import FacebootNet.Packets.Server.SFetchPostsPacket;
import FacebootNet.Packets.Server.SHandshakePacket;
import FacebootNet.Packets.Server.SLoginPacket;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ivy
 */
public class FacebootNetClientThread extends Thread {

    // Define TS queues
    public FacebootNetClient Client;
    private Queue<AbstractPacket> RequestQueue;
    private FacebootNetClientListenerThread listenerThread;
    
    
    private Socket socket;    
    private boolean IsRunning;
    private long TotalTicks;

    /**
     * Creates a new FacebootNetThread. Requires a client object.
     *
     * @param Client
     */
    public FacebootNetClientThread(FacebootNetClient Client) {
        this.Client = Client;

        // Instantiate our own thread-safe queues.
        this.RequestQueue = new ConcurrentLinkedQueue<AbstractPacket>();
        this.listenerThread = new FacebootNetClientListenerThread(this);        
        this.TotalTicks = 0L;
    }

    /**
     * Attempts to send a given packet to server.
     *
     * @param Packet
     * @param TimeoutMs
     */
    public void Send(AbstractPacket Packet, int TimeoutMs) {
        RequestQueue.add(Packet);
    }

    /**
     * Attempts to send a given packet to server.
     *
     * @param Packet
     */
    public void Send(AbstractPacket Packet) {
        Send(Packet, FacebootNet.Constants.NetTimeoutMs);
    }

    /**
     * Attempts to connect with server, if possible.
     */
    private boolean AttemptConnection() {
        String host = "127.0.0.1";
        int port = 4000;
        
        int totalAttempts = 3;
        for (int i = 1; i <= totalAttempts; i++) {
            System.out.printf("[*] FacebootNet Attempting connection on %s:%d...%d/%d\n", host, port, i, totalAttempts);
            try {
                socket = new Socket(host, port);
                Thread.sleep(200L);
                if (socket.isConnected())
                    break;

            } catch (Exception e) {
                System.out.printf("[-] FacebootNet failed to connect to server: %s\n", e.getMessage());
                socket = null;
            }
            
            try {
                Thread.sleep(300L);
            } catch (Exception e) {
            }
        }
        
        if (!IsConnected())
            return false;
        
        listenerThread.setSocket(socket);
        listenerThread.start();
        System.out.println("[+] Established connection to server successfully.\n");
        
        CHandshakePacket packet = new CHandshakePacket(Client.GenerateRequestIndex());
        packet.ApplicationVersion = Constants.ApplicationVersion;
        RequestQueue.add(packet);
        
        return true;
    }
    
    public boolean IsConnected() {
        return socket != null && socket.isConnected() == true;
    }

    /**
     * Processes all client request queue.
     */
    private void ProcessRequestQueue() {
        while (true) {
            try {
                if (!IsConnected()) {
                    continue;
                }
                DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
                AbstractPacket packet = RequestQueue.poll();
                if (packet == null) {
                    break;
                }
                byte[] data = packet.Serialize();
                int dwLen = data.length;
                // Calculate required frames for this given packet
                int totalFrames = Math.round(data.length / Constants.FrameLength) + 1;
                for(int i = 0; i < totalFrames; i++){
                    int startOffset = i * Constants.FrameLength;
                    int endOffset = (i + 1) * Constants.FrameLength;
                    if (endOffset > dwLen){
                        endOffset = dwLen;
                    }
                    byte[] frame = Arrays.copyOfRange(data, startOffset, endOffset);
                    if (frame.length <= 0)
                        break;
                    outstream.write(frame);
                }
                
            } catch (Exception e) {
                System.out.printf("[-] Failed to process request queue: %s\n", e.getMessage());
                e.printStackTrace();
            }
            
        }
    }

    /**
     * Processes all client response queue.
     */
    public void ProcessResponse(PacketBuffer packet) throws Exception {
            switch (packet.GetOpcode()) {
                case Opcodes.Hello:
                    if (Client.OnHelloMessage != null) {
                        Client.OnHelloMessage.Execute(SHandshakePacket.Deserialize(packet.Serialize()));
                    }
            }
            
            if (Client.OnMessage != null) {
                Client.OnMessage.Execute(packet.Serialize());
            }
    }
    
    @Override
    public void run() {
        this.IsRunning = true;
        
        while (IsRunning) {
            try {
                // If is the first tick, attempt to connect
                if (TotalTicks == 0) {
                    boolean result = AttemptConnection();
                    // If we could not perform a successful connection, notify to client
                    if (!result){
                        SConnectionErrorPacket err = new SConnectionErrorPacket(Client.GenerateRequestIndex());
                        err.ErrorCode = -1; // -1 = socket err
                        err.Message = "Failed to connect to Faceboot TCP server. Make sure the server is either running and the endpoint is the right one.";
                        if (Client.OnMessage != null)
                            Client.OnMessage.Execute(err.Serialize());
                    }
                }
                ProcessRequestQueue();
                Thread.sleep(1L);
                TotalTicks++;
            } catch (Exception e) {
                System.out.println("FacebootNetClientThread.run() exception:\n" + e.getMessage() + "\n" + e.getStackTrace());
            }
        }
    }
    
}
