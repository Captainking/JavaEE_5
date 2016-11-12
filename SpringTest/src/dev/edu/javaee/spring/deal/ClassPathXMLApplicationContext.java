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
		//��ȡ�����ļ��й����bean
		readCom(fileName);
//		this.readXML(fileName);
		//ʵ����bean
		this.instancesBean();
		//ע�⴦����
		this.annotationInject();
		
	}

	/**
	 * ��ȡBean�����ļ�
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
            	Node bean = beanl.item(i);//��ȡһ��bean
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
            	Node coNode = conNodeList.item(i);//��ȡһ��bean
            	String packname = coNode.getAttributes().getNamedItem("base-package").getNodeValue();
             	//���Բ���
             	
                Getallclass getallclass=new Getallclass(packname);
                //���Բ���
        		 Set<String> classNames = getallclass.getClassNames();
        		 if (classNames != null) {
        		 for (String className : classNames) {
        		 //���Բ���
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
	 * ʵ����Bean
	 */
	public void instancesBean() {
		for (BeanDefine bean : beanList) {
			try {
				sigletions.put(bean.getId(), 
						Class.forName(bean.getClassName()).newInstance());
			} catch (Exception e) {
				//log.info("ʵ����Bean����...");
				System.out.println("��ʵ����bean���� line:108"+e);
			}
		}
	}
	
	/**
	 * ע�⴦����
	 * ���ע��ZxfResource������name���ԣ������name��ָ�������ƻ�ȡҪע���ʵ�����ã�
	 * ���ע��ZxfResource;û������name���ԣ��������������������ɨ�������ļ���ȡҪ
	 * ע���ʵ������
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
	 * ������set���������ע��
	 * @param bean �����bean
	 */
	public void propertyAnnotation(Object bean){
		try {
			//��ȡ�����Ե�����
			PropertyDescriptor[] ps = 
				Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
			for(PropertyDescriptor proderdesc : ps){
				//��ȡ����set����
				Method setter = proderdesc.getWriteMethod();
				//�ж�set�����Ƿ�����ע��
				if(setter!=null && setter.isAnnotationPresent(Autowire.class)){
					//��ȡ��ǰע�⣬���ж�name�����Ƿ�Ϊ��
					Autowire resource = setter.getAnnotation(Autowire.class);
					String name ="";
					Object value = null;
					if(resource.name()!=null&&!"".equals(resource.name())){
						//��ȡע���name���Ե�����
						name = resource.name();
						value = sigletions.get(name);
					}else{ //�����ǰע��û��ָ��name����,��������ͽ���ƥ��
						for(String key : sigletions.keySet()){
							//�жϵ�ǰ���������������Ƿ��������ļ��д���
							if(proderdesc.getPropertyType().isAssignableFrom(sigletions.get(key).getClass())){
								//��ȡ����ƥ���ʵ������
								value = sigletions.get(key);
								break;
							}
						}
					}
					//�������private����
					setter.setAccessible(true);
					//�����ö���ע������
					setter.invoke(bean, value); 
				}
			}
		} catch (Exception e) {
			//log.info("set����ע������쳣..........");
		}
	}
	
	/**
	 * �������ֶ��ϵ�ע��
	 * @param bean �����bean
	 */
	public void fieldAnnotation(Object bean){
		try {
			//��ȡ��ȫ�����ֶ�����
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
							//�жϵ�ǰ���������������Ƿ��������ļ��д���
							if(f.getType().isAssignableFrom(sigletions.get(key).getClass())){
								//��ȡ����ƥ���ʵ������
								value = sigletions.get(key);
								break;
							}
						}
					}
					//�������private�ֶ�
					f.setAccessible(true);
					//�����ö���ע������
					f.set(bean, value);
				}
			}
		} catch (Exception e) {
			//log.info("�ֶ�ע������쳣..........");
		}
	}
	
	/**
	 * ��ȡMap�еĶ�Ӧ��beanʵ��
	 * @param beanId
	 * @return
	 */
	public Object getBean(String beanId) {
		return sigletions.get(beanId);
	}


	
}
