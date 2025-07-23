#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'ec-test104-bad-remotes-err.in'.
#
# Test Description: Tests various error conditions for remote commands like
# add-remote, fetch, push, and rm-remote.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

# === Part 1: Set up the remote repository (D1) ===
mkdir -p D1
cd D1

java -cp .. gitlet.Main init
echo "This is a wug." > f.txt
echo "This is not a wug." > g.txt
java -cp .. gitlet.Main add g.txt
java -cp .. gitlet.Main add f.txt
java -cp .. gitlet.Main commit "Two files"
java -cp .. gitlet.Main log
cd ..

# === Part 2: Set up the local repository (D2) and test error cases ===
mkdir -p D2
cd D2

java -cp .. gitlet.Main init
echo "Another wug." > k.txt
java -cp .. gitlet.Main add k.txt
java -cp .. gitlet.Main commit "Add k in repo 2"
java -cp .. gitlet.Main log

# --- Test various remote error conditions ---
java -cp .. gitlet.Main add-remote R1 ../Dx/.gitlet
java -cp .. gitlet.Main add-remote R1 ../D1/.gitlet
java -cp .. gitlet.Main fetch R1 master
java -cp .. gitlet.Main push R1 master
java -cp .. gitlet.Main rm-remote R1
java -cp .. gitlet.Main rm-remote glorp
java -cp .. gitlet.Main add-remote R1 ../D1/.gitlet
java -cp .. gitlet.Main fetch R1 glorp
java -cp .. gitlet.Main push R1 master
cd ..

# --- Cleanup Phase ---
trap - DEBUG
rm -rf D1
rm -rf D2