#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'test43-bai-merge.in'.
#
# Test Description: Tests every non-error condition of merging mentioned in the
# spec, as adapted from a test case by Haoran Bai.
#
# NOTE: This script uses `echo` to create and modify files with specific content,
#       which is necessary to reproduce the intended test logic.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.
# To run it all at once, comment out the 'trap' line below.


# === Initial setup from test43-bai-merge.in ===
java gitlet.Main init

# Create and add initial files
echo "a" > A.txt
echo "b" > B.txt
echo "c" > C.txt
echo "d" > D.txt
echo "e" > E.txt
java gitlet.Main add A.txt
java gitlet.Main add B.txt
java gitlet.Main add C.txt
java gitlet.Main add D.txt
java gitlet.Main add E.txt
java gitlet.Main commit "msg1"

# 1. Create a new branch
java gitlet.Main branch branch1

# 2. Make changes on the 'master' branch
java gitlet.Main rm C.txt
java gitlet.Main rm D.txt
echo "not f" > F.txt
echo "not a" > A.txt
java gitlet.Main add A.txt
java gitlet.Main add F.txt
java gitlet.Main commit "msg2"

# 3. Make changes on 'branch1'
java gitlet.Main checkout branch1
java gitlet.Main rm C.txt
java gitlet.Main rm E.txt
echo "not b" > B.txt
echo "is g" > G.txt
java gitlet.Main add B.txt
java gitlet.Main add G.txt
java gitlet.Main commit "msg3"
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

# 4. Perform the merge
java gitlet.Main merge master


# Clean up the trap to exit cleanly
trap - DEBUG
# Clean up the trap to exit cleanly
rm *.txt
rm -f -r .gitlet