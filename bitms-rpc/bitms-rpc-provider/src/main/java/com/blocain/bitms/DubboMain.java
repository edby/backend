package com.blocain.bitms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.Main;
import com.blocain.bitms.tools.bean.IdWorker;
import com.blocain.bitms.tools.utils.PropertiesUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * DubboMain Introduce
 * <p>File：DubboMain.java</p>
 * <p>Title: DubboMain</p>
 * <p>Description: DubboMain</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DubboMain
{
    public static final String                      CONTAINER_KEY     = "dubbo.container";
    
    public static final String                      SHUTDOWN_HOOK_KEY = "dubbo.shutdown.hook";
    
    private static final Logger                     logger            = LoggerFactory.getLogger(Main.class);
    
    private static final ExtensionLoader<Container> loader            = ExtensionLoader.getExtensionLoader(Container.class);
    
    private static final ReentrantLock              LOCK              = new ReentrantLock();
    
    private static final Condition                  STOP              = LOCK.newCondition();
    
    public static void main(String[] args)
    {
        try
        {
            if (args == null || args.length == 0)
            {
                String config = ConfigUtils.getProperty(CONTAINER_KEY, loader.getDefaultExtensionName());
                args = Constants.COMMA_SPLIT_PATTERN.split(config);
            }
            else
            {
                /**
                 * 自定义参数
                 * ID生成器实例化,如果未指定将采用默认的生成策略。
                 */
                if (null != args && args.length > 0)
                {
                    String idwork = null != args[0] ? args[0] : null;
                    if (StringUtils.isNotBlank(idwork))
                    {
                        PropertiesUtils properties = new PropertiesUtils(idwork);
                        Long workerId = properties.getLong("bitms.serial.number.workerId");
                        Long centerId = properties.getLong("bitms.serial.number.centerId");
                        SerialnoUtils.setIdworker(new IdWorker(workerId, centerId));
                    }
                }
                String config = ConfigUtils.getProperty(CONTAINER_KEY, loader.getDefaultExtensionName());
                args = Constants.COMMA_SPLIT_PATTERN.split(config);
            }
            final List<Container> containers = new ArrayList<Container>();
            for (int i = 0; i < args.length; i++)
            {
                containers.add(loader.getExtension(args[i]));
            }
            logger.info("Use container type(" + Arrays.toString(args) + ") to run dubbo serivce.");
            if ("true".equals(System.getProperty(SHUTDOWN_HOOK_KEY)))
            {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    for (Container container : containers)
                    {
                        try
                        {
                            container.stop();
                            logger.info("Dubbo " + container.getClass().getSimpleName() + " stopped!");
                        }
                        catch (Throwable t)
                        {
                            logger.error(t.getMessage(), t);
                        }
                        try
                        {
                            LOCK.lock();
                            STOP.signal();
                        }
                        finally
                        {
                            LOCK.unlock();
                        }
                    }
                }));
            }
            for (Container container : containers)
            {
                container.start();
                logger.info("Dubbo " + container.getClass().getSimpleName() + " started!");
            }
            System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " Dubbo service server started!");
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
        try
        {
            LOCK.lock();
            STOP.await();
        }
        catch (InterruptedException e)
        {
            logger.warn("Dubbo service server stopped, interrupted by other thread!", e);
        }
        finally
        {
            LOCK.unlock();
        }
    }
}
