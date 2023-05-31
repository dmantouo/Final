package com.org.serviceImpl;

import com.google.gson.Gson;
import com.org.entity.User;
import com.org.mapper.UserMapper;
import com.org.message.Msg;
import com.org.utils.ExcelUtil;
import com.org.utils.RedisUtil;
import com.org.utils.TokenUtil;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl {

    @Resource
    UserMapper userMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    RedisUtil redisUtil;

    Gson gson = new Gson();

    //根据学号修改身份证号
    public Msg changeSidBySno(String sid,String sno, HttpServletRequest request, HttpServletResponse response){
        TokenUtil.addToken(request, response);
        userMapper.changeSidBySno(sid,sno);
        return Msg.success().add("detailMessage", "change personal ID success");
    }

    //找回密码
    public Msg findPassword(String sno, String sid, HttpServletRequest request, HttpServletResponse response) {
        int result = userMapper.findPassword(sno, sid);
        TokenUtil.addToken(request, response);
        if (result > 0) {
            //修改成功
            return Msg.success().add("detailMessage", "change success，original password is 12345");
        } else {
            //修改失败
            return Msg.fail().add("detailMessage", "ID or personal ID wrong");
        }
    }

    //根据学号查看个人详细信息
    public Msg selectStudentInfoBySno(String sno, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        return Msg.success().add("data", userMapper.selectUserInfoBySno(sno));
    }

    //根据旧密码修改新密码
    public Msg updatePasswordByPreviewPassword(String newPassword, String previewPassword, String sno, HttpServletRequest request, HttpServletResponse response) {

        TokenUtil.addToken(request, response);
        int result = userMapper.updatePasswordByPreviewPassword(newPassword, previewPassword, sno);
        if (result > 0) {
            return Msg.success().add("detailMessage", "change success");
        } else {
            return Msg.fail().add("detailMessage", "wrong original password");
        }

    }

    //统计学生信息
    public Msg studentStatistic(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        //return Msg.success().add("data", redisUtil.getStringValue("studentStatistic",userMapper.studentStatistic(),60));
        return Msg.success().add("data", redisUtil.getStringValue("studentStatistic",gson.toJson(userMapper.userStatistic())));
    }

    //修改学生信息
    public Msg updateInfo(String sno, String spassword, String sname, String ssex, int sage, String sdept, String sid, String power, String userSno, HttpServletRequest request, HttpServletResponse response) {

        //登录状态下
        TokenUtil.addToken(request, response);
        try {
            //修改成功
            userMapper.updateInfo(sno, spassword, sname, ssex, sage, sdept, sid, power, userSno);
            return Msg.success().add("detailMessage", "change success");
        } catch (Exception e) {
            //修改失败
            return Msg.fail().add("detailMessage", "student ID exists");
        }
    }

    //根据学号查看学生信息
    public Msg selectUserByNo(String sno, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        return Msg.success().add("data", userMapper.selectUserByNo(sno));
    }

    //查看所有信息
    public Msg selectAllUsers(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        String keys="Users";
        ListOperations<String, User> listOperations=redisTemplate.opsForList();
        if(!redisTemplate.hasKey(keys)){
            //0  -1查询所有数据
            //无此key则向redis存储
            List<User> Users=userMapper.selectAllUsers();
            listOperations.leftPushAll(keys,Users);

            //设置缓存时间
            redisTemplate.expire(keys,60,TimeUnit.SECONDS);

        }
        return Msg.success().add("detailMessage","success").add("data",listOperations.range(keys,0,-1));
    }

    //批量添加学生
    public Msg addMultipleUser(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        try {
            List<User> students = ExcelUtil.getMultipleStudent(file.getInputStream(), file.getOriginalFilename());
            for (int i = 0; i < students.size(); i++) {
                if (userMapper.JudgeUserIsExistBySno(students.get(i).getSno()) != null) {
                    return Msg.fail().add("detailMessage", "stdent ID exist");
                } else if (userMapper.JudgeUserIsExistBySid(students.get(i).getSid()) != null) {
                    return Msg.fail().add("detailMessage", "personal ID exist");
                } else if (!Pattern.compile("^\\d{12}$").matcher(students.get(i).getSno()).matches()) {
                    return Msg.fail().add("detailMessage", "wrong student ID type");
                } else if (!Pattern.compile("\\d{15}|\\d{18}").matcher(students.get(i).getSid()).matches()) {
                    return Msg.fail().add("detailMessage", "wrong personal ID type");
                } else if (!Pattern.compile("^\\d{2}$").matcher("" + students.get(i).getSage()).matches()) {
                    return Msg.fail().add("detailMessage", "age is 2-digit number");
                }
                int result=userMapper.addMultipleUser(students);
                if(result>0){
                    //添加成功
                    return Msg.success().add("detailMessage", "add success");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return Msg.fail().add("detailMessage", "wrong data type");
    }

    //登录
    public Msg login(String sno, String spassword, HttpServletRequest request, HttpServletResponse response) {
        User student = userMapper.login(sno, spassword);
        if (student != null) {
            //账号密码正确
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            stringRedisTemplate.opsForValue().set(token, sno,3600*24*3,TimeUnit.SECONDS);
            response.setHeader("Access-Control-Expose-Headers",
                    "Cache-Control,Content-Type,Expires,Pragma,Content-Language,Last-Modified,token");
            response.addHeader("token", token);
            return Msg.success().add("detailMessage", "login success").add("data", student);
        } else {
            //账号密码错误
            return Msg.fail().add("detailMessage", "ID or password wrong");
        }
    }

    //删除多个学生信息
    public Msg deleteMultipleStudent(String[] snoArrays, HttpServletRequest request, HttpServletResponse response) {
        userMapper.deleteMultipleUser(snoArrays);
        TokenUtil.addToken(request, response);
        return Msg.success().add("detailMessage", "delete success");
    }

    //删除单个学生信息
    public Msg deleteStudentByNo(String sno, HttpServletRequest request, HttpServletResponse response) {
        userMapper.deleteUserByNo(sno);
        TokenUtil.addToken(request, response);
        return Msg.success().add("detailMessage", "delete success");
    }

    //添加学生信息
    public Msg addStudent(HttpServletRequest request, HttpServletResponse response, User student) {
        TokenUtil.addToken(request, response);
       if (userMapper.JudgeUserIsExistBySno(student.getSno()) != null) {
            //学号已注册过了
            return Msg.fail().add("detailMessage", "student ID exist");
        } else {
            userMapper.addUser(student);
            return Msg.success().add("detailMessage", "add success");
        }
    }

    //分页条件查看所有学生信息
    public Msg selectStudentByConditionAndPage(String condition, int currentPage, int pageSize, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        return Msg.success().add("detailMessage", "success")
                .add("total", userMapper.allUserCounts(condition))
                .add("data", userMapper.selectUserByConditionAndPage(condition, currentPage, pageSize));
    }
}
