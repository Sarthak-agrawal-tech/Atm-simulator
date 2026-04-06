package test.whitebox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.Permission;

public final class TestSupport {

    private TestSupport() {
    }

    public static String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(captured));
            action.run();
        } finally {
            System.setOut(originalOut);
        }

        return captured.toString();
    }

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException error) {
            throw new RuntimeException(error);
        }
    }

    public static Object getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException error) {
            throw new RuntimeException(error);
        }
    }

    public static void setStaticField(Class<?> type, String fieldName, Object value) {
        try {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (ReflectiveOperationException error) {
            throw new RuntimeException(error);
        }
    }

    public static int captureExitStatus(Runnable action) {
        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            action.run();
        } catch (ExitException error) {
            return error.getStatus();
        } finally {
            System.setSecurityManager(originalSecurityManager);
        }

        throw new AssertionError("Expected System.exit to be called.");
    }

    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission permission) {
        }

        @Override
        public void checkPermission(Permission permission, Object context) {
        }

        @Override
        public void checkExit(int status) {
            throw new ExitException(status);
        }
    }

    private static class ExitException extends SecurityException {
        private final int status;

        ExitException(int status) {
            this.status = status;
        }

        int getStatus() {
            return status;
        }
    }
}
