package temp;

import util.ConnectionPool;

import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangshl on 17/2/14.
 */
public class CountPVForHour {
    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        SimpleDateFormat hour_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestr = "2017-01-19 21:43:00";
        PreparedStatement pstmt = null;
        try {
            for (int i=0; i<59; i++){
                String sql = "SELECT count(1) FROM pvdetail where ctime > ? and ctime < ? ";

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(hour_sdf.parse(timestr).getTime());
                    calendar1.add(Calendar.SECOND, i);

                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTimeInMillis(hour_sdf.parse(timestr).getTime());
                    calendar2.add(Calendar.SECOND, i+1);

                    String time1 = hour_sdf.format(calendar1.getTime());
                    String time2 = hour_sdf.format(calendar2.getTime());

                    Timestamp timestamp1 = new Timestamp(hour_sdf.parse(time1).getTime());
                    Timestamp timestamp2 = new Timestamp(hour_sdf.parse(time2).getTime());

                    System.out.println(hour_sdf.parse(time1)+"-----"+hour_sdf.parse(time2));

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setTimestamp(1, timestamp1);
                    pstmt.setTimestamp(2, timestamp2);

                    ResultSet rs = pstmt.executeQuery();

                    while(rs.next()){
                        System.out.println(rs.getInt(1));
                    }

                pstmt.close();

            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            if(conn != null){   // 关闭声明
                try{
                    conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }

    }
}
