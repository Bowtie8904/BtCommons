package bt.utils.refl.anot;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
     * annotation(s).
     *
     * @param topPackage
     *            The highest package to search from. Subpackages will be searched too.
     * @param annot
     *            The annotations to look for.
     * @return The set of classes.
     */
    public static Set<Class<?>> getAnnotatedClasses(String topPackage, Class<? extends Annotation>... annots)
    {
        Reflections reflections = new Reflections(topPackage);

        Set<Class<?>> annotated = null;

        for (var annot : annots)
        {
            if (annotated == null)
            {
                annotated = reflections.getTypesAnnotatedWith(annot);
            }
            else
            {
                annotated.addAll(reflections.getTypesAnnotatedWith(annot));
            }
        }

        return annotated;
    }

    /**
     * Gets a list of methods from the given class (and its super class/es) that are annotated with the given
     * annotation(s).
     *
     * @param type
     *            The class which methods should be looked through.
     * @param annotations
     *            The annotations to search for.
     * @return The list of methods.
     */
    public static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation>... annotations)
    {
        List<Method> methods = new ArrayList<>();
        Class<?> currentClass = type;

        while (currentClass != Object.class)
        {
            List<Method> allMethods = new ArrayList<>(Arrays.asList(currentClass.getDeclaredMethods()));

            for (Method method : allMethods)
            {
                for (var annotation : annotations)
                {
                    if (method.isAnnotationPresent(annotation))
                    {
                        methods.add(method);
                        break;
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return methods;
    }

    /**
     * Gets a list of fields from the given class (and its super class/es) that are annotated with the given
     * annotation(s).
     *
     * @param type
     *            The class which fields should be looked through.
     * @param annotations
     *            The annotations to search for.
     * @return The list of fields.
     */
    public static List<Field> getFieldsAnnotatedWith(Class<?> type, Class<? extends Annotation>... annotations)
    {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = type;

        while (currentClass != Object.class)
        {
            List<Field> allFields = new ArrayList<>(Arrays.asList(currentClass.getDeclaredFields()));

            for (Field field : allFields)
            {
                for (var annotation : annotations)
                {
                    if (field.isAnnotationPresent(annotation))
                    {
                        fields.add(field);
                        break;
                    }
                }

            }
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }
}