package demo.me.base.impl;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import demo.me.annotation.Bean;
import demo.me.annotation.Id;
import demo.me.base.BaseDao;
import demo.me.util.DruidUtil;

public class BaseDaoImpl<T> implements BaseDao<T> {

	private Connection conn;

	private QueryRunner qr;
	// 操作的xxxDao字节码对象
	private Class<T> clazz;
	// 操作的表明

	private String tableName;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		conn = DruidUtil.getConn();
		qr = new QueryRunner();

		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) type;
		clazz = (Class<T>) pt.getActualTypeArguments()[0];
		// tableName=clazz.getSimpleName();
		Bean[] classType = clazz.getAnnotationsByType(Bean.class);
		tableName = classType[0].value();
		System.out.println(tableName);
	}

	@Override
	public List<T> findAll() throws SQLException {
		String sql = "select * from " + tableName;
		List<T> list = qr.query(conn, sql, new BeanListHandler<>(clazz));
		return list;
	}

	@Override
	public T findById(int id) throws SQLException {
		String idFieldName = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				idFieldName = field.getName();
			}
		}
		String sql = "select * from " + tableName + " where " + idFieldName + "=?";
		System.out.println(sql);
		T entity = qr.query(conn, sql, new BeanHandler<>(clazz), new Object[] { id });

		return entity;
	}

	@Override
	public int add(T entity) throws Exception {
		Class<? extends Object> claz = entity.getClass();
		String sql = "insert into " + tableName + " ( ";
		Field[] fields = claz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (i != fields.length - 1) {
				sql += field.getName() + ",";
			} else {
				sql += field.getName() + ")";
			}

		}
		sql += " values(";
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (i != fields.length - 1) {
				sql += " ?,";
			} else {
				sql += "?)";
			}

		}
		System.out.println(sql);
		Object[] objs = new Object[fields.length];
		Object fieldValue = null;
		for (int i = 0; i < objs.length; i++) {
			Field field = fields[i];
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(), claz);
			fieldValue = pd.getReadMethod().invoke(entity, null);// 请求字段的get方法
			objs[i] = fieldValue;

		}
		int result = qr.update(conn, sql, objs);
		return result;
	}

	@Override
	public int delete(int id) throws SQLException {
		String idFieldName = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				idFieldName = field.getName();
			}
		}
		String sql = "delete from " + tableName + " where " + idFieldName + "=?";
		System.out.println(sql);
		int result = qr.update(conn, sql, new Object[] { id });
		return result;
	}

	@Override
	public int update(T entity) throws Exception {
		Class<? extends Object> claz = entity.getClass();
		String sql = "update " + tableName + " set ";
		Field[] fields = claz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (!field.isAnnotationPresent(Id.class)) {
				sql += field.getName() + " =?,";
			}
		}
		sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
		sql += " where ";
		String idFieldName = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				idFieldName = field.getName();
			}
		}

		sql += idFieldName + "=?";
		System.out.println(sql);
		Object[] objs = new Object[fields.length];
		Object fieldValue = null;
		int j = 0;// 加到数组中的位置
		for (int i = 0; i < objs.length; i++) {
			Field field = fields[i];
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(), claz);
			fieldValue = pd.getReadMethod().invoke(entity, null);// 请求字段的get方法
			if (!field.isAnnotationPresent(Id.class)) {
				objs[j] = fieldValue;
				j++;
			} else {
				objs[objs.length - 1] = fieldValue;
			}

		}
		int result = qr.update(conn, sql, objs);
		return result;
	}

}
