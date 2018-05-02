package com.blocain.bitms.payment.btc.daemon.notification.worker;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.payment.btc.core.BitcoindException;
import com.blocain.bitms.payment.btc.core.CommunicationException;
import com.blocain.bitms.payment.btc.core.client.BtcdClient;
import com.blocain.bitms.payment.btc.core.domain.Block;

public class BlockNotificationWorker extends NotificationWorker
{
    private static final Logger LOG = LoggerFactory.getLogger(BlockNotificationWorker.class);
    
    public BlockNotificationWorker(Socket socket, BtcdClient client)
    {
        super(socket, client);
    }
    
    @Override
    protected Object getRelatedEntity(String headerHash)
    {
        Block block = new Block();
        block.setHash(headerHash);
        if (getClient() != null)
        {
            try
            {
                LOG.debug("-- getRelatedEntity(..): fetching related block data from 'bitcoind' " + "(via JSON-RPC API)");
                block = getClient().getBlock(headerHash);
            }
            catch (BitcoindException | CommunicationException e)
            {
                LOG.error("<< getRelatedEntity(..): failed to receive block data from 'bitcoind' " + "(hash: '{}'), message was: '{}'", headerHash, e.getMessage());
            }
        }
        return block;
    }
}