package pl.agh.edu;


import junit.framework.Assert;
import org.junit.Test;
import pl.agh.edu.comparator.ClassificationResult;
import pl.agh.edu.comparator.CustomComparatorReflUtils;
import pl.agh.edu.comparator.EasyClassifier;
import pl.agh.edu.data.ExampleData;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-02-01
 * Time: 23:29
 * To change this template use File | Settings | File Templates.
 */
public class CustomComparatorTest {

    @Test
    public void test() {
        ExampleData data1 = new ExampleData();
        ExampleData data2 = new ExampleData();

        data1.setName("ala");
        data2.setName("ala");

        data1.setSurname("orangutan");

        data1.setMale(true);
        data2.setMale(false);

        CustomComparatorReflUtils<ExampleData, EasyClassifier> comparator
                = new CustomComparatorReflUtils<ExampleData, EasyClassifier>(data1, data2, EasyClassifier.class);
        comparator
                .addClassifier("name", EasyClassifier.personalInfo, EasyClassifier.names)
                .addClassifier("male", EasyClassifier.names)
                .addClassifier("surname", EasyClassifier.names);
        ClassificationResult result = comparator.compare();

        Assert.assertTrue(result.areAllFieldsEquals(EasyClassifier.personalInfo));
        Assert.assertFalse(result.areAllFieldsEquals(EasyClassifier.names));

        Assert.assertEquals(result.getListOfDifferentFields(EasyClassifier.names).size(), 2);

        System.out.println(result);
    }

}
