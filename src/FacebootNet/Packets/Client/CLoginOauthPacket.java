/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets.Client;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;

/**
 * This packet is created from client to server for handshaking purposes.
 * @author Ivy
 */
public class CLoginOauthPacket extends AbstractPacket {
    
    public int OauthType;
    public String AccountId;
    
    public CLoginOauthPacket(int requestIdx){
        super(Opcodes.LoginOauth, requestIdx);
    }
    
    public static CLoginOauthPacket Deserialize(byte[] data) throws Exception{
        CLoginOauthPacket p = new CLoginOauthPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.OauthType = b.ReadInt();
        p.AccountId = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(OauthType)
                .WriteString(AccountId)
                .Serialize();
    }
    
}
