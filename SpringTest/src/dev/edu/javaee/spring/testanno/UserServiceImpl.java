package dev.edu.javaee.spring.testanno;

import dev.edu.javaee.spring.annotation.Autowire;
import dev.edu.javaee.spring.annotation.Compontent;
@Compontent(id="server")
public class UserServiceImpl {

	public UserDaoImpl userDao;
	public UserDao1Impl user1Dao;

	// 字段上的注解,可以配置name属性
	@Autowire
	public UserDao2Impl user2Dao;

	// set方法上的注解，带有name属性
	@Autowire
	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	// set方法上的注解，没有配置name属性
	@Autowire
	public void setUser1Dao(UserDao1Impl user1Dao) {
		this.user1Dao = user1Dao;
	}

	public void show() {
		userDao.show();
		user1Dao.show();
		user2Dao.show();
		System.out.println("这里是UserServiceImpl中的Service方法........");
	}
}
