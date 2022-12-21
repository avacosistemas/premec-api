package ar.com.avaco.arc.sec;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:db/secConn.properties")
@ContextConfiguration(locations={"classpath:arc/sec/spring/arcSecContext.xml"})
public class ApplicationContextTest implements ApplicationContextAware {

	@Autowired
	private ApplicationContext appCtx;
	
	@Test
	public void applicationContextTest() {
		Assert.assertNotNull(this.appCtx);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.appCtx = applicationContext;
	}
}