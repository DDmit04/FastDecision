```vue
  <template>
      <div>
          <v-col id="votingsBlock">
              <v-layout v-if="votings.length == 0"
                        justify-center
                        class="mt-3 display-3 font-italic">
                  Nothing Here!
              </v-layout>
              <v-card v-else
                      v-for="voting in votings"
                      :key="voting.id"
                      :id="'voting' + voting.id"
                      color="primary"
                      @click="goToVoting(voting.id)">
                  <v-card-title class="title">
                      <v-layout :id="'votingTitle' + voting.id" justify-start>
                          {{voting.votingTitle | normalizeString}}
                      </v-layout>
                      <v-layout justify-center
                                :id="'authorButton' + voting.id">
                          author:
                          <v-btn v-if="voting.owner != null"
                                 :id="'authorBtn' + voting.id"
                                 @click.stop="goToUser(voting.owner.id)"
                                 color="accent"
                                 class="ml-2">
                              {{voting.owner.username | normalizeString}}
                          </v-btn>
                          <v-btn v-else
                                 :id="'unknownAuthorBtn' + voting.id"
                                 color="accent"
                                 disabled
                                 class="ml-2">
                              {{'Unknown' | normalizeString}}
                          </v-btn>
                      </v-layout>
                      <v-layout justify-end>
                          <tooltip v-if="voting.isProtectedVoting"
                                   :id="'isProtecedVoting' + voting.id"
                                   class="mr-2"
                                   tooltipMessage="Voting is protected by key"
                                   :icon="protectedIcon"/>
                          <div :id="'totalVotes' + voting.id">
                              total votes: {{voting.totalVotes}}
                          </div>
                          <v-btn v-if="canDelete(voting)"
                                 :id="'deleteBtn' + voting.id"
                                 @click.stop="callDeleteVoting(voting)"
                                 class="ml-4"
                                 color="accent">
                              <v-icon>{{closeIcon}}</v-icon>
                          </v-btn>
                      </v-layout>
                  </v-card-title>
                  <v-divider color="secondary"></v-divider>
              </v-card>
          </v-col>
      </div>
  </template>
  
  <script>
      import {mdiClose, mdiLock} from '@mdi/js'
      import tooltip from "../../../components/tooltip.vue"
  
      export default {
          name: "abstractChart",
          props: {
              chartData: {
                  required: false,
                  default: () => ({
                     content: [
                         {
                             id: 1,
                             votingTitle: 'voting title',
                             votingOptions: [
                                 {
                                     id: 1,
                                     voteDiscription: 'disc option 1',
                                     pluses: 0
                                 },
                                 {
                                     id: 2,
                                     voteDiscription: 'disc option 2',
                                     pluses: 0
                                 }
                             ]
                         }, 
                         {
                             id: 2,
                             votingTitle: 'voting title 2',
                             votingOptions: [
                                 {
                                     id: 1,
                                     voteDiscription: 'disc option 3',
                                     pluses: 0
                                 },
                                 {
                                     id: 2,
                                     voteDiscription: 'disc option 4',
                                     pluses: 0
                                 }
                             ]
                         }
                     ]
                 })
              },
          },
          data() {
              return {
                  modalIsActive: false,
                  deletedVoting: null,
                  votings: this.chartData.content,
                  closeIcon: mdiClose,
                  protectedIcon: mdiLock,
                  currentPage: 1,
              }
          },
          components: {
              tooltip
          },
          methods: {
              goToVoting(votingId) {},
              goToUser(votingOwnerId) {},
              callDeleteVoting(voting) {
                  this.deletedVoting = voting
                  this.modalIsActive = true
              },
              canDelete(voting) {
                  return true
              },
              deleteVoting() {},
          }
      }
  </script>
  
  <style scoped>
  body {
          max-height: 110%;
          max-width: 100%;
      }
  </style>
```