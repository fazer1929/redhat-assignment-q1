import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ShowChart extends ApplicationFrame {
    public ShowChart(String title, HashMap<String ,Integer> ct) {
        super(title);
        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                "Words",
                "Percentage",
                createDataset(ct),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel( barChart );
        chartPanel.setPreferredSize(new java.awt.Dimension( 900 , 567 ) );
        setContentPane( chartPanel );
    }

    /**
     * Creates Dataset to be used in a Bar Graph
     * @param map A Hashmap containing words with their counts
     * @return    CategoryDataset used to create a graph
     */
    public static CategoryDataset createDataset(HashMap<String,Integer> map){
        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();
        final int THRESHOLD = 150;
        final String category = "Percentage in Considered Words (Threshold>"+THRESHOLD+")";

        int consideredWords =0;

        for(String key:map.keySet()){
            if(map.get(key)>THRESHOLD){
                consideredWords+= map.get(key);
            }
        }
        for(String key:map.keySet()){
            if(map.get(key)>THRESHOLD) {
                dataset.addValue((getPercentage(map.get(key),consideredWords)),category,key);

            }
        }
        return dataset;
    }

    /**
     * Downloads content from a remote URL and stores it locally
     * @param url A URL object from where the file is to be downloaded
     * @param fileName A String containing the name of file to put the donloaded content
     */
    public static void download(URL url, String fileName){
        try {
            InputStream in = url.openStream();
            Files.copy(in, Paths.get(fileName));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Returns @param1 % @param2
     * @return      a double after calculating the percentage
     */
    public static double getPercentage(int val,int total){
        return (val/(double)total)*100;
    }

    /**
     * Counts the number of occurences of every word in a given file
     * @param count A HashMap to keep the count of number of occurences of words
     * @param fileName A String containing the name of file to be read
     * @throws Exception
     */
    public static void countNums(HashMap<String,Integer> count,String fileName) throws Exception{

        BufferedReader br  = new BufferedReader(new FileReader(fileName));
        String s ;int c=0;
        while((s = br.readLine())!=null){
            String [] data = s.split(" ");
            for(String word:data){
                String w = word.toLowerCase().strip();
                if(w.equals(" ") || w.equals(""))continue;
                c++;
            count.put(w,count.getOrDefault(w,0)+1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Create URL object by the given link
        URL url = new URL("https://www.gutenberg.org/files/2264/2264.txt");
        String fileName = "words.txt";

        //Downloads the file and saves it locally
        download(url,fileName);

        HashMap<String ,Integer> map = new HashMap<>();

        //Counts the occurence of every word
        countNums(map,fileName);
        System.out.println("The word count of every word is");
        //Display the word along with their count
        for(String key:map.keySet()){
            System.out.println(key+" : "+map.get(key));
        }

        //Creating the word percentage chart and then displaying it
        ShowChart chart = new ShowChart("Word Percentage Graph",map);
        chart.pack();
        chart.setVisible( true );

        //Deleting the locally Created file after everything is finished
        File file = new File(fileName);
        file.delete();
    }
}
