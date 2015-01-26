#!/bin/bash

if [ ! -d target ]; then
    mkdir target;
fi

jmeter -n -t index.jmx -j jmeter.log -p jmeter.properties | tee /dev/null

jmeter -n -t sortJsonAlternativeTreeSet.jmx -j jmeter.log -p jmeter.properties | tee target/sortJsonAlternativeTreeSet-tune.log

jmeter -n -t index.jmx -j jmeter.log -p jmeter.properties | tee /dev/null

jmeter -n -t sortJsonAlternativeTreeMap.jmx -j jmeter.log -p jmeter.properties | tee target/sortJsonAlternativeTreeMap-tune.log

