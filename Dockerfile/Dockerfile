# This dockerfile uses the gradle image
# VERSION 1
# Author: wade

FROM gradle

MAINTAINER wade wadechen@outlook.com

RUN git clone https://github.com/wadeee/luckinx.git ~/luckinx\
  && cd ~/luckinx\
  && gradle bootJar

CMD java -jar ~/luckinx/build/libs/luckinx-1.0.jar --server.port=3001
