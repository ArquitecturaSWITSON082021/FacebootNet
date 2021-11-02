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
public class SDoPostPacket extends AbstractPacket {
    
    public int ErrorCode;
    public int PostId;
    public String PostTime;
    
    public SDoPostPacket(int requestIdx){
        super(Opcodes.DoComment, requestIdx);
        ErrorCode = 0;
        PostId = 0;
        PostTime = "";
    }
    
    public static SDoPostPacket Deserialize(byte[] data) throws Exception {
        SDoPostPacket p = new SDoPostPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ErrorCode = b.ReadInt();
        p.PostId = b.ReadInt();
        p.PostTime = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ErrorCode)
                .WriteInt(PostId)
                .WriteString(PostTime)
                .Serialize();
    }
    
}
