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
public class SRegisterPacket extends AbstractPacket {
    
    public int ErrorCode;
    public int UserId;
    
    public SRegisterPacket(int requestIdx){
        super(Opcodes.DoRegister, requestIdx);
        ErrorCode = 0;
        UserId = 0;
    }
    
    public static SRegisterPacket Deserialize(byte[] data) throws Exception {
        SRegisterPacket p = new SRegisterPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ErrorCode = b.ReadInt();
        p.UserId = b.ReadInt();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ErrorCode)
                .WriteInt(UserId)
                .Serialize();
    }
    
}
