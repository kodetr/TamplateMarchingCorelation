package main;

import egami.matrix.Matrix;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.weightedPixel.WeightedStandardImage;
import main.weightedPixel.WeightedStandardPixelTrainer;

public class Train {

    public static void main(String[] args) {

        try {
            //list of image files//////////////////////////////////////////////////////////////////////////////////////////
            int types = 2;
            String[] stringFilePaths = new String[types];
            for (int i = 1; i <= types; i++) {
                stringFilePaths[i - 1] = "src/res/trainingData/" + i;
            }

            ArrayList<String> filePathList = new ArrayList<>();
            ArrayList<Integer> idList = new ArrayList<>();

            int id = 1;
            for (String stringFilePath : stringFilePaths) {

                File[] files = new File(stringFilePath).listFiles();

                for (File file : files) {
                    filePathList.add(file.getAbsolutePath());
                    idList.add(id);
                }

                id++;
            }

            String[] filePaths = new String[filePathList.size()];
            filePathList.toArray(filePaths);
            Integer[] ids = new Integer[idList.size()];
            idList.toArray(ids);

            ///test
            /*for(int i=filePaths.length-1; i>=0; i--){
            System.out.println("filePaths: " + filePaths[i]);
            System.out.println("ids: " + ids[i]);
            }*/
            //train/////////////////////////////////////////////////////////////////////////////////////////////////////////
            WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer(new int[]{45, 45});

            weightedStandardPixelTrainer.train(filePaths, ids);
            WeightedStandardImage weightedStandardImage = weightedStandardPixelTrainer.getWeightedStandardImage();
            weightedStandardImage.saveKnowledge("src/res/knowledge/KnowledgeAlphabet.log");

            ///test
            for (int i = 0; i < types; i++) {
                Matrix mat = weightedStandardImage.getStandardImages(i);
                mat.write("src/res/knowledge/imageRepresentation/stdImage " + i + ".png");
            }
            System.out.println("Operation Successful!!!");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
