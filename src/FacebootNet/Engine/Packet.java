/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Engine;

import FacebootNet.Constants;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author Ivy
 */
public class Packet {
    
    protected ByteBuffer b;
    private int idx;
    private int opcode;
    
    // Default packet constructor, allocates the temporary byte buffer.
    public Packet(){
        b = ByteBuffer.allocate(Constants.packetLength);
        idx = 0;
        opcode = 0;
    }
    
    // Creates a new Packet instance. Requires the opcode value and request index.
    public Packet(int opcode, int requestIdx){
        this();
        WriteShort(Constants.packetHeader); // packet header
        WriteInt(0); // packet size
        WriteInt(requestIdx); // packet request index
        WriteShort((short) opcode); // packet opcode
        this.opcode = opcode;
    }
    
    // Attempts to deserialize a given byte array packet and returns a Packet object
    // as a result. If fails, an Exception may be thrown.
    public static Packet From(byte[] data) throws Exception{
        Packet p = new Packet();
        p.Write(data);
        p.Reset();
        
        short header = p.ReadShort();
        if (header != Constants.packetHeader){
            throw new Exception("Unexpected header. Got: " + header + ", expecting: " +Constants.packetHeader);
        }
        
        int size = p.ReadInt();
        if (size <= 0 || size >= Constants.packetLength){
            throw new Exception("Invalid packet size. Got: " + size);
        }
        
        short opcode = p.ReadShort();
        p.opcode = opcode;
        return p;
    }
    
    // Returns the current packet opcode, if given.
    public int GetOpcode(){
        return opcode;
    }
    
    // Serializes the current packet buffer and returns a crafted byte array with the result.
    public byte[] Serialize(){
        int len = idx - 2 - 4; // 2 header, 4 packet size, requestIdx 4 picket size
        b.putInt(2, len); // replace packet length
        return Arrays.copyOfRange(b.array(), 0, idx);
    }
    
    // Writes a string value into packet, based on the current index.
    public Packet WriteString(String v){
        WriteShort((short)v.length());
        byte[] data = v.getBytes();
        for(byte d : data)
            WriteByte(d);

        return this;
    }
    
    // Writes a double value into packet, based on the current index.
    public Packet WriteDouble(double v){
        b.putDouble(v);
        idx += 8;
        
        return this;
    }
    
    // Writes a float value into packet, based on the current index.
    public Packet WriteFloat(float v){
        b.putFloat(idx, v);
        idx += 4;
        return this;
    }
    
    // Writes an int value into packet, based on the current index.
    public Packet WriteInt(int v){
        b.putInt(idx, v);
        idx += 4;
        return this;
    }
    
    // Writes a short value into packet, based on the current index.
    public Packet WriteShort(short v){
        b.putShort(idx, v);
        idx += 2;
        return this;
    }
    
    // Writes a byte value into packet, based on the current index.
    public Packet WriteByte(byte v){
        b.put(idx, v);
        idx += 1;
        return this;
    }
    
    // Writes a boolean value into packet, based on the current index.
    public Packet WriteBoolean(boolean v){
        byte _v = (byte)(v == true ? 1 : 0);
        return WriteByte(_v);
    }
    
    // Writes a byte array into packet, based on the current index.
    public Packet Write(byte[] v){
        b.put(v);
        idx += v.length;
        return this;
    }
    
    // Reads an string value from packet given the current index.
    public String ReadString(){
        short len = ReadShort();
        String str = new String(ReadByteArray(len));
        return str;
    }
    
    // Reads an double value from packet given the current index.
    public double ReadDouble(){
        double v = b.getDouble(idx);
        idx += 8;
        return v;
    }
    
    // Reads an float value from packet given the current index.
    public float ReadFloat(){
        float v = b.getFloat(idx);
        idx += 4;
        return v;
    }
    
    // Reads an int value from packet given the current index.
    public int ReadInt(){
        int v = b.get(idx);
        idx += 4;
        return v;
    }
    
    // Reads a byte from packet given the current index.
    public short ReadShort(){
        short v = b.get(idx);
        idx += 2;
        return v;
    }
    
    // Reads a byte from packet given the current index.
    public byte ReadByte(){
        return b.get(idx++);
    }
    
    // Reads byte array from packet based on the given length.
    public byte[] ReadByteArray(int len){
        // copy byte stream to byte array
        byte[] data = new byte[Constants.packetLength];
        b.get(data);
        // create a new byte array which will hold the actual value
        byte[] result = new byte[len];
        System.arraycopy(data, idx, result, 0, len);
        idx += len;
        return result;
    }
    
    // Resets the packet byte index to zero.
    private void Reset(){
        idx = 0;
    }
    
    
}
