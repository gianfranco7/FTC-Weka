import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.stemmers.NullStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class SVM {
    Classifier classifier;
    Instances modelHeader;
    FilteredClassifier fc;

    public SVM(String path) throws IOException, ClassNotFoundException {
        InputStream is = new BufferedInputStream(new FileInputStream(path));
        ObjectInputStream objectStream = new ObjectInputStream(is);

        classifier = (Classifier) objectStream.readObject();
        modelHeader = (Instances) objectStream.readObject();

        objectStream.close();

        fc = new FilteredClassifier();
        fc.setClassifier(classifier);
        fc.setFilter(createMultiFilter());
        
        //try {
        //    fc.buildClassifier(modelHeader);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
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
        String tokenDelimiters = "\\r\\t.,;:\\\'\\\"()?!_";
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

    private MultiFilter createMultiFilter(){
        MultiFilter filter = new MultiFilter();
        Filter filters[] = new Filter[2];
        
        filters[0] = createParametrizedNominal2String();
        filters[1] = createParametrizedString2WordVector();

        filter.setFilters(filters);
        return filter;
    }

    private Instance createInstance(String inputText){
        //  Set ID
        Attribute id = new Attribute("id");
        
        //  Set classes
        ArrayList<String> nominal = new ArrayList<String>(3);
        nominal.add("fake");
        nominal.add("real");
        Attribute target = new Attribute("target", nominal);

        //  Set nominal text
        ArrayList<String> nominaltext = new ArrayList<String>(1);
        nominaltext.add(inputText);
        Attribute text = new Attribute("text", nominaltext);
        
        //Set attributes
        ArrayList<Attribute> attr = new ArrayList<Attribute>();
        attr.add(id);
        attr.add(target);
        attr.add(text);
        Instances dataset = new Instances("data", attr, 0);
        dataset.setClassIndex(1);
        
        //    Set Instance
        Instance inst = new SparseInstance(3);
        inst.setValue(id, 1);
        inst.setValue(target, "fake");
        inst.setValue(text, inputText);

        dataset.add(inst);

        try {
            fc.buildClassifier(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        inst.setDataset(dataset);

        try {
            double value = fc.classifyInstance(inst);
            System.out.println(dataset.classAttribute().value((int)value));
       } catch (Exception e) {
            e.printStackTrace();
       }

        return inst;
    }

    public void classify(String text){
        Instance inst = createInstance(text);

        // System.out.println(inst);
       
       
    }
}
