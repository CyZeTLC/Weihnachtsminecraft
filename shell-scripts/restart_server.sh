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

SCREEN_NAME="paper-server"

if screen -list | grep -q "$SCREEN_NAME"; then
    echo "Stopping Minecraft server..."
    screen -S "$SCREEN_NAME" -X stuff "say §eServer restartet in 1 Minute!\n"
    sleep 50
    screen -S "$SCREEN_NAME" -X stuff "say §cRestart in 10 Sekunden!\n"
    sleep 10
    screen -S "$SCREEN_NAME" -X stuff "stop\n"
else
    echo "Screen session nicht gefunden."
fi