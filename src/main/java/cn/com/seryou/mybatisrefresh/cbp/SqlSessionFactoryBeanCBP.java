package cn.com.seryou.mybatisrefresh.cbp;

import cn.com.seryou.mybatisrefresh.MybatisRefresher;
import org.zeroturnaround.bundled.javassist.*;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

/**
 * Description:
 *
 * @author yhf
 * @since 2019/8/26
 */
public class SqlSessionFactoryBeanCBP extends JavassistClassBytecodeProcessor {

    @Override
    public void process(ClassPool classPool, ClassLoader classLoader, CtClass ctClass) throws Exception {
        try {
            classPool.importPackage("cn.com.seryou.mybatisrefresh");
            classPool.importPackage("java.io");
            classPool.importPackage("java.util");
            classPool.importPackage("java.lang.reflect");
            classPool.importPackage("org.apache.ibatis.binding");
            classPool.importPackage("org.springframework.core.io");
            classPool.importPackage("org.springframework.core");
            classPool.importPackage("org.apache.ibatis.session");
            classPool.importPackage("org.apache.ibatis.builder.xml");
            classPool.importPackage("org.apache.ibatis.parsing");
            classPool.importPackage("org.apache.ibatis.io");
            classPool.importPackage("org.apache.ibatis.executor");
            classPool.importPackage("org.springframework.web.context.support");
            classPool.importPackage("org.springframework.web.context");
            classPool.importPackage("org.springframework.beans.factory.config");
            classPool.importPackage("org.springframework.beans.factory.support");
            classPool.importPackage("cn.com.seryou.xqy.framework.daoframework.support.factory");

            ctClass.addField(CtField.make("private final Map modifyRecorder = new HashMap();", ctClass));
            ctClass.addField(CtField.make("private final static Map mapperPaths = new HashMap();", ctClass));


            ctClass.addMethod(CtMethod.make(
                    "public synchronized void refresh() throws NestedIOException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {\n" +
                            "        XmlWebApplicationContext applicationContext = (XmlWebApplicationContext) ContextLoader.getCurrentWebApplicationContext();\n" +
                            "        ConfigurableListableBeanFactory factory = applicationContext.getBeanFactory();\n" +
                            "        if (mapperPaths.isEmpty()) {\n" +
                            "            Class[] classes = new Class[1];\n" +
                            "            classes[0] = String.class;\n" +
                            "            Method method = AbstractBeanFactory.class.getDeclaredMethod(\"getMergedLocalBeanDefinition\", classes);\n" +
                            "            method.setAccessible(true);\n" +
                            "            String[] names = applicationContext.getBeanNamesForType(cn.com.seryou.xqy.framework.daoframework.support.factory.SySqlSessionFactoryBean.class);\n" +
                            "            for (int c = 0; c < names.length; c++) {\n" +
                            "                String[] args = new String[1];\n" +
                            "                Object factoryBean = factory.getBean(names[c]);\n" +
                            "\n" +
                            "                args[0] = names[c].substring(1, names[c].length());\n" +
                            "                RootBeanDefinition mbd = (RootBeanDefinition) method.invoke(factory, args);\n" +
                            "                Object mapperLocations = mbd.getPropertyValues().get(\"mapperLocations\");\n" +
                            "\n" +
                            "                if (mapperLocations instanceof RuntimeBeanReference) {\n" +
                            "                    String name = ((RuntimeBeanReference) mapperLocations).getBeanName();\n" +
                            "                    Object bean = factory.getBean(name);\n" +
                            "\n" +
                            "                    if (bean instanceof List) {\n" +
                            "                        mapperPaths.put(factoryBean, (String) ((List) bean).get(0));\n" +
                            "                    } else {\n" +
                            "                        mapperPaths.put(factoryBean, (String) bean);\n" +
                            "                    }\n" +
                            "                } else if (mapperLocations instanceof TypedStringValue) {\n" +
                            "                    mapperPaths.put(factoryBean, ((TypedStringValue) mapperLocations).getValue());\n" +
                            "                } else {\n" +
                            "                    mapperPaths.put(factoryBean, (String) mapperLocations);\n" +
                            "                }\n" +
                            "            }\n" +
                            "        }\n" +
                            "\n" +
                            "        String mapperPath = (String) mapperPaths.get(this);\n" +
                            "        if (mapperPath == null) {\n" +
                            "            System.out.println(\"有bug！\");\n" +
                            "            System.out.println(\"mapperPaths size: \" + mapperPaths.size());\n" +
                            "            System.out.println(mapperPaths);\n" +
                            "        }\n" +
                            "        mapperLocations = (Resource[]) factory.getTypeConverter().convertIfNecessary(mapperPath, mapperLocations.getClass());\n" +
                            "\n" +
                            "        Map updated = new HashMap();\n" +
                            "        boolean needRefresh = false;\n" +
                            "        if (this.mapperLocations != null && this.mapperLocations.length > 0) {\n" +
                            "            for (int i = 0; i < this.mapperLocations.length; i++) {\n" +
                            "                Resource mapperLocation = this.mapperLocations[i];\n" +
                            "                if (mapperLocation != null) {\n" +
                            "                    try {\n" +
                            "                        long lastModified = mapperLocation.lastModified();\n" +
                            "                        if (modifyRecorder.containsKey(mapperLocation)) {\n" +
                            "                            Long lastTime = (Long) modifyRecorder.get(mapperLocation);\n" +
                            "                            boolean doRefresh = lastModified > lastTime.longValue();\n" +
                            "                            if (doRefresh) {\n" +
                            "                                needRefresh = true;\n" +
                            "                            }\n" +
                            "                            updated.put(mapperLocation, Boolean.valueOf(doRefresh));\n" +
                            "                        } else {\n" +
                            "                            needRefresh = true;\n" +
                            "                            updated.put(mapperLocation, Boolean.valueOf(needRefresh));\n" +
                            "                        }\n" +
                            "                        modifyRecorder.put(mapperLocation, Long.valueOf(lastModified));\n" +
                            "                    } catch (IOException e) {\n" +
                            "                        e.printStackTrace();\n" +
                            "                    }\n" +
                            "                }\n" +
                            "            }\n" +
                            "        }\n" +
                            "        if (needRefresh) {\n" +
                            "            System.out.println(\"Reloading Mappers\");\n" +
                            "        } else {\n" +
                            "            return;\n" +
                            "        }\n" +
                            "        Configuration configuration;\n" +
                            "        try {\n" +
                            "            configuration = this.getObject().getConfiguration();\n" +
                            "        } catch (Exception e) {\n" +
                            "            e.printStackTrace();\n" +
                            "            throw new RuntimeException(\"SqlSessionFactory未初始化\");\n" +
                            "        }\n" +
                            "\n" +
                            "        Field loadedResources = Configuration.class.getDeclaredField(\"loadedResources\");\n" +
                            "        loadedResources.setAccessible(true);\n" +
                            "        Set loadedResourcesSet = (Set) loadedResources.get(configuration);\n" +
                            "\n" +
                            "        Field knownMappers = MapperRegistry.class.getDeclaredField(\"knownMappers\");\n" +
                            "        knownMappers.setAccessible(true);\n" +
                            "        Map mapConfig = (Map) knownMappers.get(configuration.getMapperRegistry());\n" +
                            "\n" +
                            "        Field parameterMaps = Configuration.class.getDeclaredField(\"parameterMaps\");\n" +
                            "        parameterMaps.setAccessible(true);\n" +
                            "        Map parameterMap = (Map) parameterMaps.get(configuration);\n" +
                            "\n" +
                            "        Field resultMaps = Configuration.class.getDeclaredField(\"resultMaps\");\n" +
                            "        resultMaps.setAccessible(true);\n" +
                            "        Map resultMap = (Map) resultMaps.get(configuration);\n" +
                            "\n" +
                            "        Field keyGenerators = Configuration.class.getDeclaredField(\"keyGenerators\");\n" +
                            "        keyGenerators.setAccessible(true);\n" +
                            "        Map keyGenerator = (Map) keyGenerators.get(configuration);\n" +
                            "\n" +
                            "        Field mappedStatements = Configuration.class.getDeclaredField(\"mappedStatements\");\n" +
                            "        mappedStatements.setAccessible(true);\n" +
                            "        Map mappedStatementsMap = (Map) mappedStatements.get(configuration);\n" +
                            "\n" +
                            "        if (this.mapperLocations != null && this.mapperLocations.length > 0) {\n" +
                            "            for (int i = 0; i < this.mapperLocations.length; i++) {\n" +
                            "                Resource mapperLocation = this.mapperLocations[i];\n" +
                            "                if (mapperLocation != null) {\n" +
                            "                    Boolean toUpdate = (Boolean) updated.get(mapperLocation);\n" +
                            "                    if (toUpdate != null && toUpdate.booleanValue()) {\n" +
                            "                        try {\n" +
                            "                            XPathParser xPathParser = new XPathParser(mapperLocation.getInputStream(), true, configuration.getVariables(),\n" +
                            "                                    new XMLMapperEntityResolver());\n" +
                            "                            XNode content = xPathParser.evalNode(\"/mapper\");\n" +
                            "                            String namespace = content.getStringAttribute(\"namespace\");\n" +
                            "\n" +
                            "                            configuration.getCacheNames().remove(namespace);\n" +
                            "\n" +
                            "                            loadedResourcesSet.remove(mapperLocation.toString());\n" +
                            "\n" +
                            "                            mapConfig.remove(Resources.classForName(namespace));\n" +
                            "\n" +
                            "                            List xNodes = content.evalNodes(\"/mapper/parameterMap\");\n" +
                            "                            for (Iterator it = xNodes.iterator(); it.hasNext(); ) {\n" +
                            "                                XNode next = (XNode) it.next();\n" +
                            "                                String id = next.getStringAttribute(\"id\");\n" +
                            "                                parameterMap.remove(namespace + '.' + id);\n" +
                            "                            }\n" +
                            "\n" +
                            "                            List xNodes1 = content.evalNodes(\"/mapper/resultMap\");\n" +
                            "                            for (Iterator it = xNodes1.iterator(); it.hasNext(); ) {\n" +
                            "                                XNode next = (XNode) it.next();\n" +
                            "                                String id = next.getStringAttribute(\"id\");\n" +
                            "                                resultMap.remove(id);\n" +
                            "                                resultMap.remove(namespace + '.' + id);\n" +
                            "                            }\n" +
                            "\n" +
                            "                            List xNodes2 = content.evalNodes(\"insert|update|select|delete\");\n" +
                            "                            for (Iterator it = xNodes2.iterator(); it.hasNext(); ) {\n" +
                            "                                XNode next = (XNode) it.next();\n" +
                            "                                String id = next.getStringAttribute(\"id\");\n" +
                            "                                keyGenerator.remove(id + \"!selectKey\");\n" +
                            "                                keyGenerator.remove(namespace + '.' + id + \"!selectKey\");\n" +
                            "                                mappedStatementsMap.remove(id);\n" +
                            "                                mappedStatementsMap.remove(namespace + '.' + id);\n" +
                            "                            }\n" +
                            "\n" +
                            "                            List xNodes3 = content.evalNodes(\"/mapper/sql\");\n" +
                            "                            for (Iterator it = xNodes3.iterator(); it.hasNext(); ) {\n" +
                            "                                XNode next = (XNode) it.next();\n" +
                            "                                String id = next.getStringAttribute(\"id\");\n" +
                            "                                configuration.getSqlFragments().remove(id);\n" +
                            "                                configuration.getSqlFragments().remove(namespace + '.' + id);\n" +
                            "                            }\n" +
                            "\n" +
                            "                            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),\n" +
                            "                                    configuration, mapperLocation.toString(), configuration.getSqlFragments());\n" +
                            "                            xmlMapperBuilder.parse();\n" +
                            "                        } catch (Exception e) {\n" +
                            "                            throw new NestedIOException(\"Failed to parse mapping resource: '\" + mapperLocation + \"'\", e);\n" +
                            "                        } finally {\n" +
                            "                            ErrorContext.instance().reset();\n" +
                            "                        }\n" +
                            "\n" +
                            "                        System.out.println(\"reload mapper file: '\" + mapperLocation + \"'\");\n" +
                            "                    }\n" +
                            "                }\n" +
                            "            }\n" +
                            "        } else {\n" +
                            "            System.out.println(\"Property 'mapperLocations' was not specified or no matching resources found\");\n" +
                            "        }\n" +
                            "    }"
                    , ctClass
            ));

            ctClass.addInterface(classPool.get(MybatisRefresher.class.getName()));

            ctClass.getDeclaredMethod("buildSqlSessionFactory").insertAfter("" +
                    "ReloadHelper.addRequestListener(this);" +
                    "        if (this.mapperLocations != null && this.mapperLocations.length > 0) {\n" +
                    "            for (int i = 0; i < this.mapperLocations.length; i++) {\n" +
                    "                Resource mapperLocation = this.mapperLocations[i];\n" +
                    "                if (mapperLocation != null) {\n" +
                    "                    long lastModified = 0;\n" +
                    "                    try {\n" +
                    "                        lastModified = mapperLocation.lastModified();\n" +
                    "                        modifyRecorder.put(mapperLocation, Long.valueOf(lastModified));\n" +
                    "                    } catch (IOException e) {\n" +
                    "                        e.printStackTrace();\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }" +
                    "\n" +
                    "        System.out.println(\"Seryou Mybatis Plugin Enabled!\");"
            );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
