package server;

import java.io.IOException;

public abstract class HttpServlet implements service{

    public abstract void doGet(Request request, Response response) throws IOException;

    public abstract void doPost(Request request, Response response);


    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equals(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }
}
