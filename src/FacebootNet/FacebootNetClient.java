/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Packets.Client.CAttemptOauthPacket;
import FacebootNet.Packets.Client.CDoPostPacket;
import FacebootNet.Packets.Client.CFetchPostsPacket;
import FacebootNet.Packets.Client.CLoginPacket;
import FacebootNet.Packets.Client.CRegisterPacket;
import FacebootNet.Packets.Client.CValidateTokenPacket;
import FacebootNet.Packets.Client.CDoCommentPacket;
import FacebootNet.Packets.Client.CFetchConfigPacket;
import FacebootNet.Packets.Server.SHandshakePacket;
import FacebootNet.Packets.Server.SOauthPacket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FacebootNet client, a facade project for networks side.
 * @author Ivy
 */
public class FacebootNetClient {
    
    // Define an AtomicInteger, Thread-safe request index.
    private AtomicInteger RequestIdx;
    
    // Create our own networking thread.
    private FacebootNetClientThread NetThread;
    
    // Define the server endpoint.
    public String Hostname;
    public int Port;
    
    // Define the required callbacks.
    public FacebootNetCallback<byte[]> OnMessage;
    public FacebootNetCallback<SHandshakePacket> OnHelloMessage;
    public FacebootNetCallback<AbstractPacket> OnNewPostMessage;
    public FacebootNetCallback<AbstractPacket> OnNotificationMessage;
    public FacebootNetCallback<AbstractPacket> OnChatMessage;
    public FacebootNetCallback<AbstractPacket> OnErrorMessage;
    
    /**
     * FacebootNetClient constructor.
     * Receives Host and Port for the server endpoint.
     * @param Host
     * @param Port 
     */
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
    
    /**
     * Returns the current atomically request index.
     * @return 
     */
    public int GetRequestIndex() {
        return RequestIdx.get();
    }
    
    /**
     * Generates a new request index atomically.
     * @return 
     */
    public int GenerateRequestIndex() {
        return RequestIdx.addAndGet(1);
    }
    
    /**
     * Attempts to start the client. If it is running it may throw an exception.
     * @throws Exception 
     */
    public void Start() throws Exception {
        if (NetThread.isAlive())
            throw new Exception("Cannot call FacebootNetClient.Start() if it is already running!");
        
        NetThread.run();
    }
    
    /**
     * DoLogin() function.
     * Receives email and password, if succeeds, the OnMessage callback will be called.
     * @param email
     * @param password 
     */
    public void DoLogin(String email, String password){
        CLoginPacket request = new CLoginPacket(GenerateRequestIndex());
        request.Email = email;
        request.Password = password;
        NetThread.Send(request);
    }
    
    /**
     * DoRegister() user.
     * If succeeds, the OnMessage callback will get called.
     * @param UserName
     * @param Email
     * @param Password
     * @param Phone
     * @param Gender
     * @param BornDate 
     */
    public void DoRegister(String UserName, String LastName, String Email, String Password, String Phone, String Gender, String BornDate, SOauthPacket Oauth){
        CRegisterPacket request = new CRegisterPacket(GenerateRequestIndex());
        request.UserName = UserName;
        request.LastName = LastName;
        request.Email = Email;
        request.Password = Password;
        request.Phone = Phone;
        request.Gender = Gender;
        request.BornDate = BornDate;
        request.Oauth = Oauth;
        NetThread.Send(request);
    }
    
    /**
     * DoFetchPosts()
     * Attempts to fetch posts at certain offset, if possible.
     * @param Offset 
     */
    public void DoFetchPosts(int Offset){
        CFetchPostsPacket request = new CFetchPostsPacket(GenerateRequestIndex());
        request.Offset = Offset;
        NetThread.Send(request);
    }
    
    /**
     * DoValidateToken()
     * Attempts to validate a given token. If valid, the OnMessage
     * callback will get executed.
     * @param TokenId 
     */
    public void DoValidateToken(String TokenId){
        CValidateTokenPacket request = new CValidateTokenPacket(GenerateRequestIndex());
        request.TokenId = TokenId;
        NetThread.Send(request);
    }
    
    /**
     * DoPost()
     * Receives an string holding its text contents and
     * a byte array, holding the picture binary format... if given.
     * It may throw an exception if Contents length is higher than 250.
     * @param Contents
     * @param Picture 
     * @throws Exception
     */
    public void DoPost(String Contents, String Filename, byte[] Picture) throws Exception{
        CDoPostPacket request = new CDoPostPacket(GenerateRequestIndex());
        request.Contents = Contents;
        request.Filename = Filename;
        request.Picture = Picture;
        
        if (request.Contents.length() > 250)
            throw new Exception("Post contents cannot be higher than 250 chars.");
        NetThread.Send(request);
    }
    
    /**
     * DoComment()
     * Receives the PostId whose will be commented and an String holding the text contents of such.
     * It may throw an exception if Contents length is higher than 250.
     * @param PostId
     * @param Contents 
     * @throws Exception
     */
    public void DoComment(int PostId, String Contents) throws Exception{
        CDoCommentPacket request = new CDoCommentPacket(GenerateRequestIndex());
        request.PostId = PostId;
        request.Contents = Contents;
        if (request.Contents.length() > 250)
            throw new Exception("Comment contents cannot be higher than 250 chars.");
        NetThread.Send(request);
    }
    
    /**
     * DoFetchConfig()
     * Attempts to fetch the user config.
     * If succeeds, the OnMessage callback will get executed.
     */
    public void DoFetchConfig(){
        CFetchConfigPacket request = new CFetchConfigPacket(GenerateRequestIndex());
        NetThread.Send(request);
    }
    
    public void DoAttemptOauth(int oAuthType){
        CAttemptOauthPacket request = new CAttemptOauthPacket(GenerateRequestIndex());
        request.OauthType = oAuthType;
        NetThread.Send(request);
    }
    
    
}
