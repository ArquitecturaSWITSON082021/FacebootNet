/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets;

/**
 *
 * @author Ivy
 */
public class HelloPacketNfy {
    
    private Packet p;
    private int requestIdx;
    public int applicationVersion;
    
    public HelloPacketNfy(int requestIdx){
        this.requestIdx = requestIdx;
    }
    
    public byte[] serialize(){
        p = new Packet(Opcodes.HelloNfy, requestIdx);
        p.writeInt(applicationVersion);
        return p.serialize();
    }
    
}
