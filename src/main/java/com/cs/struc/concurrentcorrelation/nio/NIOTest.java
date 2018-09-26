package com.cs.struc.concurrentcorrelation.nio;

/**
 * @author benjaminChan
 * @date 2018/8/18 0018 上午 9:41
 *
 * Java NIO 是从jdk 1.4开始引入的api,可以替代标准的Java IO api
 *
 * NIO 核心api：Channels Buffers Selectors
 *
 * Channel -> Buffer
 * Buffer -> Channel
 * Channel : File/DataGram/Socket/ServerSocket
 * Buffer : Byte/Char/Int/LongBuffer
 *
 * Selector : 准予单个线程处理多个Channel,例如单个线程使用一个Selector处理三个Channel
 * Thread -> Selector -> (Channel,Channel,Channel)
 *
 * IO NIO的比较
 * IO : Stream Oriented , Blocking IO
 * NIO : Buffer Oriented , Non Blocking IO , Selectors
 *
 * 之前一个小型的npc是时候开始看了
 */
public class NIOTest {

}
