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
public class CFetchConfigPacket extends AbstractPacket {
    
    
    public CFetchConfigPacket(int requestIdx){
        super(Opcodes.FetchConfig, requestIdx);
    }
    
    public static CFetchConfigPacket Deserialize(byte[] data) throws Exception{
        CFetchConfigPacket p = new CFetchConfigPacket(0);
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .Serialize();
    }
    
}
