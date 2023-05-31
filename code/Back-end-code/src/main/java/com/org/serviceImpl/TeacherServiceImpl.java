package com.org.serviceImpl;

import com.google.gson.Gson;
import com.org.entity.Teacher;
import com.org.entity.User;
import com.org.mapper.TeacherMapper;
import com.org.mapper.UserMapper;
import com.org.message.Msg;
import com.org.utils.RedisUtil;
import com.org.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class TeacherServiceImpl {
    @Resource
    TeacherMapper teacherMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    RedisUtil redisUtil;

    Gson gson = new Gson();

    //教师职称男女比例
    public Msg selectTeacherOrderByTpt(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        //return Msg.success().add("data",redisUtil.getStringValue("selectTeacherOrderByTpt",teacherMapper.selectTeacherOrderByTpt(),60));
        return Msg.success().add("data",redisUtil.getStringValue("selectTeacherOrderByTpt",gson.toJson(teacherMapper.selectTeacherOrderByTpt())));
    }

    //教师学历男女比例
    public Msg selectTeacherOrderByTeb(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        //return Msg.success().add("data",redisUtil.getStringValue("selectTeacherOrderByTeb",teacherMapper.selectTeacherOrderByTeb(),60));
        return Msg.success().add("data",redisUtil.getStringValue("selectTeacherOrderByTeb",gson.toJson(teacherMapper.selectTeacherOrderByTeb())));
    }

    //教师总数
    public Msg teacherCounts(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        //return Msg.success().add("data",redisUtil.getStringValue("teacherCounts",teacherMapper.teacherCounts(),60));
        return Msg.success().add("data",redisUtil.getStringValue("teacherCounts",gson.toJson(teacherMapper.teacherCounts())));

    }

    //修改教师信息
    public Msg updateTeacherInfo(String tno, String tname, int tage, String tsex, String teb, String tpt, String cno1, String cno2, String cno3, String teacherNo, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        try {
            teacherMapper.updateTeacherInfo(tno, tname, tage, tsex, teb, tpt, cno1, cno2, cno3, teacherNo);
            return Msg.success().add("detailMessage","修改成功");
        } catch (Exception e) {
            return Msg.fail().add("detailMessage", "教工号已存在,修改教师信息失败！！");
        }
    }

    //根据tno查询教师信息
    public Msg selectTeacherBytno(String tno, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        return Msg.success().add("data", teacherMapper.selectTeacherBytno(tno));
    }

    //获取所有老师信息
    public Msg selectAllTeacher(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        return Msg.success().add("data", teacherMapper.selectAllTeacher());
    }

    //批量删除教师信息
    public Msg deleteMultipleTeacher(String[] tnoArrays, HttpServletRequest request, HttpServletResponse response) {
        teacherMapper.deleteMultipleTeacher(tnoArrays);
        TokenUtil.addToken(request, response);
        return Msg.success();
    }

    //删除单个教师
    public Msg deleteTeacherByNo(String tno, HttpServletRequest request, HttpServletResponse response) {
        teacherMapper.deleteTeacherByNo(tno);
        TokenUtil.addToken(request, response);
        return Msg.success();
    }

    //添加教师信息
    public Msg addTeacher(String tno, String tname, String tsex, int tage, String teb, String tpt, String cno1,
                          String cno2, String cno3, String spassword, String sid,
                          HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher=new Teacher();
        teacher.setTno(tno);
        teacher.setTname(tname);
        teacher.setTsex(tsex);
        teacher.setTage(tage);
        teacher.setTeb(teb);
        teacher.setTpt(tpt);
        teacher.setCno1(cno1);
        teacher.setCno2(cno2);
        teacher.setCno3(cno3);

        User user = new User();
        user.setSno(tno);
        user.setSid(sid);
        user.setSpassword(spassword);
        user.setSname(tname);
        user.setSsex(tsex);
        user.setSage(tage);
        user.setPower(tpt);

        TokenUtil.addToken(request, response);
        try {
            teacherMapper.addTeacher(teacher);
            userMapper.addUser(user);
            return Msg.success().add("detailMessage","add success");
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail().add("detailMessage", "teacher ID exist");
        }
    }
    //条件分页查询
    public Msg selectTeacherByPageAndCondition(String condition, int currentPage, int pageSize, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request, response);
        return Msg.success()
                .add("total",teacherMapper.allTeacherCounts(condition))
                .add("data",teacherMapper.selectTeacherByPageAndCondition(condition, currentPage, pageSize));
    }
}
