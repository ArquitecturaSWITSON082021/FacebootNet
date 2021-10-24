/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets.Client;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;

/**
 * This packet is created from client to server for handshaking purposes.
 * @author Ivy
 */
public class CHelloPacket extends AbstractPacket {
    
    public int applicationVersion;
    
    public CHelloPacket(int requestIdx){
        super(Opcodes.Hello, requestIdx);
    }
    
    @Override
    public byte[] Serialize(){
        return CraftPacket()
                .WriteInt(applicationVersion)
                .Serialize();
    }
    
}
