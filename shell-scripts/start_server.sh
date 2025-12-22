#!/bin/bash
#
# Copyright 2025 Tom Coombs
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

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
