#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'ec-test102-remote-fetch-push.in'.
#
# UPDATED: Now automatically extracts the required commit hash from the log
# output using grep/awk, making the script fully automated.
#
# NOTE: This script will automatically delete the D1 and D2 directories
# and all their contents upon completion.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

# Set up the first repository (D1) to act as the remote
mkdir -p D1
cd D1

java -cp .. gitlet.Main init
echo "This is a wug." > f.txt
echo "This is not a wug." > g.txt
java -cp .. gitlet.Main add g.txt
java -cp .. gitlet.Main add f.txt
java -cp .. gitlet.Main commit "Two files"

# --- Automatic Hash Extraction ---
# 1. Capture the entire log output into a variable.
LOG_OUTPUT=$(java -cp .. gitlet.Main log)

# 2. Find the "Two files" commit message, get the preceding lines (-B 2),
#    isolate the 'commit <hash>' line, and extract the hash (the 2nd field).
R1_TWO_FILES_HASH=$(echo "$LOG_OUTPUT" | grep -B 2 "Two files" | grep "commit " | awk '{print $2}')
# ---------------------------------

cd ..

# Set up the second repository (D2) to act as the local
mkdir -p D2
cd D2

java -cp .. gitlet.Main init
echo "Another wug." > k.txt
java -cp .. gitlet.Main add k.txt
java -cp .. gitlet.Main commit "Add k in repo 2"
java -cp .. gitlet.Main log

# Add D1 as a remote, fetch from it, and check out its master branch
java -cp .. gitlet.Main add-remote R1 ../D1/.gitlet
java -cp .. gitlet.Main fetch R1 master
java -cp .. gitlet.Main checkout R1/master
java -cp .. gitlet.Main log
java -cp .. gitlet.Main checkout master

# Use the automatically extracted hash in the reset command.
java -cp .. gitlet.Main reset "$R1_TWO_FILES_HASH"

# Create a new commit in D2 and push it to the R1 remote
# NOTE: The content for wug3.txt was not provided, so a placeholder is used.
echo "A third wug." > h.txt
java -cp .. gitlet.Main add h.txt
java -cp .. gitlet.Main commit "Add h"
java -cp .. gitlet.Main log
java -cp .. gitlet.Main push R1 master
cd ..

# Verify that the new commit was received in D1
cd D1
java -cp .. gitlet.Main log
cd ..

# --- Cleanup Phase ---
trap - DEBUG # Disable step-by-step for cleanup
rm -rf D1
rm -rf D2