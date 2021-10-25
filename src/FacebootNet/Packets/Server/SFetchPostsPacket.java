/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets.Server;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;
import java.util.ArrayList;

/**
 * This packet is created from client to server for handshaking purposes.
 * @author Ivy
 */
public class SFetchPostsPacket extends AbstractPacket {
    
    protected ArrayList<EPostStruct> Posts;
    
    public SFetchPostsPacket(int requestIdx){
        super(Opcodes.FetchPosts, requestIdx);
        Posts = new ArrayList<>();
    }
    
    public void AddPost(EPostStruct Post){
        Posts.add(Post);
    }
    
    public ArrayList<EPostStruct> GetPosts(){
        return Posts;
    }
    
    public static SFetchPostsPacket Deserialize(byte[] data) throws Exception{
        SFetchPostsPacket p = new SFetchPostsPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        int totalPosts = b.ReadInt();
        
        if (totalPosts > 0)
            for(int i = 0; i < totalPosts; i++){
                EPostStruct post = EPostStruct.From(b);
                p.Posts.add(post);
            }
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception {
        PacketBuffer b = CraftPacket();
        
        b.WriteInt(Posts.size());
        for(EPostStruct post : Posts){
            b.Write(post.Serialize());
        }
        
        return b.Serialize();
    }
    
}
