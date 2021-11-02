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
 * This packet is created from client to server for handshaking purposes.
 * @author Ivy
 */
public class SHandshakePacket extends AbstractPacket {
    
    public int ApplicationVersion;
    public boolean IsAuthServiceRunning;
    public boolean IsPostServiceRunning;
    public boolean IsChatMessageRunning;
    
    public SHandshakePacket(int requestIdx){
        super(Opcodes.Hello, requestIdx);
    }
    
    public static SHandshakePacket Deserialize(byte[] data) throws Exception{
        SHandshakePacket p = new SHandshakePacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ApplicationVersion = b.ReadInt();
        p.IsAuthServiceRunning = b.ReadByte() == 1;
        p.IsPostServiceRunning = b.ReadByte() == 1;
        p.IsChatMessageRunning = b.ReadByte() == 1;
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ApplicationVersion)
                .WriteBoolean(IsAuthServiceRunning)
                .WriteBoolean(IsPostServiceRunning)
                .WriteBoolean(IsChatMessageRunning)
                .Serialize();
    }
    
}
