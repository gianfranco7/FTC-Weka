public class App {
    public static void main(String[] args) throws Exception {
        
        SVM model = new SVM("Models/03_SVM_1500.model");
        model.classify("ooked at my house it screams appropriation bc buddhas and stuff");
        //model.classify("Our_Deeds_are_the_Reason_of_this_earthquake_May_ALLAH_Forgive_us_all");
    }
} 
