#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'test34-merge-conflicts.in'.
#
# Test Description: Create two branches and merge other into master with a
#                   merge conflict.
#
# NOTE: This script uses `echo` to create files with specific content. This is
#       necessary to reproduce the merge conflict described in the test.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.
# To run it all at once, comment out the 'trap' line below.


# === From prelude1.inc ===
java gitlet.Main init

# === From setup1.inc ===
echo "wug.txt" > f.txt
echo "notwug.txt" > g.txt
java gitlet.Main add g.txt
java gitlet.Main add f.txt

# === From setup2.inc ===
java gitlet.Main commit "Two files"

# === From test34-merge-conflicts.in ===

# 1. Create the 'other' branch
java gitlet.Main branch other

# 2. Make commits on the 'master' branch
echo "wug2.txt" > h.txt
java gitlet.Main add h.txt
java gitlet.Main rm g.txt
echo "wug2.txt" > f.txt
java gitlet.Main add f.txt
java gitlet.Main commit "Add h.txt, remove g.txt, and change f.txt"

# 3. Make conflicting commits on the 'other' branch
java gitlet.Main checkout other
echo "notwug.txt" > f.txt
java gitlet.Main add f.txt
echo "wug3.txt" > k.txt
java gitlet.Main add k.txt
java gitlet.Main commit "Add k.txt and modify f.txt"

# 4. Switch back to master and attempt the merge
java gitlet.Main checkout master
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG
java gitlet.Main merge other

# 5. Check the state after the failed merge
java gitlet.Main log
java gitlet.Main status
trap - DEBUG


# Clean up the trap to exit cleanly
rm *.txt
rm -f -r .gitlet