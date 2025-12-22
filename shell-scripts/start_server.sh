#!/bin/bash

sudo screen -S paper-server -A bash -c '
while true; do
    START_TIME=$(date +%s)
    
    java -jar -Djava.util.concurrent.ForkJoinPool.common.parallelism=1 server.jar
    
    END_TIME=$(date +%s)
    ELAPSED_TIME=$((END_TIME - START_TIME))
    
    if [ $ELAPSED_TIME -le 20 ]; then
        echo "Server stoppte nach $ELAPSED_TIME Sekunden. Beende..."
        exit 0
    else
        echo "Server lief $ELAPSED_TIME Sekunden. Starte neu..."
        sleep 5
    fi
done
'

echo "Server wurde in der Screen-Session 'paper-server' gestartet."
