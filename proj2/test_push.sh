#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'ec-test102-remote-fetch-push.in'.
#
# Test Description: Sets up two repositories to test `fetch`, `reset` to a
# remote commit, and `push`. It automatically extracts the necessary commit
# hash and cleans up all created directories.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.

## Part 1: Set up the first repository (D1) to act as the remote
mkdir -p D1
cd D1

# Run gitlet commands from within D1 using a classpath pointing to the parent dir
java -cp .. gitlet.Main init
echo "This is a wug." > f.txt
echo "This is not a wug." > g.txt
java -cp .. gitlet.Main add g.txt
java -cp .. gitlet.Main add f.txt
java -cp .. gitlet.Main commit "Two files"

# --- Automatic Hash Extraction ---
# Capture the log output to find the commit hash for "Two files".
LOG_OUTPUT=$(java -cp .. gitlet.Main log)
R1_TWO_FILES_HASH=$(echo "$LOG_OUTPUT" | grep -B 2 "Two files" | grep "commit " | awk '{print $2}')
# ---------------------------------

cd ..

## Part 2: Set up the second repository (D2) and perform remote operations
mkdir -p D2
cd D2

java -cp .. gitlet.Main init
echo "Another wug." > k.txt
java -cp .. gitlet.Main add k.txt
java -cp .. gitlet.Main commit "Add k in repo 2"
java -cp .. gitlet.Main log

# Add D1 as a remote, fetch from it, and reset to its state.
java -cp .. gitlet.Main add-remote R1 ../D1/.gitlet
java -cp .. gitlet.Main fetch R1 master
java -cp .. gitlet.Main checkout R1/master
java -cp .. gitlet.Main log
java -cp .. gitlet.Main checkout master

# Use the automatically extracted hash in the reset command.
java -cp .. gitlet.Main reset "$R1_TWO_FILES_HASH"

# Add a new commit in D2 and push it to R1.
echo "And yet another wug." > h.txt
java -cp .. gitlet.Main add h.txt
java -cp .. gitlet.Main commit "Add h"
java -cp .. gitlet.Main log
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

java -cp .. gitlet.Main push R1 master
cd ..

## Part 3: Verify the push in the first repository (D1)
cd D1
java -cp .. gitlet.Main log
cd ..

## Part 4: Cleanup
trap - DEBUG # Disable step-by-step for cleanup
rm -rf D1
rm -rf D2