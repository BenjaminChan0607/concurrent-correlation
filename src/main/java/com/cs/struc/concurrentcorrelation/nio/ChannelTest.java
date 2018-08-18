package com.cs.struc.concurrentcorrelation.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author benjaminChan
 * @date 2018/8/18 0018 上午 10:04
 */
public class ChannelTest {

    public static void main(String[] args) {
        ChannelTest channelTest = new ChannelTest();
        try {
//            channelTest.testChannel();
//            channelTest.testBuffer();
//            channelTest.transferFrom();
            channelTest.transferTo();
//            channelTest.transferTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transferTo() throws IOException{
        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        long transferFrom = fromChannel.transferTo(position, count, toChannel);
    }

    private void transferFrom() throws IOException {
        /*RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel      fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel      toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(position, count, fromChannel);*/

        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();
        long transferFrom = toChannel.transferFrom(fromChannel, position, count);
    }

    private void testBuffer() throws IOException {
/*        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        //create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf); //read into buffer.
        while (bytesRead != -1) {

            buf.flip();  //make buffer ready for read

            while(buf.hasRemaining()){
                System.out.print((char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
        }
        aFile.close();*/

        RandomAccessFile raf = new RandomAccessFile("c:/data/nio-data.txt", "rw");
        FileChannel fileChannel = raf.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        int readBytes = fileChannel.read(byteBuffer);
        while (readBytes != -1) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.println(byteBuffer.get());
            }
            byteBuffer.clear();
            readBytes = fileChannel.read(byteBuffer);
        }
        raf.close();
    }

    private void testChannel() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("c:/data/nio-data.txt", "rw");
        FileChannel fileChannel = raf.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);

        int readBytes = fileChannel.read(byteBuffer);
        while (readBytes != -1) {
            System.out.println("Read " + readBytes);
            byteBuffer.flip();//首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据

            while (byteBuffer.hasRemaining()) {
                System.out.println(byteBuffer.get());
            }

            byteBuffer.clear();
            readBytes = fileChannel.read(byteBuffer);
        }

        raf.close();
/*        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {

            System.out.println("Read " + bytesRead);
            buf.flip();

            while(buf.hasRemaining()){
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();*/
    }
}
