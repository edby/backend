package com.blocain.bitms.monitor.service;
 
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;

/**********************************
 *  单账户资产检查
 * <p>File：AcctAssetChkService.java</p>
 * <p>Title: AcctAssetChkService</p>
 * <p>Description:AcctAssetChkService</p>
 * <p>Copyright: Copyright (c) 2017年9月8日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public interface AcctAssetChkService
{
    /***********************************
     * 账户资产检查
     * @param accountId    账户ID
     * @return AcctAssetChkResult 
     * (存储过程执行 returnCode：0 执行成功，-1 执行失败; 检查结果  chekResult 1.正常 ，小于0 异常; 提示信息 chekMsg)
     */
    AcctAssetChkResult doAcctAssetChk(Long accountId);
}
