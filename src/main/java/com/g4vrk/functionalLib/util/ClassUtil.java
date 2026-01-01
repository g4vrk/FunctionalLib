package com.g4vrk.functionalLib.util;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

@UtilityClass
public class ClassUtil {

    private static final  StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static Optional<Class<?>> getCallerClass() {
        return STACK_WALKER.walk(stream ->
                stream.filter(frame -> frame.getDeclaringClass() != ClassUtil.class)
                        .findFirst()
                        .map(StackWalker.StackFrame::getDeclaringClass)
        );
    }

    public static String getPluginName() {
        return getCallerClass()
                .flatMap(ClassUtil::tryPluginName)
                .orElse("Unknown");
    }

    public static String getCallerClassName() {
        return getCallerClass()
                .map(Class::getSimpleName)
                .orElse("Unknown");
    }

    private static Optional<String> tryPluginName(Class<?> clazz) {
        try {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(clazz);
            return Optional.of(plugin.getName());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }
}