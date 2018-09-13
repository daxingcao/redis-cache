package com.caodaxing.redis.utils;

import java.util.regex.Pattern;

public class StringUtil {
	
	public static boolean isNumber(String str) {
		if("".equals(str.trim())) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+|[-\\+]?[\\d]*.[\\d]+$");
	    return pattern.matcher(str).matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isNumber("3"));
		System.out.println(isNumber("3."));
		System.out.println(isNumber("3.2"));
		System.out.println(isNumber("3.2334"));
		System.out.println(isNumber("-3"));
	}

}
