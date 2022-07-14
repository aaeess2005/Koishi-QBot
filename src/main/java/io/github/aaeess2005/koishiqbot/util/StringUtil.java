package io.github.aaeess2005.koishiqbot.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static String[] separateString(String text,String separator){
        List<String> subText=new ArrayList<>();
        int i=0;
        String tmp=text;
        while (tmp.contains(separator)) {
            subText.add(tmp.substring(0,tmp.indexOf(separator)));
            i=tmp.indexOf(separator)+separator.length();
            tmp=tmp.substring(i);
        }
        subText.add(tmp);
        return subText.toArray(new String[0]);
    }
}
