/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet.Engine;

/**
 *
 * @author Ivy
 */
public class ErrorCode {
    public static final int NoError = 0;
    public static final int SocketError = -1000;
    public static final int InvalidRequest = -1;
    public static final int InternalServerError = -2;
    public static final int InvalidCredentials = 1;
    public static final int UserIllegalAge = 2;
}
