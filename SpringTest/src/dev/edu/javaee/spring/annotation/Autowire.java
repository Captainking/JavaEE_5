package dev.edu.javaee.spring.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// ������ʱִ��
@Retention(RetentionPolicy.RUNTIME)
// ע�����õط�(�ֶκͷ���)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Autowire {

	//ע���name����
	public String name() default "";
}
