package hello;

import org.json.JSONObject;

public class MySingletonBean {

    private static volatile MySingletonBean mySingletonBean = null;
    private JSONObject myContext = null;
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

    public synchronized JSONObject getContext() {
        return myContext;
    }

    public synchronized void setContext(JSONObject context) {
        this.myContext = context;
        addRandomNumberToContext();
    }

    public synchronized void addRandomNumberToContext() {
        int randomNumber = (int) (Math.random() * 100);
        myContext.put("id", randomNumber);
    }
}
