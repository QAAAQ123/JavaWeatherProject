package src.server;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoordinateToRegionMapper {


    public List<List<String>> readCSV() {
        List<List<String>> regionAndCoordinate = new ArrayList<>();

        try {
            File file = new File("src/Location_Name_by_Coordinates.CSV");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "EUC-KR"));
            String line;

            while((line = br.readLine()) != null) {
                List<String> regionList;
                List<String> coordinateList;
                String[] lineArr = line.split(",");
                regionList = Arrays.asList(lineArr[2],lineArr[3],lineArr[4]);
                coordinateList = Arrays.asList(lineArr[5],lineArr[6]);
                regionAndCoordinate.add(regionList);
                regionAndCoordinate.add(coordinateList);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return regionAndCoordinate;
    }

    public List<String> getRegionList(){
        List<List<String>> origin = readCSV();
        List<String> regionList = new ArrayList<>();
        for(int i = 0; i < origin.size();i +=2 )
            regionList.addAll(origin.get(i));
        return regionList;
    }

    public List<String> getCoordinateList(){
        List<List<String>> origin = readCSV();
        List<String> coordinateList = new ArrayList<>();
        for(int i = 1; i< origin.size(); i += 2)
            coordinateList.addAll(origin.get(i));
        return coordinateList;
    }
}
