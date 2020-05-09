//package com.labor.spring.config;
//
//import org.apache.logging.log4j.LogManager;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
//import org.springframework.cache.interceptor.CacheErrorHandler;
//import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//@Configuration
//@EnableCaching
//public class CacheEhcacheConfig extends CachingConfigurerSupport {
//	
//	@Bean
//	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
//		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//		return ehCacheManagerFactoryBean;
//	}
//
//	@Bean
//	@Override
//	public CacheManager cacheManager() {
//		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
//		cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
//		return cacheManager;
//	}
//
//
//	@Override
//	public CacheErrorHandler errorHandler() {
//		return new LoggingCacheErrorHandler();
//	}
//
//	/* non-public */
//	static class LoggingCacheErrorHandler extends SimpleCacheErrorHandler {
//
//		@Override
//		public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
//			LogManager.getLogger().error(
//					String.format("cacheName:%s,cacheKey:%s", cache == null ? "unknown" : cache.getName(), key),
//					exception);
//			super.handleCacheGetError(exception, cache, key);
//		}
//
//		@Override
//		public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
//			LogManager.getLogger().error(
//					String.format("cacheName:%s,cacheKey:%s", cache == null ? "unknown" : cache.getName(), key),
//					exception);
//			super.handleCachePutError(exception, cache, key, value);
//		}
//
//		@Override
//		public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
//			LogManager.getLogger().error(
//					String.format("cacheName:%s,cacheKey:%s", cache == null ? "unknown" : cache.getName(), key),
//					exception);
//			super.handleCacheEvictError(exception, cache, key);
//		}
//
//		@Override
//		public void handleCacheClearError(RuntimeException exception, Cache cache) {
//			LogManager.getLogger().error(String.format("cacheName:%s", cache == null ? "unknown" : cache.getName()),
//					exception);
//			super.handleCacheClearError(exception, cache);
//		}
//	}
//
//}
