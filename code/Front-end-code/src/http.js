import axios from 'axios'

//进度条
import NProgress from 'nprogress'

//请求拦截
axios.interceptors.request.use(config => {
    NProgress.start();
    if (localStorage.getItem('token')) {
        config.headers.token = localStorage.getItem('token');
    }
    return config;
}, error => { return Promise.reject(error) })



//响应拦截
axios.interceptors.response.use(response => {
    localStorage.setItem("token", response.headers.token);
    NProgress.done();
    return response;
}, error => {

})

export default axios