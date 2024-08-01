package hello;

public class MySingletonBean {

    // Volatile to ensure visibility of changes across threads
    private static volatile MySingletonBean mySingletonBean = null;
    private String myContext = null;

    private MySingletonBean() {
        // Intentionally left empty
    }

    public static MySingletonBean getInstance() {
        if (mySingletonBean == null) {
            synchronized (MySingletonBean.class) {
                if (mySingletonBean == null) {
                    mySingletonBean = new MySingletonBean();
                }
            }
        }
        return mySingletonBean;
    }

    public String getContext() {
        var context = "Hello";
        myContext = context;
        return context;
    }
}
