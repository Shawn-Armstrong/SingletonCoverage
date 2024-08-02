package hello;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MySingletonBeanTest {

    @BeforeEach
    void setUp() throws Exception {
        resetSingleton();
    }

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
    void testSingletonInstance() {
        MySingletonBean instance1 = MySingletonBean.getInstance();
        MySingletonBean instance2 = MySingletonBean.getInstance();

        assertSame(instance1, instance2, "Instances should be the same");
    }

    @Test
    void testContextSetting() {
        MySingletonBean instance = MySingletonBean.getInstance();
        instance.setContext("Test Context");

        assertEquals("Test Context", instance.getContext(), "Context should be set correctly");
    }
}
