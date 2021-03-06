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
                          <transition-group name="fade">
                              <v-text-field v-for="(option, index) in newVoting.votingOptions"
                                            :key="option.voteDiscription + index"
                                            v-model="option.voteDiscription"
                                            color="secondary"
                                            placeholder="Some option"
                                            :id="'newVotingOption' + index"
                                            @click="checkOptionCount(index)"
                                            @focus="checkOptionCount(index)"
                              />
                          </transition-group>
                          <v-row>
                              <v-checkbox v-model="newVoting.isPrivateVoting"
                                          :off-icon="checkBoxOffIcon"
                                          :on-icon="checkBoxOnIcon"
                                          label="private voting"
                                          class="mx-2"
                                          disabled
                                          id="newIsPrivateVoting"
                                          color="success"
                              />
                              <tooltip class="mt-5"
                                       :tooltipMessage="privateVotingTooltipMessage"
                                       :icon="alertIcon"/>
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
                                       :tooltipMessage="protectedVotingTooltipMessage"
                                       :icon="alertIcon"></tooltip>
                          </v-row>
                          <v-row>
                              <v-checkbox v-model="newVoting.isCheckingIpVoting"
                                          :off-icon="checkBoxOffIcon"
                                          :on-icon="checkBoxOnIcon"
                                          label="votes IP check"
                                          class="mx-2"
                                          id="newIsCheckingIpVoting"
                                          color="success"
                              />
                              <tooltip class="mt-5"
                                       :tooltipMessage="ipCheckVotingTooltipMessage"
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
                  privateVotingTooltipMessage: 'Hide from charts and other users (auth users only)',
                  protectedVotingTooltipMessage: 'Protect voting by key',
                  ipCheckVotingTooltipMessage: 'Accept only one vote per IP',
                  buttonLoading: false,
                  lastOptionIndex: 1,
                  newVoting: {
                      id: null,
                      votingTitle: '',
                      votingOptions: [],
                      isPrivateVoting: false,
                      isProtectedVoting: false,
                      isCheckingIpVoting: false
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
              tryAddVoting() {},
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
    .fade-enter-active, .fade-leave-active {
        transition: opacity .5s;
    }

    .fade-enter, .fade-leave-to {
        opacity: 0;
    }
  </style>
```