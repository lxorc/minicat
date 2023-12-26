package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticResourceUtil {


    // 获取静态资源的绝对路径
    public static String getAbsPath(String path) {
        String absPath = StaticResourceUtil.class.getResource("/").getPath();

        return absPath + path;
    }

    // 读取静态资源文件输入流，通过输出流输出

    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int resourceSize = 0;

        while (resourceSize == 0) {
            resourceSize = inputStream.available();
        }

        // 输出 http 请求头，然后再输出内容
        String httpHeader200 = HttpProtocolUtil.getHttpHeader200((long) resourceSize);
        outputStream.write(httpHeader200.getBytes());


        // 读取内容输出

        long written = 0;  // 已经读取的内容
        int bytesize = 1024; // 每次缓冲的长度
        byte[] bytes = new byte[bytesize];


        while (written < resourceSize) {
            if (written + bytesize >resourceSize ) { // 说明剩余的大小不足 1024
                bytesize = (int) (resourceSize - written); // 剩余的大小
            }

            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();

            written+=bytesize;
        }



    }
}
