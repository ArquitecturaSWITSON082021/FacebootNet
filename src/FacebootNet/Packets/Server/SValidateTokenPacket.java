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
public class SValidateTokenPacket extends AbstractPacket {
    
    public int ErrorCode;
    public int UserId;
    public long TokenVigency;
    
    public SValidateTokenPacket(int requestIdx){
        super(Opcodes.ValidateToken, requestIdx);
        ErrorCode = 0;
        UserId = 0;
        TokenVigency = 0;
    }
    
    public static SValidateTokenPacket Deserialize(byte[] data) throws Exception {
        SValidateTokenPacket p = new SValidateTokenPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ErrorCode = b.ReadInt();
        p.UserId = b.ReadInt();
        p.TokenVigency = b.ReadLong();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ErrorCode)
                .WriteInt(UserId)
                .WriteLong(TokenVigency)
                .Serialize();
    }
    
}
