import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;



public class SVMClassifier {
    Classifier classifier;
    Instances modelHeader;
    Dataset dataset;

    public SVMClassifier(Dataset dataset, String path) throws IOException, ClassNotFoundException {
        InputStream is = new BufferedInputStream(new FileInputStream(path));
        ObjectInputStream objectStream = new ObjectInputStream(is);

        classifier = (Classifier) objectStream.readObject();
        modelHeader = (Instances) objectStream.readObject();
        
        objectStream.close();

        this.dataset = dataset;
    }

    public String classify(String input) throws Exception{
        input = input.toLowerCase();
        Instance inst = matchInstance(input);

        double result = classifier.classifyInstance(inst);
        double predict[] = classifier.distributionForInstance(inst);

       System.out.println("Fake: " + predict[0] + " Real: " + predict[1]);

        return modelHeader.classAttribute().value((int) result);
    }

    private Instance matchInstance(String input) throws Exception{
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        Instances data = dataset.createDummyDataset(input);
        for(int i = 2; i < data.numAttributes(); i++){
            Attribute att = modelHeader.attribute(data.attribute(i).name());
            if (att != null){
                indexes.add(att.index());
            }
        }

        Instance inst = new SparseInstance(modelHeader.numAttributes());
        inst.setDataset(modelHeader);
        //inst.setValue(0, 1);
        inst.setValue(1, "real");

        for(int i = 2; i < modelHeader.numAttributes(); i++){
            inst.setValue(i, 0);
        }

        for(int index : indexes){    
            inst.setValue(modelHeader.attribute(index), 1);
        }
        
        return inst;
    }
}
