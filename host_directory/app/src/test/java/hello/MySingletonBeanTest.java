package hello;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class MySingletonBeanTest {

    @Test
    public void testSingletonInstance() {
        MySingletonBean instance1 = MySingletonBean.getInstance();
        MySingletonBean instance2 = MySingletonBean.getInstance();
        
        assertNotNull(instance1, "Instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same");
    }

    @Test
    public void testGetContext() {
        MySingletonBean instance = MySingletonBean.getInstance();
        String context = instance.getContext();
        
        assertNotNull(context, "Context should not be null");
        assertEquals("Hello", context, "Context should be 'Hello'");
    }

    @Test
    public void testNestedNullCheckFalse() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        Field instanceField = MySingletonBean.class.getDeclaredField("mySingletonBean");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        CountDownLatch latch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            MySingletonBean.getInstance(); 
            latch.countDown();
        });

        Thread thread2 = new Thread(() -> {
            try {
                latch.await(); 
                MySingletonBean instance = MySingletonBean.getInstance();
                assertNotNull(instance, "Instance should not be null in thread2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        MySingletonBean finalInstance = MySingletonBean.getInstance();
        assertNotNull(finalInstance, "Final instance should not be null");
    }
}
