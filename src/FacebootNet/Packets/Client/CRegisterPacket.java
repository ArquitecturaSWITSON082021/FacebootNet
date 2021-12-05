/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets.Client;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Server.SOauthPacket;

/**
 * This packet is created from client to server for handshaking purposes.
 * @author Ivy
 */
public class CRegisterPacket extends AbstractPacket {
    
    public String UserName;
    public String LastName;
    public String Email;
    public String Password;
    public String Phone;
    public String Gender;
    public String BornDate;
    public SOauthPacket Oauth;
    
    public CRegisterPacket(int requestIdx){
        super(Opcodes.DoRegister, requestIdx);
    }
    
    public static CRegisterPacket Deserialize(byte[] data) throws Exception{
        CRegisterPacket p = new CRegisterPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.UserName = b.ReadString();
        p.LastName = b.ReadString();
        p.Email = b.ReadString();
        p.Password = b.ReadString();
        p.Phone = b.ReadString();
        p.Gender = b.ReadString();
        p.BornDate = b.ReadString();
        byte [] OauthBuf = b.ReadBuffer();
        p.Oauth = OauthBuf.length > 0 ? SOauthPacket.Deserialize(OauthBuf) : null;
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        byte[] OauthBuf = Oauth == null ? new byte[0] : Oauth.Serialize();
        return CraftPacket()
                .WriteString(UserName)
                .WriteString(LastName)
                .WriteString(Email)
                .WriteString(Password)
                .WriteString(Phone)
                .WriteString(Gender)
                .WriteString(BornDate)
                .WriteBuffer(OauthBuf)
                .Serialize();
    }
    
}
