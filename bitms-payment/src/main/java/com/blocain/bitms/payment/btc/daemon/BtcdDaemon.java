package com.blocain.bitms.payment.btc.daemon;

import java.util.Properties;

import com.blocain.bitms.payment.btc.daemon.event.AlertListener;
import com.blocain.bitms.payment.btc.daemon.event.BlockListener;
import com.blocain.bitms.payment.btc.daemon.event.WalletListener;

public interface BtcdDaemon
{
    void addAlertListener(AlertListener listener);
    
    int countAlertListeners();
    
    void removeAlertListener(AlertListener listener);
    
    void removeAlertListeners();
    
    void addBlockListener(BlockListener listener);
    
    int countBlockListeners();
    
    void removeBlockListener(BlockListener listener);
    
    void removeBlockListeners();
    
    void addWalletListener(WalletListener listener);
    
    int countWalletListeners();
    
    void removeWalletListener(WalletListener listener);
    
    void removeWalletListeners();
    
    boolean isMonitoring(Notifications notificationType);
    
    boolean isMonitoringAny();
    
    boolean isMonitoringAll();
    
    Properties getNodeConfig();
    
    void shutdown();
}