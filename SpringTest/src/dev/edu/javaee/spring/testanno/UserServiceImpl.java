package dev.edu.javaee.spring.testanno;

import dev.edu.javaee.spring.annotation.Autowire;
import dev.edu.javaee.spring.annotation.Compontent;
@Compontent(id="server")
public class UserServiceImpl {

	public UserDaoImpl userDao;
	public UserDao1Impl user1Dao;

	// �ֶ��ϵ�ע��,��������name����
	@Autowire
	public UserDao2Impl user2Dao;

	// set�����ϵ�ע�⣬����name����
	@Autowire
	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	// set�����ϵ�ע�⣬û������name����
	@Autowire
	public void setUser1Dao(UserDao1Impl user1Dao) {
		this.user1Dao = user1Dao;
	}

	public void show() {
		userDao.show();
		user1Dao.show();
		user2Dao.show();
		System.out.println("������UserServiceImpl�е�Service����........");
	}
}
