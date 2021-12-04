package com.wdbyte.jmx;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 * @author https://www.wdbyte.com
 */
public class MyMemoryManagement {

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException,
        InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        // 获取 MBean Server
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        MyMemory myMemory = new MyMemory();
        myMemory.setTotal(100L);
        myMemory.setUsed(20L);
        // 注册
        ObjectName objectName = new ObjectName("com.wdbyte.jmx:type=myMemory");
        platformMBeanServer.registerMBean(myMemory, objectName);

        while (true) {
            // 防止进行退出
            Thread.sleep(3000);
            System.out.println(myMemory.doMemoryInfo());
        }
    }
}