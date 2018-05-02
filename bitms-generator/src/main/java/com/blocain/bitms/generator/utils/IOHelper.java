package com.blocain.bitms.generator.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * IO操作辅助类
 * <p>File：IOHelper.java</p>
 * <p>Title: IOHelper</p>
 * <p>Description:IOHelper</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class IOHelper
{
    /**
     * 拷贝文件
     * 
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(Reader in, Writer out) throws IOException
    {
        int c = -1;
        while ((c = in.read()) != -1)
        {
            out.write(c);
        }
    }
    
    /**
     * 读取文件
     * 
     * @param file
     * @return {@link String}
     * @throws IOException
     */
    public static String readFile(File file) throws IOException
    {
        Reader in = new FileReader(file);
        StringWriter out = new StringWriter();
        copy(in, out);
        return out.toString();
    }
    
    /**
     * 根据文件和字符内容生成文件
     * 
     * @param file
     * @param content
     * @throws IOException
     */
    public static void saveFile(File file, String content) throws IOException
    {
        Writer writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }
}
