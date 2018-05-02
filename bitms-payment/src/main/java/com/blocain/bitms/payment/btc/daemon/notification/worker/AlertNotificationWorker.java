package com.blocain.bitms.payment.btc.daemon.notification.worker;

import java.net.Socket;

public class AlertNotificationWorker extends NotificationWorker
{
    public AlertNotificationWorker(Socket socket)
    {
        super(socket, null);
    }
    
    @Override
    protected Object getRelatedEntity(String alert)
    {
        return alert;
    }
}