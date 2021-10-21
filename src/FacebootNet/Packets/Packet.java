/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FacebootNet.Packets;

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
    
    public Packet(){
        b = ByteBuffer.allocate(Constants.packetLength);
        idx = 0;
        opcode = 0;
    }
    
    public Packet(int opcode, int requestIdx){
        this();
        writeShort(Constants.packetHeader); // packet header
        writeInt(0); // packet size
        writeInt(requestIdx); // packet request index
        writeShort((short) opcode); // packet opcode
        this.opcode = opcode;
    }
    
    public static Packet From(byte[] data) throws Exception{
        Packet p = new Packet();
        p.write(data);
        p.reset();
        
        short header = p.readShort();
        if (header != Constants.packetHeader){
            throw new Exception("Unexpected header. Got: " + header + ", expecting: " +Constants.packetHeader);
        }
        
        int size = p.readInt();
        if (size <= 0 || size >= Constants.packetLength){
            throw new Exception("Invalid packet size. Got: " + size);
        }
        
        short opcode = p.readShort();
        p.opcode = opcode;
        return p;
    }
    
    public int getOpcode(){
        return opcode;
    }
    
    public byte[] serialize(){
        int len = idx - 2 - 4; // 2 header, 4 packet size, requestIdx 4 picket size
        b.putInt(2, len); // replace packet length
        return Arrays.copyOfRange(b.array(), 0, idx);
    }
    
    public void writeString(String v){
        writeShort((short)v.length());
        byte[] data = v.getBytes();
        for(byte d : data)
            writeByte(d);
    }
    
    public void writeDouble(double v){
        b.putDouble(v);
        idx += 8;
    }
    
    public void writeFloat(float v){
        b.putFloat(idx, v);
        idx += 4;
    }
    
    public void writeInt(int v){
        b.putInt(idx, v);
        idx += 4;
    }
    
    public void writeShort(short v){
        b.putShort(idx, v);
        idx += 2;
    }
    
    public void writeByte(byte v){
        b.put(idx, v);
        idx += 1;
    }
    
    public void write(byte[] v){
        b.put(v);
        idx += v.length;
    }
    
    public String readString(){
        short len = readShort();
        String str = new String(readByteArray(len));
        return str;
    }
    
    public double readDouble(){
        double v = b.getDouble(idx);
        idx += 8;
        return v;
    }
    
    public float readFloat(){
        float v = b.getFloat(idx);
        idx += 4;
        return v;
    }
    
    public int readInt(){
        int v = b.get(idx);
        idx += 4;
        return v;
    }
    
    public short readShort(){
        short v = b.get(idx);
        idx += 2;
        return v;
    }
    
    public byte readByte(){
        return b.get(idx++);
    }
    
    public byte[] readByteArray(int len){
        // copy byte stream to byte array
        byte[] data = new byte[Constants.packetLength];
        b.get(data);
        // create a new byte array which will hold the actual value
        byte[] result = new byte[len];
        System.arraycopy(data, idx, result, 0, len);
        idx += len;
        return result;
    }
    
    private void reset(){
        idx = 0;
    }
    
    
}
