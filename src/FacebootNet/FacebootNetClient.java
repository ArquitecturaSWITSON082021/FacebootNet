/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Packets.Client.CFetchPostsPacket;
import FacebootNet.Packets.Client.CLoginPacket;
import FacebootNet.Packets.Server.SHelloPacket;
import FacebootNet.Packets.Server.SLoginPacket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ivy
 */
public class FacebootNetClient {
    
    private AtomicInteger RequestIdx;
    private FacebootNetClientThread NetThread;
    
    public String Hostname;
    public int Port;
    
    public FacebootNetCallback<byte[]> OnMessage;
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
        this.RequestIdx = new AtomicInteger(0);
    }
    
    public int GetRequestIndex() {
        return RequestIdx.get();
    }
    
    public int GenerateRequestIndex() {
        return RequestIdx.addAndGet(1);
    }
    
    public void Start() throws Exception {
        if (NetThread.isAlive())
            throw new Exception("Cannot call FacebootNetClient.Start() if it is already running!");
        
        NetThread.run();
    }
    
    public void DoLogin(String email, String password){
        CLoginPacket request = new CLoginPacket(GenerateRequestIndex());
        request.Email = email;
        request.Password = password;
        NetThread.Send(request);
    }
    
    public void DoFetchPosts(int Offset){
        CFetchPostsPacket request = new CFetchPostsPacket(GenerateRequestIndex());
        request.Offset = Offset;
        NetThread.Send(request);
    }
    
    
}
