<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Excel Solve</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@mdi/font@3.x/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify@2.x/dist/vuetify.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Material+Icons" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" rel="stylesheet" crossorigin="anonymous">
    <link href="/main.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
</head>
<body>
<div id="app">
    <v-app>
        <v-container fluid>
            <v-row>
                <v-col cols="12">
                    <v-row justify="center">
                        <v-col cols="6" md="4">
                            <v-form class="md-form" action="/" method="post" enctype="multipart/form-data">
                                <div class='file-input'>
                                    <input type='file' name="originFile">
                                    <span class='button'>总台账</span>
                                    <span class='label' data-js-label>No file selected</span>
                                </div>
                                <div v-for="i in 50" class='file-input' :class="{'d-none': i > cnt}">
                                    <input type='file' name="dataFiles" :disabled="i > cnt">
                                    <span class='button'>分店</span>
                                    <span class='label' data-js-label>No file selected</span>
                                </div>
                                <v-row>
                                    <v-col cols="12" sm="6" class="text-center">
                                        <v-btn fab dark color="indigo" @click="addFile">
                                            <v-icon dark>add</v-icon>
                                        </v-btn>
                                    </v-col>
                                    <v-col cols="12" sm="6" class="text-center">
                                        <v-btn fab dark color="indigo" @click="removeFile">
                                            <v-icon dark>remove</v-icon>
                                        </v-btn>
                                    </v-col>
                                </v-row>
                                <v-btn color="cyan" class="mt-2" block type="submit">submit</v-btn>
                            </v-form>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
        </v-container>
    </v-app>
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2.x/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify@2.x/dist/vuetify.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<script>
    new Vue({
        el: '#app',
        vuetify: new Vuetify(),
        data: {
            cnt: 1
        },
        methods: {
            addFile() {
                this.cnt++;
            },
            removeFile() {
                if (this.cnt > 1) {
                    this.cnt--;
                }
            }
        },
        watch: {
            cnt() {
                $("[name='dataFiles']").get(this.cnt).closest(".v-input").remove()
            },
            originFile() {
                console.log(this.originFile)
            },
            dataFiles() {
                console.log(this.dataFiles)
            }
        }
    })

    var inputs = document.querySelectorAll('.file-input')

    for (var i = 0, len = inputs.length; i < len; i++) {
        customInput(inputs[i])
    }

    function customInput (el) {
        const fileInput = el.querySelector('[type="file"]')
        const label = el.querySelector('[data-js-label]')

        fileInput.onchange =
            fileInput.onmouseout = function () {
                if (!fileInput.value) return

                var value = fileInput.value.replace(/^.*[\\\/]/, '')
                el.className += ' -chosen'
                label.innerText = value
            }
    }
</script>
</body>
</html>