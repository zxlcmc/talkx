<template>
  <div :id="galleryID" ref="wrapper" class="SimpleGallery">
    <!-- <div class="cloums" v-for="(cloum, i) in columnDat" :key="i"> -->
    <div
      class="pswp-gallery__item"
      v-for="(image, key) in props.images"
      :key="key"
      :data-id="image._id"
      @click="itemClick(image)"
    > 
      <a
        @click.prevent=""
        :href="image.src"
        :data-pswp-width="image.width"
        :data-pswp-height="image.height"
        target="_blank"
        rel="noreferrer"
      >
        <img :src="image.thumbnailURL" alt="" />
      </a>
      <div class="pswp-caption-content">
        {{ image.userPrompt }}
      </div>
    </div>
    <!-- </div> -->
  </div>
</template>

<script setup>
import "photoswipe/style.css";
import PhotoSwipeLightbox from "photoswipe/lightbox";
import "photoswipe-dynamic-caption-plugin/photoswipe-dynamic-caption-plugin.css";
import PhotoSwipeDynamicCaption from "photoswipe-dynamic-caption-plugin";
import { ref, inject, computed, onMounted, onUnmounted } from "vue";

let lightbox;
const resize = inject("resize");
const props = defineProps({
  galleryID: String,
  images: Array,
});

const base = 160;
const wrapper = ref(null);
const flexBasis = ref(base);
const column = ref(1);

const columnDat = computed(() => {
  const arr = [];
  for (let i = 0; i < column.value; i++) {
    arr.push({ h: 0, images: [] });
  }
  const getMinH = () => {
    let initV = Infinity,
      mindex;
    for (let i = 0; i < arr.length; i++) {
      if (arr[i].h < initV) {
        initV = arr[i].h;
        mindex = i;
      }
    }
    const minHObj = arr[mindex];
    return { mobj: minHObj, i: mindex };
  };
  props.images.forEach((obj) => {
    const { height = 300, width = 300 } = obj;
    const bl = width / 150;
    const { mobj } = getMinH();
    mobj.h += height / bl + 10;
    mobj.images.push(obj);
  });

  arr.forEach(({ images }, i) => {
    images.forEach((obj, j) => {
      obj._id = i + column.value * j;
    });
  });

  return arr;
});

const resizeFun = () => {
  if (!wrapper.value) return;
  const w = parseInt(window.getComputedStyle(wrapper.value, null)["width"]);
  column.value = Math.floor(w / base);
  const baseW = Math.floor(w / column.value);
  flexBasis.value = baseW;
};

const itemClick = ()=>{}

onMounted(() => { 
  if (!lightbox) {
    const options = {
      dataSource: props.images,
      gallery: "#" + props.galleryID,
      childSelector: ".pswp-gallery__item",
      pswpModule: () => import("photoswipe"),
      paddingFn: (viewportSize) => {
        // 滑动区域填充
        return { top: 30, bottom: 30, left: 70, right: 70 };
      },
    };
    lightbox = new PhotoSwipeLightbox(options);
    const captionPlugin = new PhotoSwipeDynamicCaption(lightbox, {
      mobileLayoutBreakpoint: 700,
      type: "auto",
      mobileCaptionOverlapRatio: 1,
    });
    lightbox.init();
  }
  // resizeFun();
  // resize.change(resizeFun);
});

onUnmounted(() => {
  lightbox.destroy();
  lightbox = null;
});
</script>

<style lang="scss" scoped>
.SimpleGallery {
  display: flex;
  /* justify-content: space-evenly; */
  flex-wrap: wrap;
  .cloums {
    display: flex;
    padding: 10px 0;
    flex-direction: column;
  }

  .pswp-gallery__item {
    max-width: 300px;
    color: #fff; 
    margin-right: 10px;
    a {
      text-align: center;
      padding-bottom: 10px;
      display: inline-block;
      img {
        max-width: 300px;
        border-radius: 5px;
        transform: scale(1);
        transition: all 0.2s;
      }
      &:hover img {
        transform: scale(1.05);
        /* box-shadow: #fff 0 0 5px 1px; */
      }
    }
    .pswp-caption-content {
      font-size: 14px;
    }
  }
}
</style>
