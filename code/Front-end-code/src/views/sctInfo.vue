<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Information</el-breadcrumb-item>
            <el-breadcrumb-item>my student</el-breadcrumb-item>
        </el-breadcrumb>

        <el-card shadow="always">
            <el-row :gutter="10">
                <el-col :span="8">
                    <el-input v-model="queryInfo.condition" placeholder="Please enter the student name to search" clearable @clear="selectSctByConditionAndPage">
                        <el-button slot="append" icon="el-icon-search" @click="selectSctByConditionAndPage"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="4" :offset="12">
                    <el-button type="success" @click="exportToExcel">Data export</el-button>
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
                        prop="sname"
                        label="student" width="140">
                </el-table-column>
                <el-table-column
                        prop="tname"
                        label="teacher" width="140">
                </el-table-column>
                <el-table-column
                        prop="cno"
                        label="course ID">
                </el-table-column>
                <el-table-column
                        prop="cname"
                        label="course name">
                </el-table-column>
                <el-table-column
                        prop="ccredit"
                        label="credits">
                </el-table-column>
                <el-table-column
                        prop="grade"
                        label="grade">
                </el-table-column>
                <el-table-column label="operate">
                    <template slot-scope="scope">
                        <el-button type="primary" icon="el-icon-edit" size="mini" @click="showInfo(scope.row.sno,scope.row.tno,scope.row.cno)"></el-button>
                        <el-button type="danger" icon="el-icon-delete" @click="deleteSct(scope.row.sno,scope.row.tno,scope.row.cno)" size="mini"></el-button>
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

        <el-dialog title="change information" :visible.sync="infoDialog" @close="resetInfoDialog">
            <el-form :model="InfoForm" ref="InfoForm">
                <el-form-item label="student" :label-width="formLabelWidth" prop="sno">
                    <el-input v-model="InfoForm.sname" disabled></el-input>
                </el-form-item>
                <el-form-item label="teacher" :label-width="formLabelWidth" prop="spassword">
                    <el-input v-model="InfoForm.tname" disabled></el-input>
                </el-form-item>
                <el-form-item label="course ID" :label-width="formLabelWidth" prop="sname">
                    <el-input v-model="InfoForm.cno" disabled></el-input>
                </el-form-item>
                <el-form-item label="course name" :label-width="formLabelWidth" prop="ssex">
                    <el-input v-model="InfoForm.cname" disabled></el-input>
                </el-form-item>
                <el-form-item label="credits" :label-width="formLabelWidth" prop="sage">
                    <el-input v-model="InfoForm.ccredit" disabled></el-input>
                </el-form-item>
                <el-form-item label="grade" :label-width="formLabelWidth">
                    <el-input v-model="InfoForm.grade" clearable></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetInfoDialog">Cancel</el-button>
                <el-button type="primary" @click="changeInfo()">Confirm</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        inject:['reload'],
        name: "sctInfo",
        data(){
            return {
                infoDialog:false,
                formLabelWidth: '120px',
                InfoForm:{
                    sno:'',
                    tno:'',
                    cno:'',
                    cname:'',
                    ccredit:'',
                    grade:''
                },
                selectNos:[],
                tableData:[],   
                queryInfo:{
                    condition:'',
                    tno:JSON.parse(localStorage.getItem('user')).sno,
                    currentPage:1,
                    pageSize:10
                },
                total:0,
                allTableData:[],    
                multipleSelection:[]
            }
        },
        created(){
            this.selectSctByConditionAndPage();
            this.selectAllSct();
        },
        methods:{
            changeInfo(){
                let that=this;
                let formData=new FormData();
                formData.append('grade',this.InfoForm.grade);
                formData.append('sno',this.InfoForm.sno);
                formData.append('cno',this.InfoForm.cno);
                formData.append('tno',this.InfoForm.tno);
                this.$axios({
                    method:'post',
                    data:formData,
                    url:this.apiUrl.changeGradeByNos
                }).then(response=>{
                    if(response.data.status==200){
                        that.$message({
                            type:'success',
                            message:'success'
                        })
                        that.infoDialog=false;
                        that.reload();
                    }else {
                        that.common.errorReturn(that,response.data.data.detailMessage);
                    }
                }).catch(error=>{
                    console.log('--error---',error);
                })
            },
            showInfo(sno,tno,cno){
                console.log('----sno---',sno);
                console.log('----tno---',tno);
                console.log('----cno---',cno);

                if(JSON.parse(localStorage.getItem('user')).power == 'teacher'){
                    this.infoDialog=true;
                    let that=this;
                    let params={
                        sno:sno,
                        cno:cno,
                        tno:tno
                    }
                    this.$axios.get(this.apiUrl.selectSctByNos, {
                        params:params
                    }).then(response=>{
                        console.log('----response---',response);
                        if(response.data.status==200){
                            that.InfoForm=response.data.data.data;
                        }
                    }).catch(error=>{
                        console.log('----error---',error);
                    })
                }else {
                    this.$notify({
                        title: 'Warning',
                        message: 'you are not the teacher',
                        type: 'warning'
                    });
                }
            },
            resetInfoDialog(){
                this.$refs['InfoForm'].resetFields();
                this.infoDialog=false;
            },
            deleteSct(sno,tno,cno){
                if(JSON.parse(localStorage.getItem('user')).power=='teacher'){
                    if(this.multipleSelection.length>1){
                        this.$confirm('sure to delete these students?', 'Tips', {
                            confirmButtonText: 'Confirm',
                            cancelButtonText: 'Cancel',
                            type: 'success'
                        }).then(() => {
                            let that = this;
                            let formData=new FormData();
                            formData.append('sctArrays',this.multipleSelection);
                            this.$axios({
                                method:'post',
                                data:this.multipleSelection,
                                contentType : "application/json; charset=utf-8",
                                dataType:'json',
                                url:this.apiUrl.deleteMultipleSct
                            }).then(res=>{
                                if(res.data.status==200){
                                    that.$message({
                                        message: 'success',
                                        type: 'success '
                                    });
                                    that.reload();
                                }else{
                                    that.common.errorReturn(that,res.data.detailMessage);
                                }
                            }).catch(error=>{
                                console.log('---error---',error);
                            })
                        }).catch(() => {
                        });
                    }else {
                        this.$confirm('sure to delete this student?', 'Tips', {
                            confirmButtonText: 'Confirm',
                            cancelButtonText: 'Cancel',
                            type: 'success'
                        }).then(() => {
                            let that = this;
                            let params = {
                                sno: sno,
                                tno:tno,
                                cno:cno
                            };
                            this.$axios.get(this.apiUrl.deleteSctBySno, {
                                params: params
                            }).then(response => {
                                if (response.data.status == 200) {
                                    that.$message({
                                        message: 'success',
                                        type: 'success '
                                    });
                                } else {
                                    that.common.errorReturn(that, response.data.data.detailMessage);
                                }
                                that.reload();
                            }).catch(error => {
                                console.log("--error--", error);
                            })
                        }).catch(() => {
                        });
                    }
                }else {
                    this.$notify({
                        title: 'Warning',
                        message: '',
                        type: 'warning'
                    });
                }

            },
            exportToExcel () {
                require.ensure([], () => {
                    let { exportJsonToExcel } = require("../excel/Export2Excel.js");
                    let tHeader = [
                        "student",
                        "teacher",
                        "course ID",
                        "course name",
                        "credits",
                        "grade"
                    ];
                    let filterVal = [
                        "sname",
                        "tname",
                        "cno",
                        "cname",
                        "ccredit",
                        "grade"
                    ];

                    let list = this.allTableData;
                    let data = this.formatJson(filterVal, list); 
                    var index1 = "my student"; 
                    exportJsonToExcel(tHeader, data, index1); 
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]));
            },
            handleSelectionChange(val){
                this.multipleSelection = val;
            },
            handleSizeChange(val) {
                this.queryInfo.pageSize=val;
                this.selectSctByConditionAndPage();
            },
            handleCurrentChange(val) {
                this.queryInfo.currentPage=val;
                this.selectSctByConditionAndPage();
            },
            selectSctByConditionAndPage(){
                let that=this;
                let params={
                    tno:JSON.parse(localStorage.getItem('user')).sno,
                    condition:this.queryInfo.condition,
                    currentPage:this.queryInfo.currentPage,
                    pageSize:this.queryInfo.pageSize
                }
                this.$axios.get(this.apiUrl.selectSctByPageAndCondition,{
                    params:params
                }).then(response=>{
                    that.tableData = response.data.data.data;
                    that.total = response.data.data.total;
                }).catch(error=>{
                    console.log('--error--',error);
                })
            },
            selectAllSct(){
                this.common.getAllData(this,this.apiUrl.selectAllSct);
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