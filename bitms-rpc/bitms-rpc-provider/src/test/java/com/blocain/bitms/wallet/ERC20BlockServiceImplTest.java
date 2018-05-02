package com.blocain.bitms.wallet;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;
import org.junit.Test;
import com.blocain.bitms.trade.block.service.BlockInfoERC20Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Playguy on 2017/6/23.
 */
public class ERC20BlockServiceImplTest extends AbstractBaseTest
{
    @Autowired
    private BlockInfoERC20Service blockInfoERC20Service;

    @Autowired
    private ERC20BlockService eRC20BlockService;

    @Test
    public void doSyncBlockNumberTest() throws Exception
    {
        eRC20BlockService.doSyncBlockNumber();
    }

    @Test
    public void doScanERC20BlockTest() throws Exception
    {
        BlockInfoERC20 BlockInfoERC20 = new BlockInfoERC20();
        BlockInfoERC20.setHeight(2836986L);

        eRC20BlockService.doScanERC20Block(blockInfoERC20Service.findList(BlockInfoERC20).get(0));
    }

    @Test
    public void scanERC20TransTest() throws Exception
    {
        eRC20BlockService.scanERC20Trans();
    }

    @Test
    public void collectERC20BalanceTest() throws Exception
    {
        while(true){
            eRC20BlockService.collectERC20Balance();
            Thread.sleep(30000);
        }
    }

}