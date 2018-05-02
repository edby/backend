package com.blocain.bitms.payment.btc.daemon.event;

import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.payment.btc.core.domain.Transaction;

import lombok.Getter;

/**An abstract adapter class for receiving {@code WALLET} notifications. Extend this class to 
 * override any methods of interest.*/
public abstract class WalletListener
{
    private static final Logger LOG = LoggerFactory.getLogger(WalletListener.class);
    
    @Getter
    private Observer            observer;
    
    public WalletListener()
    {
        observer = (monitor, cause) -> {
            Transaction transaction = (Transaction) cause;
            LOG.trace("-- update(..): forwarding incoming 'WALLET' notification to " + "'walletChanged(..)'");
            walletChanged(transaction);
        };
    }
    
    public void walletChanged(Transaction transaction)
    {
    }
}