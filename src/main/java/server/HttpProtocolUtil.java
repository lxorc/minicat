package server;

// http 协议工具类，主要提供响应头信息
public class HttpProtocolUtil {


    // 为http响应200提供信息
    public static String getHttpHeader200(Long contentLen){
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html\n" +
                "Content-Length: " + contentLen + "\n" +
                "\r\n";
    }


    // 为http响应404提供信息
    public static String getHttpHeader404(){
        String str404 = "<h1>404 NOT FOUND</h1>";
        return "HTTP/1.1 404 NOT FOUND\n" +
                "Content-Length: " + str404.length() + "\n" +
                "\r\n";
    }
}
