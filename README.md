# Excel数据处理应用
[![license-MIT](https://img.shields.io/badge/license-MIT-green?style=flat-square)](https://github.com/wadeee/luckinx/blob/master/LICENSE)
[![build](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)](https://github.com/wadeee/luckinx)


解决生产中的重复数据处理问题

## Docker下运行

+ 进入Dockerfile目录，构建image并查看镜像是否生成

    ```bash
    docker build -t luckinx ./Dockerfile
    docker images
    ```

+ 生成并运行container映射3001端口到3001端口（3001端口可以任意更改）

    ```bash
    docker run -itd -p 3001:3001 --name=luckinx luckinx
    ```

+ 打开[http://localhost:3000/]

    - 没有浏览器的情况下可以用curl打开查看是否正常运行
  
        ```shell script
        curl http://localhost:3001/
        ```

+ 使用完后删除image和container

    ```bash
    docker stop luckinx && docker rm luckinx && docker image rm luckinx
    ```

## 使用技术

+ [Apache POI](https://poi.apache.org/)

+ [CoffeeScript](https://coffeescript.org/)

+ [Spring](https://spring.io/)

+ [FreeMarker](https://freemarker.apache.org/)

