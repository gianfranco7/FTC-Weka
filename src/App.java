public class App {
    public static void main(String[] args) throws Exception {
        Dataset trainer = new Dataset();

        
        System.out.println("_____________________________________________________\n");
        System.out.println("                  Cargando dataset...");
        trainer.load("Models/train.arff");
        trainer.applyFilters();

        SVMClassifier model = new SVMClassifier(trainer, "Models/SVMTrainedModel.model");
        

        model.classify("There's an emergency evacuation happening now in the building across the street");
    }
} 
