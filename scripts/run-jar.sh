#! /usr/bin/env bash

REMOTE_JAR="$1"
LOCAL_JAR=$(basename $REMOTE_JAR)

shift 1

echo "Downloading $REMOTE_JAR -> $LOCAL_JAR"
[ -f "$LOCAL_JAR" ] || aws s3 cp $REMOTE_JAR $LOCAL_JAR
[ -f "$LOCAL_JAR" ] || exit 3

echo "Running jar"
echo java -cp $LOCAL_JAR $@ > runner.sh
sh runner.sh

