package com.blocain.bitms.apps.common.model;

import com.blocain.bitms.apps.account.beans.AuthTokenRequest;

/**
 * <p>Author：yukai </p>
 * <p>Description:Description </p>
 * <p>Date: Create in 9:10 2018/3/29</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class SMSModel extends AuthTokenRequest {

    private String type;

    public SMSModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
