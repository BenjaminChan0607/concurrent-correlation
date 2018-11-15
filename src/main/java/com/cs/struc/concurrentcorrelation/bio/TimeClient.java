package com.cs.struc.concurrentcorrelation.bio;

import com.cs.struc.concurrentcorrelation.common.NettyConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author benjaminChan
 * @date 2018/9/28 0028 上午 11:40
 */
public class TimeClient {

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket(NettyConstants.HOST, NettyConstants.PORT);
            //Client和Server的in/out获取方式都相同
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Query time order");
            System.out.println("Send order to server succeed.");
            String resp = in.readLine();//receive msg from server
            System.out.println("Now is :" + resp);
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
