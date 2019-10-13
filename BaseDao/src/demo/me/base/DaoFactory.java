package demo.me.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import demo.me.dao.UserDao;

/**
 * 工厂类：向service层输送Dao的实现类
 * 		    单例模式,双重锁
 * @author admin
 *
 */
public class DaoFactory {
	private static DaoFactory factory=null;
	
	private static UserDao userDao=null;
	
	private DaoFactory() {
		Properties prop=new Properties();
		@SuppressWarnings("static-access")
		InputStream is=DaoFactory.class.getClassLoader().getSystemResourceAsStream("daoConfig.properties");
		
		try {
			prop.load(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String UserDaoClass=prop.getProperty("UserDaoClass");
		try {
			userDao=(UserDao) Class.forName(UserDaoClass).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static DaoFactory getInstance() {
		if(factory==null) {
			synchronized(DaoFactory.class) {
				factory=new DaoFactory();
			}
		}
		return factory;	
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
}
