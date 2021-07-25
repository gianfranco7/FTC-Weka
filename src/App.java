import java.io.BufferedReader;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        Dataset trainer = new Dataset();

        
        System.out.println("_____________________________________________________\n");
        System.out.println("                  Cargando modelo...");
        trainer.load("Models/train.arff");
        trainer.applyFilters();

        SVMClassifier model = new SVMClassifier(trainer, "Models/SVM_radial.model");
        
        model.classifyBatch("Models/train.csv");
    }
} 
