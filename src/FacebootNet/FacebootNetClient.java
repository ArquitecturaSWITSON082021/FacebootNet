/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Packets.Server.SHelloPacket;

/**
 *
 * @author Ivy
 */
public class FacebootNetClient {
    
    private FacebootNetClientThread NetThread;
    
    public String Hostname;
    public int Port;
    
    public FacebootNetCallback<AbstractPacket> OnMessage;
    public FacebootNetCallback<SHelloPacket> OnHelloMessage;
    public FacebootNetCallback<AbstractPacket> OnNewPostMessage;
    public FacebootNetCallback<AbstractPacket> OnNotificationMessage;
    public FacebootNetCallback<AbstractPacket> OnChatMessage;
    public FacebootNetCallback<AbstractPacket> OnErrorMessage;
    
    public FacebootNetClient(String Host, int Port){
        NetThread = new FacebootNetClientThread(this);
        OnMessage = null;
        OnHelloMessage = null;
        OnNewPostMessage = null;
        OnNotificationMessage = null;
        OnChatMessage = null;
        OnErrorMessage = null;
        this.Hostname = Hostname;
        this.Port = Port;
    }
    
    public void Start() throws Exception {
        if (NetThread.isAlive())
            throw new Exception("Cannot call FacebootNetClient.Start() if it is already running!");
        
        NetThread.run();
    }
    
    
}
