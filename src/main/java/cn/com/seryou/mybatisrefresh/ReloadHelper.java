package cn.com.seryou.mybatisrefresh;

import org.zeroturnaround.javarebel.RequestIntegrationFactory;
import org.zeroturnaround.javarebel.integration.generic.RequestListenerAdapter;

public class ReloadHelper {

    public static void addRequestListener(MybatisRefresher refresher) {
        // Request listeners are called before each request is processed by the server.
        // Use them to check timestamps of different resources and reinitialize parts of the application.
        // You can order the request listeners using the priority parameter in the constructor.
        RequestIntegrationFactory.getInstance().addRequestListener(new RequestListenerAdapter(0) {
            @Override
            public void beforeRequest() {
                refresher.refresh();
            }
        });
    }

//    public static void addClassChangeListener(ReloadableServlet servlet, Class<?> configClass) throws Exception {
//        // Class event listeners are called when a class is reloaded by JRebel.
//        // You can order the reload listeners using the priority parameter in the constructor.
//        ReloaderFactory.getInstance().addClassReloadListener(configClass, new ClassEventListenerAdapter(0) {
//            @Override
//            public void onClassEvent(int eventType, Class<?> klass) throws Exception {
//                System.out.println("Reloading monitor");
//                servlet.reloadMonitor();
//            }
//        });
//
//        // Note that the Legacy JRebel Agent works differently from the JRebel Agent:
//        // * JRebel Agent automatically detects and reloads all changed classes
//        // * Legacy Agent only reloads a class when you call a method on it or use reflection on it.
//        // Use ReloaderFactory.getInstance().checkAndReload(someClass) to force a reload when using the Legacy Agent.
//    }
}
