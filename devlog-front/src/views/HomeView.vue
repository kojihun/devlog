<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div>
        <router-link :to="{name: 'read', params: {postId: post.id}}">{{post.title}}</router-link>
      </div>
      <div>{{post.content}}</div>
    </li>
  </ul>
</template>

<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const posts = ref([]);

axios.get("/api/posts?page=1&size=5").then((response) => {
  response.data.forEach((result: any) => {
    posts.value.push(result);
  });
});
</script>