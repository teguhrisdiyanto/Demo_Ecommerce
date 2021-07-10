package com.teguh.demoecomerce.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckUtil {
	
    public static Boolean isEmptyOrNull(String param) {
        return (param == null) || (param.equals(""));
    }

    public static boolean isNullOrEmpty(String param) {
        return (param == null) || (param.equals(""));
    }

    public static <E> boolean isNullOrEmpty(List<E> param) {
        return (param == null) || (param.size() == 0);
    }

    public static <E> boolean isNullOrEmpty(ArrayList<E> param) {
        return (param == null) || (param.size() == 0);
    }

    public static boolean isNullOrEmpty(Short temp) {
        return (temp == null);
    }

    public static boolean isNullOrEmpty(Integer param) {
        return param == null || param == 0;
    }

    public static boolean isNullOrEmpty(Double param) {
        return param == null || param == 0;
    }

    public static boolean isNullOrEmpty(Long param) {
        return param == null;
    }

    public static boolean isNullOrEmpty(BigDecimal param) {
        return param == null || param == BigDecimal.ZERO;
    }

    public static boolean isNullOrEmpty(Date date) {
        return date == null;
    }

    public static boolean isNullOrEmpty(Object object) {
        return object == null;
    }

    public static boolean isNullOrEmpty(Object[] object) {
        return object == null || object.length == 0;
    }

 

  

  
}