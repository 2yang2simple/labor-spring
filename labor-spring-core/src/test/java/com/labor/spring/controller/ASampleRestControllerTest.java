//package com.labor.spring.controller;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import com.labor.spring.LaborSpringCoreApplicationTest;
//import com.labor.spring.bean.Result;
//
//
//public class ASampleRestControllerTest extends LaborSpringCoreApplicationTest {
//
//    
//    @Test
//	public void aopand() {
//		Result result = restTemplate.getForObject("/rest/core/samples/aopand", Result.class);
//		System.err.println(result.getData());
//		Assert.assertEquals("yes", result.getData());
//	}
//	@Test
//	public void test() {
//		Result result = restTemplate.getForObject("/rest/core/samples/test", Result.class);
//		System.err.println(result.getData());
//		Assert.assertEquals("yes", result.getData());
//	}
//   
//}
