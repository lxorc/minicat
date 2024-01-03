package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * Minicat 的主类
 */
public class Bootstrap {

    // 定义一个soket 监听的端口号
    private int port = 8080;

    // 定义一个线程池
    // 初始大小
    int corePoolSize = 10;
    // 最大大小
    int maximumPoolSize = 50;
    // 活跃时间
    long keepAliveTime = 100L;
    // 活跃时间单位
    TimeUnit unit = TimeUnit.SECONDS;
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            workQueue,
            threadFactory,
            handler
    );

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
        // 需求3.0: 增加动态资源的请求
        // 需求3.1: 使用多线程
        //      解决问题：多个请求会卡住
        // 需求3.1: 使用多线程 + 线程池

        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletHashMap);
            executor.execute(requestProcessor);
        }


    }

    private HashMap<String, HttpServlet> servletHashMap = new HashMap<>();

    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");

            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);

                Element servletNameElement = ((Element) element.selectSingleNode("servlet-name"));
                String servletNameValue = servletNameElement.getStringValue();

                Element servletClassElement = ((Element) element.selectSingleNode("servlet-class"));
                String servletClassValue = servletClassElement.getStringValue();
                Element servletMapping = ((Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletNameValue + "']"));
                String urlPatternValue = servletMapping.selectSingleNode("url-pattern").getStringValue();

                servletHashMap.put(urlPatternValue, ((HttpServlet) Class.forName(servletClassValue).newInstance()));
            }

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Minicat 的启动入口
     *
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.loadServlet();
            bootstrap.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
