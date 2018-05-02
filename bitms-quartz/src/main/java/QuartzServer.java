import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 调度服务实例 Introduce
 * <p>File：QuartzServer.java</p>
 * <p>Title: QuartzServer</p>
 * <p>Description: QuartzServer</p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class QuartzServer
{
    private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) throws IOException
    {
        String[] config = new String[]{"spring.xml", "spring-quartz.xml","spring-consumer.xml"};
        context = new ClassPathXmlApplicationContext(config);
        context.start();
        System.in.read(); // 按任意键退出
    }
}
