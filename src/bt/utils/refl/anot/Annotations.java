package bt.utils.refl.anot;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

/**
 * Basic utilities involving annotations.
 * 
 * @author &#8904
 */
public final class Annotations
{
    /**
     * Gets a set of all classes within the given package or within subpackages that are annotated with the given
     * annotation.
     * 
     * @param topPackage
     *            The highest package to search from. Subpackages will be searched too.
     * @param anot
     *            The annotation class to look for.
     * @return The set of classes.
     */
    public static Set<Class<?>> getAnnotatedClasses(String topPackage, Class<? extends Annotation> anot)
    {
        Reflections reflections = new Reflections(topPackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(anot);
        return annotated;
    }

    /**
     * Gets a list of methods from the given class (and its super class/es) that are annotated with the given
     * annotation.
     * 
     * @param type
     *            The class which methods should be looked through.
     * @param annotation
     *            The annotation class to search for.
     * @return The list of methods.
     */
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