/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package FacebootNet;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Ivy
 * @param <T>
 */
public abstract class FacebootNetCallback<T> implements Callable {
    
    private T Request;
    private final Lock lock = new ReentrantLock(true);
    
    public T GetRequest(){
        return Request;
    }
    
    /**
     * Attempts to execute the given callback.
     * Note: If the callback is already in execution, the thread will be 
     * locked until the previous callback finishes.
     * @param Request
     * @throws Exception 
     */
    public void Execute(T Request) throws Exception {
        // Lock the current callback so Request variable will NEVER be changed.
        lock.lock();
        try{
            this.Request = Request;
            this.call();
        } catch(Exception e){
            throw e;
        } finally {
            // Unlock the current callback
            lock.unlock();
        }
    }
    
}
