package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import com.blocain.bitms.trade.fund.model.DebitAssetModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/19.
 */
public class AccountDebitAssetServiceImplTest extends AbstractBaseTest
{
    @Test
    public void debitSumData() throws Exception
    {
        AccountDebitAsset entity = new AccountDebitAsset();
        StockInfo searchEntity = new StockInfo();
        searchEntity.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> stockInfoList = stockInfoService.findList(searchEntity);
        String tables[] = new String[stockInfoList.size()];
        for(int i = 0 ; i < stockInfoList.size(); i ++)
        {
            StockInfo stockInfotemp = stockInfoList.get(i);
            tables[i] = stockInfotemp.getTableDebitAsset();
        }
        entity.setTableNames(tables);
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setRows(20);
        PaginateResult<AccountDebitAsset> result = accountDebitAssetService.debitSumData(pagination, entity);
    }

    @Autowired
    AccountDebitAssetService accountDebitAssetService;
    
    @Autowired
    StockInfoService         stockInfoService;

    /**
     * 自动计息
     * @throws Exception
     */
    @Test
    public void autoAccountDebitAssetInterest() throws Exception
    {
        accountDebitAssetService.autoAccountDebitAssetInterest();
    }
    
    /**
     * 借款转移给超级用户
     */
    @Test
    public void doDebtMoveToPlat() throws Exception
    {
        AccountDebitAsset borrowBtcDebitRecord = new AccountDebitAsset();
        borrowBtcDebitRecord.setStockinfoId(FundConsts.WALLET_USD_TYPE);
        borrowBtcDebitRecord.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        borrowBtcDebitRecord.setBorrowerAccountId(14241935732510720l);
        if (accountDebitAssetService.findList(borrowBtcDebitRecord).size() > 0)
        {
            borrowBtcDebitRecord = accountDebitAssetService.findList(borrowBtcDebitRecord).get(0);
            accountDebitAssetService.doDebtMoveToPlat(borrowBtcDebitRecord, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER);
        }
    }
    
    @Test
    /**
     * 测试自动借款
     */
    public void doDebitBorrowFromPlat() throws Exception
    {
        DebitAssetModel debitAssetModel = new DebitAssetModel();
        debitAssetModel.setBorrowerAccountId(300000060003L);
        // debitAssetModel.setStockinfoType(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY);
        // debitAssetModel.setDebitAmt(BigDecimal.valueOf(100));
        // debitAssetModel.setStockinfoId(FundConsts.WALLET_USC_TYPE);
        debitAssetModel.setDebitAmt(BigDecimal.valueOf(100));
        debitAssetModel.setStockinfoType(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH);
        debitAssetModel.setStockinfoId(FundConsts.WALLET_USD_TYPE);
        debitAssetModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        System.out.println("贷款准备=" + debitAssetModel.toString());
        debitAssetModel = accountDebitAssetService.doDebitBorrowFromPlat(debitAssetModel);
        // // 以下是杠杆现货借款测试
        // DebitAssetModel debitAssetModel = new DebitAssetModel();
        // debitAssetModel.setBorrowerAccountId(300000067890L);
        //// debitAssetModel.setDebitAmt(BigDecimal.valueOf(100));
        //// debitAssetModel.setStockinfoId(FundConsts.WALLET_USC_TYPE);
        // debitAssetModel.setDebitAmt(BigDecimal.valueOf(1));
        // debitAssetModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        // debitAssetModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        // System.out.println("贷款准备=" + debitAssetModel.toString());
        // debitAssetModel = accountDebitAssetService.doDebitBorrowFromPlat(debitAssetModel);
    }
    
    @Test
    /**
     * 测试自动还款
     */
    public void autoDebitRepaymentToPlat() throws Exception
    {
        accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, FundConsts.WALLET_USD_TYPE, FundConsts.WALLET_BTC2USD_TYPE,
                300000060003L);
        // accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY, FundConsts.WALLET_BTC2USDX_TYPE,
        // FundConsts.WALLET_BTC2USDX_TYPE, 300000067890L);
        // 借款STOCKINFOID 可用关联STOCKINFOID
        // accountDebitAssetService.autoDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY,FundConsts.WALLET_BTC2USDX_TYPE,FundConsts.WALLET_BTC2USDX_TYPE);
        // 借款STOCKINFOID 可用关联STOCKINFOID
        // accountDebitAssetService.autoDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH,FundConsts.WALLET_BTC_TYPE,FundConsts.WALLET_BTC2USDX_TYPE);
        // accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH,FundConsts.WALLET_BTC_TYPE,FundConsts.WALLET_USDZ2BTC_TYPE,FundConsts.WALLET_USDZ2BTC_TYPE);
    }
    
    @Test
    /**
     * 查询未结清的借款
     */
    public void findListForDebit() throws Exception
    {
        AccountDebitAsset r = new AccountDebitAsset();
        r.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        r.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        List list = accountDebitAssetService.findListForDebit(r);
        System.out.println(list.size());
    }
    
    @Test
    public void findListByIds() throws Exception
    {
        String id = "1,2";
        accountDebitAssetService.findListByIds(id);
    }
    
    /**
     * 交割过程中强制还款后的债务转移
     * @return
     */
    @Test
    public void settlementDebitMoveToPlatAfterPowerRepay() throws Exception
    {
        accountDebitAssetService.settlementDebitMoveToPlatAfterPowerRepay(FundConsts.WALLET_BTC2USDX_TYPE, FundConsts.WALLET_BTC_TYPE, FundConsts.SYSTEM_ACCOUNT_ID);
    }
    
    /**
     * 按条件查找总负债
     * @throws Exception
     */
    @Test
    public void findSumDebitAmt() throws Exception
    {
        StockInfo stockInfo = getStockInfo(FundConsts.WALLET_BTC2USD_TYPE);
        AccountDebitAsset recordEntity = new AccountDebitAsset();
        recordEntity.setStockinfoId(FundConsts.WALLET_USD_TYPE);// 法定货币
        recordEntity.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());// 法定货币
        recordEntity.setTableName(stockInfo.getTableDebitAsset());
        BigDecimal sumDebitAmt = accountDebitAssetService.findSumDebitAmt(recordEntity);
        System.out.println(sumDebitAmt);
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}