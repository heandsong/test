package org.com.song;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

final public class HibernateBase {
	private static Session s;
	static Session getSession()
	{
		Configuration cfg=new Configuration();
		cfg.configure(); //¼ÓÔØÇý¶¯
		SessionFactory sf=cfg.buildSessionFactory();
		s=sf.openSession();
		return s;
	}

}
