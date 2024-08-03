package hello;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

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
    void testGetInstance_givenMultipleInstances_expectSameObject() {
        MySingletonBean instance1 = MySingletonBean.getInstance();
        MySingletonBean instance2 = MySingletonBean.getInstance();

        assertSame(instance1, instance2, "Expected instances to be the same");
    }

    @Test
    void testSetContextGetContext_givenValidContext_expectContext() {
        MySingletonBean instance = MySingletonBean.getInstance();

        JSONObject context = new JSONObject();
        context.put("key", "Test Context");
        instance.setContext(context);

        // Retrieve the context and check if it matches
        JSONObject retrievedContext = instance.getContext();
        assertNotNull(retrievedContext, "Expected context to be non-null");
        assertEquals("Test Context", retrievedContext.getString("key"), "Expected context to contain key");
    }
}
