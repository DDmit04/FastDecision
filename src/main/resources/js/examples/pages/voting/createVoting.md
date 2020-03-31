```vue
  <template>
      <div>
          <v-container @keyup.enter="tryAddVoting()">
              <v-col lg="8" sm="12">
                  <v-card id="createVotingForm" color="primary">
                      <v-card-title>
                          <v-text-field id="newVotingTitle"
                                        v-model="newVoting.votingTitle"
                                        color="secondary"
                                        placeholder="Vote title"
                          />
                      </v-card-title>
                      <v-card-text>
                          <v-text-field v-for="(option, index) in newVoting.votingOptions" :key="index"
                                        v-model="option.voteDiscription"
                                        color="secondary"
                                        placeholder="Some option"
                                        :id="'newVotingOption' + index"
                                        @click="checkOptionCount(index)"
                                        @focus="checkOptionCount(index)"
                          />
                          <v-row>
                              <v-checkbox v-model="newVoting.isPrivateVoting"
                                          :off-icon="checkBoxOffIcon"
                                          :on-icon="checkBoxOnIcon"
                                          label="private voting"
                                          class="mx-2"
                                          id="newIsPrivateVoting"
                                          color="success"
                              />
                              <tooltip class="mt-5"
                                       tooltipMessage="Hide from charts and other users"
                                       :icon="alertIcon" />
                          </v-row>
                          <v-row>
                              <v-checkbox v-model="newVoting.isProtectedVoting"
                                          :off-icon="checkBoxOffIcon"
                                          :on-icon="checkBoxOnIcon"
                                          label="protected voting"
                                          class="mx-2"
                                          id="newIsProtectedVoting"
                                          color="success"
                              />
                              <tooltip class="mt-5"
                                       tooltipMessage="Protect voting by key"
                                       :icon="alertIcon"></tooltip>
                          </v-row>
                      </v-card-text>
                      <v-divider color="secondary"></v-divider>
                      <v-card-actions class="pa-3">
                          <v-btn id="addVotingBtn"
                                 class="white--text"
                                 color="accent"
                                 @click="tryAddVoting()"
                                 :disabled="!voteReadyToAdd"
                                 :block=true
                                 :loading="buttonLoading">
                              add voting
                          </v-btn>
                      </v-card-actions>
                  </v-card>
              </v-col>
          </v-container>
      </div>
  </template>
  
  <script>
      import {mdiAlertCircleOutline, mdiCheckboxBlankOutline, mdiCheckBoxOutline} from '@mdi/js'
      import tooltip from "../../../components/tooltip.vue"
  
      export default {
          name: "createVoting",
          data() {
              return {
                  alertIcon: mdiAlertCircleOutline,
                  checkBoxOffIcon: mdiCheckboxBlankOutline,
                  checkBoxOnIcon: mdiCheckBoxOutline,
                  buttonLoading: false,
                  lastOptionIndex: 1,
                  newVoting: {
                      id: null,
                      votingTitle: '',
                      votingOptions: [],
                      isPrivateVoting: false,
                      isProtectedVoting: false
                  },
                  voteOption1: {
                      id: null,
                      voteDiscription: '',
                      pluses: 0,
                  },
                  voteOption2: {
                      id: null,
                      voteDiscription: '',
                      pluses: 0,
                  }
              }
          },
          components: {
              tooltip
          },
          created() {
              this.newVoting.votingOptions.push(this.voteOption1)
              this.newVoting.votingOptions.push(this.voteOption2)
          },
          computed: {
              voteReadyToAdd() {
                  let isReady = false
                  let counter = 0
                  this.newVoting.votingOptions.forEach(opt => {
                      if (opt.voteDiscription.trim() != '') {
                          counter++
                      }
                  })
                  isReady = this.newVoting.votingTitle.trim() != '' && counter >= 2
                  return isReady
              }
          },
          methods: {
              checkOptionCount(index) {
                  if (index == this.lastOptionIndex) {
                      this.addOption()
                      this.lastOptionIndex++
                  }
              },
              tryAddVoting() {
                  if (this.voteReadyToAdd) {
                      this.addVoting()
                  }
              },
              addVoting() {},
              addOption() {
                  if (this.newVoting.votingOptions.length < 10) {
                      let newOption = {
                          id: '',
                          voteDiscription: '',
                          pluses: 0,
                      }
                      this.newVoting.votingOptions.push(newOption)
                  }
              },
          }
      }
  </script>
  
  <style scoped>
  
  </style>
```