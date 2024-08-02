package hello;

public class MySingletonBean {

    private static volatile MySingletonBean mySingletonBean = null;
    private String myContext = null;
    private static final Object lock = new Object();

    private MySingletonBean() {
        // Intentionally left empty
    }

    public static MySingletonBean getInstance() {
        if (mySingletonBean == null) {
            synchronized (lock) {
                if (mySingletonBean == null) {
                    mySingletonBean = new MySingletonBean();
                }
            }
        }
        return mySingletonBean;
    }

    public synchronized String getContext() {
        return myContext;
    }

    public synchronized void setContext(String context) {
        this.myContext = context;
    }
}
