package nia.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * @program: netty-test
 * @description:
 * @author: zzk
 * @create: 2020-09-25
 */
public class ByteBufExamples {

    private final static Random random = new Random();

    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;

    /**
     * 堆缓冲区 - 将数据存储在JVM的堆空间中 - 这种模式叫做支撑数组
     */
    public static void heapBuffer(){
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE;
        if(heapBuf.hasArray()){
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readableBytes();
            int length = heapBuf.readableBytes();
            handleArray(array, offset, length);
        }
    }

    /**
     * 直接缓冲区的数据
     */
    public static void directBuffer(){
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        if(!directBuf.hasArray()){
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            handleArray(array, 0, length);
        }
    }

    /**
     * 使用JDK的ByteBuffer来实现符合缓冲区模式
     * @param header
     * @param body
     */
    public static void byteBufferComposite(ByteBuffer header, ByteBuffer body){
        ByteBuffer[] message = new ByteBuffer[]{header, body};

        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }


    /**
     * 使用 CompositeByteBuf 的复合缓冲模式
     */
    public static void byteBufComposite(){
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;
        messageBuf.addComponents(headerBuf, bodyBuf);
        //....
        messageBuf.removeComponent(0);
        for(ByteBuf buf : messageBuf){
            System.out.println(buf.toString());
        }
    }

    /**
     * 访问CompositeByteBuf的数据 - 可能不支持其支撑数组 - 访问 CompositeByteBuf中的数据类似于（访问）直接缓冲区的模式
     */
    public static void byteBufCompositeArray(){
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes();
        byte[] array = new byte[length];
        compBuf.getBytes(compBuf.readerIndex(), array);
        handleArray(array, 0, array.length);
    }

    /**
     * 随机访问索引
     */
    public static void byteBufRelativeAccess(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        for(int i = 0; i < buffer.capacity(); i++){
            byte b = buffer.getByte(i);
            System.out.println((char)b);
        }
    }

    /**
     * 读取所有字节
     */
    public static void readAllData(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        while(buffer.isReadable()){
            System.out.println(buffer.readByte());
        }
    }

    /**
     * 写数据
     */
    public static void write(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        while(buffer.writableBytes() > 4){
            buffer.writeInt(random.nextInt());
        }
    }

    /**
     * 使用 ByteBufProcessor 来寻找\r
     */
    public static void byteProcessor(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        int index = buffer.forEachByte(ByteProcessor.FIND_CR);
    }

    /**
     * 对ByteBuf进行切片
     */
    public static void byteBufSlice(){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = buf.slice(0, 15);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte)'J');
        assert buf.getByte(0) == sliced.getByte(0);
    }

    /**
     * 复制一个ByteBuf
     */
    public static void byteBufCopy(){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString());
        buf.setByte(0, (byte)'J');
        assert buf.getByte(0) != copy.getByte(0);
    }

    /**
     * get() 和 set()
     */
    public static void byteBufSetGet(){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char) buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte)'B');
        System.out.println((char) buf.getByte(0));
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    public static void byteBufWriteRead(){
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char) '?');
        int readerIndex = buf.readerIndex();
        int writeIndex = buf.writerIndex();
        buf.writeByte((byte)'?');
        assert readerIndex == buf.readerIndex();
        assert writeIndex != buf.writerIndex();
    }

    private static void handleArray(byte[] array, int offset, int len){}

    /**
     * ByteBufAllocator引用获取
     */
    public static void obtainingByteBufAllocatorReference(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBufAllocator allocator = channel.alloc();
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        ByteBufAllocator allocator2 = ctx.alloc();
    }

    /**
     * 引用计数
     */
    public static void referenceCounting(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBufAllocator allocator = channel.alloc();

        ByteBuf buffer = allocator.directBuffer();
        assert buffer.refCnt() == 1;
    }

    /**
     * 释放引用计数的对象
     */
    public static void releaseRefernceCountedObject(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        boolean released = buffer.release();
    }
}
