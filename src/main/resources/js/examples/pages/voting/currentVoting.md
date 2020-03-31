```vue
  <template>
      <div>
          <v-container>
              <v-col lg="8" sm="12">
                  <v-card id="currentVoting" color="primary">
                      <v-card-title id="currentVotingTitle"
                                    class="title"
                      >
                          {{currentVoting.votingTitle}}
                      </v-card-title>
                      <v-divider color="secondary"></v-divider>
                      <v-card-text class="mx-3 mt-3">
                          <div v-for="(option, index) in currentVoting.votingOptions"
                               :key="index"
                               :id="'currentVotingOption' + index"
                               class="py-2"
                          >
                              <v-row align="center">
                                  <v-col color="secondary"
                                         :id="'currentVotingOptionDiscription' + index"
                                         class="font-weight-medium"
                                  >
                                      {{option.voteDiscription}}
                                  </v-col>
                                  <v-col>
                                      <v-checkbox
                                              :off-icon="checkBoxOffIcon"
                                              :on-icon="checkBoxOnIcon"
                                              id="selectedOption"
                                              color="success"
                                              class="white--text mr-2 mt-0"
                                              hide-details
                                              :value="index == selectedOptionIndex"
                                              @click="selectedOptionIndex = index"
                                      ></v-checkbox>
                                  </v-col>
                              </v-row>
                              <v-divider color="secondary"></v-divider>
                          </div>
                      </v-card-text>
                      <v-card-actions class="pa-3">
                          <v-btn id="doVoteBtn"
                                 class="white--text"
                                 color="accent"
                                 :x-large="true"
                                 :disabled="selectedOptionIndex == -1"
                                 @click="doVote()"
                          >
                              vote!
                          </v-btn>
                          <v-btn id="goToResultsBtn"
                                 class="white--text"
                                 color="secondary"
                                 :x-large="true"
                                 @click="goToResults()">
                              results
                          </v-btn>
                      </v-card-actions>
                  </v-card>
              </v-col>
          </v-container>
      </div>
  </template>
  
  <script>
    import {mdiCheckboxBlankOutline, mdiCheckBoxOutline } from '@mdi/js'

      export default {
          name: "currentVoting",
          props: {
              votingId: {
                  required: false,
                  type: [String, Number],
                  default: 0
              },
              votingKey: {
                  required: false,
                  type: String,
                  default: 'public'
              },
          },
          data() {
              return {
                  checkBoxOffIcon: mdiCheckboxBlankOutline,
                  checkBoxOnIcon: mdiCheckBoxOutline,
                  currentVoting: null,
                  selectedOptionIndex: -1,
                  modifiedVotingKey: this.votingKey,
              }
          },
          created() {
              this.currentVoting = {
                  votingTitle: 'voting title',
                  votingOptions: [
                      {
                          id: 1,
                          voteDiscription: 'disc option 2',
                          pluses: 0
                      },
                      {
                          id: 2,
                          voteDiscription: 'disc option 2',
                          pluses: 0
                      }
                  ]
              }
          },  
          computed: {
              votingLink() {
                  let origin = window.location.origin
                  let path = ""
                  return origin + path + '?votingKey=' + this.modifiedVotingKey
              }
          },
          methods: {
              doVote() {},
              goToResults() {}
          }
      }
  </script>
  
  <style scoped>
  </style>
```