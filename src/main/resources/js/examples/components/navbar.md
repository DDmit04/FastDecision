```vue
  <template>
      <div>
      <v-app-bar color="info">
          <v-toolbar-items>
              <v-btn id="mainPageBtn"
                     text
                     @click="goToOtherRote('')">
                  Fast Decision
              </v-btn>
              <v-btn id="switchThemeBtn"
                     text
                     @click="switchTheme()">
                  <div v-if="getIsDark">
                      <v-icon>{{sunIcon}}</v-icon>
                  </div>
                  <div v-else>
                      <v-icon>{{moonIcon}}</v-icon>
                  </div>
              </v-btn>
          </v-toolbar-items>
          <v-spacer></v-spacer>
          <v-text-field id="votingSearch"
                        class="mt-4 mr-4"
                        color="secondary"
                        v-model="search"
                        clearable
                        placeholder="Search by title..."
          />
          <v-btn id="searchBtn"
                 class="mr-2"
                 outlined
                 :disabled="search.length == 0"
                 @click="goToSearch()">
              <v-icon>{{searchIcon}}</v-icon>
          </v-btn>
          <v-toolbar-items>
              <v-btn id="newestVotingsBtn"
                     text
                     @click="goToOtherRote('')">
                  Newest
              </v-btn>
              <v-btn id="popularVotingsBtn"
                     text
                     @click="goToOtherRote('')">
                  Popular
              </v-btn>
              <v-btn id="userVotingsBtn"
                     text
                     @click="goToUserVotings()">
                  <div id="userAvatar">
                      <v-avatar v-if="getCurrentUserPic == null"
                                class="mr-2"
                                color="success"
                                size="35">
                          U
                      </v-avatar>
                      <v-avatar v-else
                                class="mr-2"
                                size="35">
                          <img :src="getCurrentUserPic">
                      </v-avatar>
                  </div>
                  {{getCurrentUsername}}
              </v-btn>
          </v-toolbar-items>
      </v-app-bar>
      </div>
  </template>
  
  <script>
      import {mdiLoginVariant, mdiMagnify, mdiMoonWaningCrescent, mdiWhiteBalanceSunny} from '@mdi/js'

      export default {
          name: "navbar",
          data() {
              return {
                  signInIcon: mdiLoginVariant,
                  sunIcon: mdiWhiteBalanceSunny,
                  moonIcon: mdiMoonWaningCrescent,
                  searchIcon: mdiMagnify,
  
                  modalIsActive: false,
                  search: ''
              }
          },
          computed: {
              getCurrentUsername() {
                  if (this.currentUser == null) {
                      return 'Unknown'
                  } else {
                      return this.currentUser.username
                  }
              },
              getCurrentUserPic() {
                  if (this.currentUser == null) {
                      return null
                  } else {
                      return this.currentUser.userPic
                  }
              },
              getIsDark() {
                  return this.isDarkTheme
              }
          },
          methods: {
              switchTheme() {
                  this.$vuetify.theme.dark = !this.$vuetify.theme.dark
              },
              goToOtherRote(roteName) {},
              goToUserVotings() {},
              goToSearch() {}
          }
      }
  </script>
  
  <style scoped>
  
  </style>
```