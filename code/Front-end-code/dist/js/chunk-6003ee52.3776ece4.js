(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6003ee52"],{"45bc":function(e,t,a){},"8b9c":function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-breadcrumb",{attrs:{separator:"/"}},[a("el-breadcrumb-item",{attrs:{to:{path:"/index"}}},[e._v("Home")]),a("el-breadcrumb-item",[e._v("Information")]),a("el-breadcrumb-item",[e._v("faculties")])],1),a("el-card",{attrs:{shadow:"always"}},[a("el-row",{attrs:{gutter:10}},[a("el-col",{attrs:{span:8}},[a("el-input",{attrs:{placeholder:"search by faculty name",clearable:""},on:{clear:e.selectDepartmentByPageAndCondition},model:{value:e.queryInfo.condition,callback:function(t){e.$set(e.queryInfo,"condition",t)},expression:"queryInfo.condition"}},[a("el-button",{attrs:{slot:"append",icon:"el-icon-search"},on:{click:e.selectDepartmentByPageAndCondition},slot:"append"})],1)],1),a("el-col",{attrs:{span:4,offset:12}},[a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.judgeGrade()}}},[e._v("add faculty")])],1)],1),a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"",border:""},on:{"selection-change":e.handleSelectionChange}},[a("el-table-column",{attrs:{type:"selection",width:"40"}}),a("el-table-column",{attrs:{prop:"dno",label:"faculty ID"}}),a("el-table-column",{attrs:{prop:"dname",label:"faculty name"}}),a("el-table-column",{attrs:{prop:"dmanager",label:"faculty leader"}}),a("el-table-column",{attrs:{label:"operate"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"primary",icon:"el-icon-edit",size:"mini"},on:{click:function(a){return e.showInfo(t.row.dno)}}}),a("el-button",{attrs:{type:"danger",icon:"el-icon-delete",size:"mini"},on:{click:function(a){return e.deleteDepartment(t.row.dno)}}})]}}])})],1),a("el-pagination",{attrs:{"current-page":e.queryInfo.currentPage,"page-sizes":[1,2,5,10],"page-size":e.queryInfo.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),a("el-dialog",{attrs:{title:"add faculty",visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t},close:e.resetDialog}},[a("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules}},[a("el-form-item",{attrs:{label:"faculty ID","label-width":e.formLabelWidth,prop:"dno"}},[a("el-input",{attrs:{placeholder:"please input faculty ID",clearable:""},model:{value:e.form.dno,callback:function(t){e.$set(e.form,"dno",t)},expression:"form.dno"}})],1),a("el-form-item",{attrs:{label:"faculty name","label-width":e.formLabelWidth,prop:"dname"}},[a("el-input",{attrs:{placeholder:"please input faculty name",clearable:""},model:{value:e.form.dname,callback:function(t){e.$set(e.form,"dname",t)},expression:"form.dname"}})],1),a("el-form-item",{attrs:{label:"faculty leader","label-width":e.formLabelWidth,prop:"dmanager"}},[a("el-input",{attrs:{placeholder:"please input faculty leader",clearable:""},model:{value:e.form.dmanager,callback:function(t){e.$set(e.form,"dmanager",t)},expression:"form.dmanager"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.resetDialog}},[e._v("Cancel")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.addDepartment("form")}}},[e._v("Confirm")])],1)],1),a("el-dialog",{attrs:{title:"change information",visible:e.infoDialog},on:{"update:visible":function(t){e.infoDialog=t},close:e.resetInfoDialog}},[a("el-form",{ref:"InfoForm",attrs:{model:e.InfoForm,rules:e.rules}},[a("el-form-item",{attrs:{label:"faculty ID","label-width":e.formLabelWidth,prop:"dno"}},[a("el-input",{attrs:{clearable:""},model:{value:e.InfoForm.dno,callback:function(t){e.$set(e.InfoForm,"dno",t)},expression:"InfoForm.dno"}})],1),a("el-form-item",{attrs:{label:"faculty name","label-width":e.formLabelWidth,prop:"dname"}},[a("el-input",{model:{value:e.InfoForm.dname,callback:function(t){e.$set(e.InfoForm,"dname",t)},expression:"InfoForm.dname"}})],1),a("el-form-item",{attrs:{label:"faculty leader","label-width":e.formLabelWidth,prop:"dmanager"}},[a("el-input",{model:{value:e.InfoForm.dmanager,callback:function(t){e.$set(e.InfoForm,"dmanager",t)},expression:"InfoForm.dmanager"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.resetInfoDialog}},[e._v("Cancel")]),a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.changeInfo("InfoForm")}}},[e._v("Confirm")])],1)],1)],1)},l=[],o={inject:["reload"],name:"departmentInfo",data(){return{rules:{dno:[{require:!0,message:"please input faculty ID",trigger:"blur"},{pattern:/\b\d{8}\b/,message:"Please enter the 8-digit faculty ID",trigger:"blur"}],dname:[{required:!0,message:"please input faculty name",trigger:"blur"}],dmanager:[{required:!0,message:"please input faculty leader",trigger:"blur"}]},multipleSelection:[],selectNos:[],formLabelWidth:"120px",queryInfo:{condition:"",currentPage:1,pageSize:10},total:0,form:{dno:"",dname:"",dmanager:""},InfoForm:{dno:"",dname:"",dmanager:""},tableData:[],dialogFormVisible:!1,infoDialog:!1}},created(){this.selectDepartmentByPageAndCondition()},methods:{judgeGrade(){"teacher"==JSON.parse(localStorage.getItem("user")).power?this.dialogFormVisible=!0:this.$notify({title:"Warning",message:"You do not have permission",type:"warning"})},changeInfo(e){this.$refs[e].validate(e=>{e&&this.common.changeInfo(this,this.apiUrl.updateDepartmentInfo)})},showInfo(e){this.common.selectDetailInfo(this,this.apiUrl.selectDepartmentByNo,e)},resetInfoDialog(){this.$refs["InfoForm"].resetFields(),this.infoDialog=!1},deleteDepartment(e){this.common.deletes(this,this.apiUrl.deleteDepartment,this.apiUrl.deleteMultipleDepartment,"faculty",e)},addDepartment(e){this.$refs[e].validate(e=>{e&&this.common.addOne(this,this.apiUrl.addDepartment)})},resetDialog(){this.$refs["form"].resetFields(),this.dialogFormVisible=!1},handleSelectionChange(e){this.multipleSelection=e},handleSizeChange(e){this.queryInfo.pageSize=e,this.selectDepartmentByPageAndCondition()},handleCurrentChange(e){this.queryInfo.currentPage=e,this.selectDepartmentByPageAndCondition()},selectDepartmentByPageAndCondition(){this.common.getAllDataByConditionAndPage(this,this.apiUrl.selectDepartmentByPageAndCondition)}}},r=o,i=(a("f094"),a("2877")),s=Object(i["a"])(r,n,l,!1,null,"ea7f4e20",null);t["default"]=s.exports},f094:function(e,t,a){"use strict";var n=a("45bc"),l=a.n(n);l.a}}]);
//# sourceMappingURL=chunk-6003ee52.3776ece4.js.map