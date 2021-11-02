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
public class CValidateTokenPacket extends AbstractPacket {
    
    public String TokenId;
    
    public CValidateTokenPacket(int requestIdx){
        super(Opcodes.ValidateToken, requestIdx);
    }
    
    public static CValidateTokenPacket Deserialize(byte[] data) throws Exception{
        CValidateTokenPacket p = new CValidateTokenPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.TokenId = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteString(TokenId)
                .Serialize();
    }
    
}
