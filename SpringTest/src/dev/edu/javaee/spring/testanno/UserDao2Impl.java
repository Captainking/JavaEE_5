package dev.edu.javaee.spring.testanno;

import dev.edu.javaee.spring.annotation.Compontent;

@Compontent
public class UserDao2Impl {
	String name ;
	public void show(){
		System.out.println("这里是UserDao2Impl中的show方法........");
	}
}
