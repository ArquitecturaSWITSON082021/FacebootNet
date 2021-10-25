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
public class CLoginPacket extends AbstractPacket {
    
    public String Email;
    public String Password;
    
    public CLoginPacket(int requestIdx){
        super(Opcodes.Login, requestIdx);
    }
    
    public static CLoginPacket Deserialize(byte[] data) throws Exception{
        CLoginPacket p = new CLoginPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.Email = b.ReadString();
        p.Password = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteString(Email)
                .WriteString(Password)
                .Serialize();
    }
    
}
