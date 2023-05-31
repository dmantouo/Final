<template>
    <div>
            <!--Breadcrumb-->
            <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
                <el-breadcrumb-item>Information</el-breadcrumb-item>
                <el-breadcrumb-item>student</el-breadcrumb-item>
            </el-breadcrumb>

            <el-card shadow="always">
                <el-row :gutter="10">
                    <el-col :span="8">
                        <el-input v-model="queryInfo.condition" placeholder="search by name" clearable @clear="selectStudentByConditionAndPage()">
                            <el-button slot="append" icon="el-icon-search" @click="selectStudentByConditionAndPage()"></el-button>
                        </el-input>
                    </el-col>
                    <el-col :span="3" :offset="1">
                        <el-button type="primary" @click="dialogFormVisible=true">add one</el-button>
                    </el-col>
                    <el-col :span="3">
                        <el-upload
                                action="https://jsonplaceholder.typicode.com/posts/"
                                :before-remove="beforeRemove"
                                :limit="1"
                                :auto-upload=false
                                :on-change="changeFile"
                                accept=".xls,.xlsx">
                            <el-button type="primary">add file</el-button>
                        </el-upload>
                    </el-col>
                    <el-col :span="4">
                        <el-button type="primary"
                                   @click="uploadFile()">upload data</el-button>
                    </el-col>
                    <el-col :span="4">
                        <el-button type="success" @click="exportToExcel()">Data export</el-button>
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
                            prop="sno"
                            label="student ID">
                    </el-table-column>

                    <el-table-column
                            prop="sname"
                            label="name">
                    </el-table-column>
                    <el-table-column
                            prop="ssex"
                            label="sex">
                    </el-table-column>
                    <el-table-column
                            prop="sage"
                            label="age">
                    </el-table-column>
                    <el-table-column
                            prop="sdept"
                            label="faculty">
                    </el-table-column>
                    <el-table-column
                            prop="sid"
                            label="personal ID" width="190px">
                    </el-table-column>
                    <el-table-column prop="power" label="Identity" >
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.power=='teacher'" type="success">teacher</el-tag>
                            <el-tag v-if="scope.row.power=='student'" type="success">student</el-tag>                           
                        </template>
                    </el-table-column>
                    <el-table-column label="operate" >
                        <template slot-scope="scope">
                            <el-button type="primary" icon="el-icon-edit" size="mini" @click="showInfo(scope.row.sno)"></el-button>
                            <el-button type="danger" icon="el-icon-delete" @click="deleteStudent(scope.row.sno)" size="mini"></el-button>
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

            <el-dialog title="add student" :visible.sync="dialogFormVisible" @close="resetDialog">
                <el-form :model="form" ref="form" :rules="rules">
                    <el-form-item label="student ID" :label-width="formLabelWidth" prop="sno">
                        <el-input v-model="form.sno" placeholder="please input student ID" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="password" :label-width="formLabelWidth" prop="spassword">
                        <el-input v-model="form.spassword" placeholder="please input password" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="name" :label-width="formLabelWidth" prop="sname">
                        <el-input v-model="form.sname" placeholder="please input name" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="personal ID" :label-width="formLabelWidth" prop="sid">
                        <el-input v-model="form.sid" placeholder="please input personal ID" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="sex" :label-width="formLabelWidth" prop="ssex">
                        <el-select v-model="form.ssex" placeholder="please select sex" clearable style="width:100%;">
                            <el-option label="boy" value="boy"></el-option>
                            <el-option label="girl" value="girl"></el-option>
                        </el-select> 
                    </el-form-item>
                    <el-form-item label="age" :label-width="formLabelWidth" prop="sage">
                        <el-input v-model="form.sage" placeholder="please input age" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="faculty" :label-width="formLabelWidth" prop="sdept">
                        <el-select v-model="form.sdept" placeholder="please input faculty" clearable style="width:100%;">
                            <el-option :label="item.dname" v-for="item in allDepartment" :value="item.dname"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="resetDialog">Cancel</el-button>
                    <el-button type="primary" @click="addOneStudent('form')">Confirm</el-button>
                </div>
            </el-dialog>

            <el-dialog title="change information" :visible.sync="infoDialog" @close="resetInfoDialog">
                <el-form :model="InfoForm" ref="InfoForm" :rules="rules">
                    <el-form-item label="student ID" :label-width="formLabelWidth" prop="sno">
                        <el-input v-model="InfoForm.sno" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="password" :label-width="formLabelWidth" prop="spassword">
                        <el-input v-model="InfoForm.spassword" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="name" :label-width="formLabelWidth" prop="sname">
                        <el-input v-model="InfoForm.sname" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="sex" :label-width="formLabelWidth" prop="ssex">
                        <el-select v-model="InfoForm.ssex" placeholder="please select sex" clearable style="width: 100%;">
                            <el-option label="boy" value="boy"></el-option>
                            <el-option label="girl" value="girl"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="age" :label-width="formLabelWidth" prop="sage">
                        <el-input v-model="InfoForm.sage" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="faculty" :label-width="formLabelWidth" prop="sdept">
                        <el-select v-model="InfoForm.sdept" placeholder="please select faculty" clearable style="width: 100%;">
                            <el-option :label="item.dname" v-for="item in allDepartment" :value="item.dname"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="personal ID" :label-width="formLabelWidth" prop="sid">
                        <el-input v-model="InfoForm.sid" clearable></el-input>
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
        name: "studentInfo",
        data(){
            return {
                rules:{
                    sno:[{required:true,message:'please input student ID',trigger:'blur'},
                        {pattern:/\b\d{12}\b/,message:'Please enter your 12-digit student ID',trigger:'blur'}
                    ],
                    spassword:[{required:true,message:'please input password',trigger:'blur'},
                        { min: 5, max: 20, message: 'It is between 5 and 20 characters long', trigger: 'blur'}],
                    sid:[{required:true,message:'please input personal ID',trigger:'blur'},
                        {pattern:/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,message:'please input right personal ID',trigger:'blur'}
                    ],
                    sname:[{required:true,message:'please input name',trigger:'blur'}],
                    ssex:[{required:true,message:'please select sex',trigger:'change'}],
                    sage:[{required:true,message:'please input age',trigger:'blur'},
                        {pattern:/\b\d{2}\b/,message:'please input correct age',trigger:'blur'}],
                    sdept:[{required:true,message:'please select faculty',trigger:'change'}],
                },
                allDepartment:[],
                form:{
                    sno:'',
                    sname:'',
                    spassword:'',
                    ssex:'',
                    sage:'',
                    sdept:'',
                    sid:'',
                    power:'student'
                },
                InfoForm:{
                    sno:'',
                    sname:'',
                    spassword:'',
                    ssex:'',
                    sage:'',
                    sdept:'',
                    sid:'',
                    power:'student'
                },
                dialogFormVisible:false,
                infoDialog:false,
                formLabelWidth: '120px',
                selectNos:[],
                tableData:[],       
                queryInfo:{
                    condition:'',
                    currentPage:1,
                    pageSize:5
                },
                total:0,
                file:null,
                allTableData:[],     
                multipleSelection:[]
            }
        },
        created(){
            this.selectStudentByConditionAndPage();
            this.getAllStudent();
            this.getAllDepartment();
        },
        methods:{
            changeInfo(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.common.changeInfo(this,this.apiUrl.updateInfo);
                    }
                })
            },
            resetInfoDialog(){
                this.$refs['InfoForm'].resetFields();
                this.infoDialog=false;
            },
            showInfo(sno){
                this.common.selectDetailInfo(this,this.apiUrl.selectStudentByNo,sno);
            },
            exportToExcel () {
                require.ensure([], () => {
                    let { exportJsonToExcel } = require("../excel/Export2Excel.js");
                    let tHeader = [
                        "student ID",
                        "password",
                        "name",
                        "sex",
                        "age",
                        "faculty",
                        "personal ID",
                        "identity"
                    ];
                    let filterVal = [
                        "sno",
                        "spassword",
                        "sname",
                        "ssex",
                        "sage",
                        "sdept",
                        "sid",
                        "power"
                    ];
                    let list = this.allTableData;
                    let data = this.formatJson(filterVal, list); 
                    var index1 = "student information";
                    exportJsonToExcel(tHeader, data, index1); 
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]));
            },
            uploadFile(){
               this.common.uploadData(this,this.apiUrl.addMultipleStudent);
            },
            addOneStudent(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.common.addOne(this,this.apiUrl.addStudent);
                    }
                })

            },
            resetDialog(){
                this.$refs['form'].resetFields();
                this.dialogFormVisible=false;
            },
            deleteStudent(sno){
                this.common.deletes(this,this.apiUrl.deleteStudentByNo,this.apiUrl.deleteMultipleStudent,"student",sno);
            },
            handleSelectionChange(val){
                this.multipleSelection = val;
            },
            selectStudentByConditionAndPage(){
                this.common.getAllDataByConditionAndPage(this,this.apiUrl.selectStudentByConditionAndPage);
            },
            changeFile(file){
                this.file=file;
            },
            beforeRemove(file) {
                return this.$confirm(`delete ${ file.name }？`);
            },
            handleSizeChange(val) {
                this.queryInfo.pageSize=val;
                this.selectStudentByConditionAndPage();
            },
            handleCurrentChange(val) {
                this.queryInfo.currentPage=val;
                this.selectStudentByConditionAndPage();
            },
            getAllDepartment(){
                let that=this;
                this.$axios.get(this.apiUrl.selectAllDepartment).then(response=>{
                    if(response.data.status==200){
                        that.allDepartment=response.data.data.data;
                    }
                }).catch(error=>{
                    console.log('---error---',error);
                })
            },
            getAllStudent(){
                this.common.getAllData(this,this.apiUrl.selectAllStudents);
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