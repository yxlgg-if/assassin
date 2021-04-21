/**
 * 
 */
package app.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;

import app.base.ConvertTools;
import app.model.EncryptMsgModel;
import app.model.MsgModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yxlgg-if
 * @date 2021年3月30日 上午12:57:03
 */
public class EncryptChannel extends SocketChannel<MsgModel, MsgModel>{
    
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ReentrantLock writeLock = new ReentrantLock();
    private ReentrantLock readLock = new ReentrantLock();
    
    @Setter
    @Getter
    private String autographsKey;
    
    @Setter
    @Getter
    private String aesBaseKey;
    
    @Setter
    @Getter
    private Charset charset = StandardCharsets.UTF_8;
    
    // 超时
    private Long timeoutMills = 5000L;

    /**
     * 
     */
    public EncryptChannel() {
        // TODO 自动生成的构造函数存根
    }

    @Override
    public MsgModel read() throws Exception {
        byte[] msgArray = readFromInputStream();
        String msgString = new String(msgArray, charset);
        // JSON字符串转换为JSON对象
//        try {
//            JSONObject msgJson = JSON.parseObject(msgString);
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println("msgArray:" + Arrays.toString(msgArray));
//            System.out.println("msgArrayLen:" + msgArray.length);
//            System.out.println("msgString:" + msgString);
//            e.printStackTrace();
//        }
        JSONObject msgJson = JSON.parseObject(msgString);
        // JSON对象转换成Java对象
        EncryptMsgModel encryptMsgModel = msgJson.toJavaObject(EncryptMsgModel.class);
        if (Math.abs(System.currentTimeMillis() - encryptMsgModel.getTimestamp()) > timeoutMills) {
            throw new IllegalStateException("消息超时");
        }
        
        // 检查签名模型
        boolean checkAutograph = encryptMsgModel.checkAutographMsg(autographsKey);
        if (!checkAutograph) {
            throw new IllegalStateException("签名错误");
        }
        encryptMsgModel.decryptMsg(aesBaseKey);
        
        return encryptMsgModel;
    }

    @Override
    public void write(MsgModel value) throws Exception {
        String msgString = null;
        EncryptMsgModel encryptMsgModel = new EncryptMsgModel(value);
        encryptMsgModel.setCharset(charset.name());
        encryptMsgModel.setEncryptValue(aesBaseKey, autographsKey);
        if (encryptMsgModel instanceof JSONAware) {
            msgString = ((JSONAware) encryptMsgModel).toJSONString();
        } else {
            msgString = JSON.toJSONString(encryptMsgModel);
        }
        // 字符串转换为比特数组
        byte[] msgByteArray = msgString.getBytes(charset);
        writeIntoOutputStream(msgByteArray);
    }

    @Override
    public void flush() throws Exception {
        writeLock.lock();
        try {
            this.getOutputStream().flush();
        } finally {
            writeLock.unlock();
        }
        
    }

    @Override
    public Socket getSocket() {
        // TODO 自动生成的方法存根
        return socket;
    }

    @Override
    public void setsocket(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void closeSocket() throws IOException {
        this.socket.close();
        
    }
    
    /**
     * 从输入流读取信息
     * @author yxlgg-if
     * @date 2021年3月30日 下午7:35:51
     * @return
     * @throws Exception
     */
    public byte[] readFromInputStream() throws Exception{
        readLock.lock();
        try {
            InputStream inputStream = getInputStream();
            // 约定前四位为消息的长度，byte数组作为一个缓冲区
            byte[] byteArray = new byte[4];
            // 读取四个字节的比特流。byte数组作为一个缓冲区，每次存入和缓冲区一样大小（byte.length）的数据
            inputStream.read(byteArray);
            // 将前四个字节的比特流转换为整数，当前0位置（除表示长度的4个字节外）到该整数的长度就是信息的内容
            int msgLength = ConvertTools.bytesToInt(byteArray);
            // 获取消息
            byte[] msgArray = new byte[msgLength];
            // 直接读取在网络延迟比较大的时候导致比特流不完整
//            inputStream.read(msgArray, 0, msgLength);
            int readLength = 0;
            
            // 可能会出现死循环
//            while(readLength < msgLength) {
//                readLength += inputStream.read(msgArray, readLength, msgLength - readLength);
//                System.out.println("readLength:" + readLength);
//            }

            while(readLength < msgLength) {
                // 从流中获取数据存到比特数组msgArray readLength 到 msgLength - readLength 的位置
                int read = inputStream.read(msgArray, readLength, msgLength - readLength);
                // 判断数据流是否到末尾避免死循环
                if (read == -1) {
                    break;
                }
                readLength += read;
            }
            return msgArray;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 将比特数组写入到输出流
     * @author yxlgg-if
     * @date 2021年3月30日 下午7:36:40
     * @param value
     * @throws Exception
     */
    public void writeIntoOutputStream(byte[] value) throws Exception{
        writeLock.lock();
        try {
            OutputStream outputStream = getOutputStream();
            int msgLength = value.length;
            outputStream.write(ConvertTools.intToBytes(msgLength));
            outputStream.write(value);
//            System.out.println("msgLength:" + msgLength);
//            System.out.println("value:" + Arrays.toString(value));
        } finally {
            // TODO: handle finally clause
            writeLock.unlock();
        }
    }
    
    private InputStream getInputStream() throws IOException {
        if (this.inputStream == null ) {
            return this.socket.getInputStream();
        }
        return this.inputStream;
    }
    
    private OutputStream getOutputStream() throws IOException {
        if (this.outputStream == null) {
            return this.socket.getOutputStream();
        }
        return this.outputStream;
    }

    @Override
    public void writeAndFlush(MsgModel value) throws Exception {
        this.write(value);
        this.flush();
    }
}
