package hibernateCall;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.SrcTestService;

@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring.xml","classpath:spring-hibernate.xml"})
public class daoTest {
	@Resource
	public SrcTestService srcTestService;
	
	@Test
	public void proc1(){
		Object[] object={1};
		srcTestService.proc1("{call proc1(?)}", object);
	}
	
	@Test
	public void proc2(){
		Object[] object={1};
		List<List<Map<String, Object>>> proc2 = srcTestService.proc2("{call proc1(?)}", object);
	}

}
