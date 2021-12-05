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
public class SAttemptOauthPacket extends AbstractPacket {
    
    public int OauthType;
    public String OauthUrl;
    
    public SAttemptOauthPacket(int requestIdx){
        super(Opcodes.AttemptOauth, requestIdx);
        OauthType = 0;
        OauthUrl = "";
    }
    
    public static SAttemptOauthPacket Deserialize(byte[] data) throws Exception {
        SAttemptOauthPacket p = new SAttemptOauthPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.OauthType = b.ReadInt();
        p.OauthUrl = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(OauthType)
                .WriteString(OauthUrl)
                .Serialize();
    }
    
}
