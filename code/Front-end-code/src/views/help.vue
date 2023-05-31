<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Help</el-breadcrumb-item>
            <el-breadcrumb-item>Help booklet</el-breadcrumb-item>
        </el-breadcrumb>

        <!--卡片布局-->
        <el-card shadow="always">
                <h2>1.login</h2>
                    <h3>input ID and password to login</h3>
                <h2>2.find password</h2>
                     <h3>input ID and personal ID to find password</h3>

                <h2>3.Information</h2>
                    <h3>
                        <el-image :src="require('@/assets/images/photo.png')"
                                 style="width:80%; height:400px;margin-bottom:10px;" fit="fill"></el-image>
                        <h3>1.click add one</h3>
                        <h3>
                            <strong>2.add by excel:</strong>first click add file,then select excel file to upload,up load data after file upload success<br/>
                            <p style="text-indent:6em;">Tips:<span style="color:red;">excel table data type to correspond and consistent with the table data type on the page, otherwise it is easy to report errors</span></p>
                        </h3>
                        <h3>3.export excel:click data export,The system automatically exports the table data in the page to an excel sheet</h3>
                        <h3>4.Students do not have the permission to add, modify, or delete, but have the permission to query. Teacher has all permissions</h3>
                    </h3>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "help",
        data(){
            return {

            }
        }
    }
</script>

<style scoped>
    .el-breadcrumb{
        margin-bottom: 15px;
    }
    h1{
        margin:20px 0;
    }
    h2{
        text-indent: 2em;
        margin:10px 0;
    }
    h3{
        margin:5px 0;
        text-indent: 4em;
    }
</style>