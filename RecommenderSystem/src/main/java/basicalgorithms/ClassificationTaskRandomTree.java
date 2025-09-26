package basicalgorithms;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class ClassificationTaskRandomTree {

    public static void main(String[] args) throws Exception{



        /*
         * Загружаем данные
         */

        ConverterUtils.DataSource source = new ConverterUtils.DataSource("data/zoo.arff");
        Instances data = source.getDataSet();

        System.out.println(data.numInstances() + " instance loaded");
        System.out.println(data.numAttributes() + " attributes");

    }
}
