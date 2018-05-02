package com.blocain.bitms.boss.common.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
        params.add(sysParameterService.selectByPrimaryKey(200000000031L));
        params.add(sysParameterService.selectByPrimaryKey(200000000032L));

        if(!CollectionUtils.isEmpty(params))
        {
            for (int i = 0; i < params.size(); i++)
            {
                params.get(i).setValue("yes");
            }
            sysParameterService.updateBatch(params);
        }
    }
}