#!/bin/bash

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