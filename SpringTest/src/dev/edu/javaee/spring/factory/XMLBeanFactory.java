package dev.edu.javaee.spring.factory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import dev.edu.javaee.spring.bean.BeanDefinition;
import dev.edu.javaee.spring.bean.BeanUtil;
import dev.edu.javaee.spring.bean.PropertyValue;
import dev.edu.javaee.spring.bean.PropertyValues;
import dev.edu.javaee.spring.resource.Resource;

public class XMLBeanFactory extends AbstractBeanFactory{
	
	private String xmlPath;
	
	public XMLBeanFactory(Resource resource)
	{
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(resource.getInputStream());
            NodeList beanList = document.getElementsByTagName("bean");
            for(int i = 0 ; i < beanList.getLength(); i++)
            {
            	Node bean = beanList.item(i);//获取一个bean
            	recursionFun(beanList,bean,this);
           }
            
		} catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	protected BeanDefinition GetCreatedBean(BeanDefinition beanDefinition) {
		
		try {
			// set BeanClass for BeanDefinition
			
			Class<?> beanClass = beanDefinition.getBeanClass();
			// set Bean Instance for BeanDefinition
			Object bean = beanClass.newInstance();	
			
			List<PropertyValue> fieldDefinitionList = beanDefinition.getPropertyValues().GetPropertyValues();
			for(PropertyValue propertyValue: fieldDefinitionList)
			{
				BeanUtil.invokeSetterMethod(bean, propertyValue.getName(), propertyValue.getValue());
			}
			
			beanDefinition.setBean(bean);
			
			return beanDefinition;
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

public void recursionFun(NodeList beanList,Node bean,AbstractBeanFactory abs){
     	BeanDefinition beandef = new BeanDefinition();//获取一个bean实例
     	String beanClassName = bean.getAttributes().getNamedItem("class").getNodeValue();
     	String beanName = bean.getAttributes().getNamedItem("id").getNodeValue();
 		beandef.setBeanClassName(beanClassName);
 		
			try {
				Class<?> beanClass = Class.forName(beanClassName);
				beandef.setBeanClass(beanClass);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		
 		PropertyValues propertyValues = new PropertyValues();
 		
 		NodeList propertyList = bean.getChildNodes();
     	for(int j = 0 ; j < propertyList.getLength(); j++)
     	{
     		Node property = propertyList.item(j);
     		if (property instanceof Element) {
 				Element ele = (Element) property;
 				
 				String name = ele.getAttribute("name");
 				Class<?> type;
					try {
						type = beandef.getBeanClass().getDeclaredField(name).getType();
						Object value=null;
						if (ele.hasAttribute("ref")) {
							for(int m=0;m<beanList.getLength();m++){
								if(beanList.item(m).getAttributes().getNamedItem("id").getNodeValue().equals(ele.getAttribute("ref"))){
									recursionFun(beanList,beanList.item(m),abs);
								}
							}
							value=abs.getBean(ele.getAttribute("ref"));
						}
						else{
							value = ele.getAttribute("value");
						}
     				
     				if(type == Integer.class)
     				{
     					value = Integer.parseInt((String) value);
     				}
     				
     				propertyValues.AddPropertyValue(new PropertyValue(
     						name,value));
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				
 				
 			}
     	}
     	beandef.setPropertyValues(propertyValues);//将获取的property对应的name value放到beandef
     	
     	abs.registerBeanDefinition(beanName, beandef);
	}
}
