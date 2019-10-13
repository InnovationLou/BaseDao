package demo.me.base;

import java.sql.SQLException;
import java.util.List;


public interface BaseDao<T> {
	public List<T> findAll() throws SQLException;
	public T findById(int id) throws SQLException;
	public int add(T entity) throws Exception;
	public int delete(int id) throws SQLException;
	public int update(T entity) throws Exception;
}
