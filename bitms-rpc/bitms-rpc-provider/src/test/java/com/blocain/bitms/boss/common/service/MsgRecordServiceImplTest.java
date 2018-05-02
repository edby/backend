package com.blocain.bitms.boss.common.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.basic.service.AbstractBaseTest;

/**
 * MsgRecordServiceImplTest Introduce
 * <p>File：MsgRecordServiceImplTest.java</p>
 * <p>Title: MsgRecordServiceImplTest</p>
 * <p>Description: MsgRecordServiceImplTest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class MsgRecordServiceImplTest extends AbstractBaseTest
{
    @Autowired
    MsgRecordNoSql msgRecordService;
    
    @Test
    public void sendEmailVerificationCode() throws Exception
    {

    }
}