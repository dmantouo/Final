/*
* import vue and router
* */

import Vue from 'vue'
import Router from 'vue-router'

const login=()=>import("../views/login")
const index=()=>import("../views/index")
const help=()=>import("../views/help")
const teacherInfo=()=>import("../views/teacherInfo")
const studentInfo=()=>import("../views/studentInfo")
const departmentInfo=()=>import("../views/departmentInfo")
const courseInfo=()=>import("../views/courseInfo")
const sctInfo=()=>import("../views/sctInfo")
const studentStatistic=()=>import("../views/studentStatistic")
const departmentStatistic=()=>import("../views/departmentStatistic")
const teacherStatistic=()=>import("../views/teacherStatistic")
const courseStatistic=()=>import("../views/courseStatistic")
const sctStatistic=()=>import("../views/sctStatistic")
const MySctInfo=()=>import("../views/MySctInfo")
const welcome=()=>import("../views/welcome")

Vue.use(Router)     

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
export default new Router({
    mode:'history',
    routes:[
        {
            path:'/login',
            component: login,
            name:'login'
        },
        {
            path:'/',
            component: login,
            name:'login'
        },
        {
            path:'/index',
            component:index,
            name:'',
            redirect:'/welcome',
            children:[
                {
                    path:'/welcome',
                    component:welcome,
                    name:'welcome'
                },
                {
                    path:'/help',
                    component:help,
                    name:'help'
                },
                {
                    path:'/studentInfo',
                    component:studentInfo,
                    name:'studentInfo'
                },
                {
                    path:'/teacherInfo',
                    component:teacherInfo,
                    name:'teacherInfo'
                },{
                    path:'/departmentInfo',
                    component:departmentInfo,
                    name:'departmentInfo'
                },{
                    path:'/courseInfo',
                    component:courseInfo,
                    name:'courseInfo'
                },{
                    path:'/sctInfo',
                    component:sctInfo,
                    name:'sctInfo'
                },{
                    path:'/studentStatistic',
                    component:studentStatistic,
                    name:'studentStatistic'
                },{
                    path:'/departmentStatistic',
                    component:departmentStatistic,
                    name:'departmentStatistic'
                },
                {
                    path:'/teacherStatistic',
                    component:teacherStatistic,
                    name:'teacherStatistic'
                },
                {
                    path:'/courseStatistic',
                    component:courseStatistic,
                    name:'courseStatistic'
                },{
                    path:'/sctStatistic',
                    component:sctStatistic,
                    name:'sctStatistic'
                },{
                    path:'/MySctInfo',
                    component:MySctInfo,
                    name:'MySctInfo'
                }
            ]
        }
    ]
})