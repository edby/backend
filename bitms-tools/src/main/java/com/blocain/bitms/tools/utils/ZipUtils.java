/*
 * @(#)ZipUtils.java 2015/3/13 13:30
 * Copyright 2015 吴万杰, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import com.blocain.bitms.tools.consts.CharsetConst;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.*;

/**
 * <p>File：ZipUtils.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015/3/13 13:30</p>
 * <p>Company: BloCain</p>
 *
 * @author 吴万杰
 * @version 1.0
 */
public class ZipUtils
{
    /**

     * 使用gzip进行压缩
     */
    @SuppressWarnings("restriction")
    public static String gzip(String primStr, String ... charSetStr)
    {
        if (primStr == null || primStr.length() == 0) { return primStr; }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try
        {
            gzip = new GZIPOutputStream(out);
            byte[] bytes = null;
            if (charSetStr == null || charSetStr.length == 0)
            {
                bytes = primStr.getBytes(CharsetConst.CHARSET_UT);
            }
            else
            {
                bytes = primStr.getBytes(charSetStr[0]);
            }
            gzip.write(bytes);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (gzip != null)
            {
                try
                {
                    gzip.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return new sun.misc.BASE64Encoder().encode(out.toByteArray()).replaceAll("\r\n", "").replaceAll("\n", "");
    }
    
    /**
     *
     * <p>Description:使用gzip进行解压缩</p>
     * @param compressedStr
     * @return
     */
    @SuppressWarnings("restriction")
    public static String gunzip(String compressedStr, String ... charSetStr)
    {
        if (compressedStr == null) { return null; }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try
        {
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1)
            {
                out.write(buffer, 0, offset);
            }
            if (charSetStr == null || charSetStr.length == 0)
            {
                decompressed = out.toString(CharsetConst.CHARSET_UT);
            }
            else
            {
                decompressed = out.toString(charSetStr[0]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ginzip != null)
            {
                try
                {
                    ginzip.close();
                }
                catch (IOException e)
                {
                }
            }
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return decompressed;
    }
    
    /**
     * 使用zip进行压缩
     * @param str 压缩前的文本
     * @return 返回压缩后的文本
     */
    @SuppressWarnings("restriction")
    public static final String zip(String str, String ... charSetStr)
    {
        if (str == null) return null;
        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;
        String compressedStr = null;
        try
        {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry("0"));
            byte[] bytes = null;
            if (charSetStr == null || charSetStr.length == 0)
            {
                bytes = str.getBytes(CharsetConst.CHARSET_UT);
            }
            else
            {
                bytes = str.getBytes(charSetStr[0]);
            }
            zout.write(bytes);
            zout.closeEntry();
            compressed = out.toByteArray();
            compressedStr = new sun.misc.BASE64Encoder().encodeBuffer(compressed);
        }
        catch (IOException e)
        {
            compressed = null;
        }
        finally
        {
            if (zout != null)
            {
                try
                {
                    zout.close();
                }
                catch (IOException e)
                {
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return compressedStr;
    }
    
    /**
     * 使用zip进行解压缩
     * @param compressedStr 压缩后的文本
     * @return 解压后的字符串
     */
    @SuppressWarnings("restriction")
    public static final String unzip(String compressedStr, String ... charSetStr)
    {
        if (compressedStr == null) { return null; }
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed = null;
        try
        {
            byte[] compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1)
            {
                out.write(buffer, 0, offset);
            }
            if (charSetStr == null || charSetStr.length == 0)
            {
                decompressed = out.toString(CharsetConst.CHARSET_UT);
            }
            else
            {
                decompressed = out.toString(charSetStr[0]);
            }
        }
        catch (IOException e)
        {
            decompressed = null;
        }
        finally
        {
            if (zin != null)
            {
                try
                {
                    zin.close();
                }
                catch (IOException e)
                {
                }
            }
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return decompressed;
    }
}
