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
		
		//�����������ṩ�Ĳ�������
		System.out.println("�����ṩ�Ĳ�������: ");
		 LocalFileResource resource1 = new LocalFileResource("bean1.xml");
		BeanFactory beanFactory1 = new XMLBeanFactory(resource1);
		boss boss = (boss) beanFactory1.getBean("boss");
		System.out.println(boss.tostring());
		
        System.out.println("\n");
		//�����ǲ���@autowire��@Commomentע�ⷽ���Ĳ�������
		System.out.println("����@autowire��@Commomentע�ⷽ���Ĳ�������:");
		ClassPathXMLApplicationContext path = new ClassPathXMLApplicationContext("setPackage.xml");
        UserServiceImpl userService = (UserServiceImpl) path.getBean("server");
        userService.show();
        
        System.out.println("\n");
        //�����ǲ��Ի��� setter ����������ע��
        System.out.println("���Ի��� setter ����������ע��:");
        LocalFileResource resource = new LocalFileResource("beans.xml");
		BeanFactory beanFactory = new XMLBeanFactory(resource);
		Dishes d = (Dishes) beanFactory.getBean("egg dishes");
		d.show();
		d.getAtest().show();
		d.getAtest().getBtest().show();
	}
	

}
