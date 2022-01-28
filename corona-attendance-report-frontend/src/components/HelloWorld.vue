<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <p>Token:</p>
    <div>{{ token }}</div>
    <p>Refresh Token:</p>
    <div>{{ refreshToken }}</div>

    <br>
    <input id="teamName" type="text" placeholder="Team Name" v-model="teamName">
    <button @click="createTeam">Send</button>

    <br>
    <p>Teams:</p>
    <ul>
      <li v-for="team in this.teamList" :key="team.id">{{team.name}}</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String,
  },
  data: () => ({
    teamName: '',
    teamList: [],
  }),
  mounted() {
    this.getTeamList();
  },
  computed: {
    token() {
      return localStorage.getItem('vue-token');
    },
    refreshToken() {
      return localStorage.getItem('vue-refresh-token');
    },
  },
  methods: {
    createTeam() {
      this.$axios
        .post('http://localhost:8089/teams', {
          name: this.teamName,
        })
        .then((response) => {
          this.teamList.push(response.data);
          this.teamName = '';
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getTeamList() {
      this.$axios
        .get('http://localhost:8089/teams')
        .then((response) => {
          this.teamList = response.data;
        }).catch((error) => {
          console.error(error);
        });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
