package dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDaoImpl<E> {

	protected final Logger log = Logger.getLogger(BaseDaoImpl.class);

	protected SessionFactory sessionFactory;
	protected Class<E> entityClass;

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDaoImpl() {
		this.entityClass = null;
		Class c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type)
					.getActualTypeArguments();
			this.entityClass = (Class<E>) parameterizedType[0];
		}
	}

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

}
