package com.org.controller;

import com.org.entity.User;
import com.org.message.Msg;
import com.org.serviceImpl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RestController
public class UserController {
    @Resource
    UserServiceImpl userServiceImpl;

    //根据学号修改身份证号
    @RequestMapping("/changeSidBySno")
    public Msg changeSidBySno(@RequestParam("sid")String sid,@RequestParam("sno")String sno,HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.changeSidBySno(sid,sno,request,response);
    }
    //找回密码
    @RequestMapping("/findPassword")
    public Msg findPassword(@RequestParam("sno")String sno,@RequestParam("sid")String sid,HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.findPassword(sno,sid,request,response);
    }

    //根据学号查看个人详细信息
    @GetMapping("/selectStudentInfoBySno")
    public Msg selectStudentInfoBySno(@RequestParam("sno")String sno,HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.selectStudentInfoBySno(sno,request,response);
    }

    //根据旧密码修改新密码
    @RequestMapping("/updatePasswordByPreviewPassword")
    public Msg updatePasswordByPreviewPassword(@RequestParam("newPassword")String newPassword,@RequestParam("previewPassword")String previewPassword,
                                                   @RequestParam("sno")String sno,
                                                   HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.updatePasswordByPreviewPassword(newPassword,previewPassword,sno,request,response);
    }

    //学生信息统计
    @RequestMapping("/studentStatistic")
    public Msg studentStatistic(HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.studentStatistic(request,response);
    }

    //修改学生信息
    @RequestMapping("/updateInfo")
    public Msg updateInfo(@RequestParam("sno")String sno,@RequestParam("spassword")String spassword,
                              @RequestParam("sname")String sname,@RequestParam("ssex")String ssex,
                              @RequestParam("sage")int sage,@RequestParam("sdept")String sdept,
                              @RequestParam("sid")String sid,@RequestParam("power")String power,
                              @RequestParam("studentNo")String studentNo,HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.updateInfo(sno,spassword,sname,ssex,sage,sdept,sid,power,studentNo,request,response);
    }

    //根据学号查看学生信息
    @RequestMapping("/selectStudentByNo")
    public Msg selectStudentByNo(@RequestParam("sno")String sno,HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.selectUserByNo(sno,request,response);
    }

    //查看所有学生
    @RequestMapping("/selectAllStudents")
    public Msg selectAllUsers(HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.selectAllUsers(request,response);
    }
    //添加批量学生
    @RequestMapping("/addMultipleStudent")
    public Msg addMultipleUser(@RequestParam("file")MultipartFile file,HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        return userServiceImpl.addMultipleUser(file ,request,response);
    }

    //登录
    @RequestMapping("/login")
    public Msg login(@RequestParam("sno")String sno, @RequestParam("spassword")String spassword,
                     HttpServletRequest request, HttpServletResponse response){
        return userServiceImpl.login(sno,spassword,request,response);
    }

    //删除多个学生信息
    @RequestMapping("/deleteMultipleStudent")
    public Msg deleteMultipleStudent(@RequestParam("snoArrays")String[] snoArrays,HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.deleteMultipleStudent(snoArrays,request,response);
    }
    //删除单个学生信息
    @RequestMapping("/deleteStudentByNo")
    public Msg deleteStudentByNo(@RequestParam("sno")String sno,HttpServletRequest request,
                                     HttpServletResponse response){
        return userServiceImpl.deleteStudentByNo(sno,request,response);
    }

    //分页条件查询学生信息
    @RequestMapping("/selectStudentByConditionAndPage")
    public Msg selectStudentByConditionAndPage(@RequestParam("condition")String condition,@RequestParam("currentPage")int currentPage,
                                                   @RequestParam("pageSize")int pageSize,HttpServletRequest request,HttpServletResponse response){
        return userServiceImpl.selectStudentByConditionAndPage(condition,(currentPage-1)*pageSize,pageSize,request,response);
    }

    //添加学生
    @RequestMapping("/addStudent")
    public Msg addStudent(HttpServletRequest request, HttpServletResponse response,
                          User student){
       return userServiceImpl.addStudent(request,response,student);
    }
}
