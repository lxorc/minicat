package server;

import java.io.IOException;

public class LagouServlet extends HttpServlet{
    @Override
    public void doGet(Request request, Response response)  {

        // 假装休眠
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String content = "<h1>lagou servlet</h1>";

        try {
            response.output(HttpProtocolUtil.getHttpHeader200((long) content.length()));
            response.output(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {

    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
