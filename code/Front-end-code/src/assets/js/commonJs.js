
export function errorReturn(currentObject,message) {
    if (message == 'please log again') {
        currentObject.$message({
            type: 'error',
            message: 'please log again'
        });
        currentObject.$router.push('/');
    } else {
        currentObject.$message({
            type: 'error',
            message: message
        });
    }
}

export function uploadData(currentObject,api) {

    if(currentObject.file==null){
        currentObject.$message({
            type:'error',
            message:'please click add file'
        })
    }else{
        const loading=currentObject.$loading({
            body:true,
            fullscreen:true,
            lock:true,
            text:'uploading',
            spinner:'el-icon-loading',
            background:'rgba(0, 0, 0, 0.8)'
        })
        let that=currentObject;
        let formData=new FormData();
        formData.append('file',currentObject.file.raw)
        currentObject.$axios({
            method: 'post',
            url:api,
            headers:{
                'Content-Type':'multipart/form-data'
            },
            data:formData
        }).then(response=>{
            if(response.data.status==200){
                that.$message({
                    type:'success',
                    message:'upload success'
                })
                that.reload();
            }else{
                that.common.errorReturn(that,response.data.data.detailMessage);
            }
            loading.close();   
        }).catch(error=>{
            console.log('-----Request failed----',error);
        });
    }

}

export function getAllDataByConditionAndPage(currentObject,api) {
    //A common interface to fetch all the data
    let that=currentObject;
    let params={
        condition:currentObject.queryInfo.condition,
        currentPage:currentObject.queryInfo.currentPage,
        pageSize:currentObject.queryInfo.pageSize
    }
    currentObject.$axios.get(api,{
        params:params
    }).then(response=> {
        if (response.data.status == 200) {
            that.tableData = response.data.data.data;
            that.total = response.data.data.total;

        } else {
            that.common.errorReturn(that,response.data.detailMessage);
        }
    })
}

export function getAllData(currentObject,api) {
    let that=currentObject;
    currentObject.$axios.get(api).then(response=>{
        if(response.data.status==200){
            that.allTableData=response.data.data.data;
        }else{
            that.common.errorReturn(that,response.data.data.detaiMessage);
        }
    }).catch(error=>{
        console.log('---error---',error);
    })
}

export function selectDetailInfo(currentObject,api,no) {
    if(JSON.parse(localStorage.getItem('user')).power=='teacher'){
        currentObject.infoDialog=true;
        let that=currentObject;
        let params={
            sno:no,
            tno:no,
            dno:no,
            cno:no
        }
        currentObject.$axios.get(api,{
            params:params
        }).then(response=>{
            if(response.data.status==200){
                that.InfoForm=response.data.data.data;
                that.InfoForm["studentNo"]=no;
                that.InfoForm["teacherNo"]=no;
                that.InfoForm["departmentNo"]=no;
                that.InfoForm["courseNo"]=no;
            }
        }).catch(error=>{
            console.log('----error---',error);
        })
    }else {
        currentObject.$notify({
            title: 'Warning',
            message: 'you are not the teacher',
            type: 'warning'
        });
    }

}

export function changeInfo(currentObject,api) {
    let that=currentObject;
    currentObject.$axios({
        method:'post',
        data:currentObject.qs.stringify(currentObject.InfoForm),
        url:api
    }).then(response=>{
        if(response.data.status==200){
            that.$message({
                type:'success',
                message:'success'
            })
            that.infoDialog=false;
            that.reload();
        }else{
            that.common.errorReturn(that,response.data.data.detailMessage)
        }
    }).catch(error=>{
        console.log('---error---',error);
    })
}

export function addOne(currentObject,api){
        let that=currentObject;
        currentObject.$axios({
            method:'post',
            data:currentObject.qs.stringify(currentObject.form),
            url:api
        }).then(res=>{
            if(res.data.status==200){
                that.$message({
                    type:'success',
                    message:'add success'
                })
                that.dialogFormVisible=false;
                that.reload();
            }else{
                that.common.errorReturn(that,res.data.data.detailMessage)
            }
        }).catch(error=>{
            console.log('---error--',error);
        })
}

export function deletes(currentObject,api_one,api_multiple,deleteObject,no){
    if(JSON.parse(localStorage.getItem('user')).power=='teacher'){
        if(currentObject.multipleSelection.length>1){
            for(let i=0;i<currentObject.multipleSelection.length;i++){
                if(currentObject.multipleSelection[i].sno!=undefined){
                    currentObject.selectNos[i]=currentObject.multipleSelection[i].sno;
                }
                if(currentObject.multipleSelection[i].tno!=undefined){
                    if(currentObject.multipleSelection[i].cno==undefined){
                        currentObject.selectNos[i]=currentObject.multipleSelection[i].tno;
                    }
                }
                if(currentObject.multipleSelection[i].dno!=undefined){
                    currentObject.selectNos[i]=currentObject.multipleSelection[i].dno;
                }
                if(currentObject.multipleSelection[i].cno!=undefined){
                    currentObject.selectNos[i]=currentObject.multipleSelection[i].cno;
                }
            }
            currentObject.$confirm('sure to delete '+deleteObject+' information?', 'Tips', {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'success'
            }).then(() => {
                let that = currentObject;
                let formData=new FormData();
                formData.append('snoArrays',currentObject.selectNos);
                formData.append('tnoArrays',currentObject.selectNos);
                formData.append("dnoArrays",currentObject.selectNos);
                formData.append("cnoArrays",currentObject.selectNos);
                currentObject.$axios({
                    method:'post',
                    data:formData,
                    url:api_multiple
                }).then(res=>{
                    if(res.data.status==200){
                        that.$message({
                            message: 'success',
                            type: 'success '
                        });
                        that.reload();
                    }else{
                        that.common.errorReturn(that,res.data.data.detailMessage);
                    }
                }).catch(error=>{
                    console.log('---error---',error);
                })
            }).catch(() => {
            });
        }else{
            currentObject.$confirm('sure to delete this information?', 'Tips', {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'success'
            }).then(() => {
                let that = currentObject;
                let params = {
                    sno:no,
                    tno:no,
                    dno:no,
                    cno:no
                };
                currentObject.$axios.get(api_one, {
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
    }else{
        currentObject.$notify({
            title: 'Warning',
            message: 'you are not the teacher',
            type: 'warning'
        });
    }

}

export function judgeIsLogin() {
    if(!localStorage.getItem('token')){
        this.$router.push("/");
    }
}