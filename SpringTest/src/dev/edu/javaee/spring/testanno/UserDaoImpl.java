package dev.edu.javaee.spring.testanno;

import dev.edu.javaee.spring.annotation.Compontent;

@Compontent
public class UserDaoImpl {
	
	String name ;
	
	public void show(){
		System.out.println("这里是UserDaoImpl中的show方法");
	}
}
