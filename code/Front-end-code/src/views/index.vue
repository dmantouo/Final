<template>
        <el-container class="all">
            <el-header>
                <div class="left_header">
                    <img src="../assets/images/logo_school.png" width="150" height="100">
                    <span>Course Selection System</span>
                </div>
                <div class="right_header">
                    <el-image :src="photo"  style="width:50px; height:50px;margin-right:10px;" fit="contain"></el-image>
                    <el-dropdown @command="handleCommand">
                            <span class="el-dropdown-link">
                                {{sname}} <i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="a">Personal information</el-dropdown-item>
                            <el-dropdown-item command="b">Change password</el-dropdown-item>
                            <el-dropdown-item command="c">Log out</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="220px">
                    <el-menu
                            background-color="#2d2f36"
                            text-color="#fff"
                            unique-opened
                            router>
                        <el-submenu :index="''+item.id" v-for="item in menuItems" :key="item.id">
                            <template slot="title">
                                <i :class="item.type"></i>
                                <span>{{item.name}}</span>
                            </template>

                            <el-menu-item :index="''+per.path" v-if="showHiddenItem(per.canSee)"  v-for="per in item.children" :key="per.id">
                                <template slot="title">
                                    <i :class="per.type"></i>
                                    <span>{{per.name}}</span>
                                </template>
                            </el-menu-item>

                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <router-view></router-view>
                </el-main>
            </el-container>

            <el-dialog title="Change national information" :visible.sync="dialogFormVisible" width="40%" @close="resetDialogForm">
                <el-form :model="form" ref="form" :rules="rules">
                    <el-form-item label="student ID" :label-width="labelWidth" >
                        <el-input v-model="form.sno" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="name" :label-width="labelWidth">
                        <el-input v-model="form.sname" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="sex" :label-width="labelWidth">
                        <el-input v-model="form.ssex" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="age" :label-width="labelWidth">
                        <el-input v-model="form.sage" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="faculties" :label-width="labelWidth">
                        <el-input v-model="form.sdept" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="national ID" prop="sid" :label-width="labelWidth">
                        <el-input v-model="form.sid" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="identity" :label-width="labelWidth">
                        <el-input v-model="form.power" disabled></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="resetDialogForm">Cancel</el-button>
                    <el-button type="primary" @click="changeSidBySno('form')">Confirm</el-button>
                </div>
            </el-dialog>

            <!--修改密码框-->
            <el-dialog title="change password" :visible.sync="changePasswordDialogVisible"
                       width="35%" @close="resetChangePasswordDialog">
                <el-form :model="changePasswordForm" ref="changePasswordForm" :rules="rules">
                    <el-form-item label="Original password" :label-width="labelWidth" prop="previewPassword">
                        <el-input type="password" v-model="changePasswordForm.previewPassword" placeholder="please input original password" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="New password" :label-width="labelWidth" prop="newPassword">
                        <el-input v-model="changePasswordForm.newPassword" type="password" placeholder="please input new password" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="confirm password" :label-width="labelWidth" prop="confirmPassword">
                        <el-input v-model="changePasswordForm.confirmPassword" type="password" placeholder="please confirm new password" clearable></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="resetChangePasswordDialog">Cancel</el-button>
                    <el-button type="primary" @click="changePassword">Confirm</el-button>
                </div>
            </el-dialog>
        </el-container>
</template>

<script>
    export default {
        name: "index",
        data(){
            //Used to verify the password entered twice
            let validatePass2 = (rule, value, callback) => {
                if (value !== this.changePasswordForm.newPassword) {
                    callback(new Error('Two different password input!'));
                } else {
                    callback();
                }
            };
            return {
                rules:{
                    sno:[{required:true,message:'please input ID',trigger:'blur'},
                        ],
                    previewPassword:[{required:true,message:'please input password',trigger:'blur'},
                        { min: 5, max: 20, message: 'It is between 5 and 20 characters long', trigger: 'blur'}],
                    newPassword:[{required:true,message:'please input password',trigger:'blur'},
                        { min: 5, max: 20, message: 'It is between 5 and 20 characters long', trigger: 'blur'}],
                    confirmPassword:[{required:true,message:'please input password',trigger:'blur'},
                        { min: 5, max: 20, message: 'It is between 5 and 20 characters long', trigger: 'blur'},
                        {validator:validatePass2,trigger:'blur'}],
                    sid:[{required:true,message:'please input national ID',trigger:'blur'},
                        {pattern:/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,message:'please input correct national ID',trigger:'blur'}
                        ],
                },
                changePasswordForm:{
                    previewPassword:'',
                    newPassword:'',
                    confirmPassword:''
                },
                photo:require("../assets/images/me.png"),
                sname:'',
                form:{
                    sno:'',
                    sname:'',
                    ssex:'',
                    sage:0,
                    sdept:'',
                    sid:'',
                    power:''
                },
                dialogFormVisible:false,
                changePasswordDialogVisible:false,
                labelWidth:'135px',

            }
        },
        created(){
            //Checking if you are logged in
            if(localStorage.getItem('token')!=undefined){
                //Display username
                this.sname=JSON.parse(localStorage.getItem('user')).sname;
                //Displays the left navigation bar
                this.menuItems=this.$store.state.menuItems;
            }else{
                this.$message({
                    type:'error',
                    message:'Please log in again.'
                })
                this.$router.push("/");
            }
        },
        methods:{
            resetDialogForm(){
                this.$refs['form'].resetFields();
                this.dialogFormVisible=false;
            },
            changeSidBySno(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let that=this;
                        let formData=new FormData();
                        formData.append('sno',JSON.parse(localStorage.getItem('user')).sno);
                        formData.append("sid",this.form.sid);
                        this.$axios({
                            method:'post',
                            data:formData,
                            url:this.apiUrl.changeSidBySno
                        }).then(response=>{
                            if(response.data.status==200){
                                //change success
                                that.$message({
                                    type:'success',
                                    message:response.data.data.detailMessage
                                })
                                that.dialogFormVisible=false;
                            }else{
                                //change error
                                that.common.errorReturn(that,response.data.data.detailMessage);
                            }
                        }).catch(error=>{
                            console.log('---error----',error);
                        })
                    }
                })
            },
            showHiddenItem(canSee){

                console.log("wwww:"+canSee);
                if(JSON.parse(localStorage.getItem('user')).power=='teacher'){
                    return true;
                }
                return canSee;
            },
            showStudentInfo(){
                let that=this;
                let params={
                    sno:JSON.parse(localStorage.getItem('user')).sno
                }
                this.$axios.get(this.apiUrl.selectStudentInfoBySno,{
                    params:params
                }).then(response=>{
                    that.form=response.data.data.data;
                }).catch(error=>{
                    console.log('---error--',error);
                })
            },

            changePassword(){
                let that=this;
                let formData=new FormData();
                formData.append('sno',JSON.parse(localStorage.getItem('user')).sno);
                formData.append('newPassword',this.changePasswordForm.newPassword);
                formData.append('previewPassword',this.changePasswordForm.previewPassword);
                this.$axios({
                    method:'post',
                    data:formData,
                    url:this.apiUrl.updatePasswordByPreviewPassword
                }).then(response=>{
                    if(response.data.status==200){
                        that.$message({
                            type:'success',
                            message:response.data.data.detailMessage
                        })
                        that.changePasswordDialogVisible=false;
                    }else {
                        that.common.errorReturn(that,response.data.data.detailMessage);
                    }
                }).catch(error=>{
                    console.log('---error---',error);
                })
            },
            resetChangePasswordDialog(){
                this.$refs['changePasswordForm'].resetFields();
                this.changePasswordDialogVisible=false;
            },
            handleCommand(command){
                //This event fires when a drop-down list item is clicked
                switch (command) {
                    case 'a':
                        //Click to view national information, popup dialog box to view national information
                        this.showStudentInfo();
                        this.dialogFormVisible=true;
                        break;
                    case 'b':
                        //change password
                        this.changePasswordDialogVisible=true;
                        break;
                    case 'c':
                        //log out
                        this.$confirm('Logout or not', 'log out', {
                            distinguishCancelAndClose: true,
                            confirmButtonText: 'Confirm',
                            cancelButtonText: 'Cancel'
                        })
                            .then(() => {
                                localStorage.removeItem('token');
                                localStorage.removeItem('user');
                                this.$message({
                                    type: 'success',
                                    message: 'log out success'
                                });
                                this.$router.push("/");
                            })
                            .catch(action => {

                            });
                        break;
                }
            },
        }
    };
</script>

<style scoped>
    .el-menu-item.is-active{
        background-color: #409EFF !important;
        color: #fff;
    }
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 80px;
        height: 80px;
        line-height: 80px;
        border:1px solid black;
    }
    .avatar {
        width: 100px;
        height: 100px;
        display: block;
    }
    .all{
        height:100%;
    }
    .el-header{
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: #24262e;
        color: #fff;
    }
    .left_header{
        display: flex;
        align-items: center;
        font-size:20px;
    }
    .right_header{
        display: flex;
        align-items: center;
        margin-right:20px;

    }
    .left_header>span{
        margin-left:10px;
    }
    .el-menu span{
        font-size:15px;
    }
    .el-aside{
        background:#2d2f36;
        color: #ccc;
    }
    .el-main{
        background: rgb(241, 241, 241);
        overflow: hidden;
    }
    .el-menu{
        border-right:none;
    }
    .el-dropdown-link{
        cursor: pointer;
        display: flex;
        align-items: center;
        color: #fff;
        font-size:18px;
    }
</style>