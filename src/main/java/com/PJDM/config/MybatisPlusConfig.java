package com.PJDM.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis-Plus 配置
 *
 * @author Alanine
 */
@Configuration
@MapperScan("com.PJDM.mapper")
public class MybatisPlusConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * 分页插件（必须先声明，供 sqlSessionFactory 注入）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 使用 MybatisSqlSessionFactoryBean（MyBatis-Plus 专用），
     * 确保 BaseMapper、IService 等 MP 功能正常工作。
     * 注意：必须将分页插件通过 setPlugins 注入，否则手动建的 Factory 不会加载拦截器。
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml")
        );
        factoryBean.setTypeAliasesPackage("com.PJDM.pojo");

        com.baomidou.mybatisplus.core.MybatisConfiguration config =
                new com.baomidou.mybatisplus.core.MybatisConfiguration();
        config.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(config);

        // 关键：将分页拦截器注入 SqlSessionFactory，否则分页不生效
        factoryBean.setPlugins(mybatisPlusInterceptor);

        return factoryBean.getObject();
    }
}
