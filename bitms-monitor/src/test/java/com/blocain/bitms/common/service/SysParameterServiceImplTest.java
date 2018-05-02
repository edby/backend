package com.blocain.bitms.common.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.google.common.collect.Lists;

/**
 * SysParameterServiceImplTest Introduce
 * <p>File：SysParameterServiceImplTest.java</p>
 * <p>Title: SysParameterServiceImplTest</p>
 * <p>Description: SysParameterServiceImplTest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SysParameterServiceImplTest extends AbstractBaseTest
{
    @Autowired
    private SysParameterService sysParameterService;
    
    @Test
    public void batchUpdateTest() throws Exception
    {
        List<SysParameter> params = Lists.newArrayList();
        params.add(sysParameterService.selectByPrimaryKey(200000000001L));
        params.add(sysParameterService.selectByPrimaryKey(200000000002L));
        params.add(sysParameterService.selectByPrimaryKey(200000000003L));
        if (!CollectionUtils.isEmpty(params))
        {
            for (int i = 0; i < params.size(); i++)
            {
                params.get(i).setValue("yes");
            }
            sysParameterService.updateBatch(params, true);
        }

    }
}