package MainTest;

import dev.edu.javaee.spring.deal.ClassPathXMLApplicationContext;
import dev.edu.javaee.spring.factory.BeanFactory;
import dev.edu.javaee.spring.factory.XMLBeanFactory;
import dev.edu.javaee.spring.resource.LocalFileResource;
import dev.edu.javaee.spring.testanno.UserServiceImpl;
import test.edu.javaee.spring.Dishes;
import testExample.boss;

public class Test {

	public static void main(String[] args) {
		
		//这里是助教提供的测试用例
		System.out.println("助教提供的测试用例: ");
		 LocalFileResource resource1 = new LocalFileResource("bean1.xml");
		BeanFactory beanFactory1 = new XMLBeanFactory(resource1);
		boss boss = (boss) beanFactory1.getBean("boss");
		System.out.println(boss.tostring());
		
        System.out.println("\n");
		//这里是测试@autowire和@Commoment注解方法的测试用例
		System.out.println("测试@autowire和@Commoment注解方法的测试用例:");
		ClassPathXMLApplicationContext path = new ClassPathXMLApplicationContext("setPackage.xml");
        UserServiceImpl userService = (UserServiceImpl) path.getBean("server");
        userService.show();
        
        System.out.println("\n");
        //这里是测试基于 setter 方法的依赖注入
        System.out.println("测试基于 setter 方法的依赖注入:");
        LocalFileResource resource = new LocalFileResource("beans.xml");
		BeanFactory beanFactory = new XMLBeanFactory(resource);
		Dishes d = (Dishes) beanFactory.getBean("egg dishes");
		d.show();
		d.getAtest().show();
		d.getAtest().getBtest().show();
	}
	

}
