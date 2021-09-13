package ru.otus.homework.loaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@Service
public class LoaderCSV implements Loader {

    @Value("${upload-vehicle-set-file.skip-rows-count}")
    String skipRowsCount;

    @Override
    public List<String> loadVehiclePassangerUsedSets(MultipartFile file) {

        List<String> vehicleSetsFromFile = new ArrayList<>();

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner sсanner = new Scanner(inputStream).useDelimiter(Pattern.compile("\\r\\n|\\r|\\n"));

        Integer count = 0;

        try {
            while(sсanner.hasNext()) {

                String question = sсanner.hasNext() ? sсanner.next() : "";

                //пропускаем указанное количество строк
                if(count < Integer.valueOf(skipRowsCount)) {
                    count++;
                    continue;
                }

                vehicleSetsFromFile.add(question);
            }

            inputStream.close();
            sсanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vehicleSetsFromFile;
    }

}
