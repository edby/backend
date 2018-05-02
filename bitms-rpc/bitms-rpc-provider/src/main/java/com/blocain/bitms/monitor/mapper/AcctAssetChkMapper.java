package com.blocain.bitms.monitor.mapper;

import java.util.HashMap;

import com.blocain.bitms.monitor.entity.AcctAssetChkResult; 
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper; 

/**
 * 提现账户资产检查接口
 * <p>File：WithDrawChkMapper.java </p>
 * <p>Title: WithDrawChkMapper </p>
 * <p>Description:WithDrawChkMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author jiangsc
 * @version 1.0
 */
@MyBatisDao
public interface AcctAssetChkMapper extends GenericMapper<AcctAssetChkResult>
{
    //提现资产检查
    void doAcctAssetChk(HashMap<String, Object> paramMap);
}
