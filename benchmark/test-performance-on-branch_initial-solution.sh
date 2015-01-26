#!/bin/bash

if [ ! -d target ]; then
    mkdir target;
fi

jmeter -n -t sortJson.jmx -j jmeter.log -p jmeter.properties | tee target/sortJson.log

jmeter -n -t sortJsonBlock.jmx -j jmeter.log -p jmeter.properties | tee target/sortJsonBlock.log

