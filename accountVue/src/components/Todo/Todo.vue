<template>
  <div id="todos">
    Todods
    <div
      class="todo"
      v-for="todo in todos"
      v-bind:key="todo.id"
      v-bind:class="{ 'is-complete': todo.completed }"
    >
    <input type="checkbox" @click="todo.completed = !todo.completed"/>
      {{ todo.title }}
    </div>
  </div>
</template>

<script>
export default {
  name: "Todos",
  data() {
    return {
      todos: [
        {
          id: 1,
          title: "todo one",
          completed: false,
        },
        {
          id: 2,
          title: "todo two",
          completed: false,
        },
        {
          id: 3,
          title: "todo three",
          completed: true,
        },
      ],
    };
  },
};
</script>

<style>
.is-complete {
  text-decoration: line-through;
}
</style>
