package com.blocain.bitms;

import com.blocain.bitms.tools.utils.AmazonSESUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * AmazonSESUtilsTest Introduce
 * <p>Title: AmazonSESUtilsTest</p>
 * <p>File：AmazonSESUtilsTest.java</p>
 * <p>Description: AmazonSESUtilsTest</p>
 * <p>Copyright: Copyright (c) 2018/4/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AmazonSESUtilsTest {

    @Test
    public void sendMail() {
        AmazonSESUtils.sendMail("test","test","275625385@qq.com");
    }
}