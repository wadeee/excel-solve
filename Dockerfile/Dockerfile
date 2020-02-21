# This dockerfile uses the gradle image
# VERSION 1
# Author: wade

FROM gradle

MAINTAINER wade wadechen@outlook.com

RUN git clone https://github.com/wadeee/luckinx.git ~/luckinx\
  && cd ~/luckinx\
  && gradle build

CMD cd ~/luckinx\
  && gradle bootRun
