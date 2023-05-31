package com.org.mapper;

import com.org.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface UserMapper {
    //根据学号修改身份证号
    @Update("update user set sid='${sid}' where sno='${sno}'")
    int changeSidBySno(@Param("sid")String sid,@Param("sno")String sno);

    //根据身份证判断用户存不存在
    @Select("select * from user where sid='${sid}'")
    User JudgeUserIsExistBySid(@Param("sid")String sid);

    //根据学号判断用户存不存在
    @Select("select * from user where sno='${sno}'")
    Map<String,Object> JudgeUserIsExistBySno(@Param("sno")String sno);

    //找回密码
    @Update("update user set spassword='${12345}' where sno='${sno}' and sid='${sid}'")
    int findPassword(@Param("sno")String sno,@Param("sid")String sid);

    //根据学号查看个人详细信息
    @Select("select * from user where sno='${sno}'")
    Map<String,Object> selectUserInfoBySno(@Param("sno")String sno);

    //根据旧密码修改新密码
    @Update("update user set spassword='${newPassword}' where spassword='${previewPassword}' and sno='${sno}' ")
    int updatePasswordByPreviewPassword(@Param("newPassword")String newPassword,@Param("previewPassword")String previewPassword,@Param("sno")String sno);

     //统计学生信息
    @Select("SELECT count(sname) as total,SUM(CASE WHEN ssex = 'boy' THEN 1 ELSE 0 END) AS boy,SUM(CASE WHEN ssex = 'girl' THEN 1 ELSE 0 END) AS girl FROM user WHERE power = 'student' ")
    Map<String,Integer> userStatistic();

    //修改学生信息
    @Update("update user set sno='${sno}',spassword='${spassword}',sname='${sname}',ssex='${ssex}',sage=${sage},sdept='${sdept}',sid='${sid}',power='${power}' where sno='${userNo}'")
    int updateInfo(@Param("sno")String sno,@Param("spassword")String spassword,@Param("sname")String sname,
                    @Param("ssex")String ssex,@Param("sage")int sage,@Param("sdept")String sdept,@Param("sid")String sid,@Param("power")String power,@Param("userNo")String userNo);
    //根据学号查询学生
    @Select("select * from user where sno='${sno}'")
    Map<String,Object> selectUserByNo(@Param("sno")String sno);

    //根据token查询学生
    @Select("select * from user where token='${token}'")
    Map<String,Object> selectUserByToken(@Param("token")String token);

    //根据学号更新学生token
    @Update("update user set token='${token}' where sno='${sno}'")
    void updateTokenByNo(@Param("token")String token,@Param("sno")String sno);

    //根据学号判断用户是否初次登录
    @Select("select token from user where sno='${sno}'")
    String tokenIsExist(@Param("sno")String sno);

    //登录
    @Select("select * from user where sno='${sno}' and spassword='${spassword}'")
    User login(@Param("sno")String sno, @Param("spassword")String spassword);

    //修改学生信息
    @Update("update user set sno='${sno}',sname='${sname}',ssex='${ssex}',sage=${sage},sdept='${sdept}' where sno='${sno}'")
    void updateUser();

    //添加学生
    @Insert("insert into user values('${sno}','${spassword}','${sname}','${ssex}',${sage},'${sdept}','${sid}','${power}')")
    void addUser(User user);

    //批量添加学生
    @Insert({
            "<script>",
            "insert into user values",
            "<foreach item='item' index='index' collection='users' separator= ',' >",
            "(#{item.sno},#{item.spassword},#{item.sname},#{item.ssex},#{item.sage},#{item.sdept},#{item.sid},#{item.power})",
            "</foreach>",
            "</script>"})
    int addMultipleUser(@Param("users")List<User> users);

    //分页条件查看所有学生信息
    @Select("select * from user where sname like '%${sname}%' and power = 'student' limit ${currentPage},${pageSize}")
    List<User> selectUserByConditionAndPage(@Param("sname")String sname, @Param("currentPage")int currentPage, @Param("pageSize")int pageSize);

    //分页条件查看所有学生信息的总量
    @Select("select count(*) from user where sname like '%${sname}%' and power = 'student' ")
    int allUserCounts(@Param("sname")String sname);

    //查看所有学生信息
    @Select("select * from user")
    List<User> selectAllUsers();

    //删除单个学生信息
    @Delete("delete from user where sno='${sno}'")
    void deleteUserByNo(@Param("sno")String sno);

    //批量删除学生信息
    @Delete({
            "<script>",
            "delete from user",
            "<where>",
            " sno in",
            "<foreach item='item' index='index' collection='snoArrays'  open= '('   separator= ',' close= ')'>",
            "#{item}",
            "</foreach>",
            "</where>",
            "</script>"})
    void deleteMultipleUser(@Param("snoArrays")String[] snoArrays);
}
