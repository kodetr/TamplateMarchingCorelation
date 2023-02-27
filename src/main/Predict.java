/**
 * Developer: Minhas Kamal (BSSE: 0509, IID, DU)
 * Date: 09.Sep.2014
 * */
package main;

import java.io.*;

import main.weightedPixel.WeightedStandardPixelTrainer;
import egami.matrix.Matrix;

public class Predict {

    public static void main(String[] args) throws Exception {

        String sampleImage = "src/res/sample/dua.png";
        String experience = "src/res/knowledge/KnowledgeAlphabet.log";
        String output = sampleImage + "Content.txt";

        String str = ImageToContentString(sampleImage, experience);

        System.out.println("\n\n ###String: " + str + "\n\n");

        //store the result in hard disk
        try {
            FileWriter writer = new FileWriter(new File(output));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Operation Successful!!!");
    }

    /**
     *
     * @param path
     * @param Experience
     * @return
     * @throws Exception
     */
    public static String ImageToContentString(String path, String Experience) throws Exception {
        String string = "";

        System.out.println("Loading Knowledge...");
        WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
        weightedStandardPixelTrainer.load(Experience);
        /*WeightedStandardImage weightedStandardImage = weightedStandardPixelTrainer.getWeightedStandardImage();
		System.out.println(weightedStandardImage.dump());*/

//		CvGBTrees gbt = new CvGBTrees();
//		gbt.load("src/alphabetCollection/resource/Alphabets/AlphabetDetectorGBT.xml");
        Matrix matImage = new Matrix(path, Matrix.BLACK_WHITE);

        Matrix matTemp;
        int[] a = new int[2];

        int verticalSpace = 3;
        int i = 1;
        while (true) {

            a = ObjectEdgeDetector.upDownRange(matImage, a[1]);
            if (a[1] == 0) {
                break;
            }

            matTemp = matImage.subMatrix(a[0] - verticalSpace, a[1] + verticalSpace, 0, matImage.getCols());

            Matrix matTemp2;
            int x[] = new int[2];
            int y[] = new int[2];

            while (true) {
                x = ObjectEdgeDetector.leftRightRange(matTemp, x[1]);
                if (x[1] == 0) {
                    break;
                }

                matTemp2 = matTemp.subMatrix(0, matTemp.getRows(), x[0], x[1]);

                y = ObjectEdgeDetector.upRangeDownRange(matTemp2, 0, matTemp2.getRows() - 1);
                if (y[1] == 0) {
                    break;
                }
                matTemp2 = matTemp2.subMatrix(y[0] - verticalSpace, y[1] + verticalSpace, 0, matTemp2.getCols());

                //detect
                int f = weightedStandardPixelTrainer.predict(matTemp2);

                //Highgui.imwrite("C:\\Users\\admin\\Desktop\\" + i + ".png" , matTemp2);	///test
                System.out.println("cek " + f);
                string = string + getValueOf(f);
                System.out.println("Working on alphabet >> " + i++);	///show progress
            }
        }

        return string;
    }

    /**
     *
     * @param f
     * @return
     */
    public static String getValueOf(int f) {
        String ch;

        switch (f) {
            case 1:
                ch = "ha";
                break;
            case 2:
                ch = "na";
                break;
            default:
                ch = "~";
        }

        return ch;
    }

}
