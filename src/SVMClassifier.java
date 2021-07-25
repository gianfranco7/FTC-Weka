import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
//import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;



public class SVMClassifier {
    LibSVM classifier;
    Instances modelHeader;
    Dataset dataset;

    public SVMClassifier(Dataset dataset, String path) throws Exception {
        String options[] = {"-S", "0", "-K", "2", "-D", "3", "-G", "0.0", "-R", "0.0", "-N", "0.5", "-M", "40.0", "-C", "10.0", "-E", "0.001", "-P", "0.1"};
        classifier = new LibSVM();
        classifier.setOptions(options);
        this.dataset = dataset;
        this.modelHeader = dataset.dataset;

        System.out.println("_____________________________________________________\n");
        System.out.println("                  Construyendo...\n");
        classifier.buildClassifier(modelHeader);

        Evaluation eval = new Evaluation(modelHeader);
        eval.evaluateModel(classifier, modelHeader);

        System.out.println("Correct:  "+ eval.correct() + " Incorrect: " + eval.incorrect());

        System.out.println("_____________________________________________________\n");
        System.out.println("                  Summary...");
        System.out.println(eval.toSummaryString());
         
        dataset.save("Models/generated_dataset.arff");
    }

    public void classifyBatch(String path) throws Exception{
        System.out.println("_____________________________________________________\n");
        System.out.println("                  Batch evaluation...");
        
        
        Instances localData = new Instances(modelHeader);

        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        line = reader.readLine();

        while(line != null)
        {    
            line = line.toLowerCase();
            line = line.replace("target", "");
            line = line.replace("id", "");
            String data[] = line.split(",");
            
            Instance inst = matchInstance(data[2]);
            
            inst.setValue(0, data[1]);
            
            localData.add(inst);
            line = reader.readLine();
        }

        Evaluation localEval = new Evaluation(localData);
        localEval.evaluateModel(classifier, localData);

        System.out.println(localEval.toSummaryString());

        double[][] CM = localEval.confusionMatrix();
        System.out.println(CM[0][0] + " " + CM[1][0]);
        System.out.println(CM[0][1] + " " + CM[1][1]);
    }

    public String classify(String input) throws Exception{
        input = input.toLowerCase();
        Instance inst = matchInstance(input);

        modelHeader.add(inst);
        double result = classifier.classifyInstance(modelHeader.lastInstance());
        double predict[] = classifier.distributionForInstance(modelHeader.lastInstance());
        
        System.out.println(result + ", " + "Fake: " + predict[0] + " Real: " + predict[1]);

        String res = "fake";
        if(predict[1] > 0){
            res = "real";
        }

        return res;
    }

    private Instance matchInstance(String input) throws Exception{
        Instances data = dataset.createDummyDataset(input);
        Instance inst = new SparseInstance(modelHeader.numAttributes());
        
        inst.setDataset(modelHeader);

        for(int i = 1; i < modelHeader.numAttributes(); i++){
            inst.setValue(i, 0);
        }
        
        for(int i = 1; i < data.numAttributes(); i++){
            Attribute att = modelHeader.attribute(data.attribute(i).name());
            if (att != null){
                inst.setValue(att.index(), 1);
            }
        }

        inst.setValue(0, "real");

        return inst;
    }
}
