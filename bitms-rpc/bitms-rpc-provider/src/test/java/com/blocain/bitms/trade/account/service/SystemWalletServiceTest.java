/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.service.BlockTransConfirmService;
import com.blocain.bitms.trade.fund.service.SystemWalletAddrService;
import com.blocain.bitms.trade.fund.service.SystemWalletService;

/**
 * <p>File：SystemWalletServiceTest.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月10日 上午9:56:58</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class SystemWalletServiceTest extends AbstractBaseTest
{
    @Autowired
     SystemWalletService systemWalletService;
    
    @Autowired
     SystemWalletAddrService systemWalletAddrService;
    
    @Autowired
     BlockTransConfirmService blockTransConfirmService;
    
    // 创建钱包
//    @Test
//    public void createWalletTest() {
//        try {
//            systemWalletService.createBtWallet("wallet6", "shihome");
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
    
//    // 创建钱包地址
//    @Test
//    public void createWalletAddressTest() {
//        try {
//            systemWalletAddrService.createBtWalletAddress("wallet6", "884225451885596672", "shihome");
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 创建钱包交易对应的区块交易确认结果信息
//    @Test
//    public void createWalletTransRecord() {
//        try {
//            blockTransConfirmService.createWalletTransRecord("2N64kCuTtMnHiWb2zTsCkjZfN2ni9mutMUD", "7e40666c6cd75aa7efe9a638fb498367703e0f3d0e35d9d23dd0ec2613ee7955");
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
