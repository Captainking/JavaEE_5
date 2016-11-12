package dev.edu.javaee.spring.bean;

public class BeanDefinition {
	private Object bean;//实例化后的对象
	
	private Class<?> beanClass;//bean对应的类
	
	private String beanClassName;//bean对应的类名
	
	private PropertyValues propertyValues;//propery对应的name和value
	
//	private PropertyRefs propertyRefs;
//	
//	
//	public PropertyRefs getPropertyRefs() {
//		return propertyRefs;
//	}
//
//	public void setPropertyRefs(PropertyRefs propertyRefs) {
//		this.propertyRefs = propertyRefs;
//	}

	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}
	
}
