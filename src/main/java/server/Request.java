package server;

import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String method;
    private String url;

    private InputStream inputStream;


    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }


    public Request() {
    }

    // 构造器，输入流传入
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        // 从输入流获取请求信息
        int count = 0;

        // 阻塞网络IO，直到读取到数据
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        String[] inputText = new String(bytes).split("\n");

        // 获取第一行
        String[] urlHeader = inputText[0].split(" "); // GET / HTTP/1.1
        this.method = urlHeader[0];
        this.url = urlHeader[1];

        System.out.println("Method: " + this.method);
        System.out.println("Url: " + this.url);
    }

}
