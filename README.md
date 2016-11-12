# JavaEE_5
## 重要：##

测试用的main函数放在了MainTest包中的Test类中

##这次代码一共有三个测试用例:##


### 1.助教所给测试用例 ###

放在了testExample包中

总共有三个类boss.java、car.java、office.java

测试用的xml文件是bean1.xml

### 2.测试通过set方法进行依赖注入的用例 ###

放在test.edu.javaee.spring包中
 
包含Dish.java、Atest.java、Btest.java
 
测试用的XML文件是beans.xml
 
### 3.测试@autowire和@Compontent注解方法的测试用例 ###
 
放在了dev.edu.javaee.spring.testanno包中
 
包含UserServiceImpl.Java、UserDaoImpl.java、UserDao1Impl.java、UserDao2Impl.java
 
测试用的XML文件是setPackage.xml 该配置文件指定了需要扫描的类包，类包及其递归子包中所有的类都会被处理
 
 
 *备注：*
 
 *标准的注解是@Autowired和@Component由于拼写错误写成了@Autowire和@Compontent*
       
       
 *@Compontent如果不指定参数的话默认为为所注解类的首字母小写形式否则要指定为@Compontent(id="XXX")*
