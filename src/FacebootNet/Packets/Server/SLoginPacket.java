/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets.Server;

import FacebootNet.Packets.Client.*;
import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.ErrorCode;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;

/**
 * This packet is created from client to server for handshaking purposes.
 * @author Ivy
 */
public class SLoginPacket extends AbstractPacket {
    
    public int ErrorCode;
    public int UserId;
    public String UserName;
    public String UserPhone;
    public String UserBornDate;
    public String UserEmail;
    public String UserGender;
    public String TokenId;
    
    public SLoginPacket(int requestIdx){
        super(Opcodes.Login, requestIdx);
        ErrorCode = 0;
        UserId = 0;
        UserName = "";
        UserPhone = "";
        UserBornDate = "";
        UserEmail = "";
        UserGender = "";
        TokenId = "";
    }
    
    public static SLoginPacket Deserialize(byte[] data) throws Exception {
        SLoginPacket p = new SLoginPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ErrorCode = b.ReadInt();
        p.UserId = b.ReadInt();
        p.UserName = b.ReadString();
        p.UserPhone = b.ReadString();
        p.UserBornDate = b.ReadString();
        p.UserEmail = b.ReadString();
        p.UserGender = b.ReadString();
        p.TokenId = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ErrorCode)
                .WriteInt(UserId)
                .WriteString(UserName)
                .WriteString(UserPhone)
                .WriteString(UserBornDate)
                .WriteString(UserEmail)
                .WriteString(UserGender)
                .WriteString(TokenId)
                .Serialize();
    }
    
}
