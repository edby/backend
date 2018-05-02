/*
 * @(#)Merge.java 2017年7月19日 下午5:29:13
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>File：StringMerge.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月19日 下午5:29:13</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class StringMerge
{
    public static String processString(String str1, String str2) {  
        String str = str1 + str2;// 合并字符串  
        List<Character> list_odd = new ArrayList<Character>();  
        List<Character> list_even = new ArrayList<Character>();  
        //按照奇数位偶数位分割  
        for (int i = 0; i < str.length(); i++) {  
            if (i % 2 == 0) {  
                list_even.add(str.charAt(i));  
            } else {  
                list_odd.add(str.charAt(i));  
            }  
        }  
  
        //对分割后的分别排序  
        Collections.sort(list_odd);  
        Collections.sort(list_even);  
  
        //重新合并  
        char[] array1 = new char[str.length()];  
        int j = 0, k = 0;  
        for (int i = 0; i < array1.length; i++) {  
            if (i % 2 == 0) {  
                array1[i] = list_even.get(j++);  
            } else {  
                array1[i] = list_odd.get(k++);  
            }  
        }  
  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < array1.length; i++) {  
            //对每一位进行转换，此处使用数表转换  
            sb.append(change1(array1[i]));  
        }  
        return sb.toString();  
    }
    
    private static char change1(char a) {  
        char res;  
        switch (a) {  
        case '0':  
            res = '0';  
            break;  
        case '1':  
            res = '8';  
            break;  
        case '2':  
            res = '4';  
            break;  
        case '3':  
            res = 'C';  
            break;  
        case '4':  
            res = '2';  
            break;  
        case '5':  
            res = 'A';  
            break;  
        case '6':  
            res = '6';  
            break;  
        case '7':  
            res = 'E';  
            break;  
        case '8':  
            res = '1';  
            break;  
        case '9':  
            res = '9';  
            break;  

        case 'A':  
            res = '5';  
            break;  
        case 'B':  
            res = 'D';  
            break;  
        case 'C':  
            res = '3';  
            break;  
        case 'D':  
            res = 'B';  
            break;  
        case 'E':  
            res = '7';  
            break;  
        case 'F':  
            res = 'F';  
            break;  
  
        case 'a':  
            res = '5';  
            break;  
        case 'b':  
            res = 'D';  
            break;  
        case 'c':  
            res = '3';  
            break;  
        case 'd':  
            res = 'B';  
            break;  
        case 'e':  
            res = '7';  
            break;  
        case 'f':  
            res = 'F';  
            break;  
        default:  
            res = a;  
        }  
        return res;  
    } 
    
    public static void main(String[] args) {  
        String str1 = "3465354634";
        String str2 = "awefawefersgsregsergergerg235423safs";
        System.out.println(processString(str1, str2));   
    } 
}
