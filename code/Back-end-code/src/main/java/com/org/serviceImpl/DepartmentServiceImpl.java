package com.org.serviceImpl;

import com.google.gson.Gson;
import com.org.entity.Department;
import com.org.mapper.DepartmentMapper;
import com.org.message.Msg;
import com.org.utils.RedisUtil;
import com.org.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class DepartmentServiceImpl{
    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    RedisUtil redisUtil;

    Gson gson=new Gson();
    //修改院系信息
    public Msg updateDepartmentInfo(String dno, String dname, String dmanager, String departmentNo, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request,response);
        try {
            if(departmentMapper.selectDepartmentByDname(dname)!=null){
                //
                return Msg.fail().add("detailMessage","faculty exist");
            }else{
                departmentMapper.updateDepartmentInfo(dno,dname,dmanager,departmentNo);
                return Msg.success().add("detailMessage","change success");
            }
        }catch (Exception e){
            return Msg.fail().add("detailMessage","faculty ID exist");
        }
    }

    //根据dno查看院系详细信息
    public Msg selectDepartmentByNo(String dno, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request,response);
        return Msg.success().add("detailMessage","success").add("data",departmentMapper.selectDepartmentByNo(dno));
    }

    //查看学院总人数和男女比例
    public Msg selectDepartmentRate(HttpServletRequest request, HttpServletResponse response) {
                //登录情况下
        TokenUtil.addToken(request,response);//departmentMapper.selectDepartmentRate()
        return Msg.success().add("detailMessage","success").add("data",redisUtil.getStringValue("selectDepartmentRate",gson.toJson(departmentMapper.selectDepartmentRate() )));
    }

    //学院总数
    public Msg departmentCount(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request,response);
        return Msg.success().add("detailMessage","success").add("total",redisUtil.getStringValue("departmentCount",gson.toJson(departmentMapper.departmentCount())));
    }

    //查看所有学院信息
    public Msg selectAllDepartment(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request,response);
        return Msg.success().add("detailMessage","success").add("data",departmentMapper.selectAllDepartment());
    }

    //删除单个学院信息
    public Msg deleteDepartment(String dno, HttpServletRequest request, HttpServletResponse response) {
        departmentMapper.deleteDepartment(dno);
        TokenUtil.addToken(request,response);
        return Msg.success().add("detailMessage","delete success");
    }

    //删除多个学院信息
    public Msg deleteMultipleDepartment(String[] dnoArrays, HttpServletRequest request, HttpServletResponse response) {
        departmentMapper.deleteMultipleDepartment(dnoArrays);
        TokenUtil.addToken(request,response);
        return Msg.success().add("detailMessage","delete success");
    }

    //添加学院
    public Msg addDepartment(Department department, HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.addToken(request,response);
        try {
            if(departmentMapper.selectDepartmentByDname(department.getDname())!=null){
                //
                return Msg.fail().add("detailMessage","faculty exist");
            }else{
                departmentMapper.addDepartment(department);
                return Msg.success().add("detailMessage","add success");
            }
        }catch (Exception e){
            return Msg.fail().add("detailMessage","faculty ID exist");
        }
    }

    //分页条件查看所有学院信息
    public Msg selectDepartmentByPageAndCondition(String condition, int currentPage, int pageSize, HttpServletRequest request, HttpServletResponse response) {

        TokenUtil.addToken(request,response);
        return Msg.success().add("detailMessage","success")
                .add("total",departmentMapper.allDepartmentCounts(condition))
                .add("data",departmentMapper.selectDepartmentByPageAndCondition(condition,currentPage,pageSize));
    }
}
