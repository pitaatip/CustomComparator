package pl.agh.edu.comparator;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ClassificationResult<E extends Enum<E>> {

    private final Map<E, List<String>> classifierFields;

    public boolean areAllFieldsEquals(E classifier) {
        return classifierFields.get(classifier).size() == 0;
    }

    public List<String> getListOfDifferentFields(E classifier) {
        return classifierFields.get(classifier);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (E key : classifierFields.keySet()) {
            boolean areEqual = areAllFieldsEquals(key);
            builder.append("[Classifier: ").append(key)
                    .append(" resolved to: ").append(areEqual);
            if (!areEqual) {
                builder.append(". Different Fields are: ");
                for (String field : classifierFields.get(key)) {
                    builder.append(field).append(",");
                }
                // delete last comma
                builder.deleteCharAt(builder.length() - 1);
            }
            builder.append("]");
        }
        return builder.toString();
    }
}