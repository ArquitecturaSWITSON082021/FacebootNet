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
public class SDoCommentPacket extends AbstractPacket {
    
    public int ErrorCode;
    public int PostId;
    public int CommentId;
    public String CommentTime;
    
    public SDoCommentPacket(int requestIdx){
        super(Opcodes.DoComment, requestIdx);
        ErrorCode = 0;
        PostId = 0;
        CommentId = 0;
        CommentTime = "";
    }
    
    public static SDoCommentPacket Deserialize(byte[] data) throws Exception {
        SDoCommentPacket p = new SDoCommentPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.ErrorCode = b.ReadInt();
        p.PostId = b.ReadInt();
        p.CommentId = b.ReadInt();
        p.CommentTime = b.ReadString();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(ErrorCode)
                .WriteInt(PostId)
                .WriteInt(CommentId)
                .WriteString(CommentTime)
                .Serialize();
    }
    
}
