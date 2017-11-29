package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;

@Repository
public class SrcTestDaoImpl extends BaseDaoImpl<String>{
	
	/**
     * 通过SQL执行无返回结果的存储过程(仅限于存储过程)
     * @param queryString
     * @param params
     */
    public void executeVoidProcedureSql(final String queryString,final Object[] params) throws Exception{
        Session session = getSession();
        session.doWork(new Work(){
          public void execute(Connection conn) throws SQLException {
            ResultSet rs = null;
            CallableStatement call = conn.prepareCall("{" + queryString + "}");
            if (null != params) {
                for (int i = 0; i <params.length; i++) {
                    call.setObject(i+1, params[i]);
                }
            }
            rs = call.executeQuery();
            close( call, rs);
          }
       });
    }
    
    /**
     * 通过存储过程查询(单结果集)
     * @param sql 查询sql
     * @param params 参数
     * @param columnNum 返回的列数
     * @return
     */
    public List<Map<String, Object>> find_procedure(final String sql,final Object[] params){
        final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            Session session = sessionFactory.getCurrentSession();
            session.doWork(new Work(){
              public void execute(Connection conn) throws SQLException {
                  CallableStatement cs=null;
                  ResultSet rs=null;
                  cs = conn.prepareCall(sql);
                    for(int i=1;i<=params.length;i++){
                        cs.setObject(i, params[i-1]);//设置参数
                    }
                    rs = cs.executeQuery();
                    ResultSetMetaData metaData = rs.getMetaData();
                    int colCount=metaData.getColumnCount();
                    while(rs.next()){
                        Map<String, Object> map = new HashMap<String, Object>();
                        for(int i=1;i<=colCount;i++){
                            String colName=metaData.getColumnName(i);
                            map.put(colName, rs.getObject(colName));
                        }
                        result.add(map);
                    }
                    close( cs, rs);
              }
           });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 通过存储过程查询(多结果集)
     * @param sql 查询sql
     * @param params 参数
     * @param columnNum 返回的列数
     * @return
     */
    public List<List<Map<String, Object>>> find_procedure_multi(final String sql,final Object[] params){
        final List<List<Map<String, Object>>> result = new ArrayList<List<Map<String, Object>>>();
        try {
            Session session = sessionFactory.getCurrentSession();
            session.doWork(new Work(){
                public void execute(Connection conn) throws SQLException {
                    CallableStatement cs=null;
                    ResultSet rs=null;
                    cs = conn.prepareCall(sql);
                    for(int i=1;i<=params.length;i++){
                        cs.setObject(i, params[i-1]);
                    }
                    boolean hadResults = cs.execute();
                    ResultSetMetaData metaData = null;
                    while(hadResults){//遍历结果集
                        List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();//用于装该结果集的内容
                        rs = cs.getResultSet();//获取当前结果集
                        metaData=rs.getMetaData();
                        int colCount=metaData.getColumnCount();//获取当前结果集的列数
                        while(rs.next()){
                            Map<String, Object> map = new HashMap<String, Object>();
                            for(int i=1;i<=colCount;i++){
                                String colName=metaData.getColumnName(i);
                                map.put(colName, rs.getObject(colName));
                            }
                            rsList.add(map);
                        }
                        result.add(rsList);
                        close(null, rs);//遍历完一个结果集，将其关闭
                        hadResults=cs.getMoreResults();//移到下一个结果集
                    }
                    close(cs, rs);
                }
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void close(CallableStatement cs,ResultSet rs){
        try {
            if(cs!=null){
                cs.close();
            }
            if(rs!=null){
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
