/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet.Packets.Server;

import FacebootNet.Engine.AbstractPacket;
import FacebootNet.Engine.Opcodes;
import FacebootNet.Engine.PacketBuffer;

/**
 *
 * @author Ivy
 */
public class SOauthPacket extends AbstractPacket {
    
    public int OauthType;
    public String Id;
    public String Name;
    public String Email;
    public String FirstName;
    public String LastName;
    public String Gender;
    public short BirthDay;
    public short BirthMonth;
    public short BirthYear;
    
    public SOauthPacket(int requestIdx){
        super(Opcodes.RegisterOauth, requestIdx);
        OauthType = 0;
        Id = "";
        Name = "";
        Email = "";
        FirstName = "";
        LastName = "";
        Gender = "";
        BirthDay = 0;
        BirthMonth = 0;
        BirthYear = 0;
    }
    
    public static SOauthPacket Deserialize(byte[] data) throws Exception {
        SOauthPacket p = new SOauthPacket(0);
        PacketBuffer b = PacketBuffer.From(data);
        p.OauthType = b.ReadInt();
        p.Id = b.ReadString();
        p.Name = b.ReadString();
        p.Email = b.ReadString();
        p.FirstName = b.ReadString();
        p.LastName = b.ReadString();
        p.Gender = b.ReadString();
        p.BirthDay = b.ReadShort();
        p.BirthMonth = b.ReadShort();
        p.BirthYear = b.ReadShort();
        return p;
    }
    
    @Override
    public byte[] Serialize() throws Exception{
        return CraftPacket()
                .WriteInt(OauthType)
                .WriteString(Id)
                .WriteString(Name)
                .WriteString(Email)
                .WriteString(FirstName)
                .WriteString(LastName)
                .WriteString(Gender)
                .WriteShort(BirthDay)
                .WriteShort(BirthMonth)
                .WriteShort(BirthYear)
                .Serialize();
    }
}
