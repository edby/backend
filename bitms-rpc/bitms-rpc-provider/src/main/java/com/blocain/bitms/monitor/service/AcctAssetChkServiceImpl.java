package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;
import com.blocain.bitms.monitor.mapper.MonitorEngineMapper;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**********************************************
 * 单账户资产检查
 * <p>File：AcctAssetChkServiceImpl.java</p>
 * <p>Title: AcctAssetChkServiceImpl</p>
 * <p>Description:AcctAssetChkServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年9月8日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class AcctAssetChkServiceImpl implements AcctAssetChkService
{
    public static final Logger  logger = LoggerFactory.getLogger(AcctAssetChkServiceImpl.class);
    
    private MonitorEngineMapper monitorEngineMapper;
    
    @Autowired
    public AcctAssetChkServiceImpl(MonitorEngineMapper monitorEngineMapper)
    {
        this.monitorEngineMapper = monitorEngineMapper;
    }
    
    @Override
    public AcctAssetChkResult doAcctAssetChk(Long accountId)
    {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        AcctAssetChkResult result = new AcctAssetChkResult();

        result.setReturnCode(0);
        result.setChekResult(1);

        logger.debug("===== 提现账户检查入参：accountId=" + accountId.toString());
        if (accountId != null && accountId > 0)
        {
            logger.debug("============== 执行提现账户资产检查存储过程 start ==============");
            paramMap.put("acctId", accountId.toString());
            monitorEngineMapper.doAcctAssetChk(paramMap);
            logger.debug("============== 执行提现账户资产检查存储过程 end ==============");
            result = parseResult(paramMap);
        }

        return result;
    }
    
    /********************
     * 解析存储过程返回值
     * @param resultMap   存储过程返回参数
     * @return
     */
    private AcctAssetChkResult parseResult(HashMap<String, Object> resultMap)
    {
        logger.debug("============== 解析存储过程返回值 start ==============");
        AcctAssetChkResult result = new AcctAssetChkResult();
        if (CollectionUtils.isEmpty(resultMap)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        Integer returnCode = (Integer) resultMap.get(MonitorConst.CHECK_RETURN_CODE);
        Integer chekResult = (Integer) resultMap.get(MonitorConst.CHECK_RETURN_CHEKRESULT);
        String chekMsg = (String) resultMap.get(MonitorConst.CHECK_RETURN_CHEKMSG);
        logger.debug("============== 解析存储过程返回值 end ==============");
        result.setReturnCode(returnCode);
        result.setChekResult(chekResult);
        result.setChekMsg(chekMsg);
        logger.debug("============== 返回值 ==============" + result.toString());
        return result;
    }
}
