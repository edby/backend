package com.blocain.bitms.payment.btc.daemon.event;

import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.payment.btc.core.domain.Block;

import lombok.Getter;

/**An abstract adapter class for receiving {@code BLOCK} notifications. Extend this class to 
 * override any methods of interest.*/
public abstract class BlockListener
{
    private static final Logger LOG = LoggerFactory.getLogger(BlockListener.class);
    
    @Getter
    private Observer            observer;
    
    public BlockListener()
    {
        observer = (monitor, cause) -> {
            Block block = (Block) cause;
            LOG.trace("-- update(..): forwarding incoming 'BLOCK' notification to " + "'blockDetected(..)'");
            blockDetected(block);
        };
    }
    
    public void blockDetected(Block block)
    {
    }
}