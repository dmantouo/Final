//覆盖webpack的默认配置
module.exports={

    chainWebpack:config =>{

        //图片压缩
        //chainWebpack 与 configureWebpack，只是使用方式不同，一个是以对象的形式，另一个是以链式
        config.when(process.env.NODE_ENV=='production',config=>{

            //压缩图片
            config.module
                .rule('images')
                .use('image-webpack-loader')
                .loader('image-webpack-loader')
                .options({
                    bypassOnDebug: true
                })
                .end()

            //发布模式
            config.entry('app').clear().add('./src/main-prod.js')

            //,e.prototype.$ELEMENT
            //VueRouter
            // axios:'axios',
            config.set('externals',{
                //key是包，value是对象
                vue:'Vue',
                vuex: "Vuex",
                'axios':'axios',
                echarts: 'echarts',
                nprogress:'NProgress',
                'vue-router':'VueRouter',
                'element-ui':'ELEMENT',
                xlsx:'XLSX',
                'vue-lazyload':'VueLazyload '
            })
        })

        config.when(process.env.NODE_ENV=='development',config=>{

            //压缩图片
            config.module
                .rule('images')
                .use('image-webpack-loader')
                .loader('image-webpack-loader')
                .options({
                    bypassOnDebug: true
                })
                .end()

            //开发模式
            config.entry('app').clear().add('./src/main-dev.js')
            config.set('externals',{
                //key是包，value是对象
                vue:'Vue',
                vuex: "Vuex",
                'axios':'axios',
                echarts: 'echarts',
                nprogress:'NProgress',
                'vue-router':'VueRouter',
                'element-ui':'ELEMENT',
                xlsx:'XLSX',
                'vue-lazyload':'VueLazyload '
            })
        })
    }
}