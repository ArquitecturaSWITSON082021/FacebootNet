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
public class PacketBuffer {
    
    protected ByteBuffer b;
    private int size;
    private int requestIdx;
    private int idx;
    private int opcode;
    
    // Default packet constructor, allocates the temporary byte buffer.
    public PacketBuffer(){
        b = ByteBuffer.allocate(Constants.PacketLength);
        idx = 0;
        opcode = 0;
        size = -1;
        requestIdx = 0;
    }
    
    public PacketBuffer(byte[] data) throws Exception{
        b = ByteBuffer.wrap(data);
        idx = 0;
        opcode = 0;
        
        short header = ReadShort(); // packet header
        if (header == Constants.PacketHeader){
            size = ReadInt(); // packet size
            requestIdx = ReadInt(); // packet request idx
            opcode = ReadShort(); // packet opcode
            
            if (size <= 0 || size >= Constants.PacketLength)
            throw new Exception("Invalid packet size. Got: " + size);
            
            // sum size with header
            size += 2 + 4; // + 4 + 2;
        }else{
            throw new Exception("Invalid packet header. Expecting: " + Constants.PacketHeader + ", expecting: " + header);
        }
    }
    
    // Creates a new Packet instance. Requires the opcode value and request index.
    public PacketBuffer(int opcode, int requestIdx) throws Exception{
        this();
        this.requestIdx = requestIdx;
        WriteShort(Constants.PacketHeader); // packet header
        WriteInt(0); // packet size
        WriteInt(requestIdx); // packet request index
        WriteShort((short) opcode); // packet opcode
        this.opcode = opcode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRequestIdx() {
        return requestIdx;
    }

    public int getIdx() {
        return idx;
    }

    // Attempts to deserialize a given byte array packet and returns a Packet object
    // as a result. If fails, an Exception may be thrown.
    public static PacketBuffer From(byte[] data) throws Exception{
        return new PacketBuffer(data);
    }
    
    // Returns the current packet opcode, if given.
    public int GetOpcode(){
        return opcode;
    }
    
    // Serializes the current packet buffer and returns a crafted byte array with the result.
    public byte[] Serialize(){
        int packetSize = size != -1 ? size : idx;
        // If no opcode was providen, it means that buffer is not a packet itself.
        if (opcode == 0)
            return Arrays.copyOfRange(b.array(), 0, idx);
        
        // Craft packet length if size is not providen
        if (size == -1){
            int len = idx - 2 - 4; // 2 header, 4 packet size, requestIdx 4 picket size
            b.putInt(2, len); // replace packet length
        }
        
        return Arrays.copyOfRange(b.array(), 0, packetSize);
    }
    
    // Writes a long value into packet, based on the current index.
    public PacketBuffer WriteLong(long v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        b.putLong(v);
        idx += 8;
        return this;
    }
    
    // Writes a string value into packet, based on the current index.
    public PacketBuffer WriteString(String v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        byte[] data = v.getBytes();
        WriteShort((short)data.length);
        if (data.length >= 0){
            for(byte d : data)
                WriteByte(d);
        }

        return this;
    }
    
    // Writes a byte array buffer value into packet, based on the current index.
    public PacketBuffer WriteBuffer(byte[] data) throws Exception{ 
        WriteShort((short)data.length);
        if (data.length >= 0){
            for(byte d : data)
                WriteByte(d);
        }

        return this;
    }
    
    // Writes a double value into packet, based on the current index.
    public PacketBuffer WriteDouble(double v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        b.putDouble(v);
        idx += 8;
        
        return this;
    }
    
    // Writes a float value into packet, based on the current index.
    public PacketBuffer WriteFloat(float v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        b.putFloat(idx, v);
        idx += 4;
        return this;
    }
    
    // Writes an int value into packet, based on the current index.
    public PacketBuffer WriteInt(int v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        b.putInt(idx, v);
        idx += 4;
        return this;
    }
    
    // Writes a short value into packet, based on the current index.
    public PacketBuffer WriteShort(short v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        b.putShort(idx, v);
        idx += 2;
        return this;
    }
    
    // Writes a byte value into packet, based on the current index.
    public PacketBuffer WriteByte(byte v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        b.put(idx, v);
        idx += 1;
        return this;
    }
    
    // Writes a boolean value into packet, based on the current index.
    public PacketBuffer WriteBoolean(boolean v) throws Exception{
        if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided.");
        
        byte _v = (byte)(v == true ? 1 : 0);
        return WriteByte(_v);
    }
    
    // Writes a byte array into packet, based on the current index.
    public PacketBuffer Write(byte[] v, int len){
        /* if (size != -1)
            throw new Exception("Cannot attempt to Write on packet if size is provided."); */
        
        for(int i = 0; i < len; i++){
            b.put(idx, v[i]);
            idx++;
        }
        return this;
    }
    
    // Reads an long value from packet given the current index.
    public long ReadLong(){
        long v = b.getLong(idx);
        idx += 8;
        return v;
    }
    
    // Reads an string value from packet given the current index.
    public String ReadString(){
        short len = ReadShort();
        String str = new String(ReadByteArray(len));
        return str;
    }
    
    // Reads an string value from packet given the current index.
    public byte[] ReadBuffer(){
        short len = ReadShort();
        return ReadByteArray(len);
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
        int v = b.getInt(idx);
        idx += 4;
        return v;
    }
    
    // Reads a byte from packet given the current index.
    public short ReadShort(){
        short v = b.getShort(idx);
        idx += 2;
        return v;
    }
    
    // Reads a byte from packet given the current index.
    public byte ReadByte(){
        return b.get(idx++);
    }
    
    // Reads byte array from packet based on the given length.
    public byte[] ReadByteArray(int len){
        // create a new byte array which will hold the actual value
        if (len <= 0)
            return new byte[]{};
        
        byte[] result = new byte[len];
        System.arraycopy(b.array(), idx, result, 0, len);
        idx += len;
        return result;
    }
    
    // Resets the packet byte index to zero.
    private void Reset(){
        idx = 0;
        b.rewind();
    }
    
    
}
