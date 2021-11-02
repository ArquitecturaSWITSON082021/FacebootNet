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
import FacebootNet.Packets.Server.SFetchPostsPacket;
import FacebootNet.Packets.Server.SHandshakePacket;
import FacebootNet.Packets.Server.SLoginPacket;
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
    private Queue<AbstractPacket> RequestQueue;
    private Queue<AbstractPacket> ResponseQueue;
    
    // SERVER EMULATION: REMOVE THIS WHEN SERVER IS DONE
    private Queue<AbstractPacket> ServerQueue;
    // END SERVER EMULATION
    
    private FacebootNetClient Client;
    private boolean IsRunning;
    private long TotalTicks;
    
    /**
     * Creates a new FacebootNetThread.
     * Requires a client object.
     * @param Client 
     */
    public FacebootNetClientThread(FacebootNetClient Client) {
        this.Client = Client;
        
        // Instantiate our own thread-safe queues.
        this.RequestQueue = new ConcurrentLinkedQueue<AbstractPacket>();
        this.ResponseQueue = new ConcurrentLinkedQueue<AbstractPacket>();
        
        // SERVER EMULATION: REMOVE THIS WHEN SERVER IS DONE
        this.ServerQueue = new ConcurrentLinkedQueue<AbstractPacket>();
        // --
        
        this.TotalTicks = 0L;
    }
    
    /**
     * Attempts to send a given packet to server.
     * @param Packet
     * @param TimeoutMs 
     */
    public void Send(AbstractPacket Packet, int TimeoutMs){
        RequestQueue.add(Packet);
    }
    
    /**
     * Attempts to send a given packet to server.
     * @param Packet 
     */
    public void Send(AbstractPacket Packet){
        Send(Packet, FacebootNet.Constants.NetTimeoutMs);
    }

    /**
     * Attempts to connect with server, if possible.
     */
    private void AttemptConnection() {
        // ADD YOUR SOCKET CODE HERE
        // --
        CHandshakePacket packet = new CHandshakePacket(Client.GenerateRequestIndex());
        packet.ApplicationVersion = Constants.ApplicationVersion;
        RequestQueue.add(packet);
    }

    /**
     * Processes all client request queue.
     */
    private void ProcessRequestQueue() {
        while (true) {
            AbstractPacket packet = RequestQueue.poll();
            if (packet == null) {
                break;
            }
            // REMOVE THIS WHEN SERVER IS DONE
            ServerQueue.add(packet);
        }
    }

    /**
     * Processes all client response queue.
     */
    private void ProcessResponseQueue() throws Exception {
        while (true) {
            AbstractPacket packet = ResponseQueue.poll();
            if (packet == null) {
                break;
            }

            switch (packet.GetOpcode()) {
                case Opcodes.Hello:
                    if (Client.OnHelloMessage != null) {
                        Client.OnHelloMessage.Execute((SHandshakePacket) packet);
                    }
            }
            
            if (Client.OnMessage != null)
                Client.OnMessage.Execute(packet.Serialize());
        }
    }

    /**
     * -- REMOVE THIS WHEN SERVER IS DONE
     * Processes all server client requests
     * queue.
     */
    private void ProcessServerQueue() {
        while (true) {
            AbstractPacket packet = ServerQueue.poll();
            if (packet == null) {
                break;
            }

            switch (packet.GetOpcode()) {
                case Opcodes.Hello:
                    // craft a hello response!
                    SHandshakePacket hello = new SHandshakePacket(packet.GetRequestIndex());
                    hello.ApplicationVersion = Constants.ApplicationVersion;
                    hello.IsAuthServiceRunning = true;
                    hello.IsChatMessageRunning = true;
                    hello.IsPostServiceRunning = true;
                    ResponseQueue.add(hello);
                    break;
                case Opcodes.Login:
                    SLoginPacket login = new SLoginPacket(packet.GetRequestIndex());
                    login.TokenId = "DEVTOKEN";
                    login.ErrorCode = 0;
                    login.UserBornDate = "2000-01-01";
                    login.UserEmail = "test@gmail.com";
                    login.UserGender = "male";
                    login.UserName = "José Perez";
                    login.UserId = 1;
                    login.UserPhone = "0123456789";
                    ResponseQueue.add(login);
                    break;
                case Opcodes.FetchPosts:
                    SFetchPostsPacket posts = new SFetchPostsPacket(packet.GetRequestIndex());
                    EPostStruct post1 = new EPostStruct();
                    String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc quam dolor, suscipit malesuada suscipit id, rhoncus a nunc. Curabitur nec nunc eget odio vehicula cursus. Duis at accumsan purus. Sed odio risus, ultrices eget nunc at, varius auctor nisi. Morbi sed posuere ipsum, id tempor neque. Morbi pretium ex risus, sed imperdiet eros rhoncus a. Nulla facilisi. Fusce tincidunt tortor ut est aliquet, ac mattis libero pharetra. Integer quis faucibus turpis, sit amet tincidunt eros.";
                    post1.UserId = 1;
                    post1.UserName = "ITSON";
                    post1.PostBody = lorem;
                    post1.PostTime = new Date().getTime();
                    post1.TotalComments = 1;
                    post1.TotalLikes = 5;
                    post1.TotalReactions = 0;
                    posts.AddPost(post1);
                    
                    EPostStruct post2 = new EPostStruct();
                    post2.UserId = 2;
                    post2.UserName = "José Pérez";
                    post2.PostBody = "Prueba de publicación!!!";
                    post2.PostTime = new Date().getTime();
                    post2.TotalComments = 1;
                    post2.TotalLikes = 1;
                    post2.TotalReactions = 0;
                    posts.AddPost(post2);
                    ResponseQueue.add(posts);
                    break;
            }
        }
    }

    @Override
    public void run() {
        this.IsRunning = true;

        while (IsRunning) {
            try {
                // If is the first tick, attempt to connect
                if (TotalTicks == 0) {
                    AttemptConnection();
                }
                ProcessRequestQueue();
                
                // REMOVE THIS WHEN SERVER IS DONE
                ProcessServerQueue();
                // --
                
                ProcessResponseQueue();
                Thread.sleep(1);
                TotalTicks++;
            } catch (Exception e) {
                System.out.println("FacebootNetClientThread.run() exception:\n" + e.getMessage() + "\n" + e.getStackTrace());
            }
        }
    }

}
