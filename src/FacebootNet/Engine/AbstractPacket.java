/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Engine;

/**
 *
 * @author Ivy
 */
public class AbstractPacket {
    
    // Holds the packet opcode.
    private int opcode;
    // Holds the server/client request index.
    private int requestIdx;
    
    public AbstractPacket(int opcode, int requestIdx){
        this.opcode = opcode;
        this.requestIdx = requestIdx;
    }
    
    // Returns a Packet instance with the given opcode and request index.
    protected Packet CraftPacket(){
        Packet packet = new Packet(opcode, requestIdx);
        return packet;
    }
    
    public int GetOpcode(){
        return opcode;
    }
    
    public int GetRequestIndex(){
        return requestIdx;
    }
    
    public byte[] Serialize() throws Exception{
        throw new Exception("Not implemented yet.");
    }
    
    public byte[] Deserialize() throws Exception{
        throw new Exception("Not implemented yet.");
    }
}
