// vite.config.js
import path2 from "path";
import { defineConfig, loadEnv } from "file:///D:/code/talkx-plugin-web/node_modules/vite/dist/node/index.js";
import vue from "file:///D:/code/talkx-plugin-web/node_modules/@vitejs/plugin-vue/dist/index.mjs";

// plugins/modifyHtmlPlugin.js
import fs from "fs";
import path from "path";
import jsdom from "file:///D:/code/talkx-plugin-web/node_modules/jsdom/lib/api.js";
var __vite_injected_original_dirname = "D:\\code\\talkx-plugin-web\\plugins";
function modifyHtmlPlugin() {
  return {
    name: "modify-html-plugin",
    apply: "build",
    async closeBundle() {
      const { JSDOM } = jsdom;
      const __VERSION__ = process.env.npm_package_version;
      const htmlPath = path.resolve(__vite_injected_original_dirname, "../dist/index.html");
      const htmlContent = fs.readFileSync(htmlPath, "utf-8");
      const dom = new JSDOM(htmlContent);
      const head = dom.window.document.head;
      const v = Math.random();
      const bundleJSName = `bundle.${__VERSION__}.js`;
      const script = head.querySelector(`script[src="./assets/${bundleJSName}"]`);
      script.setAttribute("src", `./assets/${bundleJSName}?v=${v}`);
      const link = head.querySelector(`link[href="./assets/index.${__VERSION__}.css"]`);
      link.setAttribute("href", `./assets/index.${__VERSION__}.css?v=${v}`);
      fs.writeFileSync(htmlPath, dom.serialize(), "utf-8");
      const assets = path.join(__vite_injected_original_dirname, "../dist/assets");
      const res = fs.readdirSync(assets);
      const jsArr = res.filter((name) => /\.js$/.test(name) && name != bundleJSName);
      for (let i = 0; i < jsArr.length; i++) {
        const jsPath = path.join(assets, jsArr[i]);
        const jsFile = fs.readFileSync(jsPath, "utf-8");
        const str = jsFile.replaceAll(bundleJSName, `${bundleJSName}?v=${v}`);
        fs.writeFileSync(jsPath, str, "utf-8");
      }
    }
  };
}
var modifyHtmlPlugin_default = modifyHtmlPlugin;

// vite.config.js
var __vite_injected_original_dirname2 = "D:\\code\\talkx-plugin-web";
var vite_config_default = defineConfig(({ mode }) => {
  const env = loadEnv(mode, __vite_injected_original_dirname2);
  console.log("env", mode, env);
  const __VERSION__ = process.env.npm_package_version;
  const chunkStr = Number(Math.random().toString().substring(2, 6)).toString(36);
  let isFirst = false;
  return {
    define: {
      __VERSION__: JSON.stringify(__VERSION__)
    },
    plugins: [
      vue(),
      modifyHtmlPlugin_default()
      // legacy({
      //   targets: ['Chrome 63'],
      //   additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
      //   modernPolyfills: true,
      // }),
    ],
    resolve: {
      alias: {
        "@": path2.resolve(process.cwd(), "src")
      }
    },
    base: env.VITE_MODE_BASE_URL || "./",
    server: {
      host: true,
      //'0.0.0.0',
      port: 5174,
      proxy: {
        "/api": {
          target: env.VITE_MODE_API_URL,
          changeOrigin: true,
          rewrite: (path3) => path3.replace(/^\/api/, "")
        },
        "/_gpt": {
          target: env.VITE_MODE_API_URL,
          changeOrigin: true,
          rewrite: (path3) => path3.replace(/^\/_gpt/, "")
        }
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: '@import "@/assets/scss/global.scss"; '
        }
      }
    },
    build: {
      target: "es2015",
      reportCompressedSize: false,
      sourcemap: false,
      commonjsOptions: {
        ignoreTryCatch: false
      },
      rollupOptions: {
        output: {
          chunkFileNames: `assets/chunk.${chunkStr}.js`,
          entryFileNames: `assets/bundle.${__VERSION__}.js`,
          // assetFileNames: `assets/[name].${__VERSION__}[extname]`,
          // 通过html引入的css使用版本号，其他css使用 hash
          assetFileNames: (assetInfo) => {
            let str = `assets/[name].[hash:8][extname]`;
            if (assetInfo.name == "index.css" && !isFirst) {
              str = `assets/[name].${__VERSION__}[extname]`;
              isFirst = true;
            }
            return str;
          }
        }
      }
    }
  };
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiLCAicGx1Z2lucy9tb2RpZnlIdG1sUGx1Z2luLmpzIl0sCiAgInNvdXJjZXNDb250ZW50IjogWyJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiRDpcXFxcY29kZVxcXFx0YWxreC1wbHVnaW4td2ViXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJEOlxcXFxjb2RlXFxcXHRhbGt4LXBsdWdpbi13ZWJcXFxcdml0ZS5jb25maWcuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0Q6L2NvZGUvdGFsa3gtcGx1Z2luLXdlYi92aXRlLmNvbmZpZy5qc1wiO2ltcG9ydCBwYXRoIGZyb20gJ3BhdGgnXHJcbmltcG9ydCB7IGRlZmluZUNvbmZpZywgbG9hZEVudiB9IGZyb20gJ3ZpdGUnXHJcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJ1xyXG5pbXBvcnQgbW9kaWZ5SHRtbFBsdWdpbiBmcm9tICcuL3BsdWdpbnMvbW9kaWZ5SHRtbFBsdWdpbidcclxuLy8gaW1wb3J0IGxlZ2FjeSBmcm9tICdAdml0ZWpzL3BsdWdpbi1sZWdhY3knO1xyXG5cclxuLy8gaHR0cHM6Ly92aXRlanMuZGV2L2NvbmZpZy9cclxuZXhwb3J0IGRlZmF1bHQgZGVmaW5lQ29uZmlnKCh7IG1vZGUgfSkgPT4ge1xyXG4gIGNvbnN0IGVudiA9IGxvYWRFbnYobW9kZSwgX19kaXJuYW1lKVxyXG4gIGNvbnNvbGUubG9nKCdlbnYnLCBtb2RlLCBlbnYpO1xyXG4gIC8vIGVudi5OT0RFX0VOVl9ERVYgPSBwcm9jZXNzLmVudi5OT0RFX0VOVl9ERVZcclxuICBjb25zdCBfX1ZFUlNJT05fXyA9IHByb2Nlc3MuZW52Lm5wbV9wYWNrYWdlX3ZlcnNpb25cclxuICBjb25zdCBjaHVua1N0ciA9IE51bWJlcihNYXRoLnJhbmRvbSgpLnRvU3RyaW5nKCkuc3Vic3RyaW5nKDIsIDYpKS50b1N0cmluZygzNilcclxuICBsZXQgaXNGaXJzdCA9IGZhbHNlXHJcbiAgcmV0dXJuIHtcclxuICAgIGRlZmluZToge1xyXG4gICAgICBfX1ZFUlNJT05fXzogSlNPTi5zdHJpbmdpZnkoX19WRVJTSU9OX18pXHJcbiAgICB9LFxyXG4gICAgcGx1Z2luczogW1xyXG4gICAgICB2dWUoKSxcclxuICAgICAgbW9kaWZ5SHRtbFBsdWdpbigpLFxyXG4gICAgICAvLyBsZWdhY3koe1xyXG4gICAgICAvLyAgIHRhcmdldHM6IFsnQ2hyb21lIDYzJ10sXHJcbiAgICAgIC8vICAgYWRkaXRpb25hbExlZ2FjeVBvbHlmaWxsczogWydyZWdlbmVyYXRvci1ydW50aW1lL3J1bnRpbWUnXSxcclxuICAgICAgLy8gICBtb2Rlcm5Qb2x5ZmlsbHM6IHRydWUsXHJcbiAgICAgIC8vIH0pLFxyXG4gICAgXSxcclxuICAgIHJlc29sdmU6IHtcclxuICAgICAgYWxpYXM6IHtcclxuICAgICAgICAnQCc6IHBhdGgucmVzb2x2ZShwcm9jZXNzLmN3ZCgpLCAnc3JjJyksXHJcbiAgICAgIH0sXHJcbiAgICB9LFxyXG4gICAgYmFzZTogZW52LlZJVEVfTU9ERV9CQVNFX1VSTCB8fCAnLi8nLFxyXG4gICAgc2VydmVyOiB7XHJcbiAgICAgIGhvc3Q6IHRydWUsIC8vJzAuMC4wLjAnLFxyXG4gICAgICBwb3J0OiAgNTE3NCxcclxuICAgICAgcHJveHk6IHtcclxuICAgICAgICAnL2FwaSc6IHtcclxuICAgICAgICAgIHRhcmdldDogZW52LlZJVEVfTU9ERV9BUElfVVJMLFxyXG4gICAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLFxyXG4gICAgICAgICAgcmV3cml0ZTogKHBhdGgpID0+IHBhdGgucmVwbGFjZSgvXlxcL2FwaS8sICcnKVxyXG4gICAgICAgIH0sXHJcbiAgICAgICAgJy9fZ3B0Jzoge1xyXG4gICAgICAgICAgdGFyZ2V0OiBlbnYuVklURV9NT0RFX0FQSV9VUkwsXHJcbiAgICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWUsXHJcbiAgICAgICAgICByZXdyaXRlOiAocGF0aCkgPT4gcGF0aC5yZXBsYWNlKC9eXFwvX2dwdC8sICcnKVxyXG4gICAgICAgIH1cclxuICAgICAgfVxyXG4gICAgfSxcclxuICAgIGNzczp7XHJcbiAgICAgIHByZXByb2Nlc3Nvck9wdGlvbnM6IHtcclxuICAgICAgICBzY3NzOiB7XHJcbiAgICAgICAgICBhZGRpdGlvbmFsRGF0YTogJ0BpbXBvcnQgXCJAL2Fzc2V0cy9zY3NzL2dsb2JhbC5zY3NzXCI7ICdcclxuICAgICAgICB9XHJcbiAgICAgIH1cclxuICAgIH0sXHJcbiAgICBidWlsZDoge1xyXG4gICAgICB0YXJnZXQ6ICdlczIwMTUnLFxyXG4gICAgICByZXBvcnRDb21wcmVzc2VkU2l6ZTogZmFsc2UsXHJcbiAgICAgIHNvdXJjZW1hcDogZmFsc2UsXHJcbiAgICAgIGNvbW1vbmpzT3B0aW9uczoge1xyXG4gICAgICAgIGlnbm9yZVRyeUNhdGNoOiBmYWxzZSxcclxuICAgICAgfSxcclxuICAgICAgcm9sbHVwT3B0aW9uczoge1xyXG4gICAgICAgIG91dHB1dDoge1xyXG4gICAgICAgICAgY2h1bmtGaWxlTmFtZXM6IGBhc3NldHMvY2h1bmsuJHtjaHVua1N0cn0uanNgLFxyXG4gICAgICAgICAgZW50cnlGaWxlTmFtZXM6IGBhc3NldHMvYnVuZGxlLiR7X19WRVJTSU9OX199LmpzYCxcclxuICAgICAgICAgIC8vIGFzc2V0RmlsZU5hbWVzOiBgYXNzZXRzL1tuYW1lXS4ke19fVkVSU0lPTl9ffVtleHRuYW1lXWAsXHJcbiAgICAgICAgICAvLyBcdTkwMUFcdThGQzdodG1sXHU1RjE1XHU1MTY1XHU3Njg0Y3NzXHU0RjdGXHU3NTI4XHU3MjQ4XHU2NzJDXHU1M0Y3XHVGRjBDXHU1MTc2XHU0RUQ2Y3NzXHU0RjdGXHU3NTI4IGhhc2hcclxuICAgICAgICAgIGFzc2V0RmlsZU5hbWVzOiAoYXNzZXRJbmZvKSA9PiB7XHJcbiAgICAgICAgICAgIGxldCBzdHIgPSBgYXNzZXRzL1tuYW1lXS5baGFzaDo4XVtleHRuYW1lXWBcclxuICAgICAgICAgICAgaWYgKGFzc2V0SW5mby5uYW1lID09ICdpbmRleC5jc3MnICYmICFpc0ZpcnN0KSB7XHJcbiAgICAgICAgICAgICAgc3RyID0gYGFzc2V0cy9bbmFtZV0uJHtfX1ZFUlNJT05fX31bZXh0bmFtZV1gXHJcbiAgICAgICAgICAgICAgaXNGaXJzdCA9IHRydWVcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICByZXR1cm4gc3RyXHJcbiAgICAgICAgICB9XHJcbiAgICAgICAgfSxcclxuICAgICAgfSxcclxuICAgIH0sXHJcbiAgfVxyXG59KVxyXG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkQ6XFxcXGNvZGVcXFxcdGFsa3gtcGx1Z2luLXdlYlxcXFxwbHVnaW5zXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJEOlxcXFxjb2RlXFxcXHRhbGt4LXBsdWdpbi13ZWJcXFxccGx1Z2luc1xcXFxtb2RpZnlIdG1sUGx1Z2luLmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9EOi9jb2RlL3RhbGt4LXBsdWdpbi13ZWIvcGx1Z2lucy9tb2RpZnlIdG1sUGx1Z2luLmpzXCI7XHJcbmltcG9ydCBmcyBmcm9tICdmcydcclxuaW1wb3J0IHBhdGggZnJvbSAncGF0aCdcclxuaW1wb3J0IGpzZG9tIGZyb20gJ2pzZG9tJ1xyXG5cclxuZnVuY3Rpb24gbW9kaWZ5SHRtbFBsdWdpbigpIHtcclxuICAgIHJldHVybiB7XHJcbiAgICAgICAgbmFtZTogJ21vZGlmeS1odG1sLXBsdWdpbicsXHJcbiAgICAgICAgYXBwbHk6ICdidWlsZCcsXHJcbiAgICAgICAgYXN5bmMgY2xvc2VCdW5kbGUoKSB7XHJcbiAgICAgICAgICAgIGNvbnN0IHsgSlNET00gfSA9IGpzZG9tXHJcbiAgICAgICAgICAgIGNvbnN0IF9fVkVSU0lPTl9fID0gcHJvY2Vzcy5lbnYubnBtX3BhY2thZ2VfdmVyc2lvblxyXG4gICAgICAgICAgICBjb25zdCBodG1sUGF0aCA9IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICcuLi9kaXN0L2luZGV4Lmh0bWwnKTsgLy8gSFRNTFx1NjU4N1x1NEVGNlx1NzY4NFx1OERFRlx1NUY4NFxyXG4gICAgICAgICAgICBjb25zdCBodG1sQ29udGVudCA9IGZzLnJlYWRGaWxlU3luYyhodG1sUGF0aCwgJ3V0Zi04Jyk7IC8vIFx1OEJGQlx1NTNENkhUTUxcdTY1ODdcdTRFRjZcdTUxODVcdTVCQjkgXHJcbiAgICAgICAgICAgIGNvbnN0IGRvbSA9IG5ldyBKU0RPTShodG1sQ29udGVudClcclxuICAgICAgICAgICAgY29uc3QgaGVhZCA9IGRvbS53aW5kb3cuZG9jdW1lbnQuaGVhZDtcclxuICAgICAgICAgICAgY29uc3QgdiA9IE1hdGgucmFuZG9tKClcclxuICAgICAgICAgICAgY29uc3QgYnVuZGxlSlNOYW1lID0gYGJ1bmRsZS4ke19fVkVSU0lPTl9ffS5qc2BcclxuICAgICAgICAgICAgY29uc3Qgc2NyaXB0ID0gaGVhZC5xdWVyeVNlbGVjdG9yKGBzY3JpcHRbc3JjPVwiLi9hc3NldHMvJHtidW5kbGVKU05hbWV9XCJdYClcclxuICAgICAgICAgICAgc2NyaXB0LnNldEF0dHJpYnV0ZSgnc3JjJywgYC4vYXNzZXRzLyR7YnVuZGxlSlNOYW1lfT92PSR7dn1gKVxyXG4gICAgICAgICAgICBjb25zdCBsaW5rID0gaGVhZC5xdWVyeVNlbGVjdG9yKGBsaW5rW2hyZWY9XCIuL2Fzc2V0cy9pbmRleC4ke19fVkVSU0lPTl9ffS5jc3NcIl1gKVxyXG4gICAgICAgICAgICBsaW5rLnNldEF0dHJpYnV0ZSgnaHJlZicsIGAuL2Fzc2V0cy9pbmRleC4ke19fVkVSU0lPTl9ffS5jc3M/dj0ke3Z9YClcclxuICAgICAgICAgICAgZnMud3JpdGVGaWxlU3luYyhodG1sUGF0aCwgZG9tLnNlcmlhbGl6ZSgpLCAndXRmLTgnKTsgLy8gXHU1QzA2XHU0RkVFXHU2NTM5XHU1NDBFXHU3Njg0XHU1MTg1XHU1QkI5XHU1MTk5XHU1MTY1SFRNTFx1NjU4N1x1NEVGNlxyXG5cclxuICAgICAgICAgICAgLy8gXHU1QkY5XHU1RTk0XHU1MTc2XHU0RUQ2Y2h1bmtqc1x1NEUyRFx1NUYxNVx1NzUyOFx1NzY4NCBidW5kbGUueHguanMgXHU1MkEwXHU0RTBBdlxyXG5cclxuICAgICAgICAgICAgY29uc3QgYXNzZXRzID0gcGF0aC5qb2luKF9fZGlybmFtZSwgJy4uL2Rpc3QvYXNzZXRzJylcclxuICAgICAgICAgICAgY29uc3QgcmVzID0gZnMucmVhZGRpclN5bmMoYXNzZXRzKVxyXG4gICAgICAgICAgICBjb25zdCBqc0FyciA9IHJlcy5maWx0ZXIobmFtZSA9PiAvXFwuanMkLy50ZXN0KG5hbWUpICYmIG5hbWUgIT0gYnVuZGxlSlNOYW1lKVxyXG4gICAgICAgICAgICBmb3IgKGxldCBpID0gMDsgaSA8IGpzQXJyLmxlbmd0aDsgaSsrKSB7XHJcbiAgICAgICAgICAgICAgICBjb25zdCBqc1BhdGggPSBwYXRoLmpvaW4oYXNzZXRzLCBqc0FycltpXSlcclxuICAgICAgICAgICAgICAgIGNvbnN0IGpzRmlsZSA9IGZzLnJlYWRGaWxlU3luYyhqc1BhdGgsICd1dGYtOCcpO1xyXG4gICAgICAgICAgICAgICAgY29uc3Qgc3RyID0ganNGaWxlLnJlcGxhY2VBbGwoYnVuZGxlSlNOYW1lLCBgJHtidW5kbGVKU05hbWV9P3Y9JHt2fWApXHJcbiAgICAgICAgICAgICAgICBmcy53cml0ZUZpbGVTeW5jKGpzUGF0aCwgc3RyLCAndXRmLTgnKTsgLy8gXHU1QzA2XHU0RkVFXHU2NTM5XHU1NDBFXHU3Njg0XHU1MTg1XHU1QkI5XHU1MTk5XHU1MTY1SFRNTFx1NjU4N1x1NEVGNlxyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgfTtcclxufVxyXG5c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USxJQUFJO0FBQUEsVUFDWixjQUFjO0FBQUEsVUFDZCxTQUFTLENBQUNBLFVBQVNBLE1BQUssUUFBUSxXQUFXLEVBQUU7QUFBQSxRQUMvQztBQUFBLE1BQ0Y7QUFBQSxJQUNGO0FBQUEsSUFDQSxLQUFJO0FBQUEsTUFDRixxQkFBcUI7QUFBQSxRQUNuQixNQUFNO0FBQUEsVUFDSixnQkFBZ0I7QUFBQSxRQUNsQjtBQUFBLE1BQ0Y7QUFBQSxJQUNGO0FBQUEsSUFDQSxPQUFPO0FBQUEsTUFDTCxRQUFRO0FBQUEsTUFDUixzQkFBc0I7QUFBQSxNQUN0QixXQUFXO0FBQUEsTUFDWCxpQkFBaUI7QUFBQSxRQUNmLGdCQUFnQjtBQUFBLE1BQ2xCO0FBQUEsTUFDQSxlQUFlO0FBQUEsUUFDYixRQUFRO0FBQUEsVUFDTixnQkFBZ0IsZ0JBQWdCO0FBQUEsVUFDaEMsZ0JBQWdCLGlCQUFpQjtBQUFBO0FBQUE7QUFBQSxVQUdqQyxnQkFBZ0IsQ0FBQyxjQUFjO0FBQzdCLGdCQUFJLE1BQU07QUFDVixnQkFBSSxVQUFVLFFBQVEsZUFBZSxDQUFDLFNBQVM7QUFDN0Msb0JBQU0saUJBQWlCO0FBQ3ZCLHdCQUFVO0FBQUEsWUFDWjtBQUNBLG1CQUFPO0FBQUEsVUFDVDtBQUFBLFFBQ0Y7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFDRixDQUFDOyIsCiAgIm5hbWVzIjogWyJwYXRoIiwgIl9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lIiwgIl9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lIiwgInBhdGgiXQp9Cg==
