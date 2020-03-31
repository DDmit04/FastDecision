```vue
  <template>
      <div>
          <v-layout justify-center
                    class="mt-5">
              <v-card id="votingAccessForm" color="primary">
                  <v-card-title id="votingAccessTitle" class="title">
                      This voting is protected! input voting-key to access it!
                  </v-card-title>
                  <v-divider color="secondary"></v-divider>
                  <v-card-text class="ma-3">
                      <v-text-field id="votingAccessKey"
                                    v-model="votingKey"
                                    color="secondary"
                                    placeholder="voting key"/>
                  </v-card-text>
                  <v-card-actions class="pa-3">
                      <v-btn id="accessVotingBtn"
                             class="white--text"
                             color="accent"
                             @click="accessVoting()">
                          Access
                      </v-btn>
                  </v-card-actions>
              </v-card>
          </v-layout>
      </div>
  </template>
  
  <script>
      export default {
          name: "protectedVointgAccess",
          props: {
              votingId: {
                  required: false,
                  type: [String, Number],
                  default: 1
              },
          },
          data() {
              return {
                  votingKey: ""
              }
          },
          methods: {
              accessVoting() {}
          }
      }
  </script>
  
  <style scoped>
  
  </style>
```