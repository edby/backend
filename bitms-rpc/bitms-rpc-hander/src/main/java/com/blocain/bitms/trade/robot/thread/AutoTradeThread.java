package com.blocain.bitms.trade.robot.thread;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.trade.robot.consts.TradeConst;
import com.blocain.bitms.trade.robot.entity.Order;
import com.blocain.bitms.trade.robot.entity.RobotModel;
import com.blocain.bitms.trade.robot.service.GridRobotService;

public class AutoTradeThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AutoTradeThread.class);

    private boolean isRunning = true;

    private RobotModel robot;

    private Date startTime;

    private GridRobotService gridRobotService;

    public void run() {
        try {
            logger.info("======启动下单机器人,账号id:" + robot.getParam().getAccountId());
            try {

                // 首次启动，清空订单
                gridRobotService.cancelAllOrders(robot);
                // 打开借贷开关
                gridRobotService.changeBorrowSwitch(robot.getParam().getAccountId(), 1);
                // 初始化缓存基准价
                robot.getParam().basePrice = gridRobotService.calBasePrice(robot);
            } catch (Exception e) {
                logger.error("下单循环体外部异常：" + e.getMessage());


            }
            while (isRunning) {
                try {
                    // 更新下单开关
                    robot = gridRobotService.updateTradeSwitch(robot);
                    // LogUtil.info(logger, "买单开关：" + buySwitch + ",卖单开关：" + sellSwitch);
                    if ((!robot.getBuySwitch()) && (!robot.getSellSwitch())) {
                        // 如果买单，卖单开关都为关闭状态，则跳过本次循环
                        return;
                    }
                    // 获取最新基准价
                    BigDecimal basePrice = gridRobotService.calBasePrice(robot);
                    if (basePrice == BigDecimal.ZERO) {
                        logger.error("基准价为0，本次交易取消");
                        return;
                    }
                    //内部机器人和外部机器人下单逻辑不一样
                    if (robot.getParam().getRobotType() == 1) {

                    } else {


                    }

                    // logger.info("最新基准价：" + basePrice.toString() + ",缓存基准价：" + RobotCache.basePrice);
                    // 比较最新基准价与缓存基准价价差幅度，以便确定是否需要撤单和补单
                    int range = basePrice.subtract(robot.getParam().basePrice).divide(robot.getParam().getAvgUpdateLimit(), 0, BigDecimal.ROUND_FLOOR).intValue();
                    // LogUtil.info(logger, "最新基准价与缓存基准价价差与阈值比例截位取整值：" + range);
                    // 获取卖单列表和买单列表
                    List<Order> buyOrders = gridRobotService.findOrders(robot, TradeConst.SPOTBUY);
                    List<Order> sellOrders = gridRobotService.findOrders(robot, TradeConst.SPOTSELL);
                    logger.debug("买单当前数量：" + buyOrders.size() + ",卖单当前数量：" + sellOrders.size());
                    if (range >= 1) {
                        // 基准价提高并超出阈值，更新缓存基准价：
                        robot.getParam().basePrice = basePrice;
                        //若买单开关开启，则撤低挂高,先撤后挂
                        if (robot.getBuySwitch()) {
                            // 批量撤买单,(买卖单一律按下单价从高到低排序)
                            // 实际撤单数量：
                            int buyCanceled = 0;
                            for (int i = 0; i < range && i < buyOrders.size(); i++) {
                                gridRobotService.cancelOrder(robot, buyOrders.get(buyOrders.size() - 1 - i));
                                buyCanceled++;
                            }
                            // LogUtil.info(logger, "本次买单撤单数量：" + buyCanceled);
                            // 批量挂买单
                            for (int i = 0; i < robot.getParam().getMaxOrderSize() - buyOrders.size() + buyCanceled && i < robot.getParam().getMaxOrderSize()
                                    && i < robot.getPair().getMaxOrderSize_plat(); i++) {
                                BigDecimal price = gridRobotService.calOrderPrice(robot, basePrice, i, TradeConst.SPOTBUY);
                                BigDecimal amt = gridRobotService.calOrderAmt(robot);
                                gridRobotService.doBuy(robot, price, amt);
//                                logger.info("盘口补买单：买入价：" + price + ",买入数量：" + amt);
                            }
                        }
                        // 若卖单开关开启，撤符合条件卖单
                        if (robot.getSellSwitch()) {
                            for (int i = 0; i < sellOrders.size(); i++) {
                                Order order = sellOrders.get(sellOrders.size() - 1 - i);
                                // m:最低价卖单-最新基准价
                                BigDecimal m = order.getPrice().subtract(basePrice);
                                if (m.compareTo(robot.getParam().getCancelLimit()) == -1) {
                                    // 撤单
                                    gridRobotService.cancelOrder(robot, order);
                                } else {
                                    break;
                                }
                            }
                        }
                    } else if (-1 < range && range < 1) {
                        // 基准价波动但是未超出阈值，需判断基准价与盘口价差是否超出撤单临界值，超出时仍需撤盘口单,
                        if (robot.getBuySwitch()) {
                            // 买单撤单
                            for (int i = 0; i < buyOrders.size(); i++) {
                                Order order = buyOrders.get(i);
                                // m:最新基准价-最高价买单
                                BigDecimal m = basePrice.subtract(order.getPrice());
                                if (m.compareTo(robot.getParam().getCancelLimit()) == -1) {
                                    // 撤单
                                    gridRobotService.cancelOrder(robot, order);
                                } else {
                                    break;
                                }
                            }
                        }
                        if (robot.getSellSwitch()) {
                            // 卖单撤单
                            for (int i = 0; i < sellOrders.size(); i++) {
                                Order order = sellOrders.get(sellOrders.size() - 1 - i);
                                // m:最低价卖单-最新基准价
                                BigDecimal m = order.getPrice().subtract(basePrice);
                                if (m.compareTo(robot.getParam().getCancelLimit()) == -1) {
                                    // 撤单
                                    gridRobotService.cancelOrder(robot, order);
                                } else {
                                    break;
                                }
                            }
                        }
                    } else {
                        // 基准价降低并超出阈值，更新缓存基价
                        robot.getParam().basePrice = basePrice;
                        // 若买单开关开启，撤符合条件的买单
                        if (robot.getBuySwitch()) {
                            for (int i = 0; i < buyOrders.size(); i++) {
                                Order order = buyOrders.get(i);
                                // m:最高价买单-最新基准价
                                BigDecimal m = basePrice.subtract(order.getPrice());
                                if (m.compareTo(robot.getParam().getCancelLimit()) == -1) {
                                    // 撤单
                                    gridRobotService.cancelOrder(robot, order);
                                } else {
                                    break;
                                }
                            }
                        }
                        // 若卖单开关开启，则撤高挂低，先撤后挂
                        if (robot.getSellSwitch()) {
                            // 批量撤卖单,(买卖单一律按下单价从高到低排序)
                            // 实际撤单数量
                            int sellCanceled = 0;
                            for (int i = 0; i < -range && i < sellOrders.size(); i++) {
                                gridRobotService.cancelOrder(robot, sellOrders.get(sellOrders.size() - 1 - i));
                                sellCanceled++;
                            }
                            // LogUtil.info(logger, "本次卖单撤单数量：" + sellCanceled);
                            // 批量挂卖单
                            for (int i = 0; i < robot.getParam().getMaxOrderSize() - sellOrders.size() + sellCanceled && i < robot.getParam().getMaxOrderSize()
                                    && i < robot.getPair().getMaxOrderSize_plat(); i++) {
                                BigDecimal price = gridRobotService.calOrderPrice(robot, basePrice, i, TradeConst.SPOTSELL);
                                BigDecimal amt = gridRobotService.calOrderAmt(robot);
                                gridRobotService.doSell(robot, price, amt);
//                                logger.debug("盘口补卖单：卖出价：" + price + ",卖出数量：" + amt);
                            }
                        }
                    }
                    /**
                     * 统一补单：挂高价卖单，低价买单(远离盘口单)
                     */
                    if (robot.getBuySwitch()) {
                        List<Order> buyOrders_cur = gridRobotService.findOrders(robot, TradeConst.SPOTBUY);
                        for (int i = 0; i < robot.getParam().getMaxOrderSize() - buyOrders_cur.size() && i < robot.getPair().getMaxOrderSize_plat(); i++) {
                            BigDecimal price = gridRobotService.calOrderPrice(robot, basePrice, robot.getParam().getMaxOrderSize() - 1 - i, TradeConst.SPOTBUY);
                            BigDecimal amt = gridRobotService.calOrderAmt(robot);
                            gridRobotService.doBuy(robot, price, amt);
//                            logger.debug("远盘口补单：买入价：" + price + ",买入数量：" + amt);
                        }
                    }
                    if (robot.getSellSwitch()) {
                        List<Order> sellOrders_cur = gridRobotService.findOrders(robot, TradeConst.SPOTSELL);
                        for (int i = 0; i < robot.getParam().getMaxOrderSize() - sellOrders_cur.size() && i < robot.getPair().getMaxOrderSize_plat(); i++) {
                            BigDecimal price = gridRobotService.calOrderPrice(robot, basePrice, robot.getParam().getMaxOrderSize() - 1 - i, TradeConst.SPOTSELL);
                            BigDecimal amt = gridRobotService.calOrderAmt(robot);
                            gridRobotService.doSell(robot, price, amt);
//                            logger.debug("远盘口补单：卖出价：" + price + ",卖出数量：" + amt);
                        }
                    }
                    /**
                     * 动态效果实现:只适用于内部账号
                     * 随机撤单并挂单，只改变价格数量的小数位
                     * 只动态替换盘口挂单，买1到买5（预留可配置）
                     */

                    if (robot.getParam().getRobotType() != null && robot.getParam().getRobotType() == 1) {
                        if (robot.getBuySwitch()) {
                            List<Order> buyOrders_dyn = gridRobotService.findOrders(robot, TradeConst.SPOTBUY);
                            for (int i = 0; i < buyOrders_dyn.size() && i < robot.getPair().getDynCount(); i++) {
                                int ran = (int) (Math.random() * 100);
                                // 如果中奖就撤本单，否则跳过撤本单
                                if (ran < robot.getPair().getDynRandom()) {
                                    Order order = buyOrders_dyn.get(i);
                                    BigDecimal oldPrice = order.getPrice();
                                    gridRobotService.cancelOrder(robot, order);
                                    BigDecimal amt = gridRobotService.calOrderAmt(robot);
                                    BigDecimal newPrice = gridRobotService.replacePrice(robot, oldPrice);
                                    gridRobotService.doBuy(robot, newPrice, amt);
                                    logger.debug("新旧价格：" + newPrice + "," + oldPrice);
                                } else {
                                    continue;
                                }
                            }
                        }
                        if (robot.getSellSwitch()) {
                            List<Order> sellOrders_dyn = gridRobotService.findOrders(robot, TradeConst.SPOTSELL);
                            for (int i = 0; i < sellOrders_dyn.size() && i < robot.getPair().getDynCount(); i++) {
                                int ran = (int) Math.random() * 100;
                                // 如果中奖就撤本单，否则跳过撤本单
                                if (ran < robot.getPair().getDynRandom()) {
                                    Order order = sellOrders_dyn.get(sellOrders_dyn.size() - 1 - i);
                                    BigDecimal oldPrice = order.getPrice();
                                    gridRobotService.cancelOrder(robot, order);
                                    BigDecimal amt = gridRobotService.calOrderAmt(robot);
                                    BigDecimal newPrice = gridRobotService.replacePrice(robot, oldPrice);
                                    gridRobotService.doSell(robot, newPrice, amt);
                                    logger.debug("新旧价格：" + newPrice + "," + oldPrice);
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                    Thread.sleep(robot.getPair().getDt());
                } catch (Exception e) {
                    logger.error("循环体异常");
                    e.printStackTrace();
                }
            }
            logger.info("======下单机器人正常关闭,账号id:" + robot.getParam().getAccountId());
        } catch (Exception e) {
            logger.error("======下单机器人异常关闭,账号id:" + robot.getParam().getAccountId());
            e.printStackTrace();
        } finally {
            // 循环结束，清空订单
            gridRobotService.cancelAllOrders(robot);
        }
        logger.info("======退出机器人线程======");
    }

    public void setRobot(RobotModel robot) {
        this.robot = robot;
    }

    public GridRobotService getGridRobotService() {
        return gridRobotService;
    }

    public void setGridRobotService(GridRobotService gridRobotService) {
        this.gridRobotService = gridRobotService;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public RobotModel getRobot() {
        return robot;
    }
}
