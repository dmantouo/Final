<!--院系报表-->
<template>
    <div>
        <!--Breadcrumb-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item>Data</el-breadcrumb-item>
            <el-breadcrumb-item>faculties data</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="totalAndCircle">
            <el-card shadow="always" class="totalCard">
                <p class="title">Faculty number</p>
                <div class="photoAndNums">
                    <el-image style="width:100px;height:100px" :src="departmentIcon" fit="contain"></el-image>
                    <p>{{total}}</p>
                </div>
            </el-card>

            <el-card shadow="always" class="detailCard" id="departmentContainStudent">
            </el-card>
        </div>

        <el-card shadow="always" id="boyAndGirl_rate">
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "departmentStatistic",
        data(){
            return {
                departmentIcon:require("../assets/images/department.png"),
                total:0,     
                departmentStatistic:[],  //The number of faculties and the ratio of men to women in the faculties
                departments:[],       
                seriesData:[],         
                barData:[ ['product', 'boy', 'girl']]              
            }
        },
        mounted(){
            this.getAllDepartment();
            this.selectDepartmentRate();
        },
        methods:{
            getAllDepartment(){
                let that=this;
                this.$axios.get(this.apiUrl.departmentCount).then(response=>{
                    that.total=JSON.parse(response.data.data.total);
                }).catch(error=>{
                    console.log('--error--',error);
                })
            },
            selectDepartmentRate(){
                let that=this;
                this.$axios.get(this.apiUrl.selectDepartmentRate).then(response=>{
                    that.departmentStatistic=JSON.parse(response.data.data.data);
                    for(let i=0;i<that.departmentStatistic.length;i++){
                        that.departments[i]=that.departmentStatistic[i].department
                        let arr=new Array();
                        arr.push(that.departmentStatistic[i].department);
                        arr.push(that.departmentStatistic[i].boy);
                        arr.push(that.departmentStatistic[i].girl);
                        that.barData.push(arr);
                    }
                    that.showPie();
                    that.showBar();
                }).catch(error=>{
                    console.log('--error--',error);
                })
            },
            showPie(){
                let data=this.getData();
                let myChart = this.$echarts.init(document.getElementById('departmentContainStudent'),"light");
                let option={
                    title: {
                        text: 'student number',
                        left: 'right'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data:data.legendData
                    },
                    series: [
                        {
                            name: 'data source',
                            type: 'pie',    
                            radius: '70%',  
                            center: ['60%', '50%'], 
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
            getData(){
                let legendData=this.departments;
                for(let i=0;i<this.departmentStatistic.length;i++){
                    this.seriesData.push(
                        {
                            name:this.departmentStatistic[i].department,
                            value:this.departmentStatistic[i].total
                        }
                    )
                }
                console.log(this.barData);
                return {
                    legendData:legendData,
                    seriesData: this.seriesData,
                    barData:this.barData
                }
            },
            showBar(){
                let data=this.getData();
                let myChart = this.$echarts.init(document.getElementById('boyAndGirl_rate'),"light");
                let option={
                    color: ['#46A3FF','#DA70D6'],
                    title: {
                        text: 'boy to girl ratio in each college',
                        left: 'left'
                    },
                    legend:{},
                    tooltip: {},
                    dataset: {
                        source:data.barData
                    },
                    xAxis: {
                        type: 'category',
                        axisLabel: {
                            interval:0
                        }
                    },
                    yAxis: {},
                    series: [
                        {type: 'bar'},
                        {type: 'bar'},

                    ]
                }
                myChart.setOption(option);
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
        margin: 0 10px;
        height:300px;
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