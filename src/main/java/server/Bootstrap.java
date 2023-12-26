package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Minicat 的主类
 */
public class Bootstrap {

    // 定义一个soket 监听的端口号

    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    // Minicat 启动，需要初始化，展开的操作
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println(">>>>>>>>>>>>>Minicat start！ on port: " + port);
        // 需求1.0: 浏览器打开 http://localhost:8080/ 服务返回 'hello world'
        // 需求2.0: 封装Request 和 Response 对象，返回html静态资源文件

        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();


            Request request = new Request(inputStream);
            Response response = new Response(outputStream);
            response.outputHtml(request.getUrl());

            socket.close();

        }





    }

    /**
     * Minicat 的启动入口
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
