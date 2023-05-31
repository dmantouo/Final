import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);
const store=new Vuex.Store({
    state:{
        token:null,
        menuItems:[{
            id:1,           
            name:'Information', //router name   
            path:'',         
            type:'el-icon-info',
            children:[
                {
                    id:11,
                    name:'student',
                    path:'/studentInfo',
                    type:'el-icon-user',
                    canSee:false
                },
                {
                    id:12,
                    name:'teacher',
                    path:'/teacherInfo',
                    type:'el-icon-user-solid',
                    canSee:true
                },
                {
                    id:13,
                    name:'faculties',
                    path:'departmentInfo',
                    type:'el-icon-school',
                    canSee:true
                },
                {
                    id:14,
                    name:'course',
                    path:'courseInfo',
                    type:'el-icon-notebook-2',
                    canSee:true
                },
                {
                    id:15,
                    name:'my student',
                    path:'sctInfo',
                    type:'el-icon-notebook-1',
                    canSee:false
                },
                {
                    id:16,
                    name:'my course',
                    path:'MySctInfo',
                    type:'el-icon-notebook-1',
                    canSee:true
                }
            ]
        },{
            id:2,
            name:'Data',
            path:'',
            type:'el-icon-s-operation',
            children:[
                {
                    id:21,
                    name:'student data',
                    path:'studentStatistic',
                    type:'el-icon-s-management',
                    canSee:true
                },
                {
                    id:22,
                    name:'teacher data',
                    path:'teacherStatistic',
                    type:'el-icon-upload',
                    canSee:true
                },{
                    id:23,
                    name:'faculties data',
                    path:'departmentStatistic',
                    type:'el-icon-document-copy',
                    canSee:true
                },{
                    id:24,
                    name:'course data',
                    path:'courseStatistic',
                    type:'el-icon-document',
                    canSee:true
                },{
                    id:25,
                    name:'course selection data',
                    path:'sctStatistic',
                    type:'el-icon-tickets',
                    canSee:false
                }
            ]
        },{
            id:3,
            name:'Help',
            path:'',
            type:'el-icon-help',
            children:[
                {
                    id:31,
                    name:'Help booklet',
                    path:'help',
                    type:'el-icon-s-help',
                    canSee:true
                }
            ]
        }]
    }
})

export default store