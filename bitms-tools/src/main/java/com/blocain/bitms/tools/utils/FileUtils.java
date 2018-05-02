/*
 * @(#)FileUtils.java 2014-6-19 下午7:44:51
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>File：FileUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-6-19 下午7:44:51</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguys
 * @version 1.0
 */
public class FileUtils
{
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    
    private FileUtils()
    {
        super();
    }
    
    /**
     * 将文件转成字节
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByte(String filename) throws IOException
    {
        File f = new File(filename);
        if (!f.exists()) { throw new FileNotFoundException(filename); }
        FileChannel fl = null;
        FileInputStream fs = null;
        try
        {
            fs = new FileInputStream(f);
            fl = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fl.size());
            fl.close();
            fs.close();
            return byteBuffer.array();
        }
        catch (IOException e)
        {
            logger.error("将文件转成字节失败", e.getCause());
            throw e;
        }
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
     * 排出SVN、CVS等资源文件
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
