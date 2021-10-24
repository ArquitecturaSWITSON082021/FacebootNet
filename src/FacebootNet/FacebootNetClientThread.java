/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.Packet;
import FacebootNet.Packets.Client.CHelloPacket;
import FacebootNet.Packets.Server.SHelloPacket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ivy
 */
public class FacebootNetClientThread extends Thread {

    private Queue<AbstractPacket> RequestQueue;
    private Queue<AbstractPacket> ResponseQueue;
    // REMOVE THIS WHEN SERVER IS DONE
    private Queue<AbstractPacket> ServerQueue;
    private FacebootNetClient Client;
    private boolean IsRunning;
    private long TotalTicks;
    private AtomicInteger RequestIdx;

    public FacebootNetClientThread(FacebootNetClient Client) {
        this.Client = Client;
        this.RequestQueue = new ConcurrentLinkedQueue<AbstractPacket>();
        this.ResponseQueue = new ConcurrentLinkedQueue<AbstractPacket>();
        this.ServerQueue = new ConcurrentLinkedQueue<AbstractPacket>();
        this.TotalTicks = 0L;
        this.RequestIdx = new AtomicInteger(0);
    }

    private int GetRequestIndex() {
        return RequestIdx.addAndGet(1);
    }

    private void AttemptConnection() {
        // Crear el socket, etc.
        // ...
        CHelloPacket packet = new CHelloPacket(GetRequestIndex());
        packet.applicationVersion = Constants.applicationVersion;
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
                        Client.OnHelloMessage.Execute((SHelloPacket) packet);
                    }
            }
        }
    }

    /**
     * -- REMOVE THIS WHEN SERVER IS DONE Processes all server client requests
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
                    SHelloPacket resp = new SHelloPacket(packet.GetRequestIndex());
                    resp.ApplicationVersion = Constants.applicationVersion;
                    resp.IsAuthServiceRunning = true;
                    resp.IsChatMessageRunning = true;
                    resp.IsPostServiceRunning = true;
                    ResponseQueue.add(resp);
            }
        }
    }

    @Override
    public void run() {
        this.IsRunning = true;

        while (IsRunning) {
            try {
                // Si se acaba de correr el hilo, entonces crear el socket, etc...
                if (TotalTicks == 0) {
                    AttemptConnection();
                }
                // Loop principal de la biblioteca de red
                ProcessRequestQueue();
                // REMOVE THIS WHEN SERVER IS DONE
                ProcessServerQueue();
                ProcessResponseQueue();
                Thread.sleep(1);
                TotalTicks++;
            } catch (Exception e) {
                System.out.println("FacebootNetClientThread.run() exception:\n" + e.getMessage() + "\n" + e.getStackTrace());
            }
        }
    }

}
