<template>
    <div class="title">
        <el-carousel :interval="4000" class="carousel" height="600px">
            <el-carousel-item v-for="item in 4" :key="item">
                <el-image :src="require('../assets/images/'+item+'.jpg')"
                          fit="fill"
                          style="width:100%; height:100%;">
                </el-image>
            </el-carousel-item>
        </el-carousel>
        <div class="btn">
            <router-link to="/courseInfo" style="text-decoration: none;">
                <div class="btn1">
                    <img src="../assets/images/select.png" width="60px" height="60px" class="img">
                    <span>Select Course</span>
                </div>
            </router-link>
            <router-link to="/MySctInfo" style="text-decoration: none;">
                <div class="btn1">
                    <img src="../assets/images/check.png" width="60px" height="60px" class="img">
                    <span>My course</span>
                </div>
            </router-link>
        </div>
    </div>
</template>

<script>
export default {
        name: "welcome",
        data(){
            return {

            }
        },
        created(){

        },
        methods:{

        }
    }
</script>

<style scoped>
.title{
    width: 900px;
}
.btn{
    height: 600px;
    width: 360px;
    position: absolute;
    right: 20px;
    top: 80px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}
.btn1{
    color: #515151;
    font-size: 38px;
    line-height: 280px;
    padding-left: 40px;
    width: 360px;
    height: 280px;
    border-radius: 12px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;
}
.btn1:hover{
    border-radius: 12px;
    cursor: pointer;
    box-shadow: 0 19px 39px 0 #999999;
    transform: translate(0, -4px);
}
.img{
    position: relative;
    top: 10px;
    right: 10px;
}
</style>