package com.cs.struc.concurrentcorrelation.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author benjaminChan
 * @date 2018/9/28 0028 上午 11:33
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String body;
            while (true) {
                body = in.readLine();//receive client send msg to server
                if (body == null) {
                    break;//没有接收到client的消息时，不做操作
                }
                System.out.println("The time server receive order:" + body);
                String currentTime = "The time server return timestamp:" + String.valueOf(System.currentTimeMillis());
                out.println(currentTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
