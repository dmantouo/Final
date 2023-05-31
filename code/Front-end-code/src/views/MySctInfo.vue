<template>
    <!--我的选课信息-->
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Information</el-breadcrumb-item>
            <el-breadcrumb-item>my course</el-breadcrumb-item>
        </el-breadcrumb>


        <el-card shadow="always">
            <el-row>
                <el-col :span="8">
                    <el-input v-model="queryInfo.condition" placeholder="Please enter the course name to query" clearable @clear="selectSctByConditionAndPage">
                        <el-button slot="append" icon="el-icon-search" @click="selectSctByConditionAndPage"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="3" :offset="13">
                    <el-button type="success" @click="exportToExcel">Data export</el-button>
                </el-col>
            </el-row>

            <el-table
                    :data="tableData"
                    stripe
                    border
                    style="width: 100%"
                    @selection-change="handleSelectionChange">

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
    </div>
</template>

<script>
    export default {
        inject:['reload'],
        name: "MySctInfo",
        data(){
            return {
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
            showInfo(sno,tno,cno){
                if(JSON.parse(localStorage.getItem('user')).power=='teacher'){
                    this.infoDialog=true;
                    let that=this;
                    let params={
                        sno:sno,
                        cno:cno,
                        tno:tno
                    }
                    this.$axios.get(this.apiUrl.selectSctByNos,{
                        params:params
                    }).then(response=>{
                        if(response.data.status==200){
                            that.InfoForm=response.data.data.data;
                        }
                    }).catch(error=>{
                        console.log('----error---',error);
                    })
                }else {
                    this.$notify({
                        title: 'Warning',
                        message: '',
                        type: 'warning'
                    });
                }
            },
            resetInfoDialog(){
                this.$refs['InfoForm'].resetFields();
                this.infoDialog=false;
            },
    
            exportToExcel () {
                //导出excel
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
                    var index1 = "my course"; 
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
                //this.common.getAllDataByConditionAndPage(this,this.apiUrl.selectSctByPageAndConditionAndSno);
                let that=this;
                let params={
                    sno:JSON.parse(localStorage.getItem('user')).sno,
                    condition:this.queryInfo.condition,
                    currentPage:this.queryInfo.currentPage,
                    pageSize:this.queryInfo.pageSize
                }
                this.$axios.get(this.apiUrl.selectSctByPageAndConditionAndSno,{
                    params:params
                }).then(response=>{
                    that.tableData = response.data.data.data;
                    that.total = response.data.data.total;
                }).catch(error=>{
                    console.log('--error--',error);
                })
            },
            selectAllSct(){
                let that=this;
                let params={
                    sno:JSON.parse(localStorage.getItem('user')).sno
                }
                this.$axios.get(this.apiUrl.selectAllSctByNo,{
                    params:params
                }).then(response=>{
                    that.allTableData=response.data.data.data;
                }).catch(error=>{
                    console.log('---error--',error);
                })

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