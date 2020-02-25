<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Excel Solve</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@mdi/font@3.x/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify@2.x/dist/vuetify.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Material+Icons" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" rel="stylesheet"
          crossorigin="anonymous">
    <link href="/main.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>
<body>
<div id="app">
    <v-app>
        <v-container fluid>
            <v-row>
                <v-col cols="6">

                    <v-card
                            :loading="loading"
                            class="mx-auto my-12"
                            max-width="374"
                    >
                        <v-img
                                height="250"
                                src="https://cdn.vuetifyjs.com/images/cards/cooking.png"
                        ></v-img>

                        <v-card-title></v-card-title>

                        <v-card-text>
                            <v-form
                                    ref="formA"
                                    @submit.prevent="submitA"
                            >
                                <v-file-input chips label="总台账" v-model="originFileA"></v-file-input>
                                <v-file-input small-chips multiple label="分店" v-model="dataFilesA"></v-file-input>
                                <v-btn
                                        text
                                        type="submit"
                                >
                                    submit
                                </v-btn>
                            </v-form>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </v-app>
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2.x/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify@2.x/dist/vuetify.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
<script>
    new Vue({
        el: '#app',
        vuetify: new Vuetify({
            theme: {
                dark: true,
            },
        }),
        data: {
            originFileA: null,
            dataFilesA: null,
        },
        methods: {
            submitA() {
                console.log(this.originFileA)
                console.log(this.dataFilesA)

                let formData = new FormData()

                formData.append("originFileA", this.originFileA)
                console.log(formData.getAll("originFileA"))

                for (let file of this.dataFilesA) {
                    formData.append("dataFilesA", file)
                }
                console.log(formData.getAll("dataFilesA"))

                axios.post('/', formData, {responseType: 'blob'}).then(res => {
                    console.log(res)
                    if (res.status == 200) {
                        let url = window.URL.createObjectURL(new Blob([res.data], {type: res["data"]["type"]}))
                        let link = document.createElement('a')
                        link.style.display = 'none'
                        link.href = url
                        link.setAttribute('download', "xiaozhi")
                        document.body.appendChild(link)
                        link.click()
                    }
                })
            },
        },
    })

</script>
</body>
</html>