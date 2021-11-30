/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets.Server;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;

/**
 * This packet is created from client to client stating connection errors, this packet is not actually sent by server.
 * @author Ivy
 */
public class SConnectionErrorPacket extends AbstractPacket {
    
    public int ErrorCode;
    public String Message;
    
    public SConnectionErrorPacket(int requestIdx){
        super(Opcodes.SocketError, requestIdx);
    }
    
    public static SConnectionErrorPacket Deserialize(byte[] data) throws Exception{
        SConnectionErrorPacket p = new SConnectionErrorPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ErrorCode = b.ReadInt();
        p.Message = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ErrorCode)
                .WriteString(Message)
                .Serialize();
    }
    
}
