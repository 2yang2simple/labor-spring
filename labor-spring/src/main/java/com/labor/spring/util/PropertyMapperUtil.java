package com.labor.spring.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.logging.log4j.LogManager;

import cn.hutool.core.convert.Convert;

public class PropertyMapperUtil {
	/**
	 * Entity and Dto Mapper
	 * 
	 * @param entry
	 * @param dto
	 * @param enToDto ture : Entity To Dto (defult) false : Dto To Entry Rule:
	 *                实现相互转换前提: Dto field name(dto和entry的field name相同并且
	 *                类上有@RelMapper) 或 field的@RelMapper(value="Entity field name")
	 *                满足其一即可转换
	 * @return
	 * @throws Exception
	 */
	public static Object entryAndDtoMapper(Object entity, Object dto) throws Exception {
		return EnAndDtoMapper(entity, dto, true);
	}

	public static Object entryAndDtoMapper(Object entity, Object dto, boolean enToDto) throws Exception {
		return EnAndDtoMapper(entity, dto, false);
	}

	// last version
	public static Object EnAndDtoMapper(Object entry, Object dto, boolean enToDto) throws Exception {
		if (enToDto == true ? entry == null : dto == null) {
			return null;
		}
		Class<? extends Object> entryclazz = entry.getClass(); // 获取entity类
		Class<? extends Object> dtoclazz = dto.getClass(); // 获取dto类
		boolean dtoExistAnno = dtoclazz.isAnnotationPresent(PropertyMapper.class); // 判断类上面是否有自定义注解
		Field[] dtofds = dtoclazz.getDeclaredFields(); // dto fields
		Field[] entryfds = entryclazz.getDeclaredFields(); // entity fields
		Method entrys[] = entryclazz.getDeclaredMethods(); // entity methods
		Method dtos[] = dtoclazz.getDeclaredMethods(); // dto methods
		String mName, fieldName, dtoFieldType = null, entFieldType = null, dtoMapName = null, dtoFieldName = null;
		Object value = null;
		for (Method m : (enToDto ? dtos : entrys)) { // 当 enToDto=true 此时是Entity转为Dto，遍历dto的属性
			if ((mName = m.getName()).startsWith("set")) { // 只进set方法
				fieldName = mName.toLowerCase().charAt(3) + mName.substring(4, mName.length()); // 通过set方法获得dto的属性名
				tohere: for (Field fd : dtofds) {
					fd.setAccessible(true); // setAccessible是启用和禁用访问安全检查的开关
					if (fd.isAnnotationPresent(PropertyMapper.class) || dtoExistAnno) {
						// 判断field上注解或类上面注解是否存在
						// 获取与Entity属性相匹配的映射值(两种情况：1.该field上注解的value值(Entity的field name 和Dto 的field name
						// 不同) 2.该field本身(本身则是Entity的field name 和Dto 的field name 相同))
						dtoMapName = fd.isAnnotationPresent(PropertyMapper.class)
								? (fd.getAnnotation(PropertyMapper.class).value().toString().equals("")
										? fd.getName().toString()
										: fd.getAnnotation(PropertyMapper.class).value().toString())
								: fd.getName().toString();
						if (((enToDto ? fd.getName() : dtoMapName)).toString().equals(fieldName)) {
							dtoFieldType = fd.getGenericType().toString()
									.substring(fd.getGenericType().toString().lastIndexOf(".") + 1); // 获取dto属性的类型(如
																										// private
																										// String field
																										// 结果 = String)
							for (Field fe : entryfds) {
								fe.setAccessible(true);
								if (fe.getName().toString().equals(enToDto ? dtoMapName : fieldName)) {// 遍历Entity类的属性与dto属性注解中的value值匹配
									entFieldType = fe.getGenericType().toString()
											.substring(fe.getGenericType().toString().lastIndexOf(".") + 1); // 获取Entity类属性类型
									dtoFieldName = enToDto ? dtoMapName : fd.getName().toString();
									break tohere;
								}
							}
						}
					}
				}
				if (dtoFieldName != null && !dtoFieldName.equals("null")) {
					for (Method md : (enToDto ? entrys : dtos)) {
						if (md.getName().toUpperCase().equals("GET" + dtoFieldName.toUpperCase())) {
							dtoFieldName = null;
							if (md.invoke(enToDto ? entry : dto) == null) {
								break;
							} // 去空操作
								// Entity类field 与Dto类field类型不一致通过TypeProcessor处理转换
							value = (entFieldType.equals(dtoFieldType)) ? md.invoke(enToDto ? entry : dto)
									: TypeProcessor(entFieldType, dtoFieldType, md.invoke(enToDto ? entry : dto),
											enToDto ? true : false);
							m.invoke(enToDto ? dto : entry, value); // 得到field的值 通过invoke()赋值给要转换类的对应属性
							value = null;
							break;
						}
					}
				}
			}
		}
		return enToDto ? dto : entry;
	}

	// 类型转换处理
	public static Object TypeProcessor(String entFieldType, String dtoFieldType, Object obj, boolean enToDto) {
		if (entFieldType.equals(dtoFieldType))
			return obj;

		if (!entFieldType.equals(dtoFieldType)) {
			switch (entFieldType) {
			case "Date":
//				return (enToDto) ? TypeConverter.dateToString((Date) obj) : TypeConverter.stringToDate(obj.toString());
//			case "Timestamp":
//				return TypeConverter.timestampToTimestampString((Timestamp) obj);
			case "Integer":
				return (enToDto) ? obj.toString() : Integer.parseInt((String) obj);
			}
		}
		return obj;
	}

	private static Object processObj(String fromType, String toType, Object obj) {
		if (fromType.equals(toType))
			return obj;

		if (!fromType.equals(toType)) {
			switch (toType) {
			case "Date":
				return Convert.toDate(obj);
			case "Integer":
				return Convert.toInt(obj);
			}
		}
		return obj;
	}

	public static Object copyProperties(Object fromObj, Object toObj)  {
		if (fromObj == null || toObj == null) {
			return null;
		}
		Class<? extends Object> fromClazz = fromObj.getClass(); // 获取entity类
		Class<? extends Object> toClazz = toObj.getClass(); // 获取dto类
		boolean toExistAnno = toClazz.isAnnotationPresent(PropertyMapper.class); // 判断类上面是否有自定义注解
		Field[] fromFields = fromClazz.getDeclaredFields(); 
		Field[] toFields = toClazz.getDeclaredFields(); 
		Method fromMethods[] = fromClazz.getDeclaredMethods(); // entity methods
		Method toMethods[] = toClazz.getDeclaredMethods(); // dto methods
		
		String mName, fieldName, fromFieldType = null, toFieldType = null, toMapName = null, toFieldName = null;
		Object value = null;
		for (Method m : toMethods) { // 当 enToDto=true 此时是Entity转为Dto，遍历dto的属性
			if ((mName = m.getName()).startsWith("set")) { // 只进set方法
				fieldName = mName.toLowerCase().charAt(3) + mName.substring(4, mName.length()); // 通过set方法获得dto的属性名
				tohere: for (Field fd : toFields) {
					fd.setAccessible(true); // setAccessible是启用和禁用访问安全检查的开关
					if (fd.isAnnotationPresent(PropertyMapper.class) || toExistAnno) {
						// 判断field上注解或类上面注解是否存在
						// 获取与Entity属性相匹配的映射值
						//(两种情况：
						//1.该field上注解的value值(Entity的field name 和Dto 的field name不同) 
						//2.该field本身(本身则是Entity的field name 和Dto 的field name 相同))
						toMapName = fd.isAnnotationPresent(PropertyMapper.class)
								? (fd.getAnnotation(PropertyMapper.class).value().toString().equals("")
										? fd.getName().toString()
										: fd.getAnnotation(PropertyMapper.class).value().toString())
								: fd.getName().toString();
						if ((fd.getName()).toString().equals(fieldName)) {
							toFieldType = fd.getGenericType().toString()
									.substring(fd.getGenericType().toString().lastIndexOf(".") + 1); 
							// 获取dto属性的类型(如
							// private
							// String field
							// 结果 = String)
							for (Field fe : fromFields) {
								fe.setAccessible(true);
								if (fe.getName().toString().equals(toMapName)) {// 遍历Entity类的属性与dto属性注解中的value值匹配
									fromFieldType = fe.getGenericType().toString()
											.substring(fe.getGenericType().toString().lastIndexOf(".") + 1); // 获取Entity类属性类型
									toFieldName = toMapName;
									break tohere;
								}
							}
						}
					}
				}
				if (toFieldName != null && !toFieldName.equals("null")) {
					for (Method md : fromMethods) {
						if (md.getName().toUpperCase().equals("GET" + toFieldName.toUpperCase())) {
							toFieldName = null;
							try {
								if (md.invoke(fromObj) == null) {
									break;
								} 
								// 去空操作
								// Entity类field 与Dto类field类型不一致通过TypeProcessor处理转换
								value = processObj(fromFieldType, toFieldType, md.invoke(fromObj));
								m.invoke(toObj, value); // 得到field的值 通过invoke()赋值给要转换类的对应属性
								value = null;
								break;
							} catch (IllegalArgumentException ilae) {
								LogManager.getLogger().error(ilae);
								break;
							} catch (InvocationTargetException ite) {
								LogManager.getLogger().error(ite);
								break;
							} catch (IllegalAccessException iae) {
								LogManager.getLogger().error(iae);
								break;
							}
						}
					}
				}
			}
		}
		return toObj;
	}
}
