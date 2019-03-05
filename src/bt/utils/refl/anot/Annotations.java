package bt.utils.refl.anot;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

/**
 * @author &#8904
 *
 */
public final class Annotations
{
    public static Set<Class<?>> getAnnotatedClasses(String topPackage, Annotation anot)
    {
        Reflections reflections = new Reflections(topPackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(anot.annotationType());
        return annotated;
    }

    public static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation)
    {
        List<Method> methods = new ArrayList<Method>();
        Class<?> currentClass = type;

        while (currentClass != Object.class)
        {
            List<Method> allMethods = new ArrayList<Method>(Arrays.asList(currentClass.getDeclaredMethods()));

            for (Method method : allMethods)
            {
                if (method.isAnnotationPresent(annotation))
                {
                    methods.add(method);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return methods;
    }
}