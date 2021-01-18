package cn.com.seryou.mybatisrefresh;

import cn.com.seryou.mybatisrefresh.cbp.SqlSessionFactoryBeanCBP;
import org.zeroturnaround.javarebel.*;

/**
 * Description: Mybatis Mapper热部署插件
 *
 * @author yhf
 * @since 2019/8/26
 */
public class MybatisRefreshPlugin implements Plugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisRefreshPlugin.class.getSimpleName());

    @Override
    public void preinit() {
        LOGGER.infoEcho("Ready config JRebel Seryou Mybatis refresh plugin...");
        Integration integration = IntegrationFactory.getInstance();
        ClassLoader classLoader = this.getClass().getClassLoader();

        // patch the bytecode of MemoryUsageServlet when it's first loaded by the classloader.
        String syName = "cn.com.seryou.xqy.framework.daoframework.support.factory.SySqlSessionFactoryBean";
        integration.addIntegrationProcessor(classLoader,
                syName,
                new SqlSessionFactoryBeanCBP());
    }

    @Override
    public boolean checkDependencies(ClassLoader classLoader, ClassResourceSource classResourceSource) {
        // only enable the plugin if the demoapp.MemoryUsageServlet class is in the classpath
        String syClassName = "cn.com.seryou.xqy.framework.daoframework.support.factory.SySqlSessionFactoryBean";
        return classResourceSource.getClassResource(syClassName) != null;
    }

    @Override
    public String getId() {
        return "seryou-mybatis-refresh-plugin";
    }

    @Override
    public String getName() {
        return "seryou-mybatis-refresh-plugin";
    }

    @Override
    public String getDescription() {
        return "mybatis mappers hot swap";
    }

    @Override
    public String getAuthor() {
        return "yhf";
    }

    @Override
    public String getWebsite() {
        return null;
    }

    @Override
    public String getSupportedVersions() {
        return null;
    }

    @Override
    public String getTestedVersions() {
        return null;
    }
}
