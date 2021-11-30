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
public class CDoPostPacket extends AbstractPacket {
    
    public String Contents;
    public String Filename;
    public byte[] Picture;
    
    public CDoPostPacket(int requestIdx){
        super(Opcodes.DoPost, requestIdx);
    }
    
    public static CDoPostPacket Deserialize(byte[] data) throws Exception{
        CDoPostPacket p = new CDoPostPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.Contents = b.ReadString();
        p.Filename = b.ReadString();
        p.Picture = b.ReadBuffer();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteString(Contents)
                .WriteString(Filename)
                .WriteBuffer(Picture)
                .Serialize();
    }
    
}
