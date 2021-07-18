import java.util.ArrayList;
import java.util.List;

import weka.classifiers.functions.LibSVM;
import weka.core.SerializationHelper;
import weka.core.stemmers.NullStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.Filter;

public class SVM {
    LibSVM svm = new LibSVM();

    public SVM(String path) {
        try {
            svm = (LibSVM) SerializationHelper.read(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double classify(String instance) {
        Instances dataset = createParametrizedDataset(instance);
        try {
            dataset = formatInstances(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    private Instances createParametrizedDataset(String inputText){
        Attribute id = new Attribute("id");
        
        ArrayList<String> nominal = new ArrayList<String>(3);
        nominal.add("fake_");
        nominal.add("real_");
        Attribute target = new Attribute("target", nominal);

        ArrayList<String> nominaltext = new ArrayList<String>(1);
        nominaltext.add(inputText);
        Attribute text = new Attribute("text", nominaltext);
        
        ArrayList<Attribute> attr = new ArrayList<Attribute>();
        attr.add(id);
        attr.add(target);
        attr.add(text);
        
        Instances dataset = new Instances("data", attr , 0);
        dataset.setRelationName("data");

        Instance inst = new DenseInstance(3);
        inst.setValue(id, 1);
        inst.setValue(target, "fake_");
        inst.setValue(text, inputText);

        dataset.add(inst);

        return dataset;
    }

    private Instances formatInstances(Instances dataset) throws Exception {
        Filter n2s = createParametrizedNominal2String();
        Filter s2wv = createParametrizedString2WordVector();
        

        //Dios 
        //Nos
        //Ha
        //Abandonado
        
        //
        //n2s.setInputFormat(dataset);
        //
        //Instances n2sApplied;
        //n2sApplied = Filter.useFilter(dataset, n2s);
        //
        //s2wv.setInputFormat(n2sApplied);
        //
        //Instances s2wvApplied;
        //s2wvApplied = Filter.useFilter(n2sApplied, s2wv);

        //return s2wvApplied;

        return dataset;
    }

    private Filter createParametrizedNominal2String(){
        String[] options = {"-C", "3"};
        Filter n2s = new NominalToString();
        try {
            n2s.setOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n2s;
    }

    private Filter createParametrizedString2WordVector(){
        String tokenDelimiters = "";
        String[] tokenOptions = {"-max", "3", "-min", "1", "-delimiters", tokenDelimiters};
        String[] s2wvOptions = {"-R", "3", "-W", "1000", "-prune-rate", "1.0", "-L"};
        
        NullStemmer stemmer = new NullStemmer();

        NGramTokenizer tokenizer = new NGramTokenizer();
        try {
            tokenizer.setOptions(tokenOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        StringToWordVector s2wv = new StringToWordVector();
        s2wv.setStemmer(stemmer);
        s2wv.setTokenizer(tokenizer);
        try {
            s2wv.setOptions(s2wvOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s2wv;
    }
}
