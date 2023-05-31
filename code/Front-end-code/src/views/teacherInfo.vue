<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Information</el-breadcrumb-item>
            <el-breadcrumb-item>teacher</el-breadcrumb-item>
        </el-breadcrumb>

        <el-card shadow="always">
            <el-row :gutter="10">
                <el-col :span="8">
                    <el-input v-model="queryInfo.condition" placeholder="search by name" clearable @clear="selectTeacherByPageAndCondition()">
                        <el-button slot="append" icon="el-icon-search" @click="selectTeacherByPageAndCondition()"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="4" :offset="1">
                    <el-button type="success" @click="exportToExcel()">Data export</el-button></el-button>
                </el-col>
                <el-col :span="4" :offset="7">
                    <el-button type="primary" @click="judgeGrade()">add teacher</el-button>
                </el-col>

            </el-row>

            <el-table
                    :data="tableData"
                    stripe
                    border
                    style="width: 100%"
                    @selection-change="handleSelectionChange">
                <el-table-column  type="selection" width="40">
                </el-table-column>
                <el-table-column
                        prop="tno"
                        label="teacher ID">
                </el-table-column>
                <el-table-column
                        prop="tname"
                        label="name">
                </el-table-column>
                <el-table-column
                        prop="tage"
                        label="age" width="50">
                </el-table-column>
                <el-table-column
                        prop="tsex"
                        label="sex" width="50">
                </el-table-column>
                <el-table-column
                        prop="tpt"
                        label="title" width="180">
                </el-table-column>
                <el-table-column
                        prop="teb"
                        label="education" width="90">
                </el-table-column>
                <el-table-column
                        prop="c1_name"
                        label="course 1">
                </el-table-column>
                <el-table-column
                        prop="c2_name"
                        label="course 2">
                </el-table-column>
                <el-table-column
                        prop="c3_name"
                        label="course 3">
                </el-table-column>
                <el-table-column label="operate">
                    <template slot-scope="scope">
                        <el-button type="primary" icon="el-icon-edit" size="mini"  @click="showInfo(scope.row.tno)"></el-button>
                        <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteTeacher(scope.row.tno)"></el-button>
                    </template>
                </el-table-column>
            </el-table>

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

        <el-dialog title="add teacher" :visible.sync="dialogFormVisible" @close="resetDialog">
            <el-form :model="form" ref="form" :rules="rules">
                <el-form-item label="teacher ID" :label-width="formLabelWidth" prop="tno">
                    <el-input v-model="form.tno" placeholder="please input teacher ID" clearable></el-input>
                </el-form-item>
                <el-form-item label="password" :label-width="formLabelWidth" prop="spassword">
                        <el-input v-model="form.spassword" placeholder="please input password" clearable></el-input>
                    </el-form-item>
                <el-form-item label="name" :label-width="formLabelWidth" prop="tname">
                    <el-input v-model="form.tname" placeholder="please input name" clearable></el-input>
                </el-form-item>
                <el-form-item label="personal ID" :label-width="formLabelWidth" prop="sid">
                        <el-input v-model="form.sid" placeholder="please input personal ID" clearable></el-input>
                    </el-form-item>
                <el-form-item label="sex" :label-width="formLabelWidth" prop="tsex">
                    <el-select v-model="form.tsex" placeholder="please select sex" clearable>
                        <el-option label="boy" value="boy"></el-option>
                        <el-option label="girl" value="girl"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="age" :label-width="formLabelWidth" prop="tage">
                    <el-input v-model="form.tage" placeholder="please input age" clearable></el-input>
                </el-form-item>
                <el-form-item label="education" :label-width="formLabelWidth" prop="teb">
                    <el-select v-model="form.teb" placeholder="please selection education" clearable>
                        <el-option label="Bachelor" value="Bachelor"></el-option>
                        <el-option label="Master" value="Master"></el-option>
                        <el-option label="PhD" value="PhD"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="course 1" :label-width="formLabelWidth" prop="cno1">
                    <el-select v-model="form.cno1" placeholder="select course 1" clearable>
                        <el-option  v-for="item in allCourse" :label="item.cname"  :value="item.cno"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="course 2" :label-width="formLabelWidth" prop="cno2">
                    <el-select v-model="form.cno2" placeholder="select course 2" clearable>
                        <el-option  v-for="item in allCourse" :label="item.cname"  :value="item.cno"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="course 3" :label-width="formLabelWidth" prop="cno3">
                    <el-select v-model="form.cno3" placeholder="select course 3" clearable>
                        <el-option  v-for="item in allCourse" :label="item.cname"  :value="item.cno"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetDialog">Cancel</el-button>
                <el-button type="primary" @click="addTeacher('form')">Confirm</el-button>
            </div>
        </el-dialog>

        <el-dialog title="change information" :visible.sync="infoDialog" @close="resetInfoDialog">
            <el-form :model="InfoForm" ref="InfoForm" :rules="rules">
                <el-form-item label="teacher ID" :label-width="formLabelWidth" prop="tno">
                    <el-input v-model="InfoForm.tno" clearable></el-input>
                </el-form-item>
                <el-form-item label="name" :label-width="formLabelWidth" prop="tname">
                    <el-input v-model="InfoForm.tname"></el-input>
                </el-form-item>
                <el-form-item label="sex" :label-width="formLabelWidth" prop="tsex">
                    <el-select v-model="InfoForm.tsex" placeholder="select sex">
                        <el-option label="boy" value="boy"></el-option>
                        <el-option label="girl" value="girl"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="age" :label-width="formLabelWidth" prop="tage">
                    <el-input v-model="InfoForm.tage"></el-input>
                </el-form-item>
                <el-form-item label="title" :label-width="formLabelWidth" prop="tpt">
                    <el-select v-model="InfoForm.tpt" >
                        <el-option  label="Teaching assistant" value="Teaching assistant"></el-option>
                        <el-option  label="lecturer" value="lecturer"></el-option>
                        <el-option  label="Associate professor" value="Associate professor"></el-option>
                        <el-option  label="Prof." value="Prof."></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="education" :label-width="formLabelWidth" prop="teb">
                    <el-select v-model="InfoForm.teb" >
                        <el-option label="Bachelor" value="Bachelor"></el-option>
                        <el-option label="Master" value="Master"></el-option>
                        <el-option label="PhD" value="PhD"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="course 1" :label-width="formLabelWidth" prop="cno1">
                        <el-select v-model="InfoForm.cno1" clearable @clear="clearNo1()">
                            <el-option v-for="item in allCourse" :label="item.cname" :value="item.cno"></el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="course 2" :label-width="formLabelWidth" prop="cno2">
                        <el-select v-model="InfoForm.cno2" clearable @clear="clearNo2()">
                            <el-option v-for="item in allCourse" :label="item.cname" :value="item.cno"></el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="course 3" :label-width="formLabelWidth" prop="cno3">
                        <el-select v-model="InfoForm.cno3" clearable @clear="clearNo3()">
                            <el-option v-for="item in allCourse" :label="item.cname" :value="item.cno"></el-option>
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
        name: "teacherInfo",
        data(){
            return {
                rules:{
                    tno:[{required:true,message:'please input techer ID',trigger:'blur'},
                        {pattern:/\b\d{8}\b/,message:'Please enter an 8-digit ID',trigger:'blur'}],
                    tname:[{required:true,message:'pelase input techer name',trigger:'blur'}],
                    tsex:[{required:true,message:'select sex',trigger:'change'}],
                    tage:[{required:true,message:'please input age',trigger:'blur'},
                        {pattern:/\b\d{2}\b/,message:'Please enter an 2-digit number',trigger:'blur'}],
                    tpt:[{required:true,message:'please select title',trigger:'change'}],
                    teb:[{required:true,message:'please select education',trigger:'change'}],
                },
                allCourse:[],
                multipleSelection:[],
                selectNos:[],
                form:{
                    tno:'',
                    spassword:'',
                    tname:'',
                    sid:'',
                    tsex:'',
                    tage:'',
                    tpt:'teacher',
                    teb:'',
                    power:'teacher'
                },
                formLabelWidth: '120px',
                queryInfo:{
                    condition:'',
                    currentPage:1,
                    pageSize:10
                },
                InfoForm:{
                    tno:'',
                    spassword:'',
                    tname:'',
                    sid:'',
                    tsex:'',
                    tage:'',
                    tpt:'teacher',
                    teb:'',
                    power:'teacher'
                },
                total:0,
                allTableData:[],     
                tableData:[],       
                dialogFormVisible:false,    
                infoDialog:false,          
            }
        },
        created(){
            this.selectTeacherByPageAndCondition();
            this.getAllCourse();
            this.getAllTeacher();
        },
        methods:{
            judgeGrade(){

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
            clearNo1(){
                delete this.InfoForm['cno1'];
            },
            clearNo2(){
                delete this.InfoForm['cno2'];
            },
            clearNo3(){
                delete this.InfoForm['cno3'];
            },
            changeInfo(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.common.changeInfo(this,this.apiUrl.updateTeacherInfo);
                    }
                })

            },
            showInfo(tno){
                this.common.selectDetailInfo(this,this.apiUrl.selectTeacherBytno,tno);
            },
            deleteTeacher(tno){
                this.common.deletes(this,this.apiUrl.deleteTeacherByNo,this.apiUrl.deleteMultipleTeacher,"老师",tno)
            },
            exportToExcel () {
                require.ensure([], () => {
                    let { exportJsonToExcel } = require("../excel/Export2Excel.js");
                    let tHeader = [
                        "teacher ID",
                        "name",
                        "age",
                        "sex",
                        "title",
                        "education",
                        "course 1",
                        "course 2",
                        "course 3"
                    ];

                    let filterVal = [
                        "tno",
                        "tname",
                        "tage",
                        "tsex",
                        "tpt",
                        "teb",
                        "c1_name",
                        "c2_name",
                        "c3_name"
                    ];

                    let list = this.allTableData;
                    let data = this.formatJson(filterVal, list); 
                    var index1 = "teacher information"; 
                    exportJsonToExcel(tHeader, data, index1); 
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]));
            },
            addTeacher(formName){
                let that=this;
                if((this.form.cno1==this.form.cno2)&&this.form.cno1!=undefined&&this.form.cno2!=undefined){
                    this.$message({
                        type:'error',
                        message:'course cannot be repeated'
                    })
                }else if((this.form.cno1==this.form.cno3)&&this.form.cno1!=undefined&&this.form.cno3!=undefined){
                    this.$message({
                        type:'error',
                        message:'course cannot be repeated'
                    })
                }else if((this.form.cno2==this.form.cno3)&&this.form.cno2!=undefined&&this.form.cno3!=undefined){
                    this.$message({
                        type:'error',
                        message:'course cannot be repeated'
                    })
                }else{
                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            this.common.addOne(this,this.apiUrl.addTeacher)
                        }
                    })

                }

            },
            getAllCourse(){
                let that=this;
                this.$axios.get(this.apiUrl.selectAllCourse).then(response=>{
                    that.allCourse=response.data.data.data;
                }).catch(error=>{
                    console.log('--error---',error);
                })
            },
            resetInfoDialog(){
                this.$refs['InfoForm'].resetFields();
                this.infoDialog=false;
            },
            resetDialog(){
                this.$refs['form'].resetFields();
                this.dialogFormVisible=false;
            },
            handleSelectionChange(val){
                this.multipleSelection = val;
            },
            handleSizeChange(val) {
                this.queryInfo.pageSize=val;
                this.selectTeacherByPageAndCondition();
            },
            handleCurrentChange(val) {
                this.queryInfo.currentPage=val;
                this.selectTeacherByPageAndCondition();
            },
            selectTeacherByPageAndCondition(){
                this.common.getAllDataByConditionAndPage(this,this.apiUrl.selectTeacherByPageAndCondition);
            },
            getAllTeacher(){
                this.common.getAllData(this,this.apiUrl.selectAllTeacher);
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