<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Information</el-breadcrumb-item>
            <el-breadcrumb-item>course</el-breadcrumb-item>
        </el-breadcrumb>

        <!--Card-->
        <el-card shadow="always">
            <!--search and button-->
            <el-row :gutter="10">
                <el-col :span="8">
                    <el-input v-model="queryInfo.condition" placeholder="please enter course name" clearable @clear="selectCourseByConditionAndPage">
                        <el-button slot="append" icon="el-icon-search" @click="selectCourseByConditionAndPage"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="2" :offset="1">
                    <el-button type="primary" @click="judgeGrade()">add course</el-button>
                </el-col>
                <el-col :span="2" :offset="8">
                    <el-button type="success" @click="exportToExcel">export course</el-button>
                </el-col>
            </el-row>

            <!--table-->
            <el-table
                    :data="tableData"
                    stripe
                    border
                    style="width: 100%"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column  type="selection" width="40">
                </el-table-column>
                <el-table-column
                        prop="cno"
                        label="course ID" width="150">
                </el-table-column>
                <el-table-column
                        prop="cname"
                        label="Course name" width="150">
                </el-table-column>
                <el-table-column
                        prop="tname"
                        label="teacher" width="100">
                </el-table-column>
                <el-table-column
                        prop="ccredit"
                        label="credits" width="70">
                </el-table-column>
                <el-table-column
                        prop="pre_cno"
                        label="pre-course ID" width="150">
                </el-table-column>
                <el-table-column
                        prop="pre_cname"
                        label="pre-course name" width="169">
                </el-table-column>
                <el-table-column
                        prop="pre_ccredit"
                        label="pre-course credits" width="169">
                </el-table-column>
                <el-table-column label="operation">
                    <template slot-scope="scope">
                        <el-button type="success" icon="el-icon-thumb" size="mini" @click="chooseCourse(scope.row.cno,scope.row.tno)"></el-button>
                        <el-button type="primary" icon="el-icon-edit" size="mini" @click="showInfo(scope.row.cno)"></el-button>
                        <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteCourse(scope.row.cno)"></el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!--Page Break-->
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryInfo.currentPage"
                    :page-sizes="[1,2,5,10]"
                    :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </el-card>

        <!--add course-->
        <el-dialog title="add course" :visible.sync="dialogFormVisible" @close="resetDialog">
            <el-form :model="form" ref="form" :rules="rules">
                <el-form-item label="course ID" :label-width="formLabelWidth" prop="cno">
                    <el-input v-model="form.cno" placeholder="please input course ID" clearable></el-input>
                </el-form-item>
                <el-form-item label="course name" :label-width="formLabelWidth" prop="cname">
                    <el-input v-model="form.cname" placeholder="please input course name" clearable></el-input>
                </el-form-item>
                <el-form-item label="credits" :label-width="formLabelWidth" prop="ccredit">
                    <el-input v-model="form.ccredit" placeholder="please input credits" clearable></el-input>
                </el-form-item>
                <el-form-item label="pre-course" :label-width="formLabelWidth">
                    <el-select v-model="form.cpno" placeholder="please select pre-course">
                        <el-option v-for="item in allCourse" :label="item.cname" :value="item.cno" :key="item.cno"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetDialog">Cancel</el-button>
                <el-button type="primary" @click="addCourse('form')">Confirm</el-button>
            </div>
        </el-dialog>

        <!--change course information-->
        <el-dialog title="change information" :visible.sync="infoDialog" @close="resetInfoDialog">
            <el-form :model="InfoForm" ref="InfoForm" :rules="rules">
                <el-form-item label="course ID" :label-width="formLabelWidth" prop="cno">
                    <el-input v-model="InfoForm.cno" clearable></el-input>
                </el-form-item>
                <el-form-item label="course name" :label-width="formLabelWidth" prop="cname">
                    <el-input v-model="InfoForm.cname" clearable=""></el-input>
                </el-form-item>
                <el-form-item label="teacher" :label-width="formLabelWidth" >
                    <el-input v-model="InfoForm.tname" disabled></el-input>
                </el-form-item>
                <el-form-item label="credits" :label-width="formLabelWidth" prop="cname">
                    <el-input v-model="InfoForm.ccredit" clearable=""></el-input>
                </el-form-item>
                <el-form-item label="pre-course" :label-width="formLabelWidth" prop="cpno">
                    <el-select v-model="InfoForm.pre_cno">
                        <el-option :label="item.cname" v-for="item in allCourse" :value="item.cno" :key="item.cno"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetInfoDialog">Cancel</el-button>
                <el-button type="primary" @click="changeInfo('InfoForm')">Confirm</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        inject:['reload'],
        name: "courseInfo",
        data(){
            return {
                rules:{
                    cno:[{require:true,message:'please input course ID',trigger:'blur'},
                        {pattern:/\b\d{8}\b/,message:'Please input the 8-digit course ID',trigger:'blur'}],
                    cname:[{required:true,message:'please input course name',trigger:'blur'}],
                    ccredit:[{required:true,message:'please input credits',trigger:'blur'},
                        {pattern: /^\d+$|^\d+[.]?\d+$/,message:'please input the number',trigger:'blur'}]
                },
                allCourse:[],
                multipleSelection:[],
                selectNos:[],
                formLabelWidth: '120px',
                queryInfo:{
                    condition:'',
                    currentPage:1,
                    pageSize:10
                },
                InfoForm:{
                    cno:'',
                    cname:'',
                    ccredit:''
                },
                total:0,
                form:{
                    cno:'',
                    cname:'',
                    ccredit:''
                },
                tableData:[],
                allTableData:[],     //all table data
                dialogFormVisible:false,
                infoDialog:false,//course information dialog
            }
        },
        created(){
            this.selectCourseByConditionAndPage();
            this.getAllCourse();//Get all course information for prior course selection
            this.selectAllCourse();//Get all course information for export to excel
        },
        methods:{
            judgeGrade(){
                //Judge of identity
                if(JSON.parse(localStorage.getItem('user')).power=='teacher'){
                    this.dialogFormVisible=true;
                }else{
                    this.$notify({
                        title: 'Warning',
                        message: 'You do not have permission',
                        type: 'warning'
                    });
                }
            },
            chooseCourse(cno,tno){
               if(tno==undefined){
                   this.$message({
                       type:'error',
                       message:'The course is not yet available'
                   })
               }else{
                if(JSON.parse(localStorage.getItem('user')).power=='teacher'){
                    this.$notify({
                        title: 'Warning',
                        message: 'Only student can select course',
                        type: 'warning'
                    });
                    return; 
                }
                   let that=this;
                   let params={
                       cno:cno,
                       tno:tno,
                       sno:JSON.parse(localStorage.getItem('user')).sno
                   }
                   this.$axios.get(this.apiUrl.addSct,{
                       params:params
                   }).then(response=>{

                    if(JSON.parse(localStorage.getItem('user')).power=='student'){
                        that.$confirm('Confirm to select this course?', 'Tips', {
                           confirmButtonText: 'Confirm',
                           cancelButtonText: 'Cancel',
                           type: 'success'
                       }).then(() => {
                            //click to confirm
                           if(response.data.status==200){
                               that.$message({
                                   type:'success',
                                   message:'select success'
                               })
                           }else{
                               that.common.errorReturn(that,response.data.data.detailMessage);
                           }
                       }).catch(() => {
                           //click to cancel
                       });
                    }

                   }).catch(error=>{
                       console.log('---error---',error);
                   })
               }

            },
            changeInfo(formName){
                //change course information
                let that=this;
                if(this.InfoForm.cno==this.InfoForm.pre_cno){
                    this.$message({
                        type:'error',
                        message:'The pre-course is the same as the course to be modified'
                    })
                }else {
                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            this.common.changeInfo(this,this.apiUrl.updateCourseInfo);
                        }
                    })

                }
            },
            showInfo(cno){
                //Query for details by course number
                this.common.selectDetailInfo(this,this.apiUrl.selectCourseByCno,cno);
            },
            resetInfoDialog(){
                //Reset the message box
                this.$refs['InfoForm'].resetFields();
                this.infoDialog=false;
            },
            deleteCourse(cno){
                //delete course
                this.common.deletes(this,this.apiUrl.deleteCourseByNo,this.apiUrl.deleteMultipleCourse,"课程",cno);
            },
            exportToExcel () {
                //Exporting to excel
                require.ensure([], () => {
                    let { exportJsonToExcel } = require("../excel/Export2Excel.js");
                    // table head
                    let tHeader = [
                        "course ID",
                        "course name",
                        "credits",
                        "pre-course ID",
                        "pre-course name",
                        "pre-course credits"
                    ];
                    //The table header corresponds to the field name, which corresponds to the following data key
                    let filterVal = [
                        "cno",
                        "cname",
                        "ccredit",
                        "pre_cno",
                        "pre_cname",
                        "pre_ccredit"
                    ];
                    // data source
                    let list = this.allTableData;
                    let data = this.formatJson(filterVal, list); //Data formatting
                    var index1 = "all course information"; //document name
                    exportJsonToExcel(tHeader, data, index1); 
                });
            },
            formatJson(filterVal, jsonData) {
                // Export excel data format
                return jsonData.map(v => filterVal.map(j => v[j]));
            },
            addCourse(formName){
                //add course
                if(this.form.cpno==this.form.cno){
                    this.$message({
                        type:'error',
                        message:'The pre-course is the same as the course to be modified'
                    })
                }else{
                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            this.common.addOne(this,this.apiUrl.addCourse);
                        }
                    })

                }
            },
            getAllCourse(){
                let that=this;
                this.$axios.get(this.apiUrl.selectAllCourse).then(response=>{
                    that.allCourse=response.data.data.data;
                }).catch(error=>{
                    console.log('--error--',error);
                })
            },
            resetDialog(){
                this.$refs['form'].resetFields();
                this.dialogFormVisible=false;
            },
            handleSelectionChange(val){
                //Listen for events where the value of the multi-select box changes dynamically
                this.multipleSelection = val;
            },
            handleSizeChange(val) {
                //Listen for pageSize
                this.queryInfo.pageSize=val;
                this.selectCourseByConditionAndPage();
            },
            handleCurrentChange(val) {
                //listen for currentPage
                this.queryInfo.currentPage=val;
                this.selectCourseByConditionAndPage();
            },
            selectCourseByConditionAndPage(){
                //Conditions pagination for all course information
                this.common.getAllDataByConditionAndPage(this,this.apiUrl.selectCourseByPageAndCondition);
            },
            selectAllCourse(){
                //Get all course information for excel export
                this.common.getAllData(this,this.apiUrl.selectAllCourses);
            }
        }
    }
</script>

<style scoped>
    .el-breadcrumb{
        margin-bottom: 15px;
    }
    .el-table{
        margin:50px 0;
    }
    .el-pagination{
        margin-top:30px;
    }
</style>