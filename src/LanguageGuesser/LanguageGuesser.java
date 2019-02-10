package LanguageGuesser;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.io.File;

public class LanguageGuesser {
    File englishText;
    File frenchText;
    File spanishText;
    String eng;
    String fren;
    String span;
    Scanner sc;
    double[][] engMat;
    double[][] frenMat;
    double[][] spanMat;
    public LanguageGuesser(){
        englishText = new File("tests/English.txt");
        System.out.println("Training for English... ");
        eng = parseTxt(englishText);
        engMat = initMatrix(eng);
        //System.out.println("English: " + eng);

        frenchText = new File("tests/French.txt");
        System.out.println("Training for French... ");
        fren = parseTxt(frenchText);
        frenMat = initMatrix(fren);
        //System.out.println("French: " + fren);

        spanishText = new File("tests/Spanish.txt");
        System.out.println("Training for Spanish... \n");
        span = parseTxt(spanishText);
        spanMat = initMatrix(span);
        //System.out.println("Spanish: " + span);


    }

    public String parseTxt(File file){
        String s = "";
        try{
            sc = new Scanner(file);
            while (sc.hasNextLine()){
                s += sc.nextLine() + " ";
            }
            sc.close();
            s = s.replaceAll("[^\\p{L}]", "").toLowerCase();


        } catch(IOException e){
            System.out.println("File does not exist.");
        }
        return s;
    }
    public double[][] initMatrix(String txt){
        double [][]counts = new double[46][46];
        for (int i = 0; i < 46; i++){
            for (int j = 0; j < 46; j++){
                counts[i][j]= 0;
            }
        }
        for(int i = 0; i < txt.length() - 1; i++){
            try {
                counts[val(txt.charAt(i))][val(txt.charAt(i + 1))] += 1;
            } catch(ArrayIndexOutOfBoundsException e){

            }
        }
        for (int i = 0; i < 45; i++){
            for (int j = 0; j < 45; j++){
                counts[i][45]+=counts[i][j];
            }
        }
        for (int i = 0; i < 45; i++){
            for (int j = 0; j < 45; j++){
                counts[45][i]+=counts[j][i];
            }
        }
        return counts;
    }
    //    public int[][] updateMatrix(int[][] mat, char a, char b){
//        mat[(int)a][(int)b] +=1;        matrix[1][0]
//        return mat;
//    }
    public int val(char c){
        char[] chars = {'à', 'á', 'â', 'ç', 'è', 'é', 'ê', 'ë', 'í', 'î', 'ï', 'ñ', 'ó', 'ô', 'ù',
                'ú', 'û', 'ü', 'œ'};
        int value = (int)c;
        if (value > 122){
            for (int i = 0; i < chars.length; i++){
                if (value == (int)chars[i]){
                    value = 26 + i;
                    return value;
                }
            }
            return value;
        } else{
            value -= 97;
            return value;
        }
    }
    public String prob(String s) {
        double engCount = 100000;
        double frenCount = 100000;
        double spanCount = 100000;
        s = s.replaceAll("[^\\p{L}]", "").toLowerCase();
        System.out.println("");
        for (int i = 0; i < s.length() - 1; i++) {
            //System.out.println((engMat[val(s.charAt(i))][val(s.charAt(i + 1))]) / (engMat[val(s
            //.charAt(i))][45]));
            engCount *= (engMat[val(s.charAt(i))][val(s.charAt(i + 1))]) / (engMat[val(s.charAt(i))
                    ][45]);
            // System.out.println(engCount);
        }
        for (int i = 0; i < s.length() - 1; i++) {
            frenCount *= (frenMat[val(s.charAt(i))][val(s.charAt(i + 1))]) / (frenMat[val(s.charAt
                    (i))
                    ][45]);
        }
        for (int i = 0; i < s.length() - 1; i++) {
            spanCount *= (spanMat[val(s.charAt(i))][val(s.charAt(i + 1))]) / (spanMat[val(s.charAt
                    (i))
                    ][45]);
        }
        double totalCount = engCount + frenCount + spanCount;
        System.out.println("The probability of English is: " + Math.floor((engCount/totalCount)
                *1000)/1000);
        System.out.println("The probability of French is: " + Math.floor((frenCount/totalCount)
                *1000)/1000);
        System.out.println("The probability of Spanish is: " + Math.floor((spanCount/totalCount)
                *1000)/1000);
        if (engCount == frenCount && frenCount == spanCount){
            return "error";
        }else if (Math.max(engCount, Math.max(frenCount, spanCount)) == engCount){
            return "English";
        } else if (Math.max(engCount, Math.max(frenCount, spanCount)) == frenCount){
            return "French";
        } else{
            return "Spanish";
        }
    }
    public static void main(String [] args){
        LanguageGuesser lang = new LanguageGuesser();
        Scanner input = new Scanner(System.in);
        //System.out.println(lang.val(lang.fren.charAt(4)));
//        for(int i = 0; i < 46; i++){
//            for (int j = 0; j < 46; j++){
//                System.out.print(lang.engMat[i][j]);
//                for (int k = Double.toString(lang.engMat[i][j]).length(); k < 5; k++){
//                    System.out.print(" ");
//                }
//            }
//            System.out.println("");
//        }
        System.out.print("Please enter a sentence or phrase: ");
        while (true) {
            String in = input.nextLine();
            if (in.equals("0")){
                System.exit(0);
            } else {
                String s = lang.prob(in);
                if (s.equals("error")) {
                    System.out.println("Unable to detect language.");
                } else {
                    System.out.println("\nThe language is most likely " + s + ".");
                }
                System.out.print("Please enter another sentence or phrase: ");
            }
        }
    }
}
