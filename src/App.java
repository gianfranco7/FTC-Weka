import java.io.BufferedReader;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        Dataset trainer = new Dataset();
        //trainer.load("Models/train.arff");
        //trainer.applyFilters();

        SVMClassifier model = new SVMClassifier(trainer, "Models/SVM_radial.model");
        

        int total = 0;
        int correct = 0;


        BufferedReader reader = new BufferedReader(new FileReader("Models/train.csv"));
        String line;
        line = reader.readLine();

        System.out.println("                      Clasificando...");

        int max = 100;
        while(line != null && max > 0)
        {   
            max--;
            line = line.toLowerCase();
            line = line.replace("target", "");
            line = line.replace("id", "");
            String data[] = line.split(",");

            String result = model.classify(data[2]);
            if (result.equals(data[1])){
                correct++;
            }
            total++;
            line = reader.readLine();
        }

        System.out.println("_____________________________________________________");
        System.out.println("Total instances: " + total);
        System.out.println("Correct instances: " + correct);
        System.out.println("Percentage: " + (correct * 100 / total) + "%");
        System.out.println("_____________________________________________________");

        //model.classify("Firepower in the lab love twister tsunami beacon california bill paxton");
    }
} 
