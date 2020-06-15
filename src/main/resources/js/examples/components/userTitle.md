```vue
  <template>
      <div class="ma-2">
          <div v-if="userIsLoading">
              <v-progress-circular
                      id="loadCircle"
                      :size="70"
                      :width="7"
                      color="success"
                      indeterminate
              />
          </div>
          <div v-else>
              <v-avatar
                      class="mr-2"
                      size="35">
                  <img :src="loadedUser.userPic">
              </v-avatar>
              <span v-if="loadedUser.id != currentUser.id" class="text-button">{{loadedUser.username}}</span>
              <span v-else class="text-button">Your votings</span>
          </div>
      </div>
  </template>
  
  <script>
      export default {
          name: "userTitle",
          props: {
              /** user ID for fetch info **/
              userId: {
                  required: true,
                  type: [Number, String]
              }
          },
          data() {
              return {
                  loadedUser: null,
                  userIsLoading: true
              }
          },
          async created() {},
          computed: {},
          methods: {
              async loadUserData() {
                  this.loadedUser.username = "username"
              },
          }
      }
  </script>
  
  <style scoped>
  
  </style>
```