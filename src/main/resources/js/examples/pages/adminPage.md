```vue
  <template>
      <div>
          <div v-if="currentUserIsAdmin" id="adminControlPanel">
              <v-card color="primary">
                  <v-card-title>
                      Give admin
                  </v-card-title>
                  <v-divider color="secondary"></v-divider>
                  <v-card-text>
                      <v-text-field id="giveAdminId"
                                    color="secondary"
                                    v-model="userIdToGiveAdmin"
                                    placeholder="user id"
                      />
                      <v-btn id="giveAdminBtn"
                             color="accent"
                             @click="giveAdmin()"
                      >
                          give admin
                      </v-btn>
                  </v-card-text>
              </v-card>
  
              <v-card color="primary" class="mt-3">
                  <v-card-title>
                      Remove admin
                  </v-card-title>
                  <v-divider color="secondary"></v-divider>
                  <v-card-text>
                      <v-text-field id="removeAdminId"
                                    color="secondary"
                                    v-model="userIdToRemoveAdmin"
                                    placeholder="user id"
                      />
                      <v-text-field id="removeAdminPassword"
                                    color="secondary"
                                    type="password"
                                    v-model="adminKey"
                                    placeholder="Admin password"
                      />
                      <v-btn id="removeAdminBtn"
                             color="accent"
                             @click="removeAdmin()"
                      >
                          remove admin
                      </v-btn>
                  </v-card-text>
              </v-card>
          </div>
          <div v-else id="askAdminPanel">
              <v-card color="primary">
                  <v-card-title>
                      Ask admin
                  </v-card-title>
                  <v-divider color="secondary"></v-divider>
                  <v-card-text>
                      <v-text-field id="askAdminPassword"
                                    type="password"
                                    color="secondary"
                                    v-model="adminKey"
                                    placeholder="Admin password"
                      />
                      <v-btn id="askAdminBtn"
                             color="accent"
                             @click="askAdmin()"
                      >
                          ask
                      </v-btn>
                  </v-card-text>
              </v-card>
  
          </div>
      </div>
  </template>
  
  <script>
      export default {
          name: "adminPage",
          data() {
              return {
                  adminKey: '',
                  userIdToRemoveAdmin: null,
                  userIdToGiveAdmin: null
              }
          },
          computed: {
              currentUserIsAdmin() {
                  return this.currentUser != null && this.currentUser.roles.includes('ADMIN')
              }
          },
          methods: {
              askAdmin() {
                  this.adminKey = ''
              },
              giveAdmin() {},
              removeAdmin() {}
          }
      }
  </script>
  
  <style scoped>
  
  </style>
```