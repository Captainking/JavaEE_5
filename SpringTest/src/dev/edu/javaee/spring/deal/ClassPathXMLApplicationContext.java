package dev.edu.javaee.spring.deal;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import dev.edu.javaee.spring.annotation.Autowire;
import dev.edu.javaee.spring.annotation.Compontent;

public class ClassPathXMLApplicationContext {

	//Logger log = Logger.getLogger(ClassPathXMLApplicationContext.class);

	List<BeanDefine> beanList = new ArrayList<BeanDefine>();
	Map<String, Object> sigletions = new HashMap<String, Object>();
	public ClassPathXMLApplicationContext(){};
	public ClassPathXMLApplicationContext(String fileName) {
		//读取配置文件中管理的bean
		readCom(fileName);
//		this.readXML(fileName);
		//实例化bean
		this.instancesBean();
		//注解处理器
		this.annotationInject();
		
	}

	/**
	 * 读取Bean配置文件
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void readXML(String fileName) {
		try {
			InputStream resource= new FileInputStream(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(resource);
            NodeList beanl = document.getElementsByTagName("bean");
            for(int i = 0 ; i < beanl.getLength(); i++)
            {
            	Node bean = beanl.item(i);//获取一个bean
            	String beanClassName = bean.getAttributes().getNamedItem("class").getNodeValue();
            	String beanId = bean.getAttributes().getNamedItem("id").getNodeValue();
            	BeanDefine beanDefine = new BeanDefine(beanId,beanClassName);
				beanList.add(beanDefine);
            }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void readCom(String fileName) {
		try {
			InputStream resource= new FileInputStream(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(resource);
            NodeList conNodeList = document.getElementsByTagName("context:component-scan");
            for(int i = 0 ; i < conNodeList.getLength(); i++)
            {
            	Node coNode = conNodeList.item(i);//获取一个bean
            	String packname = coNode.getAttributes().getNamedItem("base-package").getNodeValue();
             	//测试部分
             	
                Getallclass getallclass=new Getallclass(packname);
                //测试部分
        		 Set<String> classNames = getallclass.getClassNames();
        		 if (classNames != null) {
        		 for (String className : classNames) {
        		 //测试部分
        		 Class temp = Class.forName(className);
        		 String beanId="";
 				 String beanClassname="";
        		 if(temp.isAnnotationPresent(Compontent.class)){
        			 Compontent com =(Compontent) temp.getAnnotation(Compontent.class);
        			 if(com.id().equals("")){
        				 String[]  strs=className.split("\\.");
        				 beanId= strs[strs.length-1].substring(0,1).toLowerCase()+strs[strs.length-1].substring(1);
        				 beanClassname=className;
        			 }else{
        			    beanId=com.id();
        				beanClassname=className;
        			 }
        			    BeanDefine beanDefine = new BeanDefine(beanId,beanClassname);
      				    beanList.add(beanDefine);
        		 }          	
        		 }
        		 
        		 }

            }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 实例化Bean
	 */
	public void instancesBean() {
		for (BeanDefine bean : beanList) {
			try {
				sigletions.put(bean.getId(), 
						Class.forName(bean.getClassName()).newInstance());
			} catch (Exception e) {
				//log.info("实例化Bean出错...");
				System.out.println("在实例化bean出错 line:108"+e);
			}
		}
	}
	
	/**
	 * 注解处理器
	 * 如果注解ZxfResource配置了name属性，则根据name所指定的名称获取要注入的实例引用，
	 * 如果注解ZxfResource;没有配置name属性，则根据属性所属类型来扫描配置文件获取要
	 * 注入的实例引用
	 * 
	 */
	public void annotationInject(){
		for(String beanName:sigletions.keySet()){
			Object bean = sigletions.get(beanName);
			if(bean!=null){
				this.propertyAnnotation(bean);
				this.fieldAnnotation(bean);
			}
		}
	}
	
	/**
	 * 处理在set方法加入的注解
	 * @param bean 处理的bean
	 */
	public void propertyAnnotation(Object bean){
		try {
			//获取其属性的描述
			PropertyDescriptor[] ps = 
				Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
			for(PropertyDescriptor proderdesc : ps){
				//获取所有set方法
				Method setter = proderdesc.getWriteMethod();
				//判断set方法是否定义了注解
				if(setter!=null && setter.isAnnotationPresent(Autowire.class)){
					//获取当前注解，并判断name属性是否为空
					Autowire resource = setter.getAnnotation(Autowire.class);
					String name ="";
					Object value = null;
					if(resource.name()!=null&&!"".equals(resource.name())){
						//获取注解的name属性的内容
						name = resource.name();
						value = sigletions.get(name);
					}else{ //如果当前注解没有指定name属性,则根据类型进行匹配
						for(String key : sigletions.keySet()){
							//判断当前属性所属的类型是否在配置文件中存在
							if(proderdesc.getPropertyType().isAssignableFrom(sigletions.get(key).getClass())){
								//获取类型匹配的实例对象
								value = sigletions.get(key);
								break;
							}
						}
					}
					//允许访问private方法
					setter.setAccessible(true);
					//把引用对象注入属性
					setter.invoke(bean, value); 
				}
			}
		} catch (Exception e) {
			//log.info("set方法注解解析异常..........");
		}
	}
	
	/**
	 * 处理在字段上的注解
	 * @param bean 处理的bean
	 */
	public void fieldAnnotation(Object bean){
		try {
			//获取其全部的字段描述
			Field[] fields = bean.getClass().getFields();
			for(Field f : fields){
				if(f!=null && f.isAnnotationPresent(Autowire.class)){
					Autowire resource = f.getAnnotation(Autowire.class);
					String name ="";
					Object value = null;
					if(resource.name()!=null&&!"".equals(resource.name())){
						name = resource.name();
						value = sigletions.get(name);
					}else{
						for(String key : sigletions.keySet()){
							//判断当前属性所属的类型是否在配置文件中存在
							if(f.getType().isAssignableFrom(sigletions.get(key).getClass())){
								//获取类型匹配的实例对象
								value = sigletions.get(key);
								break;
							}
						}
					}
					//允许访问private字段
					f.setAccessible(true);
					//把引用对象注入属性
					f.set(bean, value);
				}
			}
		} catch (Exception e) {
			//log.info("字段注解解析异常..........");
		}
	}
	
	/**
	 * 获取Map中的对应的bean实例
	 * @param beanId
	 * @return
	 */
	public Object getBean(String beanId) {
		return sigletions.get(beanId);
	}


	
}
