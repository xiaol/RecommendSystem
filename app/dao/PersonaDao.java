package dao;

import beans.Newsrecommendclick;
import beans.Newsrecommendread;
import beans.persona.AppInfo;
import beans.persona.UserProfile;
import util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshl on 16/9/18.
 */
public class PersonaDao {

    public static List<Newsrecommendclick> getUserClick(){
        Connection conn = JdbcUtil.getConn();
        String sql = "select c.uid, c.nid, c.ctime, n.chid from newsrecommendclick c inner join user_profile u2 on c.uid=u2.uid inner join newslist_v2 n on c.nid = n.nid  where c.ctime > (now() - interval '30 day')";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Newsrecommendclick> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Newsrecommendclick click = new Newsrecommendclick();
                click.setUid(rs.getInt(1));
                click.setNid(rs.getInt(2));
                click.setCtime(rs.getTimestamp(3));
                click.setChid(rs.getInt(4));
                list.add(click);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs != null){   // 关闭记录集
                try{
                    rs.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(conn != null){  // 关闭连接对象
                try{
                    conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
        return list;
    }

    public static List<Newsrecommendread> getUserRead(){
        Connection conn = JdbcUtil.getConn();
        String sql = "select uid, nid, readtime, chid from view_user_profile_readnews ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Newsrecommendread> list = new ArrayList<Newsrecommendread>();
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Newsrecommendread read = new Newsrecommendread();
                read.setUid(rs.getInt(1));
                read.setNid(rs.getInt(2));
                read.setReadtime(rs.getTimestamp(3));
                read.setChid(rs.getInt(4));
                list.add(read);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs != null){   // 关闭记录集
                try{
                    rs.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(conn != null){  // 关闭连接对象
                try{
                    conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
        return list;
    }

    public static List<UserProfile> getUserProfile(){
        Connection conn = JdbcUtil.getConn();
        String sql = "select uid, brand, model, ctime from user_profile ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<UserProfile> list = new ArrayList<UserProfile>();
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UserProfile user = new UserProfile();
                user.setUid(rs.getInt(1));
                user.setBrand(rs.getString(2));
                user.setModel(rs.getString(3));
                user.setCtime(rs.getTimestamp(4));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs != null){   // 关闭记录集
                try{
                    rs.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(conn != null){  // 关闭连接对象
                try{
                    conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
        return list;
    }

    public static List<AppInfo> getUserAppInfo(){
        Connection conn = JdbcUtil.getConn();
        String sql = "select uid, app_name from user_phone_apps  where id in (select max(id) from user_phone_apps group by uid, app_name)  order by uid ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AppInfo> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AppInfo app = new AppInfo();
                app.setUid(rs.getInt(1));
                app.setApp_name(rs.getString(2));
                list.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs != null){   // 关闭记录集
                try{
                    rs.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(conn != null){  // 关闭连接对象
                try{
                    conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
        return list;
    }




    public static void main(String[] args) {
        List<AppInfo> list = getUserAppInfo();
        for(AppInfo click: list){
            System.out.println(click.getUid()+"--"+click.getApp_name());
        }
    }
}
