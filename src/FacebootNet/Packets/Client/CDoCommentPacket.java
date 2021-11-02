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
public class CDoCommentPacket extends AbstractPacket {
    
    public int PostId;
    public String Contents;
    
    public CDoCommentPacket(int requestIdx){
        super(Opcodes.DoComment, requestIdx);
    }
    
    public static CDoCommentPacket Deserialize(byte[] data) throws Exception{
        CDoCommentPacket p = new CDoCommentPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.PostId = b.ReadInt();
        p.Contents = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(PostId)
                .WriteString(Contents)
                .Serialize();
    }
    
}
