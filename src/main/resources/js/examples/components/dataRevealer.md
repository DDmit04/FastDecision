```vue
  <template>
      <v-card color="primary">
          <div v-if="!dataIsReviled"id="hidenData">
              <v-btn id="revealDataBtn"
                     class="white--text"
                     color="accent"
                     block
                     @click="revealData()"
              >
                  show voting link
              </v-btn>
          </div>
          <div v-else id="reveledData">
              <v-card-text>
                  <v-col color="secondary">
                      <v-row align="center">
                          <v-col cols="10">
                              <v-text-field id="data"
                                            v-model="dataToReveal"
                                            ref="textToCopy"
                              />
                          </v-col>
                          <v-col cols="2">
                              <v-btn id="copyDataBtn"
                                     color="secondary"
                                     @click="copyData()">
                                  copy
                              </v-btn>
                          </v-col>
                      </v-row>
                  </v-col>
              </v-card-text>
              <v-card-actions>
                  <v-btn id="hideDataBtn"
                         class="white--text"
                         color="accent"
                         block
                         @click="hideData()">
                      hide voting link
                  </v-btn>
              </v-card-actions>
          </div>
      </v-card>
  </template>
  
  <script>
      export default {
          name: "dataRevealer",
          props: {
              dataToReveal: {
                  required: false,
                  type: [String, Number],
                  default: "No data to reveal"
              }
          },
          data() {
              return {
                  dataIsReviled: false
              }
          },
          methods: {
              revealData() {
                  this.dataIsReviled = true
              },
              hideData() {
                  this.dataIsReviled = false
              },
              copyData() {
                  let textToCopy = this.$refs.textToCopy.$el.querySelector('input')
                  textToCopy.select()
                  document.execCommand("copy");
              }
          }
      }
  </script>
  
  <style scoped>
  
  </style>
```