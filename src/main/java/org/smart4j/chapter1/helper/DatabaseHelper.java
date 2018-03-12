package org.smart4j.chapter1.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.service.CustomerService;
import org.smart4j.chapter1.utils.CollectionUtil;
import org.smart4j.chapter1.utils.PropsUtils;

import java.sql.*;
import java.util.*;

/**
 * 数据库操作助手类
 *
 * @author neutron
 */
public class DatabaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private static final ThreadLocal<Connection> holder;
    private static final QueryRunner runner;

    private static final BasicDataSource DATA_SOURCE;

    static {
        holder = new ThreadLocal<>();
        runner = new QueryRunner();

        Properties prop = PropsUtils.loadProps("config.properties");

        String driver = prop.getProperty("jdbc.driver");
        String url = prop.getProperty("jdbc.url");
        String userName = prop.getProperty("jdbc.username");
        String passWord = prop.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(userName);
        DATA_SOURCE.setPassword(passWord);
    }

    /**
     * 获取数据库连接
     * @return Connection
     */
    public static Connection getConnection() {
        Connection conn = holder.get();
        if (Objects.isNull(conn)) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                logger.error("execute sql failure", e);
                throw new RuntimeException(e);
            } finally {
                holder.set(conn);
            }
        }
        return conn;
    }

    /**
     * 查询对象列表
     * @param entityClass 实体对象类对象
     * @param sql         sql
     * @param params      动态参数数组
     * @param <T>
     * @return
     */
    public static <T> List<T>  queryEntityList(Class<T> entityClass, String sql, Object ... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = runner.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("query entity list failure", e);
            throw new RuntimeException(e);
        }

        return entityList;
    }

    /**
     * 查询单个对象
     * @param entityClass 类对象
     * @param sql         sql
     * @param params      参数动态数组
     * @param <T>
     * @return
     */
    public static <T> T  queryEntity(Class<T> entityClass, String sql, Object ... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = runner.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("query entity failure", e);
            throw new RuntimeException(e);
        }

        return entity;
    }

    /**
     * 执行查询语句
     * @param sql         sql
     * @param params      参数动态数组
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object ... params) {
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            result = runner.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            logger.error("query entity failure", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 执行更新语句
     * @param sql         sql
     * @param params      参数动态数组
     * @return
     */
    public static int executeUpdate(String sql, Object ... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = runner.update(conn, sql, params);
        } catch (SQLException e) {
            logger.error("execute update", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 执行更新语句
     * @param entityClass   实体类的类对象
     * @param fieldMap      参数动态数组
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            logger.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     * @param entityClass   实体类的类对象
     * @param id            实体类的类对象
     * @param fieldMap      参数动态数组
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            logger.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuffer columns = new StringBuffer("(");

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" =?, ");
        }

        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 删除实体
     * @param entityClass   实体类的类对象
     * @param id            实体类的类对象
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ? ";
        return executeUpdate(sql, id) == 1;
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }

}