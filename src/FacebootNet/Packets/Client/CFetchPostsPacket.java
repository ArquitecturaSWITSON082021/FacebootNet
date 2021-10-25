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
public class CFetchPostsPacket extends AbstractPacket {
    
    // Post offset, starts from zero.
    public int Offset;
    
    public CFetchPostsPacket(int requestIdx){
        super(Opcodes.FetchPosts, requestIdx);
    }
    
    public static CFetchPostsPacket Deserialize(byte[] data) throws Exception{
        CFetchPostsPacket p = new CFetchPostsPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.Offset = b.ReadInt();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(Offset)
                .Serialize();
    }
    
}
