<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Data</el-breadcrumb-item>
            <el-breadcrumb-item>teacher data</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="totalAndCircle">
            <el-card shadow="always" class="totalCard">
                <!--教师总数显示-->
                <p class="title">teacher number:{{total}}</p>
                <div class="photoAndNums">
                    <el-image style="width:80px;height:80px" :src="boyTeacher" fit="contain"></el-image>
                    <p>{{man}}</p>
                    <el-image style="width:80px;height:80px" :src="girlTeacher" fit="contain"></el-image>
                    <p>{{woman}}</p>
                </div>
            </el-card>

            <el-card shadow="always" class="detailCard" id="teacher_teb">
            </el-card>
        </div>

        <el-card shadow="always" id="teacher_tpt">
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "teacherStatistic",
        data(){
            return {
                girlTeacher:require("../assets/images/girlTeacher.png"),
                boyTeacher:require("../assets/images/boyTeacher.png"),
                total:0,     
                woman:0,      
                man:0,        
                teacher_tebBarData:[ ['product', 'boy', 'girl']],   
                teacher_tptBarData:[['product', 'boy', 'girl']],     
            }
        },
        mounted(){
            this.getAllTeacherCount();
            this.selectTeacherOrderByTeb();
            this.selectTeacherOrderByTpt();
        },
        methods:{
            getAllTeacherCount(){
                let that=this;
                this.$axios.get(this.apiUrl.teacherCounts).then(response=>{
                    that.total=JSON.parse(response.data.data.data).total;
                    that.man=JSON.parse(response.data.data.data).man;
                    that.woman=JSON.parse(response.data.data.data).woman
                }).catch(error=>{
                    console.log('--error--',error);
                })

            },
            selectTeacherOrderByTeb(){
                let that=this;
                this.$axios.get(this.apiUrl.selectTeacherOrderByTeb).then(response=>{
                    let datas=JSON.parse(response.data.data.data);
                    for(let i=0;i<datas.length;i++){
                        let arr=new Array();
                        arr.push(datas[i].teb);
                        arr.push(datas[i].man);
                        arr.push(datas[i].woman);
                        that.teacher_tebBarData.push(arr);
                    }
                    that.showTeacher_tebBar();
                }).catch(error=>{
                    console.log('---error---',error);
                })
            },
            selectTeacherOrderByTpt(){
                let that=this;
                this.$axios.get(this.apiUrl.selectTeacherOrderByTpt).then(response=>{
                    let datas=JSON.parse(response.data.data.data);
                    for(let i=0;i<datas.length;i++){
                        let arr=new Array();
                        arr.push(datas[i].tpt);
                        arr.push(datas[i].man);
                        arr.push(datas[i].woman);
                        that.teacher_tptBarData.push(arr);
                    }

                    that.showTeacher_tptBar();
                }).catch(error=>{
                    console.log('---error----',error);
                })
            },
            showTeacher_tebBar(){
                let data=this.getData();
                let myChart = this.$echarts.init(document.getElementById('teacher_teb'),"light");
                let option={
                    color:['#4169E1','#FF00FF'],
                    title: {
                        text: 'boy to girl in degree',
                        left: 'right'
                    },
                    legend:{},
                    tooltip: {},
                    dataset: {
                        source:data.teacher_tebBarData
                    },
                    xAxis: {type: 'category'},
                    yAxis: {},
                    series: [
                        {
                            type: 'bar',
                        },
                        {
                            type: 'bar',
                        }
                    ]
                }
                myChart.setOption(option);
            },
            showTeacher_tptBar(){
                //显示教师职称男女比例柱状图
                let data=this.getData();
                let myChart = this.$echarts.init(document.getElementById('teacher_tpt'),"light");
                let option={
                    color:['#00BFFF','#DC143C'],
                    title: {
                        text: 'boy to girl in title',
                        left: 'left'
                    },
                    legend:{},
                    tooltip: {},
                    dataset: {
                        source:data.teacher_tptBarData
                    },
                    xAxis: {type: 'category'},
                    yAxis: {},
                    series: [
                        {
                            type: 'bar',
                        },
                        {
                            type: 'bar',
                        }
                    ]
                }
                myChart.setOption(option);
            },
            getData(){
                return {
                    teacher_tebBarData:this.teacher_tebBarData,
                    teacher_tptBarData:this.teacher_tptBarData
                }
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
    #teacher_tpt{
        width: 99%;
        height:300px;
        margin: 0 10px;
        padding:20px;
    }
    .totalCard,.detailCard{
        width: 50%;
        margin:10px;
        padding:5px;
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
</style>