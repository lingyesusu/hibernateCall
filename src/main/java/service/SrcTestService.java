package service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dao.BaseDaoImpl;
import dao.SrcTestDaoImpl;

@Service
public class SrcTestService extends BaseServiceImpl<String> {
	
	@Resource
	public SrcTestDaoImpl srcTestDaoImpl;

	@Override
	public void setBaseDaoImpl(BaseDaoImpl<String> srcTestDaoImpl ) {
		super.setBaseDaoImpl(srcTestDaoImpl);
	}
	
	public void proc1(String sql,Object[] params){
		List<Map<String, Object>> find_procedure = srcTestDaoImpl.find_procedure(sql, params);
		System.out.println(find_procedure.get(0).get("b"));
	}

	public List<List<Map<String, Object>>> proc2(String string, Object[] object) {
		List<List<Map<String, Object>>> find_procedure_multi = srcTestDaoImpl.find_procedure_multi(string, object);
		return find_procedure_multi;
		
	}

}
