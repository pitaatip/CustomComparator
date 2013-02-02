package pl.agh.edu.comparator;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.*;

public class CustomComparator<T, E extends Enum<E>> {
    private final T obj1;
    private final T obj2;
    private final Class<E> enumClass;
    private final EnumMap<E, List<String>> resultFieldsMap;
    private final Map<String, E[]> inputClassifiers = new HashMap<String, E[]>();

    public CustomComparator(T obj1, T obj2, Class<E> enumClass) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.enumClass = enumClass;
        resultFieldsMap = initFieldsMap();
    }

    public final CustomComparator<T, E> addClassifier(String fieldName, E... classifiers) {
        inputClassifiers.put(fieldName, classifiers);
        return this;
    }

    public final ClassificationResult compare() {
        throwExceptionOnNullObjects();
        for (String fieldName : inputClassifiers.keySet()) {
            Object field1value = getFieldValue(fieldName, obj1);
            Object field2Value = getFieldValue(fieldName, obj2);
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

    @SneakyThrows(IllegalAccessException.class)
    private Object getFieldValue(String fieldName, T obj) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object fieldValue = field.get(obj);
            field.setAccessible(accessible);
            return fieldValue;
        } catch (NoSuchFieldException e) {
            String message = String.format("Object %s had no field: %s.", obj.getClass().getName(), fieldName);
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