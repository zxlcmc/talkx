var Ee=Object.defineProperty,Me=Object.defineProperties;var He=Object.getOwnPropertyDescriptors;var ke=Object.getOwnPropertySymbols;var qe=Object.prototype.hasOwnProperty,Le=Object.prototype.propertyIsEnumerable;var Ce=(e,n,r)=>n in e?Ee(e,n,{enumerable:!0,configurable:!0,writable:!0,value:r}):e[n]=r,be=(e,n)=>{for(var r in n||(n={}))qe.call(n,r)&&Ce(e,r,n[r]);if(ke)for(var r of ke(n))Le.call(n,r)&&Ce(e,r,n[r]);return e},ve=(e,n)=>Me(e,He(n));var ae=(e,n,r)=>new Promise((o,i)=>{var a=v=>{try{p(r.next(v))}catch(w){i(w)}},b=v=>{try{p(r.throw(v))}catch(w){i(w)}},p=v=>v.done?o(v.value):Promise.resolve(v.value).then(a,b);p((r=r.apply(e,n)).next())});import{aw as cB,ax as c,ay as cE,ao as defineComponent,az as useConfig,aA as useStyle,aB as h,aC as createTheme,aD as commonLight,aE as buttonLight,aF as toHexaString,aG as rgba,aH as toHslaString,aI as rgb2hsl,aJ as toHsvaString,aK as rgb2hsv,aL as toRgbaString,aM as hsla,aN as hsl2rgb,aO as hsl2hsv,aP as hsva,aQ as hsv2rgb,aR as hsv2hsl,g as ref,c as computed,aS as on,aT as off,aU as createInjectionKey,i as inject,W as watchEffect,aV as toHexString,aW as toHslString,aX as toRgbString,aY as toHsvString,aZ as warn,a_ as fadeInScaleUpTransition,a$ as cM,b0 as useFormItem,b1 as useTheme,b2 as provide,b3 as toRef,b4 as useMergedState,a4 as watch,b5 as createKey,b6 as useThemeClass,b7 as isMounted,b8 as getPreciseEventTarget,b9 as Transition,Y as withDirectives,ba as clickoutside,bb as call,ae as NButton,G as nextTick,bc as insideModal,bd as insidePopover,be as onBeforeUpdate,bf as onBeforeUnmount,bg as resolveSlot,y as default_friend_avatar,bh as isInteger,_ as _export_sfc,o as openBlock,b as createBlock,D as withCtx,e as createBaseVNode,A as toDisplayString,v as unref,k as useMessage,q as onMounted,f as createVNode,z as createTextVNode,a as createElementBlock,x as renderList,F as Fragment,d as createCommentVNode,bi as mergeProps,bj as systemPrompt_url,bk as debance,C as normalizeStyle,w as normalizeClass,Z as vShow,bl as base64ToFile,bm as uploadImg,h as useRoute,j as useRouter,l as useChatStore,a2 as useGlobalStore,n as getCurrentInstance,t as onUnmounted,ah as routerBack,bn as updateFriend,bo as createFriend,bp as queryFriend}from"./bundle.0.0.2.js?v=0.6036900755232442";import{u as useAdjustedTo,V as VBinder,a as VTarget,b as VFollower,N as NPopover}from"./chunk.4zk23.js";import{N as NFormItem,a as NSelect,b as NForm,c as NTabs,d as NTabPane}from"./chunk.4zk49.js";import{i as inputLight,N as NInput}from"./chunk.4zk31.js";import{U as Upload}from"./chunk.4zk29.js";import{u as useLocale}from"./chunk.4zk39.js";import"./chunk.4zk50.js";import"./chunk.4zk34.js";import"./chunk.4zk27.js";const style$2=cB("input-group",`
 display: inline-flex;
 width: 100%;
 flex-wrap: nowrap;
 vertical-align: bottom;
`,[c(">",[cB("input",[c("&:not(:last-child)",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `),c("&:not(:first-child)",`
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 margin-left: -1px!important;
 `)]),cB("button",[c("&:not(:last-child)",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `,[cE("state-border, border",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `)]),c("&:not(:first-child)",`
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 `,[cE("state-border, border",`
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 `)])]),c("*",[c("&:not(:last-child)",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `,[c(">",[cB("input",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `),cB("base-selection",[cB("base-selection-label",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `),cB("base-selection-tags",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `),cE("box-shadow, border, state-border",`
 border-top-right-radius: 0!important;
 border-bottom-right-radius: 0!important;
 `)])])]),c("&:not(:first-child)",`
 margin-left: -1px!important;
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 `,[c(">",[cB("input",`
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 `),cB("base-selection",[cB("base-selection-label",`
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 `),cB("base-selection-tags",`
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 `),cE("box-shadow, border, state-border",`
 border-top-left-radius: 0!important;
 border-bottom-left-radius: 0!important;
 `)])])])])])]),inputGroupProps={},NInputGroup=defineComponent({name:"InputGroup",props:inputGroupProps,setup(e){const{mergedClsPrefixRef:n}=useConfig(e);return useStyle("-input-group",style$2,n),{mergedClsPrefix:n}},render(){const{mergedClsPrefix:e}=this;return h("div",{class:`${e}-input-group`},this.$slots)}});function self$1(e){const{fontSize:n,boxShadow2:r,popoverColor:o,textColor2:i,borderRadius:a,borderColor:b,heightSmall:p,heightMedium:v,heightLarge:w,fontSizeSmall:B,fontSizeMedium:V,fontSizeLarge:R,dividerColor:P}=e;return{panelFontSize:n,boxShadow:r,color:o,textColor:i,borderRadius:a,border:`1px solid ${b}`,heightSmall:p,heightMedium:v,heightLarge:w,fontSizeSmall:B,fontSizeMedium:V,fontSizeLarge:R,dividerColor:P}}const colorPickerLight=createTheme({name:"ColorPicker",common:commonLight,peers:{Input:inputLight,Button:buttonLight},self:self$1}),colorPickerLight$1=colorPickerLight;function deriveDefaultValue(e,n){switch(e[0]){case"hex":return n?"#000000FF":"#000000";case"rgb":return n?"rgba(0, 0, 0, 1)":"rgb(0, 0, 0)";case"hsl":return n?"hsla(0, 0%, 0%, 1)":"hsl(0, 0%, 0%)";case"hsv":return n?"hsva(0, 0%, 0%, 1)":"hsv(0, 0%, 0%)"}return"#000000"}function getModeFromValue(e){return e===null?null:/^ *#/.test(e)?"hex":e.includes("rgb")?"rgb":e.includes("hsl")?"hsl":e.includes("hsv")?"hsv":null}function normalizeHue(e){return e=Math.round(e),e>=360?359:e<0?0:e}function normalizeAlpha(e){return e=Math.round(e*100)/100,e>1?1:e<0?0:e}const convert={rgb:{hex(e){return toHexaString(rgba(e))},hsl(e){const[n,r,o,i]=rgba(e);return toHslaString([...rgb2hsl(n,r,o),i])},hsv(e){const[n,r,o,i]=rgba(e);return toHsvaString([...rgb2hsv(n,r,o),i])}},hex:{rgb(e){return toRgbaString(rgba(e))},hsl(e){const[n,r,o,i]=rgba(e);return toHslaString([...rgb2hsl(n,r,o),i])},hsv(e){const[n,r,o,i]=rgba(e);return toHsvaString([...rgb2hsv(n,r,o),i])}},hsl:{hex(e){const[n,r,o,i]=hsla(e);return toHexaString([...hsl2rgb(n,r,o),i])},rgb(e){const[n,r,o,i]=hsla(e);return toRgbaString([...hsl2rgb(n,r,o),i])},hsv(e){const[n,r,o,i]=hsla(e);return toHsvaString([...hsl2hsv(n,r,o),i])}},hsv:{hex(e){const[n,r,o,i]=hsva(e);return toHexaString([...hsv2rgb(n,r,o),i])},rgb(e){const[n,r,o,i]=hsva(e);return toRgbaString([...hsv2rgb(n,r,o),i])},hsl(e){const[n,r,o,i]=hsva(e);return toHslaString([...hsv2hsl(n,r,o),i])}}};function convertColor(e,n,r){return r=r||getModeFromValue(e),r?r===n?e:convert[r][n](e):null}const HANDLE_SIZE$2="12px",HANDLE_SIZE_NUM$1=12,RADIUS$2="6px",AlphaSlider=defineComponent({name:"AlphaSlider",props:{clsPrefix:{type:String,required:!0},rgba:{type:Array,default:null},alpha:{type:Number,default:0},onUpdateAlpha:{type:Function,required:!0},onComplete:Function},setup(e){const n=ref(null);function r(a){!n.value||!e.rgba||(on("mousemove",document,o),on("mouseup",document,i),o(a))}function o(a){const{value:b}=n;if(!b)return;const{width:p,left:v}=b.getBoundingClientRect(),w=(a.clientX-v)/(p-HANDLE_SIZE_NUM$1);e.onUpdateAlpha(normalizeAlpha(w))}function i(){var a;off("mousemove",document,o),off("mouseup",document,i),(a=e.onComplete)===null||a===void 0||a.call(e)}return{railRef:n,railBackgroundImage:computed(()=>{const{rgba:a}=e;return a?`linear-gradient(to right, rgba(${a[0]}, ${a[1]}, ${a[2]}, 0) 0%, rgba(${a[0]}, ${a[1]}, ${a[2]}, 1) 100%)`:""}),handleMouseDown:r}},render(){const{clsPrefix:e}=this;return h("div",{class:`${e}-color-picker-slider`,ref:"railRef",style:{height:HANDLE_SIZE$2,borderRadius:RADIUS$2},onMousedown:this.handleMouseDown},h("div",{style:{borderRadius:RADIUS$2,position:"absolute",left:0,right:0,top:0,bottom:0,overflow:"hidden"}},h("div",{class:`${e}-color-picker-checkboard`}),h("div",{class:`${e}-color-picker-slider__image`,style:{backgroundImage:this.railBackgroundImage}})),this.rgba&&h("div",{style:{position:"absolute",left:RADIUS$2,right:RADIUS$2,top:0,bottom:0}},h("div",{class:`${e}-color-picker-handle`,style:{left:`calc(${this.alpha*100}% - ${RADIUS$2})`,borderRadius:RADIUS$2,width:HANDLE_SIZE$2,height:HANDLE_SIZE$2}},h("div",{class:`${e}-color-picker-handle__fill`,style:{backgroundColor:toRgbaString(this.rgba),borderRadius:RADIUS$2,width:HANDLE_SIZE$2,height:HANDLE_SIZE$2}}))))}}),colorPickerInjectionKey=createInjectionKey("n-color-picker");function normalizeRgbUnit(e){return/^\d{1,3}\.?\d*$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e),255)):!1}function normalizeHueUnit(e){return/^\d{1,3}\.?\d*$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e),360)):!1}function normalizeSlvUnit(e){return/^\d{1,3}\.?\d*$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e),100)):!1}function normalizeHexaUnit(e){const n=e.trim();return/^#[0-9a-fA-F]+$/.test(n)?[4,5,7,9].includes(n.length):!1}function normalizeAlphaUnit(e){return/^\d{1,3}\.?\d*%$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e)/100,100)):!1}const inputThemeOverrides={paddingSmall:"0 4px"},ColorInputUnit=defineComponent({name:"ColorInputUnit",props:{label:{type:String,required:!0},value:{type:[Number,String],default:null},showAlpha:Boolean,onUpdateValue:{type:Function,required:!0}},setup(e){const n=ref(""),{themeRef:r}=inject(colorPickerInjectionKey,null);watchEffect(()=>{n.value=o()});function o(){const{value:b}=e;if(b===null)return"";const{label:p}=e;return p==="HEX"?b:p==="A"?`${Math.floor(b*100)}%`:String(Math.floor(b))}function i(b){n.value=b}function a(b){let p,v;switch(e.label){case"HEX":v=normalizeHexaUnit(b),v&&e.onUpdateValue(b),n.value=o();break;case"H":p=normalizeHueUnit(b),p===!1?n.value=o():e.onUpdateValue(p);break;case"S":case"L":case"V":p=normalizeSlvUnit(b),p===!1?n.value=o():e.onUpdateValue(p);break;case"A":p=normalizeAlphaUnit(b),p===!1?n.value=o():e.onUpdateValue(p);break;case"R":case"G":case"B":p=normalizeRgbUnit(b),p===!1?n.value=o():e.onUpdateValue(p);break}}return{mergedTheme:r,inputValue:n,handleInputChange:a,handleInputUpdateValue:i}},render(){const{mergedTheme:e}=this;return h(NInput,{size:"small",placeholder:this.label,theme:e.peers.Input,themeOverrides:e.peerOverrides.Input,builtinThemeOverrides:inputThemeOverrides,value:this.inputValue,onUpdateValue:this.handleInputUpdateValue,onChange:this.handleInputChange,style:this.label==="A"?"flex-grow: 1.25;":""})}}),ColorInput=defineComponent({name:"ColorInput",props:{clsPrefix:{type:String,required:!0},mode:{type:String,required:!0},modes:{type:Array,required:!0},showAlpha:{type:Boolean,required:!0},value:{type:String,default:null},valueArr:{type:Array,default:null},onUpdateValue:{type:Function,required:!0},onUpdateMode:{type:Function,required:!0}},setup(e){return{handleUnitUpdateValue(n,r){const{showAlpha:o}=e;if(e.mode==="hex"){e.onUpdateValue((o?toHexaString:toHexString)(r));return}let i;switch(e.valueArr===null?i=[0,0,0,0]:i=Array.from(e.valueArr),e.mode){case"hsv":i[n]=r,e.onUpdateValue((o?toHsvaString:toHsvString)(i));break;case"rgb":i[n]=r,e.onUpdateValue((o?toRgbaString:toRgbString)(i));break;case"hsl":i[n]=r,e.onUpdateValue((o?toHslaString:toHslString)(i));break}}}},render(){const{clsPrefix:e,modes:n}=this;return h("div",{class:`${e}-color-picker-input`},h("div",{class:`${e}-color-picker-input__mode`,onClick:this.onUpdateMode,style:{cursor:n.length===1?"":"pointer"}},this.mode.toUpperCase()+(this.showAlpha?"A":"")),h(NInputGroup,null,{default:()=>{const{mode:r,valueArr:o,showAlpha:i}=this;if(r==="hex"){let a=null;try{a=o===null?null:(i?toHexaString:toHexString)(o)}catch(b){}return h(ColorInputUnit,{label:"HEX",showAlpha:i,value:a,onUpdateValue:b=>{this.handleUnitUpdateValue(0,b)}})}return(r+(i?"a":"")).split("").map((a,b)=>h(ColorInputUnit,{label:a.toUpperCase(),value:o===null?null:o[b],onUpdateValue:p=>{this.handleUnitUpdateValue(b,p)}}))}}))}});function normalizeColor(e,n){if(n==="hsv"){const[r,o,i,a]=hsva(e);return toRgbaString([...hsv2rgb(r,o,i),a])}return e}function getHexFromName(e){const n=document.createElement("canvas").getContext("2d");return n?(n.fillStyle=e,n.fillStyle):"#000000"}const ColorPickerSwatches=defineComponent({name:"ColorPickerSwatches",props:{clsPrefix:{type:String,required:!0},mode:{type:String,required:!0},swatches:{type:Array,required:!0},onUpdateColor:{type:Function,required:!0}},setup(e){const n=computed(()=>e.swatches.map(a=>{const b=getModeFromValue(a);return{value:a,mode:b,legalValue:normalizeColor(a,b)}}));function r(a){const{mode:b}=e;let{value:p,mode:v}=a;return v||(v="hex",/^[a-zA-Z]+$/.test(p)?p=getHexFromName(p):(warn("color-picker",`color ${p} in swatches is invalid.`),p="#000000")),v===b?p:convertColor(p,b,v)}function o(a){e.onUpdateColor(r(a))}function i(a,b){a.key==="Enter"&&o(b)}return{parsedSwatchesRef:n,handleSwatchSelect:o,handleSwatchKeyDown:i}},render(){const{clsPrefix:e}=this;return h("div",{class:`${e}-color-picker-swatches`},this.parsedSwatchesRef.map(n=>h("div",{class:`${e}-color-picker-swatch`,tabindex:0,onClick:()=>{this.handleSwatchSelect(n)},onKeydown:r=>{this.handleSwatchKeyDown(r,n)}},h("div",{class:`${e}-color-picker-swatch__fill`,style:{background:n.legalValue}}))))}}),ColorPickerTrigger=defineComponent({name:"ColorPickerTrigger",slots:Object,props:{clsPrefix:{type:String,required:!0},value:{type:String,default:null},hsla:{type:Array,default:null},disabled:Boolean,onClick:Function},setup(e){const{colorPickerSlots:n,renderLabelRef:r}=inject(colorPickerInjectionKey,null);return()=>{const{hsla:o,value:i,clsPrefix:a,onClick:b,disabled:p}=e,v=n.label||r.value;return h("div",{class:[`${a}-color-picker-trigger`,p&&`${a}-color-picker-trigger--disabled`],onClick:p?void 0:b},h("div",{class:`${a}-color-picker-trigger__fill`},h("div",{class:`${a}-color-picker-checkboard`}),h("div",{style:{position:"absolute",left:0,right:0,top:0,bottom:0,backgroundColor:o?toHslaString(o):""}}),i&&o?h("div",{class:`${a}-color-picker-trigger__value`,style:{color:o[2]>50||o[3]<.5?"black":"white"}},v?v(i):i):null))}}}),ColorPreview=defineComponent({name:"ColorPreview",props:{clsPrefix:{type:String,required:!0},mode:{type:String,required:!0},color:{type:String,default:null,validator:e=>{const n=getModeFromValue(e);return!!(!e||n&&n!=="hsv")}},onUpdateColor:{type:Function,required:!0}},setup(e){function n(r){var o;const i=r.target.value;(o=e.onUpdateColor)===null||o===void 0||o.call(e,convertColor(i.toUpperCase(),e.mode,"hex")),r.stopPropagation()}return{handleChange:n}},render(){const{clsPrefix:e}=this;return h("div",{class:`${e}-color-picker-preview__preview`},h("span",{class:`${e}-color-picker-preview__fill`,style:{background:this.color||"#000000"}}),h("input",{class:`${e}-color-picker-preview__input`,type:"color",value:this.color,onChange:this.handleChange}))}}),HANDLE_SIZE$1="12px",HANDLE_SIZE_NUM=12,RADIUS$1="6px",RADIUS_NUM=6,GRADIENT="linear-gradient(90deg,red,#ff0 16.66%,#0f0 33.33%,#0ff 50%,#00f 66.66%,#f0f 83.33%,red)",HueSlider=defineComponent({name:"HueSlider",props:{clsPrefix:{type:String,required:!0},hue:{type:Number,required:!0},onUpdateHue:{type:Function,required:!0},onComplete:Function},setup(e){const n=ref(null);function r(a){n.value&&(on("mousemove",document,o),on("mouseup",document,i),o(a))}function o(a){const{value:b}=n;if(!b)return;const{width:p,left:v}=b.getBoundingClientRect(),w=normalizeHue((a.clientX-v-RADIUS_NUM)/(p-HANDLE_SIZE_NUM)*360);e.onUpdateHue(w)}function i(){var a;off("mousemove",document,o),off("mouseup",document,i),(a=e.onComplete)===null||a===void 0||a.call(e)}return{railRef:n,handleMouseDown:r}},render(){const{clsPrefix:e}=this;return h("div",{class:`${e}-color-picker-slider`,style:{height:HANDLE_SIZE$1,borderRadius:RADIUS$1}},h("div",{ref:"railRef",style:{boxShadow:"inset 0 0 2px 0 rgba(0, 0, 0, .24)",boxSizing:"border-box",backgroundImage:GRADIENT,height:HANDLE_SIZE$1,borderRadius:RADIUS$1,position:"relative"},onMousedown:this.handleMouseDown},h("div",{style:{position:"absolute",left:RADIUS$1,right:RADIUS$1,top:0,bottom:0}},h("div",{class:`${e}-color-picker-handle`,style:{left:`calc((${this.hue}%) / 359 * 100 - ${RADIUS$1})`,borderRadius:RADIUS$1,width:HANDLE_SIZE$1,height:HANDLE_SIZE$1}},h("div",{class:`${e}-color-picker-handle__fill`,style:{backgroundColor:`hsl(${this.hue}, 100%, 50%)`,borderRadius:RADIUS$1,width:HANDLE_SIZE$1,height:HANDLE_SIZE$1}})))))}}),HANDLE_SIZE="12px",RADIUS="6px",Pallete=defineComponent({name:"Pallete",props:{clsPrefix:{type:String,required:!0},rgba:{type:Array,default:null},displayedHue:{type:Number,required:!0},displayedSv:{type:Array,required:!0},onUpdateSV:{type:Function,required:!0},onComplete:Function},setup(e){const n=ref(null);function r(a){n.value&&(on("mousemove",document,o),on("mouseup",document,i),o(a))}function o(a){const{value:b}=n;if(!b)return;const{width:p,height:v,left:w,bottom:B}=b.getBoundingClientRect(),V=(B-a.clientY)/v,R=(a.clientX-w)/p,P=100*(R>1?1:R<0?0:R),F=100*(V>1?1:V<0?0:V);e.onUpdateSV(P,F)}function i(){var a;off("mousemove",document,o),off("mouseup",document,i),(a=e.onComplete)===null||a===void 0||a.call(e)}return{palleteRef:n,handleColor:computed(()=>{const{rgba:a}=e;return a?`rgb(${a[0]}, ${a[1]}, ${a[2]})`:""}),handleMouseDown:r}},render(){const{clsPrefix:e}=this;return h("div",{class:`${e}-color-picker-pallete`,onMousedown:this.handleMouseDown,ref:"palleteRef"},h("div",{class:`${e}-color-picker-pallete__layer`,style:{backgroundImage:`linear-gradient(90deg, white, hsl(${this.displayedHue}, 100%, 50%))`}}),h("div",{class:`${e}-color-picker-pallete__layer ${e}-color-picker-pallete__layer--shadowed`,style:{backgroundImage:"linear-gradient(180deg, rgba(0, 0, 0, 0%), rgba(0, 0, 0, 100%))"}}),this.rgba&&h("div",{class:`${e}-color-picker-handle`,style:{width:HANDLE_SIZE,height:HANDLE_SIZE,borderRadius:RADIUS,left:`calc(${this.displayedSv[0]}% - ${RADIUS})`,bottom:`calc(${this.displayedSv[1]}% - ${RADIUS})`}},h("div",{class:`${e}-color-picker-handle__fill`,style:{backgroundColor:this.handleColor,borderRadius:RADIUS,width:HANDLE_SIZE,height:HANDLE_SIZE}})))}}),style$1=c([cB("color-picker",`
 display: inline-block;
 box-sizing: border-box;
 height: var(--n-height);
 font-size: var(--n-font-size);
 width: 100%;
 position: relative;
 `),cB("color-picker-panel",`
 margin: 4px 0;
 width: 240px;
 font-size: var(--n-panel-font-size);
 color: var(--n-text-color);
 background-color: var(--n-color);
 transition:
 box-shadow .3s var(--n-bezier),
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 border-radius: var(--n-border-radius);
 box-shadow: var(--n-box-shadow);
 `,[fadeInScaleUpTransition(),cB("input",`
 text-align: center;
 `)]),cB("color-picker-checkboard",`
 background: white; 
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `,[c("&::after",`
 background-image: linear-gradient(45deg, #DDD 25%, #0000 25%), linear-gradient(-45deg, #DDD 25%, #0000 25%), linear-gradient(45deg, #0000 75%, #DDD 75%), linear-gradient(-45deg, #0000 75%, #DDD 75%);
 background-size: 12px 12px;
 background-position: 0 0, 0 6px, 6px -6px, -6px 0px;
 background-repeat: repeat;
 content: "";
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `)]),cB("color-picker-slider",`
 margin-bottom: 8px;
 position: relative;
 box-sizing: border-box;
 `,[cE("image",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `),c("&::after",`
 content: "";
 position: absolute;
 border-radius: inherit;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 box-shadow: inset 0 0 2px 0 rgba(0, 0, 0, .24);
 pointer-events: none;
 `)]),cB("color-picker-handle",`
 z-index: 1;
 box-shadow: 0 0 2px 0 rgba(0, 0, 0, .45);
 position: absolute;
 background-color: white;
 overflow: hidden;
 `,[cE("fill",`
 box-sizing: border-box;
 border: 2px solid white;
 `)]),cB("color-picker-pallete",`
 height: 180px;
 position: relative;
 margin-bottom: 8px;
 cursor: crosshair;
 `,[cE("layer",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `,[cM("shadowed",`
 box-shadow: inset 0 0 2px 0 rgba(0, 0, 0, .24);
 `)])]),cB("color-picker-preview",`
 display: flex;
 `,[cE("sliders",`
 flex: 1 0 auto;
 `),cE("preview",`
 position: relative;
 height: 30px;
 width: 30px;
 margin: 0 0 8px 6px;
 border-radius: 50%;
 box-shadow: rgba(0, 0, 0, .15) 0px 0px 0px 1px inset;
 overflow: hidden;
 `),cE("fill",`
 display: block;
 width: 30px;
 height: 30px;
 `),cE("input",`
 position: absolute;
 top: 0;
 left: 0;
 width: 30px;
 height: 30px;
 opacity: 0;
 z-index: 1;
 `)]),cB("color-picker-input",`
 display: flex;
 align-items: center;
 `,[cB("input",`
 flex-grow: 1;
 flex-basis: 0;
 `),cE("mode",`
 width: 72px;
 text-align: center;
 `)]),cB("color-picker-control",`
 padding: 12px;
 `),cB("color-picker-action",`
 display: flex;
 margin-top: -4px;
 border-top: 1px solid var(--n-divider-color);
 padding: 8px 12px;
 justify-content: flex-end;
 `,[cB("button","margin-left: 8px;")]),cB("color-picker-trigger",`
 border: var(--n-border);
 height: 100%;
 box-sizing: border-box;
 border-radius: var(--n-border-radius);
 transition: border-color .3s var(--n-bezier);
 cursor: pointer;
 `,[cE("value",`
 white-space: nowrap;
 position: relative;
 `),cE("fill",`
 border-radius: var(--n-border-radius);
 position: absolute;
 display: flex;
 align-items: center;
 justify-content: center;
 left: 4px;
 right: 4px;
 top: 4px;
 bottom: 4px;
 `),cM("disabled","cursor: not-allowed"),cB("color-picker-checkboard",`
 border-radius: var(--n-border-radius);
 `,[c("&::after",`
 --n-block-size: calc((var(--n-height) - 8px) / 3);
 background-size: calc(var(--n-block-size) * 2) calc(var(--n-block-size) * 2);
 background-position: 0 0, 0 var(--n-block-size), var(--n-block-size) calc(-1 * var(--n-block-size)), calc(-1 * var(--n-block-size)) 0px; 
 `)])]),cB("color-picker-swatches",`
 display: grid;
 grid-gap: 8px;
 flex-wrap: wrap;
 position: relative;
 grid-template-columns: repeat(auto-fill, 18px);
 margin-top: 10px;
 `,[cB("color-picker-swatch",`
 width: 18px;
 height: 18px;
 background-image: linear-gradient(45deg, #DDD 25%, #0000 25%), linear-gradient(-45deg, #DDD 25%, #0000 25%), linear-gradient(45deg, #0000 75%, #DDD 75%), linear-gradient(-45deg, #0000 75%, #DDD 75%);
 background-size: 8px 8px;
 background-position: 0px 0, 0px 4px, 4px -4px, -4px 0px;
 background-repeat: repeat;
 `,[cE("fill",`
 position: relative;
 width: 100%;
 height: 100%;
 border-radius: 3px;
 box-shadow: rgba(0, 0, 0, .15) 0px 0px 0px 1px inset;
 cursor: pointer;
 `),c("&:focus",`
 outline: none;
 `,[cE("fill",[c("&::after",`
 position: absolute;
 top: 0;
 right: 0;
 bottom: 0;
 left: 0;
 background: inherit;
 filter: blur(2px);
 content: "";
 `)])])])])]),colorPickerProps=Object.assign(Object.assign({},useTheme.props),{value:String,show:{type:Boolean,default:void 0},defaultShow:Boolean,defaultValue:String,modes:{type:Array,default:()=>["rgb","hex","hsl"]},placement:{type:String,default:"bottom-start"},to:useAdjustedTo.propTo,showAlpha:{type:Boolean,default:!0},showPreview:Boolean,swatches:Array,disabled:{type:Boolean,default:void 0},actions:{type:Array,default:null},internalActions:Array,size:String,renderLabel:Function,onComplete:Function,onConfirm:Function,onClear:Function,"onUpdate:show":[Function,Array],onUpdateShow:[Function,Array],"onUpdate:value":[Function,Array],onUpdateValue:[Function,Array]}),NColorPicker=defineComponent({name:"ColorPicker",props:colorPickerProps,slots:Object,setup(e,{slots:n}){const r=ref(null);let o=null;const i=useFormItem(e),{mergedSizeRef:a,mergedDisabledRef:b}=i,{localeRef:p}=useLocale("global"),{mergedClsPrefixRef:v,namespaceRef:w,inlineThemeDisabled:B}=useConfig(e),V=useTheme("ColorPicker","-color-picker",style$1,colorPickerLight$1,e,v);provide(colorPickerInjectionKey,{themeRef:V,renderLabelRef:toRef(e,"renderLabel"),colorPickerSlots:n});const R=ref(e.defaultShow),P=useMergedState(toRef(e,"show"),R);function F(s){const{onUpdateShow:k,"onUpdate:show":D}=e;k&&call(k,s),D&&call(D,s),R.value=s}const{defaultValue:E}=e,Z=ref(E===void 0?deriveDefaultValue(e.modes,e.showAlpha):E),C=useMergedState(toRef(e,"value"),Z),N=ref([C.value]),m=ref(0),I=computed(()=>getModeFromValue(C.value)),{modes:M}=e,_=ref(getModeFromValue(C.value)||M[0]||"rgb");function d(){const{modes:s}=e,{value:k}=_,D=s.findIndex(A=>A===k);~D?_.value=s[(D+1)%s.length]:_.value="rgb"}let u,S,U,q,G,X,K,L;const Q=computed(()=>{const{value:s}=C;if(!s)return null;switch(I.value){case"hsv":return hsva(s);case"hsl":return[u,S,U,L]=hsla(s),[...hsl2hsv(u,S,U),L];case"rgb":case"hex":return[G,X,K,L]=rgba(s),[...rgb2hsv(G,X,K),L]}}),J=computed(()=>{const{value:s}=C;if(!s)return null;switch(I.value){case"rgb":case"hex":return rgba(s);case"hsv":return[u,S,q,L]=hsva(s),[...hsv2rgb(u,S,q),L];case"hsl":return[u,S,U,L]=hsla(s),[...hsl2rgb(u,S,U),L]}}),ee=computed(()=>{const{value:s}=C;if(!s)return null;switch(I.value){case"hsl":return hsla(s);case"hsv":return[u,S,q,L]=hsva(s),[...hsv2hsl(u,S,q),L];case"rgb":case"hex":return[G,X,K,L]=rgba(s),[...rgb2hsl(G,X,K),L]}}),pe=computed(()=>{switch(_.value){case"rgb":case"hex":return J.value;case"hsv":return Q.value;case"hsl":return ee.value}}),ne=ref(0),re=ref(1),ie=ref([0,0]);function le(s,k){const{value:D}=Q,A=ne.value,z=D?D[3]:1;ie.value=[s,k];const{showAlpha:t}=e;switch(_.value){case"hsv":T((t?toHsvaString:toHsvString)([A,s,k,z]),"cursor");break;case"hsl":T((t?toHslaString:toHslString)([...hsv2hsl(A,s,k),z]),"cursor");break;case"rgb":T((t?toRgbaString:toRgbString)([...hsv2rgb(A,s,k),z]),"cursor");break;case"hex":T((t?toHexaString:toHexString)([...hsv2rgb(A,s,k),z]),"cursor");break}}function me(s){ne.value=s;const{value:k}=Q;if(!k)return;const[,D,A,z]=k,{showAlpha:t}=e;switch(_.value){case"hsv":T((t?toHsvaString:toHsvString)([s,D,A,z]),"cursor");break;case"rgb":T((t?toRgbaString:toRgbString)([...hsv2rgb(s,D,A),z]),"cursor");break;case"hex":T((t?toHexaString:toHexString)([...hsv2rgb(s,D,A),z]),"cursor");break;case"hsl":T((t?toHslaString:toHslString)([...hsv2hsl(s,D,A),z]),"cursor");break}}function ge(s){switch(_.value){case"hsv":[u,S,q]=Q.value,T(toHsvaString([u,S,q,s]),"cursor");break;case"rgb":[G,X,K]=J.value,T(toRgbaString([G,X,K,s]),"cursor");break;case"hex":[G,X,K]=J.value,T(toHexaString([G,X,K,s]),"cursor");break;case"hsl":[u,S,U]=ee.value,T(toHslaString([u,S,U,s]),"cursor");break}re.value=s}function T(s,k){k==="cursor"?o=s:o=null;const{nTriggerFormChange:D,nTriggerFormInput:A}=i,{onUpdateValue:z,"onUpdate:value":t}=e;z&&call(z,s),t&&call(t,s),D(),A(),Z.value=s}function he(s){T(s,"input"),nextTick(te)}function te(s=!0){const{value:k}=C;if(k){const{nTriggerFormChange:D,nTriggerFormInput:A}=i,{onComplete:z}=e;z&&z(k);const{value:t}=N,{value:l}=m;s&&(t.splice(l+1,t.length,k),m.value=l+1),D(),A()}}function se(){const{value:s}=m;s-1<0||(T(N.value[s-1],"input"),te(!1),m.value=s-1)}function xe(){const{value:s}=m;s<0||s+1>=N.value.length||(T(N.value[s+1],"input"),te(!1),m.value=s+1)}function ye(){T(null,"input");const{onClear:s}=e;s&&s(),F(!1)}function ce(){const{value:s}=C,{onConfirm:k}=e;k&&k(s),F(!1)}const de=computed(()=>m.value>=1),ue=computed(()=>{const{value:s}=N;return s.length>1&&m.value<s.length-1});watch(P,s=>{s||(N.value=[C.value],m.value=0)}),watchEffect(()=>{if(!(o&&o===C.value)){const{value:s}=Q;s&&(ne.value=s[0],re.value=s[3],ie.value=[s[1],s[2]])}o=null});const fe=computed(()=>{const{value:s}=a,{common:{cubicBezierEaseInOut:k},self:{textColor:D,color:A,panelFontSize:z,boxShadow:t,border:l,borderRadius:f,dividerColor:g,[createKey("height",s)]:$,[createKey("fontSize",s)]:H}}=V.value;return{"--n-bezier":k,"--n-text-color":D,"--n-color":A,"--n-panel-font-size":z,"--n-font-size":H,"--n-box-shadow":t,"--n-border":l,"--n-border-radius":f,"--n-height":$,"--n-divider-color":g}}),Y=B?useThemeClass("color-picker",computed(()=>a.value[0]),fe,e):void 0;function we(){var s;const{value:k}=J,{value:D}=ne,{internalActions:A,modes:z,actions:t}=e,{value:l}=V,{value:f}=v;return h("div",{class:[`${f}-color-picker-panel`,Y==null?void 0:Y.themeClass.value],onDragstart:g=>{g.preventDefault()},style:B?void 0:fe.value},h("div",{class:`${f}-color-picker-control`},h(Pallete,{clsPrefix:f,rgba:k,displayedHue:D,displayedSv:ie.value,onUpdateSV:le,onComplete:te}),h("div",{class:`${f}-color-picker-preview`},h("div",{class:`${f}-color-picker-preview__sliders`},h(HueSlider,{clsPrefix:f,hue:D,onUpdateHue:me,onComplete:te}),e.showAlpha?h(AlphaSlider,{clsPrefix:f,rgba:k,alpha:re.value,onUpdateAlpha:ge,onComplete:te}):null),e.showPreview?h(ColorPreview,{clsPrefix:f,mode:_.value,color:J.value&&toHexString(J.value),onUpdateColor:g=>{T(g,"input")}}):null),h(ColorInput,{clsPrefix:f,showAlpha:e.showAlpha,mode:_.value,modes:z,onUpdateMode:d,value:C.value,valueArr:pe.value,onUpdateValue:he}),((s=e.swatches)===null||s===void 0?void 0:s.length)&&h(ColorPickerSwatches,{clsPrefix:f,mode:_.value,swatches:e.swatches,onUpdateColor:g=>{T(g,"input")}})),t!=null&&t.length?h("div",{class:`${f}-color-picker-action`},t.includes("confirm")&&h(NButton,{size:"small",onClick:ce,theme:l.peers.Button,themeOverrides:l.peerOverrides.Button},{default:()=>p.value.confirm}),t.includes("clear")&&h(NButton,{size:"small",onClick:ye,disabled:!C.value,theme:l.peers.Button,themeOverrides:l.peerOverrides.Button},{default:()=>p.value.clear})):null,n.action?h("div",{class:`${f}-color-picker-action`},{default:n.action}):A?h("div",{class:`${f}-color-picker-action`},A.includes("undo")&&h(NButton,{size:"small",onClick:se,disabled:!de.value,theme:l.peers.Button,themeOverrides:l.peerOverrides.Button},{default:()=>p.value.undo}),A.includes("redo")&&h(NButton,{size:"small",onClick:xe,disabled:!ue.value,theme:l.peers.Button,themeOverrides:l.peerOverrides.Button},{default:()=>p.value.redo})):null)}return{mergedClsPrefix:v,namespace:w,selfRef:r,hsla:ee,rgba:J,mergedShow:P,mergedDisabled:b,isMounted:isMounted(),adjustedTo:useAdjustedTo(e),mergedValue:C,handleTriggerClick(){F(!0)},handleClickOutside(s){var k;!((k=r.value)===null||k===void 0)&&k.contains(getPreciseEventTarget(s))||F(!1)},renderPanel:we,cssVars:B?void 0:fe,themeClass:Y==null?void 0:Y.themeClass,onRender:Y==null?void 0:Y.onRender}},render(){const{mergedClsPrefix:e,onRender:n}=this;return n==null||n(),h("div",{class:[this.themeClass,`${e}-color-picker`],ref:"selfRef",style:this.cssVars},h(VBinder,null,{default:()=>[h(VTarget,null,{default:()=>h(ColorPickerTrigger,{clsPrefix:e,value:this.mergedValue,hsla:this.hsla,disabled:this.mergedDisabled,onClick:this.handleTriggerClick})}),h(VFollower,{placement:this.placement,show:this.mergedShow,containerClass:this.namespace,teleportDisabled:this.adjustedTo===useAdjustedTo.tdkey,to:this.adjustedTo},{default:()=>h(Transition,{name:"fade-in-scale-up-transition",appear:this.isMounted},{default:()=>this.mergedShow?withDirectives(this.renderPanel(),[[clickoutside,this.handleClickOutside,void 0,{capture:!0}]]):null})})]}))}}),sizeVariables={railHeight:"4px",railWidthVertical:"4px",handleSize:"18px",dotHeight:"8px",dotWidth:"8px",dotBorderRadius:"4px"};function self(e){const n="rgba(0, 0, 0, .85)",r="0 2px 8px 0 rgba(0, 0, 0, 0.12)",{railColor:o,primaryColor:i,baseColor:a,cardColor:b,modalColor:p,popoverColor:v,borderRadius:w,fontSize:B,opacityDisabled:V}=e;return Object.assign(Object.assign({},sizeVariables),{fontSize:B,markFontSize:B,railColor:o,railColorHover:o,fillColor:i,fillColorHover:i,opacityDisabled:V,handleColor:"#FFF",dotColor:b,dotColorModal:p,dotColorPopover:v,handleBoxShadow:"0 1px 4px 0 rgba(0, 0, 0, 0.3), inset 0 0 1px 0 rgba(0, 0, 0, 0.05)",handleBoxShadowHover:"0 1px 4px 0 rgba(0, 0, 0, 0.3), inset 0 0 1px 0 rgba(0, 0, 0, 0.05)",handleBoxShadowActive:"0 1px 4px 0 rgba(0, 0, 0, 0.3), inset 0 0 1px 0 rgba(0, 0, 0, 0.05)",handleBoxShadowFocus:"0 1px 4px 0 rgba(0, 0, 0, 0.3), inset 0 0 1px 0 rgba(0, 0, 0, 0.05)",indicatorColor:n,indicatorBoxShadow:r,indicatorTextColor:a,indicatorBorderRadius:w,dotBorder:`2px solid ${o}`,dotBorderActive:`2px solid ${i}`,dotBoxShadow:""})}const sliderLight={name:"Slider",common:commonLight,self},sliderLight$1=sliderLight,style=c([cB("slider",`
 display: block;
 padding: calc((var(--n-handle-size) - var(--n-rail-height)) / 2) 0;
 position: relative;
 z-index: 0;
 width: 100%;
 cursor: pointer;
 user-select: none;
 -webkit-user-select: none;
 `,[cM("reverse",[cB("slider-handles",[cB("slider-handle-wrapper",`
 transform: translate(50%, -50%);
 `)]),cB("slider-dots",[cB("slider-dot",`
 transform: translateX(50%, -50%);
 `)]),cM("vertical",[cB("slider-handles",[cB("slider-handle-wrapper",`
 transform: translate(-50%, -50%);
 `)]),cB("slider-marks",[cB("slider-mark",`
 transform: translateY(calc(-50% + var(--n-dot-height) / 2));
 `)]),cB("slider-dots",[cB("slider-dot",`
 transform: translateX(-50%) translateY(0);
 `)])])]),cM("vertical",`
 box-sizing: content-box;
 padding: 0 calc((var(--n-handle-size) - var(--n-rail-height)) / 2);
 width: var(--n-rail-width-vertical);
 height: 100%;
 `,[cB("slider-handles",`
 top: calc(var(--n-handle-size) / 2);
 right: 0;
 bottom: calc(var(--n-handle-size) / 2);
 left: 0;
 `,[cB("slider-handle-wrapper",`
 top: unset;
 left: 50%;
 transform: translate(-50%, 50%);
 `)]),cB("slider-rail",`
 height: 100%;
 `,[cE("fill",`
 top: unset;
 right: 0;
 bottom: unset;
 left: 0;
 `)]),cM("with-mark",`
 width: var(--n-rail-width-vertical);
 margin: 0 32px 0 8px;
 `),cB("slider-marks",`
 top: calc(var(--n-handle-size) / 2);
 right: unset;
 bottom: calc(var(--n-handle-size) / 2);
 left: 22px;
 font-size: var(--n-mark-font-size);
 `,[cB("slider-mark",`
 transform: translateY(50%);
 white-space: nowrap;
 `)]),cB("slider-dots",`
 top: calc(var(--n-handle-size) / 2);
 right: unset;
 bottom: calc(var(--n-handle-size) / 2);
 left: 50%;
 `,[cB("slider-dot",`
 transform: translateX(-50%) translateY(50%);
 `)])]),cM("disabled",`
 cursor: not-allowed;
 opacity: var(--n-opacity-disabled);
 `,[cB("slider-handle",`
 cursor: not-allowed;
 `)]),cM("with-mark",`
 width: 100%;
 margin: 8px 0 32px 0;
 `),c("&:hover",[cB("slider-rail",{backgroundColor:"var(--n-rail-color-hover)"},[cE("fill",{backgroundColor:"var(--n-fill-color-hover)"})]),cB("slider-handle",{boxShadow:"var(--n-handle-box-shadow-hover)"})]),cM("active",[cB("slider-rail",{backgroundColor:"var(--n-rail-color-hover)"},[cE("fill",{backgroundColor:"var(--n-fill-color-hover)"})]),cB("slider-handle",{boxShadow:"var(--n-handle-box-shadow-hover)"})]),cB("slider-marks",`
 position: absolute;
 top: 18px;
 left: calc(var(--n-handle-size) / 2);
 right: calc(var(--n-handle-size) / 2);
 `,[cB("slider-mark",`
 position: absolute;
 transform: translateX(-50%);
 white-space: nowrap;
 `)]),cB("slider-rail",`
 width: 100%;
 position: relative;
 height: var(--n-rail-height);
 background-color: var(--n-rail-color);
 transition: background-color .3s var(--n-bezier);
 border-radius: calc(var(--n-rail-height) / 2);
 `,[cE("fill",`
 position: absolute;
 top: 0;
 bottom: 0;
 border-radius: calc(var(--n-rail-height) / 2);
 transition: background-color .3s var(--n-bezier);
 background-color: var(--n-fill-color);
 `)]),cB("slider-handles",`
 position: absolute;
 top: 0;
 right: calc(var(--n-handle-size) / 2);
 bottom: 0;
 left: calc(var(--n-handle-size) / 2);
 `,[cB("slider-handle-wrapper",`
 outline: none;
 position: absolute;
 top: 50%;
 transform: translate(-50%, -50%);
 cursor: pointer;
 display: flex;
 `,[cB("slider-handle",`
 height: var(--n-handle-size);
 width: var(--n-handle-size);
 border-radius: 50%;
 overflow: hidden;
 transition: box-shadow .2s var(--n-bezier), background-color .3s var(--n-bezier);
 background-color: var(--n-handle-color);
 box-shadow: var(--n-handle-box-shadow);
 `,[c("&:hover",`
 box-shadow: var(--n-handle-box-shadow-hover);
 `)]),c("&:focus",[cB("slider-handle",`
 box-shadow: var(--n-handle-box-shadow-focus);
 `,[c("&:hover",`
 box-shadow: var(--n-handle-box-shadow-active);
 `)])])])]),cB("slider-dots",`
 position: absolute;
 top: 50%;
 left: calc(var(--n-handle-size) / 2);
 right: calc(var(--n-handle-size) / 2);
 `,[cM("transition-disabled",[cB("slider-dot","transition: none;")]),cB("slider-dot",`
 transition:
 border-color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 position: absolute;
 transform: translate(-50%, -50%);
 height: var(--n-dot-height);
 width: var(--n-dot-width);
 border-radius: var(--n-dot-border-radius);
 overflow: hidden;
 box-sizing: border-box;
 border: var(--n-dot-border);
 background-color: var(--n-dot-color);
 `,[cM("active","border: var(--n-dot-border-active);")])])]),cB("slider-handle-indicator",`
 font-size: var(--n-font-size);
 padding: 6px 10px;
 border-radius: var(--n-indicator-border-radius);
 color: var(--n-indicator-text-color);
 background-color: var(--n-indicator-color);
 box-shadow: var(--n-indicator-box-shadow);
 `,[fadeInScaleUpTransition()]),cB("slider-handle-indicator",`
 font-size: var(--n-font-size);
 padding: 6px 10px;
 border-radius: var(--n-indicator-border-radius);
 color: var(--n-indicator-text-color);
 background-color: var(--n-indicator-color);
 box-shadow: var(--n-indicator-box-shadow);
 `,[cM("top",`
 margin-bottom: 12px;
 `),cM("right",`
 margin-left: 12px;
 `),cM("bottom",`
 margin-top: 12px;
 `),cM("left",`
 margin-right: 12px;
 `),fadeInScaleUpTransition()]),insideModal(cB("slider",[cB("slider-dot","background-color: var(--n-dot-color-modal);")])),insidePopover(cB("slider",[cB("slider-dot","background-color: var(--n-dot-color-popover);")]))]);function isTouchEvent(e){return window.TouchEvent&&e instanceof window.TouchEvent}function useRefs(){const e=new Map,n=r=>o=>{e.set(r,o)};return onBeforeUpdate(()=>{e.clear()}),[e,n]}const eventButtonLeft=0,sliderProps=Object.assign(Object.assign({},useTheme.props),{to:useAdjustedTo.propTo,defaultValue:{type:[Number,Array],default:0},marks:Object,disabled:{type:Boolean,default:void 0},formatTooltip:Function,keyboard:{type:Boolean,default:!0},min:{type:Number,default:0},max:{type:Number,default:100},step:{type:[Number,String],default:1},range:Boolean,value:[Number,Array],placement:String,showTooltip:{type:Boolean,default:void 0},tooltip:{type:Boolean,default:!0},vertical:Boolean,reverse:Boolean,"onUpdate:value":[Function,Array],onUpdateValue:[Function,Array],onDragstart:[Function],onDragend:[Function]}),NSlider=defineComponent({name:"Slider",props:sliderProps,slots:Object,setup(e){const{mergedClsPrefixRef:n,namespaceRef:r,inlineThemeDisabled:o}=useConfig(e),i=useTheme("Slider","-slider",style,sliderLight$1,e,n),a=ref(null),[b,p]=useRefs(),[v,w]=useRefs(),B=ref(new Set),V=useFormItem(e),{mergedDisabledRef:R}=V,P=computed(()=>{const{step:t}=e;if(Number(t)<=0||t==="mark")return 0;const l=t.toString();let f=0;return l.includes(".")&&(f=l.length-l.indexOf(".")-1),f}),F=ref(e.defaultValue),E=toRef(e,"value"),Z=useMergedState(E,F),C=computed(()=>{const{value:t}=Z;return(e.range?t:[t]).map(ie)}),N=computed(()=>C.value.length>2),m=computed(()=>e.placement===void 0?e.vertical?"right":"top":e.placement),I=computed(()=>{const{marks:t}=e;return t?Object.keys(t).map(Number.parseFloat):null}),M=ref(-1),_=ref(-1),d=ref(-1),u=ref(!1),S=ref(!1),U=computed(()=>{const{vertical:t,reverse:l}=e;return t?l?"top":"bottom":l?"right":"left"}),q=computed(()=>{if(N.value)return;const t=C.value,l=le(e.range?Math.min(...t):e.min),f=le(e.range?Math.max(...t):t[0]),{value:g}=U;return e.vertical?{[g]:`${l}%`,height:`${f-l}%`}:{[g]:`${l}%`,width:`${f-l}%`}}),G=computed(()=>{const t=[],{marks:l}=e;if(l){const f=C.value.slice();f.sort((O,j)=>O-j);const{value:g}=U,{value:$}=N,{range:H}=e,W=$?()=>!1:O=>H?O>=f[0]&&O<=f[f.length-1]:O<=f[0];for(const O of Object.keys(l)){const j=Number(O);t.push({active:W(j),key:j,label:l[O],style:{[g]:`${le(j)}%`}})}}return t});function X(t,l){const f=le(t),{value:g}=U;return{[g]:`${f}%`,zIndex:l===M.value?1:0}}function K(t){return e.showTooltip||d.value===t||M.value===t&&u.value}function L(t){return u.value?!(M.value===t&&_.value===t):!0}function Q(t){var l;~t&&(M.value=t,(l=b.get(t))===null||l===void 0||l.focus())}function J(){v.forEach((t,l)=>{K(l)&&t.syncPosition()})}function ee(t){const{"onUpdate:value":l,onUpdateValue:f}=e,{nTriggerFormInput:g,nTriggerFormChange:$}=V;f&&call(f,t),l&&call(l,t),F.value=t,g(),$()}function pe(t){const{range:l}=e;if(l){if(Array.isArray(t)){const{value:f}=C;t.join()!==f.join()&&ee(t)}}else Array.isArray(t)||C.value[0]!==t&&ee(t)}function ne(t,l){if(e.range){const f=C.value.slice();f.splice(l,1,t),pe(f)}else pe(t)}function re(t,l,f){const g=f!==void 0;f||(f=t-l>0?1:-1);const $=I.value||[],{step:H}=e;if(H==="mark"){const j=T(t,$.concat(l),g?f:void 0);return j?j.value:l}if(H<=0)return l;const{value:W}=P;let O;if(g){const j=Number((l/H).toFixed(W)),oe=Math.floor(j),_e=j>oe?oe:oe-1,Se=j<oe?oe:oe+1;O=T(l,[Number((_e*H).toFixed(W)),Number((Se*H).toFixed(W)),...$],f)}else{const j=ge(t);O=T(t,[...$,j])}return O?ie(O.value):l}function ie(t){return Math.min(e.max,Math.max(e.min,t))}function le(t){const{max:l,min:f}=e;return(t-f)/(l-f)*100}function me(t){const{max:l,min:f}=e;return f+(l-f)*t}function ge(t){const{step:l,min:f}=e;if(Number(l)<=0||l==="mark")return t;const g=Math.round((t-f)/l)*l+f;return Number(g.toFixed(P.value))}function T(t,l=I.value,f){if(!(l!=null&&l.length))return null;let g=null,$=-1;for(;++$<l.length;){const H=l[$]-t,W=Math.abs(H);(f===void 0||H*f>0)&&(g===null||W<g.distance)&&(g={index:$,distance:W,value:l[$]})}return g}function he(t){const l=a.value;if(!l)return;const f=isTouchEvent(t)?t.touches[0]:t,g=l.getBoundingClientRect();let $;return e.vertical?$=(g.bottom-f.clientY)/g.height:$=(f.clientX-g.left)/g.width,e.reverse&&($=1-$),me($)}function te(t){if(R.value||!e.keyboard)return;const{vertical:l,reverse:f}=e;switch(t.key){case"ArrowUp":t.preventDefault(),se(l&&f?-1:1);break;case"ArrowRight":t.preventDefault(),se(!l&&f?-1:1);break;case"ArrowDown":t.preventDefault(),se(l&&f?1:-1);break;case"ArrowLeft":t.preventDefault(),se(!l&&f?1:-1);break}}function se(t){const l=M.value;if(l===-1)return;const{step:f}=e,g=C.value[l],$=Number(f)<=0||f==="mark"?g:g+f*t;ne(re($,g,t>0?1:-1),l)}function xe(t){var l,f;if(R.value||!isTouchEvent(t)&&t.button!==eventButtonLeft)return;const g=he(t);if(g===void 0)return;const $=C.value.slice(),H=e.range?(f=(l=T(g,$))===null||l===void 0?void 0:l.index)!==null&&f!==void 0?f:-1:0;H!==-1&&(t.preventDefault(),Q(H),ye(),ne(re(g,C.value[H]),H))}function ye(){u.value||(u.value=!0,e.onDragstart&&call(e.onDragstart),on("touchend",document,ue),on("mouseup",document,ue),on("touchmove",document,de),on("mousemove",document,de))}function ce(){u.value&&(u.value=!1,e.onDragend&&call(e.onDragend),off("touchend",document,ue),off("mouseup",document,ue),off("touchmove",document,de),off("mousemove",document,de))}function de(t){const{value:l}=M;if(!u.value||l===-1){ce();return}const f=he(t);f!==void 0&&ne(re(f,C.value[l]),l)}function ue(){ce()}function fe(t){M.value=t,R.value||(d.value=t)}function Y(t){M.value===t&&(M.value=-1,ce()),d.value===t&&(d.value=-1)}function we(t){d.value=t}function s(t){d.value===t&&(d.value=-1)}watch(M,(t,l)=>void nextTick(()=>_.value=l)),watch(Z,()=>{if(e.marks){if(S.value)return;S.value=!0,nextTick(()=>{S.value=!1})}nextTick(J)}),onBeforeUnmount(()=>{ce()});const k=computed(()=>{const{self:{markFontSize:t,railColor:l,railColorHover:f,fillColor:g,fillColorHover:$,handleColor:H,opacityDisabled:W,dotColor:O,dotColorModal:j,handleBoxShadow:oe,handleBoxShadowHover:_e,handleBoxShadowActive:Se,handleBoxShadowFocus:Be,dotBorder:Ne,dotBoxShadow:Re,railHeight:Ve,railWidthVertical:De,handleSize:$e,dotHeight:Ie,dotWidth:ze,dotBorderRadius:Pe,fontSize:Ue,dotBorderActive:Ae,dotColorPopover:Te},common:{cubicBezierEaseInOut:Fe}}=i.value;return{"--n-bezier":Fe,"--n-dot-border":Ne,"--n-dot-border-active":Ae,"--n-dot-border-radius":Pe,"--n-dot-box-shadow":Re,"--n-dot-color":O,"--n-dot-color-modal":j,"--n-dot-color-popover":Te,"--n-dot-height":Ie,"--n-dot-width":ze,"--n-fill-color":g,"--n-fill-color-hover":$,"--n-font-size":Ue,"--n-handle-box-shadow":oe,"--n-handle-box-shadow-active":Se,"--n-handle-box-shadow-focus":Be,"--n-handle-box-shadow-hover":_e,"--n-handle-color":H,"--n-handle-size":$e,"--n-opacity-disabled":W,"--n-rail-color":l,"--n-rail-color-hover":f,"--n-rail-height":Ve,"--n-rail-width-vertical":De,"--n-mark-font-size":t}}),D=o?useThemeClass("slider",void 0,k,e):void 0,A=computed(()=>{const{self:{fontSize:t,indicatorColor:l,indicatorBoxShadow:f,indicatorTextColor:g,indicatorBorderRadius:$}}=i.value;return{"--n-font-size":t,"--n-indicator-border-radius":$,"--n-indicator-box-shadow":f,"--n-indicator-color":l,"--n-indicator-text-color":g}}),z=o?useThemeClass("slider-indicator",void 0,A,e):void 0;return{mergedClsPrefix:n,namespace:r,uncontrolledValue:F,mergedValue:Z,mergedDisabled:R,mergedPlacement:m,isMounted:isMounted(),adjustedTo:useAdjustedTo(e),dotTransitionDisabled:S,markInfos:G,isShowTooltip:K,shouldKeepTooltipTransition:L,handleRailRef:a,setHandleRefs:p,setFollowerRefs:w,fillStyle:q,getHandleStyle:X,activeIndex:M,arrifiedValues:C,followerEnabledIndexSet:B,handleRailMouseDown:xe,handleHandleFocus:fe,handleHandleBlur:Y,handleHandleMouseEnter:we,handleHandleMouseLeave:s,handleRailKeyDown:te,indicatorCssVars:o?void 0:A,indicatorThemeClass:z==null?void 0:z.themeClass,indicatorOnRender:z==null?void 0:z.onRender,cssVars:o?void 0:k,themeClass:D==null?void 0:D.themeClass,onRender:D==null?void 0:D.onRender}},render(){var e;const{mergedClsPrefix:n,themeClass:r,formatTooltip:o}=this;return(e=this.onRender)===null||e===void 0||e.call(this),h("div",{class:[`${n}-slider`,r,{[`${n}-slider--disabled`]:this.mergedDisabled,[`${n}-slider--active`]:this.activeIndex!==-1,[`${n}-slider--with-mark`]:this.marks,[`${n}-slider--vertical`]:this.vertical,[`${n}-slider--reverse`]:this.reverse}],style:this.cssVars,onKeydown:this.handleRailKeyDown,onMousedown:this.handleRailMouseDown,onTouchstart:this.handleRailMouseDown},h("div",{class:`${n}-slider-rail`},h("div",{class:`${n}-slider-rail__fill`,style:this.fillStyle}),this.marks?h("div",{class:[`${n}-slider-dots`,this.dotTransitionDisabled&&`${n}-slider-dots--transition-disabled`]},this.markInfos.map(i=>h("div",{key:i.key,class:[`${n}-slider-dot`,{[`${n}-slider-dot--active`]:i.active}],style:i.style}))):null,h("div",{ref:"handleRailRef",class:`${n}-slider-handles`},this.arrifiedValues.map((i,a)=>{const b=this.isShowTooltip(a);return h(VBinder,null,{default:()=>[h(VTarget,null,{default:()=>h("div",{ref:this.setHandleRefs(a),class:`${n}-slider-handle-wrapper`,tabindex:this.mergedDisabled?-1:0,role:"slider","aria-valuenow":i,"aria-valuemin":this.min,"aria-valuemax":this.max,"aria-orientation":this.vertical?"vertical":"horizontal","aria-disabled":this.disabled,style:this.getHandleStyle(i,a),onFocus:()=>{this.handleHandleFocus(a)},onBlur:()=>{this.handleHandleBlur(a)},onMouseenter:()=>{this.handleHandleMouseEnter(a)},onMouseleave:()=>{this.handleHandleMouseLeave(a)}},resolveSlot(this.$slots.thumb,()=>[h("div",{class:`${n}-slider-handle`})]))}),this.tooltip&&h(VFollower,{ref:this.setFollowerRefs(a),show:b,to:this.adjustedTo,enabled:this.showTooltip&&!this.range||this.followerEnabledIndexSet.has(a),teleportDisabled:this.adjustedTo===useAdjustedTo.tdkey,placement:this.mergedPlacement,containerClass:this.namespace},{default:()=>h(Transition,{name:"fade-in-scale-up-transition",appear:this.isMounted,css:this.shouldKeepTooltipTransition(a),onEnter:()=>{this.followerEnabledIndexSet.add(a)},onAfterLeave:()=>{this.followerEnabledIndexSet.delete(a)}},{default:()=>{var p;return b?((p=this.indicatorOnRender)===null||p===void 0||p.call(this),h("div",{class:[`${n}-slider-handle-indicator`,this.indicatorThemeClass,`${n}-slider-handle-indicator--${this.mergedPlacement}`],style:this.indicatorCssVars},typeof o=="function"?o(i):i)):null}})})]})})),this.marks?h("div",{class:`${n}-slider-marks`},this.markInfos.map(i=>h("div",{key:i.key,class:`${n}-slider-mark`,style:i.style},typeof i.label=="function"?i.label():i.label))):null))}}),formRef=ref(null),SourceType={plaza:1,created:2},defaultFormData={avatar:default_friend_avatar,cssAvatar:"",name:"",tag:"",intro:"",welcome:"",systemPrompt:"",contentPrompt:"",messageContextSize:32,friendSource:SourceType.created,conversationStart:[""],openaiRequestBody:{maxTokens:1e3,temperature:1,topP:0,presencePenalty:0,frequencyPenalty:0}},formData=ref(JSON.parse(JSON.stringify(defaultFormData))),verify=(e,n)=>{let r=e;const{step:o,max:i,min:a}=n;if(o)if(isInteger(o)){const b=Math.floor(e/o);r=o*b}else{const b=String(o),[p,v]=b.split(".");let w=1;for(let R=0;R<v.length;R++)w*=10;const B=o*w,V=Math.floor(e*w/B);r=B*V/w}return i&&(r=r>i?i:r),a&&(r=r<a?a:r),r},config={messageContextSize:{label:"上下文数量",popover:"每次聊天发送消息时携带的历史消息数量越多，话题关联性就越高；反之，数量越少，关联性就越低。如果设置的消息数量超过模型的最大限制，系统会自动截取，以确保话题的正常进行。",attrs:{min:2,max:64,step:2},stepVerify:e=>verify(Number(e),config.messageContextSize.attrs)},openaiRequestBody:{maxTokens:{label:"最大回复数",popover:"生成内容的最大 token 数量。输入和生成的总长度受模型上下文长度的限制。",attrs:{min:100,max:2800,step:50},stepVerify:e=>verify(Number(e),config.openaiRequestBody.maxTokens.attrs)},temperature:{label:"随机性",popover:"生成内容的随机性，在0和2之间。较高的值如0.8会使输出更随机，而较低的值如0.2会使其更加集中和确定性。我们通常建议修改这个或者「词汇熟悉」，但不要同时修改两者。",attrs:{min:0,max:2,step:.1},stepVerify:e=>verify(Number(e),config.openaiRequestBody.temperature.attrs)},topP:{label:"词汇属性",popover:"一种与随机性相对的方法，模型考虑的是具有 top_p 概率质量的标记的结果。因此，0.1 表示只考虑组成前 10% 概率质量的标记。我们通常建议修改这个或者「随机性」，但不要同时修改两者。",attrs:{min:0,max:1,step:.1},stepVerify:e=>verify(Number(e),config.openaiRequestBody.topP.attrs)},presencePenalty:{label:"话题新鲜度",popover:"默认值为 0，值越大，越能够让Ai更好地控制新话题的引入，建议微调或不变。",attrs:{min:-2,max:2,step:.1},stepVerify:e=>verify(Number(e),config.openaiRequestBody.presencePenalty.attrs)},frequencyPenalty:{label:"频率惩罚度",popover:"在-2.0和2.0之间的数字。正值根据文本中新标记的现有频率对其进行惩罚，从而降低模型重复相同行的可能性。",attrs:{min:-2,max:2,step:.1},stepVerify:e=>verify(Number(e),config.openaiRequestBody.frequencyPenalty.attrs)}}},rules={name:{required:!0,message:" ",trigger:"blur"}},formDataClear=()=>{formData.value=JSON.parse(JSON.stringify(defaultFormData))},Popover_vue_vue_type_style_index_0_scoped_c95df1c2_lang="",_hoisted_1$5={class:"right_tip"},_sfc_main$6={__name:"Popover",props:["text"],setup(e){const n=e;return(r,o)=>(openBlock(),createBlock(unref(NPopover),{trigger:"hover",raw:""},{trigger:withCtx(()=>o[0]||(o[0]=[createBaseVNode("span",{class:"iconfont icon-wenhao"},null,-1)])),default:withCtx(()=>[createBaseVNode("div",_hoisted_1$5,toDisplayString(n.text),1)]),_:1}))}},Popover=_export_sfc(_sfc_main$6,[["__scopeId","data-v-c95df1c2"]]),FormComp_vue_vue_type_style_index_0_scoped_e66de1c4_lang="",_hoisted_1$4={class:"item_contet"},_hoisted_2$4={class:"item_contet"},_hoisted_3$2={class:"item_contet"},_hoisted_4$1={class:"item_contet"},_hoisted_5$1={class:"close"},_hoisted_6$1={class:"flex"},_hoisted_7$1={class:"flex"},_hoisted_8$1={class:"flex"},_hoisted_9$1={class:"flex"},_hoisted_10$1={class:"flex"},_hoisted_11$1={class:"flex"},_hoisted_12={class:"item_contet"},_hoisted_13={class:"footer flex"},_sfc_main$5={__name:"FormComp",props:["tags"],emits:["confirm","close"],setup(e,{expose:n,emit:r}){const o=useMessage(),i=inject("resize"),a=e,b=r,p=window.__talkx__.openUrl,v=ref([{text:""}]),w=(_,d)=>{const u=v.value.length;_&&d==u-1&&u<4&&v.value.push({text:""})},B=_=>{if(v.value.length<=1){o.warning("至少需要一个快速开始");return}v.value.splice(_,1)};watch(v,(_,d)=>{formData.value.conversationStart=_.filter(({text:u})=>u.trim()).map(({text:u})=>u)},{deep:!0});const V=()=>{v.value=formData.value.conversationStart.filter(_=>_).map(_=>({text:_})),v.value.length<4&&v.value.push({text:""})},R=()=>p(systemPrompt_url),P=computed(()=>a.tags.map(({name:_})=>({label:_,value:_}))||[]),F=debance(_=>_(),300),E=(_,d)=>{const[u,S]=d.split(".");let U=_;if(u&&S){const q=config[u][S].stepVerify;q&&F(()=>{formData.value[u][S]=q(U)}),formData.value[u][S]=U}else{const q=config[u].stepVerify;q&&F(()=>{formData.value[u]=q(U)}),formData.value[u]=U}},Z=()=>{formData.value.openaiRequestBody.temperature=0},C=()=>{formData.value.openaiRequestBody.topP=0};let N=!1;const m=()=>N=!1,I=()=>b("close"),M=()=>ae(this,null,function*(){if(!formRef.value||N)return;if(N=!0,yield new Promise(d=>formRef.value.validate(d)))return m();b("confirm",m)});return onMounted(()=>V()),n({update:V}),(_,d)=>(openBlock(),createBlock(unref(NForm),{class:"formComp",ref_key:"formRef",ref:formRef,"label-placement":unref(i).smallRef.value?"top":"left","label-width":110,model:unref(formData),rules:unref(rules)},{default:withCtx(()=>[d[27]||(d[27]=createBaseVNode("div",{class:"diver"},"基本信息",-1)),createVNode(unref(NFormItem),{label:"昵称",path:"name"},{default:withCtx(()=>[createVNode(unref(NInput),{class:"_input",value:unref(formData).name,"onUpdate:value":d[0]||(d[0]=u=>unref(formData).name=u),maxlength:"12","show-count":"",placeholder:""},null,8,["value"])]),_:1}),createVNode(unref(NFormItem),{label:"标签",path:"tag"},{default:withCtx(()=>[createVNode(unref(NSelect),{class:"tp_select",value:unref(formData).tag,"onUpdate:value":d[1]||(d[1]=u=>unref(formData).tag=u),options:P.value},null,8,["value","options"])]),_:1}),createVNode(unref(NFormItem),{label:"简介",path:"intro"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_1$4,[createVNode(unref(NInput),{class:"_input",value:unref(formData).intro,"onUpdate:value":d[2]||(d[2]=u=>unref(formData).intro=u),maxlength:"30","show-count":"",placeholder:""},null,8,["value"]),d[18]||(d[18]=createBaseVNode("div",{class:"desc"},"简单介绍它的作用",-1))])]),_:1}),createVNode(unref(NFormItem),{label:"招呼语",path:"welcome"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_2$4,[createVNode(unref(NInput),{class:"_input",value:unref(formData).welcome,"onUpdate:value":d[3]||(d[3]=u=>unref(formData).welcome=u),maxlength:"100","show-count":"",placeholder:""},null,8,["value"]),d[19]||(d[19]=createBaseVNode("div",{class:"desc"},"建立新话题页面所展示的招呼",-1))])]),_:1}),createVNode(unref(NFormItem),{label:"指令",path:"systemPrompt"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_3$2,[createVNode(unref(NInput),{class:"_input",type:"textarea","show-count":"",autosize:{minRows:5,maxRows:5},value:unref(formData).systemPrompt,"onUpdate:value":d[4]||(d[4]=u=>unref(formData).systemPrompt=u),maxlength:"4000",placeholder:""},null,8,["value"]),createBaseVNode("div",{class:"desc"},[d[20]||(d[20]=createTextVNode(" 设置这个AI的系统指令（角色描述），通过 ")),createBaseVNode("span",{class:"model_look",onClick:R},"「这里」"),d[21]||(d[21]=createTextVNode(" 可以获取一些帮助 "))])])]),_:1}),unref(formData).friendSource==unref(SourceType).created?(openBlock(),createBlock(unref(NFormItem),{key:0,label:"快速开始",path:"conversationStart"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_4$1,[(openBlock(!0),createElementBlock(Fragment,null,renderList(v.value,(u,S)=>(openBlock(),createElementBlock("div",{class:"rowInput",key:u},[(openBlock(),createBlock(unref(NInput),{key:S,value:u.text,"onUpdate:value":U=>u.text=U,onInput:U=>w(U,S),class:"_input",maxlength:"100","show-count":"",placeholder:""},null,8,["value","onUpdate:value","onInput"])),createBaseVNode("div",_hoisted_5$1,[createVNode(unref(NButton),{class:"_close_btn",onClick:U=>B(S)},{default:withCtx(()=>d[22]||(d[22]=[createBaseVNode("span",{class:"iconfont icon-close"},null,-1)])),_:2},1032,["onClick"])])]))),128)),d[23]||(d[23]=createBaseVNode("div",{class:"desc"},"提供用户快速开始对话的示例",-1))])]),_:1})):createCommentVNode("",!0),d[28]||(d[28]=createBaseVNode("div",{class:"diver"},"模型设置",-1)),createVNode(unref(NFormItem),{class:"modelSet",path:"messageContextSize"},{label:withCtx(()=>[createBaseVNode("span",null,toDisplayString(unref(config).messageContextSize.label),1),createVNode(Popover,{text:unref(config).messageContextSize.popover},null,8,["text"])]),default:withCtx(()=>[createBaseVNode("div",_hoisted_6$1,[createVNode(unref(NSlider),mergeProps({class:"slider",value:unref(formData).messageContextSize,"onUpdate:value":d[5]||(d[5]=u=>unref(formData).messageContextSize=u)},unref(config).messageContextSize.attrs),null,16,["value"]),createVNode(unref(NInput),{class:"input _input",value:unref(formData).messageContextSize,onInput:d[6]||(d[6]=u=>E(u,"messageContextSize")),placeholder:""},null,8,["value"])])]),_:1}),createVNode(unref(NFormItem),{class:"modelSet",path:"openaiRequestBody.maxTokens"},{label:withCtx(()=>[createBaseVNode("span",null,toDisplayString(unref(config).openaiRequestBody.maxTokens.label),1),createVNode(Popover,{text:unref(config).openaiRequestBody.maxTokens.popover},null,8,["text"])]),default:withCtx(()=>[createBaseVNode("div",_hoisted_7$1,[createVNode(unref(NSlider),mergeProps({class:"slider"},unref(config).openaiRequestBody.maxTokens.attrs,{value:unref(formData).openaiRequestBody.maxTokens,"onUpdate:value":d[7]||(d[7]=u=>unref(formData).openaiRequestBody.maxTokens=u)}),null,16,["value"]),createVNode(unref(NInput),{class:"input _input",value:unref(formData).openaiRequestBody.maxTokens,onInput:d[8]||(d[8]=u=>E(u,"openaiRequestBody.maxTokens")),placeholder:""},null,8,["value"])])]),_:1}),createVNode(unref(NFormItem),{class:"modelSet",path:"openaiRequestBody.temperature"},{label:withCtx(()=>[createBaseVNode("span",null,toDisplayString(unref(config).openaiRequestBody.temperature.label),1),createVNode(Popover,{text:unref(config).openaiRequestBody.temperature.popover},null,8,["text"])]),default:withCtx(()=>[createBaseVNode("div",_hoisted_8$1,[createVNode(unref(NSlider),mergeProps({class:"slider","onUpdate:value":C},unref(config).openaiRequestBody.temperature.attrs,{value:unref(formData).openaiRequestBody.temperature,"onUpdate:value":d[9]||(d[9]=u=>unref(formData).openaiRequestBody.temperature=u)}),null,16,["value"]),createVNode(unref(NInput),{class:"input _input",value:unref(formData).openaiRequestBody.temperature,onInput:d[10]||(d[10]=u=>E(u,"openaiRequestBody.temperature")),placeholder:""},null,8,["value"])])]),_:1}),createVNode(unref(NFormItem),{class:"modelSet",path:"openaiRequestBody.topP"},{label:withCtx(()=>[createBaseVNode("span",null,toDisplayString(unref(config).openaiRequestBody.topP.label),1),createVNode(Popover,{text:unref(config).openaiRequestBody.topP.popover},null,8,["text"])]),default:withCtx(()=>[createBaseVNode("div",_hoisted_9$1,[createVNode(unref(NSlider),mergeProps({class:"slider","onUpdate:value":Z},unref(config).openaiRequestBody.topP.attrs,{value:unref(formData).openaiRequestBody.topP,"onUpdate:value":d[11]||(d[11]=u=>unref(formData).openaiRequestBody.topP=u)}),null,16,["value"]),createVNode(unref(NInput),{class:"input _input",value:unref(formData).openaiRequestBody.topP,onInput:d[12]||(d[12]=u=>E(u,"openaiRequestBody.topP")),placeholder:""},null,8,["value"])])]),_:1}),createVNode(unref(NFormItem),{class:"modelSet",path:"openaiRequestBody.presencePenalty"},{label:withCtx(()=>[createBaseVNode("span",null,toDisplayString(unref(config).openaiRequestBody.presencePenalty.label),1),createVNode(Popover,{text:unref(config).openaiRequestBody.presencePenalty.popover},null,8,["text"])]),default:withCtx(()=>[createBaseVNode("div",_hoisted_10$1,[createVNode(unref(NSlider),mergeProps({class:"slider"},unref(config).openaiRequestBody.presencePenalty.attrs,{value:unref(formData).openaiRequestBody.presencePenalty,"onUpdate:value":d[13]||(d[13]=u=>unref(formData).openaiRequestBody.presencePenalty=u)}),null,16,["value"]),createVNode(unref(NInput),{class:"input _input",value:unref(formData).openaiRequestBody.presencePenalty,onInput:d[14]||(d[14]=u=>E(u,"openaiRequestBody.presencePenalty")),placeholder:""},null,8,["value"])])]),_:1}),createVNode(unref(NFormItem),{class:"modelSet",path:"openaiRequestBody.frequencyPenalty"},{label:withCtx(()=>[createBaseVNode("span",null,toDisplayString(unref(config).openaiRequestBody.frequencyPenalty.label),1),createVNode(Popover,{text:unref(config).openaiRequestBody.frequencyPenalty.popover},null,8,["text"])]),default:withCtx(()=>[createBaseVNode("div",_hoisted_11$1,[createVNode(unref(NSlider),mergeProps({class:"slider"},unref(config).openaiRequestBody.frequencyPenalty.attrs,{value:unref(formData).openaiRequestBody.frequencyPenalty,"onUpdate:value":d[15]||(d[15]=u=>unref(formData).openaiRequestBody.frequencyPenalty=u)}),null,16,["value"]),createVNode(unref(NInput),{class:"input _input",value:unref(formData).openaiRequestBody.frequencyPenalty,onInput:d[16]||(d[16]=u=>E(u,"openaiRequestBody.frequencyPenalty")),placeholder:""},null,8,["value"])])]),_:1}),d[29]||(d[29]=createBaseVNode("div",{class:"diver"},"其他设置",-1)),createVNode(unref(NFormItem),{label:"Prompt Template",path:"contentPrompt"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_12,[createVNode(unref(NInput),{class:"_input",type:"textarea","show-count":"",autosize:{minRows:5,maxRows:5},value:unref(formData).contentPrompt,"onUpdate:value":d[17]||(d[17]=u=>unref(formData).contentPrompt=u),maxlength:"4000",placeholder:""},null,8,["value"]),d[24]||(d[24]=createBaseVNode("div",{class:"desc"}," 修改用户的提问时的模板脚本，默认情况不建议设置。 ",-1))])]),_:1}),createVNode(unref(NFormItem),null,{default:withCtx(()=>[createBaseVNode("div",_hoisted_13,[createVNode(unref(NButton),{class:"_close_btn",onClick:I},{default:withCtx(()=>d[25]||(d[25]=[createTextVNode("取消")])),_:1}),createVNode(unref(NButton),{class:"_confirm_btn",onClick:M,type:"info"},{default:withCtx(()=>d[26]||(d[26]=[createTextVNode(" 确定 ")])),_:1})])]),_:1})]),_:1},8,["label-placement","model","rules"]))}},FormComp=_export_sfc(_sfc_main$5,[["__scopeId","data-v-e66de1c4"]]),index_vue_vue_type_style_index_0_scoped_66c60425_lang="",scale=2,_sfc_main$4={__name:"index",props:{size:{typeof:Object,default:{width:80,height:80}},bgColor:{type:String,default:"#50c7da"},type:{type:String,default:"text"},text:{type:String,default:"文字"},fontFamily:{type:String,default:"Arial"},offset:{typeof:Object,default:{x:0,y:0}},fontSize:Number},setup(__props,{expose:__expose}){const props=__props,style=computed(()=>{const{width:e,height:n}=props.size;return{width:e+"px",height:n+"px"}}),canvasRef=ref(null);let canvas,ctx;const initCanvas=()=>{!canvasRef.value||canvas||(canvas=canvasRef.value,ctx=canvas.getContext("2d"))},draw=()=>{if(!canvasRef.value)return;initCanvas();const{width,height}=props.size;canvas.width=width*scale,canvas.height=height*scale;const fontSize=props.fontSize||Math.min(Math.floor((width-10)/props.text.length),30);ctx.fillStyle=props.bgColor,ctx.fillRect(0,0,canvas.width,canvas.height),ctx.font=`${fontSize*scale}px ${props.fontFamily}`,ctx.textBaseline="middle",ctx.textAlign="center",ctx.fillStyle="#fff";let text=props.text;props.type=="icon"&&(text?text=eval(('("&#x'+text).replace("&#x","\\u").replace(";","")+'")'):text="");const x=width/2+props.offset.x,y=height/2+props.offset.y;ctx.fillText(text,x*scale,y*scale)};watch(props,draw);const toUrl=()=>canvas.toDataURL("image/png");return onMounted(()=>draw()),__expose({toUrl}),(e,n)=>(openBlock(),createElementBlock("canvas",{class:"canvas",ref_key:"canvasRef",ref:canvasRef,style:normalizeStyle(style.value)},null,4))}},CanvasAvater=_export_sfc(_sfc_main$4,[["__scopeId","data-v-66c60425"]]),styles=`@font-face {
    font-family: "aicon";
    /* Project id 4089911 */
    src: url('//at.alicdn.com/t/c/font_4089911_odspi0n5e6k.woff2?t=1697099145811') format('woff2');
}

.aicon {
    font-family: "aicon" !important;
    font-size: 16px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}

.icon-b132:before {
    content: "\\e683";
}

.icon-b131:before {
    content: "\\e67d";
}

.icon-b130:before {
    content: "\\e67e";
}

.icon-b129:before {
    content: "\\e680";
}

.icon-b128:before {
    content: "\\e682";
}

.icon-b127:before {
    content: "\\e8cb";
}

.icon-b104:before {
    content: "\\e644";
}

.icon-b120:before {
    content: "\\e67c";
}

.icon-b49:before {
    content: "\\e7c0";
}

.icon-b119:before {
    content: "\\e675";
}

.icon-b118:before {
    content: "\\e798";
}

.icon-b117:before {
    content: "\\f274";
}

.icon-b116:before {
    content: "\\e6fd";
}

.icon-b115:before {
    content: "\\e67a";
}

.icon-b114:before {
    content: "\\e67b";
}

.icon-b113:before {
    content: "\\e707";
}

.icon-b112:before {
    content: "\\ec1e";
}

.icon-b111:before {
    content: "\\e7b4";
}

.icon-b108:before {
    content: "\\e660";
}

.icon-b110:before {
    content: "\\e7b7";
}

.icon-b109:before {
    content: "\\e671";
}

.icon-b107:before {
    content: "\\e72a";
}

.icon-jingdong:before {
    content: "\\e657";
}

.icon-b106:before {
    content: "\\e653";
}

.icon-b105:before {
    content: "\\e631";
}

.icon-a58:before {
    content: "\\e797";
}

.icon-a81:before {
    content: "\\e66d";
}

.icon-a57:before {
    content: "\\e67f";
}

.icon-b103:before {
    content: "\\e75b";
}

.icon-b102:before {
    content: "\\e62a";
}

.icon-b101:before {
    content: "\\e669";
}

.icon-b100:before {
    content: "\\e638";
}

.icon-a5:before {
    content: "\\e6c7";
}

.icon-a70:before {
    content: "\\e69f";
}

.icon-b99:before {
    content: "\\eb46";
}

.icon-a48:before {
    content: "\\e640";
}

.icon-a33:before {
    content: "\\e6fc";
}

.icon-b98:before {
    content: "\\e652";
}

.icon-b93:before {
    content: "\\e674";
}

.icon-a65:before {
    content: "\\e664";
}

.icon-b121:before {
    content: "\\e665";
}

.icon-b123:before {
    content: "\\e676";
}

.icon-b124:before {
    content: "\\e678";
}

.icon-b125:before {
    content: "\\e679";
}

.icon-b126:before {
    content: "\\e842";
}

.icon-b92:before {
    content: "\\e615";
}

.icon-b96:before {
    content: "\\e64f";
}

.icon-b91:before {
    content: "\\e65c";
}

.icon-b97:before {
    content: "\\e65d";
}

.icon-b88:before {
    content: "\\e6b6";
}

.icon-b89:before {
    content: "\\e666";
}

.icon-b90:before {
    content: "\\e667";
}

.icon-b87:before {
    content: "\\e66c";
}

.icon-b85:before {
    content: "\\e66a";
}

.icon-b86:before {
    content: "\\e9c8";
}

.icon-b94:before {
    content: "\\e66b";
}

.icon-b84:before {
    content: "\\e66e";
}

.icon-b95:before {
    content: "\\e66f";
}

.icon-b83:before {
    content: "\\e672";
}

.icon-a80:before {
    content: "\\e673";
}

.icon-b82:before {
    content: "\\e677";
}

.icon-b60:before {
    content: "\\e64a";
}

.icon-b61:before {
    content: "\\e870";
}

.icon-b62:before {
    content: "\\e65b";
}

.icon-b58:before {
    content: "\\e687";
}

.icon-b50:before {
    content: "\\e7ba";
}

.icon-b63:before {
    content: "\\e6ab";
}

.icon-b59:before {
    content: "\\e8b2";
}

.icon-b57:before {
    content: "\\e8f4";
}

.icon-b81:before {
    content: "\\e68a";
}

.icon-b64:before {
    content: "\\e646";
}

.icon-b30:before {
    content: "\\e7bf";
}

.icon-b53:before {
    content: "\\e647";
}

.icon-b54:before {
    content: "\\e63e";
}

.icon-b52:before {
    content: "\\e78c";
}

.icon-b80:before {
    content: "\\e63f";
}

.icon-b79:before {
    content: "\\e6b1";
}

.icon-b55:before {
    content: "\\e6bc";
}

.icon-b56:before {
    content: "\\e69e";
}

.icon-b78:before {
    content: "\\e63d";
}

.icon-b77:before {
    content: "\\e641";
}

.icon-b76:before {
    content: "\\e648";
}

.icon-b75:before {
    content: "\\e64c";
}

.icon-b74:before {
    content: "\\e64d";
}

.icon-b73:before {
    content: "\\e64e";
}

.icon-b72:before {
    content: "\\e650";
}

.icon-b71:before {
    content: "\\e651";
}

.icon-b70:before {
    content: "\\e654";
}

.icon-b69:before {
    content: "\\e655";
}

.icon-b68:before {
    content: "\\e656";
}

.icon-b48:before {
    content: "\\e658";
}

.icon-b47:before {
    content: "\\e659";
}

.icon-b67:before {
    content: "\\e62b";
}

.icon-b51:before {
    content: "\\e63b";
}

.icon-b65:before {
    content: "\\e63c";
}

.icon-a79:before {
    content: "\\e603";
}

.icon-a78:before {
    content: "\\e611";
}

.icon-b46:before {
    content: "\\e628";
}

.icon-b45:before {
    content: "\\e985";
}

.icon-b44:before {
    content: "\\e63a";
}

.icon-b28:before {
    content: "\\e627";
}

.icon-b27:before {
    content: "\\e645";
}

.icon-a71:before {
    content: "\\e623";
}

.icon-a72:before {
    content: "\\e629";
}

.icon-a76:before {
    content: "\\e748";
}

.icon-a73:before {
    content: "\\e634";
}

.icon-a3:before {
    content: "\\e636";
}

.icon-a75:before {
    content: "\\e637";
}

.icon-a74:before {
    content: "\\eb39";
}

.icon-b25:before {
    content: "\\e630";
}

.icon-a13:before {
    content: "\\e69d";
}

.icon-a77:before {
    content: "\\e618";
}

.icon-b26:before {
    content: "\\e663";
}

.icon-a12:before {
    content: "\\e6b3";
}

.icon-a64:before {
    content: "\\e6eb";
}

.icon-a63:before {
    content: "\\e612";
}

.icon-a21:before {
    content: "\\e622";
}

.icon-a66:before {
    content: "\\e6ba";
}

.icon-a69:before {
    content: "\\e614";
}

.icon-a62:before {
    content: "\\e62c";
}

.icon-a29:before {
    content: "\\ec68";
}

.icon-b41:before {
    content: "\\e65a";
}

.icon-a51:before {
    content: "\\e62f";
}

.icon-b43:before {
    content: "\\e6df";
}

.icon-b42:before {
    content: "\\e739";
}

.icon-b21:before {
    content: "\\e6e0";
}

.icon-a55:before {
    content: "\\e65f";
}

.icon-a68:before {
    content: "\\f467";
}

.icon-a30:before {
    content: "\\e643";
}

.icon-a31:before {
    content: "\\e69a";
}

.icon-a54:before {
    content: "\\e6c4";
}

.icon-a56:before {
    content: "\\e6e1";
}

.icon-a59:before {
    content: "\\e632";
}

.icon-a60:before {
    content: "\\e633";
}

.icon-a61:before {
    content: "\\e6bb";
}

.icon-a4:before {
    content: "\\e681";
}

.icon-a67:before {
    content: "\\e635";
}

.icon-a2:before {
    content: "\\e668";
}

.icon-a43:before {
    content: "\\e789";
}

.icon-a44:before {
    content: "\\e6fe";
}

.icon-a45:before {
    content: "\\e706";
}

.icon-a14:before {
    content: "\\e709";
}

.icon-b6:before {
    content: "\\ec66";
}

.icon-a8:before {
    content: "\\f616";
}

.icon-a7:before {
    content: "\\fd58";
}

.icon-a41:before {
    content: "\\fd5b";
}

.icon-a42:before {
    content: "\\fd65";
}

.icon-a40:before {
    content: "\\fd73";
}

.icon-a39:before {
    content: "\\fd78";
}

.icon-a46:before {
    content: "\\e74f";
}

.icon-b8:before {
    content: "\\e606";
}

.icon-b7:before {
    content: "\\e73d";
}

.icon-a23:before {
    content: "\\e65e";
}

.icon-a22:before {
    content: "\\e662";
}

.icon-b1:before {
    content: "\\e88d";
}

.icon-b3:before {
    content: "\\e88e";
}

.icon-b4:before {
    content: "\\e88f";
}

.icon-b2:before {
    content: "\\e890";
}

.icon-a15:before {
    content: "\\e893";
}

.icon-a6:before {
    content: "\\e894";
}

.icon-b34:before {
    content: "\\e6d3";
}

.icon-b37:before {
    content: "\\e6fa";
}

.icon-b36:before {
    content: "\\e6fb";
}

.icon-a47:before {
    content: "\\e639";
}

.icon-a24:before {
    content: "\\e670";
}

.icon-b11:before {
    content: "\\e617";
}

.icon-a1:before {
    content: "\\e60e";
}

.icon-a25:before {
    content: "\\ee13";
}

.icon-a9:before {
    content: "\\ee15";
}

.icon-a26:before {
    content: "\\ee16";
}

.icon-a27:before {
    content: "\\ee17";
}

.icon-a28:before {
    content: "\\ee18";
}

.icon-b12:before {
    content: "\\e60f";
}

.icon-b5:before {
    content: "\\e610";
}

.icon-b29:before {
    content: "\\e613";
}

.icon-b31:before {
    content: "\\e619";
}

.icon-a32:before {
    content: "\\e642";
}

.icon-b22:before {
    content: "\\e602";
}

.icon-b14:before {
    content: "\\e60a";
}

.icon-b16:before {
    content: "\\e60b";
}

.icon-b17:before {
    content: "\\e60c";
}

.icon-b15:before {
    content: "\\e60d";
}

.icon-b18:before {
    content: "\\e61a";
}

.icon-b19:before {
    content: "\\e61c";
}

.icon-b20:before {
    content: "\\e61d";
}

.icon-b13:before {
    content: "\\e61e";
}

.icon-a11:before {
    content: "\\e607";
}

.icon-a10:before {
    content: "\\e608";
}

.icon-a49:before {
    content: "\\e61f";
}

.icon-a16:before {
    content: "\\e620";
}

.icon-a35:before {
    content: "\\e621";
}

.icon-a20:before {
    content: "\\e604";
}

.icon-b35:before {
    content: "\\e84f";
}

.icon-b23:before {
    content: "\\e851";
}

.icon-a50:before {
    content: "\\e852";
}

.icon-a38:before {
    content: "\\e624";
}

.icon-a19:before {
    content: "\\e625";
}

.icon-a34:before {
    content: "\\e626";
}

.icon-a18:before {
    content: "\\e605";
}

.icon-b32:before {
    content: "\\e62d";
}

.icon-b10:before {
    content: "\\e62e";
}

.icon-b33:before {
    content: "\\e61b";
}

.icon-a52:before {
    content: "\\e601";
}

.icon-b9:before {
    content: "\\e600";
}

.icon-b38:before {
    content: "\\e755";
}

.icon-a53:before {
    content: "\\e649";
}

.icon-b24:before {
    content: "\\e68d";
}

.icon-a36:before {
    content: "\\e661";
}

.icon-b39:before {
    content: "\\e8db";
}

.icon-a17:before {
    content: "\\e64b";
}

.icon-b40:before {
    content: "\\e616";
}

.icon-a37:before {
    content: "\\e609";
}`,index_vue_vue_type_style_index_0_scoped_5171e641_lang="",_hoisted_1$3={class:"AvatarIcons flex"},_hoisted_2$3=["onClick"],_sfc_main$3={__name:"index",props:["value","bgColor"],emits:["update:value","font"],setup(e,{emit:n}){const r=/icon-[a-zA-Z0-9]{2,8}/g,o=/content: "\\[a-zA-Z0-9]{4}/g,i=ref(styles.match(r)),a=styles.match(o),b=e,p=n,v=computed(()=>({"background-color":b.bgColor||"#0095ff"}));onMounted(()=>{b.value||setTimeout(()=>w(i.value[0],0),100)});const w=(B,V)=>{const R=a[V].slice(11),P="aicon";p("update:value",B),p("font",{text:R,family:P})};return(B,V)=>(openBlock(),createElementBlock("div",_hoisted_1$3,[(openBlock(!0),createElementBlock(Fragment,null,renderList(i.value,(R,P)=>(openBlock(),createElementBlock("div",{class:"icon_w",key:R,style:normalizeStyle(v.value),onClick:F=>w(R,P)},[createBaseVNode("span",{class:normalizeClass(["aicon",R])},null,2)],12,_hoisted_2$3))),128))]))}},AvatarIcons=_export_sfc(_sfc_main$3,[["__scopeId","data-v-5171e641"]]),index_vue_vue_type_style_index_0_scoped_0aa434d5_lang="",_hoisted_1$2={class:"colorbar flex_b"},_hoisted_2$2=["onClick"],_hoisted_3$1={class:"picker_wrapper"},_sfc_main$2={__name:"index",props:["value"],emits:["update:value"],setup(e,{emit:n}){const r=e,o=n,i=["#ff515b","#ff7500","#ffcb00","#85d700","#00cb95","#00cadd","#0095ff","#8c9dff","#ff71a2"];onMounted(()=>{r.value||o("update:value",i[0])});const a=b=>{o("update:value",b)};return(b,p)=>(openBlock(),createElementBlock("div",_hoisted_1$2,[(openBlock(),createElementBlock(Fragment,null,renderList(i,v=>createBaseVNode("div",{class:"color_item",key:v,onClick:w=>a(v),style:normalizeStyle({"background-color":v})},null,12,_hoisted_2$2)),64)),createBaseVNode("div",_hoisted_3$1,[createVNode(unref(NColorPicker),{size:"small",value:r.value,"onUpdate:value":a},null,8,["value"])])]))}},ColorBar=_export_sfc(_sfc_main$2,[["__scopeId","data-v-0aa434d5"]]),FormAvater_vue_vue_type_style_index_0_scoped_658d02ba_lang="",_hoisted_1$1={class:"formAvater"},_hoisted_2$1={class:"preview"},_hoisted_3=["src"],_hoisted_4={key:1,class:"preview_icon"},_hoisted_5={key:2,class:"",style:{"text-align":"center"}},_hoisted_6={class:"imgAvater"},_hoisted_7={class:"iconAvater"},_hoisted_8={class:"icon_wrapper"},_hoisted_9={class:"color_wrapper"},_hoisted_10={class:"textAvater"},_hoisted_11={class:"color_wrapper"},_sfc_main$1={__name:"FormAvater",setup(e,{expose:n}){const r=inject("resize"),o=ref(!1),i=ref(null),a=ref(null),b=ref({width:100,height:100}),p=ref("img"),v=ref(""),w=ref({color:"",icon:""}),B=ref({color:"",text:"文字头像"}),V=ref({text:"",family:"aicon"}),R=N=>{V.value.text=N.text},P=N=>ae(this,null,function*(){const m=base64ToFile(N),I=new FormData;return I.append("file",m),yield uploadImg(I)}),F=N=>{o.value=N};let E=!1;const Z=()=>ae(this,null,function*(){if(E)return;E=!0;let N=v.value;const m={tab:p.value,text:B.value,icon:w.value};switch(p.value){case"img":break;case"icon":N=yield P(i.value.toUrl());break;case"text":N=yield P(a.value.toUrl());break}formData.value.avatar=N,formData.value.cssAvatar=JSON.stringify(m),E=!1,o.value=!1}),C=()=>{v.value=formData.value.avatar;const N=formData.value.cssAvatar;if(N){const{text:m,icon:I,tab:M}=JSON.parse(N);p.value="img",B.value=m,w.value=I}};return n({update:C}),onMounted(()=>{C()}),(N,m)=>(openBlock(),createElementBlock("div",_hoisted_1$1,[createVNode(unref(NPopover),{trigger:"click",placement:"bottom",show:o.value,"onUpdate:show":F},{trigger:withCtx(()=>[createBaseVNode("div",{class:"avater",style:normalizeStyle({width:b.value.width+"px",height:b.value.height+"px"})},[createBaseVNode("div",_hoisted_2$1,[p.value==="img"?withDirectives((openBlock(),createElementBlock("img",{key:0,class:"img",src:v.value},null,8,_hoisted_3)),[[vShow,v.value]]):p.value==="icon"?(openBlock(),createElementBlock("div",_hoisted_4,[createVNode(CanvasAvater,{ref_key:"iconCanvasRef",ref:i,text:V.value.text,bgColor:w.value.color,size:b.value,fontSize:b.value.width*.8,type:"icon",fontFamily:V.value.family},null,8,["text","bgColor","size","fontSize","fontFamily"])])):p.value==="text"?(openBlock(),createElementBlock("div",_hoisted_5,[createVNode(CanvasAvater,{ref_key:"textCanvasRef",ref:a,text:B.value.text,bgColor:B.value.color,size:b.value,offset:{x:0,y:1}},null,8,["text","bgColor","size"])])):createCommentVNode("",!0)]),m[6]||(m[6]=createBaseVNode("div",{class:"icon"},[createBaseVNode("span",{class:"iconfont icon-edit"})],-1))],4)]),default:withCtx(()=>[createBaseVNode("div",{class:normalizeClass(["content",{small:unref(r).smallRef.value}])},[createVNode(unref(NTabs),{type:"line",animated:"",value:p.value,"onUpdate:value":m[5]||(m[5]=I=>p.value=I)},{default:withCtx(()=>[createVNode(unref(NTabPane),{name:"img",tab:"图片头像"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_6,[createVNode(Upload,{imgs:v.value,"onUpdate:imgs":m[0]||(m[0]=I=>v.value=I),max:1,size:1024*1024*5},null,8,["imgs"]),m[8]||(m[8]=createBaseVNode("div",{class:"tips"},"请先删除后再上传新头像",-1)),createVNode(unref(NButton),{class:"nextButtom",onClick:Z,type:"info"},{default:withCtx(()=>m[7]||(m[7]=[createTextVNode(" 确认 ")])),_:1})])]),_:1}),createVNode(unref(NTabPane),{name:"icon",tab:"图标头像"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_7,[createBaseVNode("div",_hoisted_8,[createVNode(AvatarIcons,{onFont:R,value:w.value.icon,"onUpdate:value":m[1]||(m[1]=I=>w.value.icon=I),bgColor:w.value.color},null,8,["value","bgColor"])]),createBaseVNode("div",_hoisted_9,[createVNode(ColorBar,{value:w.value.color,"onUpdate:value":m[2]||(m[2]=I=>w.value.color=I)},null,8,["value"])]),createVNode(unref(NButton),{class:"nextButtom",onClick:Z,type:"info"},{default:withCtx(()=>m[9]||(m[9]=[createTextVNode(" 确认 ")])),_:1})])]),_:1}),createVNode(unref(NTabPane),{name:"text",tab:"文字头像"},{default:withCtx(()=>[createBaseVNode("div",_hoisted_10,[createVNode(unref(NInput),{class:"_input",value:B.value.text,"onUpdate:value":m[3]||(m[3]=I=>B.value.text=I),maxlength:"4","show-count":"",placeholder:""},null,8,["value"]),createBaseVNode("div",_hoisted_11,[createVNode(ColorBar,{value:B.value.color,"onUpdate:value":m[4]||(m[4]=I=>B.value.color=I)},null,8,["value"])]),createVNode(unref(NButton),{class:"nextButtom",onClick:Z,type:"info"},{default:withCtx(()=>m[10]||(m[10]=[createTextVNode(" 确认 ")])),_:1})])]),_:1})]),_:1},8,["value"])],2)]),_:1},8,["show"])]))}},FormAvater=_export_sfc(_sfc_main$1,[["__scopeId","data-v-658d02ba"]]),Edit_vue_vue_type_style_index_0_scoped_c21078b2_lang="",_hoisted_1={class:"content"},_hoisted_2={class:"fromWrapper"},_sfc_main={__name:"Edit",setup(e){const n=ref(null),r=ref(null),o=useRoute(),i=useRouter(),a=useMessage(),b=useChatStore(),p=inject("resize"),v=useGlobalStore(),B=getCurrentInstance().appContext.config.globalProperties.$bus,V=computed(()=>o.query.id),R=computed(()=>v.tags),P=ref("add"),F=computed(()=>p.IDERef.value?1:0);let E={};const Z=()=>ae(this,null,function*(){E=yield queryFriend({productType:F.value,friendId:V.value});const{avatar:d,cssAvatar:u,name:S,tag:U=R.value[0].name,intro:q,welcome:G,systemPrompt:X,contentPrompt:K,friendSource:L=defaultFormData.friendSource,conversationStart:Q=defaultFormData.conversationStart,messageContextSize:J=defaultFormData.messageContextSize,openaiRequestBody:ee=JSON.parse(JSON.stringify(defaultFormData.openaiRequestBody))}=E;formData.value={avatar:d,cssAvatar:u,name:S,tag:U,intro:q,welcome:G,systemPrompt:X,contentPrompt:K,friendSource:L,conversationStart:Q,messageContextSize:Number(J),openaiRequestBody:ee}}),C=()=>{n.value&&n.value.update(),r.value&&r.value.update()},N=()=>{formDataClear(),p.IDERef.value&&P.value=="add"?formData.value.tag="编程":formData.value&&(formData.value.tag=R.value[0].name)},m=()=>ae(this,null,function*(){V.value?(P.value="edit",N(),yield Z()):(P.value="add",N()),C()});watch(()=>o.query.id,()=>{["d_friendsPlazaEdit"].includes(o.name)&&m()}),onMounted(m),onUnmounted(formDataClear);const I=()=>{},M=()=>{routerBack(i,{name:"dialogue"})},_=d=>ae(this,null,function*(){const u=ve(be({},formData.value),{productType:F.value});let S=P.value==="edit"?yield updateFriend(ve(be({},u),{id:E.userFriendId})):yield createFriend(u);if(S.err){d(),S.errMsg&&a.error(S.errMsg||"操作失败");return}d(),N();const U=P.value==="edit"?"编辑成功":"添加成功";a.success(U),b.addFriends(S),routerBack(i,{name:"dialogue"}),B.emit("friendFollow",ve(be({},S),{__type:P.value}))});return(d,u)=>(openBlock(),createElementBlock("div",{class:normalizeClass(["FriendsPlazaEdit tpage",{small:unref(p).smallRef.value}])},[createBaseVNode("div",_hoisted_1,[createVNode(FormAvater,{ref_key:"FormAvaterRef",ref:r,onOver:I},null,512),createBaseVNode("div",_hoisted_2,[createVNode(FormComp,{ref_key:"FormCompRef",ref:n,tags:R.value,onClose:M,onConfirm:_},null,8,["tags"])])])],2))}},Edit=_export_sfc(_sfc_main,[["__scopeId","data-v-c21078b2"]]);export{Edit as default};
