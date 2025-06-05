<template>
  <form @submit.prevent="submit">
    <input type="text"  v-model="title" />
    <input type="submit" value="Add" />

  </form>
</template>

<script>
export default {
    name: 'AddTodo',
    data(){
       return{
           title: ''
       }
    },
    method: {
        submit(){
            const todo = {
                id:4,
                title: this.title,
                completed: false
            }
        }
    }
}
</script>

<style>
form{
    display: flex;
}
input[type="text"]{
    flex: 10;
    border: 1px solid #ccc;
    padding: 4px;
}
input[type="submit"]{
    flex: 2;
    border: 1px solid #ccc
}


</style>