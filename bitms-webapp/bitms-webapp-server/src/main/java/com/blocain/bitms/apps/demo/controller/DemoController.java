package com.blocain.bitms.apps.demo.controller;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.apps.sdk.internal.util.Signature;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.google.common.collect.Maps;

/**
 * DemoController Introduce
 * <p>Title: DemoController</p>
 * <p>File：DemoController.java</p>
 * <p>Description: DemoController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
public class DemoController extends AppsController
{
    public static final String publicKey  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgKSzwjm3MqkkdkZOuC70oL591mnlIBvEqvpYx8MAEcRidaYcFhaNPhTzpNW9277f3ySl94iz6ymTnd7rkp9DM8JVQtZmUdM5ibYIfaphBqBJVG8HohwMH34EBIblG3kECbRQ6oeDL++kzCLPBXsrqiEutJTQ6YqBaUXYu38VpITCLK8ayCQJe0Pu16M2E3bvNOai3p/hjca9nbC9Yf0R5q0LLVEUHPK437kZBPEaw0yH9gGjBIkK1q1oFeTKdKR3Jtzga5Xi96P2qWr3g+mLT7haMGZS7B8u1d/nirpHvLcrxr78ZQaCMp03P5f/HAjGPzApHSPXLxgpt/HK5tnohQIDAQAB";
    
    public static final String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCApLPCObcyqSR2Rk64LvSgvn3WaeUgG8Sq+ljHwwARxGJ1phwWFo0+FPOk1b3bvt/fJKX3iLPrKZOd3uuSn0MzwlVC1mZR0zmJtgh9qmEGoElUbweiHAwffgQEhuUbeQQJtFDqh4Mv76TMIs8FeyuqIS60lNDpioFpRdi7fxWkhMIsrxrIJAl7Q+7XozYTdu805qLen+GNxr2dsL1h/RHmrQstURQc8rjfuRkE8RrDTIf2AaMEiQrWrWgV5Mp0pHcm3OBrleL3o/apaveD6YtPuFowZlLsHy7V3+eKuke8tyvGvvxlBoIynTc/l/8cCMY/MCkdI9cvGCm38crm2eiFAgMBAAECggEAZ4IekVzXJxAponEXzeMaOmyL3AYAskElkV02KiWg3Krjfj5VwwAKTbWWBT9mMHX4tBdM40s/WSxu9kvzyFoVPIRVDh5s3dHTLhcl5dc9kpNN7X4Y6jsfFgfTO8E+f0h0E0rfAJZg3CU6uHYZlRNyIsXoSqyjHAax8f6dUOAEjgaPjJEAQQmWKR+LPG3Lcir7rci3HnqUnSTh4lkH+qvz8Yyeaab5q0bqD6DSYNsGT//wQmqjcMZmkg4vhXUn36JoLNojutqRdW7EFQcUTSZeGO03TtWwDr9EKGfTSvPdyF6eKZy8E6k6mDnRyiAOW9bh9dPoU06aMNc14stO1vs8UQKBgQDBGs/oVH14GkTPy/BWbziCdLmP61Pij2bvOVq0yR78Z3WObXZ1mfI+EGOvK4S21k3F147ZsRdFNvMYN6omXMCAaAwN8y1XGEB7EeT9F6OAMehM/PAn1goh2h2WZuiqFHeP/y9zKtfF0makJiaO+qc4Xm5674VWlNW8GGNYIE5hJwKBgQCqixAXCq6rYxrehEQWz50uKOGRmsvkx4ciYfPq8iMCL+0r0dRFwZYYooAvBWuASDveUr+8FcfDItdCCyRNgBnVO1uMXPVQFOf4jcdz1D/GVoT1FRrdpDGBOhMU8nW9wW7StvGED1aIov4xDaqQ8uzHgovSOTykm6ad45g6QVwccwKBgE6+xUujsKqaY7ctHevRXQTt3oHO4B1ChyJEjDWu0kDxW6HiB217gqpGerADGhKJH5vvBCTHr4qdFtKoWG1eY225gDjsEnAyxpCpQmznB9iOAYHrn29PPECDto8Plg9NW1hQIqRfMzBLVWnY2N5zXl+BOqJNLF2bQfF7LH/SZMP/AoGBAI6HsmbPcrOiHC6j9BPsZ7zQRdTevlDxwKqnmqdcvo1xaJIax4GVDNCB3wdtTC7mL8La/Ys/2LfpQmXb0fdDfFONTDG+7vaCwmwkcfEIDlTAslXx0YYzV3xdAN2c1XJFW02Xv7lS6EtBjZeJiGej3ufO3vRPCbO4skN6lxr4zPapAoGBAI4xbqlC/Bz4vKvC8IRUA0CxKjkE7fSsG0U5LD/ZXMkApAc2vXIhlGTCKD56XEu0S7WTCO3WKSxES7ge7aw0Wt8V3xo8a/Rm/eVycc0YEC+udEOIEQ4xE0vVquxyGF0Pk4mJP7NTBIkFHJVPp5M3aLcP8xyHLNNczY3724wwAv9r";
    
    /**
     * 实现正常的加密解密
     * @param request
     * @return {@link Map}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/demo")
    public AppsMessage demo(HttpServletRequest request) throws BusinessException
    {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        String parameterName;
        while (parameterNames.hasMoreElements())
        {
            parameterName = parameterNames.nextElement();
            System.out.println(parameterName + " >> " + request.getParameter(parameterName));
            params.put(parameterName, request.getParameter(parameterName));
        }
        String requestContent = Signature.checkSignAndDecrypt(params, publicKey, true, true);
        AppsMessage jsonMessage = this.getJsonMessage(CommonEnums.SUCCESS,
                Encrypt.encryptContent(requestContent, BitmsConstants.ENCRYPT_TYPE_AES, params.get(BitmsConstants.ENCRYPT_KEY)));
        System.out.println(requestContent);
        return jsonMessage;
    }
}
