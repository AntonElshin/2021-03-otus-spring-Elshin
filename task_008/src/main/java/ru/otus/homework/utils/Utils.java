package ru.otus.homework.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Long> getUniqueIds(String idStr) {

        List<Long> uniqueIds = new ArrayList<>();

        String[] ids = idStr.trim().split(",");

        for(int j=0; j<ids.length; j++) {
            String idString = ids[j].trim();

            if(idString.trim().isEmpty()) {
                continue;
            }

            Long id = Long.valueOf(idString);

            if(id == null) {
                continue;
            }

            Boolean foundFlag = false;

            for(Long uniqueId : uniqueIds) {
                if(uniqueId.equals(id)) {
                    foundFlag = true;
                    break;
                }
            }

            if(!foundFlag) {
                uniqueIds.add(Long.valueOf(id));
            }
        }

        return uniqueIds;

    }

}
