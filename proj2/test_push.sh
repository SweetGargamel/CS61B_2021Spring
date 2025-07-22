#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'ec-test102-remote-fetch-push.in'.
#
# UPDATED: To run 'java' from subdirectories, this script uses the '-cp ..'
# flag. This tells Java to look for the .class files in the parent directory
# (where the script is run from), while allowing commands to operate within
# the D1 and D2 repository directories.
#
# NOTE: This script will automatically delete the D1 and D2 directories
# and all their contents upon completion.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.

# Set up the first repository (D1) to act as the remote
mkdir -p D1
cd D1

# Run gitlet commands from within D1 using a classpath pointing to the parent dir
java -cp .. gitlet.Main init
echo "This is a wug." > f.txt
echo "This is not a wug." > g.txt
java -cp .. gitlet.Main add g.txt
java -cp .. gitlet.Main add f.txt
java -cp .. gitlet.Main commit "Two files"
java -cp .. gitlet.Main log
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
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

java -cp .. gitlet.Main add-remote R1 ../D1/.gitlet
java -cp .. gitlet.Main fetch R1 master
java -cp .. gitlet.Main checkout R1/master
java -cp .. gitlet.Main log
java -cp .. gitlet.Main checkout master

# IMPORTANT: The 'reset' command requires the commit hash of the "Two files"
# commit from the R1 repository. You must copy the hash from the log output
# above and replace the placeholder text below.
java -cp .. gitlet.Main reset <hash_of_R1_"Two files"_commit>

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