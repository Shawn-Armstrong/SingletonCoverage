package hello;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class MySingletonBeanTest {

    @BeforeEach
    void setUp() throws Exception {
        resetSingleton();
    }

    // Reset the singleton instance and its context before each test
    private void resetSingleton() throws Exception {
        Field instanceField = MySingletonBean.class.getDeclaredField("mySingletonBean");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        Field contextField = MySingletonBean.class.getDeclaredField("myContext");
        contextField.setAccessible(true);
        MySingletonBean instance = MySingletonBean.getInstance();
        contextField.set(instance, null);
    }

    @Test
    void testGetInstance_givenSequentialExecution_expectSameObject() {
        MySingletonBean instance1 = MySingletonBean.getInstance();
        MySingletonBean instance2 = MySingletonBean.getInstance();

        assertSame(instance1, instance2, "Expected instances to be the same");
    }

    @Test
    void testSetContextGetContext_givenValidContext_expectContext() {
        MySingletonBean instance = MySingletonBean.getInstance();

        JSONObject context = new JSONObject();
        context.put("test", "Test Context");
        instance.setContext(context);

        JSONObject retrievedContext = instance.getContext();
        assertNotNull(retrievedContext, "Expected context to be non-null");
        assertEquals("Test Context", retrievedContext.getString("test"), "Expected context to contain key test with value");
    }

    @Test
    void testGetInstance_givenConcurrentExecution_expectSameObjects() throws Exception {
        int maxThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);
        Callable<MySingletonBean> task = MySingletonBean::getInstance;
        List<Future<MySingletonBean>> futures = new ArrayList<>(maxThreads);

        for (int i = 0; i < maxThreads; i++) {
            futures.add(executorService.submit(task));
        }

        MySingletonBean firstInstance = futures.get(0).get();
        JSONObject theContext = new JSONObject().put("test", "Test Context");
        firstInstance.setContext(theContext);
        JSONObject firstContext = firstInstance.getContext();

        for (int i = 1; i < maxThreads; i++) {
            MySingletonBean nextInstance = futures.get(i).get();
            JSONObject nextContext = nextInstance.getContext();

            assertSame(firstInstance, nextInstance, "Expected the same instance");
            assertSame(firstContext, nextContext, "Expected the same context object");
            assertEquals("Test Context", nextContext.getString("test"), "Expected context to contain key test with value");
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }
}
