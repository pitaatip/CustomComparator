package pl.agh.edu.comparator;

import org.azeckoski.reflectutils.ReflectUtils;

import java.util.*;

public class CustomComparatorReflUtils<T, E extends Enum<E>> {
    private final T obj1;
    private final T obj2;
    private final Class<E> enumClass;
    private final EnumMap<E, List<String>> resultFieldsMap;
    private final Map<String, E[]> inputClassifiers = new HashMap<String, E[]>();
    private final ReflectUtils reflectUtils = ReflectUtils.getInstance();

    public CustomComparatorReflUtils(T obj1, T obj2, Class<E> enumClass) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.enumClass = enumClass;
        resultFieldsMap = initFieldsMap();
    }

    public CustomComparatorReflUtils<T, E> addClassifier(String fieldName, E... classifiers) {
        inputClassifiers.put(fieldName, classifiers);
        return this;
    }

    public ClassificationResult compare() {
        throwExceptionOnNullObjects();
        for (String fieldName : inputClassifiers.keySet()) {
            Object field1value = reflectUtils.getFieldValue(obj1, fieldName);
            Object field2Value = reflectUtils.getFieldValue(obj2, fieldName);
            boolean areEquals = checkEquality(field1value, field2Value);
            if (!areEquals) {
                fillFieldsMap(fieldName);
            }
        }
        return new ClassificationResult<E>(resultFieldsMap);
    }

    // PRIVATE METHODS

    private void throwExceptionOnNullObjects() {
        if (obj1 == null || obj2 == null) {
            String message = String.format("Compared objects could not be null. [Obj1 = %s, Obj2 = %s]", obj1, obj2);
            throw new IllegalArgumentException(message);
        }
    }

    private void fillFieldsMap(String fieldName) {
        for (E classifier : inputClassifiers.get(fieldName)) {
            resultFieldsMap.get(classifier).add(fieldName);
        }
    }

    private boolean checkEquality(Object field1value, Object field2Value) {
        if (field1value != null) {
            return field1value.equals(field2Value);
        } else {
            return (field2Value == null);
        }
    }

    private EnumMap<E, List<String>> initFieldsMap() {
        EnumMap<E, List<String>> enumMap = new EnumMap<E, List<String>>(enumClass);
        for (E classifier : enumClass.getEnumConstants()) {
            enumMap.put(classifier, new LinkedList<String>());
        }
        return enumMap;
    }
}