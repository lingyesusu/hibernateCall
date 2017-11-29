package service;

import org.springframework.stereotype.Service;

import dao.BaseDaoImpl;

@Service
public class BaseServiceImpl<E> {
	
	private BaseDaoImpl<E> baseDaoImpl;

	public BaseDaoImpl<E> getBaseDaoImpl() {
		return baseDaoImpl;
	}

	public void setBaseDaoImpl(BaseDaoImpl<E> baseDaoImpl) {
		this.baseDaoImpl = baseDaoImpl;
	}
	
}
