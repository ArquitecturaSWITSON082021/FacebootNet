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
public class CAttemptOauthPacket extends AbstractPacket {
    
    public int OauthType;
    
    public CAttemptOauthPacket(int requestIdx){
        super(Opcodes.AttemptOauth, requestIdx);
    }
    
    public static CAttemptOauthPacket Deserialize(byte[] data) throws Exception{
        CAttemptOauthPacket p = new CAttemptOauthPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.OauthType = b.ReadInt();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(OauthType)
                .Serialize();
    }
    
}
