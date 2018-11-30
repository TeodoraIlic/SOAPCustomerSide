package com.teodora.ws.soap;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.teodora.ws.soap.entities.Customer;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	
	static {
		try{
			Configuration con = new Configuration();
			con.configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class);
			sessionFactory = con.configure().buildSessionFactory();
		}
		catch (Throwable e){
			System.err.println("Session factory error");
			e.printStackTrace();
		}	
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}

}
