#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'ec-test103-remote-fetch-pull.in'.
#
# Test Description: Tests the `pull` command by setting up a remote (D1)
# and a local (D2) repository, making divergent commits, and pulling.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

# === Part 1: Set up the first repository (D1) ===
mkdir -p D1
cd D1

java -cp .. gitlet.Main init
echo "This is a wug." > f.txt
echo "This is not a wug." > g.txt
java -cp .. gitlet.Main add g.txt
java -cp .. gitlet.Main add f.txt
java -cp .. gitlet.Main commit "Two files"

# Automatically extract the hash for the "Two files" commit.
LOG_OUTPUT=$(java -cp .. gitlet.Main log)
R1_TWO_FILES_HASH=$(echo "$LOG_OUTPUT" | grep -B 2 "Two files" | grep "commit " | awk '{print $2}')
cd ..

# === Part 2: Set up the second repository (D2) ===
mkdir -p D2
cd D2

java -cp .. gitlet.Main init
java -cp .. gitlet.Main add-remote R1 ../D1/.gitlet
java -cp .. gitlet.Main fetch R1 master

java -cp .. gitlet.Main reset "$R1_TWO_FILES_HASH"

echo "And yet another wug." > h.txt
java -cp .. gitlet.Main add h.txt
java -cp .. gitlet.Main commit "Add h"
cd ..

# === Part 3: Add a new commit to the remote (D1) ===
cd D1
echo "Another wug." > k.txt
java -cp .. gitlet.Main add k.txt
java -cp .. gitlet.Main commit "Add k"
cd ..

# === Part 4: Pull the new commit from D1 into D2 ===
cd D2

java -cp .. gitlet.Main pull R1 master
java -cp .. gitlet.Main log
cd ..

# --- Cleanup Phase ---
trap - DEBUG
rm -rf D1
rm -rf D2