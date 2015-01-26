#!/bin/bash

if [ ! -d target ]; then
    mkdir target;
fi

jmeter -n -t sortJsonAlternativeTreeSet.jmx -j jmeter.log -p jmeter.properties | tee target/sortJsonAlternativeTreeSet-tune.log

jmeter -n -t sortJsonAlternativeTreeMap.jmx -j jmeter.log -p jmeter.properties | tee target/sortJsonAlternativeTreeMap-tune.log

