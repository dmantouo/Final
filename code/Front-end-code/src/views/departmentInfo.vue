<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Information</el-breadcrumb-item>
            <el-breadcrumb-item>faculties</el-breadcrumb-item>
        </el-breadcrumb>

        <!--card-->
        <el-card shadow="always">
            <el-row :gutter="10">
                <el-col :span="8">
                    <el-input v-model="queryInfo.condition" placeholder="search by faculty name" clearable @clear="selectDepartmentByPageAndCondition">
                        <el-button slot="append" icon="el-icon-search" @click="selectDepartmentByPageAndCondition"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="4" :offset="12">
                    <el-button type="primary" @click="judgeGrade()">add faculty</el-button>
                </el-col>
            </el-row>

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
                        prop="dno"
                        label="faculty ID">
                </el-table-column>
                <el-table-column
                        prop="dname"
                        label="faculty name">
                </el-table-column>
                <el-table-column
                        prop="dmanager"
                        label="faculty leader">
                </el-table-column>
                <el-table-column label="operate">
                    <template slot-scope="scope">
                        <el-button type="primary" icon="el-icon-edit" size="mini" @click="showInfo(scope.row.dno)"></el-button>
                        <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteDepartment(scope.row.dno)"></el-button>
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

        <!--add faculty-->
        <el-dialog title="add faculty" :visible.sync="dialogFormVisible" @close="resetDialog">
            <el-form :model="form" ref="form" :rules="rules">
                <el-form-item label="faculty ID" :label-width="formLabelWidth" prop="dno">
                    <el-input v-model="form.dno" placeholder="please input faculty ID" clearable></el-input>
                </el-form-item>
                <el-form-item label="faculty name" :label-width="formLabelWidth" prop="dname">
                    <el-input v-model="form.dname" placeholder="please input faculty name" clearable></el-input>
                </el-form-item>
                <el-form-item label="faculty leader" :label-width="formLabelWidth" prop="dmanager">
                    <el-input v-model="form.dmanager" placeholder="please input faculty leader" clearable></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetDialog">Cancel</el-button>
                <el-button type="primary" @click="addDepartment('form')">Confirm</el-button>
            </div>
        </el-dialog>

        <!--faculty information-->
        <el-dialog title="change information" :visible.sync="infoDialog" @close="resetInfoDialog">
            <el-form :model="InfoForm" ref="InfoForm" :rules="rules">
                <el-form-item label="faculty ID" :label-width="formLabelWidth" prop="dno">
                    <el-input v-model="InfoForm.dno" clearable></el-input>
                </el-form-item>
                <el-form-item label="faculty name" :label-width="formLabelWidth" prop="dname">
                    <el-input v-model="InfoForm.dname"></el-input>
                </el-form-item>
                <el-form-item label="faculty leader" :label-width="formLabelWidth" prop="dmanager">
                    <el-input v-model="InfoForm.dmanager"></el-input>
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
        name: "departmentInfo",
        data(){
            return {
                rules:{
                    dno:[{ require:true,message:'please input faculty ID',trigger:'blur'},
                        {pattern:/\b\d{8}\b/,message:'Please enter the 8-digit faculty ID',trigger:'blur'}],
                    dname:[{required:true,message:'please input faculty name',trigger:'blur'},],
                    dmanager:[{required:true,message:'please input faculty leader',trigger:'blur'}]

                },
                multipleSelection:[],
                selectNos:[],
                formLabelWidth: '120px',
                queryInfo:{
                    condition:'',
                    currentPage:1,
                    pageSize:10
                },
                total:0,
                form:{
                    dno:'',
                    dname:'',
                    dmanager:''
                },
                InfoForm:{
                    dno:'',
                    dname:'',
                    dmanager:'',
                },
                tableData:[],
                dialogFormVisible:false,
                infoDialog:false,
            }
        },
        created(){
            this.selectDepartmentByPageAndCondition();
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
            changeInfo(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.common.changeInfo(this,this.apiUrl.updateDepartmentInfo);
                    }
                });

            },
            showInfo(dno){
                this.common.selectDetailInfo(this,this.apiUrl.selectDepartmentByNo,dno);
            },
            resetInfoDialog(){
                this.$refs['InfoForm'].resetFields();
                this.infoDialog=false;
            },
            deleteDepartment(dno){
                this.common.deletes(this,this.apiUrl.deleteDepartment,this.apiUrl.deleteMultipleDepartment,"faculty",dno);
            },
            addDepartment(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.common.addOne(this,this.apiUrl.addDepartment);
                    }
                });

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
                this.selectDepartmentByPageAndCondition();
            },
            handleCurrentChange(val) {
                this.queryInfo.currentPage=val;
                this.selectDepartmentByPageAndCondition();
            },
            selectDepartmentByPageAndCondition(){
                this.common.getAllDataByConditionAndPage(this,this.apiUrl.selectDepartmentByPageAndCondition);
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