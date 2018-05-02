package com.blocain.bitms.generator.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作帮助类
 * <p>File：FileHelper.java</p>
 * <p>Title: FileHelper</p>
 * <p>Description:FileHelper</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class FileHelper
{
    /**
     * 取文件的绝对地址
     * 
     * @param baseDir
     * @param file
     * @return {@link String}
     */
    public static String getRelativePath(File baseDir, File file)
    {
        if (baseDir.equals(file)) { return ""; }
        if (baseDir.getParentFile() == null) { return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length()); }
        String relativePath = file.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1);
        return relativePath;
    }
    
    /**
     * 将文件（文件夹下的文件）加入到集合
     * 
     * @param file
     * @param collector
     * @throws IOException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void listFiles(File file, List collector) throws IOException
    {
        collector.add(file);
        if ((!file.isHidden() && file.isDirectory()) && !isIgnoreFile(file))
        {
            File[] subFiles = file.listFiles();
            for (File subFile : subFiles)
            {
                listFiles(subFile, collector);
            }
        }
    }
    
    /**
     * 排出SVN、CVN等资源文件
     * 
     * @param file
     * @return {@link Boolean}
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static boolean isIgnoreFile(File file)
    {
        List ignoreList = new ArrayList();
        ignoreList.add(".svn");
        ignoreList.add("CVS");
        ignoreList.add(".svn");
        ignoreList.add("SVN");
        for (int i = 0; i < ignoreList.size(); i++)
        {
            if (file.getName().equals(ignoreList.get(i))) { return true; }
        }
        return false;
    }
}
