#!/usr/bin/env bash

echo "Provisioning android-dev VM"


# create environment variables from name/value pair arguments
while test $# -gt 0
do
    NAME=$1
    shift
    if [ $# -gt 0 ]
      then
        echo "export $NAME=$1" | tee -a /home/vagrant/.bash_profile
    fi
    shift
done

source /home/vagrant/.bash_profile

# install requried SDK components
( sleep 5 && while [ 1 ]; do sleep 1; echo y; done ) | $ANDROID_HOME/tools/android update sdk --no-ui --filter \
 "build-tools-20.0.0, android-19, extra-android-support, extra-android-m2repository, extra-google-m2repository"

