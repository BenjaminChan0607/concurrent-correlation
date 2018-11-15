package com.cs.struc.concurrentcorrelation.bio;

import com.cs.struc.concurrentcorrelation.common.NettyConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author benjaminChan
 * @date 2018/9/28 0028 上午 11:30
 */
public class TimeServer {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(NettyConstants.PORT);
            System.out.println("The time server is start in port:" + NettyConstants.PORT);
            while (true) {
                Socket socket = serverSocket.accept();//interrupt
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
