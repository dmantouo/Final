<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Data</el-breadcrumb-item>
            <el-breadcrumb-item>course selection data</el-breadcrumb-item>
        </el-breadcrumb>

        <el-card shadow="always" id="studyCourseFromStudent">
            <el-row>
                <el-col :span="8">
                    <el-input v-model="queryInfo.condition" placeholder="search by course name/student name" clearable >
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="5" :offset="3">
                    <el-select v-model="value" placeholder="please select" @change="changeSelect">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="3" :offset="5">
                    <el-button type="success" @click="exportToExcel">Data export</el-button>
                </el-col>
            </el-row>

            <!--students for each course-->
            <el-table
                    v-if="judgeSelection"
                    stripe
                    border
                    :data="courseData">
                <el-table-column
                        prop="tname"
                        label="teacher"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="cname"
                        label="course name"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="ccredit"
                        label="credits">
                </el-table-column>
                <el-table-column
                        prop="total"
                        label="number">
                </el-table-column>
            </el-table>

            <!--course for each student-->
            <el-table
                   v-else
                    stripe
                    border
                    :data="studentData">
                <el-table-column
                        prop="sname"
                        label="student"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="total_course"
                        label="total course"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="total_ccredit"
                        label="total credits">
                </el-table-column>
            </el-table>

            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[1,2,5,10]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>


        </el-card>
    </div>
</template>

<script>
    export default {
        name: "sctStatistic",
        data(){
            return {
                total:0,
                queryInfo:{
                    condition:'',
                    currentPage:1,
                    pageSize:5
                },
                courseData: [],
                options: [{
                    value: 'students for each course',
                    label: 'students for each course'
                }, {
                    value: 'course for each student',
                    label: 'course for each student'
                }],
                value: 'students for each course',
                line_x_data:[],    
                line_y_data:[],     
                studentData:[],
            }
        },
        computed:{

            judgeSelection:function () {
                if(this.value=='students for each course'){
                    this.studyCourseFromStudent();
                    return true;
                }else{
                    this.studentStudyCourse();
                    return false;
                }
            }
        },
        methods:{
            handleSizeChange(val){
                this.queryInfo.pageSize=val;
                if(this.value=='students for each course'){
                    //students for each course
                    this.studyCourseFromStudent();
                }else{
                    this.studentStudyCourse();
                }
            },
            handleCurrentChange(val) {
                this.queryInfo.currentPage=val;
                if(this.value=='students for each course'){
                    //students for each course
                    this.studyCourseFromStudent();
                }else{
                    this.studentStudyCourse();
                }
            },
            changeSelect(val){
                if(val=='students for each course'){
                    this.studyCourseFromStudent();
                }else {
                    this.studentStudyCourse();
                }
            },
            studyCourseFromStudent(){
                let that=this;
                let params={
                    condition:this.queryInfo.condition,
                    currentPage:this.queryInfo.currentPage,
                    pageSize:this.queryInfo.pageSize
                }
                this.$axios.get(this.apiUrl.studyCourseFromStudent,{
                    params:params
                }).then(response=>{
                    that.total=response.data.data.total;
                    that.courseData=response.data.data.data;

                }).catch(error=>{
                    console.log('---error--',error);
                })
            },
            studentStudyCourse(){
                let that=this;
                let params={
                    condition:this.queryInfo.condition,
                    currentPage:this.queryInfo.currentPage,
                    pageSize:this.queryInfo.pageSize
                }
                this.$axios.get(this.apiUrl.studentStudyCourse,{
                    params:params
                }).then(response=>{
                    that.total=response.data.data.total;
                    that.studentData=response.data.data.data;
                }).catch(error=>{
                    console.log('----error---',error);
                })
            },
            exportToExcel(){
                if(this.value=='students for each course'){
                    //students for each course
                    this.studyCourseFromStudentToExcel();
                }else{
                    this.studentStudyCourseToExcel();
                }
            },
            showLine(){
                //折线图显示
                let data=this.getData();
                let myChart = this.$echarts.init(document.getElementById('studyCourseFromStudent'),"light");
                let option = {
                    title: {
                        text: '每门课程多少学生修',
                        left: 'center'
                    },
                    xAxis: {
                        type: "category",
                        data: data.line_x_data
                    },
                    yAxis: {},
                    series: [{
                        data: data.line_y_data,
                        type: "line"
                    }]
                }
                myChart.setOption(option);
            },

            studyCourseFromStudentToExcel(){
                require.ensure([], () => {
                    let { exportJsonToExcel } = require("../excel/Export2Excel.js");
                    let tHeader = [
                        "teacher",
                        "course name",
                        "credits",
                        "number",
                    ];

                    let filterVal = [
                        "tname",
                        "cname",
                        "ccredit",
                        "total",
                    ];
                    let list = this.courseData;
                    let data = this.formatJson(filterVal, list); 
                    var index1 = "student for course"; 
                    exportJsonToExcel(tHeader, data, index1); 
                });
            },

            studentStudyCourseToExcel(){
                require.ensure([], () => {
                    let { exportJsonToExcel } = require("../excel/Export2Excel.js");
                    let tHeader = [
                        "student",
                        "total courses",
                        "total credits",
                    ];

                    let filterVal = [
                        "sname",
                        "total_course",
                        "total_ccredit",
                    ];
                    let list = this.studentData;
                    let data = this.formatJson(filterVal, list); 
                    var index1 = "course for student"; 
                    exportJsonToExcel(tHeader, data, index1); 
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]));
            },
        }
    }
</script>

<style scoped>
    .el-table{
        margin:15px auto;
    }
    .el-card{
        margin:15px 0;
        padding:10px;
    }
</style>