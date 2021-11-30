/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet.Packets.Server;

import FacebootNet.Engine.PacketBuffer;

/**
 *
 * @author Ivy
 */
public class EPostStruct {

    public int PostId;
    public long PostTime;
    public int UserId;
    public String UserName;
    public String PostBody;
    public int TotalReactions;
    public int TotalLikes;
    public int TotalComments;
    public boolean HasAttachment;

    public static EPostStruct From(PacketBuffer packet) throws Exception {
        EPostStruct post = new EPostStruct();
        post.PostId = packet.ReadInt();
        post.PostTime = packet.ReadLong();
        post.UserId = packet.ReadInt();
        post.UserName = packet.ReadString();
        post.PostBody = packet.ReadString();
        post.TotalReactions = packet.ReadInt();
        post.TotalLikes = packet.ReadInt();
        post.TotalComments = packet.ReadInt();
        post.HasAttachment = packet.ReadByte() == 1;
        return post;
    }

    public byte[] Serialize() throws Exception {
        PacketBuffer b = new PacketBuffer();
        return b.WriteInt(PostId)
                .WriteLong(PostTime)
                .WriteInt(UserId)
                .WriteString(UserName)
                .WriteString(PostBody)
                .WriteInt(TotalReactions)
                .WriteInt(TotalLikes)
                .WriteInt(TotalComments)
                .WriteByte((byte) (HasAttachment == true ? 1 : 0))
                .Serialize();
    }

}
