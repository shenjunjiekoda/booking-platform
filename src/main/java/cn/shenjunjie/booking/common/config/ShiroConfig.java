package cn.shenjunjie.booking.common.config;

import cn.shenjunjie.booking.common.session.MyWebSessionManager;
import com.google.common.collect.Maps;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/9 14:13
 */

@Configuration
public class ShiroConfig implements EnvironmentAware {

    private Environment env;

    private String host;
    private Integer port = 6379;
    private String password;
    private Integer timeout;
    private Integer sessionExpire;
    private String sessionKeyPrefix;
    private Integer sessionInMemoryTimeout;
    private String principalIdFieldName;
    private Integer cacheExpire;
    private String cacheKeyPrefix;


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        /*
            anon:无需认证
            authc：必须认证
            user: 拥有 记住我
            perms: 有某个资源的权限
            role: 有某个角色的权限
         */
        Map<String, String> filterMap = Maps.newLinkedHashMap();
        filterMap.put("/login", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
//        filterMap.put("/**", "authc");
        bean.setFilterChainDefinitionMap(filterMap);
        bean.setLoginUrl("/teacher/tologin");
        return bean;
    }


    @Bean("defaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm, SessionManager sessionManager, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(redisCacheManager);
        return securityManager;
    }

    @Bean
    public IRedisManager redisManager() {
        IRedisManager redisManager;
        RedisManager standaloneManager = new RedisManager();
        if (!StringUtils.isEmpty(host) && port != null) {
            standaloneManager.setHost(host.concat(":").concat(String.valueOf(port)));
        }
        if (timeout != null) {
            standaloneManager.setTimeout(timeout);
        }
        if (!StringUtils.isEmpty(password)) {
            standaloneManager.setPassword(password);
        }
        redisManager = standaloneManager;
        return redisManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(IRedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        if (sessionExpire != null) {
            redisSessionDAO.setExpire(sessionExpire);
        }
        if (!StringUtils.isEmpty(sessionKeyPrefix)) {
            redisSessionDAO.setKeyPrefix(sessionKeyPrefix);
        }
        if (sessionInMemoryTimeout != null) {
            redisSessionDAO.setSessionInMemoryTimeout(sessionInMemoryTimeout);
        }
        return redisSessionDAO;
    }

    @Bean
    public RedisCacheManager redisCacheManager(IRedisManager redisManager) {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager);
        if (!StringUtils.isEmpty(principalIdFieldName)) {
            cacheManager.setPrincipalIdFieldName(principalIdFieldName);
        }
        if (cacheExpire != null) {
            cacheManager.setExpire(cacheExpire);
        }
        if (!StringUtils.isEmpty(cacheKeyPrefix)) {
            cacheManager.setKeyPrefix(cacheKeyPrefix);
        }
        return cacheManager;
    }

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        MyWebSessionManager sessionManager = new MyWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("defaultWebSecurityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        this.host = environment.getProperty("spring.redis.host", String.class);
        this.port = environment.getProperty("spring.redis.port", Integer.class);
        this.password = environment.getProperty("spring.redis.password", String.class);
        this.timeout = environment.getProperty("spring.redis.timeout", Integer.class);
        this.sessionExpire = environment.getProperty("session.expire", Integer.class);
        this.sessionKeyPrefix = environment.getProperty("session.key-prefix", String.class);
        this.sessionInMemoryTimeout = environment.getProperty("session.sessionInMemoryTimeout", Integer.class);
        this.principalIdFieldName = environment.getProperty("session.principalIdFieldName", String.class);
        this.cacheExpire = environment.getProperty("session.cache-expire", Integer.class);
        this.cacheKeyPrefix = environment.getProperty("session.cache-key-prefix", String.class);
    }
}