<template>
    <div class="login">
        <div class="info">
            <span class="title">Course Selection System</span>
            <el-form :model="login_Form" ref="login_Form" :rules="rules" class="demo-ruleForm">
                <el-form-item prop="sno">
                    <el-input type="text" v-model="login_Form.sno" class="login_item" placeholder="ID Number" clearable></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" v-model="login_Form.password" class="login_item" placeholder="password" clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" class="login_item" :loading="isLoading" @click="login('login_Form')">Login</el-button></el-button>
                </el-form-item>
                <el-link :underline="false"  type="primary" @click="findPasswordDialogVisible=true">Find Password<i class="el-icon-view el-icon--right"></i></el-link>
            </el-form>
        </div>

        <!--Find Password-->
        <el-dialog title="Find Password" :visible.sync="findPasswordDialogVisible"
                   width="35%" @close="resetFindPasswordDialog">
            <el-form :model="findPasswordForm" ref="findPasswordForm" :rules="rules">
                <el-form-item label="ID" :label-width="labelWidth" prop="sno">
                    <el-input v-model="findPasswordForm.sno" placeholder="please input your ID" clearable></el-input>
                </el-form-item>
                <el-form-item label="National ID" :label-width="labelWidth" prop="sid">
                    <el-input v-model="findPasswordForm.sid" placeholder="please input your National ID" clearable></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetFindPasswordDialog">Cancel</el-button>
                <el-button type="primary" @click="findPassword('findPasswordForm')">Confirm</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "login",
        data(){
            return {
                isLoading:false,
                findPasswordForm:{
                    sno:'',
                    sid:''
                },
                findPasswordDialogVisible:false,
                labelWidth:'100px',
                rules:{
                    sno:[{required:true,message:'please input ID',trigger:'blur'},
                        ],
                    password:[{required:true,message:'please input password',trigger:'blur'},
                        { min: 5, max: 20, message: 'It is between 5 and 20 characters long', trigger: 'blur'}],
                    sid:[{required:true,message:'please input National ID',trigger:'blur'},
                        {pattern:/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,message:'please input right National ID',trigger:'blur'}
                        ]
                },
                login_Form: {
                    sno: '',
                    password: ''
                }
            };
        },
        methods:{
            findPassword(formName){
                //find password
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        //Validates
                        let that=this;
                        let formData=new FormData();
                        formData.append('sno',this.findPasswordForm.sno);
                        formData.append('sid',this.findPasswordForm.sid);
                        this.$axios({
                            method:'post',
                            data:formData,
                            url:this.apiUrl.findPassword
                        }).then(response=>{
                            if(response.data.status==200){
                                this.$notify({
                                    title: 'Find Password',
                                    message: response.data.data.detailMessage,
                                    type: 'success'
                                });
                                that.findPasswordDialogVisible=false;
                            }else{
                                that.common.errorReturn(that,response.data.data.detailMessage);
                            }
                        }).catch(error=>{
                            console.log('---error---',error);
                        })
                    }
                })

            },
            resetFindPasswordDialog(){
                this.$refs['findPasswordForm'].resetFields();
                this.findPasswordDialogVisible=false;
            },
            login(formName){
                //login
                let formData=new FormData();
                formData.append("sno",this.login_Form.sno);
                formData.append('spassword',this.login_Form.password);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        //Validates
                        this.isLoading=true;
                        let that=this;
                        this.$axios({
                            method:'post',
                            url:this.apiUrl.login,
                            data:formData
                        }).then(response=>{

                            if(response.data.status==200){

                                //Login Success
                                that.$message({
                                    message:'Login Success',
                                    type:'success '
                                });
                                localStorage.setItem('user',JSON.stringify(response.data.data.data));
                                that.$router.push("/index");
                            }else{
                                that.common.errorReturn(that,response.data.data.detailMessage);
                            }
                            that.isLoading=false;
                        }).catch(err=>{
                            console.log('----err---',err);
                        })
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .login{
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        min-width: 800px;
        min-height:600px;
        background-image: url("../assets/images/logo1.jpg");
        background-position: 0px -30px;
        background-size: cover;
        background-repeat: no-repeat;
    }
    .info{
        padding:40px;
        background-color: rgb(46, 42, 42,0.7);
        position: absolute;
        right:37%;
        top:20%;
        border-radius:15px;
    }
    .demo-ruleForm{
        display: flex;
        flex-direction: column;
        align-content: center;
        padding:20px;
    }
    .login_item{
        width:100%;
        height:45px;
        font-size:18px;
    }
    .title{
        font-family:"Hiragino Sans GB";
        margin-left:25px;
        font-size:25px;
        color:#fff;
    }
</style>