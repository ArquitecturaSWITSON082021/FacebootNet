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
    
    public static String Format(int ErrorCode){
        switch (ErrorCode){
            case NoError:
                return "No error.";
            case SocketError:
                return "No se ha podido completar la conexión al servidor, SocketError.";
            case InvalidRequest:
                return "Se ha recibido una petición inválida.";
            case InternalServerError:
                return "Ha ocurrido un error interno en el servidor.";
            case InvalidCredentials:
                return "Las credenciales proporcionadas son inválidas.";
            case UserIllegalAge:
                return "La edad del usuario es inválida, debe ser mayor de 18 años para hacer uso de Faceboot.";
        }
        
        return String.format("Invalid error code given: %d.", ErrorCode);
    }
}
