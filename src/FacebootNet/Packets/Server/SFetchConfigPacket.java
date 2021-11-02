/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets.Server;

import FacebootNet.Packets.Client.*;
import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;

/**
 * This packet is created from client to server for handshaking purposes.
 * @author Ivy
 */
public class SFetchConfigPacket extends AbstractPacket {
    
    public int ErrorCode;
    public int UserId;
    public String GoogleAccount;
    public String FacebookAccount;
    public String TwitterAccount;
    public String UserName;
    public String UserPhone;
    public String UserEmail;
    public String UserGender;
    
    public SFetchConfigPacket(int requestIdx){
        super(Opcodes.FetchConfig, requestIdx);
        ErrorCode = 0;
        UserId = 0;
        GoogleAccount = "";
        FacebookAccount = "";
        TwitterAccount = "";
        UserName = "";
        UserPhone = "";
        UserEmail = "";
        UserGender = "";
    }
    
    public static SFetchConfigPacket Deserialize(byte[] data) throws Exception {
        SFetchConfigPacket p = new SFetchConfigPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ErrorCode = b.ReadInt();
        p.UserId = b.ReadInt();
        p.GoogleAccount = b.ReadString();
        p.FacebookAccount = b.ReadString();
        p.TwitterAccount = b.ReadString();
        p.UserName = b.ReadString();
        p.UserPhone = b.ReadString();
        p.UserEmail = b.ReadString();
        p.UserGender = b.ReadString();
        
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ErrorCode)
                .WriteInt(UserId)
                .WriteString(GoogleAccount)
                .WriteString(FacebookAccount)
                .WriteString(TwitterAccount)
                .WriteString(UserName)
                .WriteString(UserPhone)
                .WriteString(UserEmail)     
                .WriteString(UserGender)     
                .Serialize();
    }
    
}
