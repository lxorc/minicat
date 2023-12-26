package server;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

// 封装 Response 对象需要依赖于 outputStream

// 该对象需要提供一个核心方法，输出html静态资源
public class Response {

    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    /**
     *
     * @param path url , 随后要根据url来获取到静态资源的绝对路径，进一步根据绝对路径读取该静态资源文件，最终通过输出流输出。
     * @throws IOException
     */
    public void outputHtml(String path) throws IOException {
        // 获取静态资源的绝对路径
        String absResourcePath = StaticResourceUtil.getAbsPath(path);

        // 输入静态资源文件
        File file = new File(absResourcePath);

        if (file.exists() && file.isFile()  ) {
            // 输出静态资源
            FileInputStream fileInputStream = new FileInputStream(file);
            StaticResourceUtil.outputStaticResource(fileInputStream, this.outputStream);
        } else {
            // 输出404
            String responseText = HttpProtocolUtil.getHttpHeader404();
            ouput(responseText);
        }

    }

    private void ouput(String responseText) throws IOException {
        this.outputStream.write(responseText.getBytes());
    }
}
