package ru.otus.homework.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Long> getUniqueIds(List<Long> ids) {

        List<Long> uniqueIds = new ArrayList<>();

        for(int j=0; j<ids.size(); j++) {
            Long checkId = ids.get(j);

            Boolean foundFlag = false;

            for(Long uniqueId : uniqueIds) {
                if(uniqueId.equals(checkId)) {
                    foundFlag = true;
                    break;
                }
            }

            if(!foundFlag) {
                uniqueIds.add(Long.valueOf(checkId));
            }
        }

        return uniqueIds;

    }

}
