package com.blocain.bitms.ignite.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IgniteCfg Introduce
 * <p>Title: IgniteCfg</p>
 * <p>File：IgniteCfg.java</p>
 * <p>Description: IgniteCfg</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Configuration
@EnableIgniteRepositories("com.blocain")
public class BiexIgniteCfg
{
    @Autowired
    private IgniteConfiguration igniteCfg;
    
    @Bean
    public Ignite igniteInstance()
    {
        return Ignition.start(igniteCfg);
    }
}
