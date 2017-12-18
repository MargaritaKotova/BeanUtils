import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BeanUtils {

    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */


    public static void assign(Object to, Object from) {
        List<Method> getMethods = new ArrayList<>();
        List<Method> setMethods = new ArrayList<>();

        for (Method method: from.getClass().getMethods()) {//заполняем getMethods getter-ами
            if(isGetter(method)) {
                getMethods.add(method);
            }
        }

        for (Method method: to.getClass().getMethods()) {//заполняем getMethods setter-ами
            if(isSetter(method)) {
                setMethods.add(method);
            }
        }

        for (Method setMethod: setMethods) {
            String setMethodName = setMethod.getName().substring(3);
            for (Method getMethod: getMethods) {
                if (setMethodName.equals(getMethod.getName().substring(3)) &&
                        CompareTypes(setMethod.getParameterTypes()[0], getMethod.getReturnType())) {
                    try {
                        setMethod.invoke(to, getMethod.invoke(from));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                }
            }
        }
    }

    private static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get") || method.getParameterTypes().length != 0 || void.class.equals(method.getReturnType())) {
            return false;
        }
        return true;
    }

    private static boolean isSetter(Method method) {
        if (!method.getName().startsWith("set") || method.getParameterTypes().length != 1) {
            return false;
        }
        return true;
    }

    private static boolean CompareTypes(Class to, Class from) {
        if (to == from || to == from.getSuperclass())
            return true;
        else
            return false;
    }



}