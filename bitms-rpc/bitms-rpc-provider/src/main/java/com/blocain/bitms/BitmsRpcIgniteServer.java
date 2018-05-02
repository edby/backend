package com.blocain.bitms;

import com.blocain.bitms.tools.bean.IdWorker;
import com.blocain.bitms.tools.utils.PropertiesUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 服务启动类
 */
public class BitmsRpcIgniteServer
{
    private static ClassPathXmlApplicationContext context;

    public static void main(String[] args) throws IOException
    {
        String[] config = new String[]{
                "classpath:/META-INF/spring/spring.xml",
                "classpath:/META-INF/spring/spring-service.xml",
                "classpath:/META-INF/spring/spring-ignite-test.xml",
                "classpath:/META-INF/spring/spring-mongodb.xml",
                "classpath:/META-INF/spring/spring-jedis.xml",
                "classpath:/META-INF/spring/spring-jdbc.xml",
                "classpath:/META-INF/spring/spring-provider.xml"
        };
        // ID生成器实例化,如果未指定将采用默认的生成策略。
        if(null != args && args.length > 0){
            String idwork = null != args[0] ? args[0] : null;
            if (StringUtils.isNotBlank(idwork))
            {
                PropertiesUtils properties = new PropertiesUtils(idwork);
                Long workerId = properties.getLong("bitms.serial.number.workerId");
                Long centerId = properties.getLong("bitms.serial.number.centerId");
                SerialnoUtils.setIdworker(new IdWorker(workerId, centerId));
            }
        }
        context = new ClassPathXmlApplicationContext(config);
        context.start();
        System.in.read(); // 按任意键退出
    }
}
