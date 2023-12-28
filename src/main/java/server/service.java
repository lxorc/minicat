package server;

public interface service {

    public void init() throws Exception;

    public void destory() throws Exception;

    public void service(Request request, Response response) throws Exception;
}
