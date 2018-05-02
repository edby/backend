package com.blocain.bitms.payment.btc.daemon.notification.worker;

import java.net.Socket;

import com.blocain.bitms.payment.btc.core.client.BtcdClient;
import com.blocain.bitms.payment.btc.core.common.Errors;
import com.blocain.bitms.payment.btc.daemon.Notifications;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationWorkerFactory
{
    public static NotificationWorker createWorker(Notifications type, Socket socket, BtcdClient client)
    {
        if (type.equals(Notifications.ALERT))
        {
            return new AlertNotificationWorker(socket);
        }
        else if (type.equals(Notifications.BLOCK))
        {
            return new BlockNotificationWorker(socket, client);
        }
        else if (type.equals(Notifications.WALLET))
        {
            return new WalletNotificationWorker(socket, client);
        }
        else
        {
            throw new IllegalArgumentException(Errors.ARGS_BTCD_NOTIFICATION_UNSUPPORTED.getDescription());
        }
    }
}