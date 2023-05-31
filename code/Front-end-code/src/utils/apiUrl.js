const baseUrl='http://localhost:8888'
const apiUrl={
    //login
    login:baseUrl+'/login',
    //get student information by page
    selectStudentByConditionAndPage:baseUrl+'/selectStudentByConditionAndPage',
    //delete student by ID
    deleteStudentByNo:baseUrl+'/deleteStudentByNo',
    //delete multiple student
    deleteMultipleStudent:baseUrl+'/deleteMultipleStudent',
    //add one student
    addStudent:baseUrl+'/addStudent',
    //get all faculty
    selectAllDepartment:baseUrl+'/selectAllDepartment',
    //add multiple student
    addMultipleStudent:baseUrl+'/addMultipleStudent',
    //get all student information
    selectAllStudents:baseUrl+'/selectAllStudents',
    //get student information by ID
    selectStudentByNo:baseUrl+'/selectStudentByNo',
    //change student information
    updateInfo:baseUrl+'/updateInfo',
    //get teacher information by page
    selectTeacherByPageAndCondition:baseUrl+'/selectTeacherByPageAndCondition',
    //get all course information
    selectAllCourse:baseUrl+'/selectAllCourse',
    //add one teacher
    addTeacher:baseUrl+'/addTeacher',
    //delete one teacher
    deleteTeacherByNo:baseUrl+'/deleteTeacherByNo',
    //delete multiple teacher
    deleteMultipleTeacher:baseUrl+'/deleteMultipleTeacher',
    //get all teacher information
    selectAllTeacher:baseUrl+'/selectAllTeacher',
    selectTeacherBytno:baseUrl+'/selectTeacherBytno',
    updateTeacherInfo:baseUrl+'/updateTeacherInfo',
    selectDepartmentByPageAndCondition:baseUrl+'/selectDepartmentByPageAndCondition',
    addDepartment:baseUrl+'/addDepartment',
    deleteDepartment:baseUrl+'/deleteDepartment',
    deleteMultipleDepartment:baseUrl+'/deleteMultipleDepartment',
    selectCourseByPageAndCondition:baseUrl+'/selectCourseByPageAndCondition',
    addCourse:baseUrl+'/addCourse',
    selectAllCourses:baseUrl+'/selectAllCourses',
    deleteMultipleCourse:baseUrl+'/deleteMultipleCourse',
    deleteCourseByNo:baseUrl+'/deleteCourseByNo',
    selectCourseByCno:baseUrl+'/selectCourseByCno',
    updateCourseInfo:baseUrl+'/updateCourseInfo',
    selectSctByPageAndCondition:baseUrl+'/selectSctByPageAndCondition',
    selectAllSct:baseUrl+'/selectAllSct',
    deleteSctBySno:baseUrl+'/deleteSctBySno',
    deleteMultipleSct:baseUrl+'/deleteMultipleSct',
    selectSctByNos:baseUrl+'/selectSctByNos',
    changeGradeByNos:baseUrl+'/changeGradeByNos',
    addSct:baseUrl+'/addSct',
    studentStatistic:baseUrl+'/studentStatistic',
    departmentCount:baseUrl+'/departmentCount',
    selectDepartmentRate:baseUrl+'/selectDepartmentRate',
    teacherCounts:baseUrl+'/teacherCounts',
    selectTeacherOrderByTeb:baseUrl+'/selectTeacherOrderByTeb',
    selectTeacherOrderByTpt:baseUrl+'/selectTeacherOrderByTpt',
    selectCourseGroupByCcredit:baseUrl+'/selectCourseGroupByCcredit',
    courseCounts:baseUrl+'/courseCounts',
    studyCourseFromStudent:baseUrl+'/studyCourseFromStudent',
    studentStudyCourse:baseUrl+'/studentStudyCourse',
    updatePasswordByPreviewPassword:baseUrl+'/updatePasswordByPreviewPassword',
    selectStudentInfoBySno:baseUrl+'/selectStudentInfoBySno',
    findPassword:baseUrl+'/findPassword',
    selectDepartmentByNo:baseUrl+'/selectDepartmentByNo',
    updateDepartmentInfo:baseUrl+'/updateDepartmentInfo',
    selectSctByPageAndConditionAndSno:baseUrl+'/selectSctByPageAndConditionAndSno',
    selectAllSctByNo:baseUrl+'/selectAllSctByNo',
    changeSidBySno:baseUrl+'/changeSidBySno',
    studyCourseFromStudentToExcel:baseUrl+'/studyCourseFromStudentToExcel',
    studentStudyCourseToExcel:baseUrl+'/studentStudyCourseToExcel',
}
export default  apiUrl