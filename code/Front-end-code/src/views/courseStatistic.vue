<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Data</el-breadcrumb-item>
            <el-breadcrumb-item>course data</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="totalAndCircle">
            <el-card shadow="always" class="totalCard">
                <p class="title">Total Course</p>
                <div class="photoAndNums">
                    <el-image style="width:100px;height:100px" :src="courseIcon" fit="contain"></el-image>
                    <p>{{total}}</p>
                </div>
            </el-card>
            <el-card shadow="always" class="detailCard" id="selectCourseGroupByCcredit">
            </el-card>
        </div>
        <el-card shadow="always" id="boyAndGirl_rate">
            <p class="title">Top Courses Ranking</p>
            <el-carousel type="card" height="300px">
                <el-carousel-item v-for="item in courseData" :key="item.cno">
                    <h3>course name:{{ item.cname}}</h3>
                    <h3>teacher:{{ item.tname}}</h3>
                    <h3>student number:{{ item.total}}</h3>
                </el-carousel-item>
            </el-carousel>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "courseStatistic",
        data(){
            return {
                courseIcon:require("../assets/images/course.png"),
                courseGroupByCredit:[],       
                legendData:[],          
                seriesData:[],        
                total:0,
                courseData:[],
                gold:require("../assets/images/gold.png"),
                sliver:require("../assets/images/sliver.png"),
                copper:require("../assets/images/copper.png")
            }
        },
        mounted(){
            this.getAllCourse();
            this.selectCourseGroupByCcredit();
            this.studyCourseFromStudent();
        },
        methods:{
            getAllCourse(){
                let that=this;
                this.$axios.get(this.apiUrl.courseCounts).then(response=>{
                    that.total=JSON.parse(response.data.data.total);
                }).catch(error=>{
                    console.log('---error--',error);
                })
            },
            selectCourseGroupByCcredit(){
                let that=this;
                this.$axios.get(this.apiUrl.selectCourseGroupByCcredit).then(response=>{
                      that.courseGroupByCredit=JSON.parse(response.data.data.data);
                      for(let i=0;i< that.courseGroupByCredit.length;i++){
                          that.legendData[i]=that.courseGroupByCredit[i].ccredit
                      }
                      that.showPie();
                }).catch(error=>{
                    console.log('--error--',error);
                })
            },
            getData(){
                //Get the pie chart data
                for(let i=0;i<this.courseGroupByCredit.length;i++){
                    this.seriesData.push({
                        name:this.courseGroupByCredit[i].ccredit,
                        value:this.courseGroupByCredit[i].total
                    })
                }
                return {
                    legendData:this.legendData,
                    seriesData:this.seriesData
                }
            },
            showPie(){
                let data=this.getData();
                let myChart = this.$echarts.init(document.getElementById('selectCourseGroupByCcredit'),"light");
                let option={
                    title: {
                        text: 'Number of courses per credit',
                        left: 'right'
                    },
                    tooltip: {
                        //Display content for each item of the fan chart when the mouse moves over it
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        //Fan-shaped component (far left)
                        orient: 'vertical',
                        left: 'left',
                        data:data.legendData
                    },
                    series: [
                        {
                            name: 'Source of access',
                            type: 'pie',   
                            radius:["50%", "70%"],
                            center: ['50%', '60%'],
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: "center"
                                },
                                emphasis: {
                                    show: false,
                                    textStyle: {
                                        fontSize: "30",
                                        fontWeight: "bold"
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:data.seriesData,
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };
                myChart.setOption(option);
            },
            studyCourseFromStudent(){
                let that=this;
                let params={
                    condition:"",
                    currentPage:1,
                    pageSize:3
                }
                this.$axios.get(this.apiUrl.studyCourseFromStudent,{
                    params:params
                }).then(response=>{
                    that.courseData=response.data.data.data;
                }).catch(error=>{
                    console.log('---error--',error);
                })
            }
        }
    }
</script>

<style scoped>
    .title{
        font-size: 20px;
        font-weight: bold;
    }
    .el-breadcrumb{
        margin-bottom: 15px;
    }
    #boyAndGirl_rate{
        width: 99%;
        height:400px;
        margin:0 10px;
        padding:15px;
    }
    .totalCard,.detailCard{
        width: 50%;
        margin:10px;
        padding:10px;
    }
    .photoAndNums{
        width: 30%;
        margin: 10px auto;
        text-align: center;
        font-size:30px;
    }
    .totalAndCircle{
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        margin-bottom:10px;
    }
    .el-carousel__item h3 {
        color:#303133;
        font-size: 18px;
        opacity: 0.75;
        text-align: center;
    }
    .el-carousel__item:nth-child(2n),.el-carousel__item:nth-child(2n+1){
        background:  linear-gradient(to right, #eea2a2 0%, #bbc1bf 19%, #57c6e1 42%, #b49fda 79%, #7ac5d8 100%);
        box-shadow: -5px -5px 5px 5px rgba(0, 0, 0, .05);
        -webkit-box-shadow: #d4d2d2 5px 5px 10px;
        -moz-box-shadow: #d4d2d2 5px 5px 10px;
    }
</style>