import path from 'path'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import modifyHtmlPlugin from './plugins/modifyHtmlPlugin'
// import legacy from '@vitejs/plugin-legacy';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, __dirname)
  console.log('env', mode, env);
  // env.NODE_ENV_DEV = process.env.NODE_ENV_DEV
  const __VERSION__ = process.env.npm_package_version
  const chunkStr = Number(Math.random().toString().substring(2, 6)).toString(36)
  let isFirst = false
  return {
    define: {
      __VERSION__: JSON.stringify(__VERSION__)
    },
    plugins: [
      vue(),
      modifyHtmlPlugin(),
      // legacy({
      //   targets: ['Chrome 63'],
      //   additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
      //   modernPolyfills: true,
      // }),
    ],
    resolve: {
      alias: {
        '@': path.resolve(process.cwd(), 'src'),
      },
    },
    base: env.VITE_MODE_BASE_URL || './',
    server: {
      host: true, //'0.0.0.0',
      port:  5174,
      proxy: {
        '/api': {
          target: env.VITE_MODE_API_URL,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        },
        '/_gpt': {
          target: env.VITE_MODE_API_URL,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/_gpt/, '')
        }
      }
    },
    css:{
      preprocessorOptions: {
        scss: {
          additionalData: '@import "@/assets/scss/global.scss"; '
        }
      }
    },
    build: {
      target: 'es2015',
      reportCompressedSize: false,
      sourcemap: false,
      commonjsOptions: {
        ignoreTryCatch: false,
      },
      rollupOptions: {
        output: {
          chunkFileNames: `assets/chunk.${chunkStr}.js`,
          entryFileNames: `assets/bundle.${__VERSION__}.js`,
          // assetFileNames: `assets/[name].${__VERSION__}[extname]`,
          // 通过html引入的css使用版本号，其他css使用 hash
          assetFileNames: (assetInfo) => {
            let str = `assets/[name].[hash:8][extname]`
            if (assetInfo.name == 'index.css' && !isFirst) {
              str = `assets/[name].${__VERSION__}[extname]`
              isFirst = true
            }
            return str
          }
        },
      },
    },
  }
})
