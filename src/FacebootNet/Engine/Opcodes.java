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
public class Opcodes {
    public static final int Hello = 1;
    public static final int Login = 2;
    public static final int FetchPosts = 3;
    public static final int ValidateToken = 4;
    public static final int DoPost = 5;
    public static final int DoComment = 6;
    public static final int DoRegister = 7;
    public static final int FetchConfig = 8;
    public static final int AttemptOauth = 9;
    public static final int RegisterOauth = 10;
    public static final int LoginOauth = 11;
    public static final int SocketError = 9000;
}
